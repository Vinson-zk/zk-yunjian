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
 * @Title: ZKCacheUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.cache.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 9:23:16 AM 
 * @version V1.0   
*/
package com.zk.cache.utils;

import org.junit.Test;

import com.zk.cache.helper.ZKCacheTestConfig;

import junit.framework.TestCase;

/** 
* @ClassName: ZKCacheUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCacheUtilsTest {

    @Test
    public void test() {
        try {
            TestCase.assertNotNull(ZKCacheTestConfig.getCtx());
            ZKCacheUtils.setCacheManager(ZKCacheTestConfig.getCacheManager());
            TestCase.assertNotNull(ZKCacheUtils.getCacheManager());

            String key = "test_key";
            ZKCacheUtils.remove(key);
            String v = ZKCacheUtils.getKey(key);
            TestCase.assertEquals(v, null);
            v = "test_cache";
            ZKCacheUtils.put(key, v);
            System.out.println("[^_^:2017092-90847-001] " + key + " = " + ZKCacheUtils.getKey(key));
            TestCase.assertEquals(v, ZKCacheUtils.getKey(key));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
