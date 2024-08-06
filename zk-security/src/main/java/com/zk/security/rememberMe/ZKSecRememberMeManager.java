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
* @Title: ZKSecRememberMeManager.java 
* @author Vinson 
* @Package com.zk.security.rememberMe 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 6:50:36 PM 
* @version V1.0 
*/
package com.zk.security.rememberMe;

import com.zk.core.exception.base.ZKCodeException;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.token.ZKSecAuthenticationToken;

/** 
* @ClassName: ZKSecRememberMeManager 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecRememberMeManager {

    // 记住我
    // 忘记我
    /**
     * 退出登录记住我处理
     * 
     * @param subject
     */
    void onLogout(ZKSecSubject subject);

    /**
     * 成功登录记住我处理
     * 
     * @param subject
     * @param rememberMeToken
     * @param pc
     */
    <ID> void onSuccessfulLogin(ZKSecSubject subject, ZKSecAuthenticationToken token, ZKSecPrincipalCollection<ID> pc);

    /**
     * 登录失败记住我处理
     * 
     * @param subject
     * @param rememberMeToken
     * @param ae
     */
    void onFailedLogin(ZKSecSubject subject, ZKSecAuthenticationToken token, ZKCodeException se);

    /**
     * 取记住我身份
     * 
     * @param subject
     * @return
     */
    <ID> ZKSecPrincipalCollection<ID> getRememberedPrincipals(ZKSecSubject subject);

    /**
     * 是否是记住我
     * 
     * @param rememberMeToken
     * @return
     */
    boolean isRememberMe(ZKSecAuthenticationToken token);

}
