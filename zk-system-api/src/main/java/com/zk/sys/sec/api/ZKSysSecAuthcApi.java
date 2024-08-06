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
* @Title: ZKSysSecAuthcApi.java 
* @author Vinson 
* @Package com.zk.sys.sec.api 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 19, 2024 5:28:58 PM 
* @version V1.0 
*/
package com.zk.sys.sec.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zk.core.commons.ZKMsgRes;
import com.zk.sys.common.ZKSysApiConstants;

/** 
* @ClassName: ZKSysSecAuthcApi 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@FeignClient(name = ZKSysApiConstants.YunJian_App_Name, contextId = "com.zk.sys.sec.api.ZKSysSecAuthcApi")
public interface ZKSysSecAuthcApi {

//    // 根据用户ID查询用户 权限信息

    // 取当前登录用户 权限信息
    @RequestMapping(method = RequestMethod.GET, path = ZKSysApiConstants.YunJian_Api_Prefix + "/sec/authc/getUserAuthc")
    ZKMsgRes getUserAuthc(@RequestParam("systemCode") String systemCode);

}
