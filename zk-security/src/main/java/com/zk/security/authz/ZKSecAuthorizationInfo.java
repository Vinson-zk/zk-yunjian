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
* @Title: ZKSecAuthorizationInfo.java 
* @author Vinson 
* @Package com.zk.security.authz 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 10, 2021 8:24:32 AM 
* @version V1.0 
*/
package com.zk.security.authz;

import java.io.Serializable;
import java.util.Set;

/** 
* @ClassName: ZKSecAuthorizationInfo 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecAuthorizationInfo extends Serializable {

    // 公司拥有的 Api 接口代码
    Set<String> getCompanyApiCodes();

    // 公司拥有的 权限代码
    Set<String> getCompanyAuthCodes();

    // Api 接口代码
    Set<String> getApiCodes();

    // 权限代码
    Set<String> getAuthCodes();

    // 角色代码
    Set<String> getRoleCodes();

    // 是否是拥有者公司
    boolean isOwnerCompany();

    // 是否是 admin 用户：admin 账号、超级管理员角色、admin 权限 三者之一
    boolean isAdmin();

}
