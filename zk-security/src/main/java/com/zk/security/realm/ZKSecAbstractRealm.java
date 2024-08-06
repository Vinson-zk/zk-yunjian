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
* @Title: ZKSecAbstractRealm.java 
* @author Vinson 
* @Package com.zk.security.realm 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 11:39:22 PM 
* @version V1.0 
*/
package com.zk.security.realm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.security.authz.ZKSecAuthorizationInfo;
import com.zk.security.authz.ZKSecAuthorizationInfoStore;
import com.zk.security.authz.support.ticket.ZKSecTicketAuthorizationInfoStore;
import com.zk.security.principal.ZKSecPrincipal;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.token.ZKSecAuthenticationToken;
import com.zk.security.utils.ZKSecSecurityUtils;

/** 
* @ClassName: ZKSecAbstractRealm 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecAbstractRealm extends ZKSecNameRealm {

    protected Logger log = LogManager.getLogger(getClass());

    ZKSecAuthorizationInfoStore authorizationInfoStore;

    public ZKSecAbstractRealm(String realmName) {
        super(realmName);
        this.authorizationInfoStore = new ZKSecTicketAuthorizationInfoStore();
    }

    /**
     * @return authorizationInfoStore sa
     */
    public ZKSecAuthorizationInfoStore getAuthorizationInfoStore() {
        return authorizationInfoStore;
    }

    /**
     * @param authorizationInfoStore
     *            the authorizationInfoStore to set
     */
    public void setAuthorizationInfoStore(ZKSecAuthorizationInfoStore authorizationInfoStore) {
        this.authorizationInfoStore = authorizationInfoStore;
    }

    /**
     * 认证，登录
     * 
     * @param authcToken
     * @return
     */
    @Override
    public <ID> ZKSecPrincipalCollection<ID> authentication(ZKSecAuthenticationToken authcToken) {
        if (supports(authcToken)) {
            ZKSecPrincipalCollection<ID> pc = doAuthentication(authcToken);
//            this.doLimitPrincipalTicketCount(pc);
            return pc;
        }
        return null;
    }

    /**
     * 认证，登录
     * 
     * @param authcToken
     * @return
     * @throws com.zk.security.exception.ZKSecCodeException
     */
    protected abstract <ID> ZKSecPrincipalCollection<ID> doAuthentication(ZKSecAuthenticationToken authcToken);

    /**
     * (not Javadoc)
     * Title: getZKSecAuthorizationInfo
     * Description:
     * @param principalCollection
     * @return
     * @see com.zk.security.realm.ZKSecRealm#getZKSecAuthorizationInfo(com.zk.security.principal.pc.ZKSecPrincipalCollection)
     */
    @Override
    public <ID> ZKSecAuthorizationInfo getZKSecAuthorizationInfo(ZKSecPrincipalCollection<ID> principalCollection) {
        ZKSecAuthorizationInfo authorizationInfo = null;
        if (this.getAuthorizationInfoStore() != null) {
            // 优先从 权限存储 中读取
            authorizationInfo = this.getAuthorizationInfoStore()
                    .getZKSecAuthorizationInfo(ZKSecSecurityUtils.getTikcet(), this.getRealmName());
        }
        if (authorizationInfo == null) {
            authorizationInfo = doGetZKSecAuthorizationInfo(principalCollection);
            if (authorizationInfo == null) {
                log.error("[>_<:20220517-1143-001] 取认证域权限信息为 null ");
            }
            else {
                if (this.getAuthorizationInfoStore() != null) {
                    // 写入 权限存储
                    this.getAuthorizationInfoStore().setZKSecAuthorizationInfo(ZKSecSecurityUtils.getTikcet(),
                            this.getRealmName(), authorizationInfo);
                }
            }
        }
        return authorizationInfo;
    }

    public abstract <ID> ZKSecAuthorizationInfo doGetZKSecAuthorizationInfo(
            ZKSecPrincipalCollection<ID> principalCollection);

    /**
     * 鉴权，授权; 暂未启用
     * 
     * @param principalCollection
     *            身份
     * @param permissionCode
     *            权限代码
     * @return
     */
    @Override
    public <ID> boolean checkPermission(ZKSecPrincipalCollection<ID> principalCollection, String permissionCode) {
//        ZKSecAuthorizationInfo authorizationInfo = this.getZKSecAuthorizationInfo(principalCollection);
//        boolean b = false;
//        if (authorizationInfo != null) {
//            // 鉴定是否拥有权限代码
//            if (!b && authorizationInfo.getAuthCodes() != null) {
//                b = authorizationInfo.getAuthCodes().contains(permissionCode);
//            }
//        }
//        if (!b) {
//            return this.doCheckPermission(principalCollection, permissionCode);
//        }
//        return b;
        return false;
    }

    /**
     * 鉴定 api 权限 代码
     * 
     * @param principalCollection
     * @param apiCode
     *            api 权限 代码
     * @return true-鉴定成功；反之鉴定失败
     */
    @Override
    public <ID> boolean checkApiCode(ZKSecPrincipalCollection<ID> principalCollection, String apiCode) {
        ZKSecPrincipal<?> pp = principalCollection.getPrimaryPrincipal();
        if (ZKSecPrincipal.KeyType.Distributed_server == pp.getType()) {
            // 微服务单请求，不较验权限
            return true;
        }
        ZKSecAuthorizationInfo authorizationInfo = this.getZKSecAuthorizationInfo(principalCollection);
        if (authorizationInfo != null) {
            // 1、如果用户是拥有者公司，若用户是 admin 账号、超级管理员角色、admin 权限 三者之一，返回 有权限；不是，下一步
            if (authorizationInfo.isOwnerCompany() && authorizationInfo.isAdmin()) {
                return true;
            }
            // 2、公司是否有权限，若没权限，直接返回无权限；公司有权限，下一步
            if (authorizationInfo.getCompanyApiCodes() != null) {
                if (!authorizationInfo.getCompanyApiCodes().contains(apiCode)) {
                    return false; // 公司无此权限，直接返回 false
                }
            }
            else {
                return false; // 公司无此权限，直接返回 false
            }
            // 3、若用户是 admin 账号、超级管理员角色、admin 权限 三者之一，返回 有权限；不是，返回无权限
            if (authorizationInfo.isAdmin()) {
                return true;
            }
            // 4、若用户有权限，返回有权限；用户无权限，下一步
            if (authorizationInfo.getAuthCodes() != null) {
                if (authorizationInfo.getAuthCodes().contains("admin")) {
                    return true;
                }
            }
            return this.doCheckApiCode(principalCollection, apiCode);
        }

        return false;
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
    protected abstract <ID> boolean doCheckPermission(ZKSecPrincipalCollection<ID> principalCollection,
            String permissionCode);

    /**
     * 执行  api接口权限 代码 鉴定
     * 
     * @param principalCollection
     *            身份
     * @param apiCode
     *             api接口权限 代码
     * @return
     */
    protected abstract <ID> boolean doCheckApiCode(ZKSecPrincipalCollection<ID> principalCollection, String apiCode);

}




