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
* @Title: ZKDoSomethingImpl.java 
* @author Vinson 
* @Package com.zk.demo.proxy 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 3, 2024 8:38:08 AM 
* @version V1.0 
*/
package com.zk.demo.proxy;
/** 
* @ClassName: ZKDoSomethingImpl 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDoSomethingImpl implements ZKDoSomethingInterface { // 有实现接口

    private int mP;

    public ZKDoSomethingImpl(int mP) {
        this.mP = mP;
    }

    @Override
    public int doSomething(int param) {
        System.out
                .println("[^_^:20240803-0838-001] ZKDoSomethingImpl.DoSomething 给参数 param：" + param + " 增加：" + this.mP);
        param = this.mP + param;
        return param;
    }

}
