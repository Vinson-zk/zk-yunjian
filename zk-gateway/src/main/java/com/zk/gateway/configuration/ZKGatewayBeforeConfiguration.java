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
* @Title: ZKGatewayBeforeConfiguration.java 
* @author Vinson 
* @Package com.zk.gateway.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 14, 2022 7:07:11 PM 
* @version V1.0 
*/
package com.zk.gateway.configuration;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.MutableDiscoveryClientOptionalArgs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter;

import com.zk.core.configuration.ZKCoreConfiguration;
import com.zk.core.redis.configuration.ZKRedisProperties;
import com.zk.core.web.filter.ZKCrosFilter;
import com.zk.core.web.filter.ZKFilterUtils.ZKFilterLevel;
import com.zk.db.configuration.ZKDBProperties;
import com.zk.framework.serCen.ZKSerCenEncrypt;
import com.zk.framework.serCen.eureka.ZKEurekaTransportClientFactories;
import com.zk.framework.serCen.support.ZKSerCenSampleCipher;
import com.zk.log.filter.ZKLogAccessFilter;

/**
 * @ClassName: ZKGatewayBeforeConfiguration
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@PropertySource(encoding = "UTF-8", value = { //
        "classpath:zk.gateway.redis.properties", //
        "classpath:zk.gateway.jdbc.properties", //
})
public class ZKGatewayBeforeConfiguration extends ZKCoreConfiguration {

    // # 文件上传，最大上传大小，需要比邮件附件单个的文件大小配置值要大；50M=52428800
    @Value("${zk.gateway.file.upload.multipartResolver.maxInMemorySize:52428800}")
    long maxUploadSize;

    // # 文件上传，最大处理内存大小； 1M=1048576
    @Value("${zk.gateway.file.upload.multipartResolver.maxInMemorySize:40960}")
    int maxInMemorySize;

    // # 文件上传，单个文件上传最大大小； 10M=10485760
    @Value("${zk.gateway.file.upload.multipartResolver.maxUploadSizePerFile:10485760}")
    int maxUploadSizePerFile;

    // # 文件上传，处理字符集
    @Value("${zk.gateway.file.upload.multipartResolver.defaultEncoding:UTF-8}")
    String defaultEncoding;

    @Autowired
    public void beforeGateway(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        System.out.println("[^_^:20220614-1940-001] === [" + ZKGatewayBeforeConfiguration.class.getSimpleName() + "] " + this);
        // 设置下 RequestMappingHandlerAdapter 的 ignoreDefaultModelOnRedirect=true,
        // 这样可以提高效率，避免不必要的检索。webflux 没有这个方法
//        requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
        System.out.println("[^_^:20220614-1940-001] --- [" + ZKGatewayBeforeConfiguration.class.getSimpleName() + "] " + this);
    }

    /******************************************************************/

    @Bean("zkDBProperties")
    @ConfigurationProperties(prefix = "zk.gateway.db.dynamic.jdbc")
    public ZKDBProperties zkDBProperties() {
        return new ZKDBProperties();
    }

    @Bean("redisProperties")
    @ConfigurationProperties(prefix = "zk.gateway.cache.redis")
    public ZKRedisProperties redisProperties() {
        return new ZKRedisProperties();
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("msg/zkMsg_gateway");
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
    public ZKSerCenEncrypt zkSerCenEncrypt() {
        return new ZKSerCenSampleCipher();
    }

//    @Bean
//    public ZKSerCenDecode zkSerCenDecode() {
//        return new ZKSerCenSampleCipher();
//    }

    @Bean
    @ConditionalOnClass(name = "com.sun.jersey.api.client.filter.ClientFilter")
    public MutableDiscoveryClientOptionalArgs discoveryClientOptionalArgs(ZKSerCenEncrypt zkSerCenEncrypt) {
        MutableDiscoveryClientOptionalArgs ms = new MutableDiscoveryClientOptionalArgs();
        ms.setTransportClientFactories(new ZKEurekaTransportClientFactories(zkSerCenEncrypt));
        return ms;
    }

    /******************************************************************/

    // 日志拦截器
    @Bean("logAccessFilterRegistrationBean")
    public FilterRegistrationBean<Filter> logAccessFilterRegistrationBean() {
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
    public FilterRegistrationBean<Filter> zkCrosFilterRegistrationBean() {
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
     * 文件上传 适配器
     *
     * @Title: multipartResolver
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 27, 2022 10:13:32 AM
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

}
