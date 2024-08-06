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
* @Title: ZKDistributedRealm.java 
* @author Vinson 
* @Package com.zk.framework.security.realm 
* @Description: TODO(simple description this file what to do. ) 
* @date May 16, 2022 11:46:46 AM 
* @version V1.0 
*/
package com.zk.framework.security.realm;

import com.zk.framework.security.service.ZKSecAuthService;
import com.zk.security.authz.ZKSecAuthorizationInfo;
import com.zk.security.authz.ZKSecSimpleAuthorizationInfo;
import com.zk.security.principal.ZKSecPrincipal;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.realm.ZKSecAbstractRealm;
import com.zk.security.ticket.ZKSecTicketManager;
import com.zk.security.token.ZKSecAuthenticationToken;

/** 
* @ClassName: ZKDistributedRealm 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDistributedRealm extends ZKSecAbstractRealm {

    String applicationName;

    private ZKSecAuthService<String> secAuthService;
    
    private ZKSecTicketManager ticketManager;
    
    
    
    public ZKDistributedRealm(String applicationName, ZKSecAuthService<String> secAuthService,
            ZKSecTicketManager ticketManager) {
        this(applicationName, ZKDistributedRealm.class.getName(), secAuthService, ticketManager);
    }

    public ZKDistributedRealm(String applicationName, String realmName, ZKSecAuthService<String> secAuthService,
            ZKSecTicketManager ticketManager) {
        super(realmName);
        this.applicationName = applicationName;
        this.secAuthService = secAuthService;
        this.ticketManager = ticketManager;
        this.setAuthorizationInfoStore(null);
    }

    /**
     * @return ticketManager sa
     */
    public ZKSecTicketManager getTicketManager() {
        return ticketManager;
    }

    @Override
    public boolean supports(ZKSecAuthenticationToken authcToken) {
//        if (authcToken instanceof ZKSecAuthcUserToken) {
//            return true;
//        }
        return false;
    }

    /**
     * 认证，登录；分布式认证域中，主要是做权限的鉴定
     * 
     * @param authcToken
     * @return
     * @throws com.zk.security.exception.ZKSecCodeException
     * @throws Exception
     */
    @Override
    protected <ID> ZKSecPrincipalCollection<ID> doAuthentication(ZKSecAuthenticationToken authcToken) {
        return null;
    }

    @Override
    public <ID> ZKSecAuthorizationInfo doGetZKSecAuthorizationInfo(ZKSecPrincipalCollection<ID> principalCollection) {
        ZKSecPrincipal<ID> pp = principalCollection.getPrimaryPrincipal();
        if (ZKSecPrincipal.KeyType.Distributed_server == pp.getType()) {
            // 微服务间请求，不较验权限，返回一个空的权限信息
            return new ZKSecSimpleAuthorizationInfo();
        }
        // 设备用户的 API 权限代码

        ZKSecAuthorizationInfo authInfo = this.secAuthService.getUserAuthInfo((String) pp.getPkId(), applicationName);
        return authInfo;
    }

    /**
     * 执行权限代码鉴定
     * 
     * @param principalCollection
     *            身份
     * @param permissionCode
     *            权限代码
     * @return
     */
    @Override
    protected <ID> boolean doCheckPermission(ZKSecPrincipalCollection<ID> principalCollection, String permissionCode) {
        return false;
    }

    /**
     * 执行 api接口权限 代码 鉴定
     * 
     * @param principalCollection
     *            身份
     * @param apiCode
     *            api接口权限 代码
     * @return
     */
    @Override
    protected <ID> boolean doCheckApiCode(ZKSecPrincipalCollection<ID> principalCollection, String apiCode) {
        return false;
    }

    @Override
    public <ID> void doLimitPrincipalTicketCount(ZKSecPrincipalCollection<ID> principalCollection) {

    }

}

