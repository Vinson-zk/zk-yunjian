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
* @Title: ZKServerHttpResponseWrapper.java 
* @author Vinson 
* @Package com.zk.core.web.support.webFlux.wrapper 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 21, 2024 11:59:46 PM 
* @version V1.0 
*/
package com.zk.core.web.support.webFlux.wrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;

import com.zk.core.web.wrapper.ZKResponseWrapper;

/** 
* @ClassName: ZKServerHttpResponseWrapper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKServerHttpResponseWrapper extends ServerHttpRequestDecorator implements ZKResponseWrapper {

    protected Logger log = LogManager.getLogger(getClass());

    protected ServerWebExchange exchange;

    public ZKServerHttpResponseWrapper(ServerWebExchange exchange) {
        super(exchange.getRequest());
        this.exchange = exchange;
    }

    @Override
    public void addHeader(String name, String value) {
        exchange.getResponse().getHeaders().add(name, value);
    }

    @Override
    public void setHeader(String name, String value) {
        exchange.getResponse().getHeaders().set(name, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getWrapperResponse() {
        return (T) exchange.getResponse();
    }

    @Override
    public boolean containsHeader(String name) {
        return exchange.getResponse().getHeaders().containsKey(name);
    }
}
