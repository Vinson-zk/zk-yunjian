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
* @Title: ZKMailBeforeConfiguration.java 
* @author Vinson 
* @Package com.zk.mail.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date May 25, 2022 8:45:51 AM 
* @version V1.0 
*/
package com.zk.mail.configuration;

import javax.servlet.Filter;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.MutableDiscoveryClientOptionalArgs;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.zk.core.commons.ZKValidatorMessageInterpolator;
import com.zk.core.web.filter.ZKCrosFilter;
import com.zk.framework.serCen.ZKSerCenEncrypt;
import com.zk.framework.serCen.eureka.ZKEurekaTransportClientFactories;
import com.zk.framework.serCen.support.ZKSerCenSampleCipher;
import com.zk.webmvc.configuration.ZKWebmvcConfiguration;
import com.zk.webmvc.handler.ZKExceptionHandlerResolver;
import com.zk.webmvc.interceptor.ZKLogAccessInterceptor;


/** 
* @ClassName: ZKMailBeforeConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Configuration
@AutoConfigureBefore(value = { 
        ZKMailJdbcConfiguration.class, 
        ZKMailRedisConfiguration.class, 
        ZKMailAfterConfiguration.class,
        ServletWebServerFactoryAutoConfiguration.class })
@ImportResource(locations = { 
        "classpath:xmlConfig/spring_ctx_application.xml",
        "classpath:xmlConfig/spring_ctx_mail_application.xml", 
        "classpath:xmlConfig/spring_ctx_mvc.xml" })
@PropertySource(encoding = "UTF-8", value = { 
        "classpath:zk.log.properties"})
public class ZKMailBeforeConfiguration extends ZKWebmvcConfiguration {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    // # 文件上传，最大上传大小，需要比邮件附件单个的文件大小配置值要大；50M=52428800
    @Value("${zk.mail.file.upload.multipartResolver.maxInMemorySize:52428800}")
    long maxUploadSize;

    // # 文件上传，最大处理内存大小； 1M=1048576
    @Value("${zk.mail.file.upload.multipartResolver.maxInMemorySize:40960}")
    int maxInMemorySize;

    // # 文件上传，单个文件上传最大大小； 10M=10485760
    @Value("${zk.mail.file.upload.multipartResolver.maxUploadSizePerFile:10485760}")
    int maxUploadSizePerFile;

    // # 文件上传，处理字符集
    @Value("${zk.mail.file.upload.multipartResolver.defaultEncoding:UTF-8}")
    String defaultEncoding;

    @Autowired
    public void beforeMail() {
        log.info("[^_^:20200805-1808-001] === [" + ZKMailBeforeConfiguration.class.getSimpleName() + "] " + this);
        log.info("[^_^:20200805-1808-001] --- [" + ZKMailBeforeConfiguration.class.getSimpleName() + "] " + this);
    }

    /******************************************************************/
    /**
     * 服务注册加解密
     *
     * @Title: zkSerCenSampleCipher
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 6, 2020 6:07:27 PM
     * @return
     * @return ZKSerCenSampleCipher
     */
    @Bean
    public ZKSerCenEncrypt zkSerCenEncrypt() {
        return new ZKSerCenSampleCipher();
    }

//    @Bean
//    public ZKSerCenDecode zkSerCenDecode() {
//        return new ZKSerCenSampleCipher();
//    }

    @Bean
//    @ConditionalOnBean(name = "eurekaDiscoverClientMarker")
    @ConditionalOnClass(name = "com.sun.jersey.api.client.filter.ClientFilter")
//    @ConditionalOnMissingBean(value = AbstractDiscoveryClientOptionalArgs.class, search = SearchStrategy.CURRENT)
    public MutableDiscoveryClientOptionalArgs discoveryClientOptionalArgs(ZKSerCenEncrypt zkSerCenEncrypt) {
        MutableDiscoveryClientOptionalArgs ms = new MutableDiscoveryClientOptionalArgs();
        ms.setTransportClientFactories(new ZKEurekaTransportClientFactories(zkSerCenEncrypt));
        return ms;
    }

    /******************************************************************/

    @Bean
    public FilterRegistrationBean<Filter> zkCrosFilterRegistrationBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
        ZKCrosFilter zkCrosFilter = new ZKCrosFilter();
        filterRegistrationBean.setFilter(zkCrosFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("zkCrosFilter");
//        Map<String, String> zkCrosFilterInitParams = new HashMap<>();
//        zkCrosFilterInitParams.put(ParamsName.allowOrigin, "*");
//        zkCrosFilterInitParams.put(ParamsName.maxAge, "3600");
//        zkCrosFilterInitParams.put(ParamsName.allowMethods, "POST,GET");
//        zkCrosFilterInitParams.put(ParamsName.allowHeaders, "__SID,locale,Lang,X-Requested-With");
//        filterRegistrationBean.setInitParameters(zkCrosFilterInitParams);
        return filterRegistrationBean;
    }

    /**
     * 数据验证消息处理
     *
     * @Title: validator
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Oct 28, 2019 3:02:00 PM
     * @param messageSource
     * @param applicationContext
     * @return
     * @return Validator
     */
    @Bean
    public Validator validator(MessageSource messageSource, ApplicationContext applicationContext) {

        /*
         * 重写这个方法比较好
         * 
         * 重写这个类 org.springframework.validation.beanvalidation. MessageSourceResourceBundleLocator 里的，这个类
         * org.springframework.context.support.MessageSourceResourceBundle. MessageSourceResourceBundle(MessageSource source, Locale locale) 里的 handleGetObject
         * 和 containsKey 这个方法比较好；
         * 
         * return new ResourceBundleMessageInterpolator(new MessageSourceResourceBundleLocator(messageSource));
         */
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        ZKValidatorMessageInterpolator zkValidatorMessageInterpolator = new ZKValidatorMessageInterpolator(
                messageSource);
        localValidatorFactoryBean.setMessageInterpolator(zkValidatorMessageInterpolator);
        // localValidatorFactoryBean.setValidationMessageSource(messageSource);
        return localValidatorFactoryBean;
    }

//    @Bean
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

    /**
     * 异常处理适配器
     *
     * @Title: zkExceptionHandlerResolver
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 21, 2020 11:13:24 AM
     * @return
     * @return ZKExceptionHandlerResolver
     */
    @Bean
    public ZKExceptionHandlerResolver zkExceptionHandlerResolver() {
        return new ZKExceptionHandlerResolver();
    }

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
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding(this.defaultEncoding);
        // 设置总上传数据总大小
        multipartResolver.setMaxUploadSize(this.maxUploadSize);
        multipartResolver.setMaxInMemorySize(this.maxInMemorySize);
        // 设置单个文件最大大小
        multipartResolver.setMaxUploadSizePerFile(maxUploadSizePerFile);
        return multipartResolver;
    }

    /**
     * 记录日志拦截器
     *
     * @Title: logAccessInterceptor
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 14, 2022 11:59:49 AM
     * @return
     * @return ZKLogAccessInterceptor
     */
    @Bean
    public ZKLogAccessInterceptor logAccessInterceptor() {
        return new ZKLogAccessInterceptor();
    }

}
