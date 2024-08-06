/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKDemoThreadPoolTest.java 
* @author Vinson 
* @Package com.zk.demo.multi.threads 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 5, 2024 6:33:23 PM 
* @version V1.0 
*/
package com.zk.demo.multi.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.zk.demo.ZKTestHelper.ZK;

import junit.framework.TestCase;

/** 
* @ClassName: ZKDemoThreadPoolTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDemoThreadPoolTest {

    @Test
    public void testThreadPoolExecutor() {
        try {
            List<Integer> res = new ArrayList<>();
            int count = 9;

            @SuppressWarnings("resource")
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, count, 0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());

            for (int i = 0; i < count; ++i) {
                int index = i;
                threadPoolExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        ZK.sleep("thrad1", Math.random());
                        ZK.p("[^_^:20240409-2345-002.02]", "FixedThreadPool.run.index: " + index);
                        res.add(index);
                    }
                });
                
                threadPoolExecutor.submit(new Callable<String>() {

                    @Override
                    public String call() throws Exception {
                        return "";
                    }
                });
            }
            // 等待线程池完成
            threadPoolExecutor.shutdown();
            threadPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            ZK.p("[^_^:20240409-2345-002.03]", "FixedThreadPool.count: " + count);
            int expected = 0, actual = 0;
            for (int i = 0; i < count; ++i) {
                expected += i;
            }
            for (int i : res) {
                actual += i;
            }
            TestCase.assertEquals(expected, actual);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testExecutorsFixedThreadPool() {
        try {
            List<Integer> res = new ArrayList<>();
            int count = 9;
            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(count);
            for (int i = 0; i < count; ++i) {
                int index = i;
                fixedThreadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        ZK.sleep("thrad1", Math.random());
                        ZK.p("[^_^:20240409-2345-002.02]", "FixedThreadPool.run.index: " + index);
                        res.add(index);
                    }
                });
            }
            // 等待线程池完成
            fixedThreadPool.shutdown();
            fixedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            ZK.p("[^_^:20240409-2345-002.03]", "FixedThreadPool.count: " + count);
            int expected = 0, actual = 0;
            for (int i = 0; i < count; ++i) {
                expected += i;
            }
            for (int i : res) {
                actual += i;
            }
            TestCase.assertEquals(expected, actual);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testFuture() {
        try {
            List<Integer> res = new ArrayList<>();
            int count = 3;
            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(count);

            List<Future<?>> resFuture = new ArrayList<>();
            for (int i = 0; i < count; ++i) {
                int index = i;
                Future<?> future = fixedThreadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        ZK.sleep("thrad1", Math.random());
                        ZK.p("[^_^:20240409-2345-002.02]", "testFixedThreadPoolFuture.run.index: " + index);
                        res.add(index);
                    }
                });
                resFuture.add(future);
                future = fixedThreadPool.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return "Callable: " + index;
                    }
                });
                resFuture.add(future);
            }
            // 等待线程池完成
            for (Future<?> f : resFuture) {
                Object obj = f.get();
                ZK.p("[^_^:20240409-2345-002.03]", "testFixedThreadPoolFuture.resFuture: " + obj);
            }

            ZK.p("[^_^:20240409-2345-002.04]", "testFixedThreadPoolFuture.count: " + count);
            ZK.p("[^_^:20240409-2345-002.04]", "testFixedThreadPoolFuture.resFuture.size(): " + resFuture.size());
            TestCase.assertEquals(count * 2, resFuture.size());
            int expected = 0, actual = 0;
            for (int i = 0; i < count; ++i) {
                expected += i;
            }
            for (int i : res) {
                actual += i;
            }
            TestCase.assertEquals(expected, actual);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
