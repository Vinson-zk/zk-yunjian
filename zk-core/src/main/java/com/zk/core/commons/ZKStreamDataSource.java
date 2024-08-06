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
* @Title: ZKStreamDataSource.java 
* @author Vinson 
* @Package com.zk.core.commons 
* @Description: TODO(simple description this file what to do. ) 
* @date May 26, 2022 11:19:25 PM 
* @version V1.0 
*/
package com.zk.core.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jakarta.activation.DataSource;

/**
 * @ClassName: ZKStreamDataSource
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKStreamDataSource implements DataSource {

//    private byte[] bytes;
    
    private InputStream inputStream;

    private OutputStream outputStream;

    private String contentType;

    private String name;

    public ZKStreamDataSource(String name, String contentType, InputStream inputStream,
            OutputStream outputStream) {
        this.name = name;
        this.contentType = contentType;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public InputStream getInputStream() throws IOException {
//        return new ByteArrayInputStream(this.bytes);
        if (this.inputStream != null) {
            this.inputStream.reset();
        }
        return this.inputStream;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return this.outputStream;
    }

}
