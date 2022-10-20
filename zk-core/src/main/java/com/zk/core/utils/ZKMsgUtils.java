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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    protected static Logger logger = LoggerFactory.getLogger(ZKMsgUtils.class);

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

    public static String getMessage(String code, Object... args) {
        return getMessage(code, args, ZKWebUtils.getLocale());
    }

    /**
     * 取国际化信息
     * 
     * @param code
     *            代码
     * @param args
     *            替换参数
     * @param locale
     *            语言
     * @return
     */
    public static String getMessage(String code, Object[] args, Locale locale) {
        try {
            return getMessageSource().getMessage(code, args, code, locale);
        }
        catch(Exception e) {
            logger.error("[>_<:20190829-1740-001] 国际化失败！code:{}, locale:{}", code, locale);
            e.printStackTrace();
        }
        return null;
    }

}
