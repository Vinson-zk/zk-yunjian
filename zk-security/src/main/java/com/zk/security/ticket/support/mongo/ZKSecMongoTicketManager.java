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
* @Title: ZKSecMongoTicketManager.java 
* @author Vinson 
* @Package com.zk.security.ticket.support.mongo 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 23, 2021 9:53:23 PM 
* @version V1.0 
*/
package com.zk.security.ticket.support.mongo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.zk.core.exception.ZKSystemException;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKObjectUtils;
import com.zk.mongo.command.administration.ZKCreate;
import com.zk.mongo.command.administration.ZKCreateIndexes;
import com.zk.mongo.command.administration.ZKListCollections;
import com.zk.mongo.command.administration.ZKListIndexes;
import com.zk.mongo.command.queryAndWriteOperation.ZKDelete;
import com.zk.mongo.command.queryAndWriteOperation.ZKFind;
import com.zk.mongo.command.queryAndWriteOperation.ZKFindAndModify;
import com.zk.mongo.command.queryAndWriteOperation.ZKInsert;
import com.zk.mongo.element.ZKDeleteElement;
import com.zk.mongo.element.ZKIndexElement;
import com.zk.mongo.operator.ZKQueryOpt;
import com.zk.mongo.operator.ZKUpdateOpt;
import com.zk.mongo.utils.ZKMongoUtils;
import com.zk.security.principal.ZKSecPrincipal;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.ticket.ZKSecAbstractTicketManager;
import com.zk.security.ticket.ZKSecDelegatingTicker;
import com.zk.security.ticket.ZKSecProxyTickerManager;
import com.zk.security.ticket.ZKSecTicket;

