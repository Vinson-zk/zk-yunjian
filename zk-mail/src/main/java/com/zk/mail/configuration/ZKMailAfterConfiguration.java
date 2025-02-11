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
* @Title: ZKMailAfterConfiguration.java 
* @author Vinson 
* @Package com.zk.mail.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date May 25, 2022 2:21:20 AM 
* @version V1.0 
*/
package com.zk.mail.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zk.core.configuration.ZKEnableCoreServlet;
import com.zk.core.redis.configuration.ZKEnableRedis;
import com.zk.db.configuration.ZKEnableDB;
import com.zk.log.configuration.ZKEnableLog;
import com.zk.security.configuration.ZKEnableSecurity;
import com.zk.webmvc.configuration.ZKEnableWebmvc;

import jakarta.servlet.MultipartConfigElement;

/** 
* @ClassName: ZKMailAfterConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKEnableCoreServlet
@ZKEnableLog
@ZKEnableDB
@ZKEnableRedis
@ZKEnableSecurity
@ZKEnableWebmvc
public class ZKMailAfterConfiguration implements WebMvcConfigurer {


    @Value("${zk.core.servlet.file.upload.multipartconfigelement.tempFilePath:tmp/spittr/uploads}")
    String tempFilePath;

    @Value("${zk.core.servlet.file.upload.multipartconfigelement.maxFileSize}")
    long maxFileSize;

    @Value("${zk.core.servlet.file.upload.multipartconfigelement.maxRequestSize}")
    long maxRequestSize;

    @Autowired
    public void dispatcherServletRegistrationBean(DispatcherServletRegistrationBean registration) {
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(tempFilePath, maxFileSize,
                maxRequestSize, 0);
        registration.setMultipartConfig(multipartConfigElement);
    }

}
