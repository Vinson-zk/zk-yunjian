/**   
 * Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * ZK-Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with ZK-Vinson. 
 *
 * @Title: ZKSerCenTestController.java 
 * @author Vinson 
 * @Package com.zk.server.central.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Mar 10, 2020 9:33:13 AM 
 * @version V1.0   
*/
package com.zk.server.central.controller;

import java.util.MissingResourceException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.zk.core.utils.ZKExceptionsUtils;

import jakarta.servlet.http.HttpServletRequest;

/** 
* @ClassName: ZKSerCenTestController 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@Controller
@RequestMapping("${zk.path.admin}/${zk.path.serCen}/test")
public class ZKSerCenTestController {

    // jsp
    @RequestMapping
    public String test(HttpServletRequest req, Model model) {
        model.addAttribute("testName", "这是 java 返回的属性");
        req.setAttribute("attribute", "这是 request 属性");

//        ModelAndView mv = new ModelAndView("/test/test");
        return "test/test";
    }

    // template
    @RequestMapping("testFreeMarker")
    public String testFreeMarker(HttpServletRequest req, ModelMap modelMap) {
        modelMap.put("userName", "Vinson 征客");
        return "testFreeMarker";
    }

    // 不存在的页面
    @RequestMapping("notView")
    public String notView(HttpServletRequest req) {
        return "notView";
    }

    @RequestMapping("e")
    public ModelAndView e(HttpServletRequest req) {
        throw ZKExceptionsUtils.unchecked(new MissingResourceException("s1", "ss2", "sss3"));
    }

}
