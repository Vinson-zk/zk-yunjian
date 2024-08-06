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
* @Title: ZKJerseyTransportClientFactories.java 
* @author Vinson 
* @Package com.zk.framework.serCen.eureka 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 15, 2024 10:25:14 PM 
* @version V1.0 
*/
package com.zk.framework.serCen.eureka;

import java.util.Collection;
import java.util.Optional;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.shared.transport.TransportClientFactory;
import com.netflix.discovery.shared.transport.jersey.TransportClientFactories;
import com.zk.framework.serCen.ZKSerCenEncrypt;

/** 
* @ClassName: ZKJerseyTransportClientFactories 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
//public class ZKJerseyTransportClientFactories extends RestTemplateTransportClientFactories {
public class ZKRestTemplateTransportClientFactories implements TransportClientFactories<Void> {

    private ZKSerCenEncrypt zkSerCenEncrypt;

    private final ZKRestTemplateDiscoveryClientOptionalArgs args;

    public ZKRestTemplateTransportClientFactories(ZKSerCenEncrypt zkSerCenEncrypt,
            ZKRestTemplateDiscoveryClientOptionalArgs args) {
        this.zkSerCenEncrypt = zkSerCenEncrypt;
        this.args = args;
    }

    @Override
    public TransportClientFactory newTransportClientFactory(EurekaClientConfig clientConfig,
            Collection<Void> additionalFilters, InstanceInfo myInstanceInfo) {
        return new ZKRestTemplateTransportClientFactory(zkSerCenEncrypt, this.args.getSSLContext(),
                this.args.getHostnameVerifier(), this.args.eurekaClientHttpRequestFactorySupplier,
                this.args.restTemplateBuilderSupplier);
    }

    @Override
    public TransportClientFactory newTransportClientFactory(final EurekaClientConfig clientConfig,
            final Collection<Void> additionalFilters, final InstanceInfo myInstanceInfo,
            final Optional<SSLContext> sslContext, final Optional<HostnameVerifier> hostnameVerifier) {
        return new ZKRestTemplateTransportClientFactory(zkSerCenEncrypt, this.args.getSSLContext(),
                this.args.getHostnameVerifier(), this.args.eurekaClientHttpRequestFactorySupplier,
                this.args.restTemplateBuilderSupplier);
    }

}
