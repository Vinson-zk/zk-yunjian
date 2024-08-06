/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKCrosFilter.java 
 * @author Vinson 
 * @Package com.zk.core.web.support.servlet.filter
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:19:48 PM 
 * @version V1.0   
*/
package com.zk.core.web.support.servlet.filter;

import java.io.IOException;

import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.ZKWebConstants.HeaderKey;
import com.zk.core.web.ZKWebConstants.HeaderValue;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 跨域拦截器
 * 
 * @ClassName: ZKCrosFilter
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKCrosFilter extends ZKOncePerFilter {

    public static interface ParamsName {
        /**
         * 默认 *
         */
        public final String allowOrigin = "_zk_allowOrigin";

        /**
         * 默认 POST,GET
         */
        public final String allowMethods = "_zk_allowMethods";

        /**
         * 默认 3600
         */
        public final String maxAge = "_zk_maxAge";

        /**
         * 默认 __SID,locale,Lang,X-Requested-With
         */
        public final String allowHeaders = "_zk_allowHeaders";
    }

    /***
     * header中的Access-Control-Allow-Origin,为空时禁止跨域
     */
    public static String allowOrigin = HeaderValue.allowOrigin;

    /***
     * header 中的 Access-Control-Allow-Methods
     */
    public static String allowMethods = HeaderValue.allowMethods;

    /***
     * header 中的 Access-Control-Max-Age
     */
    public static String maxAge = HeaderValue.maxAge;

    /***
     * header 中的 Access-Control-Allow-Headers
     */
    public static String allowHeaders = HeaderValue.allowHeaders;

    /**
     * @param arg0
     * @throws ServletException
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     * @Title: init
     * @Description:
     */
    @Override
    public void init(FilterConfig arg0) throws ServletException {
        if (ZKStringUtils.isNotBlank(arg0.getInitParameter(ParamsName.allowOrigin))) {
            allowOrigin = arg0.getInitParameter("allowOrigin");
        }
        if (ZKStringUtils.isNotBlank(arg0.getInitParameter(ParamsName.allowMethods))) {
            allowMethods = arg0.getInitParameter("allowMethods");
        }
        if (ZKStringUtils.isNotBlank(arg0.getInitParameter(ParamsName.maxAge))) {
            maxAge = arg0.getInitParameter("maxAge");
        }
        if (ZKStringUtils.isNotBlank(arg0.getInitParameter(ParamsName.allowHeaders))) {
            allowHeaders = arg0.getInitParameter("allowHeaders");
        }
    }

    /**
     * 
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * 
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     * @see com.zk.core.web.support.servlet.filter.ZKOncePerFilter#doFilterInternal(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (ZKStringUtils.isNotBlank(allowOrigin)) {
            HttpServletResponse hRes = (HttpServletResponse) response;
            hRes.setHeader(HeaderKey.allowOrigin, allowOrigin);
            hRes.setHeader(HeaderKey.allowMethods, allowMethods);
            hRes.setHeader(HeaderKey.maxAge, maxAge);
            hRes.setHeader(HeaderKey.allowHeaders, allowHeaders);
        }
        chain.doFilter(request, response);
    }

}
