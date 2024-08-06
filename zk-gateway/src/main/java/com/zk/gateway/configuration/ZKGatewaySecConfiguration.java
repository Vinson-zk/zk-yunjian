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
* @Title: ZKGatewaySecConfiguration.java 
* @author Vinson 
* @Package com.zk.gateway.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 14, 2022 7:07:27 PM 
* @version V1.0 
*/
package com.zk.gateway.configuration;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.WebFilter;

import com.zk.core.web.ZKWebConstants.ZKFilterLevel;
import com.zk.framework.security.configuration.ZKSecDefaultConfiguration;
import com.zk.security.configuration.ZKEnableSecurity;
import com.zk.security.mgt.ZKSecSecurityManager;
import com.zk.security.web.support.webFlux.filter.ZKSecSecurityAbstractWebFilter;
import com.zk.security.web.support.webFlux.filter.ZKSecSecurityFilterFactoryBean;

/**
 * @ClassName: ZKGatewaySecConfiguration
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@AutoConfigureBefore(value = { ZKGatewayAfterConfiguration.class })
@ZKEnableSecurity
@ImportAutoConfiguration(value = { ZKSecDefaultConfiguration.class })
public class ZKGatewaySecConfiguration {

    @Value("${zk.path.admin}")
    String pathAdmin;

    @Value("${zk.path.gateway}")
    String pathGateway;

    @Value("${zk.gateway.version}")
    String pathVersion;

    /**
     * 权限处理拦截器
     *
     * @Title: zkSecFilter
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 14, 2023 1:54:33 PM
     * @param securityManager
     * @return
     * @throws Exception
     * @return FilterRegistrationBean<Filter>
     */
    @Order(ZKFilterLevel.Security.HIGHEST)
    @Bean("zkSecProxyWebFilter")
    WebFilter zkSecProxyWebFilter(ZKSecSecurityManager securityManager) throws Exception {

        System.out.println("[^_^:20230211-1022-001] ----- zkSecProxyWebFilter 配置 zk 权限过滤器 WebFilter --- ["
                + this.getClass().getSimpleName() + "] " + this.hashCode());

        ZKSecSecurityFilterFactoryBean zkSecSecurityFilterFactoryBean = new ZKSecSecurityFilterFactoryBean();
        zkSecSecurityFilterFactoryBean.setSecurityManager(securityManager);

        // 过虑路径设置
        LinkedHashMap<String, String> setFilterChainMap = new LinkedHashMap<>();
        String prefix = String.format("/%s/%s/%s", this.pathAdmin, this.pathGateway, this.pathVersion);

        setFilterChainMap.put("/test/**", "anon");
        setFilterChainMap.put("/apiSys/**", "anon");
        setFilterChainMap.put("/apiWechat/**", "anon");
        setFilterChainMap.put("/apiDevTool/**", "anon");
        setFilterChainMap.put("/apiMail/**", "anon");
        setFilterChainMap.put("/apiFile/**", "anon");

        setFilterChainMap.put(prefix, "anon");
        setFilterChainMap.put(prefix + "/", "anon");
        setFilterChainMap.put(prefix + "/index", "anon");
        setFilterChainMap.put(prefix + "/user", "user");
        setFilterChainMap.put("/**", "user");
        zkSecSecurityFilterFactoryBean.setFilterChainDefinitionMap(setFilterChainMap);

        ZKSecSecurityAbstractWebFilter zkSecSecurityWebFilter = (ZKSecSecurityAbstractWebFilter) zkSecSecurityFilterFactoryBean
                .getObject();
        return zkSecSecurityWebFilter;
    }


}
