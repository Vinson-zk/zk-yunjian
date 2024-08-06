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
* @Title: ZKJedisOperatorByte.java 
* @author Vinson 
* @Package com.zk.core.redis 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 16, 2021 11:28:56 AM 
* @version V1.0 
*/
package com.zk.core.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

/** 
* @ClassName: ZKJedisOperatorByte 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKJedisOperatorByte {

    private static Logger log = LogManager.getLogger(ZKJedisOperatorByte.class);

    private JedisPool jedisPool;

    public ZKJedisOperatorByte() {

    }

    public ZKJedisOperatorByte(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * @return jedisPool sa
     */
    public JedisPool getJedisPool() {
        return jedisPool;
    }

    /**
     * @param jedisPool
     *            the jedisPool to set
     */
    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /********************************************************************************************************/
    /** key 键值对 处理 */
    /********************************************************************************************************/
    /**
     * 获取值；
     * 
     * @param key
     *            键
     * @return 值
     */
    public byte[] get(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                return jedis.get(key);
            }
        }
        catch(Exception e) {
            log.error("[^_^:20210812-0908-001] redis 取键值异常!");
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return null;
    }

    public String set(byte[] key, byte[] value) {
        return this.set(key, value, 0);
    }
    /**
     * 设置值
     *
     * @param key
     * @param value
     * @param validSeconds
     * @return void
     */
    public String set(byte[] key, byte[] value, int validSeconds) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            String res = jedis.set(key, value);
            if (validSeconds != 0) {
                jedis.expire(key, validSeconds);
            }
            return res;
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return null;
    }

    /********************************************************************************************************/
    /** hash 表 处理 */
    /********************************************************************************************************/
    public Map<byte[], byte[]> hget(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                return jedis.hgetAll(key);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return null;
    }

    public byte[] hget(byte[] key, byte[] field) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.hexists(key, field)) {
                return jedis.hget(key, field);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return null;
    }

    public Long hset(byte[] key, byte[] field, byte[] value) {
        return this.hset(key, field, value, 0);
    }

    public Long hset(byte[] key, byte[] field, byte[] value, int validSeconds) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            Long res = jedis.hset(key, field, value);
            if (validSeconds != 0) {
                jedis.expire(key, validSeconds);
            }
            return res;
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return null;
    }

    public Set<byte[]> hkeys(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.hkeys(key);
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return Sets.newHashSet();
    }

    public List<byte[]> hValues(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.hvals(key);
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return Lists.newArrayList();
    }

    /**
     * 判断 hex key 和 字段是否存在
     *
     * @param key
     * @param field
     * @return
     * @return boolean
     */
    public boolean hexists(byte[] key, byte[] field) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.hexists(key, field);
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return false;
    }

    public Long hdel(byte[] key, byte[]... fields) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.hdel(key, fields);
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return null;
    }
    
    /********************************************************************************************************/
    /** set 集合 处理 */
    /********************************************************************************************************/
    public Long sAdd(byte[] key, byte[]... members) {
        return this.sAdd(key, 0, members);
    }

    public Long sAdd(byte[] key, int validSeconds, byte[]... members) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            Long res = jedis.sadd(key, members);
            if (validSeconds != 0) {
                jedis.expire(key, validSeconds);
            }
            return res;
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return null;
    }

    public Set<byte[]> sget(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.smembers(key);
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return null;
    }

    public Long sremove(byte[] key, byte[]... members) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.srem(key, members);
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return null;
    }

    /********************************************************************************************************/
    /*** 通用方法 */
    /********************************************************************************************************/

    public Set<byte[]> keys(byte[] pattern) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            Set<byte[]> byteKeys = jedis.keys(pattern);
            return byteKeys;
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return Sets.newHashSet();
    }

    /**
     * 缓存是否存在
     * 
     * @param key
     *            键
     * @return
     */
    public boolean exists(byte[] key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.exists(key);
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return false;
    }

    /**
     * 删除缓存
     * 
     * @param key
     *            键
     * @return
     */
    public Long del(byte[]... keys) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.del(keys);
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        return null;
    }

    public void expire(byte[] key, int validSeconds) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (validSeconds != 0) {
                jedis.expire(key, validSeconds);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(jedis);
        }
    }

    /********************************************************************************************************/
    /*** 链接 方法 */
    /********************************************************************************************************/
    /**
     * 获取资源
     * 
     * @return
     * @throws JedisException
     */
    public Jedis getResource() throws JedisException {
        Jedis jedis = null;
        try {
//            ZKRedisUtils.printJedisPoolInfo(this.getClass().getName(), this.getJedisPool());
            jedis = this.getJedisPool().getResource();
        }
        catch(JedisException e) {
            e.printStackTrace();
            close(jedis);
            throw e;
        }
        return jedis;
    }

    public void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    /********************************************************************************************************/
    /********************************************************************************************************/
    /********************************************************************************************************/

//    public List<String> getList(String key) {
//    public List<Object> getObjectList(String key) {
//    public long setList(String key, List<String> value, int cacheSeconds) {
//    public long setObjectList(String key, List<Object> value, int cacheSeconds) {
//    public long listAdd(String key, String... value) {
//    public long listObjectAdd(String key, Object... value) {
//    public long setSetAdd(String key, String... value) {
//    public long setSetObjectAdd(String key, Object... value) {
//    public long setObjectSet(String key, Set<Object> value, int cacheSeconds) {
//    public Set<Object> getObjectSet(String key) {
//    public Set<String> getSet(String key) {
//    public long setSet(String key, Set<String> value, int cacheSeconds) {

//    public Map<String, String> getMap(String key) {
//    public Map<String, Object> getObjectMap(String key) {
//    public String setMap(String key, Map<String, String> value, int cacheSeconds) {
//    public String setObjectMap(String key, Map<String, Object> value, int cacheSeconds) {
//    public String mapPut(String key, Map<String, String> value) {
//    public String mapObjectPut(String key, Map<String, Object> value) {
//    public long mapRemove(String key, String mapKey) {
//    public long mapObjectRemove(String key, String mapKey) {
//    public boolean mapExists(String key, String mapKey) {
//    public boolean mapObjectExists(String key, String mapKey) {

}
