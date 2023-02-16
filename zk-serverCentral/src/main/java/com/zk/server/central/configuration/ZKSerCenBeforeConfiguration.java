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

import java.util.Collections;

import javax.servlet.Filter;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EurekaConstants;
import org.springframework.cloud.netflix.eureka.MutableDiscoveryClientOptionalArgs;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.Ordered;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.zk.core.commons.ZKValidatorMessageInterpolator;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.web.filter.ZKCrosFilter;
import com.zk.core.web.filter.ZKOncePerFilter;
import com.zk.framework.serCen.ZKSerCenDecode;
import com.zk.framework.serCen.ZKSerCenEncrypt;
import com.zk.framework.serCen.eureka.ZKEurekaTransportClientFactories;
import com.zk.framework.serCen.support.ZKSerCenSampleCipher;
import com.zk.server.central.commons.ZKSerCenCerCipherManager;
import com.zk.server.central.commons.support.ZKSerCenCerCipherManagerImpl;
import com.zk.server.central.filter.ZKSerCenRegisterFilter;
import com.zk.server.central.interceptor.ZKViewVariateInterceptor;
import com.zk.webmvc.configuration.ZKWebmvcConfiguration;

/**
 * @ClassName: ZKSerCenBeforeConfiguration
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
@ImportResource(locations = { "classpath:xmlConfig/spring_ctx_sc_application.xml",
        "classpath:xmlConfig/spring_ctx_sc_mvc.xml" })
//@ImportAutoConfiguration(classes = { ZKMongoAutoConfiguration.class })
@AutoConfigureBefore(value = { 
//        ZKMongoAutoConfiguration.class, // 
//        ZKSerCenRedisConfiguration.class, // 
        ZKSerCenJdbcConfiguration.class, //
        ZKSerCenShiroConfiguration.class, //
        ZKSerCenMvcConfiguration.class, //
        WebMvcAutoConfiguration.class, //
        ServletWebServerFactoryAutoConfiguration.class,
})
//@AutoConfigureAfter(value = { ServletWebServerFactoryAutoConfiguration.class })
//@AutoConfigureOrder(value = Ordered.HIGHEST_PRECEDENCE)
//@ImportAutoConfiguration(classes = { DispatcherServletAutoConfiguration.class })
@Configuration
public class ZKSerCenBeforeConfiguration extends ZKWebmvcConfiguration {

    protected static Logger log = LoggerFactory.getLogger(ZKSerCenBeforeConfiguration.class);

    @Autowired
    public void beforeSerCen() {
//        super.before(requestMappingHandlerAdapter);
        System.out.println(
                "[^_^:20191219-2154-001] === [" + ZKSerCenBeforeConfiguration.class.getSimpleName() + "] " + this);
        System.out.println("[^_^:20191219-2154-001] spring.mvc.view.prefix:     " + ZKEnvironmentUtils.getString("spring.mvc.view.prefix"));
        System.out.println("[^_^:20191219-2154-001] spring.freemarker.prefix:   " + ZKEnvironmentUtils.getString("spring.freemarker.prefix"));
        System.out.println(
                "[^_^:20191219-2154-001] --- [" + ZKSerCenBeforeConfiguration.class.getSimpleName() + "] " + this);
    }

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
    @Bean
    public ZKSerCenCerCipherManager zkSerCenCerCipherManager() {
//        return new ZKServerCertificateManagerImpl(2048, null);
        return new ZKSerCenCerCipherManagerImpl();
    }

    @Bean
    public ZKViewVariateInterceptor zkViewVariateInterceptor() {
        return new ZKViewVariateInterceptor();
    }

//    // 创建 RestTemplate
//    @DependsOn(value = { "messageConverters" })
//    @Bean
////    @LoadBalanced
//    public RestTemplate restTemplate(HttpMessageConverters messageConverters) {
//        return new RestTemplate(messageConverters.getConverters());
//    }

//    // 异常处理 xml 中定义
//    @Bean
//    public ZKViewExceptionHandlerResolver exceptionResolver() {
//        return new ZKViewExceptionHandlerResolver();
//    }

//    @Bean
//    @DependsOn(value = { "messageConverters" })
//    public RequestMappingHandlerAdapter requestMappingHandlerAdapter(HttpMessageConverters messageConverters) {
//        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
//        // session 线程安全配置
//        requestMappingHandlerAdapter.setSynchronizeOnSession(
//                ZKUtils.isTrue(ZKEnvironmentUtils.getString("zk.core.web.mvc.synchronizeOnSession", "true")));
//        // 一定要在这些设置 HttpMessageConverters 因为其他需要属性需要用到这个属性；
//        requestMappingHandlerAdapter.setMessageConverters(messageConverters.getConverters());
//        return requestMappingHandlerAdapter;
//    }

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
    public FilterRegistrationBean<ZKOncePerFilter> zkSerCenRegisterFilter(
            ZKSerCenDecode zkSerCenDecode) {
        FilterRegistrationBean<ZKOncePerFilter> bean = new FilterRegistrationBean<ZKOncePerFilter>();
        bean.setFilter(new ZKSerCenRegisterFilter(zkSerCenDecode));
        bean.setOrder(Ordered.LOWEST_PRECEDENCE + 10);
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
    public ZKSerCenEncrypt zkSerCenEncrypt() {
        return new ZKSerCenSampleCipher();
    }

    @Bean
    public ZKSerCenDecode zkSerCenDecode() {
        return new ZKSerCenSampleCipher();
    }

    @Bean
    @ConditionalOnClass(name = "com.sun.jersey.api.client.filter.ClientFilter")
//    @ConditionalOnMissingBean(value = AbstractDiscoveryClientOptionalArgs.class, search = SearchStrategy.CURRENT)
    public MutableDiscoveryClientOptionalArgs discoveryClientOptionalArgs(ZKSerCenEncrypt zkSerCenEncrypt) {
        MutableDiscoveryClientOptionalArgs ms = new MutableDiscoveryClientOptionalArgs();
        ms.setTransportClientFactories(new ZKEurekaTransportClientFactories(zkSerCenEncrypt));
        return ms;
    }

//    /**
//     * 缓存管理
//     *
//     * @Title: redisCacheManager
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date May 11, 2022 7:17:31 PM
//     * @param jedisOperatorStringKey
//     * @return
//     * @return ZKRedisCacheManager
//     */
//    @Bean
//    public ZKRedisCacheManager redisCacheManager(ZKJedisOperatorStringKey jedisOperatorStringKey) {
//        ZKRedisCacheManager cm = new ZKRedisCacheManager();
//        cm.setJedisOperator(jedisOperatorStringKey);
//        return cm;
//    }

}