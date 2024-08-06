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
* @Title: ZKJedisTest.java 
* @author Vinson 
* @Package com.zk.demo.redis 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 12, 2021 4:41:47 PM 
* @version V1.0 
*/
package com.zk.demo.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKObjectUtils;

import junit.framework.TestCase;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/** 
* @ClassName: ZKJedisTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKJedisTest {

    @Test
    public void test() {
        JedisPool jedisPool = null;
        Jedis jedis = null;
        try {
            ConfigurableApplicationContext ctx = ZKRedisTestHelperSpringBootMain.run(new String[] {});
            TestCase.assertNotNull(ctx);
            jedisPool = ctx.getBean("jedisPool2", JedisPool.class);
            TestCase.assertNotNull(jedisPool);

            String key = "test_key";
            String value = "test_value_1";
            String resValue;
            // 超期时间，秒
            int seconds = 2;
            jedis = jedisPool.getResource();

            System.out.println("*********************************************************");
            System.out.println("*** 其他");
            /*** 其他 ***/
            jedis.del(key);
            /*
             * @param 第一个参数：键 key；
             * 
             * @param 第二个参数：值 value
             * 
             * @param 第三个参数：设置操作方式：NX-仅在key不存时创建；XX-仅在key存在时修改值；
             * 
             * @param 第四个参数：过期时间单位；EX-秒seconds; PX-毫秒milliseconds
             * 
             * @param 第五个参数：过期时间
             */
            resValue = jedis.set(key, value);
            jedis.expire(key, seconds);
            System.out.println("[^_^:20210811-0832-001] key: " + key + "; 第一次设置值，结果：" + resValue);
            resValue = jedis.set(key, value);
            jedis.expire(key, seconds);
            System.out.println("[^_^:20210811-0832-001] key: " + key + "; 第二次设置值，结果：" + resValue);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Test
    public void testJedis() {

        JedisPool jedisPool = null;
        Jedis jedis = null;

        try {
            ConfigurableApplicationContext ctx = ZKRedisTestHelperSpringBootMain.run(new String[] {});
            TestCase.assertNotNull(ctx);
            jedisPool = ctx.getBean("jedisPool", JedisPool.class);
            TestCase.assertNotNull(jedisPool);

            String key = "test_key", hKey = "test_hKey", key2 = "test_key_2";
            String attr1 = "test_attr_1", attr2 = "test_attr_2";
            String value1 = "test_value_1", value2 = "test_value_2";
            String resValue;
            Map<String, String> resMap;
            // 超期时间，秒
            int seconds = 2;
            long sleepSeconds = 1500;

            jedis = jedisPool.getResource();

            /*********************************************************/
            System.out.println("*********************************************************");
            System.out.println("*** 一般操作");
            /*** 一般操作 */
            jedis.hset(hKey, attr1, value1);
            jedis.hset(hKey, attr2, value2);
            jedis.set(key, value1);

            resValue = jedis.get(key);
            TestCase.assertEquals(value1, resValue);
            resValue = jedis.hget(hKey, attr2);
            TestCase.assertEquals(value2, resValue);

            /***  ***/
            resMap = jedis.hgetAll(hKey);
            System.out.println("[^_^:20210809-1739-001] resMap：" + ZKJsonUtils.toJsonStr(resMap));

            /*********************************************************/
            System.out.println("*********************************************************");
            System.out.println("*** 测试过期");
            /*** 测试过期 */
            // 单位秒;
            jedis.set(key2, value2);
            resValue = jedis.get(key2);
            TestCase.assertEquals(value2, resValue);
            jedis.expire(key2, seconds);
            jedis.expire(key, seconds);
            jedis.expire(hKey, seconds);
            long beginTime = System.currentTimeMillis();
            System.out.println("[^_^:20210809-1628-001] 程序休眠开始: " + sleepSeconds + " 毫秒；" + beginTime);
            Thread.sleep(sleepSeconds);
            // 更新过期时间
            jedis.expire(key2, seconds);
            long endTime = System.currentTimeMillis();
            System.out.println("[^_^:20210809-1628-001] 程序休眠结束: " + (endTime - beginTime) + " 毫秒；" + endTime);
            System.out.println("[^_^:20210809-1628-001] 更新 key2 过期时间");
            beginTime = System.currentTimeMillis();
            System.out.println("[^_^:20210809-1628-001] 程序休眠开始: " + sleepSeconds + " 毫秒；" + beginTime);
            Thread.sleep(sleepSeconds);
            endTime = System.currentTimeMillis();
            System.out.println("[^_^:20210809-1628-001] 程序休眠结束: " + (endTime - beginTime) + " 毫秒；" + endTime);

            resValue = jedis.get(key2);
            TestCase.assertEquals(value2, resValue);
            resValue = jedis.get(key);
            TestCase.assertNull(resValue);
            resValue = jedis.hget(hKey, attr1);
            TestCase.assertNull(resValue);

            /*********************************************************/
            System.out.println("*********************************************************");
            System.out.println("*** key 是否存在");
            /*** key 是否存在 ***/
//            jedis.exists(key)；
            /*********************************************************/
            System.out.println("*********************************************************");
            System.out.println("*** 事务处理");
            /*** 事务处理 ***/
//            Transaction tx = jedis.multi();
//            tx.exec();
            /*********************************************************/
            System.out.println("*********************************************************");
            System.out.println("*** 其他");
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            System.out
                    .println("[^_^:20210809-1604-001] NumActive: " + jedisPool.getNumActive());
            if (jedis != null) {
                jedis.close();
            }
            System.out
                    .println("[^_^:20210809-1604-002] NumActive: " + jedisPool.getNumActive());
        }
    }

    /*** 测试键匹配 */
    @Test
    public void testJedisKeyPattern() {

        JedisPool jedisPool = null;
        Jedis jedis = null;

        try {
            ConfigurableApplicationContext ctx = ZKRedisTestHelperSpringBootMain.run(new String[] {});
            TestCase.assertNotNull(ctx);
            jedisPool = ctx.getBean("jedisPool", JedisPool.class);
            TestCase.assertNotNull(jedisPool);

            String key = "test_testTest_ssdf";
            String value1 = "test_value_1";
            String resValue;
            String pattern = "*testTest*";

            Set<byte[]> byteKeys = null;
            Set<String> strKeys = null;

            jedis = jedisPool.getResource();

            /*********************************************************/
            System.out.println("*********************************************************");
            System.out.println("*** key 测试键匹配");

            // 使用 byte[] 做为 key
            jedis.set(ZKObjectUtils.serialize(key), ZKObjectUtils.serialize(value1));
            // 使用 byte[] key 取值
            resValue = (String) ZKObjectUtils.unserialize(jedis.get(ZKObjectUtils.serialize(key)));
            TestCase.assertEquals(value1, resValue);
            // 使用 String key 取值
            resValue = jedis.get(key);
            TestCase.assertNull(resValue);

            /*** 测试键匹配 */
            byteKeys = jedis.keys(pattern.getBytes());
            System.out.println("[^_^:20210812-1634-001] byte byteKeys.size:" + byteKeys.size());
            TestCase.assertEquals(1, byteKeys.size());
            for (byte[] k : byteKeys) {
                System.out.println("[^_^:20210812-1634-001-byteKey]:" + ZKObjectUtils.unserialize(k));
            }

            strKeys = jedis.keys(pattern);
            TestCase.assertEquals(1, strKeys.size());
            System.out.println("[^_^:20210812-1634-002] String strKeys.size:" + strKeys.size());
            for (String k : strKeys) {
                System.out.println("[^_^:20210812-1634-002-strKey]:" + k);
            }

            System.out.println("[^_^:20210812-1634-003] btye-value: "
                    + ZKObjectUtils.unserialize(jedis.get(ZKObjectUtils.serialize(key))));
            System.out.println("[^_^:20210812-1634-003] String-value: " + jedis.get(key));

            ////
            jedis.del(ZKObjectUtils.serialize(key));
            jedis.del(key);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Test
    public void testJedisHashKey() {

        JedisPool jedisPool = null;
        Jedis jedis = null;

        try {
            ConfigurableApplicationContext ctx = ZKRedisTestHelperSpringBootMain.run(new String[] {});
            TestCase.assertNotNull(ctx);
            jedisPool = ctx.getBean("jedisPool", JedisPool.class);
            TestCase.assertNotNull(jedisPool);

            String key = "test_testTest_ssdf";
            String field1 = "test_field1_adfaf1", field2 = "test_field2_adfaf1";
            String value1 = "test_value_1", value2 = "test_value_2";

            Set<byte[]> byteKeys = null;
            Set<String> strKeys = null;
            List<byte[]> byteValues = null;
            List<String> strValues = null;

            jedis = jedisPool.getResource();

            /*********************************************************/
            System.out.println("*********************************************************");
            System.out.println("*** hash key 测试键匹配");

            jedis.hset(ZKObjectUtils.serialize(key), ZKObjectUtils.serialize(field1), ZKObjectUtils.serialize(value1));
            jedis.hset(ZKObjectUtils.serialize(key), ZKObjectUtils.serialize(field2), ZKObjectUtils.serialize(value2));

            byteKeys = jedis.hkeys(ZKObjectUtils.serialize(key));
            System.out.println("[^_^:20210813-0939-001] byte byteKeys.size:" + byteKeys.size());
            TestCase.assertEquals(2, byteKeys.size());
            for (byte[] k : byteKeys) {
                System.out.println("[^_^:20210813-0939-001-byteKey]:" + ZKObjectUtils.unserialize(k));
            }

            strKeys = jedis.hkeys(key);
            System.out.println("[^_^:20210813-0939-002] String strKeys.size:" + strKeys.size());
            TestCase.assertEquals(0, strKeys.size());

            byteValues = jedis.hvals(ZKObjectUtils.serialize(key));
            System.out.println("[^_^:20210813-0939-002] value byteValues.size:" + byteValues.size());
            TestCase.assertEquals(2, byteValues.size());
            for (byte[] k : byteValues) {
                System.out.println("[^_^:20210813-0939-001-value]:" + ZKObjectUtils.unserialize(k));
            }
            strValues = jedis.hvals(key);
            System.out.println("[^_^:20210813-0939-002] value strValues.size:" + strValues.size());
            TestCase.assertEquals(0, strValues.size());

            ////
            jedis.del(ZKObjectUtils.serialize(key));
            jedis.del(key);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    // 测试多线程 increment
    @Test
    public void testJedisIncrement() {

        ConfigurableApplicationContext ctx = ZKRedisTestHelperSpringBootMain.run(new String[] {});
        TestCase.assertNotNull(ctx);
        final JedisPool jedisPool = ctx.getBean("jedisPool", JedisPool.class);
        TestCase.assertNotNull(jedisPool);
        final Jedis jedis = jedisPool.getResource();

        try {
            final String jedisKey = "test_jedis_key_increment";
            // 超期时间，秒
            final int seconds = 5;
            final int threadCount = 800;
            final Map<String, String> resJedisMap = new HashMap<>();

            ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
//            threadPool = Executors.newSingleThreadExecutor();
            jedis.select(2);
            jedis.del(jedisKey);

            for (long i = 0; i < threadCount; ++i) {
                String index = String.valueOf(i);
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        Jedis jedis = jedisPool.getResource();
                        jedis.select(2);
                        @SuppressWarnings("deprecation")
                        long threadId = Thread.currentThread().getId();
                        String str = jedis.set(jedisKey, "v");
                        jedis.expire(jedisKey, seconds);
                        resJedisMap.put(index + ":Thread.id=" + threadId, str);
                        jedis.close();
                        return;
                    }
                });
            }

            try {
                threadPool.shutdown();
                threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                System.out.println("all thread complete");
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }

//            jedisClose.expire(jedisKey, seconds);
            System.out.println("[^_^:20210910-1522-001] resJedisMap.size: " + resJedisMap.size());
            System.out.println("[^_^:20210910-1522-001] resJedisMap:" + ZKJsonUtils.toJsonStr(resJedisMap));
            int count1 = 0;

            count1 = 0;
            for (Entry<String, String> e : resJedisMap.entrySet()) {
                if ("OK".equals(e.getValue())) {
                    ++count1;
                    System.out.println("[^_^:20210910-1522-002] resJedisMap 第一个插入的线程为：" + e.getKey());
                }
            }
            TestCase.assertEquals(1, count1);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}
