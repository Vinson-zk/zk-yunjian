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
* @Title: ZKSpringAopProxyFactoryTest.java 
* @author Vinson 
* @Package com.zk.demo.proxy.springProxy 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 3, 2024 11:42:12 PM 
* @version V1.0 
*/
package com.zk.demo.proxy.springProxy;

import org.junit.Test;

import com.zk.demo.proxy.ZKDoSomething;
import com.zk.demo.proxy.ZKDoSomethingImpl;
import com.zk.demo.proxy.ZKDoSomethingInterface;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSpringAopProxyFactoryTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSpringAopProxyFactoryTest {

    @Test
    public void testInvocation() {
        try {
            ZKDoSomethingImpl dsImpl = new ZKDoSomethingImpl(3);
            ZKDoSomethingInterface dsProxyInterface = null;
            int result = 0;
            ZKSpringAopProxyFactory springAopProxyFactory = new ZKSpringAopProxyFactory();

            dsProxyInterface = springAopProxyFactory.getProxyInstanceInvocation(dsImpl);
            System.out.println("[^_^:20240803-2344-001] 代理对象类名：" + dsProxyInterface.getClass());
            result = dsProxyInterface.doSomething(1);
            TestCase.assertEquals(4, result);

//            // java.lang.ClassCastException: class org.springframework.cglib.proxy.Proxy$ProxyImpl$$EnhancerByCGLIB$$805bc317
//            dsProxy = springAopProxyFactory.getProxyInstanceInvocation(ds);
//            System.out.println("[^_^:20240803-2344-002] 代理对象类名：" + dsProxy.getClass());
//            result = dsProxy.doSomething(2);
//            TestCase.assertEquals(5, result);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testInterceptor() {
        try {
            ZKDoSomething ds = new ZKDoSomething(3);
            ZKDoSomething dsProxy = null;
            int result = 0;
            ZKSpringAopProxyFactory springAopProxyFactory = new ZKSpringAopProxyFactory();

            dsProxy = springAopProxyFactory.getProxyInstanceInterceptor(ds);
            System.out.println("[^_^:20240803-2344-001] 代理对象类名：" + dsProxy.getClass());
            result = dsProxy.doSomething(3);
            TestCase.assertEquals(6, result);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
