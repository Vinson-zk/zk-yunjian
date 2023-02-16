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
 * @Title: ZKExceptionHandlerResolver.java 
 * @author Vinson 
 * @Package com.zk.webmvc.resolver 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 3:08:35 PM 
 * @version V1.0   
*/
package com.zk.webmvc.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKExceptionsUtils;

/** 
* @ClassName: ZKExceptionHandlerResolver 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@Deprecated // 建议使用 com.zk.core.web.filter.ZKExceptionHandlerFilter 拦截器
public class ZKExceptionHandlerResolver implements HandlerExceptionResolver, Ordered {

    /** Logger available to subclasses. */
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private int order = Ordered.HIGHEST_PRECEDENCE + 66;

    /**
     * 
     * @return
     * @see org.springframework.core.Ordered#getOrder()
     */
    @Override
    public int getOrder() {
        // TODO Auto-generated method stub
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * 
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     * @see org.springframework.web.servlet.HandlerExceptionResolver#resolveException(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object,
     *      java.lang.Exception)
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest hReq, HttpServletResponse hRes, Object handler,
            Exception ex) {

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
        return new ModelAndView();

    }

}
