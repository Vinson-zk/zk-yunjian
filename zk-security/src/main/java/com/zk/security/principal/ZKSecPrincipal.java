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
* @Title: ZKSecPrincipal.java 
* @author Vinson 
* @Package com.zk.security.principal 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 14, 2021 6:52:19 PM 
* @version V1.0 
*/
package com.zk.security.principal;

import java.io.Serializable;

/** 
* @ClassName: ZKSecPrincipal 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecPrincipal<ID> extends Serializable {

    /**
     * 身份类型
     * 
     * @author bs
     *
     */
    public static interface KeyType {
        /**
         * 用户
         */
        public static final int User = 0;

        /**
         * 第三方开发者应用
         */
        public static final int Developer = 2;

        /**
         * 分布式微服务
         */
        public static final int Distributed_server = 3;

    }

    /**
     * 设备 操作系统类型
     * 
     * 0-未知的 设备类型；1-iOS 设备；2-Android 设备；3-WINDOWS 设备；4-MAC 设备；
     * 
     * @ClassName: OS_TYPE
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface OS_TYPE {

        /**
         * 未知的 设备类型
         */
        public static final int UNKNOWN = 0;

        /**
         * iOS 设备
         */
        public static final int iOS = 1;

        /**
         * Android 设备
         */
        public static final int Android = 2;

        /**
         * WINDOWS 设备
         */
        public static final int WINDOWS = 3;

        /**
         * MAC 设备
         */
        public static final int OS = 4;

    }

    /**
     * 应用类型；
     * 
     * 0-未知；1-一般 web 应用；2-终端应用；3-微信公众号；4-微信小程序
     * 
     * @ClassName: APP_TYPE
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface APP_TYPE {

        /**
         * 未知的 设备类型
         */
        public static final int UNKNOWN = 0;

        /**
         * 一般 web 应用
         */
        public static final int web = 1;

        /**
         * 终端应用
         */
        public static final int app = 2;

        /**
         * 微信公众号
         */
        public static final int WECHAT_OFFICIAL_ACCOUNTS = 3;

        /**
         * 微信小程序
         */
        public static final int WECHAT_MINI_ACCOUNTS = 3;

    }

    /**
     * 令牌 ID
     *
     * @Title: getTicketId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 13, 2022 5:34:47 PM
     * @return
     * @return Serializable
     */
    public Serializable getTicketId();

    /**
     * 类型
     * 
     * @return
     */
    public int getType();

    /**
     * 唯一标识, 与用户表ID应该对应
     * 
     * @return
     */
    public ID getPkId();

    /**
     * 是否为主要的身份
     */
    public boolean isPrimary();

    /**
     * 终端设备类型
     * 
     * @return
     */
    public long getOsType();

    /**
     * 设备UDID
     * 
     * @return
     */
    public String getUdid();

    /**
     * 应用类型
     */
    public long getAppType();

    /**
     * 应该唯一标识
     */
    public String getAppId();

}
