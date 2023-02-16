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
* @Title: ZKRedisCacheManager.java 
* @author Vinson 
* @Package com.zk.cache.redis 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 11, 2021 4:12:34 PM 
* @version V1.0 
*/
package com.zk.cache.redis;

import java.util.Collection;

import com.zk.cache.ZKCache;
import com.zk.cache.ZKCacheManager;
import com.zk.core.redis.ZKJedisOperatorStringKey;

/**
 * @ClassName: ZKRedisCacheManager
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKRedisCacheManager implements ZKCacheManager<String> {

    ZKJedisOperatorStringKey jedisOperator;

    /**
     * @return jedisOperator sa
     */
    public ZKJedisOperatorStringKey getJedisOperator() {
        return jedisOperator;
    }

    /**
     * @param jedisOperator
     *            the jedisOperator to set
     */
    public void setJedisOperator(ZKJedisOperatorStringKey jedisOperator) {
        this.jedisOperator = jedisOperator;
    }

    @Override
    public <V> ZKCache<String, V> getCache(String name) {
        return this.getCache(name, 0);
    }

    /**
     * 
     * @param <K>
     * @param <V>
     * @param name
     * @param validTime
     *            单位毫秒
     * @return
     * @see com.zk.cache.ZKCacheManager#getCache(java.lang.String, long)
     */
    @Override
    public <V> ZKCache<String, V> getCache(String name, long validTime) {
        ZKCache<String, V> cache = new ZKRedisCache<>(name, validTime, this.getJedisOperator());
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return this.getJedisOperator().keys("*");
    }

}
