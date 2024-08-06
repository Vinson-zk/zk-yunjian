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
* @Title: ZKShiroTestHelperConfiguration.java 
* @author Vinson 
* @Package com.zk.demo.shiro 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 8:59:50 AM 
* @version V1.0 
*/
package com.zk.demo.shiro;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.Ordered;

import com.zk.core.web.support.servlet.filter.ZKDelegatingFilterProxyRegistrationBean;
import com.zk.webmvc.configuration.ZKEnableWebmvc;

/**
 * @ClassName: ZKShiroTestHelperConfiguration
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@ImportResource(locations = { "classpath:shiro/test_spring_ctx_shiro.xml" })
@ZKEnableWebmvc
@AutoConfigureBefore(value = { //
        WebMvcAutoConfiguration.class //
})
public class ZKShiroTestHelperConfiguration {

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
