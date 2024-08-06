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

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import com.zk.core.commons.ZKValidatorMessageInterpolator;
import com.zk.core.redis.configuration.ZKRedisProperties;
import com.zk.core.web.ZKWebConstants.ZKFilterLevel;
import com.zk.core.web.support.servlet.filter.ZKCrosFilter;
import com.zk.db.configuration.ZKDBProperties;
import com.zk.framework.serCen.ZKSerCenEncrypt;
import com.zk.framework.serCen.support.ZKSerCenSampleCipher;
import com.zk.log.filter.ZKLogAccessFilter;
import com.zk.mail.common.ZKMailAuthenticator;
import com.zk.mail.utils.ZKMailUtils;
import com.zk.security.service.ZKSecDefaultPrincipalService;
import com.zk.security.service.ZKSecPrincipalService;
import com.zk.webmvc.configuration.ZKWebmvcConfiguration;

import jakarta.servlet.Filter;
import jakarta.validation.Validator;

/** 
* @ClassName: ZKMailBeforeConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@AutoConfigureBefore(value = { ServletWebServerFactoryAutoConfiguration.class })
@PropertySource(encoding = "UTF-8", value = { //
        "classpath:zk.mail.jdbc.properties", //
        "classpath:zk.mail.redis.properties", //
})
public class ZKMailBeforeConfiguration extends ZKWebmvcConfiguration {

    protected Logger log = LogManager.getLogger(this.getClass());

//    // # 文件上传，最大上传大小，需要比邮件附件单个的文件大小配置值要大；50M=52428800
//    @Value("${zk.mail.file.upload.multipartResolver.maxInMemorySize:52428800}")
//    long maxUploadSize;
//
//    // # 文件上传，最大处理内存大小； 1M=1048576
//    @Value("${zk.mail.file.upload.multipartResolver.maxInMemorySize:40960}")
//    int maxInMemorySize;
//
//    // # 文件上传，单个文件上传最大大小； 10M=10485760
//    @Value("${zk.mail.file.upload.multipartResolver.maxUploadSizePerFile:10485760}")
//    int maxUploadSizePerFile;
//
//    // # 文件上传，处理字符集
//    @Value("${zk.mail.file.upload.multipartResolver.defaultEncoding:UTF-8}")
//    String defaultEncoding;

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
    public void beforeMail() {
        log.info("[^_^:20200805-1808-001] === [" + ZKMailBeforeConfiguration.class.getSimpleName() + "] " + this);
        log.info("[^_^:20200805-1808-001] --- [" + ZKMailBeforeConfiguration.class.getSimpleName() + "] " + this);
    }

    /******************************************************************/

    @Bean("zkDBProperties")
    @ConfigurationProperties(prefix = "zk.mail.db.dynamic.jdbc")
    ZKDBProperties zkDBProperties() {
        return new ZKDBProperties();
    }

    @Bean("redisProperties")
    @ConfigurationProperties(prefix = "zk.mail.cache.redis")
    ZKRedisProperties redisProperties() {
        return new ZKRedisProperties();
    }

    @Bean
    ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("msg/zkMsg_mail");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setCacheSeconds(3600);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    ZKMailAuthenticator mailAuthenticator() {
        return ZKMailUtils.getMailAuthenticator(account, password);
    }

    @Bean
    Properties mailProperties() {
        return ZKMailUtils.getProperties(host, port, type, validate);
    }

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
    ZKSerCenEncrypt zkSerCenEncrypt() {
        return new ZKSerCenSampleCipher();
    }

//    @Bean
//    public ZKSerCenDecode zkSerCenDecode() {
//        return new ZKSerCenSampleCipher();
//    }

    /******************************************************************/

    // 日志拦截器
    @Bean("logAccessFilterRegistrationBean")
    FilterRegistrationBean<Filter> logAccessFilterRegistrationBean() {
        System.out.println("[^_^:20230211-1022-001] ----- logAccessFilterRegistrationBean 配置 zk 日志过滤器 Filter --- ["
                + this.getClass().getSimpleName() + "] " + this.hashCode());
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
        // 访问日志记录 过虑器
        ZKLogAccessFilter logAccessFilter = new ZKLogAccessFilter();
        filterRegistrationBean.setFilter(logAccessFilter);
        filterRegistrationBean.setOrder(ZKFilterLevel.Log.HIGHEST);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("logAccessFilter");
        return filterRegistrationBean;
    }

    @Bean
    FilterRegistrationBean<Filter> zkCrosFilterRegistrationBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
        ZKCrosFilter zkCrosFilter = new ZKCrosFilter();
        filterRegistrationBean.setFilter(zkCrosFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("zkCrosFilter");
        filterRegistrationBean.setOrder(ZKFilterLevel.Normal.HIGHEST);
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
    Validator validator(MessageSource messageSource, ApplicationContext applicationContext) {

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
     * 文件上传 适配器
     *
     * @Title: multipartResolver
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 27, 2022 10:13:32 AM
     * @return
     * @return CommonsMultipartResolver
     */
//    @Bean
//    CommonsMultipartResolver multipartResolver() {
//        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//        multipartResolver.setDefaultEncoding(this.defaultEncoding);
//        // 设置总上传数据总大小
//        multipartResolver.setMaxUploadSize(this.maxUploadSize);
//        multipartResolver.setMaxInMemorySize(this.maxInMemorySize);
//        // 设置单个文件最大大小
//        multipartResolver.setMaxUploadSizePerFile(maxUploadSizePerFile);
//        return multipartResolver;
//    }

    @Bean
    StandardServletMultipartResolver multipartResolver() {
        StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
        return multipartResolver;
    }

    /**
     * 当用登录用户获取服务
     *
     * @Title: secPrincipalService
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 6, 2023 9:59:33 AM
     * @return
     * @return ZKSecPrincipalService
     */
    @Bean
    ZKSecPrincipalService secPrincipalService() {
        return new ZKSecDefaultPrincipalService();
    }

}
