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
* @Title: ZKSysBeforeConfiguration.java 
* @author Vinson 
* @Package com.zk.sys.configuration 
* @Description: TODO(simple description this file what to do.) 
* @date Jul 16, 2020 3:23:57 PM 
* @version V1.0 
*/
package com.zk.sys.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;

import com.zk.core.commons.ZKValidatorMessageInterpolator;
import com.zk.core.redis.configuration.ZKRedisProperties;
import com.zk.core.web.ZKWebConstants.ZKFilterLevel;
import com.zk.core.web.support.servlet.filter.ZKCrosFilter;
import com.zk.db.configuration.ZKDBProperties;
import com.zk.framework.serCen.ZKSerCenEncrypt;
import com.zk.framework.serCen.support.ZKSerCenSampleCipher;
import com.zk.log.filter.ZKLogAccessFilter;
import com.zk.webmvc.configuration.ZKWebmvcConfiguration;

import jakarta.servlet.Filter;
import jakarta.validation.Validator;

/**
 * @ClassName: ZKSysBeforeConfiguration
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
@PropertySources(value = { //
        @PropertySource(encoding = "UTF-8", value = { "classpath:zk.sys.redis.properties" }), //
        @PropertySource(encoding = "UTF-8", value = { "classpath:zk.sys.jdbc.properties" }), //
})
public class ZKSysBeforeConfiguration extends ZKWebmvcConfiguration {

    protected static Logger log = LogManager.getLogger(ZKSysBeforeConfiguration.class);

    @Autowired
    public void beforeSys() {
        System.out.println("[^_^:20200805-1808-001] === [" + this.getClass().getSimpleName() + "] " + this);
        System.out.println("[^_^:20200805-1808-001] ----[" + this.getClass().getSimpleName() + "] " + this);
    }

    /******************************************************************/

    @Bean("zkDBProperties")
    @ConfigurationProperties(prefix = "zk.sys.db.dynamic.jdbc")
    ZKDBProperties zkDBProperties() {
        return new ZKDBProperties();
    }

    @Bean("redisProperties")
    @ConfigurationProperties(prefix = "zk.sys.redis")
    ZKRedisProperties redisProperties() {
        return new ZKRedisProperties();
    }

    @Bean
    ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("msg/zkMsg_sys");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setCacheSeconds(3600);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
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

//    // 创建 RestTemplate
//    @DependsOn(value = { "messageConverters" })
//    @Bean
////    @LoadBalanced
//    public RestTemplate restTemplate(HttpMessageConverters messageConverters) {
//        return new RestTemplate(messageConverters.getConverters());
//    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
