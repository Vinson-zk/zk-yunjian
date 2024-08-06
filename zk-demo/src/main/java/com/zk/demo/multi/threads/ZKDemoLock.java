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
* @Title: ZKDemoLock.java 
* @author Vinson 
* @Package com.zk.demo.multi.threads 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 5, 2024 7:02:03 PM 
* @version V1.0 
*/
package com.zk.demo.multi.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/** 
* @ClassName: ZKDemoLock 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDemoLock {
    
    public static String s = "";

    static {
        synchronized (s) {
            System.out.println("");
        }
    }

    public static synchronized String tt() {
        return "";
    }

    Lock lock = new ReentrantLock();

    Condition condition = this.lock.newCondition();

    List<String> resList = new ArrayList<>();

    public String step1() {
        this.lock.lock();
        try {
            System.out.println("[^_^:20240805-1846-001] ZKDemoLock.step1 before");
            resList.add("step1");
            this.condition.signalAll();
            return "step1";
        } finally {
            this.lock.unlock();
        }
    }

    public String step2() {
        this.lock.lock();
        try {
            this.condition.await();
            System.out.println("[^_^:20240805-1846-002] ZKDemoLock.step2 after");
            resList.add("step2");
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            this.lock.unlock();
        }
        return "step2";
    }

}
