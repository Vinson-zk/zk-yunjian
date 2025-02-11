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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.server.WebFilter;

import com.zk.core.configuration.ZKCoreConfiguration;
import com.zk.core.redis.configuration.ZKRedisProperties;
import com.zk.core.web.ZKWebConstants.ZKFilterLevel;
import com.zk.core.web.support.webFlux.filter.ZKCrosWebFilter;
import com.zk.db.configuration.ZKDBProperties;
import com.zk.framework.serCen.ZKSerCenEncrypt;
import com.zk.framework.serCen.support.ZKSerCenSampleCipher;
import com.zk.log.webFilter.ZKLogAccessWebFilter;

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
    ZKDBProperties zkDBProperties() {
        return new ZKDBProperties();
    }

    @Bean("redisProperties")
    @ConfigurationProperties(prefix = "zk.gateway.cache.redis")
    ZKRedisProperties redisProperties() {
        return new ZKRedisProperties();
    }

//    @Bean("fileFluxProperties")
//    @ConfigurationProperties(prefix = "zk.core.server.file.upload.parthttpmessagereader")
//    ZKFileFluxProperties zkFileFluxProperties() {
//        return new ZKFileFluxProperties();
//    }

    @Bean
    ResourceBundleMessageSource messageSource() {
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
    ZKSerCenEncrypt zkSerCenEncrypt() {
        return new ZKSerCenSampleCipher();
    }

//    @Bean
//    public ZKSerCenDecode zkSerCenDecode() {
//        return new ZKSerCenSampleCipher();
//    }

    /******************************************************************/

    // 日志拦截器
    @Bean("logAccessWebFilter")
    WebFilter logAccessWebFilter() {
        System.out.println("[^_^:20230211-1022-001] ----- logAccessFilterWebFilter 配置 zk 日志过滤器 WebFilter --- ["
                + this.getClass().getSimpleName() + "] " + this.hashCode());
        // 访问日志记录 过虑器
        ZKLogAccessWebFilter logAccessWebFilter = new ZKLogAccessWebFilter();
        logAccessWebFilter.setOrder(ZKFilterLevel.Log.HIGHEST);
        return logAccessWebFilter;
    }

    @Bean("zkCrosFilter")
    ZKCrosWebFilter zkCrosFilter() {
        ZKCrosWebFilter zkCrosFilter = new ZKCrosWebFilter();
        zkCrosFilter.setOrder(ZKFilterLevel.Normal.HIGHEST);
        return zkCrosFilter;
    }

//    @Bean("zkExceptionHandlerWebFilter")
//    public ZKExceptionHandlerWebFilter zkExceptionHandlerWebFilter() {
//        ZKExceptionHandlerWebFilter zkExceptionHandlerWebFilter = new ZKExceptionHandlerWebFilter();
//        zkExceptionHandlerWebFilter.setOrder(ZKFilterLevel.Exception.HIGHEST);
//        return zkExceptionHandlerWebFilter;
//    }

}


