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
* @Title: ZKWebFluxFileUploadConfiguration.java 
* @author Vinson 
* @Package com.zk.webflux.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 23, 2024 9:50:00 PM 
* @version V1.0 
*/
package com.zk.webflux.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.multipart.DefaultPartHttpMessageReader;
import org.springframework.http.codec.multipart.MultipartHttpMessageReader;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.config.WebFluxConfigurerComposite;

/** 
* @ClassName: ZKWebFluxFileUploadConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Component
public class ZKWebFluxFileUploadConfiguration extends WebFluxConfigurerComposite {

    @Autowired(required = false)
    ZKFileFluxProperties zkFileFluxProperties;

    public ZKFileFluxProperties getFileFluxProperties() {
        if (this.zkFileFluxProperties == null) {
            this.zkFileFluxProperties = new ZKFileFluxProperties();
        }
        return this.zkFileFluxProperties;
    }

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
//      ObjectMapper mapper = mapper();
//      configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(mapper));
//      configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(mapper));
        super.configureHttpMessageCodecs(configurer);
        // 硬盘的
        DefaultPartHttpMessageReader partReader = new DefaultPartHttpMessageReader();
        partReader.setMaxParts(this.getFileFluxProperties().getMaxParts());
        partReader.setMaxDiskUsagePerPart(this.getFileFluxProperties().getMaxDiskUsagePerPart());
        partReader.setMaxHeadersSize(this.getFileFluxProperties().getMaxHeadersSize());
        partReader.setMaxInMemorySize(this.getFileFluxProperties().getMaxInMemorySize());
        partReader.setEnableLoggingRequestDetails(true);
//      partReader.setStreaming(true);

        MultipartHttpMessageReader multipartReader = new MultipartHttpMessageReader(partReader);
        multipartReader.setEnableLoggingRequestDetails(false); // true

        configurer.defaultCodecs().multipartReader(multipartReader);
    }

//    @Override
//    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
////        ObjectMapper mapper = mapper();
////        configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(mapper));
////        configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(mapper));
//        super.configureHttpMessageCodecs(configurer);
//        // 硬盘的
//        DefaultPartHttpMessageReader partReader = new DefaultPartHttpMessageReader();
//        partReader.setMaxParts(5);
//        partReader.setMaxDiskUsagePerPart(10 * 1024 * 1024);
////        partReader.setMaxHeadersSize(921600);
//        partReader.setEnableLoggingRequestDetails(true);
////        partReader.setStreaming(true);
//
//        MultipartHttpMessageReader multipartReader = new MultipartHttpMessageReader(partReader);
//        multipartReader.setEnableLoggingRequestDetails(false); // true
//
//        configurer.defaultCodecs().multipartReader(multipartReader);
//    }

}
