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
package com.zk.demo.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zk.core.encrypt.ZKTransferCipherManager;
import com.zk.core.encrypt.support.ZKSampleRsaAesTransferCipherManager;
import com.zk.core.web.filter.ZKTransferCipherFilter;

/** 
* @ClassName: ZKDemoConfigutation 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@Configuration
@AutoConfigureBefore(value = { ZKDemoServletContextInitializer.class })
public class ZKDemoConfigutation {

    @Bean
    public ZKSampleRsaAesTransferCipherManager zkTransferCipherManager() {
        return new ZKSampleRsaAesTransferCipherManager();
    }

    @Bean
    public ZKTransferCipherFilter transferCipherFilter(ZKTransferCipherManager zkTransferCipherManager) {
        return new ZKTransferCipherFilter(zkTransferCipherManager);
    }

}
