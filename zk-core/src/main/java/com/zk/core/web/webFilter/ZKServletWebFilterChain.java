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
* @Title: ZKServletWebFilterChain.java 
* @author Vinson 
* @Package com.zk.core.web.webFilter 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 28, 2023 4:08:21 PM 
* @version V1.0 
*/
package com.zk.core.web.webFilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/** 
* @ClassName: ZKServletWebFilterChain 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKServletWebFilterChain implements FilterChain {

    ZKServletWebFilterChainHandle handle;

    public ZKServletWebFilterChain() {
        this.handle = null;
    }

    public ZKServletWebFilterChain(ZKServletWebFilterChainHandle handle) {
        this.handle = handle;
    }

    /**
     * (not Javadoc)
     * 
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @see javax.servlet.FilterChain#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        if (this.handle != null) {
            this.handle.handle();
        }
    }

}
