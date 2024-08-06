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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
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
import com.zk.security.service.ZKSecDefaultPrincipalService;
import com.zk.security.service.ZKSecPrincipalService;
import com.zk.webmvc.configuration.ZKWebmvcConfiguration;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.Filter;
import jakarta.validation.Validator;

/** 
* @ClassName: ZKWechatConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@AutoConfigureBefore(value = { //
        WebMvcAutoConfiguration.class, //
        ServletWebServerFactoryAutoConfiguration.class, //
})
@PropertySource(encoding = "UTF-8", value = { //
        "classpath:zk.log.properties", //
        "classpath:zk.wechat.wx.officialAccounts.properties", //
        "classpath:zk.wechat.wx.thirdParty.properties", //
        "classpath:zk.wechat.wx.pay.properties", //
        "classpath:zk.wechat.jdbc.properties", //
        "classpath:zk.wechat.redis.properties", //
})
public class ZKWechatBeforeConfiguration extends ZKWebmvcConfiguration {

    protected Logger log = LogManager.getLogger(this.getClass());

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

    @Bean("zkDBProperties")
    @ConfigurationProperties(prefix = "zk.wechat.db.dynamic.jdbc")
    ZKDBProperties zkDBProperties() {
        return new ZKDBProperties();
    }

    @Bean("redisProperties")
    @ConfigurationProperties(prefix = "zk.wechat.cache.redis")
    ZKRedisProperties redisProperties() {
        return new ZKRedisProperties();
    }

    @Bean
    ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("msg/zkMsg_wechat");
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
    RestTemplate restTemplate() {
        return new RestTemplate();
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
