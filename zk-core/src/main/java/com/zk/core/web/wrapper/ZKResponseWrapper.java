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
 * @Title: ZKResponseWrapper.java 
 * @author Vinson 
 * @Package com.zk.core.web.wrapper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:37:55 PM 
 * @version V1.0   
*/
package com.zk.core.web.wrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/** 
* @ClassName: ZKResponseWrapper 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKResponseWrapper extends HttpServletResponseWrapper {

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    public ZKResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(outputStream);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ZKServletOutputStream(outputStream);
    }

    public byte[] getData() throws IOException {
        outputStream.flush();
//        printWriter.flush();
        outputStream.close();
//        printWriter.close();

        return outputStream.toByteArray();
    }

}
