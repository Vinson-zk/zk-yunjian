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
* @Title: ZKDynamicProxyFactory.java 
* @author Vinson 
* @Package com.zk.demo.proxy.dynamicProxy 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 3, 2024 8:48:42 AM 
* @version V1.0 
*/
package com.zk.demo.proxy.dynamicProxy;

import java.lang.reflect.Proxy;

/** 
* @ClassName: ZKDynamicProxyFactory 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDynamicProxyFactory {

    @SuppressWarnings("unchecked")
    public <T> T getProxyInstance(Object target) {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
                new ZKInvocationHandler(target));
    }

}
