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
* @Title: ZKJedisOperatorStringKey.java 
* @author Vinson 
* @Package com.zk.core.redis 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 16, 2021 11:26:07 AM 
* @version V1.0 
*/
package com.zk.core.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

/**
 * 操作统一使用 String 做为 key 的参数类型；但实际是以 byte 须知为 key 写入 redis 中
 * 
 * @ClassName: ZKJedisOperatorStringKey
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKJedisOperatorStringKey {

//    private static Logger log = LogManager.getLogger(ZKJedisOperatorStringKey.class);

    private ZKJedisOperatorByte jedisOperatorByte;

    public ZKJedisOperatorStringKey() {

    }

    public ZKJedisOperatorStringKey(ZKJedisOperatorByte jedisOperatorByte) {
        this.jedisOperatorByte = jedisOperatorByte;
    }

    /**
     * @return jedisOperatorByte sa
     */
    public ZKJedisOperatorByte getJedisOperatorByte() {
        return jedisOperatorByte;
    }

    /**
     * @param jedisOperatorByte
     *            the jedisOperatorByte to set
     */
    public void setJedisOperatorByte(ZKJedisOperatorByte jedisOperatorByte) {
        this.jedisOperatorByte = jedisOperatorByte;
    }

    /********************************************************************************************************/
    /** key 键值 处理 */
    /********************************************************************************************************/

    /**
     * 获取值；
     * 
     * @param key
     *            键
     * @return 值
     */
    public <V> V get(String key) {
        return toValueObject(this.getByte(key));
    }

    public byte[] getByte(String key) {
        return this.getJedisOperatorByte().get(toKeyBytes(key));
    }

    /**
     * 设置 key 值
     *
     * @param key
     * @param value
     * @param validSeconds
     * @return void
     */
    public <V> String set(String key, V value) {
        return this.set(key, value, 0);
    }

    public <V> String set(String key, byte[] value) {
        return this.set(key, value, 0);
    }

    public <V> String set(String key, V value, int validSeconds) {
        return this.set(key, toValueBytes(value), validSeconds);
    }

    public <V> String set(String key, byte[] value, int validSeconds) {
        return this.getJedisOperatorByte().set(toKeyBytes(key), value, validSeconds);
    }

    /********************************************************************************************************/
    /** hash 表键值对 处理 */
    /********************************************************************************************************/

    /**
     * 判断 hex key 和 字段是否存在
     *
     * @param key
     * @param field
     * @return
     * @return boolean
     */
    public boolean hexists(String key, String field) {
        return this.getJedisOperatorByte().hexists(toKeyBytes(key), toKeyBytes(field));
    }

    /**
     * 取 hash 集合
     *
     * @Title: hget
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 16, 2021 12:12:37 PM
     * @param <K>
     * @param <V>
     * @param key
     * @return
     * @return Map<K,V>
     */
    public <V> Map<String, V> hget(String key) {
        Map<String, V> resMap = Maps.newHashMap();
        Map<byte[], byte[]> map = this.hgetByte(key);
        for (Map.Entry<byte[], byte[]> e : map.entrySet()) {
            resMap.put(toKeyString(e.getKey()), toValueObject(e.getValue()));
        }
        return resMap;
    }

    public Map<byte[], byte[]> hgetByte(String key) {
        return this.getJedisOperatorByte().hget(this.toKeyBytes(key));
    }

    /**
     * 取 hash 键值
     *
     * @Title: hget
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 16, 2021 12:13:17 PM
     * @param <V>
     * @param key
     * @param field
     * @return
     * @return V
     */
    public <V> V hget(String key, String field) {
        return this.toValueObject(this.hgetByte(key, field));
    }

    public byte[] hgetByte(String key, String field) {
        return this.getJedisOperatorByte().hget(this.toKeyBytes(key), this.toKeyBytes(field));
    }

    /**
     * 设置 hash 键值
     *
     * @Title: hset
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 16, 2021 12:12:59 PM
     * @param <V>
     * @param key
     * @param field
     * @param value
     * @return
     * @return Long
     */
    public <V> Long hset(String key, String field, V value) {
        return this.hset(key, field, value, 0);
    }

    public <V> Long hset(String key, String field, byte[] value) {
        return this.hset(key, field, value, 0);
    }

    public <V> Long hset(String key, String field, V value, int validSeconds) {
        return this.hset(key, field, toValueBytes(value), validSeconds);
    }

    public <V> Long hset(String key, String field, byte[] values, int validSeconds) {
        return this.getJedisOperatorByte().hset(toKeyBytes(key), toKeyBytes(field), values, validSeconds);
    }

    /**
     * 取 hash 集合所有 keys
     *
     * @Title: hkeys
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 16, 2021 12:14:14 PM
     * @param key
     * @return
     * @return Set<String>
     */
    public Set<String> hkeys(String key) {
        Set<String> keys = Sets.newHashSet();
        Set<byte[]> byteKeys = this.getJedisOperatorByte().hkeys(this.toKeyBytes(key));
        for (byte[] k : byteKeys) {
            keys.add(this.toKeyString(k));
        }
        return keys;
    }

    /**
     * 取 hash 集合所有值
     *
     * @Title: hValues
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 16, 2021 12:14:02 PM
     * @param <V>
     * @param key
     * @return
     * @return List<V>
     */
    public <V> List<V> hValues(String key) {
        List<V> values = Lists.newArrayList();
        List<byte[]> byteValues = this.hValuesBytes(key);
        for (byte[] v : byteValues) {
            values.add(this.toValueObject(v));
        }
        return values;
    }

    public List<byte[]> hValuesBytes(String key) {
        List<byte[]> byteValues = this.getJedisOperatorByte().hValues(this.toKeyBytes(key));
        return byteValues;
    }

    /**
     * 删除 hash 键
     *
     * @Title: hdel
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 16, 2021 12:14:44 PM
     * @param key
     * @param field
     * @return
     * @return Long
     */
    public Long hdel(String key, String field) {
        return this.getJedisOperatorByte().hdel(this.toKeyBytes(key), this.toKeyBytes(field));
    }


    /********************************************************************************************************/
    /*** 通用方法 */
    /********************************************************************************************************/
    /******/

    public Set<String> keys(String pattern) {
        Set<String> keys = Sets.newHashSet();
        Set<byte[]> byteKeys = this.getJedisOperatorByte().keys(this.toKeyBytes(pattern));
        for (byte[] k : byteKeys) {
            keys.add(this.toKeyString(k));
        }
        return keys;
    }

    public void expire(String key, int validSeconds) {
        this.getJedisOperatorByte().expire(this.toKeyBytes(key), validSeconds);
    }

    public long del(String key) {
//        return this.getJedisOperatorByte().del(this.toKeyBytes(key));
        Jedis jedis = null;
        try {
            jedis = this.getResource();
            return jedis.del(key);
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            this.close(jedis);
        }
        return 0l;
    }

    /******/
    /**
     * 删除缓存
     * 
     * @param key
     *            键
     * @return
     */
    public Long del(String... keys) {
//        Long l = 0l;
//        for (String key : keys) {
//            l += this.getJedisOperatorByte().del(key.getBytes());
//        }
//        return l;

        Jedis jedis = null;
        try {
            jedis = this.getResource();
            return jedis.del(keys);
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            this.close(jedis);
        }
        return null;
    }

    /**
     * 缓存是否存在
     * 
     * @param key
     *            键
     * @return
     */
    public boolean exists(String key) {
//        return this.getJedisOperatorByte().exists(this.toKeyBytes(key));
        Jedis jedis = null;
        try {
            jedis = this.getResource();
            return jedis.exists(key);
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            this.close(jedis);
        }
        return false;
    }

    /**
     * 缓存是否存在
     * 
     * @param key
     *            键
     * @return
     */
    public Long exists(String... keys) {
        Jedis jedis = null;
        try {
            jedis = this.getResource();
            return jedis.exists(keys);
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            this.close(jedis);
        }
        return 0l;
    }

    /********************************************************************************************************/
    /********************************************************************************************************/
    /********************************************************************************************************/

    /**
     * 获取资源
     * 
     * @return
     * @throws JedisException
     */
    public Jedis getResource() throws JedisException {
        return this.getJedisOperatorByte().getResource();
    }

    public void close(Jedis jedis) {
        this.getJedisOperatorByte().close(jedis);
    }

//    public JedisPool getJedisPool() {
//        return this.getJedisOperatorByte().getJedisPool();
//    }

    /********************************************************************************************************/
    /********************************************************************************************************/
    /********************************************************************************************************/

    public byte[] toKeyBytes(String key) {
        return ZKRedisUtils.toKeyBytes(key);
    }

    public String toKeyString(byte[] key) {
        return ZKRedisUtils.toKeyString(key);
    }

    /**
     * 获取byte[]类型Key
     * 
     * @param key
     * @return
     */
    public byte[] toValueBytes(Object object) {
        return ZKRedisUtils.toValueBytes(object);
    }

    public <V> V toValueObject(byte[] bytes) {
        return ZKRedisUtils.toValueObject(bytes);
    }

}
