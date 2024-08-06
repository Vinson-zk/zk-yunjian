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
* @Title: ZKSecSecurityAbstractWebFilter.java 
* @author Vinson 
* @Package com.zk.security.web.support.webFlux.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 21, 2024 11:37:19 PM 
* @version V1.0 
*/
package com.zk.security.web.support.webFlux.filter;

import java.io.IOException;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import com.zk.core.web.support.webFlux.filter.ZKOncePerWebFilter;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.security.web.filter.common.ZKSecFilterChain;
import com.zk.security.web.filter.common.ZKSecFilterChainResolver;
import com.zk.security.web.mgt.ZKSecWebSecurityManager;
import com.zk.security.web.wrapper.ZKSecRequestWrapper;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;

import jakarta.servlet.ServletException;
import reactor.core.publisher.Mono;


/** 
* @ClassName: ZKSecSecurityAbstractWebFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecSecurityAbstractWebFilter extends ZKOncePerWebFilter {

    private ZKSecWebSecurityManager securityManager;

    /**
     * 是否设置追踪令牌ID
     */
    private boolean traceTicketId = false;

    private ZKSecFilterChainResolver filterChainResolver;

    public ZKSecSecurityAbstractWebFilter(ZKSecWebSecurityManager securityManager,
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

    /**
     * 修改最后访问是时间
     */
    protected void updateTicketTime() {
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
     * @return
     * @throws IOException
     * @throws ServletException
     */
    protected Mono<Void> executeFilterChains(ServerWebExchange exchange, ZKSecRequestWrapper zkSecReq,
            ZKSecResponseWrapper zkSecRes, WebFilterChain originalChain) throws IOException {
        ZKSecFilterChain chain = getResolverChain(zkSecReq, zkSecRes);
        if (chain != null) {
            chain.doFilter(zkSecReq, zkSecRes);
        }
        if (originalChain != null) {
            return originalChain.filter(exchange);
        }
        return Mono.empty();
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
    protected ZKSecSubject createSubject(ZKSecRequestWrapper zkSecReq, ZKSecResponseWrapper zkSecRes) {
        return this.getSecurityManager().createSubject(zkSecReq, zkSecRes);
    }

}
