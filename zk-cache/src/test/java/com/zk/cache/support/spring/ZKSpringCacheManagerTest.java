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
 * @Title: ZKSpringCacheManagerTest.java 
 * @author Vinson 
 * @Package com.zk.cache.support.spring 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 9:24:37 AM 
 * @version V1.0   
*/
package com.zk.cache.support.spring;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.zk.cache.helper.ZKCacheTestConfig;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSpringCacheManagerTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSpringCacheManagerTest {

    @Test
    public void test() {
        try {

            TestCase.assertNotNull(ZKCacheTestConfig.getCtx());

            CacheManager springCacheManager = ZKCacheTestConfig.getSpringCacheManager();

            TestCase.assertNotNull(springCacheManager);

            Cache cache = springCacheManager.getCache("test_spring_cache");

            String key = null;
            String value = null;
            Map<String, String> vMap = null;

            // 添加缓存值
            key = "test_cache_key_1";
            value = "test_cache_value_1";
            cache.put(key, value);
            key = "test_cache_key_2";
            value = "test_cache_value_添加值_2";
            cache.put(key, value);
            key = "test_cache_key_3";
            vMap = new HashMap<>();
            vMap.put("tmapkey_1", "tmapvalue_2");
            vMap.put("tmapkey_2", "tmapvalue_2");
            cache.put(key, vMap);

            key = "test_cache_key_2";
            value = "test_cache_value_添加值_2";
            TestCase.assertEquals(value, cache.get(key).get());
            value = "test_cache_value_update_修改值_2";
            cache.put("test_cache_key_2", value);
            TestCase.assertEquals(value, cache.get(key).get());

//          System.out.println("[^_^: 201706201653-001] " + cache.keys());
//          System.out.println("[^_^: 201706201653-002] " + cache.values());

            key = "test_cache_key_1";
            value = "test_cache_value_1";
            cache.evict(key);
            TestCase.assertNull(cache.get(key));

            cache.clear();

            key = "test_cache_key_1";
            value = "test_cache_value_1";
            cache.put(key, value);
            key = "test_cache_key_2";
            value = "test_cache_value_添加值_2";
            cache.put(key, value);
            key = "test_cache_key_3";
            vMap = new HashMap<>();
            vMap.put("tmapkey_1", "tmapvalue_2");
            vMap.put("tmapkey_2", "tmapvalue_2");
            cache.put(key, vMap);
            key = "test_cache_key_2";
            value = "test_cache_value_添加值_2";
            TestCase.assertEquals(value, cache.get(key).get());
            key = "test_cache_key_2";
            value = "test_cache_value_update_修改值_2";
            cache.put(key, value);
            TestCase.assertEquals(value, cache.get(key).get());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
