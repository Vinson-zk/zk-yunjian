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
* @Title: ZKWXIndexController.java 
* @author Vinson 
* @Package com.zk.wechat.wx.officialAccounts.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Nov 1, 2021 3:45:13 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.officialAccounts.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 使用开放平台，不使用公众号直连
 * 
 * @ClassName: ZKWXIndexController
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@RestController
@RequestMapping("${zk.path.admin}/${zk.path.wechat}/${zk.wechat.version}/${zk.path.wechat.wx.officialAccounts}")
public class ZKWXIndexController {

    // 公众号验证 Token
    @RequestMapping(value = "token")
    public void weixin(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws Exception {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=utf-8");

        String rStr = "";
//        String method = req.getMethod(); // 大写字符串

        //

        resp.getWriter().write(rStr);
    }

}
