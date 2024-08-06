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
package com.zk.webmvc.helper;

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
import com.zk.core.configuration.ZKEnableCoreServlet;

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
@ZKEnableCoreServlet
public class ZKWebmvcParentConfiguration {

    @Autowired
    Environment env;

//    @Bean
//    public ZKTransferCipherManager zkTransferCipherManager() {
//        return new ZKSampleRsaAesTransferCipherManager();
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
                "[^_^:20190625-1716-001] -------- zk.webmvc.default.locale:"
                        + env.getProperty("zk.webmvc.default.locale"));

    }

    @Bean
    public ZKFileTransfer zkFileTransfer() {
        return new ZKDiskFileTransfer(-1, -1, -1, null);
    }

//    @PostConstruct
    @Autowired
    public void dispatcherServlet(DispatcherServlet dispatcherServlet) {
//        dispatcherServlet.getServletConfig()
    }

//  @Bean
//  public CommonsMultipartResolver multipartResolver() {
////    import org.springframework.web.multipart.commons.CommonsMultipartResolver;
//      CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//      multipartResolver.setDefaultEncoding("UTF-8");
//      multipartResolver.setMaxUploadSize(20971520);
//      multipartResolver.setMaxInMemorySize(40960);
////      multipartResolver.set
//      return multipartResolver;
//  }

    @Bean
    public MultipartResolver multipartResolver() {
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
    public Validator validator(MessageSource messageSource, ApplicationContext applicationContext) {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        ZKValidatorMessageInterpolator zkValidatorMessageInterpolator = new ZKValidatorMessageInterpolator(
                messageSource);
        localValidatorFactoryBean.setMessageInterpolator(zkValidatorMessageInterpolator);
//         localValidatorFactoryBean.setValidationMessageSource(messageSource);
        return localValidatorFactoryBean;
    }

}
