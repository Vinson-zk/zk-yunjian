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

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zk.log.interceptor.ZKLogAccessInterceptor;
import com.zk.mail.common.ZKMailAuthenticator;
import com.zk.mail.utils.ZKMailUtils;

/** 
* @ClassName: ZKMailAfterConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Configuration
public class ZKMailAfterConfiguration implements WebMvcConfigurer {

    @Value("${zk.mail.server.host}")
    String host;

    @Value("${zk.mail.server.port}")
    String port;

    @Value("${zk.mail.server.type}")
    String type;

    @Value("${zk.mail.server.account}")
    String account;

    @Value("${zk.mail.server.password}")
    String password;

    @Value("${zk.mail.server.validate}")
    boolean validate;

    @Autowired
    private ZKLogAccessInterceptor logAccessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.logAccessInterceptor).addPathPatterns("/**");
    }

    @Bean
    public ZKMailAuthenticator mailAuthenticator() {
        return ZKMailUtils.getMailAuthenticator(account, password);
    }

    @Bean
    public Properties mailProperties() {
        return ZKMailUtils.getProperties(host, port, type, validate);
    }

}
