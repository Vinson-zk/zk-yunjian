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
* @Title: ZKOncePerWebFilter.java 
* @author Vinson 
* @Package com.zk.core.web.support.webFlux.filter
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 28, 2023 10:44:04 AM 
* @version V1.0 
*/
package com.zk.core.web.support.webFlux.filter;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

/** 
* @ClassName: ZKOncePerWebFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKOncePerWebFilter extends ZKAbstractWebFilter implements ZKWebFilter {

    protected Logger log = LogManager.getLogger(getClass());

    public static final String ALREADY_FILTERED_SUFFIX = "_ZK.WEB.FLUX.FILTERED";

    protected String getAlreadyFilteredAttributeName() {
        return ALREADY_FILTERED_SUFFIX + getClass().getName();
    }

    /**
     * 
     * @param exchange
     * @param chain
     * @return
     * @see org.springframework.web.server.WebFilter#filter(org.springframework.web.server.ServerWebExchange, org.springframework.web.server.WebFilterChain)
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();

        if (exchange.getAttribute(alreadyFilteredAttributeName) != null) {
            // 此拦截对这次请求已执行过了
            log.trace("[^_^:20230238-0729-001] Filter '{}' already executed.  Proceeding without invoking this filter.",
                    this.getClass().getName());
            return chain.filter(exchange);
        }
        else { // noinspection deprecation
               // Do invoke this filter...
            log.trace("Filter '{}' not yet executed.  Executing now.", this.getClass().getName());
            exchange.getAttributes().put(alreadyFilteredAttributeName, Boolean.TRUE);

            try {
                return doFilterInternal(exchange, chain);
            } finally {
                // Once the request has finished, we're done and we don't
                // need to mark as 'already filtered' any more.
                exchange.getAttributes().remove(alreadyFilteredAttributeName);
            }
        }
    }

    protected abstract Mono<Void> doFilterInternal(ServerWebExchange exchange, WebFilterChain chain);

}
