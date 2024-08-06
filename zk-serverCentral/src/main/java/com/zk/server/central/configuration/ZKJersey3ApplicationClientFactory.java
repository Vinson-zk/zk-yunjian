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
* @Title: ZKJersey3ApplicationClientFactory.java 
* @author Vinson 
* @Package com.zk.server.central.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 16, 2024 1:18:04 AM 
* @version V1.0 
*/
package com.zk.server.central.configuration;

import static com.netflix.discovery.util.DiscoveryBuildInfo.buildVersion;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.message.GZipEncoder;

import com.netflix.appinfo.AbstractEurekaIdentity;
import com.netflix.appinfo.EurekaAccept;
import com.netflix.appinfo.EurekaClientIdentity;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.provider.DiscoveryJerseyProvider;
import com.netflix.discovery.shared.resolver.EurekaEndpoint;
import com.netflix.discovery.shared.transport.EurekaClientFactoryBuilder;
import com.netflix.discovery.shared.transport.EurekaHttpClient;
import com.netflix.discovery.shared.transport.jersey3.Jersey3ApplicationClientFactory;
import com.netflix.discovery.shared.transport.jersey3.Jersey3EurekaIdentityHeaderFilter;
import com.zk.framework.serCen.ZKSerCenEncrypt;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

/** 
* @ClassName: ZKJersey3ApplicationClientFactory 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKJersey3ApplicationClientFactory extends Jersey3ApplicationClientFactory {

    private static final String KEY_STORE_TYPE = "JKS";

    private ZKSerCenEncrypt zkSerCenEncrypt;

    private final Client jersey3Client;

    private final MultivaluedMap<String, Object> additionalHeaders;

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param jersey3Client
     * @param additionalHeaders
     */
    public ZKJersey3ApplicationClientFactory(Client jersey3Client,
            MultivaluedMap<String, Object> additionalHeaders) {
        super(jersey3Client, additionalHeaders); // TODO Auto-generated constructor stub
        this.jersey3Client = jersey3Client;
        this.additionalHeaders = additionalHeaders;
    }

    @Override
    public EurekaHttpClient newClient(EurekaEndpoint endpoint) {
        return new ZKJersey3ApplicationClient(this.zkSerCenEncrypt, jersey3Client, endpoint.getServiceUrl(),
                additionalHeaders);
    }

    /**
     * @return zkSerCenEncrypt sa
     */
    public ZKSerCenEncrypt getZkSerCenEncrypt() {
        return zkSerCenEncrypt;
    }

    /**
     * @param zkSerCenEncrypt
     *            the zkSerCenEncrypt to set
     */
    public void setZkSerCenEncrypt(ZKSerCenEncrypt zkSerCenEncrypt) {
        this.zkSerCenEncrypt = zkSerCenEncrypt;
    }

    public static ZKJersey3ApplicationClientFactory create(EurekaClientConfig clientConfig,
            Collection<ClientRequestFilter> additionalFilters, InstanceInfo myInstanceInfo,
            AbstractEurekaIdentity clientIdentity) {
        return create(clientConfig, additionalFilters, myInstanceInfo, clientIdentity, Optional.empty(),
                Optional.empty());
    }

    @SuppressWarnings("deprecation")
    public static ZKJersey3ApplicationClientFactory create(EurekaClientConfig clientConfig,
            Collection<ClientRequestFilter> additionalFilters, InstanceInfo myInstanceInfo,
            AbstractEurekaIdentity clientIdentity, Optional<SSLContext> sslContext,
            Optional<HostnameVerifier> hostnameVerifier) {
        ZKJersey3ApplicationClientFactoryBuilder clientBuilder = new ZKJersey3ApplicationClientFactoryBuilder();
        clientBuilder.withAdditionalFilters(additionalFilters);
        clientBuilder.withMyInstanceInfo(myInstanceInfo);
        clientBuilder.withUserAgent("Java-EurekaClient");
        clientBuilder.withClientConfig(clientConfig);
        clientBuilder.withClientIdentity(clientIdentity);

        sslContext.ifPresent(clientBuilder::withSSLContext);
        hostnameVerifier.ifPresent(clientBuilder::withHostnameVerifier);

        if ("true".equals(System.getProperty("com.netflix.eureka.shouldSSLConnectionsUseSystemSocketFactory"))) {
            clientBuilder.withClientName("DiscoveryClient-HTTPClient-System").withSystemSSLConfiguration();
        }
        else if (clientConfig.getProxyHost() != null && clientConfig.getProxyPort() != null) {
            clientBuilder.withClientName("Proxy-DiscoveryClient-HTTPClient").withProxy(clientConfig.getProxyHost(),
                    Integer.parseInt(clientConfig.getProxyPort()), clientConfig.getProxyUserName(),
                    clientConfig.getProxyPassword());
        }
        else {
            clientBuilder.withClientName("DiscoveryClient-HTTPClient");
        }

        return clientBuilder.build();
    }

