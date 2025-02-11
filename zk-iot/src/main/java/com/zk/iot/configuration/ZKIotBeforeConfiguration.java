/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKIotBeforeConfiguration.java 
* @author Vinson 
* @Package com.zk.iot.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Dec 31, 2024 11:29:46 AM 
* @version V1.0 
*/
package com.zk.iot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.server.WebFilter;

import com.rabbitmq.client.ConnectionFactory;
import com.zk.core.configuration.ZKCoreConfiguration;
import com.zk.core.redis.configuration.ZKRedisProperties;
import com.zk.core.web.ZKWebConstants.ZKFilterLevel;
import com.zk.core.web.support.webFlux.filter.ZKCrosWebFilter;
import com.zk.db.configuration.ZKDBProperties;
import com.zk.framework.serCen.ZKSerCenEncrypt;
import com.zk.framework.serCen.support.ZKSerCenSampleCipher;
import com.zk.log.webFilter.ZKLogAccessWebFilter;
import com.zk.webflux.configuration.ZKFileFluxProperties;

/** 
* @ClassName: ZKIotBeforeConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Configuration
@AutoConfigureBefore(value = { //
        ZKIotAfterConfiguration.class, //
})
@PropertySource(encoding = "UTF-8", value = { //
        "classpath:zk.iot.jdbc.properties",   //
        "classpath:zk.iot.redis.properties",  //
})
public class ZKIotBeforeConfiguration extends ZKCoreConfiguration {

    @Autowired
    public void before(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        System.out.println(
                "[^_^:20241231-1727-001] === [" + ZKIotBeforeConfiguration.class.getSimpleName() + "] " + this);
        // 设置下 RequestMappingHandlerAdapter 的 ignoreDefaultModelOnRedirect=true,
        // 这样可以提高效率，避免不必要的检索。webflux 没有这个方法
//        requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
        System.out.println(
                "[^_^:20241231-1727-001] --- [" + ZKIotBeforeConfiguration.class.getSimpleName() + "] " + this);
    }

    /******************************************************************/

    @Bean("zkDBProperties")
    @ConfigurationProperties(prefix = "zk.iot.db.dynamic.jdbc")
    ZKDBProperties zkDBProperties() {
        return new ZKDBProperties();
    }

    @Bean("redisProperties")
    @ConfigurationProperties(prefix = "zk.iot.redis")
    ZKRedisProperties redisProperties() {
        return new ZKRedisProperties();
    }

    @Bean("fileFluxProperties")
    @ConfigurationProperties(prefix = "zk.core.server.file.upload.parthttpmessagereader")
    ZKFileFluxProperties zkFileFluxProperties() {
        return new ZKFileFluxProperties();
    }

    @Bean
    ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("msg/zkMsg_iot");
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

    @Value("${zk.iot.rabbitmq.host}")
    String rabbitmqHost;

    @Value("${zk.iot.rabbitmq.port}")
    int rabbitmqPort;

    @Value("${zk.iot.rabbitmq.virtualHost}")
    String rabbitmqVirtualHost;

    @Value("${zk.iot.rabbitmq.userName}")
    String rabbitmqUserName;

    @Value("${zk.iot.rabbitmq.password}")
    String rabbitmqPassword;

    @Bean("rabbitmqConnectionFactory")
    ConnectionFactory rabbitmqConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        // "guest"/"guest" by default, limited to localhost connections
        factory.setUsername(rabbitmqUserName);
        factory.setPassword(rabbitmqPassword);
        factory.setVirtualHost(rabbitmqVirtualHost);
        factory.setHost(rabbitmqHost);
        factory.setPort(rabbitmqPort);

        System.out.println("[^_^:20250110-1401-001] ----- rabbitMQ config:  [" + this.getClass().getSimpleName()
                + "] ==================================================");
        System.out.println("[^_^:20250110-1401-001] ----- rabbitMQ config rabbitmqHost: " + rabbitmqHost);
        System.out.println("[^_^:20250110-1401-001] ----- rabbitMQ config rabbitmqPort: " + rabbitmqPort);
        System.out.println("[^_^:20250110-1401-001] ----- rabbitMQ config rabbitmqVirtualHost: " + rabbitmqVirtualHost);
        System.out.println("[^_^:20250110-1401-001] ----- rabbitMQ config rabbitmqUserName: " + rabbitmqUserName);
        System.out.println("[^_^:20250110-1401-001] ----- rabbitMQ config rabbitmqPassword: " + rabbitmqPassword);
        System.out.println("[^_^:20250110-1401-001] ----- rabbitMQ config:  [" + this.getClass().getSimpleName()
                + "] --------------------------------------------------");

        return factory;
    }

}

