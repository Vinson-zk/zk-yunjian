/** 
、* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSecSampleRealm.java 
* @author Vinson 
* @Package com.zk.security.realm 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 11:40:38 PM 
* @version V1.0 
*/
package com.zk.security.realm;

import java.util.Arrays;
import java.util.List;

import com.zk.core.exception.ZKSecAuthenticationException;
import com.zk.security.authz.ZKSecAuthorizationInfo;
import com.zk.security.authz.ZKSecSimpleAuthorizationInfo;
import com.zk.security.principal.ZKSecDefaultUserPrincipal;
import com.zk.security.principal.ZKSecPrincipal;
import com.zk.security.principal.ZKSecUserPrincipal;
import com.zk.security.principal.pc.ZKSecDefaultPrincipalCollection;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.ticket.ZKSecTicketManager;
import com.zk.security.token.ZKSecAuthcUserToken;
import com.zk.security.token.ZKSecAuthenticationToken;
import com.zk.security.utils.ZKSecSecurityUtils;

/** 
* @ClassName: ZKSecSampleRealm 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecSampleRealm extends ZKSecAbstractRealm {

    private ZKSecTicketManager ticketManager;

    public ZKSecSampleRealm() {
        super(ZKSecSampleRealm.class.getName());
    }

    public ZKSecSampleRealm(String realmName) {
        super(realmName);
    }

    /**
     * @return ticketManager sa
     */
    public ZKSecTicketManager getTicketManager() {
        return ticketManager;
    }

    /**
     * @param ticketManager
     *            the ticketManager to set
     */
    public void setTicketManager(ZKSecTicketManager ticketManager) {
        this.ticketManager = ticketManager;
    }

    @Override
    public boolean supports(ZKSecAuthenticationToken authcToken) {
        if (authcToken instanceof ZKSecAuthcUserToken) {
            return true;
        }
        return false;
    }

    /**
     * 认证，登录
     * 
     * @param token
     * @return
     * @throws com.zk.security.exception.ZKSecCodeException
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Override
    protected ZKSecPrincipalCollection<String> doAuthentication(ZKSecAuthenticationToken authcToken) {
        ZKSecPrincipalCollection<String> pc = new ZKSecDefaultPrincipalCollection<String>();
        ZKSecAuthcUserToken authcUserToken = (ZKSecAuthcUserToken) authcToken;
        if ("admin".equals(authcUserToken.getUsername()) && "admin".equals(new String(authcUserToken.getPwd()))) {
            ZKSecPrincipal<String> p = new ZKSecDefaultUserPrincipal<String>(authcUserToken.getUsername(),
                    authcUserToken.getUsername(), "admin 游客", authcUserToken.getOsType(), authcUserToken.getUdid(),
                    authcUserToken.getAppType(), authcUserToken.getAppId(), authcUserToken.getCompanyCode(),
                    authcUserToken.getCompanyCode(), authcUserToken.getCompanyCode());
            pc.add(this.getRealmName(), p);
            return pc;
        }

        if ("test".equals(authcUserToken.getUsername()) && "test".equals(new String(authcUserToken.getPwd()))) {
            ZKSecPrincipal<String> p = new ZKSecDefaultUserPrincipal<String>(authcUserToken.getUsername(),
                    authcUserToken.getUsername(), "test 游客", authcUserToken.getOsType(), authcUserToken.getUdid(),
                    authcUserToken.getAppType(), authcUserToken.getAppId(), authcUserToken.getCompanyCode(),
                    authcUserToken.getCompanyCode(), authcUserToken.getCompanyCode());
            pc.add(this.getRealmName(), p);
            return pc;
        }

//      zk.sec.000001=用户不存在
        if (!"admin".equals(authcUserToken.getUsername()) && !"test".equals(authcUserToken.getUsername())) {
            throw ZKSecAuthenticationException.as("zk.sec.000001", null);
        }

//      zk.sec.000002=密码错误
        if (!"admin".equals(new String(authcUserToken.getPwd()))
                && !"test".equals(new String(authcUserToken.getPwd()))) {
            throw ZKSecAuthenticationException.as("zk.sec.000002", null);
        }
        return null;
    }

    @Override
    public <ID> ZKSecAuthorizationInfo doGetZKSecAuthorizationInfo(ZKSecPrincipalCollection<ID> principalCollection) {
        ZKSecSimpleAuthorizationInfo authorizationInfo = new ZKSecSimpleAuthorizationInfo();
        authorizationInfo.addApiCode(Arrays.asList("zkSecApiCode"));
        return authorizationInfo;
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
        for (ZKSecPrincipal<ID> p : principalCollection) {
            if (p instanceof ZKSecUserPrincipal<?>) {
                if ("admin".equals(((ZKSecUserPrincipal<?>) p).getUsername())) {
                    if ("test_permissionCode".equals(permissionCode)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 执行 api接口权限 代码 鉴定
     * 
     * @param principalCollection
     *            身份
     * @param apiCode
     *             api接口权限 代码
     * @return
     */
    @Override
    protected <ID> boolean doCheckApiCode(ZKSecPrincipalCollection<ID> principalCollection, String apiCode) {
        for (ZKSecPrincipal<?> p : principalCollection) {
            if (p instanceof ZKSecUserPrincipal<?>) {
                if ("admin".equals(((ZKSecUserPrincipal<?>) p).getUsername())) {
                    if ("test_adminRole".equals(apiCode)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public <ID> void doLimitPrincipalTicketCount(ZKSecPrincipalCollection<ID> principalCollection) {
        for (ZKSecPrincipal<ID> p : principalCollection) {
            if (p instanceof ZKSecUserPrincipal<?>) {
                List<ZKSecTicket> tks = null;
                ZKSecTicket tk = ZKSecSecurityUtils.getTikcet();
                if (tk == null) {
                    tks = this.getTicketManager().findTickeByPrincipal(p, null);
                }
                else {
                    tks = this.getTicketManager().findTickeByPrincipal(p, Arrays.asList(tk));
                }

                if (tks != null && tks.size() > 0) {
                    if (ZKSecSecurityUtils.getSubject().isAuthenticated()) {
                        // 当前用户是登录认证的在线用户，保留当前用户，踢掉之前的用户；
                        for (ZKSecTicket t : tks) {
                            t.stop();
                            t.put(ZKSecTicket.KeyTicketInfo.stop_info_code, "zk.sec.000012"); // zk.sec.000012=用户已在其他地方登录，请重新登录
                            log.info("[^_^:20220427-1014-001] 用户已在其他地方登录，请重新登录！");
                        }
                    }
                    else {
                        // 记住我进来的用户，退出当前用户；
                        log.info("[^_^:20220427-1014-002] 用户已在其他地方登录，请重新登录！");
                        ZKSecSecurityUtils.getSubject().logout();
                        // zk.sec.000012=用户已在其他地方登录，请重新登录
                        throw ZKSecAuthenticationException.as("zk.sec.000012", null);
                    }
                }
            }
        }
    }

}
