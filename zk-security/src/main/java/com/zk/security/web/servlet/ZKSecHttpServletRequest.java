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
* @Title: ZKSecHttpServletRequest.java 
* @author Vinson 
* @Package com.zk.security.web.servlet 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 11:14:04 AM 
* @version V1.0 
*/
package com.zk.security.web.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/** 
* @ClassName: ZKSecHttpServletRequest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecHttpServletRequest extends HttpServletRequestWrapper {

    protected ServletContext servletContext = null;

    public ZKSecHttpServletRequest(HttpServletRequest wrapped, ServletContext servletContext) {
        super(wrapped);
        this.servletContext = servletContext;
    }

}
