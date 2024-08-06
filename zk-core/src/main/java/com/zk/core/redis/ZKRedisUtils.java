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
* @Title: ZKRedisUtils.java 
* @author Vinson 
* @Package com.zk.core.redis 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 16, 2021 12:44:17 PM 
* @version V1.0 
*/
package com.zk.core.redis;

import com.zk.core.utils.ZKObjectUtils;
import com.zk.core.utils.ZKStringUtils;

import redis.clients.jedis.JedisPool;

/**
 * @ClassName: ZKRedisUtils
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKRedisUtils {

    public static byte[] toKeyBytes(String key) {
        return key.getBytes();
    }

    public static String toKeyString(byte[] key) {
        return ZKStringUtils.toString(key);
    }

    /**
     * 获取byte[]类型Key
     * 
     * @param key
     * @return
     */
    public static byte[] toValueBytes(Object object) {
        return ZKObjectUtils.serialize(object);
//        if (object instanceof String) {
//            return ZKStringUtils.getBytes((String) object);
//        }
//        else {
//            return ZKObjectUtils.serialize(object);
//        }
    }

    @SuppressWarnings("unchecked")
    public static <V> V toValueObject(byte[] bytes) {
        return bytes == null ? null : (V) ZKObjectUtils.unserialize(bytes);
//        if (bytes != null) {
//            if (ZKObjectUtils.isSerialize(bytes)) {
//                return (V) ZKObjectUtils.unserialize(bytes);
//            }
//            else {
//                return (V) ZKStringUtils.toString(bytes);
//            }
//        }
//        return null;
    }

    public static void printJedisPoolInfo(String className, JedisPool jedisPool) {
        System.out.println("[^_^:20231226-2301-001] -------------------------------------------------- ");
        System.out.println("[^_^:20231226-2301-001] className: " + className);
        System.out.println("[^_^:20231226-2301-001] isClosed: " + jedisPool.isClosed());
        System.out.println("[^_^:20231226-2301-001] getBorrowedCount: " + jedisPool.getBorrowedCount());
        System.out.println("[^_^:20231226-2301-001] getCreatedCount: " + jedisPool.getCreatedCount());
//        System.out.println("[^_^:20231226-2301-001] getCreationStackTrace: " + jedisPool.getCreationStackTrace());
        System.out.println("[^_^:20231226-2301-001] getDestroyedByBorrowValidationCount: "
                + jedisPool.getDestroyedByBorrowValidationCount());
        System.out.println(
                "[^_^:20231226-2301-001] getDestroyedByEvictorCount: " + jedisPool.getDestroyedByEvictorCount());
        System.out.println("[^_^:20231226-2301-001] getDestroyedCount: " + jedisPool.getDestroyedCount());
        System.out.println(
                "[^_^:20231226-2301-001] getMaxBorrowWaitTimeMillis: " + jedisPool.getMaxBorrowWaitTimeMillis());
        System.out.println("[^_^:20231226-2301-001] getMaxIdle: " + jedisPool.getMaxIdle());
        System.out.println("[^_^:20231226-2301-001] getMaxTotal: " + jedisPool.getMaxTotal());
        System.out.println("[^_^:20231226-2301-001] getMeanActiveTimeMillis: " + jedisPool.getMeanActiveTimeMillis());
        System.out.println(
                "[^_^:20231226-2301-001] getMeanBorrowWaitTimeMillis: " + jedisPool.getMeanBorrowWaitTimeMillis());
        System.out.println("[^_^:20231226-2301-001] getMeanIdleTimeMillis: " + jedisPool.getMeanIdleTimeMillis());

    }

}
