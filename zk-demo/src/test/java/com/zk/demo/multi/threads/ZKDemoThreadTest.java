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
* @Title: ZKDemoThreadTest.java 
* @author Vinson 
* @Package com.zk.demo.multi.threads 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 9, 2024 11:36:14 PM 
* @version V1.0 
*/
package com.zk.demo.multi.threads;

import org.junit.Test;

import com.zk.demo.ZKTestHelper.ZK;

import junit.framework.TestCase;

/**
 * @ClassName: ZKDemoThreadTest
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKDemoThreadTest {

    public static void main(String[] args) {
        ZKDemoThreadTest d = new ZKDemoThreadTest();
//        d.thrad1();
//        d.thrad2();
        d.thrad3();

    }

    @Test
    public void thrad3() {

        try {
            StringBuffer sb = new StringBuffer();

            ZK.p("[^_^:20240409-2345-001.01]", "thrad1");
            Thread t = new Thread() {
                @Override
                public void run() {
                    ZK.sleep("thrad1", Math.random());
                    ZK.p("[^_^:20240409-2345-001.02]", "thrad1.run");
                    sb.append("thrad3");
                }
            };
            t.start();
            t.join(); // 会等待 子线程先执行完
            TestCase.assertEquals("thrad3", sb.toString());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 守护进程，main 程序不会等待; main 调用才有效果；
    @Test
    public void thrad2() {
        try {
            StringBuffer sb = new StringBuffer();
            ZK.p("[^_^:20240409-2345-001.01]", "thrad2");
            Thread t = new Thread() {
                @Override
                public void run() {
                    ZK.sleep("thrad2", Math.random());
                    ZK.p("[^_^:20240409-2345-001.02]", "thrad2.run");
                    sb.append("thrad2");
                }
            };
            t.setDaemon(true);
            t.start();
            TestCase.assertEquals("", sb.toString());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void thrad1() {
        try {
            StringBuffer sb = new StringBuffer();

            ZK.p("[^_^:20240409-2345-001.01]", "thrad1");
            Thread t = new Thread() {
                @Override
                public void run() {
                    ZK.sleep("thrad1", Math.random());
                    ZK.p("[^_^:20240409-2345-001.02]", "thrad1.run");
                    sb.append("thrad1");
                }
            };
            t.start();
            TestCase.assertEquals("", sb.toString());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}


