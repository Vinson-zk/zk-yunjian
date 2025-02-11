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
     * @Title: ZKWebmvcParentConfiguration.java 
     * @author Vinson 
     * @Package com.zk.core.helper.configuration 
     * @Description: TODO(simple description this file what to do.) 
     * @date Dec 12, 2019 5:15:13 PM 
     * @version V1.0   
    */
    package com.zk.webmvc.helper.configuration;
    
    import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

import com.zk.core.commons.ZKFileTransfer;
import com.zk.core.commons.ZKValidatorMessageInterpolator;
import com.zk.core.commons.support.ZKDiskFileTransfer;

import jakarta.validation.Validator;
    
    /**
     * @ClassName: ZKWebmvcParentConfiguration
     * @Description: TODO(simple description this class what to do.)
     * @author Vinson
     * @version 1.0
     */
    @PropertySources(value = { //
            @PropertySource(ignoreResourceNotFound = true, encoding = "UTF-8", value = { "classpath:test.zk.webmvc.properties" }), // 
    })
    public class ZKWebmvcParentConfiguration {
    
        @Autowired
        Environment env;
    
    //    @Bean
    //    public ZKTransferCipherManager zkTransferCipherManager() {
    //        return new ZKSampleRsaAesTransferCipherManager();
    //    }
    
    //    @ConditionalOnMissingBean(name = { "exceptionHandlerFilter", "zkExceptionHandlerFilter" })
    //    @Bean({ "exceptionHandlerFilter", "zkExceptionHandlerFilter" })
    //    FilterRegistrationBean<Filter> zkExceptionHandlerFilter() {
    ////        System.out.println("[^_^:20230211-1022-001] ----- zkExceptionHandlerFilter 配置 异常处理拦截器 Filter --- ["
    ////                + this.getClass().getSimpleName() + "] " + this.hashCode());
    //        System.out.println("[^_^:20230211-1022-002] ----- zkExceptionHandlerFilter 配置 异常处理拦截器 Filter --- ["
    //                + this.getClass().getSimpleName() + "] " + this.hashCode());
    //        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
    //        // 访问日志记录 过虑器
    //        filterRegistrationBean.setFilter(new ZKExceptionHandlerFilter());
    //        filterRegistrationBean.setOrder(ZKFilterLevel.Exception.HIGHEST);
    //        filterRegistrationBean.addUrlPatterns("/zkexs/*");
    //        filterRegistrationBean.setName("zkExceptionHandlerFilter");
    //        return filterRegistrationBean;
    //    }
    
        @Autowired
        public void step() {
    
            System.out.println("[^_^:20190625-1716-001] -------- zkCoreServerPort:"
                    + env.getProperty("zk.webmvc.server.port", Integer.class, 8080));
            System.out.println(
                    "[^_^:20190625-1716-001] -------- zk.webmvc.server.port:" + env.getProperty("server.port"));
            System.out.println("[^_^:20190625-1716-001] -------- zk.path.admin:" + env.getProperty("zk.path.admin"));
            System.out.println("[^_^:20190625-1716-001] -------- zk.path.webmvc:" + env.getProperty("zk.path.webmvc"));
            System.out.println(
                    "[^_^:20190625-1716-001] -------- zk.default.locale:" + env.getProperty("zk.default.locale"));
    
        }
    
        @Bean
        ZKFileTransfer zkFileTransfer() {
            return new ZKDiskFileTransfer(-1, -1, -1, null);
        }
    
    //    @PostConstruct
        @Autowired
        public void dispatcherServlet(DispatcherServlet dispatcherServlet) {
    //        dispatcherServlet.getServletConfig()
        }
    
//      // # 文件上传，最大上传大小，需要比邮件附件单个的文件大小配置值要大；50M=52428800
//      @Value("${zk.core.servlet.file.upload.multipartresolver.maxInMemorySize:52428800}")
//      long maxUploadSize;
//  
//      // # 文件上传，最大处理内存大小； 1M=1048576
//      @Value("${zk.core.servlet.file.upload.multipartresolver.maxInMemorySize:1048576}")
//      int maxInMemorySize;
//  
//      // # 文件上传，单个文件上传最大大小； 10M=10485760
//      @Value("${zk.core.servlet.file.upload.multipartresolver.maxUploadSizePerFile:10485760}")
//      int maxUploadSizePerFile;
//  
//      // # 文件上传，处理字符集
//      @Value("${zk.core.servlet.file.upload.multipartresolver.defaultEncoding:UTF-8}")
//      String defaultEncoding;

        /**
         * 文件上传 适配器
         *
         * @Title: multipartResolver
         * @Description: TODO(simple description this method what to do.)
         * @author Vinson
         * @date May 27, 2022 10:13:32 AM
         * @return
         * @return CommonsMultipartResolver
         */
//      @Bean
//      CommonsMultipartResolver multipartResolver() {
//          CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//          multipartResolver.setDefaultEncoding(this.defaultEncoding);
//          // 设置总上传数据总大小
//          multipartResolver.setMaxUploadSize(this.maxUploadSize);
//          multipartResolver.setMaxInMemorySize(this.maxInMemorySize);
//          // 设置单个文件最大大小
//          multipartResolver.setMaxUploadSizePerFile(maxUploadSizePerFile);
//          return multipartResolver;
//      }

        @Bean
        MultipartResolver multipartResolver() {
    //      CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
            StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
    //      multipartResolver.setDefaultEncoding("UTF-8");
    //      multipartResolver.setMaxUploadSize(20971520);
    //      multipartResolver.setMaxInMemorySize(40960);
    //        multipartResolver.
            return multipartResolver;
        }
    
        /**
         * 数据验证消息处理
         *
         * @Title: validator
         * @Description: TODO(simple description this method what to do.)
         * @author Vinson
         * @date Dec 12, 2019 5:43:19 PM
         * @param messageSource
         * @param applicationContext
         * @return
         * @return Validator
         */
        @Bean
        Validator validator(MessageSource messageSource, ApplicationContext applicationContext) {
            LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
            ZKValidatorMessageInterpolator zkValidatorMessageInterpolator = new ZKValidatorMessageInterpolator(
                    messageSource);
            localValidatorFactoryBean.setMessageInterpolator(zkValidatorMessageInterpolator);
    //         localValidatorFactoryBean.setValidationMessageSource(messageSource);
            return localValidatorFactoryBean;
        }
    
    }
