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
* @Title: ZKSysOrgUserApi.java 
* @author Vinson 
* @Package com.zk.sys.org.openFeign 
* @Description: TODO(simple description this file what to do. ) 
* @date May 12, 2022 11:20:12 AM 
* @version V1.0 
*/
package com.zk.sys.api.org;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zk.core.web.ZKMsgRes;
import com.zk.sys.common.ZKSysApiConstants;

/**
 * @ClassName: ZKSysOrgUserApi
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@FeignClient(name = ZKSysApiConstants.YunJian_App_Name, contextId = "com.zk.sys.api.org.ZKSysOrgUserApi")
public interface ZKSysOrgUserApi {

    // 根据用户ID查询用户详情
    @RequestMapping(method = RequestMethod.GET, path = ZKSysApiConstants.YunJian_Api_Prefix
            + "/org/sysOrgUser/sysOrgUser")
    ZKMsgRes getUserByPkId(@RequestParam("pkId") String pkId);

    // 根据用户公司代码和手机号友查询用户详情
    @RequestMapping(method = RequestMethod.GET, path = ZKSysApiConstants.YunJian_Api_Prefix
            + "/org/sysOrgUser/sysOrgUserByPhoneNum")
    ZKMsgRes getUserByPhnoeNum(@RequestParam("companyId") String companyId, @RequestParam("phoneNum") String phoneNum);

}
