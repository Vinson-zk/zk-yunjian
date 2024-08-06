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
* @Title: ZKCglibProxyFactoryTest.java 
* @author Vinson 
* @Package com.zk.demo.proxy.cglibProxy 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 3, 2024 11:56:56 PM 
* @version V1.0 
*/
package com.zk.demo.proxy.cglibProxy;

import org.junit.Test;

import com.zk.demo.proxy.ZKDoSomething;

import junit.framework.TestCase;

/** 
* @ClassName: ZKCglibProxyFactoryTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCglibProxyFactoryTest {

    /**
     * 需要在 jvm 启动时，配置参数：
     * 
     * --add-opens java.base/java.lang=ALL-UNNAMED
     * 
     * --add-opens java.base/sun.net.util=ALL-UNNAMED
     */
    @Test
    public void test() {
        try {
            ZKDoSomething ds = new ZKDoSomething(3);
            ZKDoSomething dsProxy = null;
            int result = 0;

            ZKCglibProxyFactory cpf = new ZKCglibProxyFactory();
            dsProxy = cpf.getProxyInstance(ds);
            System.out.println("[^_^:20240803-0921-001] 代理对象类名：" + ds.getClass());
            result = dsProxy.doSomething(7);
            TestCase.assertEquals(10, result);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
