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
* @Title: ZKZuulIndexController.java 
* @author Vinson 
* @Package com.zk.zuul.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 14, 2022 7:16:11 PM 
* @version V1.0 
*/
package com.zk.zuul.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.web.ZKMsgRes;
import com.zk.core.web.utils.ZKWebUtils;

/** 
* @ClassName: ZKZuulIndexController 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RestController
@RequestMapping("${zk.path.admin}/${zk.path.zuul}/${zk.zuul.version}")
public class ZKZuulIndexController {

    @RequestMapping({ "", "index" })
    @ResponseBody
    public ZKMsgRes welcome() {
        return ZKMsgRes.asOk(ZKMsgUtils.getMessage("zk.zuul.msg.welcome", null, ZKWebUtils.getLocale())
                + ZKEnvironmentUtils.getString("spring.application.name", "zk zuul"));
    }

    @RequestMapping("noZuul")
    public String noZuul() {
        return "noZuul";
    }

}
