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
* @Title: ZKSysConstants.java 
* @author Vinson 
* @Package com.zk.sys.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 17, 2024 9:16:28 AM 
* @version V1.0 
*/
package com.zk.sys.common;

import java.util.regex.Pattern;

import com.zk.core.commons.ZKCoreConstants.ValidationRegexp;

/** 
* @ClassName: ZKSysConstants 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSysConstants {

    // 数据内容校验正则表达式
    public static interface ValidationPattern {
//        /**
//         * 编码
//         */
//        public static final String code = "^[a-zA-Z0-9_.-]*$";

        /**
         * 账号
         */
        public static final Pattern patternAccount = Pattern.compile(ValidationRegexp.account);

        /**
         * 中国手机号：1开头 加 一个非0/1/2的数字 加 8位数字
         */
        public static final Pattern patternPhoneNum = Pattern.compile(ValidationRegexp.phoneNum);

        /**
         * 国际通用手机号：+开头 加 1-4位国际区号 加 中杠 加 7-11位手机号
         */
        public static final Pattern patternIntlPhoneNum = Pattern.compile(ValidationRegexp.intl_phoneNum);

        /**
         * 国际通用手机号或中国手机号：+开头 加 1-4位国际区号 加 中杠 加 7-11位手机号，1开头 加 一个非0/1/2的数字 加 8位数字
         */
        public static final Pattern patternAllPhoneNum = Pattern.compile(ValidationRegexp.all_phoneNum);

        /**
         * 邮箱
         */
        public static final Pattern patternMail = Pattern.compile(ValidationRegexp.mail);
    }

}
