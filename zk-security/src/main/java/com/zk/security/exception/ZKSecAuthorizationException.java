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
* @Title: ZKSecAuthorizationException.java 
* @author Vinson 
* @Package com.zk.security.exception 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 12:53:44 PM 
* @version V1.0 
*/
package com.zk.security.exception;

/**
 * 鉴权异常
 * 
 * @ClassName: ZKSecAuthorizationException
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKSecAuthorizationException extends ZKSecUnknownException {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = -2704958363434808054L;

    public static interface Type {
        // 没有 api 权限代码
        public static final int apiCode = 1;

        // 没有权限
        public static final int permissions = 2;

    }

    /**
     * 鉴权异常类型
     */
    protected int type;

    /**
     * 权限标识
     */
    protected String authCode;

    public ZKSecAuthorizationException(int type, String authCode, String msg) {
        super(msg); // TODO Auto-generated constructor stub
        this.type = type;
        this.authCode = authCode;
    }

    @Override
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    @Override
    public String getMessage() {
        if (this.getType() == 1) {
            // 没有 api 权限代码
            return "没有 Api 接口 [" + this.getAuthCode() + "] 权限代码。" + super.getMessage();
        }
        return "没有权限 [" + this.getAuthCode() + "] 权限代码。" + super.getMessage();
    }

}
