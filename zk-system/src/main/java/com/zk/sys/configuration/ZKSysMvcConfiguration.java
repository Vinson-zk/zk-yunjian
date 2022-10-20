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
* @Title: ZKSysMvcConfiguration.java 
* @author Vinson 
* @Package com.zk.sys.configuration 
* @Description: TODO(simple description this file what to do.) 
* @date Jul 16, 2020 3:24:20 PM 
* @version V1.0 
*/
package com.zk.sys.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zk.log.interceptor.ZKLogAccessInterceptor;

/** 
* @ClassName: ZKSysMvcConfiguration 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@Configuration
public class ZKSysMvcConfiguration implements WebMvcConfigurer{ 

    @Autowired
    private ZKLogAccessInterceptor logAccessInterceptor;
     
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.logAccessInterceptor).addPathPatterns("/**");
    } 

}
