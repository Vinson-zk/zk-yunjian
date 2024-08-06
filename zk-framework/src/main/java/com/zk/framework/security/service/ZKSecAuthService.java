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
* @Title: ZKSecAuthService.java 
* @author Vinson 
* @Package com.zk.framework.security.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 19, 2024 11:08:10 PM 
* @version V1.0 
*/
package com.zk.framework.security.service;

import com.zk.framework.security.userdetails.ZKUser;
import com.zk.security.authz.ZKSecAuthorizationInfo;

/** 
* @ClassName: ZKSecAuthService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecAuthService<ID> {

    /**
     * 根据用户ID 取用户实体
     *
     * @Title: getUserById
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 18, 2024 4:53:35 PM
     * @param userId
     * @return
     * @return ZKUser<ID>
     */
    public ZKUser<ID> getUserById(ID userId);

    public ZKSecAuthorizationInfo getUserAuthInfo(ID userId, String systemCode);

}
