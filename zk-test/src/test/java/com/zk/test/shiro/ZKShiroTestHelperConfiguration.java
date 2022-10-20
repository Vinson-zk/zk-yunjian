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
* @Package com.zk.test.shiro 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 8:59:50 AM 
* @version V1.0 
*/
package com.zk.test.shiro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.core.web.filter.ZKDelegatingFilterProxyRegistrationBean;
import com.zk.core.web.resolver.ZKExceptionHandlerResolver;
import com.zk.core.web.utils.ZKWebUtils;

/**
 * @ClassName: ZKShiroTestHelperConfiguration
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@ImportResource(locations = { "classpath:xmlConfig/spring_ctx_application.xml",
        "classpath:xmlConfig/spring_ctx_mvc.xml", "classpath:shiro/spring_ctx.xml",
        "classpath:shiro/spring_ctx_shiro.xml" })
public class ZKShiroTestHelperConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

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

    @Autowired
    public void before(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        System.out.println("[^_^:20210705-1808-001] ===== " + this.getClass() + " class before ");

        ZKEnvironmentUtils.initContext(applicationContext);
//        ZKLocaleUtils.setLocale(ZKLocaleUtils.valueOf("en_US"));
//        ZKLocaleUtils.setLocale(ZKLocaleUtils.valueOf("zh_CN"));
//        // # 默认语言；注意这里不影响到 localeResolver 的默认语言
        ZKWebUtils
                .setLocale(ZKLocaleUtils.distributeLocale(ZKEnvironmentUtils.getString("zk.default.locale", "zh_CN")));

        // 设置下 RequestMappingHandlerAdapter 的 ignoreDefaultModelOnRedirect=true,
        // 这样可以提高效率，避免不必要的检索。
        requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
        System.out.println("[^_^:20210705-1808-001] ----- " + this.getClass() + " class before ");
    }

    /**
     * 异常处理适配器
     *
     * @Title: zkExceptionHandlerResolver
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 21, 2020 11:13:24 AM
     * @return
     * @return ZKExceptionHandlerResolver
     */
    @Bean
    public ZKExceptionHandlerResolver zkExceptionHandlerResolver() {
        return new ZKExceptionHandlerResolver();
    }
}
