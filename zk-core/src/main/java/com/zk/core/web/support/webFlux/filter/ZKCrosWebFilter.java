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
* @Title: ZKCrosWebFilter.java 
* @author Vinson 
* @Package com.zk.core.web.support.webFlux.filter
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 28, 2023 3:36:57 PM 
* @version V1.0 
*/
package com.zk.core.web.support.webFlux.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import com.zk.core.web.ZKWebConstants.HeaderKey;
import com.zk.core.web.ZKWebConstants.HeaderValue;

import reactor.core.publisher.Mono;

/** 
* @ClassName: ZKCrosWebFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCrosWebFilter extends ZKOncePerWebFilter {

    /**
     * (not Javadoc) <dscription:
     * </p>
     * 
     * @param exchange
     * @param chain
     * @return
     * @see com.zk.core.web.support.webFlux.filter.ZKOncePerWebFilter#doFilterInternal(org.springframework.web.server.ServerWebExchange,
     *      org.springframework.web.server.WebFilterChain)
     */
    @Override
    protected Mono<Void> doFilterInternal(ServerWebExchange exchange, WebFilterChain chain) {
//        System.out.println(
//                "[^_^:20240801-0012-001] request uri path: " + exchange.getRequest().getURI().getPath());

        HttpHeaders resHeaders = exchange.getResponse().getHeaders();
        resHeaders.set(HeaderKey.allowOrigin, HeaderValue.allowOrigin);
        resHeaders.set(HeaderKey.allowMethods, HeaderValue.allowMethods);
        resHeaders.set(HeaderKey.maxAge, HeaderValue.maxAge);
        resHeaders.set(HeaderKey.allowHeaders, HeaderValue.allowHeaders);

        return chain.filter(exchange);
    }

}
