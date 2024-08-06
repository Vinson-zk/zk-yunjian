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
* @Title: ZKSecHttpServletRequestWrapper.java 
* @author Vinson 
* @Package com.zk.security.web.support.servlet.wrapper 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 11:14:04 AM 
* @version V1.0 
*/
package com.zk.security.web.support.servlet.wrapper;

import java.io.Serializable;

import com.zk.core.web.support.servlet.wrapper.ZKHttpServletRequestWrapper;
import com.zk.core.web.utils.ZKServletUtils;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.web.wrapper.ZKSecRequestWrapper;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @ClassName: ZKSecHttpServletRequestWrapper
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKSecHttpServletRequestWrapper extends ZKHttpServletRequestWrapper implements ZKSecRequestWrapper {

    /**
     * 
     * @param request
     * @param parameterMap
     * @param requestBody
     */
    public ZKSecHttpServletRequestWrapper(HttpServletRequest request) {
        super(request); // TODO Auto-generated constructor stub
    }

    public static final String POST_METHOD = "POST";

    @Override
    public boolean isLoginSubmission() {
        return (super.getRequest() instanceof HttpServletRequest)
                && ZKServletUtils.toHttp(super.getRequest()).getMethod().equalsIgnoreCase(POST_METHOD);
    }

    /**
     * 从请求中取令牌ID
     */
    public Serializable getTikcetId(boolean isTicketCookieEnabled) {
        Serializable tkId = null;
        if ((super.getRequest() instanceof HttpServletRequest)) {
            HttpServletRequest request = ZKServletUtils.toHttp(this.getRequest());
            // 1、从请属性中取令牌ID
            tkId = getTikcetIdInAttribute(request);
            // 2、从请求头中取令牌ID
            if (tkId == null) {
                tkId = getTikcetIdInHeader(request);
            }
            // 3、从请求参数中取令牌ID
            if (tkId == null) {
                tkId = getTikcetIdInParam(request);
            }
            // 4、从请求 cookie 中令牌ID
            if (tkId == null && isTicketCookieEnabled) {
                tkId = getTikcetIdInCookie(request);
            }
        }
        return tkId;
    }

    /**
     * 从请求头中取
     * 
     * @param request
     * @return
     */
    private Serializable getTikcetIdInHeader(HttpServletRequest request) {
        return request.getHeader(ZKSecConstants.PARAM_NAME.TicketId);
    }

    /**
     * 从请求属性中取
     * 
     * @param request
     * @return
     */
    private Serializable getTikcetIdInAttribute(HttpServletRequest request) {
        return request.getAttribute(ZKSecConstants.PARAM_NAME.TicketId) == null ? null
            : request.getAttribute(ZKSecConstants.PARAM_NAME.TicketId).toString();
    }

    /**
     * 从请求参数中取
     * 
     * @param request
     * @return
     */
    private Serializable getTikcetIdInParam(HttpServletRequest request) {
        return request.getParameter(ZKSecConstants.PARAM_NAME.TicketId);
    }

    /**
     * 从请求 Cookie 中取
     * 
     * @param request
     * @return
     */
    private Serializable getTikcetIdInCookie(HttpServletRequest request) {
        return ZKServletUtils.getCookieValueByRequest(request, ZKSecConstants.PARAM_NAME.TicketId);
    }

}
