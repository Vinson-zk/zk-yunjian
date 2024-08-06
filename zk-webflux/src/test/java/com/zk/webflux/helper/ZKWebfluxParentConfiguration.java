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
* @Title: ZKWebfluxParentConfiguration.java 
* @author Vinson 
* @Package com.zk.webflux.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 23, 2024 12:04:23 AM 
* @version V1.0 
*/
package com.zk.webflux.helper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.zk.core.commons.ZKFileTransfer;
import com.zk.core.commons.support.ZKDiskFileTransfer;
import com.zk.core.configuration.ZKEnableCoreReactive;

/** 
* @ClassName: ZKWebfluxParentConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@PropertySources(value = { //
        @PropertySource(ignoreResourceNotFound = true, encoding = "UTF-8", value = {
                "classpath:test.zk.webflux.properties" }), //
})
@ZKEnableCoreReactive
public class ZKWebfluxParentConfiguration {

    @Bean
    public ZKFileTransfer zkFileTransfer() {
        return new ZKDiskFileTransfer(-1, -1, -1, null);
    }
    
//    @Bean
//    public MultipartHttpMessageReader multipartHttpMessageReader() {
//        // 硬盘的
//        DefaultPartHttpMessageReader partReader = new DefaultPartHttpMessageReader();
//        partReader.setMaxParts(5);
//        partReader.setMaxDiskUsagePerPart(10 * 1024 * 1024);
////        partReader.setMaxHeadersSize(921600);
//        partReader.setEnableLoggingRequestDetails(true);
////        partReader.setStreaming(true);
//
//        MultipartHttpMessageReader multipartReader = new MultipartHttpMessageReader(partReader);
//        multipartReader.setEnableLoggingRequestDetails(true);
//        return multipartReader;
//    }

}

