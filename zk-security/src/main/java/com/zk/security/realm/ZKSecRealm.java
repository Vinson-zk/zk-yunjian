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
* @Title: ZKSecRealm.java 
* @author Vinson 
* @Package com.zk.security.realm 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 11:34:12 PM 
* @version V1.0 
*/
package com.zk.security.realm;

import com.zk.security.authz.ZKSecAuthorizationInfo;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.token.ZKSecAuthenticationToken;

/** 
* @ClassName: ZKSecRealm 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecRealm {

    /**
     * 认证域名称
     * 
     * @return
     */
    public String getRealmName();

    /**
     * 支持认证的 token
     * 
     * @param authcToken
     * @return
     */
    boolean supports(ZKSecAuthenticationToken authcToken);

//  /**
//   * 支持鉴权的身份
//   * @param authcToken
//   * @return
//   */
//  boolean supports(ZKSecPrincipal<?> principal);

    /**
     * 认证，登录
     * 
     * @param token
     * @return
     */
    <ID> ZKSecPrincipalCollection<ID> authentication(ZKSecAuthenticationToken authcToken);

    /**
     * 鉴权，授权
     * 
     * @param principalCollection
     *            身份
     * @param permissionCode
     *            权限代码
     * @return
     */
    <ID> boolean checkPermission(ZKSecPrincipalCollection<ID> principalCollection, String permissionCode);

    /**
     * 鉴定  api接口权限 代码
     * 
     * @param pc
     * @param apiCode
     *             api接口权限 代码
     * @return true-鉴定成功；反之鉴定失败
     */
    <ID> boolean checkApiCode(ZKSecPrincipalCollection<ID> principalCollection, String apiCode);

    /**
     * 身份在线数量控制
     * 
     * @param principalCollection
     *            身份
     * @return
     */
    <ID> void doLimitPrincipalTicketCount(ZKSecPrincipalCollection<ID> principalCollection);

    /**
     * 取身份的权限
     *
     * @Title: principalsCount
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 2, 2021 10:05:18 PM
     * @param principalCollection
     * @return void
     */
    <ID> ZKSecAuthorizationInfo getZKSecAuthorizationInfo(ZKSecPrincipalCollection<ID> principalCollection);

}
