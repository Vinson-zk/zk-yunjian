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
* @Title: ZKSecDefaultFilterChain.java 
* @author Vinson 
* @Package com.zk.security.web.filter.common
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 15, 2024 5:08:45 PM 
* @version V1.0 
*/
package com.zk.security.web.filter.common;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.security.web.filter.ZKSecFilter;
import com.zk.security.web.wrapper.ZKSecRequestWrapper;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;

/** 
* @ClassName: ZKSecDefaultFilterChain 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecDefaultFilterChain implements ZKSecFilterChain {

    private static final Logger log = LogManager.getLogger(ZKSecDefaultFilterChain.class);

    private ZKSecFilterChain orig;

    private List<ZKSecFilter> filters;

    private int index = 0;

    public ZKSecDefaultFilterChain(ZKSecFilterChain orig, List<ZKSecFilter> filters) {
        this.orig = orig;
        this.filters = filters;
        this.index = 0;
    }

    public List<ZKSecFilter> getFilters() {
        return this.filters;
    }

    @Override
    public void doFilter(ZKSecRequestWrapper request, ZKSecResponseWrapper response) throws IOException {
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
            if (this.orig != null) {
                this.orig.doFilter(request, response);
            }
        }
        else {
            if (log.isTraceEnabled()) {
                log.trace("Invoking wrapped filter at index [" + this.index + "]");
            }
            this.filters.get(this.index++).doFilter(request, response, this);
        }
    }

}
