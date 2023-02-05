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

import javax.servlet.Filter;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.MutableDiscoveryClientOptionalArgs;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;

import com.zk.cache.redis.ZKRedisCacheManager;
import com.zk.core.commons.ZKValidatorMessageInterpolator;
import com.zk.core.redis.ZKJedisOperatorStringKey;
import com.zk.core.web.filter.ZKCrosFilter;
import com.zk.framework.serCen.ZKSerCenEncrypt;
import com.zk.framework.serCen.eureka.ZKEurekaTransportClientFactories;
import com.zk.framework.serCen.support.ZKSerCenSampleCipher;
import com.zk.webmvc.configuration.ZKWebmvcConfiguration;
import com.zk.webmvc.handler.ZKExceptionHandlerResolver;
import com.zk.webmvc.interceptor.ZKLogAccessInterceptor;

/**
 * @ClassName: ZKSysBeforeConfiguration
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
@Configuration
@ImportResource(locations = { "classpath:xmlConfig/spring_ctx_application.xml",
        "classpath:xmlConfig/spring_ctx_sys_application.xml", "classpath:xmlConfig/spring_ctx_mvc.xml",
        "classpath:xmlConfig/spring_ctx_dynamic_mybatis.xml" })
@PropertySource(encoding = "UTF-8", value = { "classpath:zk.log.properties" })
//@ImportAutoConfiguration(classes = { ZKMongoAutoConfiguration.class })
@AutoConfigureBefore(value = { //
        ZKSysMvcConfiguration.class, //
//        ZKSysSecConfiguration.class, //
//        ZKMongoAutoConfiguration.class, //
        ZKSysJdbcConfiguration.class, //
        ZKSysRedisConfiguration.class, //
        EnableWebMvcConfiguration.class, //
        ServletWebServerFactoryAutoConfiguration.class //
})
public class ZKSysBeforeConfiguration extends ZKWebmvcConfiguration {

    protected static Logger log = LoggerFactory.getLogger(ZKSysBeforeConfiguration.class);

    @Autowired
    public void beforeSys() {
        System.out.println("[^_^:20200805-1808-001] === [" + ZKSysBeforeConfiguration.class.getSimpleName() + "] " + this);
        System.out.println("[^_^:20200805-1808-001] ----[" + ZKSysBeforeConfiguration.class.getSimpleName() + "] " + this);
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

    // 异常处理 xml 中定义
//  @Bean
//  public ZKViewExceptionHandlerResolver exceptionResolver() {
//      return new ZKViewExceptionHandlerResolver();
//  }

//    @Bean
//    @DependsOn(value = { "messageConverters" })
//    public RequestMappingHandlerAdapter requestMappingHandlerAdapter(HttpMessageConverters messageConverters) {
//        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
//        // session 线程安全配置
//        requestMappingHandlerAdapter.setSynchronizeOnSession(
//                ZKWebUtils.isTrue(ZKEnvironmentUtils.getString("zk.core.web.mvc.synchronizeOnSession", "true")));
//        // 一定要在这些设置 HttpMessageConverters 因为其他需要属性需要用到这个属性；
//        requestMappingHandlerAdapter.setMessageConverters(messageConverters.getConverters());
//        return requestMappingHandlerAdapter;
//    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

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
     * 缓存管理
     *
     * @Title: redisCacheManager
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 11, 2022 7:17:31 PM
     * @param jedisOperatorStringKey
     * @return
     * @return ZKRedisCacheManager
     */
    @Bean
    public ZKRedisCacheManager redisCacheManager(ZKJedisOperatorStringKey jedisOperatorStringKey) {
        ZKRedisCacheManager cm = new ZKRedisCacheManager();
        cm.setJedisOperator(jedisOperatorStringKey);
        return cm;
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
