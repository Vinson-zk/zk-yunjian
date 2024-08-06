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
* @Title: ZKTransferCipherException.java 
* @author Vinson 
* @Package com.zk.core.exception.sub 
* @Description: TODO(simple description this file what to do. ) 
* @date May 27, 2024 2:55:27 PM 
* @version V1.0 
*/
package com.zk.core.exception.sub;

import com.zk.core.exception.base.ZKUnknownException;

/** 
* @ClassName: ZKTransferCipherException 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKTransferCipherException extends ZKUnknownException {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    public ZKTransferCipherException(String msg) {
        super(msg); // TODO Auto-generated constructor stub
    }

    public ZKTransferCipherException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
