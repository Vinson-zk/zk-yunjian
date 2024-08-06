/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: JavaMethodAreaOOM.java 
* @author Vinson 
* @Package com.zk.demo.memory 
* @Description: TODO(simple description this file what to do.) 
* @date Jul 29, 2020 2:31:30 PM 
* @version V1.0 
*/
package com.zk.demo.memory;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodProxy;

import com.zk.demo.memory.HeapOOM.OOMObject;

/** 
* @ClassName: JavaMethodAreaOOM 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class JavaMethodAreaOOM {

    /**
     * 借助 CGLib 使方法区出现内存溢出异常
     * 
     * VM Args: -XX:PermSize=10m -XX:MaxPermSize=10m
     */

    public static void main(String[] args) {
        while (true) {
            Enhancer enhancer = new Enhancer();
            
            enhancer.setSuperclass(OOMObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new org.springframework.cglib.proxy.MethodInterceptor() {
                
                @Override
                public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                    return proxy.invokeSuper(obj, args);
                }
            });
            
            enhancer.create();

        }
    }


}
