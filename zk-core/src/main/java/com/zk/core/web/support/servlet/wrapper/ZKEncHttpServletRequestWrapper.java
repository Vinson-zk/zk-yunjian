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
* @Title: ZKEncHttpServletRequestWrapper.java 
* @author Vinson 
* @Package com.zk.core.web.support.servlet.wrapper 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 16, 2024 4:18:19 PM 
* @version V1.0 
*/
package com.zk.core.web.support.servlet.wrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import jakarta.servlet.http.HttpServletRequest;

import com.zk.core.web.common.ZKWebInputStream;
import com.zk.core.web.wrapper.ZKEncRequestWrapper;

/** 
* @ClassName: ZKEncHttpServletRequestWrapper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKEncHttpServletRequestWrapper extends ZKHttpServletRequestWrapper implements ZKEncRequestWrapper {

    Map<String, Object> encInfo = new HashMap<String, Object>();

    public Map<String, Object> getEncInfo() {
        return encInfo;
    }

    Map<String, String[]> parameterMap;

    byte[] requestBody = null;

    public ZKEncHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * The default behavior of this method is to return getParameter(String name) on the wrapped request object.
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
     * The default behavior of this method is to return getParameterMap() on the wrapped request object.
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        if (this.parameterMap == null) {
            return super.getParameterMap();
        }
        return this.parameterMap;
    }

    /**
     * The default behavior of this method is to return getParameterNames() on the wrapped request object.
     */
    @Override
    public Enumeration<String> getParameterNames() {
        return new Vector<String>(this.getParameterMap().keySet()).elements();
//        return this.request.getParameterNames();
    }

    /**
     * The default behavior of this method is to return getParameterValues(String name) on the wrapped request object.
     */
    @Override
    public String[] getParameterValues(String name) {
        return this.getParameterMap().get(name);
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
    public ZKWebInputStream getZKInputStream() throws IOException {

        if (this.requestBody == null) {
            return new ZKWebInputStream(super.getInputStream());
        }
        else {
            return new ZKWebInputStream(new ByteArrayInputStream(requestBody));
        }

    }

    /**
     * (not Javadoc)
     * 
     * @param zkParameterMap
     * @param reqBody
     * @return
     * @see com.zk.core.web.wrapper.ZKEncRequestWrapper#afterDecryptSet(java.util.Map, byte[])
     */
    @Override
    public void afterDecryptSet(Map<String, String[]> zkParameterMap, byte[] reqBody) {
        this.parameterMap = zkParameterMap;
        this.requestBody = reqBody;

    }

}
