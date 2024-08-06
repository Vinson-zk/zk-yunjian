/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
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
* @date Jun 19, 2024 11:15:56 PM 
* @version V1.0 
*/
package com.zk.sys.sec.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.core.utils.ZKStringUtils;
import com.zk.framework.security.userdetails.ZKUser;
import com.zk.security.authz.ZKSecAuthorizationInfo;
import com.zk.security.authz.ZKSecSimpleAuthorizationInfo;
import com.zk.sys.auth.service.ZKSysAuthCompanyService;
import com.zk.sys.auth.service.ZKSysAuthDeptService;
import com.zk.sys.auth.service.ZKSysAuthFuncApiService;
import com.zk.sys.auth.service.ZKSysAuthRankService;
import com.zk.sys.auth.service.ZKSysAuthRoleService;
import com.zk.sys.auth.service.ZKSysAuthUserRoleService;
import com.zk.sys.auth.service.ZKSysAuthUserService;
import com.zk.sys.auth.service.ZKSysAuthUserTypeService;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.org.service.ZKSysOrgUserService;
import com.zk.sys.sec.common.ZKSysSecConstants;
import com.zk.sys.sec.common.ZKSysSecConstants.ZKAuthChannel;
import com.zk.sys.settings.service.ZKSysSetItemService;
import com.zk.sys.utils.ZKSysCompanyCacheUtils;
import com.zk.sys.utils.ZKSysUserCacheUtils;
import com.zk.sys.utils.ZKSysUtils;

