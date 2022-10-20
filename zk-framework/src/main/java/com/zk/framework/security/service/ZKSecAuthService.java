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
* @Title: ZKSecAuthService.java 
* @author Vinson 
* @Package com.zk.security.service 
* @Description: TODO(simple description this file what to do. ) 
* @date May 10, 2022 10:16:25 AM 
* @version V1.0 
*/
package com.zk.framework.security.service;

import java.util.List;

import com.zk.framework.security.ZKAuthPermission;
import com.zk.framework.security.userdetails.ZKUser;

/**
 * @ClassName: ZKSecAuthService
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public interface ZKSecAuthService<ID> {

    /**
     * 权限渠道枚举类型
     * 
     * @ClassName: ZKAuthChannel
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static enum ZKAuthChannel {
        // 通过部门获取的权限
        Dept,
        // 用户直接获取的权限
        User,
        // 通过角色获取的权限
        Role,
        // 通过职级获取的权限
        Rank,
        // 通过用户类型获取的权限
        UserType
    }

    /**
     * 取用户实体
     *
     * @Title: getByUserId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:25:40 AM
     * @param userId
     * @return ZKUser<ID>
     */
    public ZKUser<ID> getByUserId(ID userId);

    /**
     * 取用户API 接口代码
     *
     * @Title: getByAuthUserId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:25:44 AM
     * @param user
     * @return ZKAuthPermission
     */
    public ZKAuthPermission getByAuthUser(ZKUser<ID> user);

    /**
     * 更新指定渠道的权限代码和API接口代码
     *
     * @Title: getByAuthUser
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 11, 2022 11:50:03 AM
     * @param user
     * @param authPermission
     * @param authChannel
     * @return ZKAuthPermission
     */
    public ZKAuthPermission getByAuthUser(ZKUser<ID> user, ZKAuthPermission authPermission, ZKAuthChannel authChannel);

    /**
     * 取用户角色代码集合
     *
     * @Title: getByRoleCodesUserId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:25:48 AM
     * @param userId
     * @return List<String>
     */
    public List<String> getByRoleCodesUserId(ID userId);

}
