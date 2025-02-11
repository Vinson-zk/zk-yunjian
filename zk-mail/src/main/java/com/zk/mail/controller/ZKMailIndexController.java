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
* @Title: ZKMailSendController.java 
* @author Vinson 
* @Package com.zk.mail.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date May 26, 2022 7:08:47 PM 
* @version V1.0 
*/
package com.zk.mail.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.web.utils.ZKWebUtils;

/** 
* @ClassName: ZKMailSendController 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RestController
//@RequestMapping(value = "${zk.path.admin}/${zk.path.mail}/${zk.mail.version}")
@RequestMapping("/")
public class ZKMailIndexController extends ZKBaseController {

    @RequestMapping({ "", "index" })
    public ZKMsgRes welcome() {
        return ZKMsgRes.asOk(null,
                "index: " + ZKMsgUtils.getMessage(ZKWebUtils.getLocale(), "zk.mail.msg.welcome") + " "
                + ZKEnvironmentUtils.getString("spring.application.name", "zk mail"));
    }

    @RequestMapping({ "user" })
    public ZKMsgRes user() {
        return ZKMsgRes.asOk(null, "user: " + ZKMsgUtils.getMessage(ZKWebUtils.getLocale(), "zk.mail.msg.welcome") + " "
                + ZKEnvironmentUtils.getString("spring.application.name", "zk mail"));
    }

}
