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
* @Title: ZKDynamicProxyFactoryTest.java 
* @author Vinson 
* @Package com.zk.demo.proxy.dynamicProxy 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 3, 2024 11:47:22 PM 
* @version V1.0 
*/
package com.zk.demo.proxy.dynamicProxy;

import org.junit.Test;

import com.zk.demo.proxy.ZKDoSomethingImpl;
import com.zk.demo.proxy.ZKDoSomethingInterface;

import junit.framework.TestCase;

/** 
* @ClassName: ZKDynamicProxyFactoryTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDynamicProxyFactoryTest {

    @Test
    public void test() {
        try {
            ZKDoSomethingImpl dsImpl = new ZKDoSomethingImpl(3);
            ZKDoSomethingInterface dsProxy = null;
            int result = 0;

            ZKDynamicProxyFactory dpf = new ZKDynamicProxyFactory();
            dsProxy = dpf.getProxyInstance(dsImpl);
            System.out.println("[^_^:20240803-0839-001] 代理对象类名：" + dsProxy.getClass());
            result = dsProxy.doSomething(2);
            TestCase.assertEquals(5, result);

//            // ZKDoSomething 没实现接口，报错：java.lang.ClassCastException: class jdk.proxy2.$Proxy8 cannot be cast to class com.zk.demo.proxy.ZKDoSomething ... ...
//            ZKDoSomething ds = new ZKDoSomething(3);
//            dpf = new ZKDynamicProxyFactory();
//            ds = dpf.getProxyInstance(ds);
//            System.out.println("[^_^:20240803-0839-002] 代理对象类名：" + ds.getClass());
//            ds.doSomething(1);
//            TestCase.assertEquals(4, result);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
