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
* @Title: ZKStaticProxy.java 
* @author Vinson 
* @Package com.zk.demo.proxy.staticProxy 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 3, 2024 6:26:40 PM 
* @version V1.0 
*/
package com.zk.demo.proxy.staticProxy;

import com.zk.demo.proxy.ZKDoSomethingInterface;

/** 
* @ClassName: ZKStaticProxy 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKStaticProxy implements ZKDoSomethingInterface {

    ZKDoSomethingInterface ids = null;

    public ZKStaticProxy(ZKDoSomethingInterface ids) {
        this.ids = ids;
    }

    @Override
    public int doSomething(int param) {
        System.out.println("[^_^:20240803-1837-001] ZKStaticProxy doSomething ");
        int rev = ids.doSomething(param);
        System.out.println("[^_^:20240803-1837-001] ZKStaticProxy 执行被代理目标对象的方法结束：" + rev);
        return rev;
    }

}
