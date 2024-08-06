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
* @Title: ZKDemoSynchronized.java 
* @author Vinson 
* @Package com.zk.demo.multi.threads 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 5, 2024 6:42:53 PM 
* @version V1.0 
*/
package com.zk.demo.multi.threads;

import java.util.ArrayList;
import java.util.List;

/** 
* @ClassName: ZKDemoSynchronized 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDemoSynchronized {


    List<String> resList = new ArrayList<>();

    public synchronized String step1() {
        System.out.println("[^_^:20240805-1844-001] ZKDemoSynchronized.step1 before");
        resList.add("step1");
        this.notifyAll();
        return "step1";
    }

    public synchronized String step2() {
        try {
            this.wait();
            System.out.println("[^_^:20240805-1844-002] ZKDemoSynchronized.step2 after");
            resList.add("step2");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return "step2";
    }
}
