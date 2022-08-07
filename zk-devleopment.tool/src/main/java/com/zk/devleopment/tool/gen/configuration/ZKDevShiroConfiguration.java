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
* @Title: ZKCodeGenShiroConfiguration.java 
* @author Vinson 
* @Package com.zk.code.generate.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 29, 2021 7:10:38 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.Ordered;

import com.zk.core.web.filter.ZKDelegatingFilterProxyRegistrationBean;

/** 
* @ClassName: ZKCodeGenShiroConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
//@Configuration
@ImportResource(locations = { "classpath:xmlConfig/spring_ctx_shiro.xml" })
public class ZKCodeGenShiroConfiguration {

    /**
     * 拦截器 - DelegatingFilterProxyRegistrationBean
     *
     * @Title: zkServerServletContextInitializer
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Oct 28, 2019 4:11:07 PM
     * @return
     * @return ZKServerServletContextInitializer
     */
    @Bean
    public ZKDelegatingFilterProxyRegistrationBean shiroFilterProxyRegistrationBean() {
        String filterName = "shiroFilter";
        ZKDelegatingFilterProxyRegistrationBean zkDfprb = new ZKDelegatingFilterProxyRegistrationBean(filterName);
        zkDfprb.setName(filterName);
        zkDfprb.addUrlPatterns("/*");
        zkDfprb.setOrder(Ordered.LOWEST_PRECEDENCE);
        return zkDfprb;
    }

}
