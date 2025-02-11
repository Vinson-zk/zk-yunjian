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
* @Title: ZKFileIndexController.java 
* @author Vinson 
* @Package com.zk.file.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 21, 2022 11:46:17 AM 
* @version V1.0 
*/
package com.zk.file.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.web.utils.ZKWebUtils;

/** 
* @ClassName: ZKFileIndexController 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RestController
//@RequestMapping("${zk.path.admin}/${zk.path.file}/${zk.file.version}")
@RequestMapping("/")
public class ZKFileIndexController {

    @RequestMapping({ "", "index" })
    public ZKMsgRes index() {
        return ZKMsgRes.asOk(null,
                "index: " + ZKMsgUtils.getMessage(ZKWebUtils.getLocale(), "zk.file.msg.welcome") + " "
                + ZKEnvironmentUtils.getString("spring.application.name", "zk file"));
    }

    @RequestMapping("user")
    public ZKMsgRes user() {
        return ZKMsgRes.asOk(null, "user: " + ZKMsgUtils.getMessage(ZKWebUtils.getLocale(), "zk.file.msg.welcome") + " "
                + ZKEnvironmentUtils.getString("spring.application.name", "zk file"));
    }

}
