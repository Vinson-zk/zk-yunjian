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
 * @Title: ZKMongoCacheTest.java 
 * @author Vinson 
 * @Package com.zk.cache.mongo 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 9:22:38 AM 
 * @version V1.0   
*/
package com.zk.cache.mongo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.zk.cache.ZKCacheTest;
import com.zk.cache.helper.ZKCacheTestConfig;
import com.zk.cache.utils.ZKCacheUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKMongoCacheTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMongoCacheTest {

    private static MongoTemplate mongoTemplate;

    private static final String cache_name = "test_mongo_cache";

    @Test
    public void testGetAndPut() {
        try {
            TestCase.assertNotNull(ZKCacheTestConfig.getCtx());
            mongoTemplate = ZKCacheTestConfig.getCtx().getBean(MongoTemplate.class);
            TestCase.assertNotNull(mongoTemplate);

            ZKMongoCache<String, Object> zkCache = ZKMongoCache.CreateMongoCache(mongoTemplate, cache_name, 2000);

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

            TestCase.assertNotNull(ZKCacheTestConfig.getCtx());
            mongoTemplate = ZKCacheTestConfig.getCtx().getBean(MongoTemplate.class);
            TestCase.assertNotNull(mongoTemplate);

            ZKCacheTest.testExpired(millisecond -> {
                return ZKMongoCache.CreateMongoCache(mongoTemplate, cache_name, millisecond);
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

            TestCase.assertNotNull(ZKCacheTestConfig.getCtx());
            mongoTemplate = ZKCacheTestConfig.getCtx().getBean(MongoTemplate.class);
            TestCase.assertNotNull(mongoTemplate);

            ZKMongoCache<String, Object> zkCache = ZKMongoCache.CreateMongoCache(mongoTemplate, cache_name, 2000);
            zkCache.clear();
            ZKCacheTest.testOther(zkCache);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testCache() {
        try {

            ZKCacheUtils.setCacheManager(ZKCacheTestConfig.getCacheManager());
            TestCase.assertNotNull(ZKCacheUtils.getCacheManager());

            mongoTemplate = ZKCacheTestConfig.getCtx().getBean(MongoTemplate.class);

            if (mongoTemplate != null) {
                System.out.println("[^_^:20190616-1842-001] 初始化 mongodb template 成功");
            }
            else {
                System.out.println("[>_<:20190616-1842-001] 初始化 mongodb template 失败");
            }


            Collection<String> cacheNames = ZKMongoCache.getCacheIds(mongoTemplate);

            if (cacheNames != null) {
                for (String cacheName : cacheNames) {
                    ZKMongoCache<String, Object> mc = ZKMongoCache.getCahce(cacheName);
                    if (mc != null) {
                        mc.clear();
                    }
                }
            }

            cacheNames = ZKMongoCache.getCacheIds(mongoTemplate);
            System.out.println("[^_^: 20170716-2316-001] cacheNames:" + cacheNames);
            TestCase.assertNull(cacheNames);

            ZKMongoCache<String, Object> mc = ZKMongoCache.CreateMongoCache(mongoTemplate, cache_name, 2000);
            mc.clear();

            mc = ZKMongoCache.getCahce(cache_name);
            TestCase.assertNull(mc);

            cacheNames = ZKMongoCache.getCacheIds(mongoTemplate);
            System.out.println("[^_^: 20170716-2316-002] " + cacheNames);
            TestCase.assertNull(cacheNames);

            mc = ZKMongoCache.CreateMongoCache(mongoTemplate, cache_name, 2000);
            String key = null;
            String value = null;
            Map<String, String> vMap = null;

            /*** 一个不存在的缓存取值 ***/
            key = "test_cache_key_1";
            value = (String) mc.get(key);
            TestCase.assertNull(value);

            /*** 一个不存在的缓存移除 key ***/
            mc.remove(key);

            // 添加缓存值
            key = "test_cache_key_1";
            value = "test_cache_value_1";
            mc.put(key, value);
            key = "test_cache_key_2";
            value = "test_cache_value_添加值_2";
            mc.put(key, value);
            key = "test_cache_key_3";
            vMap = new HashMap<>();
            vMap.put("tmapkey_1", "tmapvalue_2");
            vMap.put("tmapkey_2", "tmapvalue_2");
            mc.put(key, vMap);

            key = "test_cache_key_2";
            value = "test_cache_value_添加值_2";
            mc.put(key, value);
            TestCase.assertEquals(value, mc.get(key));
            value = "test_cache_value_update_修改值_2";
            mc.put("test_cache_key_2", value);
            TestCase.assertEquals(value, mc.get(key));
            TestCase.assertEquals(3, mc.size());

            System.out.println("[^_^: 20170620-1653-001] " + mc.keys());
            System.out.println("[^_^: 20170620-1653-002] " + mc.values());
            cacheNames = ZKMongoCache.getCacheIds(mongoTemplate);
            System.out.println("[^_^: 20170620-1653-003] " + cacheNames);
            TestCase.assertEquals(1, cacheNames.size());
            TestCase.assertEquals(cache_name, cacheNames.toArray()[0]);

            key = "test_cache_key_1";
            value = "test_cache_value_1";
            mc.remove(key);
            TestCase.assertNull(mc.get(key));
            TestCase.assertEquals(2, mc.size());

            mc.clear();
            TestCase.assertEquals(0, mc.size());

            mc = ZKMongoCache.getCahce(cache_name);
            TestCase.assertNull(mc);

            mc = ZKMongoCache.CreateMongoCache(mongoTemplate, cache_name, 2000);

            key = "test_cache_key_1";
            value = "test_cache_value_1";
            mc.put(key, value);
            key = "test_cache_key_2";
            value = "test_cache_value_添加值_2";
            mc.put(key, value);
            key = "test_cache_key_3";
            vMap = new HashMap<>();
            vMap.put("tmapkey_1", "tmapvalue_2");
            vMap.put("tmapkey_2", "tmapvalue_2");
            mc.put(key, vMap);
            TestCase.assertEquals("test_cache_value_添加值_2", mc.get("test_cache_key_2"));
            mc.put("test_cache_key_2", "test_cache_value_update_修改值_2");
            TestCase.assertEquals("test_cache_value_update_修改值_2", mc.get("test_cache_key_2"));
            TestCase.assertEquals(3, mc.size());

            // 休息 3 秒，2 秒后过期
            Thread.sleep(3000);
            TestCase.assertEquals(0, mc.size());
            TestCase.assertEquals(null, mc.get("test_cache_key_2"));

            mc = ZKMongoCache.getCahce(cache_name);
            TestCase.assertNull(mc);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
