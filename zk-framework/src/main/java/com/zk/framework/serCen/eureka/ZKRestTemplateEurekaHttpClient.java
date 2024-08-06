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
* @Title: ZKRestTemplateEurekaHttpClient.java 
* @author Vinson 
* @Package com.zk.framework.serCen.eureka 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 15, 2024 10:35:40 PM 
* @version V1.0 
*/
package com.zk.framework.serCen.eureka;

import static com.netflix.discovery.shared.transport.EurekaHttpResponse.anEurekaHttpResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.netflix.eureka.http.EurekaApplications;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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

/** 
* @ClassName: ZKRestTemplateEurekaHttpClient 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
//public class ZKRestTemplateEurekaHttpClient extends RestTemplateEurekaHttpClient {
public class ZKRestTemplateEurekaHttpClient implements EurekaHttpClient {

    // Vinson 添加 ==========================================================

    protected Logger log = LogManager.getLogger(this.getClass());

    private ZKSerCenEncrypt zkSerCenEncrypt;

    protected HttpHeaders addExtraHeaders(HttpHeaders headers) {
        headers = headers == null ? new HttpHeaders() : headers;

        Map<String, String> zkEncryptHeaderValues = zkSerCenEncrypt.encrypt();
        for (Entry<String, String> entry : zkEncryptHeaderValues.entrySet()) {
            headers.add(entry.getKey(), entry.getValue());
        }
        log.info("[^_^:20200701-1048-001] 添加 zk 请求头: {}", ZKJsonUtils.toJsonStr(headers));
        return headers;
    }

    protected void preClientResponse(ResponseEntity<?> response) {
        if (response.getStatusCode().value() == 601) {
//            Object obj = res.getEntity();
//            System.out.println("======= " + ZKJsonUtils.toJsonStr(obj));
            String resStr = readResponseStr(response);
            log.error("[>_<:20200702-1705-001] 服务注册失败; resStr:{}", resStr);
        }
    }

    private String readResponseStr(ResponseEntity<?> response) {
        byte[] bs = readResponse(response);
        return new String(bs);
    }

    private byte[] readResponse(ResponseEntity<?> response) {
        Object resObj = response.getBody();
        System.out.println("------ 2020000000 resObj: " + resObj.getClass());
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        InputStream is = res.getEntityInputStream();
//        try {
//            ZKStreamUtils.readAndWrite(is, baos);
//            return baos.toByteArray();
//        }
//        catch(Exception e) {
//            log.error("[>_<:20200706-1706-001] 读取 CloseableHttpResponse 失败；errMsg: {}", e.getMessage());
//            e.printStackTrace();
//        } finally {
//            ZKStreamUtils.closeStream(is);
//            ZKStreamUtils.closeStream(baos);
//        }
        return new byte[0];
    }

    // 原 RestTemplateEurekaHttpClient 类内容 ==========================================================
    private final RestTemplate restTemplate;

    private String serviceUrl;

    public ZKRestTemplateEurekaHttpClient(ZKSerCenEncrypt zkSerCenEncrypt, RestTemplate restTemplate,
            String serviceUrl) {
        this.zkSerCenEncrypt = zkSerCenEncrypt;
        this.restTemplate = restTemplate;
        this.serviceUrl = serviceUrl;
        if (!serviceUrl.endsWith("/")) {
            this.serviceUrl = this.serviceUrl + "/";
        }
    }

    public String getServiceUrl() {
        return this.serviceUrl;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    @Override
    public EurekaHttpResponse<Void> register(InstanceInfo info) {
        String urlPath = serviceUrl + "apps/" + info.getAppName();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT_ENCODING, "gzip");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // Vinson 添加请求头，加密
        headers = addExtraHeaders(headers);
        ResponseEntity<Void> response = restTemplate.exchange(urlPath, HttpMethod.POST, new HttpEntity<>(info, headers),
                Void.class);
        // Vinson 请求响应预处理
        preClientResponse(response);

        return anEurekaHttpResponse(response.getStatusCode().value()).headers(headersOf(response)).build();
    }

    @Override
    public EurekaHttpResponse<Void> cancel(String appName, String id) {
        String urlPath = serviceUrl + "apps/" + appName + '/' + id;

        // Vinson 添加请求头，加密
        HttpHeaders headers = addExtraHeaders(null);
        // 原代码
//        ResponseEntity<Void> response = restTemplate.exchange(urlPath, HttpMethod.DELETE, null, Void.class);
        ResponseEntity<Void> response = restTemplate.exchange(urlPath, HttpMethod.DELETE,
                new HttpEntity<>(null, headers), Void.class);
        // Vinson 请求响应预处理
        preClientResponse(response);

        return anEurekaHttpResponse(response.getStatusCode().value()).headers(headersOf(response)).build();
    }

    @Override
    public EurekaHttpResponse<InstanceInfo> sendHeartBeat(String appName, String id, InstanceInfo info,
            InstanceStatus overriddenStatus) {
        String urlPath = serviceUrl + "apps/" + appName + '/' + id + "?status=" + info.getStatus().toString()
                + "&lastDirtyTimestamp=" + info.getLastDirtyTimestamp().toString()
                + (overriddenStatus != null ? "&overriddenstatus=" + overriddenStatus.name() : "");

        // Vinson 添加请求头，加密
        HttpHeaders headers = addExtraHeaders(null);
        // 原代码
//        ResponseEntity<InstanceInfo> response = restTemplate.exchange(urlPath, HttpMethod.PUT, null,
//                InstanceInfo.class);
        ResponseEntity<InstanceInfo> response = restTemplate.exchange(urlPath, HttpMethod.PUT,
                new HttpEntity<>(null, headers),
                InstanceInfo.class);
        // Vinson 请求响应预处理
        preClientResponse(response);

        EurekaHttpResponseBuilder<InstanceInfo> eurekaResponseBuilder = anEurekaHttpResponse(
                response.getStatusCode().value(), InstanceInfo.class).headers(headersOf(response));

        if (response.hasBody()) {
            eurekaResponseBuilder.entity(response.getBody());
        }

        return eurekaResponseBuilder.build();
    }

    @Override
    public EurekaHttpResponse<Void> statusUpdate(String appName, String id, InstanceStatus newStatus,
            InstanceInfo info) {
        String urlPath = serviceUrl + "apps/" + appName + '/' + id + "/status?value=" + newStatus.name()
                + "&lastDirtyTimestamp=" + info.getLastDirtyTimestamp().toString();

        // Vinson 添加请求头，加密
        HttpHeaders headers = addExtraHeaders(null);
        // 原代码
//        ResponseEntity<Void> response = restTemplate.exchange(urlPath, HttpMethod.PUT, null, Void.class);
        ResponseEntity<Void> response = restTemplate.exchange(urlPath, HttpMethod.PUT, new HttpEntity<>(null, headers),
                Void.class);
        // Vinson 请求响应预处理
        preClientResponse(response);

        return anEurekaHttpResponse(response.getStatusCode().value()).headers(headersOf(response)).build();
    }

    @Override
    public EurekaHttpResponse<Void> deleteStatusOverride(String appName, String id, InstanceInfo info) {
        String urlPath = serviceUrl + "apps/" + appName + '/' + id + "/status?lastDirtyTimestamp="
                + info.getLastDirtyTimestamp().toString();

        // Vinson 添加请求头，加密
        HttpHeaders headers = addExtraHeaders(null);
        // 原代码
//        ResponseEntity<Void> response = restTemplate.exchange(urlPath, HttpMethod.DELETE, null, Void.class);
        ResponseEntity<Void> response = restTemplate.exchange(urlPath, HttpMethod.DELETE,
                new HttpEntity<>(null, headers), Void.class);
        // Vinson 请求响应预处理
        preClientResponse(response);

        return anEurekaHttpResponse(response.getStatusCode().value()).headers(headersOf(response)).build();
    }

    @Override
    public EurekaHttpResponse<Applications> getApplications(String... regions) {
        return getApplicationsInternal("apps/", regions);
    }

    private EurekaHttpResponse<Applications> getApplicationsInternal(String urlPath, String[] regions) {
        String url = serviceUrl + urlPath;

        if (regions != null && regions.length > 0) {
            url = url + (urlPath.contains("?") ? "&" : "?") + "regions=" + StringUtil.join(regions);
        }

        // Vinson 添加请求头，加密
        HttpHeaders headers = addExtraHeaders(null);
        // 原代码
//        ResponseEntity<EurekaApplications> response = restTemplate.exchange(url, HttpMethod.GET, null,
//                EurekaApplications.class);
        ResponseEntity<EurekaApplications> response = restTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<>(null, headers),
                EurekaApplications.class);
        // Vinson 请求响应预处理
        preClientResponse(response);

        return anEurekaHttpResponse(response.getStatusCode().value(),
                response.getStatusCode().value() == HttpStatus.OK.value() && response.hasBody()
                    ? (Applications) response.getBody() : null).headers(headersOf(response)).build();
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
        String urlPath = serviceUrl + "apps/" + appName;

        // Vinson 添加请求头，加密
        HttpHeaders headers = addExtraHeaders(null);
        // 原代码
//        ResponseEntity<Application> response = restTemplate.exchange(urlPath, HttpMethod.GET, null, Application.class);
        ResponseEntity<Application> response = restTemplate.exchange(urlPath, HttpMethod.GET,
                new HttpEntity<>(null, headers), Application.class);
        // Vinson 请求响应预处理
        preClientResponse(response);

        Application application = response.getStatusCode().value() == HttpStatus.OK.value() && response.hasBody()
            ? response.getBody() : null;

        return anEurekaHttpResponse(response.getStatusCode().value(), application).headers(headersOf(response)).build();
    }

    @Override
    public EurekaHttpResponse<InstanceInfo> getInstance(String appName, String id) {
        return getInstanceInternal("apps/" + appName + '/' + id);
    }

    @Override
    public EurekaHttpResponse<InstanceInfo> getInstance(String id) {
        return getInstanceInternal("instances/" + id);
    }

    private EurekaHttpResponse<InstanceInfo> getInstanceInternal(String urlPath) {
        urlPath = serviceUrl + urlPath;

        // Vinson 添加请求头，加密
        HttpHeaders headers = addExtraHeaders(null);
        // 原代码
//        ResponseEntity<InstanceInfo> response = restTemplate.exchange(urlPath, HttpMethod.GET, null,
//                InstanceInfo.class);
        ResponseEntity<InstanceInfo> response = restTemplate.exchange(urlPath, HttpMethod.GET,
                new HttpEntity<>(null, headers),
                InstanceInfo.class);
        // Vinson 请求响应预处理
        preClientResponse(response);

        return anEurekaHttpResponse(response.getStatusCode().value(),
                response.getStatusCode().value() == HttpStatus.OK.value() && response.hasBody() ? response.getBody()
                    : null).headers(headersOf(response)).build();
    }

    @Override
    public void shutdown() {
        // Nothing to do
    }

    private static Map<String, String> headersOf(ResponseEntity<?> response) {
        HttpHeaders httpHeaders = response.getHeaders();
        if (httpHeaders == null || httpHeaders.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> headers = new HashMap<>();
        for (Entry<String, List<String>> entry : httpHeaders.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                headers.put(entry.getKey(), entry.getValue().get(0));
            }
        }
        return headers;
    }

}
