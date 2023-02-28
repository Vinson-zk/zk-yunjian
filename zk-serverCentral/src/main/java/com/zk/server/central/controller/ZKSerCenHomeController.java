/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKSerCenHomeController.java 
 * @author Vinson 
 * @Package com.zk.server.central.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:18:23 PM 
 * @version V1.0   
*/
package com.zk.server.central.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.zk.server.central.controller.base.ZKSerCenBaseController;

/** 
* @ClassName: ZKSerCenHomeController 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@Controller
@RequestMapping("${zk.path.admin}/${zk.path.serCen}/home")
public class ZKSerCenHomeController extends ZKSerCenBaseController {

    // 服务中心的功能主页
    @RequestMapping("serCenIndex")
    public ModelAndView scIndex() {
        ModelAndView mv = new ModelAndView("/modules/serCen/serCenIndex");
        return mv;
    }

    // 指引页
    @RequestMapping("scGuide")
    public ModelAndView scGuide() {
        ModelAndView mv = new ModelAndView("/modules/scGuide");
        return mv;
    }

}
