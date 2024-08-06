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
* @Title: ZKWebfluxChildConfiguration.java 
* @author Vinson 
* @Package com.zk.webflux.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 23, 2024 12:04:35 AM 
* @version V1.0 
*/
package com.zk.webflux.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import com.zk.webflux.configuration.ZKFileFluxProperties;

/** 
* @ClassName: ZKWebfluxChildConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ImportAutoConfiguration(classes = { DispatcherServletAutoConfiguration.class,
        ReactiveWebServerFactoryAutoConfiguration.class })
@ComponentScan(basePackages = { "com.zk.webflux.helper.controller" })
@AutoConfigureAfter(value = { ZKWebfluxParentConfiguration.class })
public class ZKWebfluxChildConfiguration {

    @Autowired
    Environment env;

//  使用默认值
    @Bean
    public ZKFileFluxProperties zkFileFluxProperties() {
        ZKFileFluxProperties zkFileFluxProperties = new ZKFileFluxProperties();
//        zkFileFluxProperties.setMaxHeadersSize(-1);
        return zkFileFluxProperties;
    }

//    @Bean
//    public ConfigurableServletWebServerFactory webServerFactory() {
//        TomcatServletWebServerFactory tomcatFactory = new TomcatServletWebServerFactory();
//        int zkCoreServerPort = env.getProperty("zk.core.server.port", Integer.class, 8080);
//        tomcatFactory.setPort(zkCoreServerPort);
//        return tomcatFactory;
//    }

//    @Bean
//    @DependsOn(value = { "messageConverters" })
//    public RequestMappingHandlerAdapter requestMappingHandlerAdapter(HttpMessageConverters messageConverters) {
//        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
//        return requestMappingHandlerAdapter;
//    }

//    @Bean
//    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
//        RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
//        mapping.setOrder(0);
//        return mapping;
//    }

//    @Bean
//    public SessionLocaleResolver localeResolver() {
//        ZKSessionLocaleResolver zkSessionLocaleResolver = new ZKSessionLocaleResolver();
//        zkSessionLocaleResolver.setDefaultLocale(
//                ZKLocaleUtils.valueOf(env.getProperty("zk.core.default.locale", String.class, "en_US")));
//        return zkSessionLocaleResolver;
//    }

}
