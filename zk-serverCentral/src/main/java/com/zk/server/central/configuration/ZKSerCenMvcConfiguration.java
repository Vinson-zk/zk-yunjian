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
 * @Title: ZKSerCenMvcConfig.java 
 * @author Vinson 
 * @Package com.zk.server.central.configuration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:10:32 PM 
 * @version V1.0   
*/
package com.zk.server.central.configuration;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zk.server.central.interceptor.ZKViewVariateInterceptor;
import com.zk.webmvc.configuration.ZKEnableWebmvc;
import com.zk.webmvc.handler.ZKViewExceptionHandlerResolver;

/** 
* @ClassName: ZKSerCenMvcConfig 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@ZKEnableWebmvc
public class ZKSerCenMvcConfiguration implements WebMvcConfigurer {

    public ZKSerCenMvcConfiguration() {
        System.out.println("[^_^:20230211-1022-001] ----- 实例化 -----" + this.getClass().getSimpleName());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态文件访问映射；如：将 /static/** 访问映射到 classpath: /mystatic/
//        registry.addResourceHandler("/**").addResourceLocations("/","classpath:templates/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/static/**").addResourceLocations("/static/**", "classpath:/static/");

//        registry.addResourceHandler("/eureka/**").addResourceLocations("classpath:/static/eureka/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println(
                "[^_^:20230129-1648-002] --- [" + this.getClass().getSimpleName() + "] 配置 zk mvc 日志拦截器；" + this);
//        registry.addInterceptor(new ZKLocaleInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new ZKViewVariateInterceptor()).addPathPatterns("/**");
    }

//    // 国际化语言信息扩展
//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
////        converters.addAll(messageConverters.getConverters());
//    }
//
//    // 国际化语言信息扩展
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.addAll(messageConverters.getConverters());
//    }

    // 其他 mvc 的 bean 注入 ---------------------------------------------------------------------------
    // 异常处理
    @Bean
    ZKViewExceptionHandlerResolver viewExceptionHandlerResolver() {
        ZKViewExceptionHandlerResolver viewExceptionHandlerResolver = new ZKViewExceptionHandlerResolver();
        viewExceptionHandlerResolver.setDefaultStatusCode(444);

        Properties statusCodes = new Properties();
        statusCodes.put("error/400", "400");
        statusCodes.put("error/404", "404");
//        statusCodes.put("error/5xx", "500");
        viewExceptionHandlerResolver.setStatusCodes(statusCodes);

        Properties exceptionMappings = new Properties();
        exceptionMappings.put("java.util.MissingResourceException", "/error/missingResource");
        viewExceptionHandlerResolver.setExceptionMappings(exceptionMappings);

        return viewExceptionHandlerResolver;
    }
}
