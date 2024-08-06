/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKSerCenConfiguration.java 
 * @author Vinson 
 * @Package com.zk.server.central.helper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:07:32 PM 
 * @version V1.0   
*/
package com.zk.server.central.helper;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

/** 
* @ClassName: ZKSerCenConfiguration 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSerCenConfiguration {

    @DependsOn(value = { "messageConverters" })
    @Bean
    public RestTemplateBuilder restTemplateBuilder(HttpMessageConverters messageConverters) {
        System.out.println("[^_^:20191028-1500-001] messageConverters: " + messageConverters.getConverters().size());

        for (HttpMessageConverter<?> mc : messageConverters.getConverters()) {
            for (MediaType mt : mc.getSupportedMediaTypes()) {
                System.out.println("======== " + mc.getClass() + " -> " + mt.toString());
//                System.out.println("======== " + mt.getType());
            }
        }

        return new RestTemplateBuilder().messageConverters(messageConverters.getConverters());
    }

}
