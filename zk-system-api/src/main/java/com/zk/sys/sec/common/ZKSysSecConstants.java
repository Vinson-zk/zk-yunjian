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
* @Title: ZKSysSecConstants.java 
* @author Vinson 
* @Package com.zk.sys.sec.common
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 26, 2022 8:26:36 AM 
* @version V1.0 
*/
package com.zk.sys.sec.common;
/** 
* @ClassName: ZKSysSecConstants 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSysSecConstants {

    /**
     * 定义登录时与前端约定好的一些 key
     * 
     * @ClassName: KeyWebLogin
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public interface KeyWebLogin {
//        public static final String company = "company";
        public static final String user = "user";
    }

    public interface KeyAuth {
        public static final String adminAccount = "admin";

        public static final String adminRoleCode = "superAdmin";

        public static final String adminAuthCode = "admin";
    }

    // 权限配置项组别代码
    public static final String Key_Auth_Strategy = "Auth_Strategy";

    /**
     * 权限渠道枚举类型
     * 
     * @ClassName: ZKAuthChannel
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static enum ZKAuthChannel {

        // 通过部门获取的权限
        Dept("Auth_Dept"),
        // 用户直接获取的权限
        User("Auth_User"),
        // 通过角色获取的权限
        Role("Auth_Role"),
        // 通过职级获取的权限
        Rank("Auth_Rank"),
        // 通过用户类型获取的权限
        UserType("Auth_UserType");

        String channel;

        ZKAuthChannel(String channel) {
            this.channel = channel;
        }

        public String channel() {
            return this.channel;
        }

    }



}
