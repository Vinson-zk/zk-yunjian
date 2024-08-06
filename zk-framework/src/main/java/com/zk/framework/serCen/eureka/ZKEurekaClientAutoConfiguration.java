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
* @Title: ZKEurekaClientAutoConfiguration.java 
* @author Vinson 
* @Package com.zk.framework.serCen.eureka 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 16, 2024 2:43:54 AM 
* @version V1.0 
*/
package com.zk.framework.serCen.eureka;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.configuration.SSLContextFactory;
import org.springframework.cloud.configuration.TlsProperties;
import org.springframework.cloud.netflix.eureka.config.DiscoveryClientOptionalArgsConfiguration;
import org.springframework.cloud.netflix.eureka.http.EurekaClientHttpRequestFactorySupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.zk.framework.serCen.ZKSerCenEncrypt;

/** 
* @ClassName: ZKEurekaClientAutoConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
//@Configuration
@AutoConfigureBefore(value = { DiscoveryClientOptionalArgsConfiguration.class })
public class ZKEurekaClientAutoConfiguration {

//  @Bean
//  @ConditionalOnClass(name = "org.springframework.web.client.RestTemplate")
//  @ConditionalOnMissingClass("org.glassfish.jersey.client.JerseyClient")
//  @ConditionalOnMissingBean(value = { AbstractDiscoveryClientOptionalArgs.class }, search = SearchStrategy.CURRENT)
//  @ConditionalOnProperty(prefix = "eureka.client", name = "webclient.enabled", matchIfMissing = true,
//          havingValue = "false")
//  public RestTemplateDiscoveryClientOptionalArgs restTemplateDiscoveryClientOptionalArgs(TlsProperties tlsProperties,
//          EurekaClientHttpRequestFactorySupplier eurekaClientHttpRequestFactorySupplier,
//          ObjectProvider<RestTemplateBuilder> restTemplateBuilders) throws GeneralSecurityException, IOException {
//      logger.info("Eureka HTTP Client uses RestTemplate.");
//      RestTemplateDiscoveryClientOptionalArgs result = new RestTemplateDiscoveryClientOptionalArgs(
//              eurekaClientHttpRequestFactorySupplier, restTemplateBuilders::getIfAvailable);
//      setupTLS(result, tlsProperties);
//      return result;
//  }
//
//  @Bean
//  @ConditionalOnClass(name = "org.springframework.web.client.RestTemplate")
//  @ConditionalOnMissingClass("org.glassfish.jersey.client.JerseyClient")
//  @ConditionalOnMissingBean(value = { TransportClientFactories.class }, search = SearchStrategy.CURRENT)
//  @ConditionalOnProperty(prefix = "eureka.client", name = "webclient.enabled", matchIfMissing = true,
//          havingValue = "false")
//  public RestTemplateTransportClientFactories restTemplateTransportClientFactories(
//          RestTemplateDiscoveryClientOptionalArgs optionalArgs) {
//      return new RestTemplateTransportClientFactories(optionalArgs);
//  }

    private static void setupTLS(AbstractDiscoveryClientOptionalArgs<?> args, TlsProperties properties)
            throws GeneralSecurityException, IOException {
        if (properties.isEnabled()) {
            SSLContextFactory factory = new SSLContextFactory(properties);
            args.setSSLContext(factory.createSSLContext());
        }
    }

    @Primary
    @Bean
    @ConditionalOnClass(name = "org.springframework.web.client.RestTemplate")
//    @ConditionalOnMissingClass("org.glassfish.jersey.client.JerseyClient")
//    @ConditionalOnMissingBean(value = {
//            AbstractDiscoveryClientOptionalArgs.class }, search = SearchStrategy.CURRENT)
    @ConditionalOnProperty(prefix = "eureka.client", name = "webclient.enabled", matchIfMissing = true, havingValue = "false")
    ZKRestTemplateDiscoveryClientOptionalArgs restTemplateDiscoveryClientOptionalArgs(TlsProperties tlsProperties,
            EurekaClientHttpRequestFactorySupplier eurekaClientHttpRequestFactorySupplier,
            ObjectProvider<RestTemplateBuilder> restTemplateBuilders) throws GeneralSecurityException, IOException {
        ZKRestTemplateDiscoveryClientOptionalArgs result = new ZKRestTemplateDiscoveryClientOptionalArgs(
                eurekaClientHttpRequestFactorySupplier, restTemplateBuilders::getIfAvailable);
        setupTLS(result, tlsProperties);
        return result;
    }

    @Primary
    @Bean
    @ConditionalOnClass(name = "org.springframework.web.client.RestTemplate")
//    @ConditionalOnMissingClass("org.glassfish.jersey.client.JerseyClient")
//    @ConditionalOnMissingBean(value = { TransportClientFactories.class }, search = SearchStrategy.CURRENT)
    @ConditionalOnProperty(prefix = "eureka.client", name = "webclient.enabled", matchIfMissing = true, havingValue = "false")
    ZKRestTemplateTransportClientFactories restTemplateTransportClientFactories(ZKSerCenEncrypt zkSerCenEncrypt,
            ZKRestTemplateDiscoveryClientOptionalArgs optionalArgs) {
        return new ZKRestTemplateTransportClientFactories(zkSerCenEncrypt, optionalArgs);
    }
}
