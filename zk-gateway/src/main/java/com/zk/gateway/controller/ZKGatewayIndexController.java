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
* @Title: ZKGatewayIndexController.java 
* @author Vinson 
* @Package com.zk.gateway.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 14, 2022 7:16:11 PM 
* @version V1.0 
*/
package com.zk.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.web.utils.ZKWebUtils;

/**
 * @ClassName: ZKGatewayIndexController
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@RestController
//@RequestMapping("${zk.path.admin}/${zk.path.gateway}/${zk.gateway.version}")
@RequestMapping("/")
public class ZKGatewayIndexController {

    @RequestMapping({ "", "index" })
    public ZKMsgRes welcome(@RequestParam(value = "q", required = false) String q) {
        ZKMsgRes res = ZKMsgRes.asOk("index: " + ZKMsgUtils.getMessage(ZKWebUtils.getLocale(), "zk.gateway.msg.welcome")
                + " -> " + ZKEnvironmentUtils.getString("spring.application.name", "zk Gateway") + " -> q: " + q);
        return res;
    }

    @RequestMapping({ "user" })
    public ZKMsgRes user() {
        return ZKMsgRes.asOk(null, "user: " + ZKMsgUtils.getMessage(ZKWebUtils.getLocale(), "zk.gateway.msg.welcome")
                + " " + ZKEnvironmentUtils.getString("spring.application.name", "zk Gateway"));
    }

    @RequestMapping("noGateway")
    @ResponseBody
    public String noGateway() {
        return "noGateway";
    }

}
