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
 * @Package com.zk.core.web.filter 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:19:48 PM 
 * @version V1.0   
*/
package com.zk.core.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.zk.core.utils.ZKStringUtils;

/**
 * 跨域拦截器
 * 
 * @ClassName: ZKCrosFilter
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKCrosFilter implements Filter {

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
    private String allowOrigin = "*";

    /***
     * header 中的 Access-Control-Allow-Methods
     */
    private String allowMethods = "POST,GET";

    /***
     * header 中的 Access-Control-Max-Age
     */
    private String maxAge = "3600";

    /***
     * header 中的 Access-Control-Allow-Headers
     */
    private String allowHeaders = "__SID,locale,Lang,X-Requested-With";

    /**
     * (not Javadoc)
     * <p>
     * Title: doFilter
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (ZKStringUtils.isNotBlank(allowOrigin)) {
            HttpServletResponse hRes = (HttpServletResponse) response;
            hRes.setHeader("Access-Control-Allow-Origin", allowOrigin);
            hRes.setHeader("Access-Control-Allow-Methods", allowMethods);
            hRes.setHeader("Access-Control-Max-Age", maxAge);
            hRes.setHeader("Access-Control-Allow-Headers", allowHeaders);
        }

        chain.doFilter(request, response);

    }

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

}
