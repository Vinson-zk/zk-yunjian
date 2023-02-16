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
* @Title: ZKSecRememberMeToken.java 
* @author Vinson 
* @Package com.zk.security.token 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 12:58:15 PM 
* @version V1.0 
*/
package com.zk.security.token;
/** 
* @ClassName: ZKSecRememberMeToken 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecRememberMeToken {

    /**
     * 是否记住我，是，token 生成加密字符写入Cookie 或请求头中，通 rememberMeManager 实现
     */
    boolean isRememberMe();

}
