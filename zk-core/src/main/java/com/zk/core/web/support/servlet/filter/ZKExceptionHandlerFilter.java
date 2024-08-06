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
* @Title: ZKExceptionHandlerFilter.java 
* @author Vinson 
* @Package com.zk.core.web.support.servlet.filter
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 15, 2023 9:46:34 AM 
* @version V1.0 
*/
package com.zk.core.web.support.servlet.filter;

import java.io.IOException;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.web.ZKWebConstants.HeaderKey;
import com.zk.core.web.utils.ZKServletUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** 
* @ClassName: ZKExceptionHandlerFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKExceptionHandlerFilter extends ZKOncePerFilter {

    /**
     * (not Javadoc)
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
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("ZKExceptionHandlerFilter just supports HTTP requests");
        }
        HttpServletRequest hReq = (HttpServletRequest) request;
        HttpServletResponse hRes = (HttpServletResponse) response;

        Exception ex = null;
        try {
            log.debug("[^_^:20240206-2322-001] ZKExceptionHandlerFilter 异常拦截");
            chain.doFilter(hReq, hRes);
        }
        catch(Exception e) {
            ex = e;
        } finally {
            if(ex != null) {
                /* 使用response返回 */
                hRes.setStatus(HttpStatus.OK.value()); // 设置状态码
                hRes.setContentType(MediaType.APPLICATION_JSON_VALUE); // 设置ContentType
                hRes.setCharacterEncoding("UTF-8"); // 避免乱码
                hRes.setHeader(HeaderKey.cacheControl, "no-cache, must-revalidate");
                Locale locale = ZKServletUtils.getLocale(ZKServletUtils.toHttp(request));
                ZKMsgRes msgRes = ZKMsgRes.as(locale, ex);
                log.error("[>_<:20230214-2357-001] {} 请求处理结果异常 -> status code:{}; msg:{}; exception class:{}; ",
                        this.getClass().getSimpleName(), hRes.getStatus(), msgRes.toString(), ex.getClass().getName());
                ZKServletUtils.write(hRes, msgRes.toString());
            }
        }
    }

}
