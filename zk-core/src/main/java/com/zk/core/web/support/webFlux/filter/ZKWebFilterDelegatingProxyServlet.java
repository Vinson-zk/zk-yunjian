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
* @Title: ZKWebFilterDelegatingProxyServlet.java 
* @author Vinson 
* @Package com.zk.core.web.support.webFlux.filter
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 18, 2023 9:16:37 AM 
* @version V1.0 
*/
package com.zk.core.web.support.webFlux.filter;
/** 
* @ClassName: ZKWebFilterDelegatingProxyServlet 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import com.zk.core.web.support.servlet.filter.ZKServletFilter;

import reactor.core.publisher.Mono;

public class ZKWebFilterDelegatingProxyServlet implements ZKWebFilter {

    ZKServletFilter zkServletFilter = null;

    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

//        ServerHttpRequest req = exchange.getRequest();
//        ServerHttpResponse res = exchange.getResponse();
//        MultiValueMap<String, HttpCookie> cs = req.getCookies();
        
        return chain.filter(exchange);

    }

}
