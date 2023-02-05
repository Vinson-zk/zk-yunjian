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
* @Title: ZKSecSecurityManager.java 
* @author Vinson 
* @Package com.zk.security.mgt 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 8, 2021 6:29:17 PM 
* @version V1.0 
*/
package com.zk.security.mgt;

import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.rememberMe.ZKSecRememberMeManager;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.ticket.ZKSecTicketManager;
import com.zk.security.token.ZKSecAuthenticationToken;

/** 
* @ClassName: ZKSecSecurityManager 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecSecurityManager {

    /**
     * 取权限管理中设置的令牌管理
     * 
     * @return
     */
    ZKSecTicketManager getTicketManager();

    /**
     * 记住我管理
     * 
     * @return
     */
    ZKSecRememberMeManager getRememberMeManager();

    /**
     * 创建主体
     * 
     * @return
     */
    ZKSecSubject createSubject();

    /**
     * 主体创建令牌时触发
     * 
     * @param ticket
     *            要设置的令牌
     * @param toSubject
     *            主体
     */
    void onSetTicketToSubject(ZKSecTicket ticket, ZKSecSubject toSubject);

    /**
     * 登录认证
     * 
     * @param subject
     *            主体
     * @param token
     *            token
     * @return true-登录成功；false-登录失败；
     */
    <ID> boolean login(ZKSecSubject subject, ZKSecAuthenticationToken token);

    /**
     * 登出
     * 
     * @param subject
     *            主体
     * @return
     */
    void logOut(ZKSecSubject subject);

    /**
     * 用户在线数量控制策略
     */
    <ID> void doPrincipalsCount(ZKSecPrincipalCollection<ID> pc);

    /**
     * 对 api 接口代码对待鉴权
     *
     * @Title: checkApiCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 8, 2021 6:26:33 PM
     * @param pc
     * @param apiCode
     * @return boolean 有权限返回 true; 没有返回 false;
     */
    <ID> boolean checkApiCode(ZKSecPrincipalCollection<ID> pc, String apiCode);

    /**
     * 鉴权-权限代码
     * 
     * @param pc
     * @param permissionCode
     *            权限代码
     * @return true-鉴权成功；反之鉴权失败
     */
    <ID> boolean checkPermission(ZKSecPrincipalCollection<ID> pc, String permissionCode);

}
