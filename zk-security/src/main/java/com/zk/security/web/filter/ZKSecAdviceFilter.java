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
* @Title: ZKSecAdviceFilter.java 
* @author Vinson 
* @Package com.zk.security.web.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 6:56:07 AM 
* @version V1.0 
*/
package com.zk.security.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/** 
* @ClassName: ZKSecAdviceFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecAdviceFilter extends ZKSecAbstractFilter {

    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        Exception exception = null;

        try {

            boolean continueChain = preHandle(request, response);
            if (logger.isTraceEnabled()) {
                logger.trace("Invoked preHandle method.  Continuing chain?: [" + continueChain + "]");
            }

            if (continueChain) {
                executeChain(request, response, chain);
            }
            else {
                // zk.sec.000017=未通过身份鉴定拦截
                logger.info("[>_<:20220514-1036-001] {} zksec 身份拦截未通过；", this.getName());
                logger.info("[>_<:20220514-1036-002] {}-{} zksec 身份拦截未通过；", this.getClass(), chain.getClass());
            }

            postHandle(request, response);
            if (logger.isTraceEnabled()) {
                logger.trace("Successfully invoked postHandle method");
            }

        }
        catch(Exception e) {
            exception = e;
        } finally {
            cleanup(request, response, exception);
        }
    }

    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        return onPreHandle(request, response, null);
    }

    protected abstract boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception;

    protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
    }

    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception)
            throws Exception {
        // 拦截完成后处理
    }

    protected void executeChain(ServletRequest request, ServletResponse response, FilterChain chain) throws Exception {
        chain.doFilter(request, response);
    }

    protected void cleanup(ServletRequest request, ServletResponse response, Exception existing)
            throws ServletException, IOException {
        Exception exception = existing;
        try {
            afterCompletion(request, response, exception);
            if (logger.isTraceEnabled()) {
                logger.trace("Successfully invoked afterCompletion method.");
            }
        }
        catch(Exception e) {
            if (exception == null) {
                exception = e;
            }
            else {
                logger.debug("afterCompletion implementation threw an exception.  This will be ignored to "
                        + "allow the original source exception to be propagated.", e);
            }
        }
        if (exception != null) {
//            if (exception instanceof ZKCodeException) {
//                throw (ZKCodeException) exception;
//            }
//            else if (exception instanceof ZKMsgException) {
//                throw (ZKMsgException) exception;
//            }
//            else if (exception instanceof ZKSecUnknownException) {
//                throw (ZKSecUnknownException) exception;
//            }
//            else 
            if (exception instanceof ServletException) {
                throw (ServletException) exception;
            }
            else if (exception instanceof IOException) {
                throw (IOException) exception;
            }
            else {
                if (logger.isDebugEnabled()) {
                    String msg = "Filter execution resulted in an unexpected Exception "
                            + "(not IOException or ServletException as the Filter API recommends).  "
                            + "Wrapping in ServletException and propagating.";
                    logger.debug(msg);
                }
                throw new ServletException(exception);
            }
        }
    }
}
