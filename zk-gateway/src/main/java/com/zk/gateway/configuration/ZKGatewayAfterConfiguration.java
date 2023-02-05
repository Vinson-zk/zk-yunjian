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
* @Title: ZKGatewayAfterConfiguration.java 
* @author Vinson 
* @Package com.zk.gateway.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 14, 2022 7:06:51 PM 
* @version V1.0 
*/
package com.zk.gateway.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName: ZKGatewayAfterConfiguration
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Configuration
@AutoConfigureAfter(value = {
        EnableWebMvcConfiguration.class,
        ServletWebServerFactoryAutoConfiguration.class,
})
public class ZKGatewayAfterConfiguration implements WebMvcConfigurer {

//    @Autowired
//    private ZKLogAccessInterceptor logAccessInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(this.logAccessInterceptor).addPathPatterns("/**");
//    }

}
