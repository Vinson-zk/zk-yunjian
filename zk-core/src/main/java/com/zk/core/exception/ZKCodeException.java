/**
 * Copyright (c) 2004-2014 Vinson Technologies, Inc. address: All rights reserved.
 * 
 * This software is the confidential and proprietary information of Vinson Technologies, Inc. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with Vinson.
 *
 * @Title: ZKCodeException.java
 * @author Vinson
 * @Package com.zk.core.exception
 * @Description: TODO(simple description this file what to do.)
 * @date Dec 3, 2019 2:42:18 PM
 * @version V1.0
 */
package com.zk.core.exception;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.core.utils.ZKJsonUtils;

/**
 * 此类异常的异常消息为自定义的异常消息，没有经过国际化，消息不会做响应数据；响应数据中的消息需要根据错误码自行国际化；这一点会在异常处理器中统一处理
 * 
 * @ClassName: ZKCodeException
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKCodeException extends ZKUnknownException {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    /**
     * 异常代码
     */
    private String code;

    /**
     * 异常消息的参数
     */
    private Object[] msgArgs;

    /**
     * 异常时，返回的数据。
     */
    private Object data;

    /**
     * 生成数据检验错误异常
     *
     * @Title: asDataValidator
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 14, 2021 9:37:34 PM
     * @param validatorMsg
     * @return
     * @return ZKCodeException
     */
    public static ZKCodeException asDataValidator(Map<String, String> validatorMsg) {
        return as("zk.000002", "", null, validatorMsg);
    }

    public static ZKCodeException as(String code) {
        return as(KeyExceptionType.general, code, null, null, null, null);
    }

    public static ZKCodeException as(String code, String msg) {
        return as(KeyExceptionType.general, code, msg, null, null, null);
    }

    public static ZKCodeException as(String code, String msg, Object... msgArgs) {
        return as(KeyExceptionType.general, code, msg, msgArgs, null, null);
    }

    public static ZKCodeException as(String code, String msg, Object[] msgArgs, Object data) {
        return as(KeyExceptionType.general, code, msg, msgArgs, data, null);
    }

    public static ZKCodeException as(String code, String msg, Object[] msgArgs, Object data, Throwable cause) {
        return as(KeyExceptionType.general, code, msg, msgArgs, data, cause);
    }

    ///
    public static ZKCodeException as(int type, String code, String msg, Object[] msgArgs, Object data,
        Throwable cause) {
        return new ZKCodeException(type, code, msg, msgArgs, data, cause);
    }

    protected ZKCodeException(int type, String code, String msg, Object[] msgArgs, Object data, Throwable cause) {
        super(type, msg, cause);
        this.code = code;
        this.msgArgs = msgArgs;
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{type:").append(this.getType()).append(", code:").append(this.getCode()).append(", msg:")
            .append(super.getMessage()).append(", msgArgs:").append(this.getMsgArgsStr()).append(", data:")
            .append(this.getDataStr()).append("}");
        return sb.toString();
    }

//    @Override
//    public String getMessage() {
//        return this.toString();
//    }

    /**
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return msgArgs
     */
    public Object[] getMsgArgs() {
        return msgArgs;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * @return data
     */
    public Object getData() {
        return data;
    }

    @JsonIgnore
    public String getDataStr() {
        if (this.data != null) {
            if (this.data instanceof String) {
                return (String)this.data;
            }
            return ZKJsonUtils.writeObjectJson(this.data);
        }
        return "";
    }

    @JsonIgnore
    private String getMsgArgsStr() {
        if (this.msgArgs != null) {
            return ZKJsonUtils.writeObjectJson(this.msgArgs);
        }
        return "";
    }

}
