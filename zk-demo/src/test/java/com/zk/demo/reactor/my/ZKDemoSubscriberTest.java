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
* @Title: ZKDemoSubscriberTest.java 
* @author Vinson 
* @Package com.zk.demo.reactor 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 22, 2024 5:25:45 PM 
* @version V1.0 
*/
package com.zk.demo.reactor.my;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKDemoSubscriberTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDemoSubscriberTest {

    @Test
    public void testZKDemoSubscriber() {
        try {
            System.out.println("[^_^:20240322-2250-001] -------------------------------");
            ZKDemoPublisher pub = new ZKDemoPublisher();
            ZKDemoSubscriber sub1 = new ZKDemoSubscriber("sub1");
            ZKDemoSubscriber sub2 = new ZKDemoSubscriber("sub2");

            pub.subscribe(sub1);
            pub.subscribe(sub2);

            pub.put("data1");
            pub.put("data2");
            pub.put("data3");

            pub.close();

//            Thread.currentThread().join();
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!(sub1.isFinish() && sub1.isFinish())) {

                        System.out.println(
                                "[^_^:20240322-2250-001] sub.isFinish(): " + sub1.isFinish() + " " + sub2.isFinish());
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
            System.out.println("[^_^:20240322-2250-001] ================================");

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
