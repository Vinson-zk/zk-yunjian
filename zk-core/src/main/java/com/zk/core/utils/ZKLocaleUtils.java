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
 * @Title: ZKLocaleUtils.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 4:12:13 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** 
* @ClassName: ZKLocaleUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKLocaleUtils {

    protected static Logger log = LogManager.getLogger(ZKLocaleUtils.class);

    /**
     * 根据字符串转化为 Locale 对象；
     *
     * @Title: valueOf
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 14, 2019 10:54:57 AM
     * @param localeStr
     * @return
     * @return Locale
     */
    public static Locale valueOf(String localeStr) {

        Locale locale = null;
        try {
            localeStr = localeStr.replaceAll("-", "_");
            String[] ls = localeStr.split("_");
            if (ls.length > 1) {
//                locale = new Locale(ls[0], ls[1]);
                locale = Locale.of(ls[0], ls[1]);
            }
            else {
//                locale = new Locale(localeStr);
                locale = Locale.of(localeStr);
            }
        }
        catch(Exception e) {
//            locale = Locale.getDefault() == null ? new Locale("zh", "CN") : Locale.getDefault();
            locale = Locale.getDefault() == null ? Locale.of("zh", "CN") : Locale.getDefault();
            log.error("[>_<:20190308-1731-001] 转换语言 localeStr:[{}] 失败。返回系统默认语言:[{}]", localeStr,
                    locale.toLanguageTag());
            e.printStackTrace();
        }

        return locale;
    }

    /**
     * 各类语言符号转化为统一的语言
     *
     * @Title: distributeLocale
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 4, 2020 2:08:48 PM
     * @param localeStr
     * @return
     * @return Locale
     */
    public static Locale distributeLocale(String localeStr) {
        Locale locale = null;
        switch (localeStr) {
            case "en":
            case "en_US":
                localeStr = "en_US";
                locale = valueOf(localeStr);
                break;
            case "zh_Hans":
            case "zh_CN":
                localeStr = "zh_CN";
                locale = valueOf(localeStr);
                break;
            case "zh_Hant":
            case "zh_TW":
                localeStr = "zh_TW";
                locale = valueOf(localeStr);
                break;
            default:
//                locale = Locale.getDefault() == null ? new Locale("zh", "CN") : Locale.getDefault();
                locale = null;
                log.error("[>_<:20190314-0856-001] 暂未支持此语言，使用默认语言：localeStr:[{}]", localeStr);
                break;
        }
        return locale;
    }

    /**
     * 无上下文环境，上下文环境请使用 ZKWebUtils
     * 
     * 取系统默认语言
     *
     * @Title: getDefautLocale
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 3, 2019 5:48:41 PM
     * @return
     * @return Locale
     */
    public static Locale getDefautLocale() {
        return Locale.getDefault();
    }

    public static Locale getLocale() {
        Locale locale = null;
        return locale == null ? getDefautLocale() : locale;
    }

    /**
     * 无上下文环境; 有上下文环境请使用 ZKWebUtils
     * 
     * 设置整个系统的默认语言
     *
     * @Title: setSystemLocale
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 14, 2019 5:19:02 PM
     * @param locale
     * @return void
     */
    public static void setLocale(Locale locale) {
        Locale.setDefault(locale);
    }


}
