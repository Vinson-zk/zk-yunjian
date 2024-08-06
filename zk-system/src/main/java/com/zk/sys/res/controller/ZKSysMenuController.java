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
* @Title: ZKSysMenuController.java 
* @author Vinson 
* @Package com.zk.sys.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Oct 23, 2020 2:29:29 PM 
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
import com.zk.sys.res.entity.ZKSysMenu;
import com.zk.sys.res.service.ZKSysMenuService;

/**
 * @ClassName: ZKSysMenuController
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@RestController
@RequestMapping("${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/${zk.path.sys.res}/menu")
public class ZKSysMenuController extends ZKBaseController {

    @Autowired
    ZKSysMenuService zkSysMenuService;

    // 编辑
    @RequestMapping(value = "sysMenu", method = RequestMethod.POST
//            , consumes = "application/json"
    )
    public ZKMsgRes editSys(@RequestBody ZKSysMenu zkSysMenu) {
        this.zkSysMenuService.save(zkSysMenu);
        return ZKMsgRes.asOk(zkSysMenu);
    }

    // 删除
    @RequestMapping(value = "sysMenu", method = RequestMethod.DELETE)
    public ZKMsgRes deleteSysMenu(@RequestParam("pkId[]") String[] pkIds) {
        int count = 0;
        if (pkIds != null && pkIds.length > 0) {
            for (String pkId : pkIds) {
                // count += this.zkSysMenuService.delete(new ZKSysMenu(pkId));
                count += this.zkSysMenuService.del(new ZKSysMenu(pkId));
            }
            }
            return ZKMsgRes.asOk(count);
        }

    // 详情查询
    @RequestMapping(value = "sysMenu", method = RequestMethod.GET)
    public ZKMsgRes getSysMenu(@RequestParam("pkId") String pkId) {
//        ZKSysMenu zkSysMenu = this.zkSysMenuService.get(new ZKSysMenu(pkId));
        ZKSysMenu zkSysMenu = this.zkSysMenuService.getDetail(new ZKSysMenu(pkId));
        return ZKMsgRes.asOk(zkSysMenu);
    }

    // 分页列表查询
    @RequestMapping(value = "sysMenusTree", method = RequestMethod.GET)
    public ZKMsgRes pageSysMenusTree(ZKSysMenu zkSysMenu, HttpServletRequest hReq) {
        ZKPage<ZKSysMenu> resPage = ZKPage.asPage(hReq);
        resPage = this.zkSysMenuService.findTree(resPage, zkSysMenu);
        return ZKMsgRes.asOk(resPage);
    }

}
