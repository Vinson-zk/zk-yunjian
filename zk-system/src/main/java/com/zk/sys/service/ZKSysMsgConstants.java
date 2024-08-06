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
* @Title: ZKSysMsgConstants.java 
* @author Vinson 
* @Package com.zk.sys.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 6, 2024 3:17:51 PM 
* @version V1.0 
*/
package com.zk.sys.service;
/** 
* @ClassName: ZKSysMsgConstants 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSysMsgConstants {

    public static interface ZKCodeType {

        // 注册公司
        public static final String registerCompany = "registerCompany";

        // 注册用户
        public static final String registerUser = "registerUser";

        // 重置密码
        public static final String restPassword = "restPassword";

        // 修改邮箱
        public static final String changeMail = "changeMail";

        // 修改手机号码
        public static final String changePhoneNum = "changePhoneNum";

    }

}
