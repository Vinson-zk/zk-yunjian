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
* @Title: ZKSpringMethodInterceptor.java 
* @author Vinson 
* @Package com.zk.demo.proxy.springProxy 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 3, 2024 10:36:10 PM 
* @version V1.0 
*/
package com.zk.demo.proxy.springProxy;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * 
 * @ClassName: ZKSpringMethodInterceptor
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKSpringMethodInterceptor implements MethodInterceptor {

    /**
     * spring cglib 方式：被代理的真实对象;
     */
    private Object target;

    public ZKSpringMethodInterceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("[^_^:20240803-2240-001] ZKSpringMethodInterceptor proxy：" + proxy.getClass());
//        System.out.println("[^_^:20240803-2240-001] ZKSpringMethodInterceptor methodProxy：" + proxy.getClass());
        System.out.println("[^_^:20240803-2240-001] ZKSpringMethodInterceptor method：" + method.getName());
        System.out
                .println("[^_^:20240803-2240-001] ZKSpringMethodInterceptor methodProxy：" + methodProxy.getSuperName());
        Object rev = method.invoke(this.target, args);
//        Object rev = methodProxy.invokeSuper(this.target, args);
        System.out.println("[^_^:20240803-2240-001] ZKSpringMethodInterceptor 执行被代理目标对象的方法结束：" + rev);
        return rev;

    }

}
