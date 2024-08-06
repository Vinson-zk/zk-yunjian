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
 * @Title: ZKSerCenBeforeConfiguration.java 
 * @author Vinson 
 * @Package com.zk.server.central.configuration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:09:53 PM 
 * @version V1.0   
*/
package com.zk.server.central.configuration;

import java.io.File;
import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EurekaConstants;
import org.springframework.cloud.netflix.eureka.server.EurekaServerAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.zk.core.commons.ZKValidatorMessageInterpolator;
import com.zk.core.redis.configuration.ZKRedisProperties;
import com.zk.core.web.ZKWebConstants.ZKFilterLevel;
import com.zk.core.web.support.servlet.filter.ZKCrosFilter;
import com.zk.core.web.support.servlet.filter.ZKOncePerFilter;
import com.zk.db.configuration.ZKDBProperties;
import com.zk.framework.serCen.ZKSerCenDecode;
import com.zk.framework.serCen.ZKSerCenEncrypt;
import com.zk.framework.serCen.support.ZKSerCenSampleCipher;
import com.zk.log.filter.ZKLogAccessFilter;
import com.zk.security.configuration.ZKEnableSecurity;
import com.zk.security.service.ZKSecPrincipalService;
import com.zk.server.central.commons.ZKSerCenCerCipherManager;
import com.zk.server.central.commons.support.ZKSerCenCerCipherManagerImpl;
import com.zk.server.central.filter.ZKSerCenRegisterFilter;
import com.zk.server.central.security.ZKShiroPrincipalService;

import jakarta.servlet.Filter;
import jakarta.validation.Validator;

/**
 * @ClassName: ZKSerCenBeforeConfiguration
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
@AutoConfigureBefore(value = {
//        WebMvcAutoConfiguration.class, //
//        ServletWebServerFactoryAutoConfiguration.class,
        EurekaServerAutoConfiguration.class
})
@PropertySource(encoding = "UTF-8", value = { "classpath:zk.ser.cen.jdbc.properties" })
@PropertySource(encoding = "UTF-8", value = { "classpath:zk.ser.cen.redis.properties" })
public class ZKSerCenBeforeConfiguration {

    protected static Logger log = LogManager.getLogger(ZKSerCenBeforeConfiguration.class);

    @Value("${zk.path.admin}")
    String pathAdmin;

    @Value("${zk.path.serCen}")
    String pathSerCen;

    public ZKSerCenBeforeConfiguration() {
        System.out.println("[^_^:20230211-1022-001] ----- 实例化 -----" + this.getClass().getSimpleName());
    }

    @Bean
    ZKSecPrincipalService zkSecPrincipalService() {
        System.out.println(ZKEnableSecurity.printLog + " create zkSecPrincipalService: ZKShiroPrincipalService; "
                + this.getClass().getSimpleName() + " " + this.hashCode());
        return new ZKShiroPrincipalService();
    }

    @Bean("zkDBProperties")
    @ConfigurationProperties(prefix = "zk.ser.cen.db.dynamic.jdbc")
    ZKDBProperties zkDBProperties() {
        return new ZKDBProperties();
    }

    @Bean("redisProperties")
    @ConfigurationProperties(prefix = "zk.ser.cen.redis")
    ZKRedisProperties redisProperties() {
        return new ZKRedisProperties();
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
        filterRegistrationBean.addUrlPatterns(File.separator + this.pathAdmin + File.separator + this.pathSerCen + "/*");
        filterRegistrationBean.setName("logAccessFilter");
        return filterRegistrationBean;
    }

    @Bean
    ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("msg/zkMsg_serCen");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setCacheSeconds(3600);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
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
    @DependsOn("messageSource")
    @Bean
    Validator validator(MessageSource messageSource, ApplicationContext applicationContext) {
        /*
         * 重写这个方法比较好
         * 
         * 重写这个类 org.springframework.validation.beanvalidation.
         * MessageSourceResourceBundleLocator 里的，这个类
         * org.springframework.context.support.MessageSourceResourceBundle.
         * MessageSourceResourceBundle(MessageSource source, Locale locale) 里的
         * handleGetObject 和 containsKey 这个方法比较好；
         * 
         * return new ResourceBundleMessageInterpolator(new
         * MessageSourceResourceBundleLocator(messageSource));
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

    /**
     * 服务证书管理实现
     *
     * @Title: zkServerCertificateManager
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Oct 28, 2019 3:01:53 PM
     * @return
     * @return ZKServerCertificateManager
     */
    @Primary
    @Bean
    ZKSerCenCerCipherManager zkSerCenCerCipherManager() {
//        return new ZKServerCertificateManagerImpl(2048, null);
        return new ZKSerCenCerCipherManagerImpl();
    }

    /**
     * 服务注册 证书加密拦截器
     *
     * @Title: jerseyFilterRegistration
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 12, 2020 9:06:08 PM
     * @param zkSerCenDecode
     * @return FilterRegistrationBean
     */
    @Bean
    FilterRegistrationBean<ZKOncePerFilter> zkSerCenRegisterFilter(
            ZKSerCenDecode zkSerCenDecode) {
        FilterRegistrationBean<ZKOncePerFilter> bean = new FilterRegistrationBean<ZKOncePerFilter>();
        bean.setFilter(new ZKSerCenRegisterFilter(zkSerCenDecode));
        bean.setOrder(ZKFilterLevel.Normal.LOWEST);
        bean.setUrlPatterns(Collections.singletonList(EurekaConstants.DEFAULT_PREFIX + "/*"));
        return bean;
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

    @Bean
    ZKSerCenDecode zkSerCenDecode() {
        return new ZKSerCenSampleCipher();
    }

    @Primary
    @Bean
    ZKJersey3TransportClientFactories jersey3TransportClientFactories(ZKSerCenEncrypt zkSerCenEncrypt) {
        return new ZKJersey3TransportClientFactories(zkSerCenEncrypt);
    }

}
