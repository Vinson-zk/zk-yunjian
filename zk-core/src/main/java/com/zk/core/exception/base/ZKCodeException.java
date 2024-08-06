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
* @Title: ZKCodeException.java 
* @author Vinson 
* @Package com.zk.core.exception.base 
* @Description: TODO(simple description this file what to do. ) 
* @date May 27, 2024 11:33:47 AM 
* @version V1.0 
*/
package com.zk.core.exception.base;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStringUtils;

/** 
* @ClassName: ZKCodeException 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCodeException extends ZKUnknownException{
    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    /**
     * 异常代码
     */
    private String code;

    /**
     * 异常时，返回的数据。
     */
    private Object data;

    /**
     * 异常消息的参数
     */
    private Object[] msgArgs;

    // =======================================

//    public static ZKCodeException as(String code, Object data, Object... msgArgs) {
//        return as(code, ZKMsgUtils.getMessage(null, code, msgArgs), data);
//    }
//
//    public static ZKCodeException as(Locale locale, String code, Object data, Object... msgArgs) {
//        return as(code, ZKMsgUtils.getMessage(locale, code, msgArgs), data);
//    }
//
//    public static ZKCodeException as(String code, String msg, Object data) {
//        return new ZKCodeException(code, msg, data);
//    }

    // =======================================
    protected ZKCodeException(String code, String msg, Object data, Object... msgArgs) {
        super(msg);
        this.code = code;
        this.data = data;
        this.msgArgs = msgArgs;
    }

    /**
     * @return code
     */
    public String getCode() {
        return code;
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
                return (String) this.data;
            }
            return ZKJsonUtils.toJsonStr(this.data);
        }
        return "";
    }

    /**
     * @return msgArgs
     */
    @JsonIgnore
    public Object[] getMsgArgs() {
        return msgArgs;
    }

    @JsonIgnore
    private String getMsgArgsStr() {
        if (this.getMsgArgs() != null) {
            return ZKJsonUtils.toJsonStr(this.getMsgArgs());
        }
        return "";
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{code:").append(this.getCode()).append(", msg:").append(super.getMessage()).append(", msgArgs:")
                .append(this.getMsgArgsStr()).append(", data:").append(this.getDataStr()).append("}");
        return sb.toString();
    }

    public String getMsgByLocale(Locale locale) {
        // 当国际化语言不空，且异常消息为空时，根据传入语言重新设备国际化异常消息
        if (locale != null && ZKStringUtils.isEmpty(this.getMessage())) {
            return ZKMsgUtils.getMessage(locale, this.getCode(), this.getMsgArgs());
        }
        return this.getMessage();
    }

}