/** 
* @ClassName: ZKSysSecAuthService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
@Transactional(readOnly = true)
public class ZKSysSecAuthService {

    @Autowired
    ZKSysOrgUserService sysOrgUserService;

    @Autowired
    ZKSysAuthCompanyService sysAuthCompanyService;

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

    // 取用户；先从缓存中取，如果缓存中不存在；再查数据库，并设置缓存；
    public ZKSysOrgUser getUserById(String userId) {
        ZKSysOrgUser user = ZKSysUserCacheUtils.getUser(userId);
        if (user == null) {
            user = sysOrgUserService.get(userId);
            ZKSysUserCacheUtils.putUser(userId, user);
        }
        return user;
    }

    // 取用户权限信息
    public ZKSecAuthorizationInfo getUserAuthInfo(String userId, String systemCode) {
        return this.getUserAuthInfo(this.getUserById(userId), systemCode);
    }

    public ZKSecAuthorizationInfo getUserAuthInfo(ZKSysOrgUser user, String systemCode) {
        ZKSecSimpleAuthorizationInfo zkAuthInfo = new ZKSecSimpleAuthorizationInfo();
        // 角色代码
        Set<String> codes = this.getRoleCodesByUserId(user.getPkId());
        if (codes != null) {
            zkAuthInfo.addRoleCode(codes);
        }
        // 权限代码
        codes = this.getAuthCodesByUser(user);
        if (codes != null) {
            zkAuthInfo.addAuthCode(codes);
        }
        // API接口代码
        codes = this.getApiCodesByUser(user, systemCode);
        if (codes != null) {
            zkAuthInfo.addApiCode(codes);
        }
        // 设置公司拥有的 接口代码
        codes = this.getAutchCodesByCompanyId(user.getCompanyId());
        if (codes != null) {
            zkAuthInfo.addCompanyAuthCode(codes);
        }
        // 设置公司拥有的 权限代码
        codes = this.getApiCodesByCompanyId(user.getCompanyId(), systemCode);
        if (codes != null) {
            zkAuthInfo.addCompanyApiCode(codes);
        }
        // 设置是否是拥有者公司
        zkAuthInfo.setIsOwnerCompany(ZKSysUtils.getOwnerCompanyCode().equals(user.getCompanyCode()));
        // 设置是否是 admin 用户：admin 账号、超级管理员角色、admin 权限 三者之一
        zkAuthInfo.setIsAdmin(this.isAdmin(user, zkAuthInfo.getRoleCodes(), zkAuthInfo.getAuthCodes()));
        return zkAuthInfo;
    }

    // 取用户角色代码；先从缓存中取，如果缓存中不存在；再查数据库，并设置缓存；
    public Set<String> getRoleCodesByUserId(String userId) {
        Set<String> roleCodes = ZKSysUserCacheUtils.getRoleCodes(userId);
        if (roleCodes == null) {
            roleCodes = sysAuthUserRoleService.findRoleCodesByUserId(userId);
            ZKSysUserCacheUtils.putRoleCodes(userId, roleCodes);
        }
        return roleCodes;
    }

    // 取用户权限代码；先从缓存中取，如果缓存中不存在；再查数据库，并设置缓存；
    public Set<String> getAuthCodesByUserId(String userId) {
        return this.getAuthCodesByUser(this.getUserById(userId));
    }

    public Set<String> getAuthCodesByUser(ZKUser<String> user) {
        if (user == null) {
            return Collections.emptySet();
        }
        Set<String> authCodes = ZKSysUserCacheUtils.getAuthCodes(user.getPkId());
        if (authCodes == null) {
            authCodes = new HashSet<String>();
            for (ZKAuthChannel authChannel : ZKAuthChannel.values()) {
                switch (authChannel) {
                    case Dept:
                        if (this.isOpenAuth(authChannel)) {
                            authCodes.addAll(this.sysAuthDeptService.findAuthCodesByDeptId(user.getDeptId()));
                        }
                        break;
                    case User:
                        if (this.isOpenAuth(authChannel)) {
                            authCodes.addAll(this.sysAuthUserService.findAuthCodesByUserId(user.getPkId()));
                        }
                        break;
                    case Role:
                        if (this.isOpenAuth(authChannel)) {
                            authCodes.addAll(this.sysAuthUserRoleService.findAuthCodesByUserId(user.getPkId()));
                        }
                        break;
                    case Rank:
                        if (this.isOpenAuth(authChannel)) {
                            authCodes.addAll(this.sysAuthRankService.findAuthCodesByRankId(user.getRankId()));
                        }
                        break;
                    case UserType:
                        if (this.isOpenAuth(authChannel)) {
                            authCodes.addAll(
                                    this.sysAuthUserTypeService.findAuthCodesByUserTypeId(user.getUserTypeId()));
                        }
                        break;
                }
            }
            ZKSysUserCacheUtils.putAuthCodes(user.getPkId(), authCodes);
        }
        return authCodes;
    }

    // 取用户API接口代码；先从缓存中取，如果缓存中不存在；再查数据库，并设置缓存；
    public Set<String> getApiCodesByUserId(String userId, String systemCode) {
        return this.getApiCodesByUser(this.getUserById(userId), systemCode);
    }

    public Set<String> getApiCodesByUser(ZKUser<String> user, String systemCode) {
        if (user == null || ZKStringUtils.isEmpty(systemCode)) {
            return Collections.emptySet();
        }
        Set<String> apiCodes = ZKSysUserCacheUtils.getApiCodes(user.getPkId(), systemCode);
        if (apiCodes == null) {
            apiCodes = new HashSet<>();
            for (ZKAuthChannel authChannel : ZKAuthChannel.values()) {
                switch (authChannel) {
                    case Dept:
                        if (this.isOpenAuth(authChannel)) {
                            apiCodes.addAll(this.sysAuthDeptService.findApiCodesByDeptId(user.getDeptId(), systemCode));
                        }
                        break;
                    case User:
                        if (this.isOpenAuth(authChannel)) {
                            apiCodes.addAll(this.sysAuthUserService.findApiCodesByUserId(user.getPkId(), systemCode));
                        }
                        break;
                    case Role:
                        if (this.isOpenAuth(authChannel)) {
                            apiCodes.addAll(
                                    this.sysAuthUserRoleService.findApiCodesByUserId(user.getPkId(), systemCode));
                        }
                        break;
                    case Rank:
                        if (this.isOpenAuth(authChannel)) {
                            apiCodes.addAll(this.sysAuthRankService.findApiCodesByRankId(user.getRankId(), systemCode));
                        }
                        break;
                    case UserType:
                        if (this.isOpenAuth(authChannel)) {
                            apiCodes.addAll(this.sysAuthUserTypeService.findApiCodesByUserTypeId(user.getUserTypeId(),
                                    systemCode));
                        }
                        break;
                }
            }
            ZKSysUserCacheUtils.putApiCodes(user.getPkId(), systemCode, apiCodes);
        }
        return apiCodes;
    }

    // 取公司的权限代码；先从缓存中取，如果缓存中不存在；再查数据库，并设置缓存；
    public Set<String> getAutchCodesByCompanyId(String companyId) {
        if (ZKStringUtils.isEmpty(companyId)) {
            return Collections.emptySet();
        }
        Set<String> autchCodes = ZKSysCompanyCacheUtils.getAuthCodes(companyId);
        if (autchCodes == null) {
            autchCodes = this.sysAuthCompanyService.findAuthCodesByCompanyId(companyId);
            ZKSysCompanyCacheUtils.putAuthCodes(companyId, autchCodes);
        }
        return autchCodes;
    }

    // 取公司的Api接口代码；先从缓存中取，如果缓存中不存在；再查数据库，并设置缓存；
    public Set<String> getApiCodesByCompanyId(String companyId, String systemCode) {
        if (ZKStringUtils.isEmpty(companyId) || ZKStringUtils.isEmpty(systemCode)) {
            return Collections.emptySet();
        }
        Set<String> apiCodes = ZKSysCompanyCacheUtils.getAuthCodes(companyId);
        if (apiCodes == null) {
            apiCodes = this.sysAuthCompanyService.findApiCodesByCompanyId(companyId, systemCode);
            ZKSysCompanyCacheUtils.putApiCodes(companyId, systemCode, apiCodes);
        }
        return apiCodes;
    }

    /*************************************************************************************/

    public boolean isOpenAuth(ZKAuthChannel authChannel) {
        return this.sysSetItemService.getByCode(ZKSysSecConstants.Key_Auth_Strategy, authChannel.channel())
                .getBooleanValue();
    }

    public boolean isAdmin(ZKSysOrgUser user, Set<String> roleCodes, Set<String> authCodes) {
        // 判断账号是不是 admin;
        if (ZKSysSecConstants.KeyAuth.adminAccount.equals(user.getAccount())) {
            return true;
        }
        // 判断有没有超级管理员角色
        if (roleCodes.contains(ZKSysSecConstants.KeyAuth.adminRoleCode)) {
            return true;
        }
        // 判断有没有 admin 权限代码
        if (authCodes.contains(ZKSysSecConstants.KeyAuth.adminAuthCode)) {
            return true;
        }
        return false;
    }

    /*************************************************************************************/
