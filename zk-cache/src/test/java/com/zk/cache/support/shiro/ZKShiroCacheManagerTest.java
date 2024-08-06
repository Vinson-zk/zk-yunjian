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
 * @Title: ZKShiroCacheManagerTest.java 
 * @author Vinson 
 * @Package com.zk.cache.support.shiro 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 9:24:16 AM 
 * @version V1.0   
*/
package com.zk.cache.support.shiro;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.junit.Test;

import com.zk.cache.helper.ZKCacheTestConfig;

import junit.framework.TestCase;

/** 
* @ClassName: ZKShiroCacheManagerTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKShiroCacheManagerTest {

    @Test
    public void test() {
        try {

            TestCase.assertNotNull(ZKCacheTestConfig.getCtx());

            CacheManager shiroCacheManager = ZKCacheTestConfig.getShiroCacheManager();
            TestCase.assertNotNull(shiroCacheManager);

            Cache<String, Object> cache = shiroCacheManager.getCache("test_shiro_cache");

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
            TestCase.assertEquals(value, cache.get(key));
            value = "test_cache_value_update_修改值_2";
            cache.put("test_cache_key_2", value);
            TestCase.assertEquals(value, cache.get(key));
            TestCase.assertEquals(3, cache.size());

            System.out.println("[^_^: 201706201653-001] " + cache.keys());
            System.out.println("[^_^: 201706201653-002] " + cache.values());

            key = "test_cache_key_1";
            value = "test_cache_value_1";
            cache.remove(key);
            TestCase.assertNull(cache.get(key));
            TestCase.assertEquals(2, cache.size());

            cache.clear();
            TestCase.assertEquals(0, cache.size());

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
            TestCase.assertEquals("test_cache_value_添加值_2", cache.get("test_cache_key_2"));
            cache.put("test_cache_key_2", "test_cache_value_update_修改值_2");
            TestCase.assertEquals("test_cache_value_update_修改值_2", cache.get("test_cache_key_2"));
            TestCase.assertEquals(3, cache.size());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
