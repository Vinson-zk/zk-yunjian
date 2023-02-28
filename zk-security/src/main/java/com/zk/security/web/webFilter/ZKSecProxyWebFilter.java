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
* @Package com.zk.security.web.webFilter 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 28, 2023 4:04:22 PM 
* @version V1.0 
*/
package com.zk.security.web.webFilter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.ServletException;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.core.web.webFilter.ZKOncePerWebFilter;
import com.zk.core.web.webFilter.ZKServletWebFilterChain;

import reactor.core.publisher.Mono;

/** 
* @ClassName: ZKSecProxyWebFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecProxyWebFilter extends ZKOncePerWebFilter {

    Filter secFilter;

    public ZKSecProxyWebFilter(Filter secFilter) {
        this.secFilter = secFilter;
    }

    /**
     * (not Javadoc)
     * 
     * @param exchange
     * @param chain
     * @return
     * @throws ServletException
     * @throws IOException
     * @see com.zk.core.web.webFilter.ZKOncePerWebFilter#doFilterInternal(org.springframework.web.server.ServerWebExchange,
     *      org.springframework.web.server.WebFilterChain)
     */
    @Override
    protected Mono<Void> doFilterInternal(ServerWebExchange exchange, WebFilterChain chain) {
        try {
            if (this.secFilter != null) {
                this.secFilter.doFilter(ZKWebUtils.getRequest(), ZKWebUtils.getResponse(),
                        new ZKServletWebFilterChain());
            }
        }
        catch(Exception e) {
            throw ZKExceptionsUtils.unchecked(e);
        }
        return chain.filter(exchange);
    }

}
