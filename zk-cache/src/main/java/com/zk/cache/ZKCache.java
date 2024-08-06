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
 * @Title: ZKCache.java 
 * @author Vinson 
 * @Package com.zk.cache 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 8:57:33 AM 
 * @version V1.0   
*/
package com.zk.cache;

import java.util.Collection;
import java.util.Set;

/** 
* @ClassName: ZKCache 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKCache<K, V> {

    public String getName();

    /**
     * 根据缓存 KEY 取缓存值，缓存不存在时，返回null
     * 
     * @param key
     * @return
     * @throws CustormCacheException
     */
    public V get(K key);

    /**
     * 设置缓存，成功返回true, 失败返回 false
     * 
     * @param key
     * @param value
     * @return
     * @throws CustormCacheException
     */
    public boolean put(K key, V value);

    /**
     * 移除缓存
     * 
     * @param key
     * @return
     * @throws CustormCacheException
     */
    public boolean remove(K key);

    /**
     * 清空缓存
     * 
     * @throws CustormCacheException
     */
    public void clear();

    /**
     * 取缓存大小
     * 
     * @return
     */
    public int size();

    /**
     * 取所有缓存 key
     * 
     * @return
     */
    public Set<K> keys();

    /**
     * 取所有缓存 值
     * 
     * @return
     */
    public Collection<V> values();

    /**
     * 判断缓存中 key 是否存在
     * 
     * @param key
     * @return
     */
    public boolean containsKey(K key);

}