//    /******************************************************/
//    // 通过部门拥有的 API 接口代码
//    protected List<String> findDeptApiCodes(ZKUser<String> user, String systemCode) {
//        return this.sysAuthDeptService.findApiCodesByDeptId(user.getDeptId(), systemCode);
//    }
//
//    // 通过用户直接拥有的 API 接口代码
//    protected List<String> findUserApiCodes(ZKUser<String> user, String systemCode) {
//        return this.sysAuthUserService.findApiCodesByUserId(user.getPkId(), systemCode);
//    }
//
//    // 通过用户类型拥有的 API 接口代码
//    protected List<String> findUserTypeApiCodes(ZKUser<String> user, String systemCode) {
//        return this.sysAuthUserTypeService.findApiCodesByUserTypeId(user.getUserTypeId(), systemCode);
//    }
//
//    // 通过职级拥有的 API 接口代码
//    protected List<String> findRankApiCodes(ZKUser<String> user, String systemCode) {
//        return this.sysAuthRankService.findApiCodesByRankId(user.getRankId(), systemCode);
//    }
//
//    // 通过角色拥有的 API 接口代码
//    protected List<String> findRoleApiCodes(ZKUser<String> user, String systemCode) {
//        return this.sysAuthUserRoleService.findApiCodesByUserId(user.getPkId(), systemCode);
//    }

