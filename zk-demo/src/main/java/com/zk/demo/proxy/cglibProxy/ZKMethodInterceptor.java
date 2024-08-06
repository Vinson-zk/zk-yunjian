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
* @Title: ZKMethodInterceptor.java 
* @author Vinson 
* @Package com.zk.demo.proxy.cglibProxy 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 3, 2024 11:55:16 PM 
* @version V1.0 
*/
package com.zk.demo.proxy.cglibProxy;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/** 
* @ClassName: ZKMethodInterceptor 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMethodInterceptor implements MethodInterceptor {

    /**
     * 被代理的真实对象;
     */
    private Object target;

    public ZKMethodInterceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("[^_^:20240803-0919-001] ZKMethodInterceptor proxy:  " + proxy.getClass());
        System.out.println("[^_^:20240803-0919-001] ZKMethodInterceptor target: " + target.hashCode());
        System.out.println("[^_^:20240803-0919-001] ZKMethodInterceptor method：" + method.getName());
        System.out
                .println("[^_^:20240803-0919-001] ZKMethodInterceptor methodProxy：" + methodProxy.getSuperName());
        Object rev = method.invoke(target, args);
        System.out.println("[^_^:20240803-0919-001] ZKMethodInterceptor 执行被代理目标对象的方法结束：" + rev);
        return rev;
    }

}
