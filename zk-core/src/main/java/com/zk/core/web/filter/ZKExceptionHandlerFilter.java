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
* @Package com.zk.core.web.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 15, 2023 9:46:34 AM 
* @version V1.0 
*/
package com.zk.core.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKExceptionsUtils;

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
     * @see com.zk.core.web.filter.ZKOncePerFilter#doFilterInternal(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("ZKLogAccessFilter just supports HTTP requests");
        }
        HttpServletRequest hReq = (HttpServletRequest) request;
        HttpServletResponse hRes = (HttpServletResponse) response;

        Exception ex = null;
        try {
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
                hRes.setHeader("Cache-Control", "no-cache, must-revalidate");
                ZKMsgRes msgRes = ZKExceptionsUtils.as(ex);
                log.error("[>_<:20230214-2357-001] {} 请求处理结果异常 -> status code:{}; msg:{}; exception class:{}; ",
                        this.getClass().getSimpleName(), hRes.getStatus(), msgRes.toString(), ex.getClass().getName());
                ex.printStackTrace();
                msgRes.write(hRes);
            }
        }
    }

}
