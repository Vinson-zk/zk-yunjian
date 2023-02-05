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
 * @Title: ZKBaseHelperController.java 
 * @author Vinson 
 * @Package com.zk.base.helper.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 3:36:09 PM 
 * @version V1.0   
*/
package com.zk.base.helper.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.base.helper.entity.ZKBaseHelperEntityString;
import com.zk.core.utils.ZKJsonUtils;

/** 
* @ClassName: ZKBaseHelperController 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@RestController
@RequestMapping("base/helper")
public class ZKBaseHelperController extends ZKBaseController {

    @RequestMapping("data")
    @ResponseBody
    public ZKBaseHelperEntityString data(ZKBaseHelperEntityString he) {

        System.out.println("[^_^:20190809-1535-001]: " + ZKJsonUtils.writeObjectJson(he));

        return he;

    }

}
