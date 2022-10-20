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
* @Title: ZKAuthPermission.java 
* @author Vinson 
* @Package com.zk.framework.security 
* @Description: TODO(simple description this file what to do. ) 
* @date May 10, 2022 10:11:07 AM 
* @version V1.0 
*/
package com.zk.framework.security;

import java.io.Serializable;
import java.util.Set;

/**
 * 权限记录实体，按渠道分开记录，以控制权限开启渠道
 * 
 * @ClassName: ZKAuthPermission
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public interface ZKAuthPermission extends Serializable {

    /*** Api 接口代码 *****************************************************************/
    /**
     * 取部门的ApiCode权限集合
     *
     * @Title: getApiCodeByDept
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:14:17 AM
     * @return Set<String> 返回为可空，但不要为 null
     */
    Set<String> getApiCodeByDept();

    /**
     * 取角色的ApiCode权限集合
     *
     * @Title: getApiCodeByRole
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:14:21 AM
     * @return Set<String> 返回为可空，但不要为 null
     */
    Set<String> getApiCodeByRole();

    /**
     * 取用户的ApiCode权限集合
     *
     * @Title: getApiCodeByUser
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:14:25 AM
     * @return Set<String> 返回为可空，但不要为 null
     */
    Set<String> getApiCodeByUser();

    /**
     * 取职级的ApiCode权限集合
     *
     * @Title: getApiCodeByRank
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:14:28 AM
     * @return Set<String> 返回为可空，但不要为 null
     */
    Set<String> getApiCodeByRank();

    /**
     * 取用户类型的ApiCode权限集合
     *
     * @Title: getApiCodeByUserType
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 10:14:31 AM
     * @return Set<String> 返回为可空，但不要为 null
     */
    Set<String> getApiCodeByUserType();

    /*** 权限代码 *****************************************************************/
    /**
     * 取部门的 AuthCode 权限集合
     *
     * @Title: getAuthCodeByDept
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 3:20:47 PM
     * @return
     * @return Set<String> 返回为可空，但不要为 null
     */
    Set<String> getAuthCodeByDept();

    /**
     * 取角色的 AuthCode 权限集合
     *
     * @Title: getAuthCodeByRole
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 3:20:57 PM
     * @return
     * @return Set<String> 返回为可空，但不要为 null
     */
    Set<String> getAuthCodeByRole();

    /**
     * 取用户的 AuthCode 权限集合
     *
     * @Title: getAuthCodeByUser
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 3:21:02 PM
     * @return
     * @return Set<String> 返回为可空，但不要为 null
     */
    Set<String> getAuthCodeByUser();

    /**
     * 取职级的 AuthCode 权限集合
     *
     * @Title: getAuthCodeByRank
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 3:21:07 PM
     * @return
     * @return Set<String> 返回为可空，但不要为 null
     */
    Set<String> getAuthCodeByRank();

    /**
     * 取用户类型的 AuthCode 权限集合
     *
     * @Title: getAuthCodeByUserType
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 10, 2022 3:21:12 PM
     * @return
     * @return Set<String> 返回为可空，但不要为 null
     */
    Set<String> getAuthCodeByUserType();

}
