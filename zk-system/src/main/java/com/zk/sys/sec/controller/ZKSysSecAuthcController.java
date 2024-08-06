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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.core.commons.ZKMsgRes;
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
    protected Logger log = LogManager.getLogger(getClass());

    @Autowired
    ZKSysOrgCompanyService orgCompanyService;

    @Autowired
    ZKSysSecAuthService sysSecAuthcService;

    /**
     * 查询当前使用用户，指定应用的权限
    *
    * @Title: getUserAuthc 
    * @Description: TODO(simple description this method what to do.) 
    * @author Vinson 
    * @date Jun 19, 2024 5:08:28 PM 
    * @param systemCode
    * @return
    * @return ZKMsgRes
     */
    @RequestMapping(value = "getUserAuthc", method = RequestMethod.GET)
    public ZKMsgRes getUserAuthc(@RequestParam("systemCode")String systemCode) {
        String userId = ZKSecSecurityUtils.getUserId();
        ZKSecAuthorizationInfo authc = sysSecAuthcService.getUserAuthInfo(userId, systemCode);
        return ZKMsgRes.asOk(null, authc);
    }

}
