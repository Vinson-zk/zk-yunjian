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
* @Title: ZKSysSecAuthService.java 
* @author Vinson 
* @Package com.zk.sys.sec.service 
* @Description: TODO(simple description this file what to do. ) 
* @date May 10, 2022 10:41:56 AM 
* @version V1.0 
*/
package com.zk.sys.sec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.framework.security.ZKAuthPermission;
import com.zk.framework.security.ZKSampleAuthPermission;
import com.zk.framework.security.service.ZKSecAuthService;
import com.zk.framework.security.userdetails.ZKUser;
import com.zk.security.authz.ZKSecAuthorizationInfo;
import com.zk.security.authz.ZKSecSimpleAuthorizationInfo;
import com.zk.sys.auth.service.*;
import com.zk.sys.org.service.ZKSysOrgUserService;
import com.zk.sys.sec.realm.ZKSysSecRealm.Key_Auth_Strategy;
import com.zk.sys.settings.service.ZKSysSetItemService;

/** 
* @ClassName: ZKSysSecAuthService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
@Transactional(readOnly = true)
public class ZKSysSecAuthService implements ZKSecAuthService<String> {

    @Autowired
    ZKSysOrgUserService sysOrgUserService;

    @Autowired
    ZKSysAuthUserRoleService sysAuthUserRoleService;

    @Autowired
    ZKSysAuthDeptService sysAuthDeptService;

    @Autowired
    ZKSysAuthUserService sysAuthUserService;

    @Autowired
    ZKSysAuthRankService sysAuthRankService;

    @Autowired
    ZKSysAuthUserTypeService sysAuthUserTypeService;

    @Autowired
    ZKSysAuthRoleService sysAuthRoleService;

    @Autowired
    ZKSysAuthFuncApiService sysAuthFuncApiService;

    @Autowired
    ZKSysSetItemService sysSetItemService;

    /**
     * (not Javadoc)
     * <p>
     * Title: getByUserId
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param userId
     * @return
     * @see com.zk.framework.security.service.ZKSecAuthService#getByUserId(java.lang.Object)
     */
    @Override
    public ZKUser<String> getByUserId(String userId) {
        return sysOrgUserService.get(userId);
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: ZKAuthPermission
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param user
     * @return
     * @see com.zk.framework.security.service.ZKSecAuthService#getByAuthUser(com.zk.framework.security.userdetails.ZKUser)
     */
    @Override
    public ZKAuthPermission getByAuthUser(ZKUser<String> user) {
        ZKSampleAuthPermission authPermission = new ZKSampleAuthPermission();
        this.setDeptApiCode(user, authPermission);
        this.setUserApiCode(user, authPermission);
        this.setRoleApiCode(user, authPermission);
        this.setRankApiCode(user, authPermission);
        this.setUserTypeApiCode(user, authPermission);

        this.setDeptAuthCode(user, authPermission);
        this.setUserAuthCode(user, authPermission);
        this.setRoleAuthCode(user, authPermission);
        this.setRankAuthCode(user, authPermission);
        this.setUserTypeAuthCode(user, authPermission);
        return authPermission;
    }

    // 更新指定渠道的权限代码和API接口代码
    @Override
    public ZKAuthPermission getByAuthUser(ZKUser<String> user, ZKAuthPermission authPermission,
            ZKAuthChannel authChannel) {
        if (authPermission != null) {
            switch (authChannel) {
                case Dept:
                    this.setDeptApiCode(user, authPermission);
                    this.setDeptAuthCode(user, authPermission);
                    break;
                case User:
                    this.setUserApiCode(user, authPermission);
                    this.setUserAuthCode(user, authPermission);
                    break;
                case Role:
                    this.setRoleApiCode(user, authPermission);
                    this.setRoleAuthCode(user, authPermission);
                    break;
                case Rank:
                    this.setRankApiCode(user, authPermission);
                    this.setRankAuthCode(user, authPermission);
                    break;
                case UserType:
                    this.setUserTypeApiCode(user, authPermission);
                    this.setUserTypeAuthCode(user, authPermission);
                    break;
            }
        }

        return authPermission;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getByRoleCodesUserId
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param userId
     * @return
     */
    @Override
    public List<String> getByRoleCodesUserId(String userId) {
        return sysAuthUserRoleService.findRoleCodesByUserId(userId);
    }

    /*************************************************************************************/
    /** API 接口代码 *****************************************************/
    // 设置用户通过部门拥有的 API 接口代码
    protected void setDeptApiCode(ZKUser<String> user, ZKAuthPermission authPermission) {
        authPermission.getApiCodeByDept()
                .addAll(this.sysAuthDeptService.findApiCodesByDeptId(user.getDeptId()));
    }

    // 设置用户直接拥有的 API 接口代码
    protected void setUserApiCode(ZKUser<String> user, ZKAuthPermission authPermission) {
        authPermission.getApiCodeByUser().addAll(this.sysAuthUserService.findApiCodesByUserId(user.getPkId()));
    }

    // 设置用户通过用户类型拥有的 API 接口代码
    protected void setUserTypeApiCode(ZKUser<String> user, ZKAuthPermission authPermission) {
        authPermission.getApiCodeByUserType()
                .addAll(this.sysAuthUserTypeService.findApiCodesByUserTypeId(user.getUserTypeId()));
    }

    // 设置用户通过职级拥有的 API 接口代码
    protected void setRankApiCode(ZKUser<String> user, ZKAuthPermission authPermission) {
        authPermission.getApiCodeByRank()
                .addAll(this.sysAuthRankService.findApiCodesByRankId(user.getRankId()));
    }

    // 设置用户通过角色拥有的 API 接口代码
    protected void setRoleApiCode(ZKUser<String> user, ZKAuthPermission authPermission) {
        authPermission.getApiCodeByRole()
                .addAll(this.sysAuthUserRoleService.findApiCodesByUserId(user.getPkId()));
    }

    /** 权限代码 *********************************************************/
    // 设置部门的权限代码
    protected void setDeptAuthCode(ZKUser<String> user, ZKAuthPermission authPermission) {
        authPermission.getAuthCodeByDept()
                .addAll(this.sysAuthDeptService.findAuthCodesByDeptId(user.getDeptId()));
    }
    // 设置用户的权限代码
    protected void setUserAuthCode(ZKUser<String> user, ZKAuthPermission authPermission) {
        authPermission.getAuthCodeByUser().addAll(this.sysAuthUserService.findAuthCodesByUserId(user.getPkId()));
    }
    // 设置用户类型的权限代码
    protected void setUserTypeAuthCode(ZKUser<String> user, ZKAuthPermission authPermission) {
        authPermission.getAuthCodeByUserType()
                .addAll(this.sysAuthUserTypeService.findAuthCodesByUserTypeId(user.getUserTypeId()));
    }
    // 设置职级的权限代码
    protected void setRankAuthCode(ZKUser<String> user, ZKAuthPermission authPermission) {
        authPermission.getAuthCodeByRank()
                .addAll(this.sysAuthRankService.findAuthCodesByRankId(user.getRankId()));
    }
    // 设置角色的权限代码
    protected void setRoleAuthCode(ZKUser<String> user, ZKAuthPermission authPermission) {
        authPermission.getAuthCodeByRole()
                .addAll(this.sysAuthUserRoleService.findAuthCodesByUserId(user.getPkId()));
    }

    // 将权限 ZKAuthPermission 转为 ZKSecSimpleAuthorizationInfo
    public ZKSecAuthorizationInfo paseByAuthPermission(ZKAuthPermission ap) {
        ZKSecSimpleAuthorizationInfo authorizationInfo = new ZKSecSimpleAuthorizationInfo();
        // 部门的权限
        if (this.sysSetItemService.getByCode(Key_Auth_Strategy.name, Key_Auth_Strategy.Auth_Dept)
                .getBooleanValue()) {
            authorizationInfo.addApiCode(ap.getApiCodeByDept());
            authorizationInfo.addAuthCode(ap.getAuthCodeByDept());
        }
        // 用户直接拥有的权限
        if (this.sysSetItemService.getByCode(Key_Auth_Strategy.name, Key_Auth_Strategy.Auth_User)
                .getBooleanValue()) {
            authorizationInfo.addApiCode(ap.getApiCodeByUser());
            authorizationInfo.addAuthCode(ap.getAuthCodeByUser());
        }
        // 角色拥有的权限
        if (this.sysSetItemService.getByCode(Key_Auth_Strategy.name, Key_Auth_Strategy.Auth_Role)
                .getBooleanValue()) {
            authorizationInfo.addApiCode(ap.getApiCodeByRole());
            authorizationInfo.addAuthCode(ap.getAuthCodeByRole());
        }
        // 职级的权限
        if (this.sysSetItemService.getByCode(Key_Auth_Strategy.name, Key_Auth_Strategy.Auth_Rank)
                .getBooleanValue()) {
            authorizationInfo.addApiCode(ap.getApiCodeByRank());
            authorizationInfo.addAuthCode(ap.getAuthCodeByRank());
        }
        // 用户类型的权限
        if (this.sysSetItemService.getByCode(Key_Auth_Strategy.name, Key_Auth_Strategy.Auth_UserType)
                .getBooleanValue()) {
            authorizationInfo.addApiCode(ap.getApiCodeByUserType());
            authorizationInfo.addAuthCode(ap.getAuthCodeByUserType());
        }
        return authorizationInfo;
    }

}
