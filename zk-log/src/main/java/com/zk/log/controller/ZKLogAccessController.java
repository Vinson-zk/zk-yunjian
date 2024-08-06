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
* @Title: ZKLogAccessController.java 
* @author Vinson 
* @Package com.zk.log.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 14, 2022 11:10:29 AM 
* @version V1.0 
*/
package com.zk.log.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.log.entity.ZKLogAccess;
import com.zk.log.service.ZKLogAccessService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** 
* @ClassName: ZKLogAccessController 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.log}/${zk.log.version}/log")
public class ZKLogAccessController extends ZKBaseController {

    @Autowired
    ZKLogAccessService logAccessService;

    // 分页查询
    @RequestMapping(value = "logsPage", method = RequestMethod.GET)
    public ZKMsgRes logsPage(ZKLogAccess logAccess, HttpServletRequest hReq, HttpServletResponse hRes) {
        ZKPage<ZKLogAccess> resPage = ZKPage.asPage(hReq);
        resPage = this.logAccessService.findPage(resPage, logAccess);
        return ZKMsgRes.asOk(null, resPage);
    }

}
