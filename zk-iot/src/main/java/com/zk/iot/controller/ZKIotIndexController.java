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
* @Title: ZKIotIndexController.java 
* @author Vinson 
* @Package com.zk.iot.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Dec 31, 2024 4:29:55 PM 
* @version V1.0 
*/
package com.zk.iot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.web.utils.ZKWebUtils;

/** 
* @ClassName: ZKIotIndexController 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RestController
//@RequestMapping("${zk.path.admin}/${zk.path.iot}/${zk.iot.version}")
@RequestMapping("/")
public class ZKIotIndexController {

    @RequestMapping({ "", "index" })
    public ZKMsgRes welcome() {
        ZKMsgRes res = ZKMsgRes.asOk("index: " + ZKMsgUtils.getMessage(ZKWebUtils.getLocale(), "zk.iot.msg.welcome")
                + " -> " + ZKEnvironmentUtils.getString("spring.application.name", "zk iot"));
        return res;
    }

}
