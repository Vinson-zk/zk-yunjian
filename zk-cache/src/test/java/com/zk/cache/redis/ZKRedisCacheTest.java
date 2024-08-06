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
* @Title: ZKRedisCacheTest.java 
* @author Vinson 
* @Package com.zk.cache.redis 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 13, 2021 11:07:17 AM 
* @version V1.0 
*/
package com.zk.cache.redis;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.cache.ZKCacheTest;
import com.zk.cache.helper.ZKCacheTestSpringbootMain;
import com.zk.core.redis.ZKJedisOperatorStringKey;

import junit.framework.TestCase;

/** 
* @ClassName: ZKRedisCacheTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKRedisCacheTest {

    private static final String cache_name = "test_redis_cache";

    @Test
    public void testGetAndPut() {
        try {
            ConfigurableApplicationContext ctx = ZKCacheTestSpringbootMain.run(new String[] {});
            TestCase.assertNotNull(ctx);
            ZKJedisOperatorStringKey zkJedisOperator = ctx.getBean(ZKJedisOperatorStringKey.class);
            TestCase.assertNotNull(zkJedisOperator);

            ZKRedisCache<Object> zkCache = new ZKRedisCache<Object>(cache_name, 2000, zkJedisOperator);

            zkCache.clear();
            ZKCacheTest.testGetAndPut(zkCache);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testExpired() {
        try {

            ConfigurableApplicationContext ctx = ZKCacheTestSpringbootMain.run(new String[] {});
            TestCase.assertNotNull(ctx);
            ZKJedisOperatorStringKey zkJedisOperator = ctx.getBean(ZKJedisOperatorStringKey.class);
            TestCase.assertNotNull(zkJedisOperator);

            ZKCacheTest.testExpired(millisecond -> {
                return new ZKRedisCache<Object>(cache_name, millisecond, zkJedisOperator);
            });

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testOther() {
        try {
            ConfigurableApplicationContext ctx = ZKCacheTestSpringbootMain.run(new String[] {});
            TestCase.assertNotNull(ctx);
            ZKJedisOperatorStringKey zkJedisOperator = ctx.getBean(ZKJedisOperatorStringKey.class);
            TestCase.assertNotNull(zkJedisOperator);

            ZKRedisCache<Object> zkCache = new ZKRedisCache<Object>(cache_name, 20000, zkJedisOperator);

            zkCache.clear();
            ZKCacheTest.testOther(zkCache);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
