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
 * @Title: ZKServletOutputStream.java 
 * @author Vinson 
 * @Package com.zk.core.web.wrapper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:38:33 PM 
 * @version V1.0   
*/
package com.zk.core.web.wrapper;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/** 
* @ClassName: ZKServletOutputStream 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKServletOutputStream extends ServletOutputStream {

    private final OutputStream outputStream;

    public ZKServletOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public boolean isReady() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setWriteListener(WriteListener listener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void write(int b) throws IOException {
        // TODO Auto-generated method stub
        outputStream.write(b);
    }

}
