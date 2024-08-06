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

import com.zk.core.exception.base.ZKUnknownException;
import com.zk.security.web.filter.common.ZKSecFilterChain;
import com.zk.security.web.wrapper.ZKSecRequestWrapper;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;

/** 
* @ClassName: ZKSecAdviceFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecAdviceFilter extends ZKSecAbstractFilter {

    @Override
    protected void doFilterInternal(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes, ZKSecFilterChain zkChain)
            throws IOException {
        // TODO Auto-generated method stub
        Exception exception = null;

        try {

            boolean continueChain = preHandle(zkReq, zkRes);
            if (logger.isTraceEnabled()) {
                logger.trace("Invoked preHandle method.  Continuing chain?: [" + continueChain + "]");
            }

            if (continueChain) {
                executeChain(zkReq, zkRes, zkChain);
            }

            postHandle(zkReq, zkRes);
            if (logger.isTraceEnabled()) {
                logger.trace("Successfully invoked postHandle method");
            }

        }
        catch(Exception e) {
            logger.error("[>_<:20220514-1036-001] {} zksec 拦截未通过；", this.getName());
            logger.error("[>_<:20220514-1036-002] {}-{} zksec 拦截未通过；", this.getClass(), zkChain.getClass());
            exception = e;
        } finally {
            cleanup(zkReq, zkRes, exception);
        }
    }

    protected boolean preHandle(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes) throws Exception {
        return onPreHandle(zkReq, zkRes, null);
    }

    protected abstract boolean onPreHandle(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes, Object mappedValue)
            throws Exception;

    protected void postHandle(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes) throws Exception {
    }

    public void afterCompletion(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes, Exception exception)
            throws Exception {
        // 拦截完成后处理
    }

    protected void executeChain(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes, ZKSecFilterChain zkChain)
            throws Exception {
        zkChain.doFilter(zkReq, zkRes);
    }

    protected void cleanup(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes, Exception existing)
            throws IOException {
        Exception exception = existing;
        try {
            afterCompletion(zkReq, zkRes, exception);
            if (logger.isTraceEnabled()) {
                logger.trace("Successfully invoked afterCompletion method.");
            }
        }
        catch(Exception e) {
            if (logger.isDebugEnabled()) {
                String msg = "ZKSecFilter execution resulted in an unexpected Exception "
                        + "(not IOException or ServletException as the Filter API recommends).  "
                        + "Wrapping in ServletException and propagating.";
                logger.debug(msg);
            }
            if (exception == null) {
                exception = e;
            }
            else {
                logger.debug("afterCompletion implementation threw an exception.  This will be ignored to "
                        + "allow the original source exception to be propagated.", e);
            }
        }
        if (exception != null) {
            if (exception instanceof ZKUnknownException) {
                throw (ZKUnknownException) exception;
            }
//            else if (exception instanceof IOException) {
//                throw (IOException) exception;
//            }
//            else if (exception instanceof ServletException) {
//                throw (ServletException) exception;
//            }
            else {
                if (logger.isDebugEnabled()) {
                    String msg = "Filter execution resulted in an unexpected Exception "
                            + "(not IOException or ServletException as the Filter API recommends).  "
                            + "Wrapping in ServletException and propagating.";
                    logger.debug(msg);
                }
                throw new ZKUnknownException(exception);
            }
        }
    }
}
