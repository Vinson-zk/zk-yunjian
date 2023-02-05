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
* @Title: ZKWechatConfiguration.java 
* @author Vinson 
* @Package com.zk.wechat.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 18, 2021 11:17:17 AM 
* @version V1.0 
*/
package com.zk.wechat.configuration;

import javax.annotation.PostConstruct;
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

import com.zk.core.commons.ZKValidatorMessageInterpolator;
import com.zk.core.web.filter.ZKCrosFilter;
import com.zk.framework.serCen.ZKSerCenEncrypt;
import com.zk.framework.serCen.eureka.ZKEurekaTransportClientFactories;
import com.zk.framework.serCen.support.ZKSerCenSampleCipher;
import com.zk.webmvc.configuration.ZKWebmvcConfiguration;
import com.zk.webmvc.handler.ZKExceptionHandlerResolver;
import com.zk.webmvc.interceptor.ZKLogAccessInterceptor;

/** 
* @ClassName: ZKWechatConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Configuration
@AutoConfigureBefore(value = { 
        ZKWechatJdbcConfiguration.class, 
        ZKWechatRedisConfiguration.class, 
        ZKWechatAfterConfiguration.class,
        WebMvcAutoConfiguration.class,
        ServletWebServerFactoryAutoConfiguration.class })
@ImportResource(locations = {
        "classpath:xmlConfig/spring_ctx_application.xml",
        "classpath:xmlConfig/spring_ctx_wechat_application.xml",
        "classpath:xmlConfig/spring_ctx_mvc.xml" })
@PropertySource(encoding = "UTF-8", value = { 
        "classpath:zk.log.properties",
        "classpath:zk.wechat.wx.officialAccounts.properties",
        "classpath:zk.wechat.wx.thirdParty.properties", 
        "classpath:zk.wechat.wx.pay.properties" })
public class ZKWechatBeforeConfiguration extends ZKWebmvcConfiguration {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void postConstruct() {
        // 方法在 @Autowired before 后执行
    }

    @Autowired
    public void beforeWechat() {
        System.out.println("[^_^:20230205-0416-001] === [" + ZKWechatBeforeConfiguration.class.getSimpleName() + "] " + this);
        System.out.println("[^_^:20230205-0416-001] ----[" + ZKWechatBeforeConfiguration.class.getSimpleName() + "] " + this);
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

//    /**
//     * 打开feign默认契约后 spring的注解就不起作用咯 要使用它自己的注解 如 RequestLine @RuequestLine("GET /api/server/list")
//     * 
//     * 还有feign对于@GetMapping @PostMapping等 是不支持的 支持从requestMapping 对于参数自定义对象也是不支持的 可以使用map 或 注册类型转换器到spring的convert中
//     *
//     * @Title: feignContract
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date May 12, 2022 4:27:25 PM
//     * @return
//     * @return Contract
//     */
//    @Bean
//    public Contract feignContract() {
//        /**
//         * Contract feign的默认契约 如果FeignClient想要使用feign自己定义的注解，需要在configuration中配置feign的契约模式，因为其默认采用的时sping的注解方式，所以会不识别feign的注解
//         * 
//         */
//        return new Contract.Default();
//    }

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
