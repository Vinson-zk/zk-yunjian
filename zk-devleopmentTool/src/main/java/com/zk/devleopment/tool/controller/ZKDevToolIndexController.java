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
* @Title: ZKDevToolIndexController.java 
* @author Vinson 
* @Package com.zk.code.generate.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 30, 2021 11:51:21 AM 
* @version V1.0 
*/
package com.zk.devleopment.tool.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.web.utils.ZKWebUtils;

/**
 * @ClassName: ZKDevToolIndexController
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@RestController
@RequestMapping("${zk.path.admin}/${zk.path.dev.tool}/${zk.dev.tool.version}")
public class ZKDevToolIndexController {

    @RequestMapping({ "", "index" })
//  @ResponseBody
    public String welcome() {
        return "index: " + ZKMsgUtils.getMessage(ZKWebUtils.getLocale(), "zk.dev.tool.msg.welcome") + " "
                + ZKEnvironmentUtils.getString("spring.application.name", "zk code Gen");
    }

    @RequestMapping("user")
    public String user() {
        return "user: " + ZKMsgUtils.getMessage(ZKWebUtils.getLocale(), "zk.dev.tool.msg.welcome") + " "
                + ZKEnvironmentUtils.getString("spring.application.name", "zk code Gen");
    }

}
