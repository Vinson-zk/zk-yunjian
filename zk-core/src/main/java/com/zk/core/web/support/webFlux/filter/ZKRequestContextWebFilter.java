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
* @Title: ZKRequestContextWebFilter.java 
* @author Vinson 
* @Package com.zk.core.web.support.webFlux.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 21, 2024 12:03:43 AM 
* @version V1.0 
*/
package com.zk.core.web.support.webFlux.filter;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import com.zk.core.web.support.webFlux.request.ZKRequestContextHolder;

import reactor.core.publisher.Mono;

/** 
* @ClassName: ZKRequestContextWebFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKRequestContextWebFilter extends ZKOncePerWebFilter {

    @Override
    protected Mono<Void> doFilterInternal(ServerWebExchange exchange, WebFilterChain chain) {
        ZKRequestContextHolder.setServerWebExchange(exchange);
        return chain.filter(exchange);
    }

}
