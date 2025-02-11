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

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKSecAuthenticationException;
import com.zk.core.exception.ZKSecAuthorizationException;
import com.zk.core.exception.ZKSystemException;
import com.zk.core.exception.ZKValidatorException;
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
    protected static Logger log = LogManager.getLogger(ZKMsgRes.class);

    /**
     * 响应错误码，zk.0 成功
     */
    private String code;

    /**
     * 响应错误码，对应的国际化信息
     */
    private String msg;

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 响应码分类
     */
    int type;

    // 响应码分类 定义
    public static interface ResCodeType {
        public static final int general = 0; // 正常响应; 正常业务处理，可根据场景提示成功或不提示

        public static final int system = 1; // 系统异常，配置或代码运行错误; 提示用户找系统管理员解决

        public static final int busniness = 2; // 业务异常，不满足业务的条件; 根据国际化信息提示用户

        public static final int dataValidator = 3; // 数据验证异常，输入参数不格式不正确; 提示用户具体的校验不通过的字段及原因

        public static final int secAuthentication = 4; // 身份认证异常;

        public static final int secAuthorization = 5; // 无访问权限；
    }

    protected ZKMsgRes(int type, String code, String msg, Object data) {
        this.type = type;
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    protected ZKMsgRes() {

    }

    // -------------------------------------------------------------------
    // 返回一个成功，正常的响应数据
    public static ZKMsgRes asOk() {
        return asOk(null);
    }

    public static ZKMsgRes asOk(Object data) {
        return asOk(null, data);
    }

    public static ZKMsgRes asOk(Locale locale, Object data) {
        return new ZKMsgRes(ResCodeType.general, "zk.0", ZKMsgUtils.getMessage(locale, "zk.0"), data);
    }

    public static ZKMsgRes asSysErr() {
        return asSysErr(null);
    }

    public static ZKMsgRes asSysErr(Object data) {
        return asSysErr(null, data);
    }

    public static ZKMsgRes asSysErr(Locale locale, Object data) {
        return new ZKMsgRes(ResCodeType.system, "zk.1", ZKMsgUtils.getMessage(locale, "zk.1"), data);
    }

    // -------------------------------------------------------------------
    public static ZKMsgRes asCode(String code) {
        return asCode(null, code);
    }

    public static ZKMsgRes asCode(Locale locale, String code) {
        return asCode(locale, ResCodeType.system, code);
    }

    public static ZKMsgRes asCode(Locale locale, int resCodeType, String code) {
        return asCode(locale, ResCodeType.system, code, null);
    }

    public static ZKMsgRes asCode(Locale locale, int resCodeType, String code, Object data, Object... msgArgs) {
        return new ZKMsgRes(resCodeType, code, ZKMsgUtils.getMessage(locale, code, msgArgs), data);
    }

    // -------------------------------------------------------------------
    public static ZKMsgRes as(Locale locale, ZKBusinessException be) {
        return new ZKMsgRes(ResCodeType.busniness, be.getCode(), be.getMsgByLocale(locale), be.getData());
    }

    public static ZKMsgRes as(Locale locale, ZKValidatorException ve) {
        return new ZKMsgRes(ResCodeType.dataValidator, "zk.000002",
                ZKMsgUtils.getMessage(locale, "zk.000002", (Object[]) null),
                ve.getMessagePropertyAndMessageAsMap());
    }

    public static ZKMsgRes as(Locale locale, ZKSystemException se) {
        return new ZKMsgRes(ResCodeType.dataValidator, se.getCode(), se.getMsgByLocale(locale), se.getData());
    }

    public static ZKMsgRes as(Locale locale, ZKSecAuthenticationException sae) {
        return new ZKMsgRes(ResCodeType.secAuthentication, sae.getCode(), sae.getMsgByLocale(locale), sae.getData());
    }

    public static ZKMsgRes as(Locale locale, ZKSecAuthorizationException sae) {
        return new ZKMsgRes(ResCodeType.secAuthorization, sae.getCode(), sae.getMsgByLocale(locale), sae.getData());
    }

    /**
     * 将异常转为 ZKMsgRes
     */
    public static ZKMsgRes as(Locale locale, Throwable e) {
        // 数据验证异常：com.zk.utils.exception.ZKValidatorExceptions
        if (e instanceof ZKValidatorException) {
            // 数据验证异常
            ZKValidatorException ve = (ZKValidatorException) e;
            if (log.isInfoEnabled()) {
                e.printStackTrace();
            }
            return ZKMsgRes.as(locale, ve);
        }
        else if (e instanceof ZKBusinessException) {
            // 根据异常码，国际化异常信息
            ZKBusinessException be = (ZKBusinessException) e;
            if (log.isInfoEnabled()) {
                e.printStackTrace();
            }
            return ZKMsgRes.as(locale, be);
        }
        else if (e instanceof ZKSystemException) {
            ZKSystemException se = (ZKSystemException) e;
            e.printStackTrace();
            return ZKMsgRes.asSysErr(se.getData());
        }
        else if (e instanceof ZKSecAuthenticationException) {
            ZKSecAuthenticationException sae = (ZKSecAuthenticationException) e;
            if (log.isInfoEnabled()) {
                e.printStackTrace();
            }
            return ZKMsgRes.as(locale, sae);
        }
        else if (e instanceof ZKSecAuthorizationException) {
            ZKSecAuthorizationException sae = (ZKSecAuthorizationException) e;
            if (log.isInfoEnabled()) {
                e.printStackTrace();
            }
            return ZKMsgRes.as(locale, sae);
        }
//        else if (e instanceof ServletException) {
//            if (e.getCause() != null) {
//                return as(locale, e.getCause());
//            }
//            e.printStackTrace();
//            return ZKMsgRes.as(locale, "zk.1", e.getMessage());
//        }
        else {
            if (e.getCause() != null) {
                return as(locale, e.getCause());
            }
            e.printStackTrace();
//            log.error("[>_<:20230214-2348-002] ZKExceptionsUtils.as -> exception class:{}", e.getClass().getName());
            e.printStackTrace();
            return ZKMsgRes.asSysErr(locale, e.getMessage());
        }
    }

    // ================================================================

    // java Bean 的方法
    public void setData(Object data) {
        this.data = data;
    }

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
            return ZKJsonUtils.parseObject(this.getDataStr(), classz);
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

    @JsonIgnore
    @Override
    public String toString() {
        return ZKJsonUtils.toJsonStr(this);
    }

}
