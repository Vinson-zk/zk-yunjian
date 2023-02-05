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
* @Title: ZKSecSubject.java 
* @author Vinson 
* @Package com.zk.security.sec.subject 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 8, 2021 6:28:05 PM 
* @version V1.0 
*/
package com.zk.security.subject;

import java.util.concurrent.Callable;

import com.zk.security.annotation.ZKSecApiCode;
import com.zk.security.mgt.ZKSecSecurityManager;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.token.ZKSecAuthenticationToken;

/** 
* @ClassName: ZKSecSubject 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecSubject {

    /**
     * 对 api 接口代码对待鉴权
     *
     * @Title: checkApiCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 8, 2021 6:26:33 PM
     * @param apiCode
     * @return boolean 有权限返回 true; 没有返回 false;
     */
    boolean checkApiCode(ZKSecApiCode apiCode);

    boolean checkApiCode(String apiCode);

    /**
     * 鉴权-权限代码
     * 
     * @param permissionCode
     *            权限代码
     * @return true-鉴权成功；反之鉴权失败
     */
    boolean checkPermission(String permissionCode);

    ZKSecSecurityManager getSecurityManager();

    /**
     * 取当前登录用户所有身份
     */
    <ID> ZKSecPrincipalCollection<ID> getPrincipalCollection();

    /**
     * 取主体对应令牌
     * 
     * 不存在时，不创建；
     * 
     * @return
     */
    ZKSecTicket getTicket();

    /**
     * 取主体对应令牌
     * 
     * @param isCreate
     *            令牌不存在时，是否创建？true-创建；false-不创建;
     * @return
     */
    ZKSecTicket getTicket(boolean isCreate);

    /**
     * 判断是否有用户身份
     * 
     * @return true-有；false-没有
     */
    boolean isAuthcUser();

    /**
     * 判断是否有 dev 开发者 身份
     * 
     * @return true-有；false-没有
     */
    boolean isAuthcDev();

    /**
     * 判断是不是微服务身份
     *
     * @Title: isAuthcServer
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 14, 2022 8:46:10 AM
     * @return
     * @return boolean
     */
    boolean isAuthcServer();

//  /**
//   * 设置令牌，传入令牌
//   * @param identification
//   * @return
//   */
//  public void setTicket(Ticket ticket);

    /**
     * 执行线程
     * 
     * @param callable
     * @return
     * @throws Throwable
     */
    <V> V execute(Callable<V> callable) throws Exception;

    /**
     * 登录
     * 
     * @param authenticationToken
     */
    void login(ZKSecAuthenticationToken authenticationToken);

    /**
     * 登出
     */
    void logout();

    /**
     * 是否已进行登录认证；
     * 
     * @return true-已进行登录认证；false-未进行登录认证；
     */
    boolean isAuthenticated();

}
