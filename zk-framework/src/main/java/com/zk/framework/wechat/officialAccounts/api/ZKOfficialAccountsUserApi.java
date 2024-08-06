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
* @Title: ZKOfficialAccountsUserApi.java 
* @author Vinson 
* @Package com.zk.framework.wechat.officialAccounts.api 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 6, 2022 8:23:41 AM 
* @version V1.0 
*/
package com.zk.framework.wechat.officialAccounts.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zk.core.commons.ZKMsgRes;
import com.zk.framework.common.ZKApiConstants.YunJian_App;

/** 
* @ClassName: ZKOfficialAccountsUserApi 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@FeignClient(name = YunJian_App.wechat.name, contextId = "com.zk.framework.wechat.officialAccounts.api.ZKOfficialAccountsUserApi")
public interface ZKOfficialAccountsUserApi {

    @RequestMapping(method = RequestMethod.GET, path = YunJian_App.wechat.apiPrefix
            + "/officialAccounts/officialAccountsUser/officialAccountsUser")
    ZKMsgRes getWxUser(@RequestParam("pkId") String pkId);

}
