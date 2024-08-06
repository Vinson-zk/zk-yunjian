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
* @Title: ZKDoSomething.java 
* @author Vinson 
* @Package com.zk.demo.proxy 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 3, 2024 9:23:10 AM 
* @version V1.0 
*/
package com.zk.demo.proxy;
/** 
* @ClassName: ZKDoSomething 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDoSomething { // 没有实现接口

    private int mP;

    public ZKDoSomething() {
        this.mP = 11;
    }

    public ZKDoSomething(int mP) {
        this.mP = mP;
    }

    public int doSomething(int param) {
        System.out.println("[^_^:20240803-0920-001] ZKDoSomething.DoSomething 给参数 param：" + param + " 增加：" + this.mP);
        param = param + this.mP;
        return param;
    }

}
