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
* @Title: ZKSecFilterFactoryBean.java 
* @author Vinson 
* @Package com.zk.security.web.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 27, 2021 8:09:43 PM 
* @version V1.0 
*/
package com.zk.security.web.filter;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.zk.core.utils.ZKCollectionUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.security.common.ZKSecIni;
import com.zk.security.common.ZKSecNameable;
import com.zk.security.exception.ZKSecUnknownException;
import com.zk.security.mgt.ZKSecSecurityManager;
import com.zk.security.web.common.ZKSecDefaultFilterChainManager;
import com.zk.security.web.common.ZKSecDefaultFilterChainResolver;
import com.zk.security.web.common.ZKSecFilterChainManager;
import com.zk.security.web.common.ZKSecFilterChainResolver;
import com.zk.security.web.filter.servlet.ZKSecDefaultSecurityFilter;
import com.zk.security.web.filter.servlet.ZKSecSecurityAbstractFilter;
import com.zk.security.web.mgt.ZKSecWebSecurityManager;

/** 
* @ClassName: ZKSecFilterFactoryBean 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecFilterFactoryBean implements FactoryBean<Object>, BeanPostProcessor {

    private static transient final Logger log = LoggerFactory.getLogger(ZKSecFilterFactoryBean.class);

    private ZKSecSecurityManager securityManager;

    private Map<String, Filter> filters;

    private Map<String, String> filterChainDefinitionMap; // urlPathExpression_to_comma-delimited-filter-chain-definition

    private String loginUrl;

//    private String successUrl;
//
//    private String unauthorizedUrl;

    private ZKSecSecurityAbstractFilter instance;

    public ZKSecFilterFactoryBean() {
        this.filters = new LinkedHashMap<String, Filter>();
        this.filterChainDefinitionMap = new LinkedHashMap<String, String>(); //order matters!
    }

    public ZKSecSecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setSecurityManager(ZKSecSecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public Map<String, Filter> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Filter> filters) {
        this.filters = filters;
    }

    public Map<String, String> getFilterChainDefinitionMap() {
        return filterChainDefinitionMap;
    }

    public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
    }


    public void setFilterChainDefinitions(String definitions) {
        ZKSecIni ini = new ZKSecIni();
        ini.load(definitions);
        // did they explicitly state a 'urls' section? Not necessary, but just
        // in case:
        ZKSecIni.Section section = ini.getSection(ZKSecIni.URLS);
        if (ZKCollectionUtils.isEmpty(section)) {
            // no urls section. Since this _is_ a urls chain definition
            // property, just assume the
            // default section contains only the definitions:
            section = ini.getSection(ZKSecIni.DEFAULT_SECTION_NAME);
        }
        setFilterChainDefinitionMap(section);
    }

    public Object getObject() throws Exception {
        if (instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    public Class<?> getObjectType() {
        return ZKSecDefaultSecurityFilter.class;
    }

    public boolean isSingleton() {
        return true;
    }

    protected ZKSecFilterChainManager createFilterChainManager() {

        ZKSecDefaultFilterChainManager manager = new ZKSecDefaultFilterChainManager();
        Map<String, Filter> defaultFilters = manager.getFilters();
        // apply global settings if necessary:
        for (Filter filter : defaultFilters.values()) {
            applyGlobalPropertiesIfNecessary(filter);
        }

        // Apply the acquired and/or configured filters:
        Map<String, Filter> filters = getFilters();
        if (!ZKCollectionUtils.isEmpty(filters)) {
            for (Map.Entry<String, Filter> entry : filters.entrySet()) {
                String name = entry.getKey();
                Filter filter = entry.getValue();
                applyGlobalPropertiesIfNecessary(filter);
                if (filter instanceof ZKSecNameable) {
                    ((ZKSecNameable) filter).setName(name);
                }
                // 'init' argument is false, since Spring-configured filters
                // should be initialized
                // in Spring (i.e. 'init-method=blah') or implement
                // InitializingBean:
                manager.addFilter(name, filter, false);
            }
        }

        // build up the chains:
        Map<String, String> chains = getFilterChainDefinitionMap();
        if (!ZKCollectionUtils.isEmpty(chains)) {
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue();
                manager.createChain(url, chainDefinition);
            }
        }

        return manager;
    }

    protected ZKSecSecurityAbstractFilter createInstance() throws Exception {

        log.debug("Creating ZKSecSecurityAbstractFilter Filter instance.");

        ZKSecSecurityManager securityManager = getSecurityManager();
        if (securityManager == null) {
            String msg = "SecurityManager property must be set.";
            throw new ZKSecUnknownException(msg);
        }

        if (!(securityManager instanceof ZKSecWebSecurityManager)) {
            String msg = "The security manager does not implement the ZKSecWebSecurityManager interface.";
            throw new ZKSecUnknownException(msg);
        }

        ZKSecFilterChainManager filterChainManager = createFilterChainManager();

        ZKSecFilterChainResolver filterChainResolver = new ZKSecDefaultFilterChainResolver(filterChainManager);

        return new ZKSecDefaultSecurityFilter((ZKSecWebSecurityManager) securityManager, filterChainResolver);
    }

    private void applyLoginUrlIfNecessary(Filter filter) {
        String loginUrl = getLoginUrl();
        if (ZKStringUtils.hasText(loginUrl) && (filter instanceof ZKSecAccessControlFilter)) {
            ZKSecAccessControlFilter acFilter = (ZKSecAccessControlFilter) filter;
            // only apply the login url if they haven't explicitly configured
            // one already:
            String existingLoginUrl = acFilter.getLoginUrl();
            if (ZKSecAccessControlFilter.DEFAULT_LOGIN_URL.equals(existingLoginUrl)) {
                acFilter.setLoginUrl(loginUrl);
            }
        }
    }

    private void applyGlobalPropertiesIfNecessary(Filter filter) {
        applyLoginUrlIfNecessary(filter);
//        applySuccessUrlIfNecessary(filter);
//        applyUnauthorizedUrlIfNecessary(filter);
    }

    /**
     * Inspects a bean, and if it implements the {@link Filter} interface,
     * automatically adds that filter instance to the internal
     * {@link #setFilters(java.util.Map) filters map} that will be referenced
     * later during filter chain construction.
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Filter) {
            log.debug("Found filter chain candidate filter '{}'", beanName);
            Filter filter = (Filter) bean;
            applyGlobalPropertiesIfNecessary(filter);
            getFilters().put(beanName, filter);
        }
        else {
            log.trace("Ignoring non-Filter bean '{}'", beanName);
        }
        return bean;
    }

    /**
     * Does nothing - only exists to satisfy the BeanPostProcessor interface and
     * immediately returns the {@code bean} argument.
     */
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
