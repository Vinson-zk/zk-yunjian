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
* @Title: ZKUnknownException.java 
* @author Vinson 
* @Package com.zk.core.exception.base 
* @Description: TODO(simple description this file what to do. ) 
* @date May 27, 2024 11:33:14 AM 
* @version V1.0 
*/
package com.zk.core.exception.base;
/** 
* @ClassName: ZKUnknownException 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKUnknownException extends RuntimeException {

    /**
     * @Fields serialVersionUID : TODO(描述变量)
     */
    private static final long serialVersionUID = 1L;

    public ZKUnknownException(String msg) {
        this(msg, null);

    }

    public ZKUnknownException(Throwable cause) {
        this(null, cause);
    }

    public ZKUnknownException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
