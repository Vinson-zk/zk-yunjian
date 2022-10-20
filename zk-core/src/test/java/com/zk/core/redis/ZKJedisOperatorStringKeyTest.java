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
* @Title: ZKJedisOperatorStringKeyTest.java 
* @author Vinson 
* @Package com.zk.core.redis 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 16, 2021 12:55:50 PM 
* @version V1.0 
*/
package com.zk.core.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.core.helper.ZKCoreTestHelperSpringBootMain;
import com.zk.core.helper.entity.ZKRedisTestChildEntity;
import com.zk.core.helper.entity.ZKRedisTestEntity;

import junit.framework.TestCase;

/** 
* @ClassName: ZKJedisOperatorStringKeyTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKJedisOperatorStringKeyTest {

    /********************************************************************************************************/
    /** key 键值对 测试 */
    /********************************************************************************************************/
    @Test
    public void testStringKey() {

        ConfigurableApplicationContext ctx = ZKCoreTestHelperSpringBootMain.run(new String[] {});
        TestCase.assertNotNull(ctx);
        ZKJedisOperatorStringKey jedisOperatorStringKey = ctx.getBean(ZKJedisOperatorStringKey.class);
        TestCase.assertNotNull(jedisOperatorStringKey);

        try {

            String jedisKey = "test_jedis_key_core";
            String pattern = "*jedis_key*";
            // 超期时间，秒
            int seconds = 2;
            String valueStr = "valueStr", resStr;
            int valueInt = 2, resInt;
            ZKRedisTestEntity obj = null, resObj;
            Set<String> keys;

            /** key 设置与取值 */
            jedisOperatorStringKey.set(jedisKey, valueStr, seconds);
            resStr = jedisOperatorStringKey.get(jedisKey);
            TestCase.assertEquals(valueStr, resStr);
            // 基础类型 int
            jedisOperatorStringKey.set(jedisKey, valueInt);
            resInt = jedisOperatorStringKey.get(jedisKey);
            TestCase.assertEquals(valueInt, resInt);
            // 数据对象
            obj = new ZKRedisTestEntity();
            obj.test();
            obj.setV2("v2");
            obj.setV3(new ZKRedisTestChildEntity());
            obj.getV3().setV1(2);
            obj.getV3().setV2("v2-1");
            obj.getV3().setV3(new ZKRedisTestChildEntity());
            obj.getV3().getV3().setV1(3);
            obj.getV3().getV3().setV2("v2-2-2");
            jedisOperatorStringKey.set(jedisKey, obj);
            resObj = jedisOperatorStringKey.get(jedisKey);
            TestCase.assertEquals(obj.getV1(), resObj.getV1());
            TestCase.assertEquals(obj.getV3().getV2(), resObj.getV3().getV2());
            TestCase.assertEquals(obj.getV3().getV3().getV1(), resObj.getV3().getV3().getV1());
            /* 键是否存在 */
            TestCase.assertTrue(jedisOperatorStringKey.exists(jedisKey));
            /* 键匹配 */
            keys = jedisOperatorStringKey.keys(pattern);
            System.out.println("[^_^:20210812-1634-001] byte ks.size:" + keys.size());
            TestCase.assertEquals(1, keys.size());
            for (String kStr : keys) {
                System.out.println("[^_^:20210812-1634-002-kStr]:" + kStr);
            }
            /* 过期 */
            jedisOperatorStringKey.expire(jedisKey, 1);
            Thread.sleep(1000);
            TestCase.assertNull(jedisOperatorStringKey.get(jedisKey));
            /* 删除 */
            jedisOperatorStringKey.set(jedisKey, obj);
            jedisOperatorStringKey.del(jedisKey);
            TestCase.assertNull(jedisOperatorStringKey.get(jedisKey));

            System.out.println("[^_^:20210816-1648-001] key *************************************************");

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /********************************************************************************************************/
    /** hash 表键值对 测试 */
    /********************************************************************************************************/
    @Test
    public void testStringHashKey() {

        ConfigurableApplicationContext ctx = ZKCoreTestHelperSpringBootMain.run(new String[] {});
        TestCase.assertNotNull(ctx);
        ZKJedisOperatorStringKey jedisOperatorStringKey = ctx.getBean(ZKJedisOperatorStringKey.class);
        TestCase.assertNotNull(jedisOperatorStringKey);

        try {
            String jedisKey = "test_testTest_ssdf";
            String field1 = "test_field1_adfaf1", field2 = "test_field2_adfaf1";
            String value1 = "test_value_1", value2 = "test_value_2", resStr;
            int valueInt = 2, resInt;
            ZKRedisTestEntity obj = null, resObj;

            Set<String> strKeys = null;
            List<byte[]> byteValues = null;
            List<String> strValues = null;
            Map<String, Object> hashMap = null;

            // 超期时间，秒
            int seconds = 2;

            /** hash 设置与取值 */
            jedisOperatorStringKey.hset(jedisKey, field1, value1, seconds);
            resStr = jedisOperatorStringKey.hget(jedisKey, field1);
            TestCase.assertEquals(value1, resStr);
            // 基础类型 int
            jedisOperatorStringKey.hset(jedisKey, field1, valueInt);
            resInt = jedisOperatorStringKey.hget(jedisKey, field1);
            TestCase.assertEquals(valueInt, resInt);
            // 数据对象
            obj = new ZKRedisTestEntity();
            obj.test();
            obj.setV2("v2");
            obj.setV3(new ZKRedisTestChildEntity());
            obj.getV3().setV1(2);
            obj.getV3().setV2("v2-1");
            obj.getV3().setV3(new ZKRedisTestChildEntity());
            obj.getV3().getV3().setV1(3);
            obj.getV3().getV3().setV2("v2-2-2");
            jedisOperatorStringKey.hset(jedisKey, field1, obj);
            resObj = jedisOperatorStringKey.hget(jedisKey, field1);
            TestCase.assertEquals(obj.getV1(), resObj.getV1());
            TestCase.assertEquals(obj.getV3().getV2(), resObj.getV3().getV2());
            TestCase.assertEquals(obj.getV3().getV3().getV1(), resObj.getV3().getV3().getV1());
            /* 取全部值 */
            hashMap = jedisOperatorStringKey.hget(jedisKey);
            TestCase.assertNotNull(hashMap);
            TestCase.assertEquals(1, hashMap.size());
            /* field 是否存在 */
            TestCase.assertTrue(jedisOperatorStringKey.hexists(jedisKey, field1));

            /* hash 键匹配 */
            jedisOperatorStringKey.hset(jedisKey, field1, value1, seconds);
            jedisOperatorStringKey.hset(jedisKey, field2, value2, seconds);

            // 所有 field
            strKeys = jedisOperatorStringKey.hkeys(jedisKey);
            System.out.println("[^_^:20210813-1042-001] String strKeys.size:" + strKeys.size());
            TestCase.assertEquals(2, strKeys.size());
            for (String k : strKeys) {
                System.out.println("[^_^:20210813-1042-002-kStr]:" + k);
            }

            // 所有 value
            byteValues = jedisOperatorStringKey.hValuesBytes(jedisKey);
            System.out.println("[^_^:20210813-1042-001] byteValues byteValues.size:" + byteValues.size());
            TestCase.assertEquals(2, byteValues.size());
            for (byte[] v : byteValues) {
                System.out.println("[^_^:20210813-1042-002-vByte]:" + ZKRedisUtils.toValueObject(v));
            }

            strValues = jedisOperatorStringKey.hValues(jedisKey);
            System.out.println("[^_^:20210813-1042-001] String strValues.size:" + strValues.size());
            TestCase.assertEquals(2, strValues.size());
            for (String v : strValues) {
                System.out.println("[^_^:20210813-1042-002-vStr]:" + v);
            }

            /* 删除 */
            jedisOperatorStringKey.hdel(jedisKey, field1);
            TestCase.assertNull(jedisOperatorStringKey.hget(jedisKey, field1));

            System.out.println("[^_^:20210816-1648-001] hash *************************************************");

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
