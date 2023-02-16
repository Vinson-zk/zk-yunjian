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
 * @Title: ZKMsgException.java 
 * @author Vinson 
 * @Package com.zk.core.exception 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:46:15 PM 
 * @version V1.0   
*/
package com.zk.core.exception;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStringUtils;

/**
 * 带有消息的异常，消息自行国际化，会在异常处理中直接根据消息，代码，数据做成响应数据
 * 
 * @ClassName: ZKMsgException
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKMsgException extends ZKUnknownException {

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

    public static ZKMsgException as(String code) {
        return as(KeyExceptionType.general, null, (Locale) null, code, null, null, (Object) null);
    }

    public static ZKMsgException as(String code, String msg) {
        return as(KeyExceptionType.general, null, (Locale) null, code, msg, null, (Object) null);
    }

    public static ZKMsgException as(String code, String msg, Object data) {
        return as(KeyExceptionType.general, null, (Locale) null, code, msg, data, (Object) null);
    }

    public static ZKMsgException as(String code, String msg, Object data, Object... msgArgs) {
        return as(KeyExceptionType.general, null, (Locale) null, code, msg, data, msgArgs);
    }

    public static ZKMsgException as(int type, Throwable cause, Locale locale, String code, String msg, Object data,
            Object... msgArgs) {
        if (ZKStringUtils.isEmpty(msg)) {
            msg = ZKMsgUtils.getMessage(locale, code, msgArgs);
        }
        return new ZKMsgException(type, cause, code, msg, data);
    }

    protected ZKMsgException(int type, Throwable cause, String code, String msg, Object data) {
        super(type, msg, cause);
        this.code = code;
        this.data = data;
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
            else {
                return ZKJsonUtils.writeObjectJson(this.data);
            }
        }
        return "";
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{type:").append(this.getType()).append(", code:").append(this.getCode()).append(", msg:")
                .append(super.getMessage()).append(", data:").append(this.getDataStr()).append("}");
        return sb.toString();
    }

}
