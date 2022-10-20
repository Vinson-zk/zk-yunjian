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
* @Title: ZKSecSecurityAbstractFilter.java 
* @author Vinson 
* @Package com.zk.security.web.filter.servlet 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 9:35:44 AM 
* @version V1.0 
*/
package com.zk.security.web.filter.servlet;

import java.io.IOException;
import java.util.concurrent.Callable;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zk.core.web.filter.ZKOncePerFilter;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.security.web.common.ZKSecFilterChainResolver;
import com.zk.security.web.mgt.ZKSecWebSecurityManager;
import com.zk.security.web.servlet.ZKSecHttpServletRequest;
import com.zk.security.web.servlet.ZKSecHttpServletResponse;

/** 
* @ClassName: ZKSecSecurityAbstractFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecSecurityAbstractFilter extends ZKOncePerFilter {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private ZKSecWebSecurityManager securityManager;

    /**
     * 是否设置追踪令牌ID
     */
    private boolean traceTicketId = false;

    private ZKSecFilterChainResolver filterChainResolver;

    public ZKSecSecurityAbstractFilter(ZKSecWebSecurityManager securityManager,
            ZKSecFilterChainResolver filterChainResolver) {
        this.securityManager = securityManager;
        this.filterChainResolver = filterChainResolver;
    }

    public ZKSecWebSecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setSecManager(ZKSecWebSecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    public boolean isTraceTicketId() {
        return traceTicketId;
    }

    public void setTraceTicketId(boolean traceTicketId) {
        this.traceTicketId = traceTicketId;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {
        super.destroy();
    }

    public ZKSecFilterChainResolver getFilterChainResolver() {
        return filterChainResolver;
    }

    public void setFilterChainResolver(ZKSecFilterChainResolver filterChainResolver) {
        this.filterChainResolver = filterChainResolver;
    }

    /****************/

    protected ServletRequest wrapServletRequest(HttpServletRequest orig) {
        return new ZKSecHttpServletRequest(orig, getServletContext());
    }

    protected ServletRequest prepareServletRequest(ServletRequest request, ServletResponse response,
            FilterChain chain) {
        ServletRequest toUse = request;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest http = (HttpServletRequest) request;
            toUse = wrapServletRequest(http);
        }
        return toUse;
    }

    protected ServletResponse wrapServletResponse(HttpServletResponse orig, ZKSecHttpServletRequest request) {
        return new ZKSecHttpServletResponse(orig, getServletContext(), request, isTraceTicketId());
    }

    protected ServletResponse prepareServletResponse(ServletRequest request, ServletResponse response,
            FilterChain chain) {
        ServletResponse toUse = response;
        if ((request instanceof ZKSecHttpServletRequest) && (response instanceof HttpServletResponse)) {
            toUse = wrapServletResponse((HttpServletResponse) response, (ZKSecHttpServletRequest) request);
        }
        return toUse;
    }

    protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse,
            final FilterChain chain) throws ServletException, IOException {
        Throwable t = null;
        try {
            final ServletRequest request = prepareServletRequest(servletRequest, servletResponse, chain);
            final ServletResponse response = prepareServletResponse(request, servletResponse, chain);

            final ZKSecSubject subject = createSubject(ZKWebUtils.toHttp(request), ZKWebUtils.toHttp(response));

            // noinspection unchecked
            subject.execute(new Callable<Object>() {
                public Object call() throws Exception {
                    updateTicketTime(request, response);
                    executeFilterChains(request, response, chain);
                    return null;
                }
            });
        }
        catch(Exception e) {
            t = e.getCause();
        }
        catch(Throwable throwable) {
            t = throwable;
        }

        if (t != null) {
            if (t instanceof ServletException) {
                throw (ServletException) t;
            }
            if (t instanceof IOException) {
                throw (IOException) t;
            }
            String msg = "Filtered request failed.";
            throw new ServletException(msg, t);
        }
    }

    /**
     * 修改最后访问是时间
     */
    protected void updateTicketTime(ServletRequest request, ServletResponse response) {
        ZKSecTicket tk = ZKSecSecurityUtils.getTikcet();
        // Subject should never _ever_ be null, but just in case:
        if (tk != null) {
            try {
                tk.updateLastTime();
            }
            catch(Throwable t) {
                logger.error(
                        "ticket.touch() method invocation has failed.  Unable to update the corresponding ticket's update time based on the incoming request.",
                        t);
            }
        }
    }

    /**
     * 执行过虑
     * 
     * @param request
     * @param response
     * @param origChain
     * @throws IOException
     * @throws ServletException
     */
    protected void executeFilterChains(ServletRequest request, ServletResponse response, FilterChain originalChain)
            throws IOException, ServletException {
        FilterChain chain = getResolverChain(request, response, originalChain);
        if (chain != null) {
            chain.doFilter(request, response);
        }
    }

    /**
     * 适配过虑器
     * 
     * @param request
     * @param response
     * @param origChain
     * @return
     */
    protected FilterChain getResolverChain(ServletRequest request, ServletResponse response, FilterChain original) {

        ZKSecFilterChainResolver resolver = getFilterChainResolver();
        if (resolver == null) {
            logger.debug("No FilterChainResolver configured.  Returning original FilterChain.");
            return null;
        }

        FilterChain chain = resolver.getChain(request, response, original);
        if (chain != null) {
            logger.trace("Resolved a configured FilterChain for the current request.");
        }
        else {
            logger.trace("No FilterChain configured for the current request.  Using the default.");
        }

        return chain;
    }

    /**
     * 创建会话主体
     */
    private ZKSecSubject createSubject(HttpServletRequest request, HttpServletResponse response) {
        return this.getSecurityManager().createSubject(request, response);
    }

}
