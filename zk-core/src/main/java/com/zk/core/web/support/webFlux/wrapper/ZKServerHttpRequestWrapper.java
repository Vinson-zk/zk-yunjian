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
* @Title: ZKServerHttpRequestWrapper.java 
* @author Vinson 
* @Package com.zk.core.web.support.webFlux.wrapper 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 17, 2024 11:57:27 AM 
* @version V1.0 
*/
package com.zk.core.web.support.webFlux.wrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;

import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.utils.ZKCookieUtils;
import com.zk.core.web.wrapper.ZKRequestWrapper;

/**
 * @ClassName: ZKServerHttpRequestWrapper
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKServerHttpRequestWrapper extends ServerHttpRequestDecorator implements ZKRequestWrapper {

    protected Logger log = LogManager.getLogger(getClass());

    protected ServerWebExchange exchange;

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param delegate
     */
    public ZKServerHttpRequestWrapper(ServerWebExchange exchange) {
        super(exchange.getRequest());
        this.exchange = exchange;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getAttribute
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param name
     * @return
     * @see com.zk.core.web.wrapper.ZKRequestWrapper#getAttribute(java.lang.String)
     */
    @Override
    public Object getAttribute(String name) {
        return this.exchange.getAttribute(name);
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: setAttribute
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param key
     * @param value
     * @see com.zk.core.web.wrapper.ZKRequestWrapper#setAttribute(java.lang.String, java.lang.Object)
     */
    @Override
    public void setAttribute(String key, Object value) {
        this.exchange.getAttributes().put(key, value);
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: removeAttribute
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param key
     * @see com.zk.core.web.wrapper.ZKRequestWrapper#removeAttribute(java.lang.String)
     */
    @Override
    public void removeAttribute(String key) {
        this.exchange.getAttributes().remove(key);
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getPathWithinApplication
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see com.zk.core.web.wrapper.ZKRequestWrapper#getPathWithinApplication()
     */
    @Override
    public String getPathWithinApplication() {
        return this.exchange.getRequest().getPath().pathWithinApplication().value();
//        return null;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getParameter
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param name
     * @return
     * @see com.zk.core.web.wrapper.ZKRequestWrapper#getParameter(java.lang.String)
     */
    @Override
    public String getParameter(String name) {
        return this.exchange.getRequest().getQueryParams().getFirst(name);
//        return null;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getContextPath
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see com.zk.core.web.wrapper.ZKRequestWrapper#getContextPath()
     */
    @Override
    public String getContextPath() {
//        if (super.getDelegate() != null && super.getDelegate().getPath() != null) {
//            return super.getDelegate().getPath().contextPath().value();
//        }
//        return null;
        return super.getDelegate().getPath().contextPath().value();
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getRequestURI
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see com.zk.core.web.wrapper.ZKRequestWrapper#getRequestURI()
     */
    @Override
    public String getRequestURI() {
//        if (super.getDelegate() != null && super.getDelegate().getURI() != null) {
//            return super.getDelegate().getURI().getPath();
//        }
//        return null;
        return super.getDelegate().getURI().getPath();
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getHeader
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param name
     * @return
     * @see com.zk.core.web.wrapper.ZKRequestWrapper#getHeader(java.lang.String)
     */
    @Override
    public String getHeader(String name) {
        if (super.getDelegate() != null) {
            if (super.getDelegate().getHeaders() != null) {
                return super.getDelegate().getHeaders().getFirst(name);
            }
            else {
                log.error("");
            }
        }
        return null;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getWrapperRequest
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param <T>
     * @return
     * @see com.zk.core.web.wrapper.ZKRequestWrapper#getWrapperRequest()
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getWrapperRequest() {
        return (T) super.getDelegate();
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getCookieValue
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param cookieName
     * @return
     * @see com.zk.core.web.wrapper.ZKRequestWrapper#getCookieValue(java.lang.String)
     */
    @Override
    public String getCookieValue(String cookieName) {
        HttpCookie c = ZKCookieUtils.getCookie(super.getDelegate(), cookieName);
        return c == null ? null : c.getValue();
    }

    @Override
    public String getCleanParam(String paramName) {
        if (super.getDelegate() != null) {
            if (super.getDelegate().getQueryParams() != null) {
                return ZKStringUtils.clean(super.getDelegate().getQueryParams().getFirst(paramName));
            }
            else {
                log.error("");
            }
        }
        return null;
    }

}
