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
* @Title: ZKWXConstants.java 
* @author Vinson 
* @Package com.zk.wechat.wx 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 10, 2021 4:11:31 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.common;
/** 
* @ClassName: ZKWXConstants 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKWXConstants {
    
    /**
     * 缓存 KEY 组合时 分隔符
     */
    public static final String cacheKeySeparator = "_";

    /**
     * 错误码 转换前缀
     */
    public static final String errCodePrefix = "wx";

    /**
     * 没有第三方平台时，填充第三方平台账号；一般公众号自己后台实现时，用到
     */
    public static final String noThirdParty = "";

    /**
     * 语言标识，国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语，默认为zh-CN
     * 
     * @ClassName: KeyLocale
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface KeyLocale {
        /**
         * zh_CN 简体
         */
        public static final String zh_CN = "zh_CN";

        /**
         * zh_TW 繁体
         */
        public static final String zh_TW = "zh_TW";

        /**
         * en 英语
         */
        public static final String en = "en";
    }

}
