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
* @Title: ZKSecDefaultAuthcUserToken.java 
* @author Vinson 
* @Package com.zk.security.token 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 1:12:44 PM 
* @version V1.0 
*/
package com.zk.security.token;
/** 
* @ClassName: ZKSecDefaultAuthcUserToken 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecDefaultAuthcUserToken extends ZKSecAbstractAuthenticationToken implements ZKSecAuthcUserToken {

    /**
     * 
     */
    private static final long serialVersionUID = 2810412149427606436L;

    // 公司代码
    private String companyCode;

    // 登录名
    private String username;

    // 用户密码
    private String pwd;

    // 是否记住我，是，token 生成加密字符写入Cookie中
    private boolean rememberMe;

    public ZKSecDefaultAuthcUserToken(String companyCode, String username, String pwd, boolean rememberMe, long osType,
            String udid, long appType, String appId) {
        super(companyCode, osType, udid, appType, appId);

        this.companyCode = companyCode;
        this.username = username;
        this.pwd = pwd;
        this.rememberMe = rememberMe;
    }

    /**
     * 组织代码
     */
    @Override
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * 登录名
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 用户密码
     */
    @Override
    public String getPwd() {
        return pwd;
    }

    /**
     * 是否记住我，是，token 生成加密字符写入Cookie中
     */
    @Override
    public boolean isRememberMe() {
        return rememberMe;
    }

}
