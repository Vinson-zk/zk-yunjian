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
* @Title: ZKWebInputStream.java 
* @author Vinson 
* @Package com.zk.core.web.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 16, 2024 10:16:29 AM 
* @version V1.0 
*/
package com.zk.core.web.common;

import java.io.IOException;
import java.io.InputStream;

import jakarta.servlet.ReadListener;

/** 
* @ClassName: ZKWebInputStream 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWebInputStream extends InputStream {

    private final InputStream inputStream;

    public ZKWebInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public boolean isFinished() {
        try {
            return inputStream == null || inputStream.available() <= 0;
        }
        catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isReady() {
        try {
            return inputStream != null && inputStream.available() > 0;
        }
        catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
//      this.readListener = readListener;
    }

    public int read() throws IOException {
        return inputStream.read();
    }

}
