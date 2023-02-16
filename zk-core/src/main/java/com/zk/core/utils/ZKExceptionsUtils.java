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
 * @Title: ZKExceptionsUtils.java 
 * @author Vinson 
 * @Package com.zk.core.exception 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 2:59:21 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.exception.ZKCodeException;
import com.zk.core.exception.ZKMsgException;
import com.zk.core.exception.ZKValidatorException;

/** 
* @ClassName: ZKExceptionsUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKExceptionsUtils {

    protected static Logger log = LoggerFactory.getLogger(ZKExceptionsUtils.class);

    /**
     * 将CheckedException转换为UncheckedException.
     */
    public static RuntimeException unchecked(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        else {
            return new RuntimeException(e);
        }
    }

    /**
     * 将ErrorStack转化为String.
     */
    public static String getStackTraceAsString(Throwable e) {
        if (e == null) {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * 将异常转为 ZKMsgRes
     */
    public static ZKMsgRes as(Throwable e) {
        // 数据验证异常：com.zk.utils.exception.ZKValidatorExceptions
        if (e instanceof ZKValidatorException) {
            ZKValidatorException zkValidatorE = (ZKValidatorException) e;
            // 数据验证异常
            log.error("[>_<:20230214-2348-001] {}",
                    ZKJsonUtils.writeObjectJson(zkValidatorE.getMessagePropertyAndMessageAsMap()));
//            ZKMsgRes msgRes = ZKMsgRes.as("zk.000002", null, zkValidatorE.getMessagePropertyAndMessageAsList());
            return ZKMsgRes.as("zk.000002", null, zkValidatorE.getMessagePropertyAndMessageAsMap());
        }
        else if (e instanceof ZKCodeException) {
            ZKCodeException codeE = (ZKCodeException) e;
            // 根据异常码，国际化异常信息
            return ZKMsgRes.as(codeE);
        }
        else if (e instanceof ZKMsgException) {
            ZKMsgException msgE = (ZKMsgException) e;
            return ZKMsgRes.as(msgE);
        }
        else {
            log.error("[>_<:20230214-2348-002] ZKExceptionsUtils.as -> exception class:{}", e.getClass().getName());
            return ZKMsgRes.as("zk.1", e.getMessage());
        }
    }
}
