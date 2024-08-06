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
* @Title: ZKJavaProcessorTest.java 
* @author Vinson 
* @Package com.zk.demo.reactor.java 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 21, 2024 11:24:51 PM 
* @version V1.0 
*/
package com.zk.demo.reactor.java;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKJavaProcessorTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKJavaProcessorTest {

    @Test
    public void testZKJavaProcessor() {
        try {

            System.out.println("[^_^:20240321-2333-001] -------------------------------");
            ZKJavaPublisher publisher = new ZKJavaPublisher();

            ZKJavaProcessor zkJavaProcessor = new ZKJavaProcessor();
            ZKJavaSubscriber zkJavaSubscriber = new ZKJavaSubscriber("sub");

            publisher.subscribe(zkJavaProcessor);
            zkJavaProcessor.subscribe(zkJavaSubscriber);

            publisher.submit("t1");
            publisher.submit("t2");
            publisher.submit("t3");
            publisher.close();

            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!(zkJavaSubscriber.isFinish())) {

                        System.out.println(
                                "[^_^:20240321-2250-001] zkJavaSubscriber.isFinish(): " + zkJavaSubscriber.isFinish());
                        try {
                            Thread.sleep(1500);
                        }
                        catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            t1.start();
            t1.join();
            System.out.println("[^_^:20240321-2333-001] ================================");

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
