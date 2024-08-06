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
* @Title: ShiroInit.java 
* @author Vinson 
* @Package com.zk.demo.shiro 
* @Description: TODO(simple description this file what to do. ) 
* @date May 21, 2021 8:56:54 AM 
* @version V1.0 
*/
package com.zk.demo.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.ini.IniSecurityManagerFactory;
import org.apache.shiro.lang.util.Factory;
import org.apache.shiro.subject.Subject;

/** 
* @ClassName: ShiroInit 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@SuppressWarnings("deprecation")
public class ShiroInit {

    private static String config;

    public Subject subject = null;

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public ShiroInit(String config) {
        ShiroInit.config = config;
    }

    public boolean login(String userName, String password) {

        if (subject != null) {
            this.logout();
        }
        else {
            // 1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
            Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(config);
            // 2、得到SecurityManager实例 并绑定给SecurityUtils
            org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
            SecurityUtils.setSecurityManager(securityManager);
            // 3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
            subject = SecurityUtils.getSubject();
        }

        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);

        try {
            // 4、登录，即身份验证
            subject.login(token);
        }
        catch(AuthenticationException e) {
            // 5、身份验证失败
            e.printStackTrace();
            return false;
        }
        return subject.isAuthenticated(); // 返回登录结果
    }

    public boolean logout() {
        // 6、退出
        subject.logout();
        return !subject.isAuthenticated(); // 返回退出登录结果
    }

}
