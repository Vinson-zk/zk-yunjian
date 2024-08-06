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
* @Title: ZKSecThreadContextTest.java 
* @author Vinson 
* @Package com.zk.security.thread 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 15, 2024 5:48:06 PM 
* @version V1.0 
*/
package com.zk.security.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSecThreadContextTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecThreadContextTest {

    public ExecutorService threadPool = Executors.newFixedThreadPool(10);

    final List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();

    final String key = "_key";

    final List<Integer> vListBefore = new ArrayList<Integer>();

    final List<Integer> vListAfter = new ArrayList<Integer>();

    public void putThreadLocal(int count) {
        for (int i = 0; i < count; ++i) {
            final int index = i;
            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("[^_^:20240415-2001-002.01] resRun: " + ZKSecThreadContext.get(key));
                    vListBefore.add((int) ZKSecThreadContext.get(key));
                    ZKSecThreadContext.put(key, index);
                    vListAfter.add((int) ZKSecThreadContext.get(key));
                    System.out.println("[^_^:20240415-2001-002.02] resRun: " + ZKSecThreadContext.get(key));
                    Map<Object, Object> resRun = ZKSecThreadContext.getResources();
                    list.add(resRun);
                    System.out.println("[^_^:20240415-2001-002.03] resRun: " + resRun.hashCode() + " ThreadName:"
                            + Thread.currentThread().getName());
                }
            });
        }
    }

    @Test
    public void test() {

        try {

            System.out.println("[^_^:20240415-2001-001.01] resMain: " + ZKSecThreadContext.get(key));
            ZKSecThreadContext.put(key, -1);
            vListBefore.add(-1);
            System.out.println("[^_^:20240415-2001-001.02] resMain: " + ZKSecThreadContext.get(key));
            Map<Object, Object> resMain = ZKSecThreadContext.getResources();
            list.add(resMain);
            System.out.println("[^_^:20240415-2001-001.03] resMain: " + resMain.hashCode() + " ThreadName:"
                    + Thread.currentThread().getName());

            int count = 3;
            this.putThreadLocal(count);

            // 等待线程池完成
            threadPool.shutdown();
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            vListAfter.add((int) ZKSecThreadContext.get(key));
            System.out.println("[^_^:20240415-2001-001.04] resMain: " + ZKSecThreadContext.get(key));
            resMain = ZKSecThreadContext.getResources();
            list.add(resMain);
            System.out.println("[^_^:20240415-2001-001.05] resMain: " + resMain.hashCode() + " ThreadName:"
                    + Thread.currentThread().getName());

            TestCase.assertEquals(count + 2, list.size());
            TestCase.assertEquals(count + 1, vListBefore.size());
            TestCase.assertEquals(count + 1, vListAfter.size());
            for (int i = 0; i < count; ++i) {
                TestCase.assertFalse(list.get(0).hashCode() == list.get(i + 1).hashCode());
                TestCase.assertTrue(vListBefore.get(0) == vListBefore.get(i + 1));
                TestCase.assertFalse(vListAfter.get(0) == vListAfter.get(i + 1));
            }
            TestCase.assertTrue(list.getFirst().hashCode() == list.getLast().hashCode());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
