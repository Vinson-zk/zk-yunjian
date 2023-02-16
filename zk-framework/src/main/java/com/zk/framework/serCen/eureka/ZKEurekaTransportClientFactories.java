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
* @Title: ZKEurekaTransportClientFactories.java 
* @author Vinson 
* @Package com.zk.framework.serCen.eureka 
* @Description: TODO(simple description this file what to do.) 
* @date May 3, 2020 8:32:34 AM 
* @version V1.0 
*/
package com.zk.framework.serCen.eureka;

import java.util.Collection;
import java.util.Collections;
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
import com.netflix.discovery.shared.transport.jersey.EurekaJerseyClient;
import com.netflix.discovery.shared.transport.jersey.TransportClientFactories;
import com.sun.jersey.api.client.filter.ClientFilter;
import com.sun.jersey.client.apache4.ApacheHttpClient4;
import com.zk.framework.serCen.ZKSerCenEncrypt;

/**
 * @ClassName: ZKEurekaTransportClientFactories
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKEurekaTransportClientFactories implements TransportClientFactories<ClientFilter> {

    private ZKSerCenEncrypt zkSerCenEncrypt;

    public ZKEurekaTransportClientFactories(ZKSerCenEncrypt zkSerCenEncrypt) {
        this.zkSerCenEncrypt = zkSerCenEncrypt;
    }

    @Override
    public TransportClientFactory newTransportClientFactory(Collection<ClientFilter> additionalFilters,
            EurekaJerseyClient providedJerseyClient) {
        ApacheHttpClient4 apacheHttpClient = providedJerseyClient.getClient();
        if (additionalFilters != null) {
            for (ClientFilter filter : additionalFilters) {
                if (filter != null) {
                    apacheHttpClient.addFilter(filter);
                }
            }
        }

        final TransportClientFactory zkEurekaHttpClientFactory = new ZKEurekaHttpClientFactory(providedJerseyClient, -1,
                Collections.singletonMap(ZKEurekaHttpClientFactory.ZK_HTTP_X_DISCOVERY_ALLOW_REDIRECT, "false"),
                this.zkSerCenEncrypt);
//        final TransportClientFactory jerseyFactory = new ZKEurekaHttpClientFactory(providedJerseyClient, false);
        final TransportClientFactory metricsFactory = MetricsCollectingEurekaHttpClient
                .createFactory(zkEurekaHttpClientFactory);

        return new TransportClientFactory() {
            @Override
            public EurekaHttpClient newClient(EurekaEndpoint serviceUrl) {
                return metricsFactory.newClient(serviceUrl);
            }

            @Override
            public void shutdown() {
                metricsFactory.shutdown();
                zkEurekaHttpClientFactory.shutdown();
            }
        };
    }

    @Override
    public TransportClientFactory newTransportClientFactory(EurekaClientConfig clientConfig,
            Collection<ClientFilter> additionalFilters, InstanceInfo myInstanceInfo) {
        return newTransportClientFactory(clientConfig, additionalFilters, myInstanceInfo, Optional.empty(),
                Optional.empty());
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: newTransportClientFactory
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param clientConfig
     * @param additionalFilters
     * @param myInstanceInfo
     * @param sslContext
     * @param hostnameVerifier
     * @return
     * @see com.netflix.discovery.shared.transport.jersey.TransportClientFactories#newTransportClientFactory(com.netflix.discovery.EurekaClientConfig,
     *      java.util.Collection, com.netflix.appinfo.InstanceInfo,
     *      java.util.Optional, java.util.Optional)
     */
    @Override
    public TransportClientFactory newTransportClientFactory(EurekaClientConfig clientConfig,
            Collection<ClientFilter> additionalFilters, InstanceInfo myInstanceInfo, Optional<SSLContext> sslContext,
            Optional<HostnameVerifier> hostnameVerifier) {

        final TransportClientFactory zkEurekaHttpClientFactory = ZKEurekaHttpClientFactory.create(this.zkSerCenEncrypt,
                clientConfig, additionalFilters, myInstanceInfo, new EurekaClientIdentity(myInstanceInfo.getIPAddr()),
                sslContext, hostnameVerifier);

        final TransportClientFactory metricsFactory = MetricsCollectingEurekaHttpClient
                .createFactory(zkEurekaHttpClientFactory);

        return new TransportClientFactory() {
            @Override
            public EurekaHttpClient newClient(EurekaEndpoint serviceUrl) {
                return metricsFactory.newClient(serviceUrl);
            }

            @Override
            public void shutdown() {
                metricsFactory.shutdown();
                zkEurekaHttpClientFactory.shutdown();
            }
        };
    }


}
