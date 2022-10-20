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
 * @Title: ZKSerCenConstants.java 
 * @author Vinson 
 * @Package com.zk.server.central.commons 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:08:43 PM 
 * @version V1.0   
*/
package com.zk.server.central.commons;

/** 
* @ClassName: ZKSerCenConstants 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSerCenConstants {

    public static interface ParamKey {

        /**
         * 验证码参数名
         */
        public final static String param_captcha = "captcha";

        /**
         * 是否需要验证码，对应 key
         */
        public final static String param_isCaptcha = "isCaptcha";

        /**
         * 指定跳转 URL 的参数
         */
        public final static String __url = "__url";

        /**
         * 用户 SecretKey 一些接口中的特殊字段加密处理；如用户登录时的用户名与密码
         */
        public final static String userSecretKey = "_userSecretKey";

    }

    public static interface captchaKey {

        /**
         * 请求失败次数KEY
         */
        public static final String KEY_LOGIN_FAIL_NUM = "_KEY_LOGIN_FAIL_NUM_";

        /**
         * 验证码名 KEY
         */
        public static final String KEY_CAPTCHA = "_KEY_CAPTCHA";

        /**
         * 验证码有效期 KEY
         */
        public static final String KEY_CAPTCHA_VALIDITY_DATE = "KEY_CAPTCHA_VALIDITY_DATE";

    }

}
