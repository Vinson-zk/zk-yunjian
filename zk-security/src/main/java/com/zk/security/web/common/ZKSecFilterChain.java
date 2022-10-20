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
* @Title: ZKSecFilterChain.java 
* @author Vinson 
* @Package com.zk.security.web.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 27, 2021 7:48:09 PM 
* @version V1.0 
*/
package com.zk.security.web.common;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
* @ClassName: ZKSecFilterChain 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecFilterChain implements FilterChain {

    private static final Logger log = LoggerFactory.getLogger(ZKSecFilterChain.class);

    private FilterChain orig;

    private List<Filter> filters;

    private int index = 0;

    public ZKSecFilterChain(FilterChain orig, List<Filter> filters) {
        this.orig = orig;
        this.filters = filters;
        this.index = 0;
    }

    public List<Filter> getFilters() {
        return this.filters;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
//        if (this.filters != null && this.filters.size() != this.index) {
//            if (log.isTraceEnabled()) {
//                log.trace("Invoking wrapped filter at index [" + this.index + "]");
//            }
//            this.filters.get(this.index++).doFilter(request, response, this);
//        }

        if (this.filters == null || this.filters.size() == this.index) {
            // we've reached the end of the wrapped chain, so invoke the
            // original one:
            if (log.isTraceEnabled()) {
                log.trace("Invoking original filter chain.");
            }
            this.orig.doFilter(request, response);
        }
        else {
            if (log.isTraceEnabled()) {
                log.trace("Invoking wrapped filter at index [" + this.index + "]");
            }
            this.filters.get(this.index++).doFilter(request, response, this);
        }
    }

}
