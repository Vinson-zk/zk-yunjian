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
 * @Title: ZKMsgUtils.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 4:14:12 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.util.Locale;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.context.MessageSource;

import com.zk.core.web.utils.ZKWebUtils;

/** 
* @ClassName: ZKMsgUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMsgUtils {

    /**
     * 日志对象
     */
    protected static Logger logger = LogManager.getLogger(ZKMsgUtils.class);

    private static MessageSource messageSource = null;

    /**
     * @return messageSource
     */
    public static MessageSource getMessageSource() {
        if (messageSource == null) {
            messageSource = ZKEnvironmentUtils.getApplicationContext().getBean(MessageSource.class);
        }
        return messageSource;
    }

    /**
     * @param messageSource
     *            the messageSource to set
     */
    public static void setMessageSource(MessageSource messageSource) {
        ZKMsgUtils.messageSource = messageSource;
    }

//    /**
//     * 使用 getMessage(String code, Object[] args, Locale locale)
//     *
//     * @Title: getMessage
//     * @Description: TODO(simple description this method what tos do.)
//     * @author Vinson
//     * @date Jan 29, 2023 2:57:04 PM
//     * @param code
//     * @param args
//     * @return
//     * @return String
//     */
////    @Deprecated
//    public static String getMessage(String code, Object... args) {
//        return getMessage(code, args, ZKLocaleUtils.getDefautLocale());
//    }

//    public static String getMessage(String code, Object... args) {
//        return getMessage(null, code, args);
//    }

    /**
     * 取国际化信息
     * 
     * @param locale
     *            语言
     * 
     * @param code
     *            代码
     * @param args
     *            替换参数
     * @return
     */
    public static String getMessage(Locale locale, String code, Object... args) {
        try {
            locale = locale == null ? getLocale() : locale;
            return getMessageSource().getMessage(code, args, code, locale);
        }
        catch(Exception e) {
            logger.error("[>_<:20190829-1740-001] 国际化失败！code:{}, locale:{}", code, locale);
            e.printStackTrace();
        }
        return null;
    }

    public static String getMessage(String code, Object... args) {
        return getMessage(null, code, args);
    }

    protected static Locale getLocale() {
        return ZKWebUtils.getLocale();
    }

}
