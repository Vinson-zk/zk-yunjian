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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.framework.security.ZKAuthPermission;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.security.authz.ZKSecAuthorizationInfo;
import com.zk.security.authz.ZKSecSimpleAuthorizationInfo;
import com.zk.security.exception.ZKSecCodeException;
import com.zk.security.principal.ZKSecDefaultUserPrincipal;
import com.zk.security.principal.ZKSecPrincipal;
import com.zk.security.principal.ZKSecUserPrincipal;
import com.zk.security.principal.pc.ZKSecDefaultPrincipalCollection;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.realm.ZKSecAbstractRealm;
import com.zk.security.realm.ZKSecSampleRealm;
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
    
    // 权限策略配置的 KEY
    public static interface Key_Auth_Strategy {
        // 权限配置项组别代码
        public static final String name = "Auth_Strategy";

        // 权限配置项 部门策略配置项代码
        public static final String Auth_Dept = "Auth_Dept";

        // 权限配置项 用户策略配置项代码
        public static final String Auth_User = "Auth_User";

        // 权限配置项 角色策略配置项代码
        public static final String Auth_Role = "Auth_Role";

        // 权限配置项 职级策略配置项代码
        public static final String Auth_Rank = "Auth_Rank";

        // 权限配置项 用户类型策略配置项代码
        public static final String Auth_UserType = "Auth_UserType";
    }

    /**
     * 日志对象
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    ZKSysSecUserService secUserService;

    ZKSysSecAuthService sysSecAuthService;

    ZKSecTicketManager ticketManager;

    public ZKSysSecRealm() {
        super(ZKSecSampleRealm.class.getName());
        this.setAuthorizationInfoStore(null);
    }

    public ZKSysSecRealm(String realmName) {
        super(realmName);
        this.setAuthorizationInfoStore(null);
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

    /**
     * @return sysSetItemService sa
     */
    public ZKSysSecAuthService getSysSecAuthService() {
        if (sysSecAuthService == null) {
            sysSecAuthService = ZKEnvironmentUtils.getApplicationContext().getBean(ZKSysSecAuthService.class);
        }
        return sysSecAuthService;
    }

    /**
     * @param sysSetItemService
     *            the sysSetItemService to set
     */
    public void setSysSecAuthService(ZKSysSecAuthService sysSecAuthService) {
        this.sysSecAuthService = sysSecAuthService;
    }

    /**
     * @return secUserService sa
     */
    public ZKSysSecUserService getSecUserService() {
        if (secUserService == null) {
            secUserService = ZKEnvironmentUtils.getApplicationContext().getBean(ZKSysSecUserService.class);
        }
        return secUserService;
    }

    /**
     * @param secUserService
     *            the secUserService to set
     */
    public void setSecUserService(ZKSysSecUserService secUserService) {
        this.secUserService = secUserService;
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
    @Override
    protected ZKSecPrincipalCollection doAuthentication(ZKSecAuthenticationToken authcToken) {
        ZKSecPrincipalCollection pc = new ZKSecDefaultPrincipalCollection();
        ZKSecAuthcUserToken authcUserToken = (ZKSecAuthcUserToken) authcToken;
        ZKSysOrgUser loginUser = this.getSecUserService().login(authcUserToken);
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
            throw new ZKSecCodeException("zk.sys.020003");
        }
    }

    @Override
    public ZKSecAuthorizationInfo doGetZKSecAuthorizationInfo(ZKSecPrincipalCollection principalCollection) {
        ZKSecPrincipal<?> pp = principalCollection.getPrimaryPrincipal();
        if (ZKSecPrincipal.KeyType.Distributed_server == pp.getType()) {
            // 微服务间请求，不较验权限，返回一个空的权限信息
            return new ZKSecSimpleAuthorizationInfo();
        }
        // 设备用户的 API 权限代码
        ZKAuthPermission p = ZKUserCacheUtils.getAuth(ZKUserCacheUtils.getUser(pp.getPkId()));
        ZKSecAuthorizationInfo authorizationInfo = this.getSysSecAuthService().paseByAuthPermission(p);
        return authorizationInfo;
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
    protected boolean doCheckPermission(ZKSecPrincipalCollection principalCollection, String permissionCode) {
        ZKSecPrincipal<?> pp = principalCollection.getPrimaryPrincipal();
        if (ZKSecPrincipal.KeyType.Distributed_server == pp.getType()) {
            // 微服务单请求，不较验权限
            return true;
        }
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
    protected boolean doCheckApiCode(ZKSecPrincipalCollection principalCollection, String apiCode) {
        ZKSecPrincipal<?> pp = principalCollection.getPrimaryPrincipal();
        if (ZKSecPrincipal.KeyType.Distributed_server == pp.getType()) {
            // 微服务单请求，不较验权限
            return true;
        }
        ZKSecAuthorizationInfo authorizationInfo = this.getZKSecAuthorizationInfo(principalCollection);
        boolean r = false;
        if (authorizationInfo != null) {
            // 如果用户拥有 admin 权限代码，所有接口可访问
            if (!r && authorizationInfo.getAuthCodes() != null) {
                r = authorizationInfo.getAuthCodes().contains("admin");
            }
        }
        return r;
    }

    @Override
    public void doLimitPrincipalTicketCount(ZKSecPrincipalCollection principalCollection) {
        for (ZKSecPrincipal<?> p : principalCollection) {
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
                        throw new ZKSecCodeException("zk.sec.000012"); // zk.sec.000012=用户已在其他地方登录，请重新登录
                    }
                }
            }
        }
    }

}


