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
* @Title: ZKJersey3TransportClientFactories.java 
* @author Vinson 
* @Package com.zk.server.central.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 16, 2024 1:35:25 AM 
* @version V1.0 
*/
package com.zk.server.central.configuration;

import java.util.Collection;
import java.util.Optional;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import com.netflix.appinfo.EurekaClientIdentity;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.shared.resolver.EurekaEndpoint;
import com.netflix.discovery.shared.transport.EurekaHttpClient;
import com.netflix.discovery.shared.transport.TransportClientFactory;
import com.netflix.discovery.shared.transport.decorator.MetricsCollectingEurekaHttpClient;
import com.netflix.discovery.shared.transport.jersey.TransportClientFactories;
import com.zk.framework.serCen.ZKSerCenEncrypt;

import jakarta.ws.rs.client.ClientRequestFilter;

/** 
* @ClassName: ZKJersey3TransportClientFactories 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
//public class Jersey3TransportClientFactories implements TransportClientFactories<ClientRequestFilter> {
public class ZKJersey3TransportClientFactories implements TransportClientFactories<ClientRequestFilter> {

    private ZKSerCenEncrypt zkSerCenEncrypt;

    public ZKJersey3TransportClientFactories(ZKSerCenEncrypt zkSerCenEncrypt) {
        this.zkSerCenEncrypt = zkSerCenEncrypt;
    }

    @Override
    public TransportClientFactory newTransportClientFactory(final EurekaClientConfig clientConfig,
            final Collection<ClientRequestFilter> additionalFilters, final InstanceInfo myInstanceInfo) {
        return newTransportClientFactory(clientConfig, additionalFilters, myInstanceInfo, Optional.empty(),
                Optional.empty());
    }

    @Override
    public TransportClientFactory newTransportClientFactory(EurekaClientConfig clientConfig,
            Collection<ClientRequestFilter> additionalFilters, InstanceInfo myInstanceInfo,
            Optional<SSLContext> sslContext, Optional<HostnameVerifier> hostnameVerifier) {
        final ZKJersey3ApplicationClientFactory jerseyFactory = ZKJersey3ApplicationClientFactory.create(clientConfig,
                additionalFilters, myInstanceInfo,
                new EurekaClientIdentity(myInstanceInfo.getIPAddr(), "Jersey3DefaultClient"), sslContext,
                hostnameVerifier);
        jerseyFactory.setZkSerCenEncrypt(this.zkSerCenEncrypt);
        final TransportClientFactory metricsFactory = MetricsCollectingEurekaHttpClient.createFactory(jerseyFactory);

        return new TransportClientFactory() {
            @Override
            public EurekaHttpClient newClient(EurekaEndpoint serviceUrl) {
                return metricsFactory.newClient(serviceUrl);
            }

            @Override
            public void shutdown() {
                metricsFactory.shutdown();
                jerseyFactory.shutdown();
            }
        };
    }

}
