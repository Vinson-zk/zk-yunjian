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
 * @Title: ZKMongoCacheManager.java 
 * @author Vinson 
 * @Package com.zk.cache.mongo 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 9:11:47 AM 
 * @version V1.0   
*/
package com.zk.cache.mongo;

import java.util.Collection;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.zk.cache.ZKCache;
import com.zk.cache.ZKCacheManager;

/** 
* @ClassName: ZKMongoCacheManager 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMongoCacheManager implements ZKCacheManager<String> {

//  private Logger logger = LogManager.getLogger(getClass());

    /**
     * mongo 操作工具，需要注入
     */
    private MongoTemplate mongoTemplate;

    public ZKMongoCacheManager(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public <V> ZKCache<String, V> getCache(String name) {
        ZKCache<String, V> cache = ZKMongoCache.CreateMongoCache(mongoTemplate, name);
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return ZKMongoCache.getCacheIds(mongoTemplate);
    }

    @Override
    public <V> ZKCache<String, V> getCache(String name, long millisecond) {
        ZKCache<String, V> cache = ZKMongoCache.CreateMongoCache(mongoTemplate, name, millisecond);
        return cache;
    }

}
