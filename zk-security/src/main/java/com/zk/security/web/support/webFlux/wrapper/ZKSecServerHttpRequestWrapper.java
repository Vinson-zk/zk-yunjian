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
* @Title: ZKSecServerHttpRequestWrapper.java 
* @author Vinson 
* @Package com.zk.security.web.support.webFlux.wrapper 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 21, 2024 11:55:49 PM 
* @version V1.0 
*/
package com.zk.security.web.support.webFlux.wrapper;

import java.io.Serializable;

import org.springframework.web.server.ServerWebExchange;

import com.zk.core.web.support.webFlux.wrapper.ZKServerHttpRequestWrapper;
import com.zk.core.web.utils.ZKReactiveUtils;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.web.wrapper.ZKSecRequestWrapper;

/** 
* @ClassName: ZKSecServerHttpRequestWrapper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecServerHttpRequestWrapper extends ZKServerHttpRequestWrapper implements ZKSecRequestWrapper {

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param exchange
     */
    public ZKSecServerHttpRequestWrapper(ServerWebExchange exchange) {
        super(exchange); // TODO Auto-generated constructor stub
    }

    public static final String POST_METHOD = "POST";

    @Override
    public boolean isLoginSubmission() {
        return this.exchange.getRequest().getMethod().name().equalsIgnoreCase(POST_METHOD);
    }

    /**
     * 从请求中取令牌ID
     */
    @Override
    public Serializable getTikcetId(boolean isTicketCookieEnabled) {
        Serializable tkId = null;
        // 1、从请属性中取令牌ID
        tkId = getTikcetIdInAttribute(exchange);
        // 2、从请求头中取令牌ID
        if (tkId == null) {
            tkId = getTikcetIdInHeader(exchange);
        }
        // 3、从请求参数中取令牌ID
        if (tkId == null) {
            tkId = getTikcetIdInParam(exchange);
        }
        // 4、从请求 cookie 中令牌ID
        if (tkId == null && isTicketCookieEnabled) {
            tkId = getTikcetIdInCookie(exchange);
        }
        return tkId;
    }

    /**
     * 从请求头中取
     * 
     * @param request
     * @return
     */
    private Serializable getTikcetIdInHeader(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst(ZKSecConstants.PARAM_NAME.TicketId);
    }

    /**
     * 从请求属性中取
     * 
     * @param request
     * @return
     */
    private Serializable getTikcetIdInAttribute(ServerWebExchange exchange) {
        return exchange.getAttribute(ZKSecConstants.PARAM_NAME.TicketId) == null ? null
            : exchange.getAttribute(ZKSecConstants.PARAM_NAME.TicketId);
    }

    /**
     * 从请求参数中取
     * 
     * @param request
     * @return
     */
    private Serializable getTikcetIdInParam(ServerWebExchange exchange) {
        return exchange.getRequest().getQueryParams().getFirst(ZKSecConstants.PARAM_NAME.TicketId);
    }

    /**
     * 从请求 Cookie 中取
     * 
     * @param request
     * @return
     */
    private Serializable getTikcetIdInCookie(ServerWebExchange exchange) {
        return ZKReactiveUtils.getCookieValueByRequest(exchange.getRequest(), ZKSecConstants.PARAM_NAME.TicketId);
    }

}