//    public static ZKJersey3ApplicationClientFactoryBuilder newBuilder() {
//        return new ZKJersey3ApplicationClientFactoryBuilder();
//    }

    public static class ZKJersey3ApplicationClientFactoryBuilder extends
            EurekaClientFactoryBuilder<Jersey3ApplicationClientFactory, Jersey3ApplicationClientFactoryBuilder> {

        private List<Feature> features = new ArrayList<>();

        private List<ClientRequestFilter> additionalFilters = new ArrayList<>();

        public ZKJersey3ApplicationClientFactoryBuilder withFeature(Feature feature) {
            features.add(feature);
            return this;
        }

        ZKJersey3ApplicationClientFactoryBuilder withAdditionalFilters(
                Collection<ClientRequestFilter> additionalFilters) {
            if (additionalFilters != null) {
                this.additionalFilters.addAll(additionalFilters);
            }
            return this;
        }

        @Override
        public ZKJersey3ApplicationClientFactory build() {
            ClientBuilder clientBuilder = ClientBuilder.newBuilder();
            ClientConfig clientConfig = new ClientConfig();

            for (ClientRequestFilter filter : additionalFilters) {
                clientConfig.register(filter);
            }

            for (Feature feature : features) {
                clientConfig.register(feature);
            }

            addProviders(clientConfig);
            addSSLConfiguration(clientBuilder);
            addProxyConfiguration(clientConfig);

            if (hostnameVerifier != null) {
                clientBuilder.hostnameVerifier(hostnameVerifier);
            }

            // Common properties to all clients
            final String fullUserAgentName = (userAgent == null ? clientName : userAgent) + "/v" + buildVersion();
            clientBuilder.register(new ClientRequestFilter() { // Can we do it better, without filter?
                @Override
                public void filter(ClientRequestContext requestContext) {
                    requestContext.getHeaders().put(HttpHeaders.USER_AGENT,
                            Collections.<Object> singletonList(fullUserAgentName));
                }
            });
            clientConfig.property(ClientProperties.FOLLOW_REDIRECTS, allowRedirect);
            clientConfig.property(ClientProperties.READ_TIMEOUT, readTimeout);
            clientConfig.property(ClientProperties.CONNECT_TIMEOUT, connectionTimeout);

            clientBuilder.withConfig(clientConfig);

            // Add gzip content encoding support
            clientBuilder.register(new GZipEncoder());

            // always enable client identity headers
            String ip = myInstanceInfo == null ? null : myInstanceInfo.getIPAddr();
            final AbstractEurekaIdentity identity = clientIdentity == null ? new EurekaClientIdentity(ip)
                : clientIdentity;
            clientBuilder.register(new Jersey3EurekaIdentityHeaderFilter(identity));

            JerseyClient jerseyClient = (JerseyClient) clientBuilder.build();

            MultivaluedMap<String, Object> additionalHeaders = new MultivaluedHashMap<>();
            if (allowRedirect) {
                additionalHeaders.add(HTTP_X_DISCOVERY_ALLOW_REDIRECT, "true");
            }
            if (EurekaAccept.compact == eurekaAccept) {
                additionalHeaders.add(EurekaAccept.HTTP_X_EUREKA_ACCEPT, eurekaAccept.name());
            }

            return new ZKJersey3ApplicationClientFactory(jerseyClient, additionalHeaders);
        }

        private void addSSLConfiguration(ClientBuilder clientBuilder) {
            FileInputStream fin = null;
            try {
                if (systemSSL) {
                    clientBuilder.sslContext(SSLContext.getDefault());
                }
                else if (trustStoreFileName != null) {
                    KeyStore trustStore = KeyStore.getInstance(KEY_STORE_TYPE);
                    fin = new FileInputStream(trustStoreFileName);
                    trustStore.load(fin, trustStorePassword.toCharArray());
                    clientBuilder.trustStore(trustStore);
                }
                else if (sslContext != null) {
                    clientBuilder.sslContext(sslContext);
                }
            }
            catch(Exception ex) {
                throw new IllegalArgumentException("Cannot setup SSL for Jersey3 client", ex);
            } finally {
                if (fin != null) {
                    try {
                        fin.close();
                    }
                    catch(IOException ignore) {
                    }
                }
            }
        }

        private void addProxyConfiguration(ClientConfig clientConfig) {
            if (proxyHost != null) {
                String proxyAddress = proxyHost;
                if (proxyPort > 0) {
                    proxyAddress += ':' + proxyPort;
                }
                clientConfig.property(ClientProperties.PROXY_URI, proxyAddress);
                if (proxyUserName != null) {
                    if (proxyPassword == null) {
                        throw new IllegalArgumentException("Proxy user name provided but not password");
                    }
                    clientConfig.property(ClientProperties.PROXY_USERNAME, proxyUserName);
                    clientConfig.property(ClientProperties.PROXY_PASSWORD, proxyPassword);
                }
            }
        }

        private void addProviders(ClientConfig clientConfig) {
            DiscoveryJerseyProvider discoveryJerseyProvider = new DiscoveryJerseyProvider(encoderWrapper,
                    decoderWrapper);
            clientConfig.register(discoveryJerseyProvider);

            // Disable json autodiscovery, since json (de)serialization is provided by DiscoveryJerseyProvider
            clientConfig.property(ClientProperties.JSON_PROCESSING_FEATURE_DISABLE, Boolean.TRUE);
            clientConfig.property(ClientProperties.MOXY_JSON_FEATURE_DISABLE, Boolean.TRUE);
        }
    }

}
