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
* @Title: ZKModuleController.java 
* @author Vinson 
* @Package com.zk.code.generate.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 30, 2021 11:42:24 AM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.devleopment.tool.gen.entity.ZKModule;
import com.zk.devleopment.tool.gen.service.ZKModuleService;

import jakarta.servlet.http.HttpServletRequest;

/** 
* @ClassName: ZKModuleController 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RestController
@RequestMapping("${zk.path.admin}/${zk.path.dev.tool}/${zk.dev.tool.version}/${zk.path.dev.tool.code.gen}/fm")
public class ZKModuleController extends ZKBaseController {

    @Autowired
    ZKModuleService zkModuleService;

    // 编辑
    @RequestMapping(value = "module", method = RequestMethod.POST
//            , consumes = "application/json"
    )
    public ZKMsgRes editModule(@RequestBody ZKModule zkModule) {
        this.zkModuleService.save(zkModule);
        return ZKMsgRes.asOk(zkModule);
    }

    /**
     * 批量删除
     *
     * @Title: deleteModule
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 25, 2021 5:03:22 PM
     * @param pkIds
     *            id 数组
     * @return ZKMsgRes
     */
    @RequestMapping(value = "module", method = RequestMethod.DELETE)
    public ZKMsgRes deleteModule(@RequestParam("pkId[]")
    String[] pkIds) {
        int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.zkModuleService.del(new ZKModule(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
    }

    // 详情查询
    @RequestMapping(value = "module", method = RequestMethod.GET)
    public ZKMsgRes getModule(@RequestParam("pkId")
    String pkId) {
        ZKModule zkModule = this.zkModuleService.get(new ZKModule(pkId));
        return ZKMsgRes.asOk(zkModule);
    }

    // 分页列表查询
    @RequestMapping(value = "modules", method = RequestMethod.GET)
    public ZKMsgRes pageModule(ZKModule zkModule, HttpServletRequest hReq) {
        ZKPage<ZKModule> resPage = ZKPage.asPage(hReq);
        resPage = this.zkModuleService.findPage(resPage, zkModule);
        return ZKMsgRes.asOk(resPage);
    }

}

