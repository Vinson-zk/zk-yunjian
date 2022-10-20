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
* @Title: ZKSecHelperController.java 
* @author Vinson 
* @Package com.zk.security.helper.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 4, 2021 9:00:19 AM 
* @version V1.0 
*/
package com.zk.security.helper.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zk.core.web.ZKMsgRes;
import com.zk.security.annotation.ZKSecApiCode;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.exception.ZKSecCodeException;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.utils.ZKSecSecurityUtils;

/** 
* @ClassName: ZKSecHelperController 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RestController
@RequestMapping("sec/h/c")
public class ZKSecHelperController {

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
    public String login(HttpServletRequest hReq) {
        ZKSecSubject subject = ZKSecSecurityUtils.getSubject();
        ZKSecTicket tk = subject.getTicket();
        tk.put("test", "dd");
        if (subject != null && subject.isAuthenticated() && subject.isAuthcUser()) {
            if (tk != null) {
                System.out.println("[^_^:20210705-1819-002] 用户登录 controller login 被调用了，并登录成功！");
                return tk.getTkId().toString();
            }
        }
        System.out.println("[>_<:20210705-1819-001] 用户登录 controller login 被调用了，但登录失败！");
        ZKSecCodeException se = (ZKSecCodeException) hReq.getAttribute(ZKSecConstants.SEC_KEY.SecException);
        if (se != null) {
            return ZKMsgRes.as(se).toString();
        }
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

    @RequestMapping("noZkSecApiCode")
    @ResponseBody
    @ZKSecApiCode("noZkSecApiCode")
    public String noZkSecApiCode() {
        System.out.println("[^_^:2021-806-001] 自定义 api 代码 controller noZkSecApiCode 被调用了");
        return "noZkSecApiCode";
    }

    @RequestMapping("zkSecApiCode")
    @ResponseBody
    @ZKSecApiCode("zkSecApiCode")
    public String zkSecApiCode() {
        System.out.println("[^_^:20210705-0028-003] 自定义 api 代码 controller zkSecApiCode 被调用了");
        return "ok:zkSecApiCode";
    }

}
