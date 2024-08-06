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
 * @Title: ZKSerCenBaseController.java 
 * @author Vinson 
 * @Package com.zk.server.central.controller.base 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:12:10 PM 
 * @version V1.0   
*/
package com.zk.server.central.controller.base;

import java.util.MissingResourceException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.utils.ZKJsonUtils;

/** 
* @ClassName: ZKSerCenBaseController 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSerCenBaseController extends ZKBaseController {

    @Value("${zk.path.admin}")
    protected String adminPath;

    @Value("${zk.path.serCen}")
    protected String modulePath;

    /**
     * 参数绑定异常
     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ Exception.class })
    public String defaultException(Exception e) {
        System.out.println("[>_<:20190826-1150-001] default exception: " + ZKJsonUtils.toJsonStr(e));
        return "error/default";
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ MissingResourceException.class })
    public ModelAndView MissingResourceException(MissingResourceException errMsg) {
        ModelAndView mv = new ModelAndView("error/missingResource");
        mv.addObject("errMsg", errMsg);

        System.out.println("[>_<:20190826-1150-002] missingResource exception: " + ZKJsonUtils.toJsonStr(errMsg));

        return mv;
    }

}
