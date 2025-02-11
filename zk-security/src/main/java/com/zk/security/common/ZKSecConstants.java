/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSecConstants.java 
* @author Vinson 
* @Package com.zk.security.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 5, 2021 8:42:04 PM 
* @version V1.0 
*/
package com.zk.security.common;
/** 
* @ClassName: ZKSecConstants 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecConstants {

    /**
     * 参数名
     */
    public static interface PARAM_NAME {
        /**
         * 请求登录用户所在公司码
         */
        public static final String CompanyCode = "companyCode";

        /**
         * 请求登录用户名参数名
         */
        public static final String Username = "username";

        /**
         * 请求登录密码参数名
         */
        public static final String Pwd = "pwd";

        /**
         * 请求登录记录我参数名
         */
        public static final String RememberMe = "rememberMe";

        /**
         * 请求登录验证码参数名
         */
        public static final String Captcha = "captcha";

        /**
         * 验证码跟踪ID
         */
        public static final String CaptchaId = "captchaId";

        /**
         * 请求设备 操作系统类型 标识参数名;
         * 
         * 0-未知的 设备类型；1-iOS 设备；2-Android 设备；3-WINDOWS 设备；4-MAC 设备；
         */
        public static final String OsType = "osType";

        /**
         * 请求设备UDID参数名
         */
        public static final String DriverUdid = "driverUdid";

        /**
         * 请求应用类型；
         * 
         * 0-未知；1-一般 web 应用；2-终端应用；3-微信公众号；4-微信小程序
         */
        public static final String AppType = "AppType";

        /**
         * 请求应用 ID
         */
        public static final String appId = "appId";

        /**
         * 请求令牌参数名，与响应头相同
         */
        public static final String TicketId = "__tk"; //

        /**
         * 第三方应用 Id
         */
        public static final String DevId = "devId";

        /**
         * 第三方应用 secretKey
         */
        public static final String SecretKey = "secretKey";

        /**
         * 第三方标识
         */
        public static final String thirdPartyId = "thirdPartyId";

        /**
         * 请求摘要标记参数名或请求头名
         */
        public static final String Digest = "digest";

        /**
         * 默认 cookie 名称 key
         */
        public static final String DefaultCookieNameKey = "SEC_COOKIE";

        /**
         * 用户ID
         */
        public static final String UserId = "userId";
    }

    /**
     * 权限验证异常 key
     */
    public static interface SEC_KEY {
        /**
         * Request 异常 key
         */
        public static final String SecException = "Sec_Exception";

        /**
         * 请求是否包含令牌ID
         */
        public static final String SecIsHaveTicket = "Sec_IsHaveTicket";

    }

}
