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
* @Title: ZKSecDistributedAuthService.java 
* @author Vinson 
* @Package com.zk.framework.security.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 22, 2024 9:32:58 PM 
* @version V1.0 
*/
package com.zk.framework.security.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.exception.ZKSecAuthorizationException;
import com.zk.framework.security.userdetails.ZKUser;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.security.authz.ZKSecAuthorizationInfo;
import com.zk.sys.org.api.ZKSysOrgUserApi;
import com.zk.sys.sec.api.ZKSysSecAuthcApi;

/** 
* @ClassName: ZKSecDistributedAuthService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecDistributedAuthService implements ZKSecAuthService<String> {
    
    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    @Autowired
    ZKSysSecAuthcApi sysSecAuthcApi;

    @Autowired
    ZKSysOrgUserApi sysOrgUserApi;

    // 先从缓存中取，如果缓存中不存在；再调用system服务，并设置缓存；
    @Override
    public ZKUser<String> getUserById(String userId) {
        ZKUser<String> user = ZKUserCacheUtils.getUser(userId);
        if (user == null) {
            ZKMsgRes res = sysOrgUserApi.getUserByPkId(userId);
            if (res.isOk()) {
                user = res.getData();
            }
            else {
                throw ZKSecAuthorizationException.asMsg(res.getCode(), res.getMsg(), res.getData(), (Object[]) null);
            }
        }
        ZKUserCacheUtils.putUser(userId, user);
        return user;
    }

    // // 先从缓存中取，如果缓存中不存在；再调用system服务，并设置缓存；
    @Override
    public ZKSecAuthorizationInfo getUserAuthInfo(String userId, String systemCode) {
        ZKSecAuthorizationInfo authInfo = ZKUserCacheUtils.getAuthInfo(userId, systemCode);
        if(authInfo == null) {
            ZKMsgRes res = this.sysSecAuthcApi.getUserAuthc(systemCode);
            if (res.isOk()) {
                authInfo = res.getData();
            }
            else {
                throw ZKSecAuthorizationException.asMsg(res.getCode(), res.getMsg(), res.getData(), (Object[]) null);
            }
        }
        ZKUserCacheUtils.putAuthInfo(userId, systemCode, authInfo);
        return authInfo;
    }

}
