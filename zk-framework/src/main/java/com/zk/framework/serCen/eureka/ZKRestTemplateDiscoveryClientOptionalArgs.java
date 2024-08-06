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
* @Title: ZKRestTemplateDiscoveryClientOptionalArgs.java 
* @author Vinson 
* @Package com.zk.framework.serCen.eureka 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 15, 2024 10:42:21 PM 
* @version V1.0 
*/
package com.zk.framework.serCen.eureka;

import java.util.function.Supplier;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.eureka.http.EurekaClientHttpRequestFactorySupplier;

import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;

/** 
* @ClassName: ZKRestTemplateDiscoveryClientOptionalArgs 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
//public class RestTemplateDiscoveryClientOptionalArgs extends AbstractDiscoveryClientOptionalArgs<Void> {
public class ZKRestTemplateDiscoveryClientOptionalArgs extends AbstractDiscoveryClientOptionalArgs<Void> {

    protected final EurekaClientHttpRequestFactorySupplier eurekaClientHttpRequestFactorySupplier;

    protected final Supplier<RestTemplateBuilder> restTemplateBuilderSupplier;

    public ZKRestTemplateDiscoveryClientOptionalArgs(
            EurekaClientHttpRequestFactorySupplier eurekaClientHttpRequestFactorySupplier) {
        this(eurekaClientHttpRequestFactorySupplier, RestTemplateBuilder::new);
    }

    public ZKRestTemplateDiscoveryClientOptionalArgs(
            EurekaClientHttpRequestFactorySupplier eurekaClientHttpRequestFactorySupplier,
            Supplier<RestTemplateBuilder> restTemplateBuilderSupplier) {
        this.eurekaClientHttpRequestFactorySupplier = eurekaClientHttpRequestFactorySupplier;
        this.restTemplateBuilderSupplier = restTemplateBuilderSupplier;
    }

}
