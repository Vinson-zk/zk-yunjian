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
 * @Title: ZKMemoryCacheManager.java 
 * @author Vinson 
 * @Package com.zk.cache.memory 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 9:12:37 AM 
 * @version V1.0   
*/
package com.zk.cache.memory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.zk.cache.ZKCache;
import com.zk.cache.ZKCacheManager;

/** 
* @ClassName: ZKMemoryCacheManager 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMemoryCacheManager implements ZKCacheManager<String> {

    private static final Map<String, Object> cMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <V> ZKCache<String, V> getCache(String name) {
        if (cMap.get(name) == null) {
            cMap.put(name, new ZKMemoryCache<String, V>(name));
        }
        return (ZKCache<String, V>) cMap.get(name);
    }

    /**
     * 
     * @param name
     * @param validTime
     *            ，有效时长，0-不过期，单位为秒
     * @return int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <V> ZKCache<String, V> getCache(String name, long validTime) {
        if (cMap.get(name) == null) {
            cMap.put(name, new ZKMemoryCache<String, V>(name));
        }
        return (ZKCache<String, V>) cMap.get(name);
    }

    @Override
    public Collection<String> getCacheNames() {
        return cMap.keySet();
    }

}
