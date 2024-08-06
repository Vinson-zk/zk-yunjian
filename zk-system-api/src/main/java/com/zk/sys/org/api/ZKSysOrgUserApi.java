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
* @Title: ZKSysOrgUserApi.java 
* @author Vinson 
* @Package com.zk.sys.org.api 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 19, 2024 5:30:07 PM 
* @version V1.0 
*/
package com.zk.sys.org.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zk.core.commons.ZKMsgRes;
import com.zk.sys.common.ZKSysApiConstants;

/** 
* @ClassName: ZKSysOrgUserApi 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@FeignClient(name = ZKSysApiConstants.YunJian_App_Name, contextId = "com.zk.sys.org.api.ZKSysOrgUserApi")
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
