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
 * @Title: ZKSerCenAuthenticationException.java 
 * @author Vinson 
 * @Package com.zk.server.central.security 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:29:58 PM 
 * @version V1.0   
*/
package com.zk.server.central.security;

import org.apache.shiro.authc.AuthenticationException;

/** 
* @ClassName: ZKSerCenAuthenticationException 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSerCenAuthenticationException extends AuthenticationException {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    private String code;

    private Object[] msgArgs;

    private Object data;

    public ZKSerCenAuthenticationException() {

    }

    public ZKSerCenAuthenticationException(String code, Object[] msgArgs, Object data) {
        this.code = code;
        this.msgArgs = msgArgs;
        this.data = data;
    }

    public ZKSerCenAuthenticationException(String code, Object[] msgArgs, Object data, Throwable cause) {
        super(cause);
        this.code = code;
        this.msgArgs = msgArgs;
        this.data = data;
    }

    /**
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return msgArgs
     */
    public Object[] getMsgArgs() {
        return msgArgs;
    }

    /**
     * @param msgArgs
     *            the msgArgs to set
     */
    public void setMsgArgs(Object[] msgArgs) {
        this.msgArgs = msgArgs;
    }

    /**
     * @return data
     */
    public Object getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Object data) {
        this.data = data;
    }

}
