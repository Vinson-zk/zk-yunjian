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
 * @Package com.zk.core.web.filter 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:21:30 PM 
 * @version V1.0   
*/
package com.zk.core.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zk.core.web.wrapper.ZKServletContextSupport;

/**
 * 同一请求，只执行一次拦截
 * 
 * @ClassName: ZKOncePerFilter
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public abstract class ZKOncePerFilter extends ZKServletContextSupport implements Filter {

    protected Logger log = LoggerFactory.getLogger(getClass());

    public static final String ALREADY_FILTERED_SUFFIX = ".FILTERED";

    protected String getAlreadyFilteredAttributeName() {
        return getClass().getName() + ALREADY_FILTERED_SUFFIX;
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

}
