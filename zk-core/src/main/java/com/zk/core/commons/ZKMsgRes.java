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
 * @Title: ZKMsgRes.java 
 * @author Vinson 
 * @Package com.zk.core.web 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 3:00:52 PM 
 * @version V1.0   
*/
package com.zk.core.commons;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zk.core.exception.ZKCodeException;
import com.zk.core.exception.ZKMsgException;
import com.zk.core.exception.ZKUnknownException.KeyExceptionType;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStringUtils;

/**
 * @ClassName: ZKMsgRes
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
@JsonIgnoreProperties(value = { "ok" }, allowGetters = true) // 序列化时不字段不忽略；反序列化时字段忽略；
public class ZKMsgRes {

    /**
     * 日志对象
     */
    protected static Logger log = LoggerFactory.getLogger(ZKMsgRes.class);

    /**
     * 响应错误码，zk.0 成功
     */
    private String code;

    /**
     * 响应错误码，对应的国际化信息
     */
    private String msg;

//    /**
//     * 响应的国际化语言
//     */
//    private Locale locale;

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 响应分类，与 ZKUnknownException 异常类型对应
     */
    int type;

    /**
     * 重载的构造方法，会同步自动设置 msg
     * 
     * @param code
     */
//    public ZKMsgRes(String code, Locale locale) {
//        this(code, null, locale, null);
//    }
//
//    public ZKMsgRes(String code, Locale locale, String msg) {
//        this(code, msg, locale, null);
//    }
//
//    public ZKMsgRes(String code, String msg, Locale locale, Object data) {
//        this(code, msg, locale, null, data);
//    }
    protected ZKMsgRes() {
    }

    protected ZKMsgRes(int type, Locale locale, String code, String msg, Object data, Object... msgArgs) {
        this.type = type;
//      this.locale = locale;
        this.code = code;
        this.data = data;
        if(ZKStringUtils.isEmpty(msg)) {
            this.msg = ZKMsgUtils.getMessage(locale, code, msgArgs);
        }else {
            this.msg = msg;
        }   
    }

//    public ZKMsgRes(ZKMsgException zkMsgE) {
//        this.code = zkMsgE.getCode();
//        this.msg = zkMsgE.getMessage();
//        this.data = zkMsgE.getData();
//    }

    // 返回一个成功，正常的响应数据
    public static ZKMsgRes asOk() {
        return new ZKMsgRes(KeyExceptionType.general, (Locale) null, "zk.0", null, null, (Object) null);
    }

    public static ZKMsgRes asOk(Object data) {
        return new ZKMsgRes(KeyExceptionType.general, (Locale) null, "zk.0", null, data, (Object) null);
    }

    public static ZKMsgRes as(String code) {
        return new ZKMsgRes(KeyExceptionType.general, (Locale) null, code, null, null, (Object) null);
    }

    public static ZKMsgRes as(String code, String msg) {
        return new ZKMsgRes(KeyExceptionType.general, (Locale) null, code, msg, null);
    }

    public static ZKMsgRes as(String code, String msg, Object data) {
        return new ZKMsgRes(KeyExceptionType.general, (Locale) null, code, msg, data);
    }

    public static ZKMsgRes as(String code, String msg, Object data, Object... msgArgs) {
        return new ZKMsgRes(KeyExceptionType.general, (Locale) null, code, msg, data, msgArgs);
    }

    public static ZKMsgRes as(int type, Locale locale, String code, String msg, Object data, Object... msgArgs) {
        return new ZKMsgRes(type, locale, code, msg, data, msgArgs);
    }

    public static ZKMsgRes as(ZKCodeException zkCodeE) {
        return new ZKMsgRes(zkCodeE.getType(), (Locale) null, zkCodeE.getCode(), null, zkCodeE.getData(),
                zkCodeE.getMsgArgs());
    }

    public static ZKMsgRes as(ZKMsgException zkMsgE) {
        return new ZKMsgRes(zkMsgE.getType(), (Locale) null, zkMsgE.getCode(), zkMsgE.getMessage(), zkMsgE.getData());
    }

    ////////////////////////////////////////

//    // java Bean 的方法
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    // java Bean 的方法
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }

//    /**
//     * @param locale
//     *            the locale to set
//     */
//    public void setLocale(Locale locale) {
//        this.locale = locale;
//    }


    // java Bean 的方法
    public void setData(Object data) {
        this.data = data;
    }

//    /**
//     * 重载人 setCode 可以自动设置 Msg;
//     *
//     * @Title: setCode
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date Aug 30, 2019 12:21:23 AM
//     * @param code
//     * @param msg
//     * @return void
//     */
//    public void setCode(String code, String msg) {
//        this.setCode(code, msg, null);
//    }
//
//    public void setCode(String code, String msg, Object[] msgArgs) {
//        this.setCode(code, msg, msgArgs, null);
//    }
//
//    public void setCode(String code, String msg, Object[] msgArgs, Object data) {
//
//        this.code = code;
//
//        if (ZKStringUtils.isEmpty(msg)) {
//            this.msg = ZKMsgUtils.getMessage(code, msgArgs);
//        }
//        else {
//            this.msg = msg;
//        }
//
//        if (data != null) {
//            this.data = data;
//        }
//    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    ////////////////////////////////////////
//    @JsonIgnore // 对象序列化时，即转 json 字符串时，忽略
    public boolean isOk() {
        return "zk.0".equals(this.getCode());
    }

    /**
     * @return errCode
     */
    public String getCode() {
        return code;
    }

    /**
     * @return msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @return type sa
     */
    public int getType() {
        return type;
    }

//    /**
//     * @return locale sa
//     */
//    @JsonIgnore
//    public Locale getLocale() {
//        return locale;
//    }

    @SuppressWarnings("unchecked")
    public <T> T getData() {
        if (this.data != null) {
            return (T) this.data;
        }
        return null;
    }

    @JsonIgnore
    public <T> T getDataByClass(Class<T> classz) {
        if (this.data != null) {
            return ZKJsonUtils.jsonStrToObject(this.getDataStr(), classz);
        }
        return null;
    }

    @JsonIgnore
    public String getDataStr() {
        if (this.data != null) {
            return ZKStringUtils.toString(this.data);
        }
        return null;
    }

    /**
     * 
     *
     * @Title: write
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 2, 2019 3:58:56 PM
     * @param res
     * @return void
     * @throws IOException
     */
    public void write(ServletResponse res) {
        PrintWriter pw = null;
        try {
            pw = res.getWriter();
            pw.write(this.toString());
        }
        catch(IOException e) {
            log.error("[>_<:20190902-1556-001] Response 通信异常！");
            e.printStackTrace();
            throw ZKExceptionsUtils.unchecked(e);
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    @JsonIgnore
    @Override
    public String toString() {
        return ZKJsonUtils.writeObjectJson(this);
    }

}
