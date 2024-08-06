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
 * @Title: ZKSerCenInfoController.java 
 * @author Vinson 
 * @Package com.zk.server.central.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Mar 8, 2020 8:39:47 PM 
 * @version V1.0   
*/
package com.zk.server.central.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/** 
* @ClassName: ZKSerCenInfoController 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@Controller
@RequestMapping("${zk.path.admin}/${zk.path.serCen}/info")
public class ZKSerCenInfoController {

    // 服务中心 信息页面
    @RequestMapping("serCenInfo")
    public ModelAndView serCenInfo() {
        ModelAndView mv = new ModelAndView("/modules/serCen/serverCentralInfo/serCenInfo");
        return mv;
    }

}
