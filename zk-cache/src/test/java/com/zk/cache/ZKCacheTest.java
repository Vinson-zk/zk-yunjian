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
* @Title: ZKCacheTest.java 
* @author Vinson 
* @Package com.zk.cache 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 13, 2021 11:25:37 AM 
* @version V1.0 
*/
package com.zk.cache;

import com.zk.cache.helper.entity.ZKRedisTestChildEntity;
import com.zk.cache.helper.entity.ZKRedisTestEntity;
import com.zk.core.utils.ZKStringUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKCacheTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCacheTest {

    @FunctionalInterface
    public static interface GetCache {
        public ZKCache<?, ?> getCache(long millisecond);
    }

    public static ZKRedisTestEntity getObjectValue() {
        ZKRedisTestEntity obj = new ZKRedisTestEntity();
        obj.test();
        obj.setV2("v2");
        obj.setV3(new ZKRedisTestChildEntity());
        obj.getV3().setV1(2);
        obj.getV3().setV2("v2-1");
        obj.getV3().setV3(new ZKRedisTestChildEntity());
        obj.getV3().getV3().setV1(3);
        obj.getV3().getV3().setV2("v2-2-2");
        return obj;
    }

    /**
     * 测试 get put
     */
    public static void testGetAndPut(ZKCache<String, Object> zkCache) {
        TestCase.assertNotNull(zkCache);

        String key = "ZKCacheTest";
        byte[] vBytes = "v-ZKCacheTest".getBytes(), resBytes;
        String vStr = "v-ZKCacheTest", resStr;
        ZKRedisTestEntity vObj = getObjectValue(), resObj;

        /**** 字符串 key ***/
        // 存放 字节
        zkCache.put(key, vBytes);
        resBytes = (byte[]) zkCache.get(key);
        TestCase.assertEquals(ZKStringUtils.toString(vBytes), ZKStringUtils.toString(resBytes));
        // 存放 字符串
        zkCache.put(key, vStr);
        resStr = (String) zkCache.get(key);
        TestCase.assertEquals(vStr, resStr);
        // 存放对象
        zkCache.put(key, vObj);
        resObj = (ZKRedisTestEntity) zkCache.get(key);
        TestCase.assertEquals(vObj.getV1(), resObj.getV1());
        TestCase.assertEquals(vObj.getV3().getV2(), resObj.getV3().getV2());
        TestCase.assertEquals(vObj.getV3().getV3().getV1(), resObj.getV3().getV3().getV1());

        zkCache.remove(key);
    }

    /**
     * 测试过期
     * 
     * @throws InterruptedException
     * 
     */
    public static void testExpired(GetCache getCache) throws InterruptedException {

        long millisecond = 1000, sleep = 800;
        @SuppressWarnings("unchecked")
        ZKCache<String, Object> zkCache = (ZKCache<String, Object>) getCache.getCache(millisecond);

        // ZKCache<Object, Object>
        String key = "ZKCacheTest";
        String vStr = "v-ZKCacheTest", resStr;

        // 设置
        zkCache.put(key, vStr);
        resStr = zkCache.get(key).toString();
        TestCase.assertEquals(vStr, resStr);
        // 删除值
        zkCache.remove(key);
        resStr = (String) zkCache.get(key);
        TestCase.assertNull(resStr);
        // 过期测试
        zkCache.put(key, vStr);
        resStr = (String) zkCache.get(key);
        TestCase.assertEquals(vStr, resStr);
        System.out.println("[20210815-1150-001] 休眠 " + sleep + " 毫秒；");
        Thread.sleep(sleep);
        System.out.println("[20210815-1150-001] 休眠 " + sleep + " 毫秒；结束");
        resStr = (String) zkCache.get(key);
        TestCase.assertEquals(vStr, resStr);

        zkCache.put(key, vStr);
        resStr = (String) zkCache.get(key);
        System.out.println("[20210815-1150-001] 休眠 " + sleep + " 毫秒；");
        Thread.sleep(sleep);
        System.out.println("[20210815-1150-001] 休眠 " + sleep + " 毫秒；结束");
        resStr = (String) zkCache.get(key);
        TestCase.assertEquals(vStr, resStr);

        System.out.println("[20210815-1150-001] 休眠 " + sleep + " 毫秒；");
        Thread.sleep(2 * sleep);
        System.out.println("[20210815-1150-001] 休眠 " + sleep + " 毫秒；结束");
        resStr = (String) zkCache.get(key);
        TestCase.assertNull(resStr);

    }

    /**
     * 清空缓存
     * 
     * @throws CustormCacheException
     */
    public static void testOther(ZKCache<String, Object> zkCache) {
        String key1 = "ZKCacheTest_1", key2 = "ZKCacheTest_2";
        String vStr1 = "v-ZKCacheTest_1", vStr2 = "v-ZKCacheTest_2";

        zkCache.put(key1, vStr1);
        zkCache.put(key2, vStr2);

        TestCase.assertEquals(2, zkCache.size());
        TestCase.assertEquals(2, zkCache.keys().size());
        TestCase.assertEquals(2, zkCache.values().size());

        TestCase.assertFalse(zkCache.containsKey("no-key"));

        TestCase.assertTrue(zkCache.containsKey(key1));
        TestCase.assertTrue(zkCache.containsKey(key2));
    }

}
