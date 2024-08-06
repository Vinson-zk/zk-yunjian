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
 * @Title: ZKCacheTestConfig.java 
 * @author Vinson 
 * @Package com.zk.cache.helper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 9:21:45 AM 
 * @version V1.0   
*/
package com.zk.cache.helper;

import org.springframework.context.ApplicationContext;

import com.zk.cache.ZKCacheManager;
import com.zk.cache.memory.ZKMemoryCacheManager;
import com.zk.cache.mongo.ZKMongoCacheManager;
import com.zk.cache.support.shiro.ZKShiroCacheManager;
import com.zk.cache.support.spring.ZKSpringCacheManager;

/** 
* @ClassName: ZKCacheTestConfig 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCacheTestConfig {

    static ApplicationContext ctx;

    public static ApplicationContext getCtx() {
        if (ctx == null) {
            ctx = ZKCacheTestSpringbootMain.run(new String[] {});
        }
        return ctx;
    }

    // 测试 内存、mongo 缓存
    enum ZKCacheType {
        MEMORY, MONGO
    }

//    public static final ZKCacheType zkCacheType = ZKCacheType.MEMORY;
    public static final ZKCacheType zkCacheType = ZKCacheType.MONGO;

    public static ZKCacheManager<String> getCacheManager() {
        switch (zkCacheType) {
            case MONGO:
                return getCtx().getBean(ZKMongoCacheManager.class);
            default:
                return getCtx().getBean(ZKMemoryCacheManager.class);
        }
    }

    public static org.apache.shiro.cache.CacheManager getShiroCacheManager() {

        switch (zkCacheType) {
            case MONGO:
                return new ZKShiroCacheManager(getCtx().getBean(ZKMongoCacheManager.class));
            default:
                return new ZKShiroCacheManager(getCtx().getBean(ZKMemoryCacheManager.class));
        }
    }

    public static org.springframework.cache.CacheManager getSpringCacheManager() {

        switch (zkCacheType) {
            case MONGO:
                return new ZKSpringCacheManager(getCtx().getBean(ZKMongoCacheManager.class));
            default:
                return new ZKSpringCacheManager(getCtx().getBean(ZKMemoryCacheManager.class));
        }
    }

}
