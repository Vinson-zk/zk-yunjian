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
* @Title: ZKJersey3ApplicationClient.java 
* @author Vinson 
* @Package com.zk.server.central.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 16, 2024 1:06:02 AM 
* @version V1.0 
*/
package com.zk.server.central.configuration;

import static com.netflix.discovery.shared.transport.EurekaHttpResponse.anEurekaHttpResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.netflix.discovery.shared.transport.EurekaHttpClient;
import com.netflix.discovery.shared.transport.EurekaHttpResponse;
import com.netflix.discovery.shared.transport.EurekaHttpResponse.EurekaHttpResponseBuilder;
import com.netflix.discovery.util.StringUtil;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.framework.serCen.ZKSerCenEncrypt;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/** 
* @ClassName: ZKJersey3ApplicationClient 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
//public class ZKJersey3ApplicationClient extends Jersey3ApplicationClient {
public class ZKJersey3ApplicationClient implements EurekaHttpClient {

    protected Logger logger = LogManager.getLogger(this.getClass());

    private ZKSerCenEncrypt zkSerCenEncrypt;

    private MultivaluedMap<String, Object> additionalHeaders;

    public ZKJersey3ApplicationClient(ZKSerCenEncrypt zkSerCenEncrypt, Client jerseyClient, String serviceUrl,
            MultivaluedMap<String, Object> additionalHeaders) {
        this(jerseyClient, serviceUrl);
        this.additionalHeaders = additionalHeaders;
        this.zkSerCenEncrypt = zkSerCenEncrypt;
    }

    protected void addExtraHeaders(Builder webResource) {
        if (additionalHeaders != null) {
            for (String key : additionalHeaders.keySet()) {
                webResource.header(key, additionalHeaders.get(key));
            }
        }

        Map<String, String> headers = zkSerCenEncrypt.encrypt();
        headers.forEach((key, value) -> {
            webResource.header(key, value);
        });

        logger.info("[^_^:20240616-0115-001] 添加 zk 请求头: {}", ZKJsonUtils.toJsonStr(headers));
    }

    protected void preClientResponse(Response res) {
        if (res.getStatus() == 601) {
//            Object obj = res.getEntity();
//            System.out.println("======= " + ZKJsonUtils.writeObjectJson(obj));
            String resStr = readResponseStr(res);
            logger.error("[>_<:20240616-0115-002] 服务注册失败; resStr:{}", resStr);
        }

    }

    private String readResponseStr(Response res) {
        byte[] bs = readResponse(res);
        return new String(bs);
    }

    private byte[] readResponse(Response res) {

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        InputStream is = res.get.getEntityInputStream();
//        try {
//            ZKStreamUtils.readAndWrite(is, baos);
//            return baos.toByteArray();
//        }
//        catch(Exception e) {
//            logger.error("[>_<:20200706-1706-001] 读取 CloseableHttpResponse 失败；errMsg: {}", e.getMessage());
//            e.printStackTrace();
//        } finally {
//            ZKStreamUtils.closeStream(is);
//            ZKStreamUtils.closeStream(baos);
//        }
        return new byte[0];
    }

//    protected void addExtraHeaders(Builder webResource) {
//        if (additionalHeaders != null) {
//            for (Map.Entry<String, List<Object>> entry : additionalHeaders.entrySet()) {
//                webResource.header(entry.getKey(), entry.getValue());
//            }
//        }
//    }

    // ---------------------------------------------------------------------------

    protected final Client jerseyClient;

    protected final String serviceUrl;

    private final String userName;

    private final String password;

    private ZKJersey3ApplicationClient(Client jerseyClient, String serviceUrl) {
        this.jerseyClient = jerseyClient;
        this.serviceUrl = serviceUrl;

        // Jersey3 does not read credentials from the URI. We extract it here and enable authentication feature.
        String localUserName = null;
        String localPassword = null;
        try {
            URI serviceURI = new URI(serviceUrl);
            if (serviceURI.getUserInfo() != null) {
                String[] credentials = serviceURI.getUserInfo().split(":");
                if (credentials.length == 2) {
                    localUserName = credentials[0];
                    localPassword = credentials[1];
                }
            }
        } catch (URISyntaxException ignore) {
        }
        this.userName = localUserName;
        this.password = localPassword;
        if (userName != null) {
            HttpAuthenticationFeature basicAuth = HttpAuthenticationFeature.basic(userName, password);
            this.jerseyClient.register(basicAuth);
        }
    }

    @Override
    public EurekaHttpResponse<Void> register(InstanceInfo info) {
        String urlPath = "apps/" + info.getAppName();
        Response response = null;
        try {
            Builder resourceBuilder = jerseyClient.target(serviceUrl).path(urlPath).request();
            addExtraProperties(resourceBuilder);
            addExtraHeaders(resourceBuilder);
            response = resourceBuilder.accept(MediaType.APPLICATION_JSON).acceptEncoding("gzip")
                    .post(Entity.json(info));
            // Vinson 请求响应预处理
            preClientResponse(response);
            return anEurekaHttpResponse(response.getStatus()).headers(headersOf(response)).build();
        } finally {
            if (logger.isDebugEnabled()) {
                logger.debug("Jersey3 HTTP POST {}/{} with instance {}; statusCode={}", serviceUrl, urlPath,
                        info.getId(), response == null ? "N/A" : response.getStatus());
            }
            if (response != null) {
                response.close();
            }
        }
    }

    @Override
    public EurekaHttpResponse<Void> cancel(String appName, String id) {
        String urlPath = "apps/" + appName + '/' + id;
        Response response = null;
        try {
            Builder resourceBuilder = jerseyClient.target(serviceUrl).path(urlPath).request();
            addExtraProperties(resourceBuilder);
            addExtraHeaders(resourceBuilder);
            response = resourceBuilder.delete();
            // Vinson 请求响应预处理
            preClientResponse(response);
            return anEurekaHttpResponse(response.getStatus()).headers(headersOf(response)).build();
        } finally {
            if (logger.isDebugEnabled()) {
                logger.debug("Jersey3 HTTP DELETE {}/{}; statusCode={}", serviceUrl, urlPath,
                        response == null ? "N/A" : response.getStatus());
            }
            if (response != null) {
                response.close();
            }
        }
    }

    @Override
    public EurekaHttpResponse<InstanceInfo> sendHeartBeat(String appName, String id, InstanceInfo info,
            InstanceStatus overriddenStatus) {
        String urlPath = "apps/" + appName + '/' + id;
        Response response = null;
        try {
            WebTarget webResource = jerseyClient.target(serviceUrl).path(urlPath)
                    .queryParam("status", info.getStatus().toString())
                    .queryParam("lastDirtyTimestamp", info.getLastDirtyTimestamp().toString());
            if (overriddenStatus != null) {
                webResource = webResource.queryParam("overriddenstatus", overriddenStatus.name());
            }
            Builder requestBuilder = webResource.request();
            addExtraProperties(requestBuilder);
            addExtraHeaders(requestBuilder);
            requestBuilder.accept(MediaType.APPLICATION_JSON_TYPE);
            response = requestBuilder.put(Entity.entity("{}", MediaType.APPLICATION_JSON_TYPE)); // Jersey3 refuses to handle PUT with no body
            // Vinson 请求响应预处理
            preClientResponse(response);
            EurekaHttpResponseBuilder<InstanceInfo> eurekaResponseBuilder = anEurekaHttpResponse(response.getStatus(),
                    InstanceInfo.class).headers(headersOf(response));
            if (response.hasEntity()) {
                eurekaResponseBuilder.entity(response.readEntity(InstanceInfo.class));
            }
            return eurekaResponseBuilder.build();
        } finally {
            if (logger.isDebugEnabled()) {
                logger.debug("Jersey3 HTTP PUT {}/{}; statusCode={}", serviceUrl, urlPath,
                        response == null ? "N/A" : response.getStatus());
            }
            if (response != null) {
                response.close();
            }
        }
    }

    @Override
    public EurekaHttpResponse<Void> statusUpdate(String appName, String id, InstanceStatus newStatus,
            InstanceInfo info) {
        String urlPath = "apps/" + appName + '/' + id + "/status";
        Response response = null;
        try {
            Builder requestBuilder = jerseyClient.target(serviceUrl).path(urlPath).queryParam("value", newStatus.name())
                    .queryParam("lastDirtyTimestamp", info.getLastDirtyTimestamp().toString()).request();
            addExtraProperties(requestBuilder);
            addExtraHeaders(requestBuilder);
            response = requestBuilder.put(Entity.entity("{}", MediaType.APPLICATION_JSON_TYPE)); // Jersey3 refuses to handle PUT with no body
            // Vinson 请求响应预处理
            preClientResponse(response);
            return anEurekaHttpResponse(response.getStatus()).headers(headersOf(response)).build();
        } finally {
            if (logger.isDebugEnabled()) {
                logger.debug("Jersey3 HTTP PUT {}/{}; statusCode={}", serviceUrl, urlPath,
                        response == null ? "N/A" : response.getStatus());
            }
            if (response != null) {
                response.close();
            }
        }
    }

    @Override
    public EurekaHttpResponse<Void> deleteStatusOverride(String appName, String id, InstanceInfo info) {
        String urlPath = "apps/" + appName + '/' + id + "/status";
        Response response = null;
        try {
            Builder requestBuilder = jerseyClient.target(serviceUrl).path(urlPath)
                    .queryParam("lastDirtyTimestamp", info.getLastDirtyTimestamp().toString()).request();
            addExtraProperties(requestBuilder);
            addExtraHeaders(requestBuilder);
            response = requestBuilder.delete();
            // Vinson 请求响应预处理
            preClientResponse(response);
            return anEurekaHttpResponse(response.getStatus()).headers(headersOf(response)).build();
        } finally {
            if (logger.isDebugEnabled()) {
                logger.debug("Jersey3 HTTP DELETE {}/{}; statusCode={}", serviceUrl, urlPath,
                        response == null ? "N/A" : response.getStatus());
            }
            if (response != null) {
                response.close();
            }
        }
    }

    @Override
    public EurekaHttpResponse<Applications> getApplications(String... regions) {
        return getApplicationsInternal("apps/", regions);
    }

    @Override
    public EurekaHttpResponse<Applications> getDelta(String... regions) {
        return getApplicationsInternal("apps/delta", regions);
    }

    @Override
    public EurekaHttpResponse<Applications> getVip(String vipAddress, String... regions) {
        return getApplicationsInternal("vips/" + vipAddress, regions);
    }

    @Override
    public EurekaHttpResponse<Applications> getSecureVip(String secureVipAddress, String... regions) {
        return getApplicationsInternal("svips/" + secureVipAddress, regions);
    }

    @Override
    public EurekaHttpResponse<Application> getApplication(String appName) {
        String urlPath = "apps/" + appName;
        Response response = null;
        try {
            Builder requestBuilder = jerseyClient.target(serviceUrl).path(urlPath).request();
            addExtraProperties(requestBuilder);
            addExtraHeaders(requestBuilder);
            response = requestBuilder.accept(MediaType.APPLICATION_JSON_TYPE).get();
            // Vinson 请求响应预处理
            preClientResponse(response);
            Application application = null;
            if (response.getStatus() == Status.OK.getStatusCode() && response.hasEntity()) {
                application = response.readEntity(Application.class);
            }
            return anEurekaHttpResponse(response.getStatus(), application).headers(headersOf(response)).build();
        } finally {
            if (logger.isDebugEnabled()) {
                logger.debug("Jersey3 HTTP GET {}/{}; statusCode={}", serviceUrl, urlPath,
                        response == null ? "N/A" : response.getStatus());
            }
            if (response != null) {
                response.close();
            }
        }
    }

    private EurekaHttpResponse<Applications> getApplicationsInternal(String urlPath, String[] regions) {
        Response response = null;
        try {
            WebTarget webTarget = jerseyClient.target(serviceUrl).path(urlPath);
            if (regions != null && regions.length > 0) {
                webTarget = webTarget.queryParam("regions", StringUtil.join(regions));
            }
            Builder requestBuilder = webTarget.request();
            addExtraProperties(requestBuilder);
            addExtraHeaders(requestBuilder);
            response = requestBuilder.accept(MediaType.APPLICATION_JSON_TYPE).get();
            // Vinson 请求响应预处理
            preClientResponse(response);
            Applications applications = null;
            if (response.getStatus() == Status.OK.getStatusCode() && response.hasEntity()) {
                applications = response.readEntity(Applications.class);
            }
            return anEurekaHttpResponse(response.getStatus(), applications).headers(headersOf(response)).build();
        } finally {
            if (logger.isDebugEnabled()) {
                logger.debug("Jersey3 HTTP GET {}/{}; statusCode={}", serviceUrl, urlPath,
                        response == null ? "N/A" : response.getStatus());
            }
            if (response != null) {
                response.close();
            }
        }
    }

    @Override
    public EurekaHttpResponse<InstanceInfo> getInstance(String id) {
        return getInstanceInternal("instances/" + id);
    }

    @Override
    public EurekaHttpResponse<InstanceInfo> getInstance(String appName, String id) {
        return getInstanceInternal("apps/" + appName + '/' + id);
    }

    private EurekaHttpResponse<InstanceInfo> getInstanceInternal(String urlPath) {
        Response response = null;
        try {
            Builder requestBuilder = jerseyClient.target(serviceUrl).path(urlPath).request();
            addExtraProperties(requestBuilder);
            addExtraHeaders(requestBuilder);
            response = requestBuilder.accept(MediaType.APPLICATION_JSON_TYPE).get();
            // Vinson 请求响应预处理
            preClientResponse(response);
            InstanceInfo infoFromPeer = null;
            if (response.getStatus() == Status.OK.getStatusCode() && response.hasEntity()) {
                infoFromPeer = response.readEntity(InstanceInfo.class);
            }
            return anEurekaHttpResponse(response.getStatus(), infoFromPeer).headers(headersOf(response)).build();
        } finally {
            if (logger.isDebugEnabled()) {
                logger.debug("Jersey3 HTTP GET {}/{}; statusCode={}", serviceUrl, urlPath,
                        response == null ? "N/A" : response.getStatus());
            }
            if (response != null) {
                response.close();
            }
        }
    }

    @Override
    public void shutdown() {
    }

    protected void addExtraProperties(Builder webResource) {
        if (userName != null) {
            webResource.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_USERNAME, userName)
                    .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_PASSWORD, password);
        }
    }

    private static Map<String, String> headersOf(Response response) {
        MultivaluedMap<String, String> jerseyHeaders = response.getStringHeaders();
        if (jerseyHeaders == null || jerseyHeaders.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> headers = new HashMap<>();
        for (Entry<String, List<String>> entry : jerseyHeaders.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                headers.put(entry.getKey(), entry.getValue().get(0));
            }
        }
        return headers;
    }

}
