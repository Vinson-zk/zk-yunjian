/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKDemoServletContextInitializer.java 
 * @author Vinson 
 * @Package com.zk.demo.configuration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 4:57:05 PM 
 * @version V1.0   
*/
package com.zk.demo.web.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.core.env.Environment;

import com.zk.core.web.support.servlet.filter.ZKTransferCipherFilter;

import jakarta.servlet.FilterRegistration.Dynamic;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;


/** 
* @ClassName: ZKDemoServletContextInitializer 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDemoServletContextInitializer implements ServletContextInitializer {

    @Autowired
    private Environment env;

    @Autowired
    private ZKTransferCipherFilter transferCipherFilter;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        String baseUrl = String.format("/%s", env.getProperty("zk.path.admin", "zk"));

        Dynamic filterDynamic = servletContext.addFilter("transfer-cipher-*", transferCipherFilter);
        filterDynamic.addMappingForUrlPatterns(null, false, baseUrl + "/cipher/*");
        System.out.println(
                "[^_^:20190625-1714-001] 添加传输加解密 filter：ZKTransferCipherFilter.class，path:" + baseUrl + "/cipher/*");
    }

}
