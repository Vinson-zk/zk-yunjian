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
 * @Title: ZKDemoConfigutation.java 
 * @author Vinson 
 * @Package com.zk.demo.configuration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 4:56:32 PM 
 * @version V1.0   
*/
package com.zk.demo.web.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import com.zk.core.configuration.ZKEnableCoreServlet;
import com.zk.core.web.encrypt.ZKSampleRsaAesTransferCipherManager;
import com.zk.core.web.encrypt.ZKTransferCipherManager;
import com.zk.core.web.support.servlet.filter.ZKTransferCipherFilter;
import com.zk.db.configuration.ZKDBProperties;
import com.zk.db.configuration.ZKEnableDB;
import com.zk.framework.serCen.support.ZKSerCenSampleCipher;

/** 
* @ClassName: ZKDemoConfigutation 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@AutoConfigureBefore(value = { ZKDemoServletContextInitializer.class })
@PropertySource(encoding = "UTF-8", value = { "classpath:datasource/test_database.properties" })
@ZKEnableCoreServlet
@ZKEnableDB(configLocation = "classpath:datasource/test_mybatis_settings.xml")
public class ZKDemoConfigutation {

    //
    @ConfigurationProperties(prefix = "zk.demo.dynamic.jdbc")
    @Bean
    public ZKDBProperties zkDBProperties() {
        return new ZKDBProperties();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ZKSampleRsaAesTransferCipherManager zkTransferCipherManager() {
        return new ZKSampleRsaAesTransferCipherManager();
    }

    @Bean
    public ZKTransferCipherFilter transferCipherFilter(ZKTransferCipherManager zkTransferCipherManager) {
        return new ZKTransferCipherFilter(zkTransferCipherManager);
    }

    @Bean
    public ZKSerCenSampleCipher zkSerCenSampleCipher() {
        return new ZKSerCenSampleCipher();
    }

}
