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
* @Title: ZKSysSecConfiguration.java 
* @author Vinson 
* @Package com.zk.sys.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 20, 2022 4:23:52 PM 
* @version V1.0 
*/
package com.zk.sys.configuration;

import java.util.LinkedHashMap;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.zk.core.web.filter.ZKFilterUtils.ZKFilterLevel;
import com.zk.framework.security.configuration.ZKSecDefaultConfiguration;
import com.zk.framework.security.service.ZKSecAuthService;
import com.zk.security.configuration.ZKEnableSecurity;
import com.zk.security.mgt.ZKSecSecurityManager;
import com.zk.security.ticket.support.redis.ZKSecRedisTicketManager;
import com.zk.security.web.filter.ZKSecFilterFactoryBean;
import com.zk.security.web.filter.authc.ZKSecAuthcUserFilter;
import com.zk.security.web.filter.authc.ZKSecLogoutFilter;
import com.zk.security.web.filter.authc.ZKSecUserFilter;
import com.zk.sys.sec.realm.ZKSysSecRealm;
import com.zk.sys.sec.service.ZKSysSecAuthService;

/** 
* @ClassName: ZKSysSecConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@AutoConfigureAfter(value = { //
        ZKSysMvcConfiguration.class, //
        EnableWebMvcConfiguration.class, //
        ServletWebServerFactoryAutoConfiguration.class, //
})
@ZKEnableSecurity
@ImportAutoConfiguration(value = { ZKSecDefaultConfiguration.class })
public class ZKSysSecConfiguration {

    @Value("${zk.path.admin}")
    String pathAdmin;

    @Value("${zk.path.sys}")
    String pathSys;

    @Value("${zk.sys.version}")
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
        LinkedHashMap<String, Filter> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("logout", new ZKSecLogoutFilter());
        filterChainDefinitionMap.put("login", new ZKSecAuthcUserFilter());
        filterChainDefinitionMap.put("user", new ZKSecUserFilter());
        zkSecFilterFactoryBean.setFilters(filterChainDefinitionMap);

        // 过虑路径设置
        LinkedHashMap<String, String> setFilterChainMap = new LinkedHashMap<>();
        String prefix = String.format("/%s/%s/%s", this.pathAdmin, this.pathSys, this.pathVersion);
        setFilterChainMap.put(prefix, "anon");
        setFilterChainMap.put(prefix + "/", "anon");
        setFilterChainMap.put(prefix + "/index", "anon");
        setFilterChainMap.put(prefix + "/sec/login", "login");
        setFilterChainMap.put(prefix + "/sec/logout", "logout");
        setFilterChainMap.put(prefix + "/org/sysOrgCompany/sysOrgCompanyByCode", "serverOrUser");
        setFilterChainMap.put(prefix + "/sec/authc/getUserAuthc", "serverAndUser");
        setFilterChainMap.put("/**", "user");
        zkSecFilterFactoryBean.setFilterChainDefinitionMap(setFilterChainMap);

        FilterRegistrationBean<Filter> zkSecFilterRegistrationBean = new FilterRegistrationBean<Filter>();
        zkSecFilterRegistrationBean.setFilter((Filter) zkSecFilterFactoryBean.getObject());// 设置过滤器对象
        zkSecFilterRegistrationBean.addUrlPatterns("/*");// 配置过滤规则
        zkSecFilterRegistrationBean.setOrder(ZKFilterLevel.Security.HIGHEST); // order的数值越小 则优先级越高
        zkSecFilterRegistrationBean.setName("zkSecFilter");// 配置名称;

        return zkSecFilterRegistrationBean;
    }

    /**
     * 权限管理，与鉴权服务
     *
     * @Title: getSecAuthService
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 11, 2022 3:52:24 PM
     * @return
     * @return ZKSecAuthService<String>
     */
    @Bean
    @Primary
    public ZKSecAuthService<String> getSecAuthService() {
        ZKSecAuthService<String> bean = new ZKSysSecAuthService();
        return bean;
    }

    @Bean
    public ZKSysSecRealm zkSecRealm(ZKSecRedisTicketManager secRedisTicketManager) {
        System.out.println(
                "[^_^:20230308-0613-001] ----- zk-sys config: zkSecRealm: " + ZKSysSecRealm.class.getSimpleName());
        ZKSysSecRealm realm = new ZKSysSecRealm();
//        realm.setSecUserService(secUserService);
        realm.setTicketManager(secRedisTicketManager);
        return realm;
    }

}
