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
* @Title: ZKCglibProxyFactory.java 
* @author Vinson 
* @Package com.zk.demo.proxy.cglibProxy 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 3, 2024 9:15:30 AM 
* @version V1.0 
*/
package com.zk.demo.proxy.cglibProxy;

import net.sf.cglib.proxy.Enhancer;

/** 
* @ClassName: ZKCglibProxyFactory 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCglibProxyFactory {

    /**
     * 生成代理对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxyInstance(Object target) {
        // 工具类
        Enhancer en = new Enhancer();
        // 设置父类
        en.setSuperclass(target.getClass());
        // 设置回调函数
        en.setCallback(new ZKMethodInterceptor(target));
        // 创建子类对象代理
        return (T) en.create();
    }

}
