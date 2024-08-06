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
* @Title: ZKSysSecRealm.java 
* @author Vinson 
* @Package com.zk.sys.common.sec.realm 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 20, 2022 4:53:07 PM 
* @version V1.0 
*/
package com.zk.sys.sec.realm;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.exception.ZKSecAuthenticationException;
import com.zk.security.authz.ZKSecAuthorizationInfo;
import com.zk.security.authz.ZKSecSimpleAuthorizationInfo;
import com.zk.security.principal.ZKSecDefaultUserPrincipal;
import com.zk.security.principal.ZKSecPrincipal;
import com.zk.security.principal.ZKSecUserPrincipal;
import com.zk.security.principal.pc.ZKSecDefaultPrincipalCollection;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.realm.ZKSecAbstractRealm;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.ticket.ZKSecTicketManager;
import com.zk.security.token.ZKSecAuthcUserToken;
import com.zk.security.token.ZKSecAuthenticationToken;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.sec.service.ZKSysSecAuthService;
import com.zk.sys.sec.service.ZKSysSecUserService;

/** 
* @ClassName: ZKSysSecRealm 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSysSecRealm extends ZKSecAbstractRealm {
//ZKSecAbstractRealm {
    
    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    ZKSysSecUserService secUserService;

    ZKSysSecAuthService sysSecAuthService;

    ZKSecTicketManager ticketManager;

    String applicationName;

    public ZKSysSecRealm(String applicationName, ZKSysSecUserService secUserService,
            ZKSysSecAuthService sysSecAuthService, ZKSecTicketManager ticketManager) {
        this(applicationName, ZKSysSecRealm.class.getName(), secUserService, sysSecAuthService, ticketManager);
    }

    public ZKSysSecRealm(String applicationName, String realmName, ZKSysSecUserService secUserService,
            ZKSysSecAuthService sysSecAuthService, ZKSecTicketManager ticketManager) {
        super(realmName);
        this.applicationName = applicationName;
        this.secUserService = secUserService;
        this.sysSecAuthService = sysSecAuthService;
        this.ticketManager = ticketManager;
        this.setAuthorizationInfoStore(null);
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
     * @throws ZKSecCodeException
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Override
    protected ZKSecPrincipalCollection<String> doAuthentication(ZKSecAuthenticationToken authcToken) {
        ZKSecPrincipalCollection<String> pc = new ZKSecDefaultPrincipalCollection<String>();
        ZKSecAuthcUserToken authcUserToken = (ZKSecAuthcUserToken) authcToken;
        ZKSysOrgUser loginUser = this.secUserService.login(authcUserToken);
        if (loginUser != null) {
            ZKSecPrincipal<String> p = new ZKSecDefaultUserPrincipal<String>(loginUser.getPkId(),
                    loginUser.getAccount(), loginUser.getNickname(), authcUserToken.getOsType(),
                    authcUserToken.getUdid(), authcUserToken.getAppType(), authcUserToken.getAppId(),
                    loginUser.getGroupCode(), loginUser.getCompanyId(), loginUser.getCompanyCode());
            pc.add(this.getRealmName(), p);
            return pc;
        }
        else {
            log.error("[>_<:2022425-1723-001] 登录失败，用户名或密码错误 {}-{}", authcUserToken.getCompanyCode(),
                    authcUserToken.getUsername());
            throw ZKSecAuthenticationException.as("zk.sys.020003");
        }
    }

    @Override
    public <ID> ZKSecAuthorizationInfo doGetZKSecAuthorizationInfo(ZKSecPrincipalCollection<ID> principalCollection) {
        ZKSecPrincipal<ID> pp = principalCollection.getPrimaryPrincipal();
        if (ZKSecPrincipal.KeyType.Distributed_server == pp.getType()) {
            // 微服务间请求，不较验权限，返回一个空的权限信息
            return new ZKSecSimpleAuthorizationInfo();
        }
        // 设备用户的 API 权限代码
        ZKSecAuthorizationInfo authInfo = sysSecAuthService.getUserAuthInfo(pp.getPkId().toString(), applicationName);
        return authInfo;
    }

    /**
     * 执行权限代码鉴定; 暂未启动
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
        for (ZKSecPrincipal<ID> p : principalCollection) {
            if (p instanceof ZKSecUserPrincipal<?>) {
                List<ZKSecTicket> tks = null;
                ZKSecTicket tk = ZKSecSecurityUtils.getTikcet();
                if (tk == null) {
                    tks = this.ticketManager.findTickeByPrincipal(p, null);
                }
                else {
                    tks = this.ticketManager.findTickeByPrincipal(p, Arrays.asList(tk));
                }

                if (tks != null && tks.size() > 0) {
                    if (ZKSecSecurityUtils.getSubject().isAuthenticated()) {
                        // 当前用户是登录认证的在线用户，保留当前用户，踢掉之前的用户；
                        for (ZKSecTicket t : tks) {
                            if (t.isValid()) {
                                t.stop();
                                t.put(ZKSecTicket.KeyTicketInfo.stop_info_code, "zk.sec.000012"); // zk.sec.000012=用户已在其他地方登录，请重新登录
                            }
                            else {
                                log.info("[^_^:20220518-0906-001] 令牌 [{}] 失效！", t.getTkId());
                            }
                        }
                    }
                    else {
                        // 记住我进来的用户，退出当前用户；
                        ZKSecSecurityUtils.getSubject().logout();
                        throw ZKSecAuthenticationException.as("zk.sec.000012"); // zk.sec.000012=用户已在其他地方登录，请重新登录
                    }
                }
            }
        }
    }

}


