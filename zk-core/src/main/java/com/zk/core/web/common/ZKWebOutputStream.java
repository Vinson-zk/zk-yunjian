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
* @Title: ZKWebOutputStream.java 
* @author Vinson 
* @Package com.zk.core.web.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 16, 2024 10:17:42 AM 
* @version V1.0 
*/
package com.zk.core.web.common;

import java.io.IOException;
import java.io.OutputStream;

import jakarta.servlet.WriteListener;

/** 
* @ClassName: ZKWebOutputStream 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWebOutputStream extends OutputStream {

    private final OutputStream outputStream;

    public ZKWebOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public boolean isReady() {
        return false;
    }

    public void setWriteListener(WriteListener listener) {

    }

    public void write(int b) throws IOException {
        outputStream.write(b);
    }

}
