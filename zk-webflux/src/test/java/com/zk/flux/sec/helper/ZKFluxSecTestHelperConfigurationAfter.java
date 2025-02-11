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
* @Title: ZKFluxSecTestHelperConfigurationAfter.java 
* @author Vinson 
* @Package com.zk.flux.sec.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 16, 2024 1:14:12 AM 
* @version V1.0 
*/
package com.zk.flux.sec.helper;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import com.zk.core.web.ZKWebConstants.ZKFilterLevel;
import com.zk.security.mgt.ZKSecSecurityManager;
import com.zk.security.web.filter.ZKSecFilter;
import com.zk.security.web.filter.authc.ZKSecAuthcUserFilter;
import com.zk.security.web.filter.authc.ZKSecLogoutFilter;
import com.zk.security.web.filter.authc.ZKSecUserFilter;
import com.zk.security.web.support.webFlux.filter.ZKSecSecurityAbstractWebFilter;
import com.zk.security.web.support.webFlux.filter.ZKSecSecurityFilterFactoryBean;

/** 
* @ClassName: ZKFluxSecTestHelperConfigurationAfter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
//@ImportResource(locations = { "classpath:sec/test_spring_ctx_sec.xml" })
@AutoConfigureAfter(value = { ZKFluxSecTestHelperConfigurationBefore.class })
//@ZKEnableWebmvc
public class ZKFluxSecTestHelperConfigurationAfter {

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

    // FilterRegistrationBean<Filter>

    @Order(ZKFilterLevel.Security.HIGHEST)
    @Bean("zkSecFilter")
    public ZKSecSecurityAbstractWebFilter zkSecFilter(ZKSecSecurityManager securityManager) throws Exception {

        System.out.println("[^_^:20240422-0057-001] ----- zkSecFilter 配置 zk 权限过滤器 Filter --- ["
                + this.getClass().getSimpleName() + "] " + this.hashCode());
        ZKSecSecurityFilterFactoryBean zkSecSecurityFilterFactoryBean = new ZKSecSecurityFilterFactoryBean();
        zkSecSecurityFilterFactoryBean.setSecurityManager(securityManager);

        zkSecSecurityFilterFactoryBean.setLoginUrl("/flux/sec/h/c/login");

        // 过滤器配置
        LinkedHashMap<String, ZKSecFilter> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("logout", new ZKSecLogoutFilter());
        filterChainDefinitionMap.put("login", new ZKSecAuthcUserFilter());
        filterChainDefinitionMap.put("user", new ZKSecUserFilter());
        zkSecSecurityFilterFactoryBean.setFilters(filterChainDefinitionMap);

        // 过虑路径设置
        LinkedHashMap<String, String> setFilterChainMap = new LinkedHashMap<>();
        setFilterChainMap.put("/static/**", "anon");
        setFilterChainMap.put("/flux/sec/h/c/none", "anon");
        setFilterChainMap.put("/flux/sec/h/c/login", "login");
        setFilterChainMap.put("/flux/sec/h/c/logout", "logout");
        setFilterChainMap.put("/**", "user");
        zkSecSecurityFilterFactoryBean.setFilterChainDefinitionMap(setFilterChainMap);

//        ZKWebFilter
        ZKSecSecurityAbstractWebFilter zkSecSecurityWebFilter = (ZKSecSecurityAbstractWebFilter) zkSecSecurityFilterFactoryBean
                .getObject();
        return zkSecSecurityWebFilter;

//        FilterRegistrationBean<Filter> zkSecFilterRegistrationBean = new FilterRegistrationBean<Filter>();
//        zkSecFilterRegistrationBean.setFilter((Filter) zkSecSecurityFilterFactoryBean.getObject());// 设置过滤器对象
//        zkSecFilterRegistrationBean.addUrlPatterns("/*");// 配置过滤规则
//        zkSecFilterRegistrationBean.setOrder(ZKFilterLevel.Security.HIGHEST); // order的数值越小 则优先级越高
//        zkSecFilterRegistrationBean.setName("zkSecFilter");// 配置名称
//        return zkSecFilterRegistrationBean;
    }

}
