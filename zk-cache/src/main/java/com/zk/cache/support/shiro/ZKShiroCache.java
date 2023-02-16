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
 * @Title: ZKShiroCache.java 
 * @author Vinson 
 * @Package com.zk.cache.support.shiro 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 9:07:35 AM 
 * @version V1.0   
*/
package com.zk.cache.support.shiro;

import java.util.Collection;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import com.zk.cache.ZKCache;

/** 
* @ClassName: ZKShiroCache 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKShiroCache<K, V> implements Cache<K, V> {

    ZKCache<K, V> cache;

    public ZKShiroCache(ZKCache<K, V> cache) {
        this.cache = cache;
    }

    @Override
    public V get(K key) throws CacheException {
        return cache.get(key);
    }

    @Override
    public V put(K key, V value) throws CacheException {
        cache.put(key, value);
        return value;
    }

    @Override
    public V remove(K key) throws CacheException {
        V v = cache.get(key);
        cache.remove(key);
        return v;
    }

    @Override
    public void clear() throws CacheException {
        cache.clear();
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public Set<K> keys() {
        return cache.keys();
    }

    @Override
    public Collection<V> values() {
        return cache.values();
    }

}