//    /** 取用户权限代码 *********************************************************/
//    // 通过部门拥有的权限代码
//    protected List<String> findDeptAuthCodes(ZKUser<String> user) {
//        return this.sysAuthDeptService.findAuthCodesByDeptId(user.getDeptId());
//    }
//
//    // 通过用户拥有的权限代码
//    protected List<String> findUserAuthCodes(ZKUser<String> user) {
//        return this.sysAuthUserService.findAuthCodesByUserId(user.getPkId());
//    }
//
//    // 通过用户类型拥有的权限代码
//    protected List<String> findUserTypeAuthCodes(ZKUser<String> user) {
//        return this.sysAuthUserTypeService.findAuthCodesByUserTypeId(user.getUserTypeId());
//    }
//
//    // 通过职级拥有的权限代码
//    protected List<String> findRankAuthCodes(ZKUser<String> user) {
//        return this.sysAuthRankService.findAuthCodesByRankId(user.getRankId());
//    }
//
//    // 通过角色拥有的权限代码
//    protected List<String> findRoleAuthCodes(ZKUser<String> user) {
//        return this.sysAuthUserRoleService.findAuthCodesByUserId(user.getPkId());
//    }

    /** 取用户权限ID *********************************************************/
//    // 通过部门拥有的权限代码
//    protected List<String> findDeptAuthIds(ZKUser<String> user) {
//        return this.sysAuthDeptService.findAuthIdsByDeptId(user.getDeptId());
//    }
//
//    // 通过用户直接拥有的权限代码
//    protected List<String> findUserAuthIds(ZKUser<String> user) {
//        return this.sysAuthUserService.findAuthIdsByUserId(user.getPkId());
//    }
//
//    // 通过用户类型拥有的权限代码
//    protected List<String> findUserTypeAuthIds(ZKUser<String> user) {
//        return this.sysAuthUserTypeService.findAuthIdsByUserTypeId(user.getUserTypeId());
//    }
//
//    // 通过职级拥有的权限代码
//    protected List<String> findRankAuthIds(ZKUser<String> user) {
//        return this.sysAuthRankService.findAuthIdsByRankId(user.getRankId());
//    }
//
//    // 通过角色拥有的权限代码
//    protected List<String> findRoleAuthIds(ZKUser<String> user) {
//        return this.sysAuthUserRoleService.findAuthIdsByUserId(user.getPkId());
//    }

    // 取用户所拥有的 权限ID
    public List<String> getAuthIdsByUserId(ZKSysOrgUser user) {
        return this.getAuthIdsByUserId(user, ZKAuthChannel.values());
    }

    public List<String> getAuthIdsByUserId(ZKSysOrgUser user, ZKAuthChannel... authChannels) {
        List<String> authIds = new ArrayList<>();
        for (ZKAuthChannel authChannel : authChannels) {
            switch (authChannel) {
                case Dept:
                    if (this.isOpenAuth(authChannel)) {
                        authIds.addAll(this.sysAuthDeptService.findAuthIdsByDeptId(user.getDeptId()));
                    }
                    break;
                case User:
                    if (this.isOpenAuth(authChannel)) {
                        authIds.addAll(this.sysAuthUserService.findAuthIdsByUserId(user.getPkId()));
                    }
                    break;
                case Role:
                    if (this.isOpenAuth(authChannel)) {
                        authIds.addAll(this.sysAuthUserRoleService.findAuthIdsByUserId(user.getPkId()));
                    }
                    break;
                case Rank:
                    if (this.isOpenAuth(authChannel)) {
                        authIds.addAll(this.sysAuthRankService.findAuthIdsByRankId(user.getRankId()));
                    }
                    break;
                case UserType:
                    if (this.isOpenAuth(authChannel)) {
                        authIds.addAll(this.sysAuthUserTypeService.findAuthIdsByUserTypeId(user.getUserTypeId()));
                    }
                    break;
            }
        }
        return authIds;
    }

}
