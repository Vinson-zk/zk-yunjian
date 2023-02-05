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
 * @Title: ZKWebmvcChildConfiguration.java 
 * @author Vinson 
 * @Package com.zk.core.helper.configuration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 12, 2019 5:15:38 PM 
 * @version V1.0   
*/
package com.zk.webmvc.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.zk.core.utils.ZKLocaleUtils;
import com.zk.core.utils.ZKUtils;
import com.zk.webmvc.resolver.ZKSessionLocaleResolver;

/**
 * @ClassName: ZKWebmvcChildConfiguration
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
//@Configuration
//@PropertySources(value = { @PropertySource(value = {
//        "classpath:test-application.properties" }, ignoreResourceNotFound = true, encoding = "UTF-8") })
@ImportResource(locations = { "classpath:xmlConfig/test_spring_mvc_context.xml" })
@ImportAutoConfiguration(classes = { DispatcherServletAutoConfiguration.class,
        ServletWebServerFactoryAutoConfiguration.class })
@ComponentScan(basePackages = { "com.zk.webmvc.helper.controller" })
@AutoConfigureAfter(value = { ZKWebmvcParentConfiguration.class })
public class ZKWebmvcChildConfiguration {

    @Autowired
    Environment env;

//    @Bean
//    public ConfigurableServletWebServerFactory webServerFactory() {
//        TomcatServletWebServerFactory tomcatFactory = new TomcatServletWebServerFactory();
//        int zkCoreServerPort = env.getProperty("zk.core.server.port", Integer.class, 8080);
//        tomcatFactory.setPort(zkCoreServerPort);
//        return tomcatFactory;
//    }

    @Bean
    @DependsOn(value = { "messageConverters" })
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter(HttpMessageConverters messageConverters) {
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        // session 线程安全配置
        requestMappingHandlerAdapter.setSynchronizeOnSession(
                ZKUtils.isTrue(env.getProperty("zk.core.web.mvc.synchronizeOnSession", String.class, "true")));
        // 一定要在这些设置 HttpMessageConverters 因为其他需要属性需要用到这个属性；
        requestMappingHandlerAdapter.setMessageConverters(messageConverters.getConverters());
        return requestMappingHandlerAdapter;
    }

    @Bean
    public SessionLocaleResolver localeResolver() {
        ZKSessionLocaleResolver zkSessionLocaleResolver = new ZKSessionLocaleResolver();
        zkSessionLocaleResolver.setDefaultLocale(
                ZKLocaleUtils.valueOf(env.getProperty("zk.core.default.locale", String.class, "en_US")));
        return zkSessionLocaleResolver;
    }

}
