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
* @Title: ZKJersey1TransportClientFactories.java 
* @author Vinson 
* @Package com.zk.framework.serCen.eureka 
* @Description: TODO(simple description this file what to do.) 
* @date Jul 2, 2020 5:31:03 PM 
* @version V1.0 
*/
package com.zk.framework.serCen.eureka;

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
import com.netflix.discovery.shared.transport.jersey.Jersey1TransportClientFactories;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.zk.framework.serCen.ZKSerCenEncrypt;

/** 
* @ClassName: ZKJersey1TransportClientFactories 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKJersey1TransportClientFactories extends Jersey1TransportClientFactories {

    private ZKSerCenEncrypt zkSerCenEncrypt;

    public ZKJersey1TransportClientFactories(ZKSerCenEncrypt zkSerCenEncrypt) {
        this.zkSerCenEncrypt = zkSerCenEncrypt;
    }

    @Override
    public TransportClientFactory newTransportClientFactory(EurekaClientConfig clientConfig,
            Collection<ClientFilter> additionalFilters, InstanceInfo myInstanceInfo, Optional<SSLContext> sslContext,
            Optional<HostnameVerifier> hostnameVerifier) {
//        final TransportClientFactory jerseyFactory = JerseyEurekaHttpClientFactory.create(clientConfig,
//                additionalFilters, myInstanceInfo, new EurekaClientIdentity(myInstanceInfo.getIPAddr()), sslContext,
//                hostnameVerifier);

        final TransportClientFactory jerseyFactory = ZKJerseyEurekaHttpClientFactory.create(zkSerCenEncrypt,
                clientConfig, additionalFilters, myInstanceInfo, new EurekaClientIdentity(myInstanceInfo.getIPAddr()),
                sslContext, hostnameVerifier);

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
