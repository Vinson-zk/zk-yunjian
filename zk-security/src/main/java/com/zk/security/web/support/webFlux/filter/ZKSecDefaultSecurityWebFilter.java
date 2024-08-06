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
* @Title: ZKSecProxyWebFilter.java 
* @author Vinson 
* @Package com.zk.security.web.support.webFlux.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 28, 2023 4:04:22 PM 
* @version V1.0 
*/
package com.zk.security.web.support.webFlux.filter;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import com.zk.core.exception.base.ZKUnknownException;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.security.web.filter.common.ZKSecFilterChainResolver;
import com.zk.security.web.mgt.ZKSecWebSecurityManager;
import com.zk.security.web.support.webFlux.wrapper.ZKSecServerHttpRequestWrapper;
import com.zk.security.web.support.webFlux.wrapper.ZKSecServerHttpResponseWrapper;

import jakarta.servlet.ServletException;
import reactor.core.publisher.Mono;

/** 
* @ClassName: ZKSecProxyWebFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecDefaultSecurityWebFilter extends ZKSecSecurityAbstractWebFilter {

    /** 
    * <p>Title: </p> 
    * <p>Description: </p> 
    * @param securityManager
    * @param filterChainResolver 
    */
    public ZKSecDefaultSecurityWebFilter(ZKSecWebSecurityManager securityManager,
            ZKSecFilterChainResolver filterChainResolver) {
        super(securityManager, filterChainResolver); // TODO Auto-generated constructor stub
    }

    /**
     * (not Javadoc)
     * 
     * @param exchange
     * @param chain
     * @return
     * @throws ServletException
     * @throws IOException
     * @see com.zk.core.web.support.webFlux.filter.ZKOncePerWebFilter#doFilterInternal(org.springframework.web.server.ServerWebExchange,
     *      org.springframework.web.server.WebFilterChain)
     */
    @Override
    protected Mono<Void> doFilterInternal(ServerWebExchange exchange, WebFilterChain chain) {

        Throwable t = null;
        try {

            final ZKSecServerHttpRequestWrapper zkSecReq = new ZKSecServerHttpRequestWrapper(exchange);
            final ZKSecServerHttpResponseWrapper zkSecRes = new ZKSecServerHttpResponseWrapper(exchange);

//            final ServletRequest request = prepareServletRequest(servletRequest, servletResponse, chain);
//            final ServletResponse response = prepareServletResponse(request, servletResponse, chain);

            final ZKSecSubject subject = this.createSubject(zkSecReq, zkSecRes);

            // noinspection unchecked
            return subject.execute(new Callable<Mono<Void>>() {
                public Mono<Void> call() throws Exception {
                    updateTicketTime();
                    return executeFilterChains(exchange, zkSecReq, zkSecRes, chain);
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
            String msg = "Filtered request failed.";
            throw new ZKUnknownException(msg, t);
        }
        return Mono.empty();

    }

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

}
