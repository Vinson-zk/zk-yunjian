/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSecMongoAuthorizationInfoStore.java 
* @author Vinson 
* @Package com.zk.security.authz.support.mongo 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 3, 2021 8:32:03 AM 
* @version V1.0 
*/
package com.zk.security.authz.support.mongo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.zk.core.exception.ZKSystemException;
import com.zk.core.utils.ZKObjectUtils;
import com.zk.mongo.command.administration.ZKCreate;
import com.zk.mongo.command.administration.ZKCreateIndexes;
import com.zk.mongo.command.administration.ZKListCollections;
import com.zk.mongo.command.administration.ZKListIndexes;
import com.zk.mongo.command.queryAndWriteOperation.ZKDelete;
import com.zk.mongo.command.queryAndWriteOperation.ZKFind;
import com.zk.mongo.command.queryAndWriteOperation.ZKFindAndModify;
import com.zk.mongo.element.ZKDeleteElement;
import com.zk.mongo.element.ZKIndexElement;
import com.zk.mongo.operator.ZKQueryOpt;
import com.zk.mongo.operator.ZKUpdateOpt;
import com.zk.mongo.utils.ZKMongoUtils;
import com.zk.security.authz.ZKSecAuthorizationInfo;
import com.zk.security.authz.ZKSecAuthorizationInfoStore;
import com.zk.security.principal.ZKSecPrincipal;
import com.zk.security.ticket.ZKSecTicket;

