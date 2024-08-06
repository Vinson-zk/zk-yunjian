/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKMongoCache.java 
 * @author Vinson 
 * @Package com.zk.cache.mongo 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 9:11:23 AM 
 * @version V1.0   
*/
package com.zk.cache.mongo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.types.Binary;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.zk.cache.ZKCache;
import com.zk.core.utils.ZKObjectUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.mongo.command.administration.ZKCreate;
import com.zk.mongo.command.administration.ZKCreateIndexes;
import com.zk.mongo.command.administration.ZKListCollections;
import com.zk.mongo.command.administration.ZKListIndexes;
import com.zk.mongo.command.queryAndWriteOperation.ZKDelete;
import com.zk.mongo.command.queryAndWriteOperation.ZKFind;
import com.zk.mongo.command.queryAndWriteOperation.ZKUpdate;
import com.zk.mongo.element.ZKDeleteElement;
import com.zk.mongo.element.ZKIndexElement;
import com.zk.mongo.element.ZKUpdateElement;
import com.zk.mongo.operator.ZKQueryOpt;
import com.zk.mongo.operator.ZKUpdateOpt;
import com.zk.mongo.utils.ZKMongoUtils;

/** 
* @ClassName: ZKMongoCache 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMongoCache<K, V> implements ZKCache<K, V> {

    private static Logger logger = LogManager.getLogger(ZKMongoCache.class);

    private static interface CONSTANT {
        /**
         * mongo 缓存集合的名称
         */
        public static final String collectionName = "c_cache_system_";

        /**
         * 过期时间索引名
         */
        public static final String indexExpireTimeName = "index_expire_time_";

        /**
         * 过期时间属性名
         */
        public static final String keyNameExpireTime = "expireTime";

        /**
         * 有效时间长属性名
         */
        public static final String keyNameValidTime = "validTime";