/** 
* @ClassName: ZKSecMongoTicketManager 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecMongoTicketManager extends ZKSecAbstractTicketManager implements ZKSecProxyTickerManager {

    protected Logger logger = LogManager.getLogger(this.getClass());

    // 默认有效时长，900000 毫秒；单位：毫秒
    @Value("${zk.sec.ticket.default.valid.time:900000}")
    private long defaultValidTime;

    /**
     * 集合集合名
     */
    public static final String ZKSecTicket_collection_name = "zk_sec_ZKSecTicket";

    /**
     * 过期时间索引名
     */
    private static final String indexExpireTimeName = "index_expire_time_";

    public static interface AttrKeyName {
        /**
         * 令牌 put 信息时 key 的前缀，防止与固定属性 key 重复
         */
        public static final String valueKey_prefix = "tk_v_keyPerfix";

        /**
         * 过期时间属性名
         */
        public static final String expireTime = "expireTime";

        public static final String principalCollection = "principalCollection";

        public static final String type = "type";

        public static final String securityType = "securityType";

        public static final String status = "status";

        public static final String validTime = "validTime";

        public static final String lastTime = "lastTime";

        /**
         * 身份集合放入 mongo 中时做一些处理，把主要的属性抽出来做个数组，以便查询。 其他属性还是以二进制存放与读取。
         */
        public static final String pcKey_PC = "pcKeyPC";

        public static final String pcKey_PS = "pcKeyPS";

        public static final String pKey_pkID = "pKeypkID";

        public static final String pKey_Type = "pKeyType";

//        public static final String pKey_CompanyCode = "pKeyCompanyCode";

        public static final String pKey_AuthorizationInfo = "pKeyAuthorizationInfo";
    }

    public ZKSecMongoTicketManager(MongoTemplate mongoTemplate) {
        if (mongoTemplate == null) {
            throw ZKSystemException.as("zk.sec.000007", null);
        }
        this.mongoTemplate = mongoTemplate;
        createTicketCollection();
    }

    /**
     * @return mongoTemplate sa
     */
    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    private void createTicketCollection() {
        // 判断集合是否存在
        ZKListCollections ZKListCollections = new ZKListCollections();
        Document filterDoc = new Document();
        filterDoc.put("name", ZKSecTicket_collection_name);
        ZKListCollections.setFilter(filterDoc);
        Document resDoc = getMongoTemplate().executeCommand(ZKListCollections);
        if (ZKMongoUtils.getListResult(resDoc).size() > 0) {
            // 集合存在
            logger.info("[^_^:20180606-1613-001] 令牌集合：{}，已存在！", ZKSecTicket_collection_name);
        }
        else {
            // 集合不存在，创建集合
            ZKCreate ZKCreate = new ZKCreate(ZKSecTicket_collection_name);
            getMongoTemplate().executeCommand(ZKCreate);
            logger.info("[^_^:20180606-1613-002] 令牌集合：{}，创建成功", ZKSecTicket_collection_name);
        }

        // 判断过期索引是否存在
        ZKListIndexes ZKListIndexes = new ZKListIndexes(ZKSecTicket_collection_name);
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
            logger.info("[^_^:20180606-1613-003] 令牌集合：{}，中的过期索引:{}，已存在！", ZKSecTicket_collection_name,
                    indexExpireTimeName);
        }
        else {
            // 创建过期索引
            ZKIndexElement ZKIndexElement = ZKCreateIndexes.IndexElement(indexExpireTimeName,
                    ZKSecTicket_collection_name);
            ZKIndexElement.setExpireAfterSeconds(0);
            ZKCreateIndexes ZKCreateIndexes = new ZKCreateIndexes(ZKSecTicket_collection_name, ZKIndexElement);
            getMongoTemplate().executeCommand(ZKCreateIndexes);
            logger.info("[^_^:20180606-1613-003] 令牌集合：{}，中的过期索引:{}，创建成功！", ZKSecTicket_collection_name,
                    indexExpireTimeName);
        }
    }

    /**
     * mongo 操作工具
     */
    private final MongoTemplate mongoTemplate;

    @Override
    public ZKSecTicket createTicket(Serializable identification) {
        return createTicket(identification, defaultValidTime);
    }

    @Override
    public ZKSecTicket createTicket(Serializable identification, long validTime) {
        return this.createTicket(identification, validTime, ZKSecTicket.KeyType.General, -1,
                ZKSecTicket.KeyStatus.Start);
    }

    @Override
    public ZKSecTicket createSecTicket(Serializable identification) {
        return createSecTicket(identification, ZKSecTicket.KeySecurityType.User);
    }

    @Override
    public ZKSecTicket createSecTicket(Serializable identification, int securityType) {
        return createSecTicket(identification, securityType, defaultValidTime);
    }

    @Override
    public ZKSecTicket createSecTicket(Serializable identification, int securityType, long validTime) {
        return this.createTicket(identification, validTime, ZKSecTicket.KeyType.Security, securityType,
                ZKSecTicket.KeyStatus.Start);
    }

    //
    private ZKSecTicket createTicket(Serializable identification, long validTime, int type, int securityType,
            int status) {
        ZKSecTicket t = new ZKSecDelegatingTicker(this, identification);

        Document resDoc;

        // 创建令牌到令牌集合中，不存在创建，存在，不创建，并抛出异常
        ZKInsert insert = new ZKInsert(ZKSecTicket_collection_name);
        Document doc = new Document();
        doc.put(ZKMongoUtils.autoIndexIdName, t.getTkId().toString());
        doc.put(AttrKeyName.expireTime, new Date(System.currentTimeMillis() + validTime));
        doc.put(AttrKeyName.lastTime, new Date());
        doc.put(AttrKeyName.status, status);
        doc.put(AttrKeyName.type, type);
        doc.put(AttrKeyName.securityType, securityType);
        doc.put(AttrKeyName.validTime, validTime);
        insert.addDoc(doc);
        resDoc = getMongoTemplate().executeCommand(insert);
        if (resDoc.getInteger("n").intValue() == 1) {
            logger.info("[^_^:20180606-1654-001] ZKCreate ZKSecTicket success -> {}", identification);
        }
        else {
            logger.error("[>_<:20180606-1654-002] ZKCreate ZKSecTicket fail -> {} - {}", identification,
                    ZKJsonUtils.toJsonStr(resDoc));
            throw ZKSystemException.as("zk.sec.000019", "ZKCreate ZKSecTicket fail");
        }

//      // 创建令牌到令牌集合中，不存在创建，存在，不修改
//      ZKFindAndModify ZKFindAndModify = new ZKFindAndModify(ZKSecTicket_collection_name);
//      // 令牌不存在，新增
//      ZKFindAndModify.setUpsert(true);
//      ZKFindAndModify.setQuery(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(this.getTkId().toString()));
//      ZKUpdateOpt ZKUpdateOpt = new ZKUpdateOpt();
//      // 仅在插入时有效ZKSecTicket.KeyStatus.Start
//      ZKUpdateOpt.setOnInsert(keyName_lastTime, new Date());
//      ZKUpdateOpt.setOnInsert(keyName_status, status);
//      ZKUpdateOpt.setOnInsert(keyName_type, type);
//      ZKUpdateOpt.setOnInsert(keyName_validTime, validTime);
//      if(validTime > 0){
//          ZKUpdateOpt.setOnInsert(keyName_expireTime, new Date(System.currentTimeMillis() + validTime));
//      }
////        ZKUpdateOpt.setOnInsert(keyName_ownerId, value);
//      ZKFindAndModify.setUpdate(ZKUpdateOpt);
//      
//      resDoc = getMongoTemplate().executeCommand(ZKFindAndModify);
//      resDoc = resDoc.get("lastErrorObject", Document.class);
//      if(resDoc.getInteger("n").intValue() == 1){
//          logger.info("[^_^:20180606-1654-001] ZKCreate ZKSecTicket success -> {}", tkId); 
//      }else{
//          logger.error("[>_<:20180606-1654-002] ZKCreate ZKSecTicket fail -> {}", tkId); 
//          throw new ZKSecTicketException("zk.sec.000019", "生成信息异常，请联系管理员", null, "ZKCreate ZKSecTicket fail");
//      }
        return t;
    }

    @Override
    public int dropTicketByType(int type) {
        ZKDelete delete = new ZKDelete(ZKSecTicket_collection_name);
        delete.addDeletes(new ZKDeleteElement(ZKQueryOpt.where(AttrKeyName.type).eq(type)));
        Document doc = this.getMongoTemplate().executeCommand(delete);
        // {"n":6,"ok":1.0}
        return doc.getInteger("n", 0);
    }

    @Override
    public int dropAll() {
        ZKDelete delete = new ZKDelete(ZKSecTicket_collection_name);
        // 条件传空，匹配所有的
        delete.addDeletes(new ZKDeleteElement(new ZKQueryOpt()));
        Document doc = this.getMongoTemplate().executeCommand(delete);
//      System.out.println("=== " + JsonUtils.writeObjectJson(doc));
        // {"n":6,"ok":1.0}
        return doc.getInteger("n", 0);
    }

    @Override
    public void validateTickets() {
        // 待补充
    }

    @Override
    public ZKSecTicket getTicket(Serializable identification) {
        if (this.isValid(identification)) {
            return new ZKSecDelegatingTicker(this, identification);
        }
        return null;
    }

    @Override
    public int dropTicket(ZKSecTicket ticket) {
        return this.dropTicket(ticket.getTkId());
    }

    @Override
    public int dropTicket(Serializable identification) {
        return this.drop(identification);
    }

    @Override
    public <ID> List<ZKSecTicket> findTickeByPrincipal(ZKSecPrincipal<ID> principal) {
        return this.findTickeByPrincipal(principal, null);
    }

    @Override
    public <ID> List<ZKSecTicket> findTickeByPrincipal(ZKSecPrincipal<ID> principal, List<ZKSecTicket> filterTickets) {
        List<ZKSecTicket> tks = new ArrayList<>();
        ZKFind find = new ZKFind(ZKSecTicket_collection_name);
        List<ZKQueryOpt> queryOpts = new ArrayList<>();
        queryOpts.add(ZKQueryOpt.where(AttrKeyName.type).eq(ZKSecTicket.KeyType.Security));
        queryOpts.add(ZKQueryOpt.where(AttrKeyName.principalCollection + "." + AttrKeyName.pcKey_PS)
                .elemMatch(ZKQueryOpt.where(AttrKeyName.pKey_pkID).eq(principal.getPkId())));
        queryOpts.add(ZKQueryOpt.where(AttrKeyName.principalCollection + "." + AttrKeyName.pcKey_PS)
                .elemMatch(ZKQueryOpt.where(AttrKeyName.pKey_Type).eq(principal.getType())));
        if (filterTickets != null && filterTickets.size() > 0) {
            for (ZKSecTicket tk : filterTickets) {
                queryOpts.add(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).ne(tk.getTkId()));
            }
        }
        find.setFilter(ZKQueryOpt.and(queryOpts));
        find.setProjectionIncludeKeys(ZKMongoUtils.autoIndexIdName);
