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
 * @Title: ZKOncePerFilter.java 
 * @author Vinson 
 * @Package com.zk.core.web.support.servlet.filter
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:21:30 PM 
 * @version V1.0   
*/
package com.zk.core.web.support.servlet.filter;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.web.support.servlet.wrapper.ZKServletContextSupport;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/**
 * 同一请求，只执行一次拦截
 * 
 * @ClassName: ZKOncePerFilter
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public abstract class ZKOncePerFilter extends ZKServletContextSupport implements ZKServletFilter {

    protected Logger log = LogManager.getLogger(getClass());

    public static final String ALREADY_FILTERED_SUFFIX = "_ZK.FILTERED";

    protected String getAlreadyFilteredAttributeName() {
        return ALREADY_FILTERED_SUFFIX + getClass().getName();
    }

    @Override
    public final void doFilter(ServletRequest sReq, ServletResponse sRes, FilterChain filterChain)
            throws ServletException, IOException {
        String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
        if (sReq.getAttribute(alreadyFilteredAttributeName) != null) {
            // 此拦截对这次请求已执行过了
            log.trace("[^_^:20180622-0929-001] Filter '{}' already executed.  Proceeding without invoking this filter.",
                    this.getClass().getName());
            filterChain.doFilter(sReq, sRes);
        }
        else { // noinspection deprecation
               // Do invoke this filter...
            log.trace("Filter '{}' not yet executed.  Executing now.", this.getClass().getName());
            sReq.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);

            try {
                doFilterInternal(sReq, sRes, filterChain);
            } finally {
                // Once the request has finished, we're done and we don't
                // need to mark as 'already filtered' any more.
                sReq.removeAttribute(alreadyFilteredAttributeName);
            }
        }
    }

    protected abstract void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException;

//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        ServletContext servletContext = filterConfig.getServletContext();
//        System.out.println("[^_^:20240624-0030-001]----- servletContext.getClass: " + servletContext.getClass());
////        filterConfig.getInitParameter(ALREADY_FILTERED_SUFFIX)
////        StandardContext sc = null;
////        if (servletContext instanceof StandardContext) {
////            
////        }
////        servletContext.getcon
//    }

    @Override
    public void destroy() {

    }

}
