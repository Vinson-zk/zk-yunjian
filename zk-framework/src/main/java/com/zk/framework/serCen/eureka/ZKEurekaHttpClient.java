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
* @Title: ZKEurekaHttpClient.java 
* @author Vinson 
* @Package com.zk.framework.serCen.eureka 
* @Description: TODO(simple description this file what to do.) 
* @date Jul 2, 2020 5:17:43 PM 
* @version V1.0 
*/
package com.zk.framework.serCen.eureka;

import static com.netflix.discovery.shared.transport.EurekaHttpResponse.anEurekaHttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.netflix.discovery.shared.transport.EurekaHttpClient;
import com.netflix.discovery.shared.transport.EurekaHttpResponse;
import com.netflix.discovery.shared.transport.EurekaHttpResponse.EurekaHttpResponseBuilder;
import com.netflix.discovery.util.StringUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStreamUtils;
import com.zk.framework.serCen.ZKSerCenEncrypt;

/** 
* @ClassName: ZKEurekaHttpClient 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKEurekaHttpClient implements EurekaHttpClient {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    protected final Client jerseyClient;

    protected final String serviceUrl;

    private ZKSerCenEncrypt zkSerCenEncrypt;

    private final Map<String, String> additionalHeaders;

    protected ZKEurekaHttpClient(ZKSerCenEncrypt zkSerCenEncrypt, Client jerseyClient, String serviceUrl,
            Map<String, String> additionalHeaders) {
        this.jerseyClient = jerseyClient;
        this.serviceUrl = serviceUrl;
        this.zkSerCenEncrypt = zkSerCenEncrypt;
        this.additionalHeaders = additionalHeaders;
        log.debug("Created client for url: {}", serviceUrl);
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

        log.info("[^_^:20200701-1048-001] 添加 zk 请求头: {}", ZKJsonUtils.writeObjectJson(headers));
    }

    protected void preClientResponse(ClientResponse res) {
        if (res.getStatus() == 601) {
//            Object obj = res.getEntity();
//            System.out.println("======= " + ZKJsonUtils.writeObjectJson(obj));
            String resStr = readResponseStr(res);
            log.error("[>_<:20200702-1705-001] 服务注册失败; resStr:{}", resStr);
        }

    }

    private String readResponseStr(ClientResponse res) {
        byte[] bs = readResponse(res);
        return new String(bs);
    }

    private byte[] readResponse(ClientResponse res) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = res.getEntityInputStream();
        try {
            ZKStreamUtils.readAndWrite(is, baos);
            return baos.toByteArray();
        }
        catch(Exception e) {
            log.error("[>_<:20200706-1706-001] 读取 CloseableHttpResponse 失败；errMsg: {}", e.getMessage());
            e.printStackTrace();
        } finally {
            ZKStreamUtils.closeStream(is);
            ZKStreamUtils.closeStream(baos);
        }
        return new byte[0];
    }

    /**********************************************************************************/

    @Override
    public EurekaHttpResponse<Void> register(InstanceInfo info) {
        String urlPath = "apps/" + info.getAppName();
        ClientResponse response = null;
        try {
            Builder resourceBuilder = jerseyClient.resource(serviceUrl).path(urlPath).getRequestBuilder();
            addExtraHeaders(resourceBuilder);
            response = resourceBuilder.header("Accept-Encoding", "gzip").type(MediaType.APPLICATION_JSON_TYPE)
                    .accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, info);
            // Vinson 预处理响应
            preClientResponse(response);

            return anEurekaHttpResponse(response.getStatus()).headers(headersOf(response)).build();
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Jersey HTTP POST {}/{} with instance {}; statusCode={}", serviceUrl, urlPath,
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
        ClientResponse response = null;
        try {
            Builder resourceBuilder = jerseyClient.resource(serviceUrl).path(urlPath).getRequestBuilder();
            addExtraHeaders(resourceBuilder);
            response = resourceBuilder.delete(ClientResponse.class);
            // Vinson 预处理响应
            preClientResponse(response);

            return anEurekaHttpResponse(response.getStatus()).headers(headersOf(response)).build();
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Jersey HTTP DELETE {}/{}; statusCode={}", serviceUrl, urlPath,
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
        ClientResponse response = null;
        try {
            WebResource webResource = jerseyClient.resource(serviceUrl).path(urlPath)
                    .queryParam("status", info.getStatus().toString())
                    .queryParam("lastDirtyTimestamp", info.getLastDirtyTimestamp().toString());
            if (overriddenStatus != null) {
                webResource = webResource.queryParam("overriddenstatus", overriddenStatus.name());
            }
            Builder requestBuilder = webResource.getRequestBuilder();
            addExtraHeaders(requestBuilder);
            response = requestBuilder.put(ClientResponse.class);
            // Vinson 预处理响应
            preClientResponse(response);

            EurekaHttpResponseBuilder<InstanceInfo> eurekaResponseBuilder = anEurekaHttpResponse(response.getStatus(),
                    InstanceInfo.class).headers(headersOf(response));
            if (response.hasEntity()) {
                eurekaResponseBuilder.entity(response.getEntity(InstanceInfo.class));
            }
            return eurekaResponseBuilder.build();
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Jersey HTTP PUT {}/{}; statusCode={}", serviceUrl, urlPath,
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
        ClientResponse response = null;
        try {
            Builder requestBuilder = jerseyClient.resource(serviceUrl).path(urlPath)
                    .queryParam("value", newStatus.name())
                    .queryParam("lastDirtyTimestamp", info.getLastDirtyTimestamp().toString()).getRequestBuilder();
            addExtraHeaders(requestBuilder);
            response = requestBuilder.put(ClientResponse.class);
            // Vinson 预处理响应
            preClientResponse(response);

            return anEurekaHttpResponse(response.getStatus()).headers(headersOf(response)).build();
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Jersey HTTP PUT {}/{}; statusCode={}", serviceUrl, urlPath,
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
        ClientResponse response = null;
        try {
            Builder requestBuilder = jerseyClient.resource(serviceUrl).path(urlPath)
                    .queryParam("lastDirtyTimestamp", info.getLastDirtyTimestamp().toString()).getRequestBuilder();
            addExtraHeaders(requestBuilder);
            response = requestBuilder.delete(ClientResponse.class);
            // Vinson 预处理响应
            preClientResponse(response);

            return anEurekaHttpResponse(response.getStatus()).headers(headersOf(response)).build();
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Jersey HTTP DELETE {}/{}; statusCode={}", serviceUrl, urlPath,
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

    private EurekaHttpResponse<Applications> getApplicationsInternal(String urlPath, String[] regions) {
        ClientResponse response = null;
        String regionsParamValue = null;
        try {
            WebResource webResource = jerseyClient.resource(serviceUrl).path(urlPath);
            if (regions != null && regions.length > 0) {
                regionsParamValue = StringUtil.join(regions);
                webResource = webResource.queryParam("regions", regionsParamValue);
            }
            Builder requestBuilder = webResource.getRequestBuilder();
            addExtraHeaders(requestBuilder);
            response = requestBuilder.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
            // Vinson 预处理响应
            preClientResponse(response);

            Applications applications = null;
            if (response.getStatus() == Status.OK.getStatusCode() && response.hasEntity()) {
                applications = response.getEntity(Applications.class);
            }
            return anEurekaHttpResponse(response.getStatus(), Applications.class).headers(headersOf(response))
                    .entity(applications).build();
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Jersey HTTP GET {}/{}?{}; statusCode={}", serviceUrl, urlPath,
                        regionsParamValue == null ? "" : "regions=" + regionsParamValue,
                        response == null ? "N/A" : response.getStatus());
            }
            if (response != null) {
                response.close();
            }
        }
    }

    @Override
    public EurekaHttpResponse<Application> getApplication(String appName) {
        String urlPath = "apps/" + appName;
        ClientResponse response = null;
        try {
            Builder requestBuilder = jerseyClient.resource(serviceUrl).path(urlPath).getRequestBuilder();
            addExtraHeaders(requestBuilder);
            response = requestBuilder.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);

            // Vinson 预处理响应
            preClientResponse(response);

            Application application = null;
            if (response.getStatus() == Status.OK.getStatusCode() && response.hasEntity()) {
                application = response.getEntity(Application.class);
            }
            return anEurekaHttpResponse(response.getStatus(), Application.class).headers(headersOf(response))
                    .entity(application).build();
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Jersey HTTP GET {}/{}; statusCode={}", serviceUrl, urlPath,
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
        ClientResponse response = null;
        try {
            Builder requestBuilder = jerseyClient.resource(serviceUrl).path(urlPath).getRequestBuilder();
            addExtraHeaders(requestBuilder);
            response = requestBuilder.accept(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
            // Vinson 预处理响应
            preClientResponse(response);

            InstanceInfo infoFromPeer = null;
            if (response.getStatus() == Status.OK.getStatusCode() && response.hasEntity()) {
                infoFromPeer = response.getEntity(InstanceInfo.class);
            }
            return anEurekaHttpResponse(response.getStatus(), InstanceInfo.class).headers(headersOf(response))
                    .entity(infoFromPeer).build();
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Jersey HTTP GET {}/{}; statusCode={}", serviceUrl, urlPath,
                        response == null ? "N/A" : response.getStatus());
            }
            if (response != null) {
                response.close();
            }
        }
    }

    @Override
    public void shutdown() {
        // Do not destroy jerseyClient, as it is owned by the corresponding
        // EurekaHttpClientFactory
    }

    private static Map<String, String> headersOf(ClientResponse response) {
        MultivaluedMap<String, String> jerseyHeaders = response.getHeaders();
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