//      find.setProjectionIncludeKeys(AttrKeyName.principalCollection + "." + MongoTicket.pcKey_PS + "." + MongoTicket.pKey_pkID);
        Document doc = getMongoTemplate().executeCommand(find);
        List<Document> tkIds = ZKMongoUtils.getListResult(doc);
        for (Document o : tkIds) {
            tks.add(new ZKSecDelegatingTicker(this, o.getString(ZKMongoUtils.autoIndexIdName)));
        }

//      System.out.println("=================================");
//      System.out.println("=== " + JsonUtils.writeObjectJson(find));
//      System.out.println("=== " + JsonUtils.writeObjectJson(doc));
//      System.out.println("=================================");

        return tks;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <ID> ZKSecPrincipalCollection<ID> getPrincipalCollection(Serializable identification) {
        Document doc = getAttr(identification, AttrKeyName.principalCollection);
        if (doc != null && doc.get(AttrKeyName.principalCollection) != null) {
            doc = doc.get(AttrKeyName.principalCollection, Document.class);
            if(doc != null && doc.get(AttrKeyName.pcKey_PC) != null) {
                byte[] r = doc.get(AttrKeyName.pcKey_PC, Binary.class).getData();
                return (ZKSecPrincipalCollection<ID>) ZKObjectUtils.unserialize(r);
            }
        }
        return null;
    }

    @Override
    public <ID> void setPrincipalCollection(Serializable identification,
            ZKSecPrincipalCollection<ID> principalCollection) {
        Document pcDoc = new Document();
        pcDoc.put(AttrKeyName.pcKey_PC, ZKObjectUtils.serialize(principalCollection));
        pcDoc.put(AttrKeyName.pcKey_PS, makePsDoc(principalCollection.asSet()));
        putAttr(identification, AttrKeyName.principalCollection, pcDoc);
    }

    @Override
    public int getType(Serializable identification) {
        Document doc = getAttr(identification, AttrKeyName.type);
        if (doc != null) {
            return doc.getInteger(AttrKeyName.type, ZKSecTicket.KeyType.General);
        }
        return ZKSecTicket.KeyType.General;
    }

    @Override
    public int getSecurityType(Serializable identification) {
        Document doc = getAttr(identification, AttrKeyName.securityType);
        if (doc != null) {
            return doc.getInteger(AttrKeyName.securityType, -1);
        }
        return -1;
    }

    @Override
    public int getStatus(Serializable identification) {
        Document doc = getAttr(identification, AttrKeyName.status);
        if (doc != null) {
            return doc.getInteger(AttrKeyName.status, ZKSecTicket.KeyStatus.Stop);
        }
        return ZKSecTicket.KeyStatus.Stop;
    }

    @Override
    public void start(Serializable identification) {
        this.putAttr(identification, AttrKeyName.status, ZKSecTicket.KeyStatus.Start);
    }

    @Override
    public void stop(Serializable identification) {
        this.putAttr(identification, AttrKeyName.status, ZKSecTicket.KeyStatus.Stop);
    }

    @Override
    public Date getLastTime(Serializable identification) {
        Document doc = getAttr(identification, AttrKeyName.lastTime);
        if (doc != null) {
            return doc.getDate(AttrKeyName.lastTime);
        }
        return null;
    }

    @Override
    public void updateLastTime(Serializable identification) {
        if (!this.isValid(identification)) {
            return;
        }
        ZKUpdateOpt ZKUpdateOpt = new ZKUpdateOpt();
        long validTime = getValidTime(identification);
        this.makeUpdateLastTime(ZKUpdateOpt, validTime);
        // 创建令牌到令牌集合中
        ZKFindAndModify ZKFindAndModify = new ZKFindAndModify(ZKSecTicket_collection_name);
        // 令牌不存在，不新增
        ZKFindAndModify.setUpsert(false);
        ZKFindAndModify.setQuery(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(identification.toString()));
        ZKFindAndModify.setUpdate(ZKUpdateOpt);
        Document resDoc = getMongoTemplate().executeCommand(ZKFindAndModify);
        resDoc = resDoc.get("lastErrorObject", Document.class);
        if (resDoc.getInteger("n").intValue() == 1) {
            logger.info("[^_^:20180606-1707-001] update ZKSecTicket lastTime success -> {}", identification);
        }
        else {
            logger.error("[>_<:20180606-1707-002] update ZKSecTicket lastTime fail -> {}", identification);
        }
    }

    @Override
    public long getValidTime(Serializable identification) {
        Document doc = this.getAttr(identification, AttrKeyName.validTime);
        if (doc != null) {
            return doc.getLong(AttrKeyName.validTime);
        }
        return 0;
    }

    @Override
    public void setValidTime(Serializable identification, long validTime) {
        putAttr(identification, AttrKeyName.validTime, validTime);
    }

    @Override
    public boolean isValid(Serializable identification) {
        if (identification == null) {
            logger.info("[^_^:20180606-1910-001] ZKSecTicket is dropped ");
            return false;
        }

        ZKFind find = new ZKFind(ZKSecTicket_collection_name);
        find.setFilter(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(identification.toString()));
        find.setProjectionIncludeKeys(AttrKeyName.lastTime, AttrKeyName.validTime);
        Document doc = getMongoTemplate().executeCommand(find);
        if (doc != null) {
            doc = ZKMongoUtils.getOneResult(doc);
        }
        if (doc == null) {
            logger.info("[^_^:20180606-1910-002] ZKSecTicket is not found ");
            return false;
        }

        long validTime = doc.getLong(AttrKeyName.validTime);
        Date lastTime = doc.getDate(AttrKeyName.lastTime);
        if (validTime > 0 && lastTime != null && ((lastTime.getTime() + validTime) < System.currentTimeMillis())) {
            // 令牌过期
            logger.info("[^_^:20180606-1910-003] ZKSecTicket is expired ");
            this.drop(identification);
            return false;
        }
        return true;
    }

    @Override
    public int drop(Serializable identification) {
        if (identification == null) {
            return 0;
        }
        ZKDelete delete = new ZKDelete(ZKSecTicket_collection_name);
        ZKDeleteElement de = new ZKDeleteElement(
                ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(identification.toString()));
        delete.addDeletes(de);
        Document resDoc = getMongoTemplate().executeCommand(delete);
        if (resDoc.getInteger("n").intValue() == 1) {
            logger.info("[^_^:20180526-2305-001] 令牌:{} 销毁成功", identification);
            return 1;
        }
        else {
            logger.error("[^_^:20180526-2305-002] 令牌:{} 销毁失败", identification);
        }
        return 0;
    }

    @Override
    public <V> boolean put(Serializable identification, String key, V value) {
        return this.putAttr(identification, AttrKeyName.valueKey_prefix + key, ZKObjectUtils.serialize(value));
    }

    @Override
    public <V> V get(Serializable identification, String key) {
        return this.getAttrOne(identification, AttrKeyName.valueKey_prefix + key);
    }

    @Override
    public boolean remove(Serializable identification, String key) {
        return this.putAttr(identification, AttrKeyName.valueKey_prefix + key, null);
    }

    /************************************************************************/

    /**
     * 查询一个属性或多个属性的
     * 
     * @param identification
     *            令牌ID
     * @param keyNames
     * @return 返回结果的 Document
     */
    private Document getAttr(Serializable identification, String... keyNames) {
        if (!this.isValid(identification)) {
            return null;
        }
        ZKFind find = new ZKFind(ZKSecTicket_collection_name);
        find.setFilter(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(identification.toString()));
        find.setProjectionIncludeKeys(keyNames);
        Document doc = getMongoTemplate().executeCommand(find);
        if (doc == null) {
            logger.error("[>_<:20180606-1749-001] ZKSecTicket:{} is not found ", identification);
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
     *            令牌ID
     * @param keyName
     * @param keyValue
     *            不为null 修改，为null 删除 keyName
     * @return
     */
    private <V> boolean putAttr(Serializable identification, String keyName, V keyValue) {
        if (!this.isValid(identification)) {
            return false;
        }
        ZKUpdateOpt updateOpt = new ZKUpdateOpt();
        long validTime = this.getValidTime(identification);
        this.makeUpdateLastTime(updateOpt, validTime);
        // 创建令牌到令牌集合中
        ZKFindAndModify ZKFindAndModify = new ZKFindAndModify(ZKSecTicket_collection_name);
        // 令牌不存在，不新增
        ZKFindAndModify.setUpsert(false);
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
            logger.info("[^_^:20180606-1751-001] set ZKSecTicket {} key-value success -> key:{}", identification,
                    keyName);
        }
        else {
            logger.error("[>_<:20180606-1751-002] set ZKSecTicket {} key-value fail -> key:{}", identification,
                    keyName);
            return false;
        }
        return true;
    }

    private void makeUpdateLastTime(ZKUpdateOpt updateOpt, long validTime) {
        updateOpt.set(AttrKeyName.lastTime, new Date(System.currentTimeMillis()));
        if (validTime > 0) {
            updateOpt.set(AttrKeyName.expireTime, new Date(System.currentTimeMillis() + validTime));
        }
        else {
            updateOpt.unset(AttrKeyName.expireTime);
        }
    }

    private <ID> List<Document> makePsDoc(Set<ZKSecPrincipal<ID>> principals) {
        List<Document> pDocs = new ArrayList<>();
        Document pDoc = null;
        for (ZKSecPrincipal<?> p : principals) {
            pDoc = new Document();
            pDoc.put(AttrKeyName.pKey_pkID, p.getPkId());
            pDoc.put(AttrKeyName.pKey_Type, p.getType());
//            pDoc.put(AttrKeyName.pKey_CompanyCode, p.getCompanyCode());
//          pDoc.put(pKey_ZKSecTicketId, p.getZKSecTicketId());
            pDocs.add(pDoc);
        }
        return pDocs;
    }

    /**
     * 
     * @throws Exception
     * @see com.zk.security.common.ZKSecDestroyable#destroy()
     */
    @Override
    public void destroy() {

    }

}
