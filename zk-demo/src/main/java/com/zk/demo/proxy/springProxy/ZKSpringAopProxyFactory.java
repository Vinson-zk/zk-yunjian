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
* @Title: ZKSpringAopProxyFactory.java 
* @author Vinson 
* @Package com.zk.demo.proxy.springProxy 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 3, 2024 11:33:48 PM 
* @version V1.0 
*/
package com.zk.demo.proxy.springProxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.Proxy;

/** 
* @ClassName: ZKSpringAopProxyFactory 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSpringAopProxyFactory {

    /**
     * java 方式动态代理：被代理目标对象，需要实现接口
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxyInstanceInvocation(Object target) {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
                new ZKSpringInvocationHandler(target));
    }

    /**
     * cglib 方式动态代理：被代理目标对象，可不实现接口
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxyInstanceInterceptor(Object target) {
        Enhancer e = new Enhancer();
        e.setSuperclass(target.getClass());
        e.setCallback(new ZKSpringMethodInterceptor(target));
        return (T) e.create();
    }

}
