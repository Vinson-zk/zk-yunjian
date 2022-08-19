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
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.zk.cache.ZKCacheManager;
import com.zk.cache.redis.ZKRedisCacheManager;
import com.zk.core.commons.ZKValidatorMessageInterpolator;
import com.zk.core.redis.ZKJedisOperatorStringKey;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.core.web.filter.ZKCrosFilter;
import com.zk.core.web.resolver.ZKExceptionHandlerResolver;
import com.zk.core.web.utils.ZKWebUtils;

/** 
* @ClassName: ZKWechatConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ImportResource(locations = { "classpath:xmlConfig/spring_ctx_application.xml",
        "classpath:xmlConfig/spring_ctx_wechat_application.xml", "classpath:xmlConfig/spring_ctx_mvc.xml" })
@AutoConfigureBefore(value = { 
        ServletWebServerFactoryAutoConfiguration.class
        })
@AutoConfigureAfter(value = { ZKWechatRedisConfiguration.class, ZKWechatJdbcConfiguration.class })
public class ZKWechatConfiguration {
    
    protected static Logger log = LoggerFactory.getLogger(ZKWechatConfiguration.class);

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void postConstruct() {
        // 方法在 @Autowired before 后执行
//        System.out.println("[^_^:20191219-2154-001] ===== ZKSerCenConfiguration class postConstruct ");
//        System.out.println("[^_^:20191219-2154-001] ----- ZKSerCenConfiguration class postConstruct ");
    }

    @Autowired
    public void before(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        log.info("[^_^:20200805-1808-001] ===== ZKSysConfiguration class before ");

        ZKEnvironmentUtils.initContext(applicationContext);
//        ZKLocaleUtils.setLocale(ZKLocaleUtils.valueOf("en_US"));
//        ZKLocaleUtils.setLocale(ZKLocaleUtils.valueOf("zh_CN"));
//        // # 默认语言；注意这里不影响到 localeResolver 的默认语言
        ZKWebUtils.setLocale(
                ZKLocaleUtils.distributeLocale(ZKEnvironmentUtils.getString("zk.wechat.default.locale", "zh_CN")));

        // 设置下 RequestMappingHandlerAdapter 的 ignoreDefaultModelOnRedirect=true,
        // 这样可以提高效率，避免不必要的检索。
        requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
        log.info("[^_^:20200805-1808-001] ----- ZKWechatConfiguration class before ");
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
     * 项目的缓存管理
     *
     * @Title: zkCacheManager
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:26:49 PM
     * @param jedisOperator
     * @return
     * @return ZKCacheManager<String>
     */
    @Bean(name = "zkCacheManager")
    public ZKCacheManager<String> zkCacheManager(ZKJedisOperatorStringKey jedisOperator) {
        ZKRedisCacheManager zkCacheManager = new ZKRedisCacheManager();
        zkCacheManager.setJedisOperator(jedisOperator);
        return zkCacheManager;
    }

}
