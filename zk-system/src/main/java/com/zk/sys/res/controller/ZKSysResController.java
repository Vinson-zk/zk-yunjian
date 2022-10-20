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
* @Title: ZKSysResController.java 
* @author Vinson 
* @Package com.zk.sys.res.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 21, 2021 4:04:28 PM 
* @version V1.0 
*/
package com.zk.sys.res.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zk.core.web.ZKMsgRes;
import com.zk.sys.res.entity.ZKSysMenu;
import com.zk.sys.res.entity.ZKSysNav;
import com.zk.sys.res.service.ZKSysMenuService;
import com.zk.sys.res.service.ZKSysNavService;

/**
 * @ClassName: ZKSysResController
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/${zk.path.sys.res}")
public class ZKSysResController {

    @Autowired
    ZKSysNavService zkSysNavService;

    @Autowired
    ZKSysMenuService zkSysMenuService;

    // 前端取栏目资源
    @RequestMapping(value = "getNavItems", method = RequestMethod.GET)
    public ZKMsgRes getNavItems(ZKSysNav zkSysNav) {
        zkSysNav.setIsShow(ZKSysNav.KeyIsShow.show);
        return ZKMsgRes.as("zk.0", null, this.zkSysNavService.findList(zkSysNav));
    }

    // 前端取栏目菜单
    @RequestMapping(value = "getNavMenus/{navCode}", method = RequestMethod.GET)
    public ZKMsgRes getNavMenus(@PathVariable("navCode")String navCode, ZKSysMenu zkSysMenu) {
        zkSysMenu.setNavCode(navCode);
        return ZKMsgRes.as("zk.0", null, zkSysMenuService.findList(zkSysMenu));
    }

}
