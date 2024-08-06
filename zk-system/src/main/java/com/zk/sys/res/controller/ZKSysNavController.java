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
* @Title: ZKSysNavController.java 
* @author Vinson 
* @Package com.zk.sys.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 20, 2020 2:14:59 PM 
* @version V1.0 
*/
package com.zk.sys.res.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.base.controller.ZKBaseController;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.security.annotation.ZKSecApiCode;
import com.zk.sys.res.entity.ZKSysNav;
import com.zk.sys.res.service.ZKSysNavService;

/**
 * @ClassName: ZKSysNavController
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@RestController
@RequestMapping("${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/${zk.path.sys.res}/nav")
public class ZKSysNavController extends ZKBaseController {

    @Autowired
    ZKSysNavService zkSysNavService;

    // 编辑
    @RequestMapping(value = "sysNav", method = RequestMethod.POST
//            , consumes = "application/json"
    )
    public ZKMsgRes editSysNav(@RequestBody ZKSysNav zkSysNav) {
        this.zkSysNavService.save(zkSysNav);
        return ZKMsgRes.asOk(zkSysNav);
    }

    /**
     * 批量删除
     *
     * @Title: deleteSysNav
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 25, 2021 5:03:22 PM
     * @param pkIds
     *            id 数组
     * @return ZKMsgRes
     */
    @RequestMapping(value = "sysNav", method = RequestMethod.DELETE)
    public ZKMsgRes deleteSysNav(@RequestParam("pkId[]") String[] pkIds) {
        int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                count += this.zkSysNavService.del(new ZKSysNav(pkId));
            }
        }
        return ZKMsgRes.asOk(count);
    }

    // 详情查询
    @ZKSecApiCode("com_zk_sys_res_sysNav_detail")
    @RequestMapping(value = "sysNav", method = RequestMethod.GET)
    public ZKMsgRes getSysNav(@RequestParam("pkId") String pkId) {
        ZKSysNav zkSysNav = this.zkSysNavService.get(new ZKSysNav(pkId));
        return ZKMsgRes.asOk(zkSysNav);
    }

    // 分页列表查询
    @RequestMapping(value = "sysNavs", method = RequestMethod.GET)
    public ZKMsgRes pageSysNav(ZKSysNav zkSysNav, HttpServletRequest hReq) {
        ZKPage<ZKSysNav> resPage = ZKPage.asPage(hReq);
        resPage = this.zkSysNavService.findPage(resPage, zkSysNav);
        return ZKMsgRes.asOk(resPage);
    }

}
