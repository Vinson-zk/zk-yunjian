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
* @Title: ZKJerseyApplicationClient.java 
* @author Vinson 
* @Package com.zk.framework.serCen.eureka 
* @Description: TODO(simple description this file what to do.) 
* @date May 3, 2020 12:46:23 AM 
* @version V1.0 
*/
package com.zk.framework.serCen.eureka;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.netflix.discovery.shared.transport.EurekaHttpResponse;
import com.netflix.discovery.shared.transport.jersey.JerseyApplicationClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource.Builder;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.framework.serCen.ZKSerCenEncrypt;

/** 
* @ClassName: ZKJerseyApplicationClient 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKJerseyApplicationClient extends JerseyApplicationClient {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    private ZKSerCenEncrypt zkSerCenEncrypt;

    public ZKJerseyApplicationClient(ZKSerCenEncrypt zkSerCenEncrypt, Client jerseyClient, String serviceUrl,
            Map<String, String> additionalHeaders) {
        super(jerseyClient, serviceUrl, additionalHeaders);
        this.zkSerCenEncrypt = zkSerCenEncrypt;
    }

    @Override
    protected void addExtraHeaders(Builder webResource) {
        super.addExtraHeaders(webResource);
        
        Map<String, String> headers = zkSerCenEncrypt.encrypt();
        headers.forEach((key, value) -> {
            webResource.header(key, value);
        });
        
        log.info("[^_^:20200701-1048-001] 添加 zk 请求头: {}", ZKJsonUtils.writeObjectJson(headers));
    }

    @Override
    public EurekaHttpResponse<Void> register(InstanceInfo info){
        EurekaHttpResponse<Void> res = super.register(info);
        preEurekaHttpResponse(res);
        return res;
    }

    @Override
    public EurekaHttpResponse<Void> cancel(String appName, String id) {
        EurekaHttpResponse<Void> res = super.cancel(appName, id);
        preEurekaHttpResponse(res);
        return res;
    }

    @Override
    public EurekaHttpResponse<InstanceInfo> sendHeartBeat(String appName, String id, InstanceInfo info,
            InstanceStatus overriddenStatus) {
        EurekaHttpResponse<InstanceInfo> res = super.sendHeartBeat(appName, id, info, overriddenStatus);
        preEurekaHttpResponse(res);
        return res;
    }

    @Override
    public EurekaHttpResponse<Void> statusUpdate(String appName, String id, InstanceStatus newStatus,
            InstanceInfo info) {
        EurekaHttpResponse<Void> res = super.register(info);
        preEurekaHttpResponse(res);
        return res;
    }

    @Override
    public EurekaHttpResponse<Void> deleteStatusOverride(String appName, String id, InstanceInfo info) {
        EurekaHttpResponse<Void> res = super.register(info);
        preEurekaHttpResponse(res);
        return res;
    }

    @Override
    public EurekaHttpResponse<Applications> getApplications(String... regions) {
        EurekaHttpResponse<Applications> res = super.getApplications(regions);
        preEurekaHttpResponse(res);
        return res;
    }

    @Override
    public EurekaHttpResponse<Applications> getDelta(String... regions) {
        EurekaHttpResponse<Applications> res = super.getDelta(regions);
        preEurekaHttpResponse(res);
        return res;
    }

    @Override
    public EurekaHttpResponse<Applications> getVip(String vipAddress, String... regions) {
        EurekaHttpResponse<Applications> res = super.getVip(vipAddress, regions);
        preEurekaHttpResponse(res);
        return res;
    }

    @Override
    public EurekaHttpResponse<Applications> getSecureVip(String secureVipAddress, String... regions) {
        EurekaHttpResponse<Applications> res = super.getSecureVip(secureVipAddress, regions);
        preEurekaHttpResponse(res);
        return res;
    }

    @Override
    public EurekaHttpResponse<Application> getApplication(String appName) {
        EurekaHttpResponse<Application> res = super.getApplication(appName);
        preEurekaHttpResponse(res);
        return res;
    }

    @Override
    public EurekaHttpResponse<InstanceInfo> getInstance(String appName, String id) {
        EurekaHttpResponse<InstanceInfo> res = super.getInstance(appName, id);
        preEurekaHttpResponse(res);
        return res;
    }

    @Override
    public EurekaHttpResponse<InstanceInfo> getInstance(String id) {
        EurekaHttpResponse<InstanceInfo> res = super.getInstance(id);
        preEurekaHttpResponse(res);
        return res;
    }

    protected void preEurekaHttpResponse(EurekaHttpResponse<?> res) {
        if (res.getStatusCode() == 601) {
//            Object obj = res.getEntity();
//            System.out.println("======= " + ZKJsonUtils.writeObjectJson(obj));
            log.error("[>_<:20200702-1705-001] 服务注册失败; heads:{}", ZKJsonUtils.writeObjectJson(res.getHeaders()));
        }

    }

}
