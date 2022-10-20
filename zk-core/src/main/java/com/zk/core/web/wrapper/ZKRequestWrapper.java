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
 * @Title: ZKRequestWrapper.java 
 * @author Vinson 
 * @Package com.zk.core.web.wrapper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:37:32 PM 
 * @version V1.0   
*/
package com.zk.core.web.wrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/** 
* @ClassName: ZKRequestWrapper 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKRequestWrapper extends HttpServletRequestWrapper {

    Map<String, Object> encInfo = new HashMap<String, Object>();

    public Map<String, Object> getEncInfo() {
        return encInfo;
    }

    Map<String, String[]> parameterMap;

    byte[] requestBody = null;

    public ZKRequestWrapper(HttpServletRequest request, Map<String, String[]> parameterMap, byte[] requestBody) {
        super(request);
        this.parameterMap = parameterMap;
        this.requestBody = requestBody;
//        this.requestBody = new byte[0];
    }

    /**
     * The default behavior of this method is to return getParameter(String
     * name) on the wrapped request object.
     */
    @Override
    public String getParameter(String name) {

        String[] values = this.getParameterMap().get(name);
        if (values != null) {
            return values.length > 0 ? values[0] : null;
        }

//        return null;
        return super.getParameter(name);
    }

    /**
     * The default behavior of this method is to return getParameterMap() on the
     * wrapped request object.
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        return this.parameterMap;
//        return this.request.getParameterMap();
    }

    /**
     * The default behavior of this method is to return getParameterNames() on
     * the wrapped request object.
     */
    @Override
    public Enumeration<String> getParameterNames() {
        return new Vector<String>(this.parameterMap.keySet()).elements();
//        return this.request.getParameterNames();
    }

    /**
     * The default behavior of this method is to return
     * getParameterValues(String name) on the wrapped request object.
     */
    @Override
    public String[] getParameterValues(String name) {

        return this.parameterMap.get(name);
//        return this.request.getParameterValues(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequestWrapper#getReader()
     */
    @Override
    public BufferedReader getReader() throws IOException {
        if (this.requestBody == null) {
            return super.getReader();
        }
        else {
            return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(requestBody)));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ServletRequestWrapper#getInputStream()
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {

        if (this.requestBody == null) {
            return super.getInputStream();
        }
        else {
            return new ZKServletInputStream(new ByteArrayInputStream(requestBody));
        }

    }

}
