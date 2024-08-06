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
* @Title: ZKEncHttpServletResponseWrapper.java 
* @author Vinson 
* @Package com.zk.core.web.support.servlet.wrapper 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 16, 2024 4:18:38 PM 
* @version V1.0 
*/
package com.zk.core.web.support.servlet.wrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletResponse;

import com.zk.core.web.common.ZKWebOutputStream;
import com.zk.core.web.wrapper.ZKEncResponseWrapper;

/** 
* @ClassName: ZKEncHttpServletResponseWrapper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKEncHttpServletResponseWrapper extends ZKHttpServletResponseWrapper implements ZKEncResponseWrapper {

    /**
     * 
     * @param response
     */
    public ZKEncHttpServletResponseWrapper(HttpServletResponse response) {
        super(response); // TODO Auto-generated constructor stub
    }

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(outputStream);
    }

    @Override
    public ZKWebOutputStream getZKOutputStream() throws IOException {
        return new ZKWebOutputStream(outputStream);
    }

    @Override
    public byte[] getData() throws IOException {
        outputStream.flush();
//        printWriter.flush();
        outputStream.close();
//        printWriter.close();

        return outputStream.toByteArray();
    }

}
