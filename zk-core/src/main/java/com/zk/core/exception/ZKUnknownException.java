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
 * @Title: ZKUnknownException.java 
 * @author Vinson 
 * @Package com.zk.core.exception 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 2:24:12 PM 
 * @version V1.0   
*/
package com.zk.core.exception;

/**
 * 自定义运行时异常，为方便抓取异常时，区分系统抛出的异常和业务异常
 * @ClassName: ZKUnknownException
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
*/
public class ZKUnknownException extends RuntimeException {

    /**
     * @Fields serialVersionUID : TODO(描述变量)
     */
    private static final long serialVersionUID = 1L;

    /**
     * 异常分类
     */
    public static interface KeyExceptionType {
        public static final int general = 0;
    }

    /**
     * 异常的分类，暂未使用，0-未知；方便后继将异常按大类处理，如类型 1-弹出提示，2-xxx
     */
    int type;

    public ZKUnknownException(String msg) {
        this(KeyExceptionType.general, msg, null);

    }

    public ZKUnknownException(Throwable cause) {
        this(KeyExceptionType.general, null, cause);
    }

    public ZKUnknownException(String msg, Throwable cause) {
        this(KeyExceptionType.general, msg, cause);
    }

    public ZKUnknownException(int type, String msg, Throwable cause) {
        super(msg, cause);
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
