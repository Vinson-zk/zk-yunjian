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
* @Title: ZKSecUnknownException.java 
* @author Vinson 
* @Package com.zk.security.exception 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 7:24:27 PM 
* @version V1.0 
*/
package com.zk.security.exception;

import com.zk.core.exception.ZKUnknownException;

/** 
* @ClassName: ZKSecUnknownException 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecUnknownException extends ZKUnknownException {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = -2314118306924424408L;

    public ZKSecUnknownException(String msg) {
        super(msg); // TODO Auto-generated constructor stub
    }

    public ZKSecUnknownException(Throwable cause) {
        super(cause);
    }

    public ZKSecUnknownException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
