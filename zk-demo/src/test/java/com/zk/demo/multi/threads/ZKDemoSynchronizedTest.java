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
* @Title: ZKDemoSynchronizedTest.java 
* @author Vinson 
* @Package com.zk.demo.multi.threads 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 5, 2024 6:48:54 PM 
* @version V1.0 
*/
package com.zk.demo.multi.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKDemoSynchronizedTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDemoSynchronizedTest {

    @Test
    public void testSynchronizedWait() {
        try {
            ZKDemoSynchronized demoSynchronized = new ZKDemoSynchronized();
            ExecutorService es;
            es = Executors.newFixedThreadPool(2);
            es.submit(() -> demoSynchronized.step2());
            es.submit(() -> demoSynchronized.step1());
            // 等待线程池完成
            es.shutdown();
            es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            TestCase.assertEquals("step1", demoSynchronized.resList.get(0));
            TestCase.assertEquals("step2", demoSynchronized.resList.get(1));
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
