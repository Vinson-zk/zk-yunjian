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
* @Title: ZKSysSecResService.java 
* @author Vinson 
* @Package com.zk.sys.sec.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 9, 2024 5:52:29 PM 
* @version V1.0 
*/
package com.zk.sys.sec.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.sys.auth.service.ZKSysAuthCompanyService;
import com.zk.sys.auth.service.ZKSysAuthMenuService;
import com.zk.sys.auth.service.ZKSysAuthNavService;
import com.zk.sys.auth.service.ZKSysAuthUserRoleService;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.res.entity.ZKSysMenu;
import com.zk.sys.res.entity.ZKSysNav;
import com.zk.sys.res.service.ZKSysMenuService;
import com.zk.sys.res.service.ZKSysNavService;
import com.zk.sys.sec.common.ZKSysSecConstants;
import com.zk.sys.utils.ZKSysUtils;

/** 
* @ClassName: ZKSysSecResService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
@Transactional(readOnly = true)
public class ZKSysSecResService {

    @Autowired
    ZKSysNavService zkSysNavService;

    @Autowired
    ZKSysMenuService zkSysMenuService;

    @Autowired
    ZKSysSecAuthService zkSysSecAuthService;

    @Autowired
    ZKSysAuthNavService zkSysAuthNavService;

    @Autowired
    ZKSysAuthMenuService zkSysAuthMenuService;

    @Autowired
    ZKSysAuthCompanyService zkSysAuthCompanyService;

    @Autowired
    ZKSysAuthUserRoleService zkSysAuthUserRoleService;

    public interface AuthType {
        // 1. 拥有者公司、用户账号为 admin 或 用户有 superAdmin 角色；返回所有显示的导航栏
        public static final int admin = 1;

        // 2. 普通公司，用户账号为 admin 或 用户有 superAdmin 角色；返回公司所拥有的权限ID
        public static final int companyAdmin = 2;

        // 3. 普通用户，根据用户权限取导航栏
        public static final int generic = 3;

    }

    // 取查询权限的方式
    public int getFindAuthType(ZKSysOrgUser user) {
        // 1. 拥有者公司、用户账号为 admin 或 用户有 superAdmin 角色；返回所有显示的导航栏
        // 2. 普通公司，用户账号为 admin 或 用户有 superAdmin 角色；返回公司所拥有的权限ID
        // 3. 普通用户，根据用户权限取导航栏
        
        // 取平台拥有者公司代码
        String ownerCode = ZKSysUtils.getOwnerCompanyCode();

        if (ZKSysSecConstants.KeyAuth.adminAccount.equals(user.getAccount())) {
            // 判断是不是 拥有者公司
            if (ownerCode.equals(user.getCompanyCode())) {
                return AuthType.admin;
            }
            else {
                return AuthType.companyAdmin;
            }
        }

        // 取用户角色代码
        Set<String> roles = zkSysSecAuthService.getRoleCodesByUserId(user.getPkId());
        if (roles.contains(ZKSysSecConstants.KeyAuth.adminRoleCode)) {
            // 判断是不是 拥有者公司
            if (ownerCode.equals(user.getCompanyCode())) {
                return AuthType.admin;
            }
            else {
                return AuthType.companyAdmin;
            }
        }
        else {
            // 取用户权限ID
            return AuthType.generic;
        }
    }

    // 取用户导航栏
    public List<ZKSysNav> findNavByUser(ZKSysOrgUser user) {

        int findAuthType = this.getFindAuthType(user);
        if (findAuthType == AuthType.admin) {
            ZKSysNav zkSysNav = new ZKSysNav();
            zkSysNav.setIsShow(ZKSysNav.KeyIsShow.show);
            zkSysNav.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);
            return this.zkSysNavService.findList(zkSysNav);
        }

        List<String> authIds = null;
        if (findAuthType == AuthType.companyAdmin) {
            // 取公司权限ID
            authIds = this.zkSysAuthCompanyService.findAuthIdsByCompanyId(user.getCompanyId());
        }
        else {
            // 取用户权限ID
            authIds = this.zkSysSecAuthService.getAuthIdsByUserId(user);
        }
        return this.zkSysAuthNavService.findNavByAuthIds(authIds, ZKSysNav.KeyIsShow.show);
    }

    // 取用户菜单
    public List<ZKSysMenu> findMenuByUser(ZKSysOrgUser user, String navCode) {
        int findAuthType = this.getFindAuthType(user);
        if (findAuthType == AuthType.admin) {
            ZKSysMenu zkSysMenu = new ZKSysMenu();
            zkSysMenu.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);
            zkSysMenu.setNavCode(navCode);
            return this.zkSysMenuService.findList(zkSysMenu);
        }
        List<String> authIds = null;
        if (findAuthType == AuthType.companyAdmin) {
            // 取公司权限ID
            authIds = this.zkSysAuthCompanyService.findAuthIdsByCompanyId(user.getCompanyId());
        }
        else {
            // 取用户权限ID
            authIds = this.zkSysSecAuthService.getAuthIdsByUserId(user);
        }
        return zkSysAuthMenuService.findMenuByAuthIds(navCode, authIds, null);
    }

}

