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
* @Title: ZKWebmvcServletContextInitializer.java 
* @author Vinson 
* @Package com.zk.webmvc.servlet.context 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 22, 2024 6:43:21 PM 
* @version V1.0 
*/
package com.zk.webmvc.servlet.context;

import org.springframework.boot.web.servlet.ServletContextInitializer;

import com.zk.webmvc.configuration.ZKEnableWebmvc;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

/**
 * 未使用
 * 
 * @ClassName: ZKWebmvcServletContextInitializer
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKWebmvcServletContextInitializer implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println(ZKEnableWebmvc.printLog + "ServletContextInitializer --- [" + this.getClass().getSimpleName()
                + "] " + this);
        System.out.println(ZKEnableWebmvc.printLog + "----- servletContext.class: " + servletContext.getClass());

    }

}
