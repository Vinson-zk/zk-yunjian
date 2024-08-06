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
* @Title: ZKJavaSubscriberTest.java 
* @author Vinson 
* @Package com.zk.demo.reactor.java 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 21, 2024 10:48:05 PM 
* @version V1.0 
*/
package com.zk.demo.reactor.java;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKJavaSubscriberTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKJavaSubscriberTest {

    @Test
    public void testZKJavaSubscriber() {
        try {
            System.out.println("[^_^:20240321-2250-001] -------------------------------");
            ZKJavaPublisher pub = new ZKJavaPublisher();
            ZKJavaSubscriber sub1 = new ZKJavaSubscriber("sub1");
            ZKJavaSubscriber sub2 = new ZKJavaSubscriber("sub2");

            pub.subscribe(sub1);
            pub.subscribe(sub2);

            pub.submit("data1");
            pub.submit("data2");
            pub.submit("data3");

            pub.close();

//            Thread.currentThread().join();
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!(sub1.isFinish() && sub1.isFinish())) {

                        System.out.println(
                                "[^_^:20240321-2250-001] sub.isFinish(): " + sub1.isFinish() + " " + sub2.isFinish());
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
            System.out.println("[^_^:20240321-2250-001] ================================");

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
