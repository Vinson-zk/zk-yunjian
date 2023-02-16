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
 * @Title: ZKMemoryCache.java 
 * @author Vinson 
 * @Package com.zk.cache.memory 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 9:12:15 AM 
 * @version V1.0   
*/
package com.zk.cache.memory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.zk.cache.ZKCache;

/** 
* @ClassName: ZKMemoryCache 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMemoryCache<K, V> implements ZKCache<K, V> {

    private final Map<K, V> cMap = new HashMap<K, V>();

    /**
     * 缓存唯一ID, 缓存集名称
     */
    private String cacheName;

    public ZKMemoryCache(String name) {
        this.cacheName = name;
    }

    @Override
    public String getName() {
        return cacheName;
    }

    /**
     * @param key
     * @return
     * @throws CustormCacheException
     * @see com.zk.cache.CustomCache#get(java.lang.Object)
     */
    @Override
    public V get(K key) {
        return cMap.get(key);
    }

    /**
     * 
     * @param key
     * @param value
     * @return
     * @throws CustormCacheException
     * @see com.zk.cache.CustomCache#put(java.lang.Object,
     *      java.lang.Object)
     */
    @Override
    public boolean put(K key, V value) {
        cMap.put(key, value);
        return true;
    }

    /**
     * 
     * @param key
     * @return
     * @throws CustormCacheException
     * @see com.zk.cache.CustomCache#remove(java.lang.Object)
     */
    @Override
    public boolean remove(K key) {
        cMap.remove(key);
        return true;
    }

    /**
     * @throws CustormCacheException
     * @see com.zk.cache.CustomCache#clear()
     */
    @Override
    public void clear() {
        cMap.clear();
    }

    /**
     * 
     * @return
     * @see com.zk.cache.CustomCache#size()
     */
    @Override
    public int size() {
        return cMap.size();
    }

    /**
     * 
     * @return
     * @see com.zk.cache.CustomCache#keys()
     */
    @Override
    public Set<K> keys() {
        return cMap.keySet();
    }

    /**
     * 
     * @return
     * @see com.zk.cache.CustomCache#values()
     */
    @Override
    public Collection<V> values() {
        return cMap.values();
    }

    @Override
    public boolean containsKey(K key) {
        return cMap.containsKey(key);
    }

}