//      /**
//       * key 前缀
//       */
//      public static final String keyPrefix = "_key";

    }

    private static MongoTemplate mongoTemplate = null;

    private static void setMongoTemplate(MongoTemplate mongoTemplate) {
        if (ZKMongoCache.mongoTemplate == null) {
            ZKMongoCache.mongoTemplate = mongoTemplate;
            // 创建集合
            createCacheCollection(mongoTemplate);
        }
    }

    private static MongoTemplate getMongoTemplate() {
        if (ZKMongoCache.mongoTemplate == null) {
            logger.error("[>_<:20180716-2301-001] mongoTemplate is Null！");
        }
        return ZKMongoCache.mongoTemplate;
    }

    private static void createCacheCollection(MongoTemplate mongoTemplate) {
        // 判断集合是否存在
        ZKListCollections listCollections = new ZKListCollections();
        Document filterDoc = new Document();
        filterDoc.put("name", CONSTANT.collectionName);
        listCollections.setFilter(filterDoc);
        Document resDoc = getMongoTemplate().executeCommand(listCollections);
        if (ZKMongoUtils.getListResult(resDoc).size() > 0) {
            // 集合存在
            logger.info("[^_^:20180605-2220-001] 系统缓存集合：{}，已存在！", CONSTANT.collectionName);
        }
        else {
            // 集合不存在，创建集合
            ZKCreate create = new ZKCreate(CONSTANT.collectionName);
            getMongoTemplate().executeCommand(create);
            logger.info("[^_^:20180605-2220-002] 系统缓存集合：{}，创建成功", CONSTANT.collectionName);
        }

        // 判断过期索引是否存在
        ZKListIndexes listIndexes = new ZKListIndexes(CONSTANT.collectionName);
        resDoc = getMongoTemplate().executeCommand(listIndexes);
        List<Document> resDocs = ZKMongoUtils.getListResult(resDoc);
        boolean isExist = false;
        for (Document d : resDocs) {
            if (CONSTANT.indexExpireTimeName.equals(d.getString("name"))) {
                isExist = true;
                break;
            }
        }
        if (isExist) {
            // 过期索引已存在
            logger.info("[^_^:20180605-2220-003] 系统缓存集合：{}，中的过期索引:{}，已存在！", CONSTANT.collectionName,
                    CONSTANT.indexExpireTimeName);
        }
        else {
            // 创建过期索引
            ZKIndexElement indexElement = ZKCreateIndexes.IndexElement(CONSTANT.indexExpireTimeName,
                    CONSTANT.keyNameExpireTime);
            indexElement.setExpireAfterSeconds(0);
            ZKCreateIndexes createIndexes = new ZKCreateIndexes(CONSTANT.collectionName, indexElement);
            getMongoTemplate().executeCommand(createIndexes);
            logger.info("[^_^:20180605-2220-003] 系统缓存集合：{}，中的过期索引:{}，创建成功！", CONSTANT.collectionName,
                    CONSTANT.indexExpireTimeName);
        }
    }

    /**
     * 取一个缓存
     */
    public static <K, V> ZKMongoCache<K, V> getCahce(String cahceName) {
        if (mongoTemplate == null) {
            return null;
        }
        ZKFind find = new ZKFind(CONSTANT.collectionName);
        find.setFilter(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(cahceName));
        find.setProjectionIncludeKeys(ZKMongoUtils.autoIndexIdName, CONSTANT.keyNameValidTime,
                CONSTANT.keyNameExpireTime);
        Document doc = getMongoTemplate().executeCommand(find);
        doc = ZKMongoUtils.getOneResult(doc);
        if (doc != null) {
            ZKMongoCache<K, V> mc = new ZKMongoCache<K, V>(cahceName, doc.getLong(CONSTANT.keyNameValidTime),
                    doc.getDate(CONSTANT.keyNameExpireTime));
            if (mc.isValid()) {
                return mc;
            }
            else {
                // 缓存无效，删除缓存
                mc.clear();
            }
        }
        return null;
    }

    /**
     * 创建缓存
     */
    /**
     * @param cacheName
     */
    public static <K, V> ZKMongoCache<K, V> CreateMongoCache(MongoTemplate mongoTemplate, String cacheName) {
        return CreateMongoCache(mongoTemplate, cacheName, 0);
    }

    /**
     * @param cacheName
     * @param valideTime
     *            有效时长， 0 不过期，单位为毫秒
     */
    public static <K, V> ZKMongoCache<K, V> CreateMongoCache(MongoTemplate mongoTemplate, String cacheName,
            long validTime) {
        if (validTime == 0) {
            return CreateMongoCache(mongoTemplate, cacheName, validTime, null);
        }
        else {
            return CreateMongoCache(mongoTemplate, cacheName, validTime,
                    new Date(System.currentTimeMillis() + validTime));
        }
    }

    public static <K, V> ZKMongoCache<K, V> CreateMongoCache(MongoTemplate mongoTemplate, String cacheName,
            long validTime, Date expireTime) {
        setMongoTemplate(mongoTemplate);
        // 如果需要创建的缓存已存在，则使用已存在的缓存
        ZKMongoCache<K, V> mc = getCahce(cacheName);
        if (mc != null) {
            return mc;
        }
        return new ZKMongoCache<K, V>(cacheName, validTime, expireTime);
    }

    /////////////////////////////////////////////////////////
    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 有效时长 毫秒
     */
    private long validTime;

    /**
     * @param cacheName
     */
    protected ZKMongoCache(String cacheName) {
        this(cacheName, 0);
    }

    /**
     * @param cacheName
     * @param millisecond
     *            有效时长， 0 不过期，单位为毫秒
     */
    protected ZKMongoCache(String cacheName, long millisecond) {
        this(cacheName, millisecond, new Date(System.currentTimeMillis() + millisecond));
    }

    protected ZKMongoCache(String cacheName, long millisecond, Date expireTime) {
        this.cacheName = cacheName;
        this.validTime = millisecond;
        this.expireTime = expireTime;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(K key) {
        if (!this.isValid()) {
            return null;
        }
        ZKFind find = new ZKFind(CONSTANT.collectionName);
        find.setFilter(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(this.getName()));
        find.setProjectionIncludeKeys(key.toString());
        Document doc = getMongoTemplate().executeCommand(find);
        byte[] r = ZKMongoUtils.getByteByKey(doc, key);
        return r == null ? null : (V) ZKObjectUtils.unserialize(r);
    }

    @Override
    public boolean put(K key, V value) {
        if (!this.isValid()) {
            return false;
        }

        ZKUpdate update = new ZKUpdate(CONSTANT.collectionName);
        ZKUpdateElement updateElement = new ZKUpdateElement();
        updateElement.setQuery(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(this.getName()));
        // 不存在，新建
        updateElement.setUpsert(true);
        ZKUpdateOpt updateOpt = new ZKUpdateOpt();
        updateOpt.set(key.toString(), ZKObjectUtils.serialize(value));
        updateExpireTime(updateOpt);
        updateElement.setUpdate(updateOpt);
        update.addUpdates(updateElement);

        Document resDoc = getMongoTemplate().executeCommand(update);

        if (resDoc.getInteger("n").intValue() == 1) {
            logger.info("[^_^:20180605-2304-001] put cache success -> key={}; value={};", key, value.toString());
            return true;
        }
        else {
            logger.error("[>_<:20180605-2304-002] put cache fail -> key={}; value={};", key, value.toString());
            return false;
        }
    }

    /**
     * 判断缓存是否有效
     * 
     * @return
     */
    protected boolean isValid() {
        if (!ZKStringUtils.isEmpty(this.cacheName)
                && (this.expireTime == null || this.expireTime.getTime() > System.currentTimeMillis())) {
            return true;
        }
        logger.info("[^_^:20180606-1426-001] 缓存:{}, 已过期！", this.getName());
        return false;
    }

    /**
     * 更新过期时间的参数
     * 
     * @param updateOpt
     */
    private void updateExpireTime(ZKUpdateOpt updateOpt) {
        updateOpt.set(CONSTANT.keyNameValidTime, this.validTime);
        this.expireTime = null;
        if (this.validTime > 0) {
            this.expireTime = new Date(System.currentTimeMillis() + this.validTime);
            updateOpt.set(CONSTANT.keyNameExpireTime, this.expireTime);
        }
        else {
            updateOpt.unset(CONSTANT.keyNameExpireTime);
        }
    }

    @Override
    public boolean remove(K key) {
        if (!this.isValid()) {
            return true;
        }
        ZKUpdate update = new ZKUpdate(CONSTANT.collectionName);
        ZKUpdateElement updateElement = new ZKUpdateElement();
        updateElement.setQuery(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(this.getName()));
        // 不存在，不新增新建
        updateElement.setUpsert(false);
        ZKUpdateOpt updateOpt = new ZKUpdateOpt();
        updateOpt.unset(key.toString());
        updateExpireTime(updateOpt);
        updateElement.setUpdate(updateOpt);
        update.addUpdates(updateElement);

        Document resDoc = getMongoTemplate().executeCommand(update);
        if (resDoc.getDouble("ok").intValue() == 1) {
            logger.info("[^_^:20180605-2307-001] remove cache key success -> key={} ", key);
            return true;
        }
        else {
            logger.error("[>_<:20180605-2307-002] remove cache key fail -> key={} ", key);
            return false;
        }

    }

    @Override
    public void clear() {
        ZKDeleteElement de = new ZKDeleteElement(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(this.getName()));
        ZKDelete delete = new ZKDelete(CONSTANT.collectionName, de);
        Document resDoc = getMongoTemplate().executeCommand(delete);
        if (resDoc.getDouble("ok").intValue() == 1) {
            logger.info("[^_^:20180605-2307-001] clear cache {} success ", this.getName());
//          this.cacheName = null;
        }
        else {
            logger.error("[>_<:20180605-2307-002] clear cache {} fail ", this.getName());
        }

    }

    @Override
    public int size() {
        if (!this.isValid()) {
            return 0;
        }
        ZKFind find = new ZKFind(CONSTANT.collectionName);
        find.setFilter(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(this.getName()));
        find.setProjectionExcludeKeys(ZKMongoUtils.autoIndexIdName, CONSTANT.keyNameExpireTime,
                CONSTANT.keyNameValidTime, "_class");
        Document doc = getMongoTemplate().executeCommand(find);
        doc = ZKMongoUtils.getOneResult(doc);
        if (doc == null) {
            return 0;
        }
        return doc.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        if (!this.isValid()) {
            return null;
        }
        ZKFind find = new ZKFind(CONSTANT.collectionName);
        find.setFilter(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(this.getName()));
        find.setProjectionExcludeKeys(ZKMongoUtils.autoIndexIdName, CONSTANT.keyNameExpireTime,
                CONSTANT.keyNameValidTime, "_class");
        Document doc = getMongoTemplate().executeCommand(find);
        doc = ZKMongoUtils.getOneResult(doc);
        if (doc == null) {
            return null;
        }
        return (Set<K>) doc.keySet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<V> values() {
        if (!this.isValid()) {
            return null;
        }
        ZKFind find = new ZKFind(CONSTANT.collectionName);
        find.setFilter(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(this.getName()));
        find.setProjectionExcludeKeys(ZKMongoUtils.autoIndexIdName, CONSTANT.keyNameExpireTime,
                CONSTANT.keyNameValidTime, "_class");
        Document resDoc = getMongoTemplate().executeCommand(find);
        resDoc = ZKMongoUtils.getOneResult(resDoc);
        if (resDoc == null) {
            return null;
        }
        List<V> l = new ArrayList<>();
        for (Object b : resDoc.values()) {
            l.add((V) ZKObjectUtils.unserialize(((Binary) b).getData()));
        }
        return l;

    }

    public static Collection<String> getCacheIds(MongoTemplate mongoTemplate) {
        setMongoTemplate(mongoTemplate);
        ZKFind find = new ZKFind(CONSTANT.collectionName);
        find.setProjectionIncludeKeys(ZKMongoUtils.autoIndexIdName);
        Document resDoc = getMongoTemplate().executeCommand(find);
        List<Document> resDocs = ZKMongoUtils.getListResult(resDoc);
        if (resDoc == null) {
            return null;
        }
        Collection<String> cs = null;
        if (resDocs != null && resDocs.size() > 0) {
            cs = new ArrayList<>();
            for (Document m : resDocs) {
                cs.add(m.get("_id", String.class));
            }
        }
        return cs;
    }

    @Override
    public String getName() {
        return this.cacheName;
    }

    /**
     * 判断缓存中 key 是否存在
     * 
     * @param key
     * @return
     */
    @Override
    public boolean containsKey(K key) {
        if (!this.isValid()) {
            return false;
        }
        ZKFind find = new ZKFind(CONSTANT.collectionName);
        find.setFilter(ZKQueryOpt.where(ZKMongoUtils.autoIndexIdName).eq(this.getName()));
        find.setProjectionExcludeKeys(ZKMongoUtils.autoIndexIdName, CONSTANT.keyNameExpireTime,
                CONSTANT.keyNameValidTime, "_class");
        Document resDoc = getMongoTemplate().executeCommand(find);
        resDoc = ZKMongoUtils.getOneResult(resDoc);
        if (resDoc == null) {
            return false;
        }
        return resDoc.containsKey(key);

    }

}
