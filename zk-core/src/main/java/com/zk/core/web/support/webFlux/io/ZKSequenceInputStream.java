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
* @Title: ZKSequenceInputStream.java 
* @author Vinson 
* @Package com.zk.core.web.support.webFlux.io 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 6, 2024 5:42:28 PM 
* @version V1.0 
*/
package com.zk.core.web.support.webFlux.io;

import java.io.InputStream;
import java.io.SequenceInputStream;

/** 
* @ClassName: ZKSequenceInputStream 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSequenceInputStream {

    private InputStream is;

    public ZKSequenceInputStream() {

    }

    public ZKSequenceInputStream(InputStream is) {
        this.is = is;
    }

    public void add(InputStream is) {
        if (this.is == null) {
            this.is = is;
        }
        else {
            this.is = new SequenceInputStream(this.is, is);
        }
    }

    public void lastAdd(InputStream is) {
        if (this.is == null) {
            this.is = is;
        }
        else {
            this.is = new SequenceInputStream(is, this.is);
        }
    }

    public InputStream getInputStream() {
        return this.is;
    }

}
