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
 * @Title: ZKFilterDelegatingProxy.java 
 * @author Vinson 
 * @Package com.zk.core.web.support.servlet.filter
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:18:14 PM 
 * @version V1.0   
*/
package com.zk.core.web.support.servlet.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/** 
* @ClassName: ZKFilterDelegatingProxy 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKFilterDelegatingProxy implements Filter, ServletContextAware {

    protected Logger log = LogManager.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;

        if (log.isInfoEnabled()) {
            log.info("[^_^:20190629-1515-001] Filter '" + filterConfig.getFilterName() + "' configured successfully");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        log.debug("======= FilterDelegatingProxy ================================= ");

        Filter delegateToUse = this.delegate;
        if (delegateToUse == null) {
            synchronized (this.delegateMonitor) {
                if (this.delegate == null) {
                    WebApplicationContext wac = findWebApplicationContext();
                    if (wac == null) {
                        throw new IllegalStateException("No WebApplicationContext found: "
                                + "no ContextLoaderListener or DispatcherServlet registered?");
                    }
                    this.delegate = initDelegate(wac);
                }
                delegateToUse = this.delegate;
            }
        }

        // Let the delegate perform the actual doFilter operation.
        invokeDelegate(delegateToUse, request, response, filterChain);

    }

    @Override
    public void destroy() {
        if (this.delegate != null) {
            this.delegate.destroy();
        }
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /************************************************/
    public ZKFilterDelegatingProxy() {
        
    }

    public ZKFilterDelegatingProxy(String targetName, WebApplicationContext wac) {
        this.targetBeanName = targetName;
        this.webApplicationContext = wac;
    }

    protected void invokeDelegate(Filter delegate, ServletRequest request, ServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        delegate.doFilter(request, response, filterChain);
    }

    /************************************************/
    private String targetBeanName;

    private FilterConfig filterConfig;

    private Filter delegate;

    private WebApplicationContext webApplicationContext;

    private ServletContext servletContext;

    private String contextAttribute;

    private final Object delegateMonitor = new Object();

    public void setTargetBeanName(String targetBeanName) {
        this.targetBeanName = targetBeanName;
    }

    protected Filter initDelegate(WebApplicationContext wac) throws ServletException {
        Filter delegate = wac.getBean(getTargetBeanName(), Filter.class);
//      if (isTargetFilterLifecycle()) {
//          delegate.init(getFilterConfig());
//      }
        return delegate;
    }

    protected String getTargetBeanName() {
        if (this.targetBeanName == null) {
            this.targetBeanName = getFilterName();
        }
        if (log.isInfoEnabled()) {
            log.info("[^_^:20180429-0110-001] target filter name is '{}' ", this.targetBeanName);
        }
        return this.targetBeanName;
    }

    protected final String getFilterName() {
        return (this.filterConfig != null ? this.filterConfig.getFilterName() : "zkFilter");
    }

    protected WebApplicationContext findWebApplicationContext() {
        if (this.webApplicationContext != null) {
            // The user has injected a context at construction time -> use it...
            if (this.webApplicationContext instanceof ConfigurableApplicationContext) {
                ConfigurableApplicationContext cac = (ConfigurableApplicationContext) this.webApplicationContext;
                if (!cac.isActive()) {
                    // The context has not yet been refreshed -> do so before
                    // returning it...
                    cac.refresh();
                }
            }
            return this.webApplicationContext;
        }
        String attrName = getContextAttribute();
        if (attrName != null) {
            return WebApplicationContextUtils.getWebApplicationContext(getServletContext(), attrName);
        }
        else {
            return WebApplicationContextUtils.findWebApplicationContext(getServletContext());
        }
    }

    protected final ServletContext getServletContext() {
        return (this.filterConfig != null ? this.filterConfig.getServletContext() : this.servletContext);
    }

    public void setContextAttribute(String contextAttribute) {
        this.contextAttribute = contextAttribute;
    }

    public String getContextAttribute() {
        return this.contextAttribute;
    }

}
