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
 * @Title: ZKSerCenShiroConfiguration.java 
 * @author Vinson 
 * @Package com.zk.server.central.configuration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 20, 2019 9:00:18 AM 
 * @version V1.0   
*/
package com.zk.server.central.configuration;

import java.util.LinkedHashMap;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.zk.core.web.ZKWebConstants.ZKFilterLevel;
import com.zk.server.central.security.ZKSerCenAuthenticationFilter;
import com.zk.server.central.security.ZKSerCenUserSampleRealm;

import jakarta.servlet.Filter;

/** 
* @ClassName: ZKSerCenShiroConfiguration 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSerCenShiroConfiguration {

    @Value("${zk.path.admin}")
    String pathAdmin;

    @Value("${zk.path.serCen}")
    String pathSerCen;

    public ZKSerCenShiroConfiguration() {
        System.out.println("[^_^:20230211-102232-001] ----- 实例化 -----" + this.getClass().getSimpleName());
    }

    @Bean("securityManager")
    SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(new ZKSerCenUserSampleRealm());

        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setDeleteInvalidSessions(false);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    @Bean("shiroFilter")
    FilterRegistrationBean<Filter> zkSecFilter(SecurityManager securityManager) throws Exception {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        String prefix = String.format("/%s/%s", this.pathAdmin, this.pathSerCen);

        shiroFilterFactoryBean.setLoginUrl(prefix + "/l/login");
        shiroFilterFactoryBean.setSuccessUrl(prefix);

        // 过滤器配置
        LinkedHashMap<String, Filter> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("authc", new ZKSerCenAuthenticationFilter());
        shiroFilterFactoryBean.setFilters(filterChainDefinitionMap);

        // 过虑路径设置
        LinkedHashMap<String, String> setFilterChainMap = new LinkedHashMap<>();
        setFilterChainMap.put("/eureka/**", "anon");
        setFilterChainMap.put("/static/**", "anon");
        setFilterChainMap.put(prefix + "/anon", "anon");

        setFilterChainMap.put(prefix + "/test/**", "anon");
//        setFilterChainMap.put(prefix + "/index", "anon");
//        setFilterChainMap.put(prefix + "/zkEureka", "anon");
//        setFilterChainMap.put(prefix + "/zkEureka/**", "anon");

        setFilterChainMap.put(prefix + "/locale", "anon");
        setFilterChainMap.put(prefix + "/l/captcha", "anon");
        setFilterChainMap.put(prefix + "/l/doingCaptcha", "anon");
        setFilterChainMap.put(prefix + "/l/login", "authc");
        setFilterChainMap.put(prefix + "/l/logout", "logout");
        setFilterChainMap.put("/**", "user");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(setFilterChainMap);

        FilterRegistrationBean<Filter> shiroFilterRegistrationBean = new FilterRegistrationBean<Filter>();
        shiroFilterRegistrationBean.setFilter((Filter) shiroFilterFactoryBean.getObject());// 设置过滤器对象
        shiroFilterRegistrationBean.addUrlPatterns("/*");// 配置过滤规则
        shiroFilterRegistrationBean.setOrder(ZKFilterLevel.Security.HIGHEST); // order的数值越小 则优先级越高
        shiroFilterRegistrationBean.setName("zkSecFilter");// 配置名称;

        return shiroFilterRegistrationBean;
    }

}
