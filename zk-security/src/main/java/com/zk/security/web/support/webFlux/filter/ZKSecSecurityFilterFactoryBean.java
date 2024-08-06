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
* @Title: ZKSecSecurityFilterFactoryBean.java 
* @author Vinson 
* @Package com.zk.security.web.support.webFlux.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 22, 2024 12:20:14 AM 
* @version V1.0 
*/
package com.zk.security.web.support.webFlux.filter;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.zk.core.exception.base.ZKUnknownException;
import com.zk.security.mgt.ZKSecSecurityManager;
import com.zk.security.web.filter.ZKSecFilter;
import com.zk.security.web.filter.common.ZKSecDefaultFilterChainManager;
import com.zk.security.web.filter.common.ZKSecDefaultFilterChainResolver;
import com.zk.security.web.filter.common.ZKSecFilterChainManager;
import com.zk.security.web.filter.common.ZKSecFilterChainResolver;
import com.zk.security.web.mgt.ZKSecWebSecurityManager;
import com.zk.security.web.support.servlet.filter.ZKSecFilterFactoryBean;

/** 
* @ClassName: ZKSecSecurityFilterFactoryBean 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecSecurityFilterFactoryBean implements FactoryBean<Object>, BeanPostProcessor {

    private static transient final Logger log = LogManager.getLogger(ZKSecFilterFactoryBean.class);

    ZKSecDefaultSecurityWebFilter instance;

    private ZKSecSecurityManager securityManager;

    private Map<String, ZKSecFilter> filters;

    private Map<String, String> filterChainDefinitionMap; // urlPathExpression_to_comma-delimited-filter-chain-definition

    private String loginUrl;

    /**
     * @return instance sa
     */
    public ZKSecDefaultSecurityWebFilter getInstance() {
        return instance;
    }

    /**
     * @return securityManager sa
     */
    public ZKSecSecurityManager getSecurityManager() {
        return securityManager;
    }

    /**
     * @return filters sa
     */
    public Map<String, ZKSecFilter> getFilters() {
        return filters;
    }

    /**
     * @return filterChainDefinitionMap sa
     */
    public Map<String, String> getFilterChainDefinitionMap() {
        return filterChainDefinitionMap;
    }

    /**
     * @return loginUrl sa
     */
    public String getLoginUrl() {
        return loginUrl;
    }

    /**
     * @param instance
     *            the instance to set
     */
    public void setInstance(ZKSecDefaultSecurityWebFilter instance) {
        this.instance = instance;
    }

    /**
     * @param securityManager
     *            the securityManager to set
     */
    public void setSecurityManager(ZKSecSecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    /**
     * @param filters
     *            the filters to set
     */
    public void setFilters(Map<String, ZKSecFilter> filters) {
        this.filters = filters;
    }

    /**
     * @param filterChainDefinitionMap
     *            the filterChainDefinitionMap to set
     */
    public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
    }

    /**
     * @param loginUrl
     *            the loginUrl to set
     */
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    @Override
    public Object getObject() throws Exception {
        if (instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    @Override
    public Class<?> getObjectType() {
        return ZKSecDefaultSecurityWebFilter.class;
    }

    protected ZKSecDefaultSecurityWebFilter createInstance() throws Exception {

        log.debug("Creating ZKSecSecurityAbstractFilter Filter instance.");

        ZKSecSecurityManager securityManager = this.getSecurityManager();
        if (securityManager == null) {
            String msg = "SecurityManager property must be set.";
            throw new ZKUnknownException(msg);
        }

        if (!(securityManager instanceof ZKSecWebSecurityManager)) {
            String msg = "The security manager does not implement the ZKSecWebSecurityManager interface.";
            throw new ZKUnknownException(msg);
        }

        ZKSecFilterChainManager filterChainManager = createFilterChainManager();

        ZKSecFilterChainResolver filterChainResolver = new ZKSecDefaultFilterChainResolver(filterChainManager);

        return new ZKSecDefaultSecurityWebFilter((ZKSecWebSecurityManager) securityManager, filterChainResolver);
    }

    protected ZKSecFilterChainManager createFilterChainManager() {

        ZKSecDefaultFilterChainManager manager = new ZKSecDefaultFilterChainManager(this.getLoginUrl(),
                this.getFilters(), this.getFilterChainDefinitionMap());

        return manager;
    }

}
