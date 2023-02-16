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
* @Title: ZKJedisOperatorByteTest.java 
* @author Vinson 
* @Package com.zk.core.redis 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 16, 2021 4:26:42 PM 
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
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

/** 
* @ClassName: ZKJedisOperatorByteTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKJedisOperatorByteTest {

    /********************************************************************************************************/
    /** key 键值对 测试 */
    /********************************************************************************************************/
    @Test
    public void testKey() {

        ConfigurableApplicationContext ctx = ZKCoreTestHelperSpringBootMain.run(new String[] {});
        TestCase.assertNotNull(ctx);
        ZKJedisOperatorByte jedisOperatorByte = ctx.getBean(ZKJedisOperatorByte.class);
        TestCase.assertNotNull(jedisOperatorByte);

        try {

            byte[] jedisKey = "test_jedis_key_core".getBytes(), jedisKey2 = "test_jedis_key_core_2".getBytes();
            byte[] pattern = "*jedis_key*".getBytes();
            Set<byte[]> keys = null;
            String valueStr = "valueStr", resStr;
            Integer valueInt = 2, resInt;
            ZKRedisTestEntity obj = null, resObj;
            // 超期时间，秒
            int seconds = 2;

            /* 设置与取值 */
            jedisOperatorByte.set(jedisKey, valueStr.getBytes(), seconds);
            resStr = new String(jedisOperatorByte.get(jedisKey));
            TestCase.assertEquals(valueStr, resStr);
            // 基础类型 int
            jedisOperatorByte.set(jedisKey, ZKRedisUtils.toValueBytes(valueInt));
            resInt = ZKRedisUtils.toValueObject(jedisOperatorByte.get(jedisKey));
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
            jedisOperatorByte.set(jedisKey, ZKRedisUtils.toValueBytes(obj));
            resObj = ZKRedisUtils.toValueObject(jedisOperatorByte.get(jedisKey));
            TestCase.assertEquals(obj.getV1(), resObj.getV1());
            TestCase.assertEquals(obj.getV3().getV2(), resObj.getV3().getV2());
            TestCase.assertEquals(obj.getV3().getV3().getV1(), resObj.getV3().getV3().getV1());
            /* 是否存在 */
            TestCase.assertTrue(jedisOperatorByte.exists(jedisKey));
            /* 过期 */
            jedisOperatorByte.expire(jedisKey, 1);
            Thread.sleep(1000);
            TestCase.assertNull(jedisOperatorByte.get(jedisKey));
            /* key 配置 */
            jedisOperatorByte.set(jedisKey, jedisKey);
            jedisOperatorByte.set(jedisKey2, jedisKey2);
            keys = jedisOperatorByte.keys(pattern);
            System.out.println("[^_^:20210812-1634-001] byte ks.size:" + keys.size());
            TestCase.assertEquals(2, keys.size());
            for (byte[] k : keys) {
                System.out.println("[^_^:20210812-1634-002-k]:" + new String(k));
            }
            /* 删除 */
            jedisOperatorByte.set(jedisKey, valueStr.getBytes());
            jedisOperatorByte.del(jedisKey);
            TestCase.assertNull(jedisOperatorByte.get(jedisKey));
            jedisOperatorByte.del(jedisKey2);
            TestCase.assertNull(jedisOperatorByte.get(jedisKey2));
            System.out.println("[^_^:20210816-1654-001] key *************************************************");

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testCursor() {

        ConfigurableApplicationContext ctx = ZKCoreTestHelperSpringBootMain.run(new String[] {});
        TestCase.assertNotNull(ctx);
        ZKJedisOperatorByte jedisOperatorByte = ctx.getBean(ZKJedisOperatorByte.class);
        TestCase.assertNotNull(jedisOperatorByte);

        try {
            /* 测试遍历查询 */
            int keyCount = 23;
            String key = "scankey";
            for (int i = 0; i < keyCount; ++i) {
                jedisOperatorByte.set(("adfad_" + key + "_test" + i).getBytes(), key.getBytes());
            }
            Jedis j = jedisOperatorByte.getResource();

            ScanParams scanParams = new ScanParams();
            scanParams.match("*" + key + "*".getBytes());
            scanParams.count(10);

            ScanResult<String> res = j.scan("0", scanParams);
//            System.out.println("------- " + res.getCursor());
            System.out.println("------- " + res.getCursor());
            System.out.println("------- " + new String(res.getCursorAsBytes()));
            System.out.println("------- " + res.getResult().size());

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
    public void testHash() {

        try {
            ConfigurableApplicationContext ctx = ZKCoreTestHelperSpringBootMain.run(new String[] {});
            TestCase.assertNotNull(ctx);
            ZKJedisOperatorByte jedisOperatorByte = ctx.getBean(ZKJedisOperatorByte.class);
            TestCase.assertNotNull(jedisOperatorByte);

            byte[] key = "test_testTest_hash_ssdf".getBytes();
            byte[] field1 = "test_field1_adfaf1".getBytes(), field2 = "test_field2_adfaf1".getBytes();
            String value1 = "test_value_1", value2 = "test_value_2", resStr;
            Integer valueInt = 2, resInt;
            ZKRedisTestEntity obj = null, resObj;

            Set<byte[]> keys = null;
            List<byte[]> values = null;
            Map<byte[], byte[]> hashMap;

            // 超期时间，秒
            int seconds = 2;

            /* 设置与取值 */
            jedisOperatorByte.hset(key, field1, value1.getBytes(), seconds);
            resStr = new String(jedisOperatorByte.hget(key, field1));
            TestCase.assertEquals(new String(value1), resStr);

            // 基础类型 int
            jedisOperatorByte.hset(key, field2, ZKRedisUtils.toValueBytes(valueInt));
            resInt = ZKRedisUtils.toValueObject(jedisOperatorByte.hget(key, field2));
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
            jedisOperatorByte.hset(key, field2, ZKRedisUtils.toValueBytes(obj));
            resObj = ZKRedisUtils.toValueObject(jedisOperatorByte.hget(key, field2));
            TestCase.assertEquals(obj.getV1(), resObj.getV1());
            TestCase.assertEquals(obj.getV3().getV2(), resObj.getV3().getV2());
            TestCase.assertEquals(obj.getV3().getV3().getV1(), resObj.getV3().getV3().getV1());
            /* 取全部值 */
            hashMap = jedisOperatorByte.hget(key);
            TestCase.assertNotNull(hashMap);
            TestCase.assertEquals(2, hashMap.size());

            /* field 是否存在 */
            TestCase.assertTrue(jedisOperatorByte.hexists(key, field1));

            /* hash 键匹配 */
            jedisOperatorByte.hset(key, field1, value1.getBytes(), seconds);
            jedisOperatorByte.hset(key, field2, value2.getBytes(), seconds);

            // 所有 field
            keys = jedisOperatorByte.hkeys(key);
            System.out.println("[^_^:20210813-1042-001] byte[] keys.size:" + keys.size());
            TestCase.assertEquals(2, keys.size());
            for (byte[] k : keys) {
                System.out.println("[^_^:20210813-1042-002-byte key]:" + new String(k));
            }

            // 所有 value
            values = jedisOperatorByte.hValues(key);
            System.out.println("[^_^:20210813-1042-001] byte[] values.size:" + values.size());
            TestCase.assertEquals(2, values.size());
            for (byte[] v : values) {
                System.out.println("[^_^:20210813-1042-002-vByte]:" + new String(v));
            }

            /* 删除 */
            jedisOperatorByte.hdel(key, field1);
            TestCase.assertNull(jedisOperatorByte.hget(key, field1));

            System.out.println("[^_^:20210816-1654-001] hash *************************************************");

            jedisOperatorByte.del(key);
            TestCase.assertNull(jedisOperatorByte.get(key));
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /********************************************************************************************************/
    /** set 集合 测试 */
    /********************************************************************************************************/
    @Test
    public void testSet() {

        try {
            ConfigurableApplicationContext ctx = ZKCoreTestHelperSpringBootMain.run(new String[] {});
            TestCase.assertNotNull(ctx);
            ZKJedisOperatorByte jedisOperatorByte = ctx.getBean(ZKJedisOperatorByte.class);
            TestCase.assertNotNull(jedisOperatorByte);

            byte[] key = "test_testTest_ssdf".getBytes();
            String value1 = "test_value_1", value2 = "test_value_2";
            Integer valueInt = 2;
            ZKRedisTestEntity obj = null, resObj;

            Set<byte[]> values = null;
            Long resLong;

            // 超期时间，秒
            int seconds = 2;

            /* 设置与取值 */
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
            
            jedisOperatorByte.sAdd(key, seconds, value1.getBytes(), value2.getBytes(),
                    ZKRedisUtils.toValueBytes(valueInt),
                    ZKRedisUtils.toValueBytes(obj));
            values = jedisOperatorByte.sget(key);
            TestCase.assertEquals(4, values.size());

            /* 移除值 */
            resLong = jedisOperatorByte.sremove(key, value1.getBytes(), ZKRedisUtils.toValueBytes(valueInt),
                    value2.getBytes());
            TestCase.assertEquals(3, resLong.intValue());

            values = jedisOperatorByte.sget(key);
            TestCase.assertEquals(1, values.size());
            resObj = ZKRedisUtils.toValueObject(values.iterator().next());
            TestCase.assertEquals(obj.getV1(), resObj.getV1());
            TestCase.assertEquals(obj.getV3().getV2(), resObj.getV3().getV2());
            TestCase.assertEquals(obj.getV3().getV3().getV1(), resObj.getV3().getV3().getV1());

            /* 测试 set 中 对象是否可以覆盖； */
            jedisOperatorByte.sAdd(key, ZKRedisUtils.toValueBytes(resObj));
            values = jedisOperatorByte.sget(key);
            TestCase.assertEquals(1, values.size());

            System.out.println("[^_^:20210816-1654-001] hash *************************************************");

            jedisOperatorByte.del(key);
            TestCase.assertNull(jedisOperatorByte.get(key));
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }
}
