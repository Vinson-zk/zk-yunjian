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
* @Title: ZKApiConstants.java 
* @author Vinson 
* @Package com.zk.framework.common
* @Description: TODO(simple description this file what to do. ) 
* @date May 27, 2022 11:30:07 AM 
* @version V1.0 
*/
package com.zk.framework.common;
/** 
* @ClassName: ZKApiConstants 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKApiConstants {

    /**
     * 在服务注册中应用名称及请求路径前缀
     * 
     * @ClassName: YunJian_App
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface YunJian_App {

        /**
         * 文件服务
         */
        public static interface file {
            /**
             * 名称
             */
            public static final String name = "yunjian.zk.file";

            /**
             * api 前缀
             */
            public static final String apiPrefix = "/zk/f/v1.0";
        }

        /**
         * 邮件发送服务
         */
        public static interface mail {
            /**
             * 名称
             */
            public static final String name = "yunjian.zk.mail";

            /**
             * api 前缀
             */
            public static final String apiPrefix = "/zk/mail/v1.0";
        }

        /**
         * 微信平台
         */
        public static interface wechat {
            /**
             * 名称
             */
            public static final String name = "yunjian.zk.wechat";

            /**
             * api 前缀
             */
            public static final String apiPrefix = "/zk/wechat/v1.0";
        }

    }

}
