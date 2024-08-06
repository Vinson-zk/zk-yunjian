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
* @Title: ZKShiroController.java 
* @author Vinson 
* @Package com.zk.demo.shiro.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 29, 2021 6:52:53 PM 
* @version V1.0 
*/
package com.zk.demo.shiro.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** 
* @ClassName: ZKShiroController 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RestController
@RequestMapping("test/h/c")
public class ZKShiroController {

    /**
     * 开放结果
     *
     * @Title: none
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 4, 2021 10:50:57 AM
     * @return
     * @return String
     */
    @RequestMapping("none")
    @ResponseBody
    public String none() {
        System.out.println("[^_^:20210704-2232-001] 权限不拦截 controller none 被调用了");
        return this.getClass().getName();
    }

    /**
     * 登录接口
     *
     * @Title: login
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 4, 2021 10:51:09 AM
     * @return
     * @return String
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public String login() {
//        Subject subject = SecurityUtils.getSubject();
//        Session s = subject.getSession();
//        s.setAttribute("test", "dd");
        if (SecurityUtils.getSubject() != null) {
            if (SecurityUtils.getSubject().isAuthenticated() || SecurityUtils.getSubject().isRemembered()) {
//            if (SecurityUtils.getSubject().getSession(false) != null) {
                System.out.println("[^_^:20210705-1819-002] 用户登录 controller login 被调用了，并登录成功！");
                return SecurityUtils.getSubject().getSession().getId().toString();
            }
        }

        System.out.println("[>_<:20210705-1819-001] 用户登录 controller login 被调用了，但登录失败！");
        return "not-login";
    }

    @RequestMapping("logout")
    @ResponseBody
    public String logout() {
        System.out.println("[^_^:20210704-2232-002] 权限不拦截 controller logout 被调用了");
        return "logout";
    }

    /**
     * 需要用房登录的接口
     *
     * @Title: user
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 4, 2021 10:51:18 AM
     * @return
     * @return String
     */
    @RequestMapping("user")
    @ResponseBody
    public String user() {
        System.out.println("[^_^:20210705-0030-001] 需要用户登录 controller user 被调用了");
        return "user";
    }

    /**
     * 需要权限代码[permission]
     *
     * @Title: permission
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 5, 2021 12:31:25 AM
     * @return
     * @return String
     */
    @RequestMapping("permission")
    @ResponseBody
    @RequiresPermissions("permission")
    public String permission() {
        System.out.println("[^_^:20210705-0028-001] 需要权限代码[permission] controller permission 被调用了");
        return "permission";
    }

    /**
     * 需要角色代码[role]
     *
     * @Title: role
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 5, 2021 12:31:38 AM
     * @return
     * @return String
     */
    @RequestMapping("role")
    @ResponseBody
    @RequiresRoles("role")
    public String role() {
        System.out.println("[^_^:20210705-0028-002] 需要角色代码[role] controller role 被调用了");
        return "role";
    }

    /**
     * 需要角色代码且需要权限代码[permission and role]
     *
     * @Title: permissionAndRole
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 5, 2021 12:31:50 AM
     * @return
     * @return String
     */
    @RequestMapping("permissionAndRole")
    @ResponseBody
    @RequiresRoles("role")
    @RequiresPermissions("permission")
    public String permissionAndRole() {
        System.out.println(
                "[^_^:20210705-0028-003] 需要角色代码且需要权限代码[permission and role] controller permissionAndRole 被调用了");
        return "permissionAndRole";
    }

    @RequestMapping("noPermissionAndRole")
    @ResponseBody
    @RequiresRoles("no-role")
    @RequiresPermissions("no-permission")
    public String noPermissionAndRole() {
        System.out.println("[^_^:20210705-0028-003] 用户会没有权限代码 controller noPermissionAndRole 被调用了");
        return "noPermissionAndRole";
    }

}
