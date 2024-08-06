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
* @Package com.zk.security.web.support.servlet.filter
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 9:35:44 AM 
* @version V1.0 f
*/
package com.zk.security.web.support.servlet.filter;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.zk.core.exception.base.ZKUnknownException;
import com.zk.core.web.support.servlet.filter.ZKOncePerFilter;
import com.zk.core.web.utils.ZKServletUtils;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.security.web.filter.common.ZKSecFilterChain;
import com.zk.security.web.filter.common.ZKSecFilterChainResolver;
import com.zk.security.web.mgt.ZKSecWebSecurityManager;
import com.zk.security.web.support.servlet.wrapper.ZKSecHttpServletRequestWrapper;
import com.zk.security.web.support.servlet.wrapper.ZKSecHttpServletResponseWrapper;
import com.zk.security.web.wrapper.ZKSecRequestWrapper;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * @ClassName: ZKSecSecurityAbstractFilter
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKSecSecurityAbstractFilter extends ZKOncePerFilter {

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

    public ZKSecFilterChainResolver getFilterChainResolver() {
        return filterChainResolver;
    }

    public void setFilterChainResolver(ZKSecFilterChainResolver filterChainResolver) {
        this.filterChainResolver = filterChainResolver;
    }

    /****************/

//    protected ServletRequest wrapServletRequest(HttpServletRequest orig) {
////        return new ZKSecHttpServletRequestWrapper(orig, getServletContext());
//        return new ZKSecHttpServletRequestWrapper(orig);
//    }
//
//    protected ServletRequest prepareServletRequest(ServletRequest request, ServletResponse response,
//            FilterChain chain) {
//        ServletRequest toUse = request;
//        if (request instanceof HttpServletRequest) {
//            HttpServletRequest http = (HttpServletRequest) request;
//            toUse = wrapServletRequest(http);
//        }
//        return toUse;
//    }

//    protected ServletResponse wrapServletResponse(HttpServletResponse orig, ZKSecHttpServletRequestWrapper request) {
//        return new ZKSecHttpServletResponseWrapper(orig, getServletContext(), request, isTraceTicketId());
//    }
//
//    protected ServletResponse prepareServletResponse(ServletRequest request, ServletResponse response,
//            FilterChain chain) {
//        ServletResponse toUse = response;
//        if ((request instanceof ZKSecHttpServletRequestWrapper) && (response instanceof HttpServletResponse)) {
//            toUse = wrapServletResponse((HttpServletResponse) response, (ZKSecHttpServletRequestWrapper) request);
//        }
//        return toUse;
//    }

    protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse,
            final FilterChain chain) throws ServletException, IOException {
        Throwable t = null;
        try {

            final ZKSecHttpServletRequestWrapper zkSecReq = new ZKSecHttpServletRequestWrapper(
                    ZKServletUtils.toHttp(servletRequest));
            final ZKSecHttpServletResponseWrapper zkSecRes = new ZKSecHttpServletResponseWrapper(
                    ZKServletUtils.toHttp(servletResponse));

//            final ServletRequest request = prepareServletRequest(servletRequest, servletResponse, chain);
//            final ServletResponse response = prepareServletResponse(request, servletResponse, chain);

            final ZKSecSubject subject = createSubject(zkSecReq, zkSecRes);

            // noinspection unchecked
            subject.execute(new Callable<Object>() {
                public Object call() throws Exception {
                    updateTicketTime(zkSecReq, zkSecRes);
                    executeFilterChains(zkSecReq, zkSecRes, chain);
                    return null;
                }
            });
        }
        catch(Exception e) {
            if (e instanceof ZKUnknownException) {
                throw (ZKUnknownException) e;
            }
            t = e.getCause();
        }
        catch(Throwable throwable) {
            t = throwable;
        }

        if (t != null) {
            if (t instanceof ZKUnknownException) {
                throw (ZKUnknownException) t;
            }
            else if (t instanceof ServletException) {
                throw (ServletException) t;
            }
//            else if (t instanceof IOException) {
//                throw (IOException) t;
//            }
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
                log.error(
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
    protected void executeFilterChains(ZKSecRequestWrapper zkSecReq, ZKSecResponseWrapper zkSecRes,
            FilterChain originalChain)
            throws IOException, ServletException {
        ZKSecFilterChain chain = getResolverChain(zkSecReq, zkSecRes);
        if (chain != null) {
            chain.doFilter(zkSecReq, zkSecRes);
        }
        if (originalChain != null) {
            originalChain.doFilter((ServletRequest) zkSecReq.getWrapperRequest(),
                    (ServletResponse) zkSecRes.getWrapperResponse());
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
    protected ZKSecFilterChain getResolverChain(ZKSecRequestWrapper zkSecReq, ZKSecResponseWrapper zkSecRes) {

        ZKSecFilterChainResolver resolver = getFilterChainResolver();
        if (resolver == null) {
            log.debug("No FilterChainResolver configured.  Returning original FilterChain.");
            return null;
        }

        ZKSecFilterChain chain = resolver.getChain(zkSecReq, zkSecRes, null);
        if (chain != null) {
            log.trace("Resolved a configured FilterChain for the current request.");
        }
        else {
            log.trace("No FilterChain configured for the current request.  Using the default.");
        }

        return chain;
    }

    /**
     * 创建会话主体
     */
    private ZKSecSubject createSubject(ZKSecRequestWrapper zkSecReq, ZKSecResponseWrapper zkSecRes) {
        return this.getSecurityManager().createSubject(zkSecReq, zkSecRes);
    }

}
