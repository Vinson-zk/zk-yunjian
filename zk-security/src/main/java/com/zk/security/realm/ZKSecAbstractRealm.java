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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zk.security.authz.ZKSecAuthorizationInfo;
import com.zk.security.authz.ZKSecAuthorizationInfoStore;
import com.zk.security.authz.support.ticket.ZKSecTicketAuthorizationInfoStore;
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

    protected Logger log = LoggerFactory.getLogger(getClass());

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
    public ZKSecPrincipalCollection authentication(ZKSecAuthenticationToken authcToken) {
        if (supports(authcToken)) {
            ZKSecPrincipalCollection pc = doAuthentication(authcToken);
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
    protected abstract ZKSecPrincipalCollection doAuthentication(ZKSecAuthenticationToken authcToken);

    /**
     * (not Javadoc)
     * Title: getZKSecAuthorizationInfo
     * Description:
     * @param principalCollection
     * @return
     * @see com.zk.security.realm.ZKSecRealm#getZKSecAuthorizationInfo(com.zk.security.principal.pc.ZKSecPrincipalCollection)
     */
    @Override
    public ZKSecAuthorizationInfo getZKSecAuthorizationInfo(ZKSecPrincipalCollection principalCollection) {
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

    public abstract ZKSecAuthorizationInfo doGetZKSecAuthorizationInfo(ZKSecPrincipalCollection principalCollection);

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
    public boolean checkPermission(ZKSecPrincipalCollection principalCollection, String permissionCode) {
        ZKSecAuthorizationInfo authorizationInfo = this.getZKSecAuthorizationInfo(principalCollection);
        boolean b = false;
        if (authorizationInfo != null) {
            // 鉴定是否拥有权限代码
            if (!b && authorizationInfo.getAuthCodes() != null) {
                b = authorizationInfo.getAuthCodes().contains(permissionCode);
            }
        }
        if (!b) {
            return this.doCheckPermission(principalCollection, permissionCode);
        }
        return b;
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
    public boolean checkApiCode(ZKSecPrincipalCollection principalCollection, String apiCode) {
        ZKSecAuthorizationInfo authorizationInfo = this.getZKSecAuthorizationInfo(principalCollection);
        boolean b = false;
        if (authorizationInfo != null) {
            // 鉴定是否拥有权限代码
            if (!b && authorizationInfo.getApiCodes() != null) {
                b = authorizationInfo.getApiCodes().contains(apiCode);
            }
//            if(!b && authorizationInfo.getAuthCodes() != null) {
//                b = authorizationInfo.getAuthCodes().contains(apiCode);
//            }
        }
        if (!b) {
            return this.doCheckApiCode(principalCollection, apiCode);
        }
        return b;
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
    protected abstract boolean doCheckPermission(ZKSecPrincipalCollection principalCollection, String permissionCode);

    /**
     * 执行  api接口权限 代码 鉴定
     * 
     * @param principalCollection
     *            身份
     * @param apiCode
     *             api接口权限 代码
     * @return
     */
    protected abstract boolean doCheckApiCode(ZKSecPrincipalCollection principalCollection, String apiCode);

}




