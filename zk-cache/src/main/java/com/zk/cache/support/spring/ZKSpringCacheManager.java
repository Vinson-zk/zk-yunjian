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
 * @Title: ZKSpringCacheManager.java 
 * @author Vinson 
 * @Package com.zk.cache.support.spring 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 9:10:43 AM 
 * @version V1.0   
*/
package com.zk.cache.support.spring;

import java.util.Collection;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.zk.cache.ZKCache;
import com.zk.cache.ZKCacheManager;

/** 
* @ClassName: ZKSpringCacheManager 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSpringCacheManager implements CacheManager {

    /**
     * 需要注入实现类
     */
    private ZKCacheManager<?> cacheManager;

    public ZKSpringCacheManager(ZKCacheManager<?> cacheManager) {
        this.cacheManager = cacheManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Cache getCache(String name) {
        return new ZKSpringCache((ZKCache<Object, Object>) cacheManager.getCache(name));
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheManager.getCacheNames();
    }

}
