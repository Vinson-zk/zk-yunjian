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
* @Title: ZKMvcSecTestHelperConfigurationAfter.java 
* @author Vinson 
* @Package com.zk.mvc.sec.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 4, 2021 12:32:15 AM 
* @version V1.0 
*/
package com.zk.mvc.sec.helper;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.zk.core.web.ZKWebConstants.ZKFilterLevel;
import com.zk.security.mgt.ZKSecSecurityManager;
import com.zk.security.web.filter.ZKSecFilter;
import com.zk.security.web.filter.authc.ZKSecAuthcUserFilter;
import com.zk.security.web.filter.authc.ZKSecLogoutFilter;
import com.zk.security.web.filter.authc.ZKSecUserFilter;
import com.zk.security.web.support.servlet.filter.ZKSecFilterFactoryBean;
import com.zk.webmvc.configuration.ZKEnableWebmvc;

import jakarta.servlet.Filter;

/** 
* @ClassName: ZKMvcSecTestHelperConfigurationAfter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
//@ImportResource(locations = { "classpath:sec/test_spring_ctx_sec.xml" })
@AutoConfigureAfter(value = { ZKMvcSecTestHelperConfigurationBefore.class })
@ZKEnableWebmvc
public class ZKMvcSecTestHelperConfigurationAfter {

    @Autowired
    public void before() { // RequestMappingHandlerAdapter requestMappingHandlerAdapter
        System.out.println("[^_^:20210705-1808-001] ===== " + this.getClass().getSimpleName() + " class before ");

        // 设置下 RequestMappingHandlerAdapter 的 ignoreDefaultModelOnRedirect=true,
        // 这样可以提高效率，避免不必要的检索。
//        requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
        System.out.println("[^_^:20210705-1808-001] ----- " + this.getClass().getSimpleName() + " class before ");
    }

    /********************************************************************************/
    /*** zk security 配置 */
    /********************************************************************************/

//    /**
//     * 拦截器 - DelegatingFilterProxyRegistrationBean
//     *
//     * @Title: 
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date Oct 28, 2019 4:11:07 PM
//     * @return
//     */
//    @Bean
//    public ZKDelegatingFilterProxyRegistrationBean shiroFilterProxyRegistrationBean() {
//        String filterName = "zkSecFilter";
//        ZKDelegatingFilterProxyRegistrationBean zkDfprb = new ZKDelegatingFilterProxyRegistrationBean(filterName);
//        zkDfprb.setName(filterName);
//        zkDfprb.addUrlPatterns("/*");
//        zkDfprb.setOrder(Ordered.LOWEST_PRECEDENCE);
//        return zkDfprb;
//    }

    @Bean("zkSecFilter")
    public FilterRegistrationBean<Filter> zkSecFilter(ZKSecSecurityManager securityManager) throws Exception {
        System.out.println("[^_^:20230211-1022-001] ----- zkSecFilter 配置 zk 权限过滤器 Filter --- ["
                + this.getClass().getSimpleName() + "] " + this.hashCode());
        ZKSecFilterFactoryBean zkSecFilterFactoryBean = new ZKSecFilterFactoryBean();
        zkSecFilterFactoryBean.setSecurityManager(securityManager);

        zkSecFilterFactoryBean.setLoginUrl("/mvc/sec/h/c/login");

        // 过滤器配置
        LinkedHashMap<String, ZKSecFilter> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("logout", new ZKSecLogoutFilter());
        filterChainDefinitionMap.put("login", new ZKSecAuthcUserFilter());
        filterChainDefinitionMap.put("user", new ZKSecUserFilter());
        zkSecFilterFactoryBean.setFilters(filterChainDefinitionMap);

        // 过虑路径设置
        LinkedHashMap<String, String> setFilterChainMap = new LinkedHashMap<>();
        setFilterChainMap.put("/static/**", "anon");
        setFilterChainMap.put("/mvc/sec/h/c/none", "anon");
        setFilterChainMap.put("/mvc/sec/h/c/login", "login");
        setFilterChainMap.put("/mvc/sec/h/c/logout", "logout");
        setFilterChainMap.put("/**", "user");
        zkSecFilterFactoryBean.setFilterChainDefinitionMap(setFilterChainMap);

        FilterRegistrationBean<Filter> zkSecFilterRegistrationBean = new FilterRegistrationBean<Filter>();
        zkSecFilterRegistrationBean.setFilter((Filter) zkSecFilterFactoryBean.getObject());// 设置过滤器对象
        zkSecFilterRegistrationBean.addUrlPatterns("/*");// 配置过滤规则
        zkSecFilterRegistrationBean.setOrder(ZKFilterLevel.Security.HIGHEST); // order的数值越小 则优先级越高
        zkSecFilterRegistrationBean.setName("zkSecFilter");// 配置名称
        return zkSecFilterRegistrationBean;
    }


}
