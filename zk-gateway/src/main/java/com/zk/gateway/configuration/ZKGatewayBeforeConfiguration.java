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
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.MutableDiscoveryClientOptionalArgs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter;

import com.zk.core.configuration.ZKCoreConfiguration;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.core.web.filter.ZKCrosFilter;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.framework.serCen.ZKSerCenEncrypt;
import com.zk.framework.serCen.eureka.ZKEurekaTransportClientFactories;
import com.zk.framework.serCen.support.ZKSerCenSampleCipher;
import com.zk.security.service.ZKSecDefaultPrincipalService;
import com.zk.security.service.ZKSecPrincipalService;

/**
 * @ClassName: ZKGatewayBeforeConfiguration
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Configuration
@ImportResource(locations = { //
        "classpath:xmlConfig/spring_ctx_application.xml", //
        "classpath:xmlConfig/spring_ctx_gateway_application.xml", //
//        "classpath:xmlConfig/spring_ctx_mvc.xml", //
})
@PropertySource(encoding = "UTF-8", value = { 
        "classpath:zk.log.properties" })
@AutoConfigureBefore(value = {
    ZKGatewayJdbcConfiguration.class, 
    ZKGatewayRedisConfiguration.class, 
    ZKGatewaySecConfiguration.class,
    ZKGatewayAfterConfiguration.class,
//    ServletWebServerFactoryAutoConfiguration.class,
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
        // # 默认语言；
        ZKWebUtils
                .setLocale(ZKLocaleUtils.distributeLocale(ZKEnvironmentUtils.getString("zk.default.locale", "zh_CN")));
        // 设置下 RequestMappingHandlerAdapter 的 ignoreDefaultModelOnRedirect=true,
        // 这样可以提高效率，避免不必要的检索。
//        requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
        System.out.println("[^_^:20220614-1940-001] --- [" + ZKGatewayBeforeConfiguration.class.getSimpleName() + "] " + this);
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
//    @Bean
//    public ZKLogAccessInterceptor logAccessInterceptor() {
//        return new ZKLogAccessInterceptor();
//    }

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

//    /**
//     * 异常处理适配器
//     *
//     * @Title: zkExceptionHandlerResolver
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date Aug 21, 2020 11:13:24 AM
//     * @return
//     * @return ZKExceptionHandlerResolver
//     */
//    @Bean
//    public ZKExceptionHandlerResolver zkExceptionHandlerResolver() {
//        return new ZKExceptionHandlerResolver();
//    }

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
    public ZKSecPrincipalService secPrincipalService() {
        return new ZKSecDefaultPrincipalService();
    }

}