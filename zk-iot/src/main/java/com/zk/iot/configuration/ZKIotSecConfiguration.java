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
* @Title: ZKIotSecConfiguration.java 
* @author Vinson 
* @Package com.zk.iot.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Dec 31, 2024 11:30:04 AM 
* @version V1.0 
*/
package com.zk.iot.configuration;

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
* @ClassName: ZKIotSecConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@AutoConfigureBefore(value = { ZKIotAfterConfiguration.class })
@ZKEnableSecurity
@ImportAutoConfiguration(value = { ZKSecDefaultConfiguration.class })
public class ZKIotSecConfiguration {

    @Value("${zk.path.admin}")
    String pathAdmin;

    @Value("${zk.path.iot}")
    String pathIot;

    @Value("${zk.iot.version}")
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
        setFilterChainMap.put("/favicon.ico", "anon");
//        String prefix = String.format("/%s/%s/%s", this.pathAdmin, this.pathIot, this.pathVersion);
//        setFilterChainMap.put(prefix, "anon");
//        setFilterChainMap.put(prefix + "/", "anon");
//        setFilterChainMap.put(prefix + "/index", "anon");
        setFilterChainMap.put("/", "anon");
        setFilterChainMap.put("/index", "anon");

        setFilterChainMap.put("/**", "user");
        zkSecSecurityFilterFactoryBean.setFilterChainDefinitionMap(setFilterChainMap);

        ZKSecSecurityAbstractWebFilter zkSecSecurityWebFilter = (ZKSecSecurityAbstractWebFilter) zkSecSecurityFilterFactoryBean
                .getObject();
        return zkSecSecurityWebFilter;
    }

}
