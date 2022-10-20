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
* @Title: ZKSecAuthcUserToken.java 
* @author Vinson 
* @Package com.zk.security.token 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 12:59:30 PM 
* @version V1.0 
*/
package com.zk.security.token;
/** 
* @ClassName: ZKSecAuthcUserToken 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecAuthcUserToken extends ZKSecAuthenticationToken, ZKSecRememberMeToken {

    /**
     * 登录名
     */
    public String getUsername();

    /**
     * 用户密码
     */
    public String getPwd();

}
