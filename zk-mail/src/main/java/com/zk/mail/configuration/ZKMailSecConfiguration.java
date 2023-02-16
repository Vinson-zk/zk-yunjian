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
* @Title: ZKMailSecConfiguration.java 
* @author Vinson 
* @Package com.zk.mail.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date May 25, 2022 8:46:29 AM 
* @version V1.0 
*/
package com.zk.mail.configuration;

import java.util.LinkedHashMap;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.zk.core.web.filter.ZKFilterUtils.ZKFilterLevel;
import com.zk.framework.security.configuration.ZKSecDefaultConfiguration;
import com.zk.security.mgt.ZKSecSecurityManager;
import com.zk.security.web.filter.ZKSecFilterFactoryBean;


/** 
* @ClassName: ZKMailSecConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@AutoConfigureAfter(value = {
        EnableWebMvcConfiguration.class, 
        ServletWebServerFactoryAutoConfiguration.class})
@ImportAutoConfiguration(value = { ZKSecDefaultConfiguration.class })
public class ZKMailSecConfiguration {

    @Value("${zk.path.admin}")
    String pathAdmin;

    @Value("${zk.path.mail}")
    String pathMail;

    @Value("${zk.mail.version}")
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
    @Bean("zkSecFilter")
    public FilterRegistrationBean<Filter> zkSecFilter(ZKSecSecurityManager securityManager) throws Exception {
        System.out.println("[^_^:20230211-1022-001] ----- zkSecFilter 配置 zk 权限过滤器 Filter --- ["
                + this.getClass().getSimpleName() + "] " + this.hashCode());
        ZKSecFilterFactoryBean zkSecFilterFactoryBean = new ZKSecFilterFactoryBean();
        zkSecFilterFactoryBean.setSecurityManager(securityManager);

        // 过滤器配置
//        LinkedHashMap<String, Filter> filterChainDefinitionMap = new LinkedHashMap<>();
//        filterChainDefinitionMap.put("user", new ZKSecUserFilter());
//        zkSecFilterFactoryBean.setFilters(filterChainDefinitionMap);

        // 过虑路径设置
        LinkedHashMap<String, String> setFilterChainMap = new LinkedHashMap<>();
        String prefix = String.format("/%s/%s/%s", this.pathAdmin, this.pathMail, this.pathVersion);
        setFilterChainMap.put(prefix, "anon");
        setFilterChainMap.put(prefix + "/", "anon");
        setFilterChainMap.put(prefix + "/index", "anon");
        setFilterChainMap.put(prefix + "/mailSend/mailSend/*", "serverOrUser");
        setFilterChainMap.put("/**", "user");
        zkSecFilterFactoryBean.setFilterChainDefinitionMap(setFilterChainMap);

        FilterRegistrationBean<Filter> zkSecFilterRegistrationBean = new FilterRegistrationBean<Filter>();
        zkSecFilterRegistrationBean.setFilter((Filter) zkSecFilterFactoryBean.getObject());// 设置过滤器对象
        zkSecFilterRegistrationBean.addUrlPatterns("/*");// 配置过滤规则
        zkSecFilterRegistrationBean.setOrder(ZKFilterLevel.Security.HIGHEST); // order的数值越小 则优先级越高
        zkSecFilterRegistrationBean.setName("zkSecFilter");// 配置名称;

        return zkSecFilterRegistrationBean;
    }

}
