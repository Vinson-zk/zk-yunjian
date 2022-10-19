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
* @Title: ZKSecCodeException.java 
* @author Vinson 
* @Package com.zk.security.exception 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 14, 2021 7:24:41 PM 
* @version V1.0 
*/
package com.zk.security.exception;

import com.zk.core.exception.ZKCodeException;

/** 
* @ClassName: ZKSecCodeException 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecCodeException extends ZKCodeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 创建指定代码异常
     * @param code
     */
    public ZKSecCodeException(String code) {
        super(KeyExceptionType.general, code, null, null, null, null);
    }

    public ZKSecCodeException(String code, String msg, Object[] msgArgs, Object data) {
        super(KeyExceptionType.general, code, msg, msgArgs, data, null);
    }

    /**
     * 创建指定代码异常
     * @param code 错误代码
     * @param cause 上级异常
     * @param msgArgs 消息替换参数
     * @param data 异常数据
     */
    public ZKSecCodeException(String code, String msg, Object[] msgArgs, Object data, Throwable cause) {
        super(KeyExceptionType.general, code, msg, msgArgs, data, cause);
    }

}
