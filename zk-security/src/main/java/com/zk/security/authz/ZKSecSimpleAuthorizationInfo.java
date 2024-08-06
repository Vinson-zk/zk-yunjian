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
* @Title: ZKSecSimpleAuthorizationInfo.java 
* @author Vinson 
* @Package com.zk.security.authz 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 13, 2021 2:50:04 PM 
* @version V1.0 
*/
package com.zk.security.authz;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** 
* @ClassName: ZKSecSimpleAuthorizationInfo 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecSimpleAuthorizationInfo implements ZKSecAuthorizationInfo {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 2649735532521497365L;

    private Set<String> mCompanyApiCodes;

    private Set<String> mCompanyAuthCodes;

    private Set<String> mApiCodes;

    private Set<String> mAuthCodes;

    private Set<String> mRoleCodes;

    private boolean isOwnerCompany;

    private boolean isAdmin;

    // API 接口代码
    private Set<String> getApiCodeStore() {
        if (mApiCodes == null) {
            mApiCodes = new HashSet<String>();
        }
        return mApiCodes;
    }

    public void addApiCode(Collection<String> apiCodes) {
        if (apiCodes == null || apiCodes.isEmpty()) {
            return;
        }
        this.getApiCodeStore().addAll(apiCodes);
        this.mApiCodes = Collections.unmodifiableSet(this.mApiCodes);
    }

    @Override
    public Set<String> getApiCodes() {
        if (mApiCodes == null) {
            return Collections.emptySet();
        }
        else {
            return mApiCodes;
        }
    }

    // 权限代码
    private Set<String> getAuthCodeStore() {
        if (mAuthCodes == null) {
            mAuthCodes = new HashSet<String>();
        }
        return mAuthCodes;
    }

    public void addAuthCode(Collection<String> authCodes) {
        if (authCodes == null || authCodes.isEmpty()) {
            return;
        }
        this.getAuthCodeStore().addAll(authCodes);
        this.mAuthCodes = Collections.unmodifiableSet(this.mAuthCodes);
    }

    @Override
    public Set<String> getAuthCodes() {
        if (mAuthCodes == null) {
            return Collections.emptySet();
        }
        else {
            return mAuthCodes;
        }
    }

    // 角色代码
    private Set<String> getRoleCodeStore() {
        if (mRoleCodes == null) {
            mRoleCodes = new HashSet<String>();
        }
        return mRoleCodes;
    }

    public void addRoleCode(Collection<String> roleCodes) {
        if (roleCodes == null || roleCodes.isEmpty()) {
            return;
        }
        this.getRoleCodeStore().addAll(roleCodes);
        this.mRoleCodes = Collections.unmodifiableSet(this.mRoleCodes);
    }

    @Override
    public Set<String> getRoleCodes() {
        if (mRoleCodes == null) {
            return Collections.emptySet();
        }
        else {
            return mRoleCodes;
        }
    }

    // 公司拥有的 接口代码
    private Set<String> getCompanyApiCodeStore() {
        if (mCompanyApiCodes == null) {
            mCompanyApiCodes = new HashSet<String>();
        }
        return mCompanyApiCodes;
    }

    public void addCompanyApiCode(Collection<String> apiCodes) {
        if (apiCodes == null || apiCodes.isEmpty()) {
            return;
        }
        this.getCompanyApiCodeStore().addAll(apiCodes);
        this.mCompanyApiCodes = Collections.unmodifiableSet(this.mCompanyApiCodes);
    }
    @Override
    public Set<String> getCompanyApiCodes() {
        if (mCompanyApiCodes == null) {
            return Collections.emptySet();
        }
        else {
            return mCompanyApiCodes;
        }
    }

    // 公司拥有的 权限代码
    private Set<String> getCompanyAuthCodeStore() {
        if (mCompanyAuthCodes == null) {
            mCompanyAuthCodes = new HashSet<String>();
        }
        return mCompanyAuthCodes;
    }

    public void addCompanyAuthCode(Collection<String> authCodes) {
        if (authCodes == null || authCodes.isEmpty()) {
            return;
        }
        this.getCompanyAuthCodeStore().addAll(authCodes);
        this.mCompanyAuthCodes = Collections.unmodifiableSet(this.mCompanyAuthCodes);
    }
    @Override
    public Set<String> getCompanyAuthCodes() {
        if (mCompanyAuthCodes == null) {
            return Collections.emptySet();
        }
        else {
            return mCompanyAuthCodes;
        }
    }

    public void setIsOwnerCompany(boolean isOwnerCompany) {
        this.isOwnerCompany = isOwnerCompany;
    }

    @Override
    public boolean isOwnerCompany() {
        return this.isOwnerCompany;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public boolean isAdmin() {
        return isAdmin;
    }

}
