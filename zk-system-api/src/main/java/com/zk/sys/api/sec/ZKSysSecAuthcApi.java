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
* @Title: ZKSysSecAuthcApi.java 
* @author Vinson 
* @Package com.zk.sys.sec.api 
* @Description: TODO(simple description this file what to do. ) 
* @date May 17, 2022 10:29:52 AM 
* @version V1.0 
*/
package com.zk.sys.api.sec;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zk.core.web.ZKMsgRes;
import com.zk.sys.common.ZKSysApiConstants;

/** 
* @ClassName: ZKSysSecAuthcApi 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@FeignClient(name = ZKSysApiConstants.YunJian_App_Name, contextId = "com.zk.sys.api.sec.ZKSysSecAuthcApi")
public interface ZKSysSecAuthcApi {

    // 根据用户ID查询用户详情
    @RequestMapping(method = RequestMethod.GET, path = ZKSysApiConstants.YunJian_Api_Prefix + "/sec/authc/getUserAuthc")
    ZKMsgRes getUserAuthc();

}
