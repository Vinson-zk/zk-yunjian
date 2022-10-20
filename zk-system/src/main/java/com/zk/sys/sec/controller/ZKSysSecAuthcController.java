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
* @Title: ZKSysSecAuthcController.java 
* @author Vinson 
* @Package com.zk.sys.sec.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date May 17, 2022 9:43:55 AM 
* @version V1.0 
*/
package com.zk.sys.sec.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zk.core.web.ZKMsgRes;
import com.zk.framework.security.ZKAuthPermission;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.security.authz.ZKSecAuthorizationInfo;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.sys.org.service.ZKSysOrgCompanyService;
import com.zk.sys.sec.service.ZKSysSecAuthService;

/**
 * 权限对外接口
 * 
 * @ClassName: ZKSysSecAuthcController
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@RestController
@RequestMapping("${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/sec/authc")
public class ZKSysSecAuthcController {

    /**
     * 日志对象
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    ZKSysOrgCompanyService orgCompanyService;

    @Autowired
    ZKSysSecAuthService sysSecAuthcService;

    /**
     * 查询当前使用用户权限
     *
     * @Title: getUserAuthc
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 17, 2022 10:20:40 AM
     * @return ZKMsgRes
     */
    @RequestMapping(value = "getUserAuthc", method = RequestMethod.GET)
    public ZKMsgRes getUserAuthc() {
        String userId = ZKSecSecurityUtils.getUserId();
        ZKAuthPermission ap = ZKUserCacheUtils.getAuth(userId);
        ZKSecAuthorizationInfo authc = sysSecAuthcService.paseByAuthPermission(ap);
        return ZKMsgRes.asOk(authc);
    }

}
