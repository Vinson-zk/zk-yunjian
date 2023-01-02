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
        return as(KeyExceptionType.general, code, null, null, null);
    }

    public static ZKMsgException as(String code, String msg) {
        return as(KeyExceptionType.general, code, msg, null, null);
    }

    public static ZKMsgException as(String code, String msg, Object data) {
        return as(KeyExceptionType.general, code, msg, data, null);
    }

    public static ZKMsgException as(String code, String msg, Object data, Throwable cause) {
        return as(KeyExceptionType.general, code, msg, data, cause);
    }

    public static ZKMsgException as(int type, String code, String msg, Object data, Throwable cause) {
        if(ZKStringUtils.isEmpty(msg)){
            msg = ZKMsgUtils.getMessage(code, (Object) null);
        }
        return new ZKMsgException(type, code, msg, data, cause);
    }

    protected ZKMsgException(int type, String code, String msg, Object data, Throwable cause) {
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
