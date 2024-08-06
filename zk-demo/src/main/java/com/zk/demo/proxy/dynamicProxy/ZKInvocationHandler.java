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
* @Title: ZKInvocationHandler.java 
* @author Vinson 
* @Package com.zk.demo.proxy.dynamicProxy 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 3, 2024 11:45:55 PM 
* @version V1.0 
*/
package com.zk.demo.proxy.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/** 
* @ClassName: ZKInvocationHandler 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKInvocationHandler implements InvocationHandler {

    /**
     * 被代理的真实对象;
     * 
     * 必须实现一个或多个接口
     */
    private Object target;

    public ZKInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("[^_^:20240803-0855-001] ZKInvocationHandler proxy：" + proxy.getClass());
        System.out.println("[^_^:20240803-0855-001] ZKInvocationHandler method：" + method.getName());
        Object rev = method.invoke(this.target, args);
        System.out.println("[^_^:20240803-0855-001] ZKInvocationHandler 执行被代理目标对象的方法结束：" + rev);
        return rev;
    }

}
