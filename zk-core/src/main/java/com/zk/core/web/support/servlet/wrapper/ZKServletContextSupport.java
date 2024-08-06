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
* @Title: ZKServletContextSupport.java 
* @author Vinson 
* @Package com.zk.webmvc.servlet 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 27, 2021 2:35:53 PM 
* @version V1.0 
*/
package com.zk.core.web.support.servlet.wrapper;

import jakarta.servlet.ServletContext;

/** 
* @ClassName: ZKServletContextSupport 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKServletContextSupport {

    private ServletContext servletContext = null;

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    protected String getContextInitParam(String paramName) {
        return getServletContext().getInitParameter(paramName);
    }

    private ServletContext getRequiredServletContext() {
        ServletContext servletContext = getServletContext();
        if (servletContext == null) {
            String msg = "ServletContext property must be set via the setServletContext method.";
            throw new IllegalStateException(msg);
        }
        return servletContext;
    }

    protected void setContextAttribute(String key, Object value) {
        if (value == null) {
            removeContextAttribute(key);
        }
        else {
            getRequiredServletContext().setAttribute(key, value);
        }
    }

    protected Object getContextAttribute(String key) {
        return getRequiredServletContext().getAttribute(key);
    }

    protected void removeContextAttribute(String key) {
        getRequiredServletContext().removeAttribute(key);
    }

    /**
     * It is highly recommended not to override this method directly, and
     * instead override the {@link #toStringBuilder() toStringBuilder()} method,
     * a better-performing alternative.
     *
     * @return the String representation of this instance.
     */
    @Override
    public String toString() {
        return toStringBuilder().toString();
    }

    /**
     * Same concept as {@link #toString() toString()}, but returns a
     * {@link StringBuilder} instance instead.
     *
     * @return a StringBuilder instance to use for appending String data that
     *         will eventually be returned from a {@code toString()} invocation.
     */
    protected StringBuilder toStringBuilder() {
        return new StringBuilder(super.toString());
    }

}
