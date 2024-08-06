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
 * @Title: ZKCoreConstants.java 
 * @author Vinson 
 * @Package com.zk.core.commons 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 10:13:49 AM 
 * @version V1.0   
*/
package com.zk.core.commons;

import java.nio.charset.Charset;

/** 
* @ClassName: ZKCoreConstants 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKCoreConstants {

    public static interface System {

        public static final String userName = "user.name";

    }

    public static interface Consts {

        public static final Charset UTF_8 = Charset.forName("UTF-8");

        public static final Charset ASCII = Charset.forName("US-ASCII");

        public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

    }

    /**
     * 一些全局属性名
     * 
     * @ClassName: Global
     * @Description: TODO(simple description this class what to do.)
     * @author Vinson
     * @version 1.0
     */
    public static interface Global {

        /**
         * 请求标记
         */
        public static final String requestSign = "_zk_req_sign_";

//        /**
//         * 是否是重复提交
//         */
//        public static final String isRepeatSubmit = "_zk_is_repeat_submit_";

        /**
         * 是否强制提交，忽略重复提交
         */
        public static final String isForceSubmit = "_zk_is_force_submit_";

        /**
         * 默认平台代码；数据一般分公司数据和平台数据；集团代码、公司代码为默认平台代码表示为平台数据；
         */
        public static final String default_platform_code = "_default_platform_code_";
    }

    // 数据内容校验正则表达式
    public static interface ValidationRegexp {
        /**
         * 编码
         */
        public static final String code = "^[a-zA-Z0-9_.-]*$";

        /**
         * 账号
         */
        public static final String account = "^[a-zA-Z_][a-z0-9A-Z_]*$";

        /**
         * 中国手机号：1开头 加 一个非0/1/2的数字 加 8位数字
         */
        public static final String phoneNum = "^1[3-9]\\d{9}$";

        /**
         * 国际通用手机号：+开头 加 1-4位国际区号 加 中杠 加 7-11位手机号
         */
        public static final String intl_phoneNum = "^\\+[1-9]\\d{1,3}-\\d{7,11}$";

        /**
         * 国际通用手机号或中国手机号：+开头 加 1-4位国际区号 加 中杠 加 7-11位手机号，1开头 加 一个非0/1/2的数字 加 8位数字
         */
        public static final String all_phoneNum = "(^\\+[1-9]\\d{1,3}-\\d{7,11}$)|(^1[3-9]\\d{9}$)";

        /**
         * 邮箱
         */
        public static final String mail = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+\\.)+[a-zA-Z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]{2,}))$";
    }

}
