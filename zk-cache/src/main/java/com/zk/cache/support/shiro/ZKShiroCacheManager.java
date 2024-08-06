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
 * @Title: ZKShiroCacheManager.java 
 * @author Vinson 
 * @Package com.zk.cache.support.shiro 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 9:07:58 AM 
 * @version V1.0   
*/
package com.zk.cache.support.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import com.zk.cache.ZKCache;
import com.zk.cache.ZKCacheManager;

/** 
* @ClassName: ZKShiroCacheManager 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKShiroCacheManager implements CacheManager {

    /**
     * 需要注入缓存管理的实现类
     */
    private ZKCacheManager<?> cacheManager;

    public ZKShiroCacheManager(ZKCacheManager<?> cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        @SuppressWarnings("unchecked")
        ZKCache<K, V> cc = (ZKCache<K, V>) cacheManager.getCache(name);
        Cache<K, V> cache = new ZKShiroCache<K, V>(cc);
        return cache;
    }

}