/** 
* @ClassName: ZKSecMongoAuthorizationInfoStore 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecMongoAuthorizationInfoStore implements ZKSecAuthorizationInfoStore {

    protected Logger logger = LogManager.getLogger(this.getClass());

    // 默认有效时长，30 分钟，1800000 毫秒；单位：毫秒
    @Value("${zk.sec.authorization.info.store.valid.time:1800000}")
    private long validTime;

    /**
     * 集合集合名
     */
    public static final String ZKSecAuthorizationInfoStore_collection_name = "zk_sec_ZKSecAuthorizationInfoStore";

    /**
     * 过期时间索引名
     */
    private static final String indexExpireTimeName = "index_expire_time_";

    public static interface AttrKeyName {
        /**
         * 权限信息数据 key
         */
        public static final String AuthorizationInfo = "AuthorizationInfo";

        /**
         * 过期时间属性名
         */
        public static final String expireTime = "expireTime";

        public static final String lastTime = "lastTime";

    }

    public ZKSecMongoAuthorizationInfoStore(MongoTemplate mongoTemplate) {
        if (mongoTemplate == null) {
            // "权限信息存储初始化配置异常"
            throw ZKSystemException.as("zk.sec.000011", null);
        }
        this.mongoTemplate = mongoTemplate;
        createAuthorizationInfoStoreCollection();
    }

    /**
     * @return mongoTemplate sa
     */
    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    private void createAuthorizationInfoStoreCollection() {
        // 判断集合是否存在
        ZKListCollections ZKListCollections = new ZKListCollections();
        Document filterDoc = new Document();
        filterDoc.put("name", ZKSecAuthorizationInfoStore_collection_name);
        ZKListCollections.setFilter(filterDoc);
        Document resDoc = getMongoTemplate().executeCommand(ZKListCollections);
        if (ZKMongoUtils.getListResult(resDoc).size() > 0) {
            // 集合存在
            logger.info("[^_^:20210803-0839-001] 权限存储集合：{}，已存在！", ZKSecAuthorizationInfoStore_collection_name);
        }
        else {
            // 集合不存在，创建集合
            ZKCreate ZKCreate = new ZKCreate(ZKSecAuthorizationInfoStore_collection_name);
            getMongoTemplate().executeCommand(ZKCreate);
            logger.info("[^_^:20210803-0839-002] 权限存储集合：{}，创建成功", ZKSecAuthorizationInfoStore_collection_name);
        }

        // 判断过期索引是否存在
        ZKListIndexes ZKListIndexes = new ZKListIndexes(ZKSecAuthorizationInfoStore_collection_name);
        resDoc = getMongoTemplate().executeCommand(ZKListIndexes);
        List<Document> resDocs = ZKMongoUtils.getListResult(resDoc);
        boolean isExist = false;
        for (Document d : resDocs) {
            if (indexExpireTimeName.equals(d.getString("name"))) {
                isExist = true;
                break;
            }
        }
        if (isExist) {
            // 过期索引已存在
            logger.info("[^_^:20210803-0839-003] 权限存储集合：{}，中的过期索引:{}，已存在！", ZKSecAuthorizationInfoStore_collection_name,
                    indexExpireTimeName);
        }
        else {
            // 创建过期索引
            ZKIndexElement ZKIndexElement = ZKCreateIndexes.IndexElement(indexExpireTimeName,
                    ZKSecAuthorizationInfoStore_collection_name);
            ZKIndexElement.setExpireAfterSeconds(0);
            ZKCreateIndexes ZKCreateIndexes = new ZKCreateIndexes(ZKSecAuthorizationInfoStore_collection_name,
                    ZKIndexElement);
            getMongoTemplate().executeCommand(ZKCreateIndexes);
            logger.info("[^_^:20210803-0840-003] 权限存储集合：{}，中的过期索引:{}，创建成功！",
                    ZKSecAuthorizationInfoStore_collection_name,
                    indexExpireTimeName);
        }
    }

    /**
     * mongo 操作工具
     */
    private final MongoTemplate mongoTemplate;

    @Override
    public ZKSecAuthorizationInfo getZKSecAuthorizationInfo(ZKSecTicket ticket, String realmName) {
        if (ticket != null && ticket.getPrincipalCollection() != null) {
            ZKSecPrincipal<?> p = ticket.getPrincipalCollection().getPrimaryPrincipal(realmName);
            return this.getAttrOne(realmName + p.getPkId(), AttrKeyName.AuthorizationInfo);
        }
        return null;
    }

    @Override
    public void setZKSecAuthorizationInfo(ZKSecTicket ticket, String realmName,
            ZKSecAuthorizationInfo authorizationInfo) {
        if (ticket != null && ticket.getPrincipalCollection() != null) {
            ZKSecPrincipal<?> p = ticket.getPrincipalCollection().getPrimaryPrincipal(realmName);
            this.putAttr(realmName + p.getPkId(), AttrKeyName.AuthorizationInfo,
                    ZKObjectUtils.serialize(authorizationInfo));
        }
    }

    public boolean isValid(Serializable identification, Document doc) {
        if (doc != null) {
            doc = ZKMongoUtils.getOneResult(doc);
        }
        if (doc == null) {
            logger.info("[^_^:20210803-0913-001] ZKSecAuthorizationInfo is not found ");
            return false;
        }

        Date lastTime = doc.getDate(AttrKeyName.lastTime);
        if (this.validTime > 0 && lastTime != null
                && ((lastTime.getTime() + this.validTime) < System.currentTimeMillis())) {
            // 过期
            logger.info("[^_^:20210803-0913-002] ZKSecAuthorizationInfo is expired ");
            this.drop(identification);
            return false;
        }
        return true;
    }

    private int drop(Serializable identification) {
        if (identification == null) {
            return 0;
        }
        ZKDelete delete = new ZKDelete(ZKSecAuthorizationInfoStore_collection_name);
        ZKDeleteElement de = new ZKDeleteElement(
                ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(identification.toString()));
        delete.addDeletes(de);
        Document resDoc = getMongoTemplate().executeCommand(delete);
        if (resDoc.getInteger("n").intValue() == 1) {
            logger.info("[^_^:20210803-0914-001] 权限信息:{} 销毁成功", identification);
            return 1;
        }
        else {
            logger.error("[^_^:20210803-0914-002] 权限信息:{} 销毁失败", identification);
        }
        return 0;
    }

    /**
     * 查询一个属性或多个属性的
     * 
     * @param attrKeyName
     * @return 返回结果的 Document
     */
    private Document getAttr(Serializable identification, String... keyNames) {
        ZKFind find = new ZKFind(ZKSecAuthorizationInfoStore_collection_name);
        find.setFilter(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(identification.toString()));
        find.setProjectionIncludeKeys(keyNames);
        Document doc = getMongoTemplate().executeCommand(find);
        if (doc == null) {
            logger.error("[>_<:20210803-0915-001] 权限信息:{} is not found ", identification);
            return null;
        }

        if (!this.isValid(identification, doc)) {
            return null;
        }

        return ZKMongoUtils.getOneResult(doc);
    }

    @SuppressWarnings("unchecked")
    private <V> V getAttrOne(Serializable identification, String keyName) {
        Document doc = this.getAttr(identification, keyName);
        if (doc != null && doc.get(keyName) != null) {
            byte[] r = doc.get(keyName, Binary.class).getData();
            return r == null ? null : (V) ZKObjectUtils.unserialize(r);
        }
        return null;
    }

    /**
     * @param identification
     *            ID
     * @param keyName
     * @param keyValue
     *            不为null 修改，为null 删除 keyName
     * @return
     */
    private <V> boolean putAttr(Serializable identification, String keyName, V keyValue) {

        ZKUpdateOpt updateOpt = new ZKUpdateOpt();
        // 设置过期时间
        updateOpt.set(AttrKeyName.expireTime, new Date(System.currentTimeMillis() + validTime));
        ZKFindAndModify ZKFindAndModify = new ZKFindAndModify(ZKSecAuthorizationInfoStore_collection_name);
        // 身份的权限信息不存在时 新增
        ZKFindAndModify.setUpsert(true);
        ZKFindAndModify.setQuery(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(identification.toString()));
        if (keyValue == null) {
            updateOpt.unset(keyName);
        }
        else {
            updateOpt.set(keyName, keyValue);
        }
        ZKFindAndModify.setUpdate(updateOpt);
        Document resDoc = getMongoTemplate().executeCommand(ZKFindAndModify);
        resDoc = resDoc.get("lastErrorObject", Document.class);
        if (resDoc.getInteger("n").intValue() == 1) {
            logger.info("[^_^:20210803-0927-001] set AuthorizationInfo {} success", identification);
        }
        else {
            logger.error("[>_<:20210803-0927-002] set AuthorizationInfo {} fail", identification);
            return false;
        }
        return true;
    }

}
