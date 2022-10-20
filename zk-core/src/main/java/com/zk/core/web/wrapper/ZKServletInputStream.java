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
 * @Title: ZKServletInputStream.java 
 * @author Vinson 
 * @Package com.zk.core.web.wrapper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:38:13 PM 
 * @version V1.0   
*/
package com.zk.core.web.wrapper;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

/** 
* @ClassName: ZKServletInputStream 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKServletInputStream extends ServletInputStream {

    private final InputStream inputStream;

    public ZKServletInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public boolean isFinished() {
        try {
            return inputStream == null || inputStream.available() <= 0;
        }
        catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isReady() {
        try {
            return inputStream != null && inputStream.available() > 0;
        }
        catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
//      this.readListener = readListener;
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

}
