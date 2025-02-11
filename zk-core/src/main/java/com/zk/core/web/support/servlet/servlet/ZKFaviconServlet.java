/** 
* Copyright (c) 2012-2025 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKFaviconServlet.java 
* @author Vinson 
* @Package com.zk.core.web.support.servlet.servlet 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 10, 2025 2:50:10 PM 
* @version V1.0 
*/
package com.zk.core.web.support.servlet.servlet;

import java.io.IOException;

import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/** 
* @ClassName: ZKFaviconServlet 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKFaviconServlet implements Servlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
    }

    @Override
    public ServletConfig getServletConfig() {
        // TODO Auto-generated method stub return null;
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        // TODO Auto-generated method stub
//        res.getWriter().write("favicon.ico");
    }

    @Override
    public String getServletInfo() {
        // TODO Auto-generated method stub return null;
        return null;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

}
