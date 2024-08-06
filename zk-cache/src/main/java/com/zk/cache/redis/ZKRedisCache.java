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
* @Title: ZKRedisCache.java 
* @author Vinson 
* @Package com.zk.cache.redis 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 11, 2021 4:12:07 PM 
* @version V1.0 
*/
package com.zk.cache.redis;

import java.util.Collection;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.cache.ZKCache;
import com.zk.core.redis.ZKJedisOperatorStringKey;

/**
 * @ClassName: ZKRedisCache
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKRedisCache<V> implements ZKCache<String, V> {

    private static Logger log = LogManager.getLogger(ZKRedisCache.class);

//    public static final String redisCacheNamePrefix = "zk_cachePrefix_";

    ZKJedisOperatorStringKey jedisOperatorStringKey;

    /**
     * 缓存名称
     */
    private String cacheName;

    /**
     * 有效时长 毫秒
     */
    private long validTime;

    public ZKRedisCache() {
    }

    public ZKRedisCache(String cacheName, long validTime, ZKJedisOperatorStringKey jedisOperatorStringKey) {
        this.cacheName = cacheName;
        this.validTime = validTime;
        this.jedisOperatorStringKey = jedisOperatorStringKey;
    }

    public ZKJedisOperatorStringKey getJedisOperator() {
        if (jedisOperatorStringKey == null) {
            log.error("[>_<:20210812-1745-001] redis 缓存，jedisOperatorStringKey not null.");
        }
        return jedisOperatorStringKey;
    }

    public void setJedisOperator(ZKJedisOperatorStringKey jedisOperatorStringKey) {
        this.jedisOperatorStringKey = jedisOperatorStringKey;
    }

    public long getValidTime() {
        return validTime;
    }

    public int getValidTimeInt() {
        return (int) validTime / 1000;
    }

    public void setValidTime(long validTime) {
        this.validTime = validTime;
    }

    @Override
    public String getName() {
        return this.cacheName;
    }

    @Override
    public V get(String key) {
        return this.getJedisOperator().hget(this.getName(), key.toString());
    }

    @Override
    public boolean put(String key, V value) {
        Long res = this.getJedisOperator().hset(this.getName(), key.toString(), value,
                this.getValidTimeInt());
        return res != null ? true : false;
    }

    @Override
    public boolean remove(String key) {
        Long res = this.getJedisOperator().hdel(this.getName(), key.toString());
        return res != null ? true : false;
    }

    @Override
    public void clear() {
        this.getJedisOperator().del(this.getName());
    }

    @Override
    public int size() {
        return this.getJedisOperator().hget(this.getName()).size();
    }

    @Override
    public Set<String> keys() {
        return this.getJedisOperator().hkeys(this.getName());
    }

    @Override
    public Collection<V> values() {
        return this.getJedisOperator().hValues(this.getName());
    }

    @Override
    public boolean containsKey(String key) {
        return this.keys().contains(key);
    }

}
