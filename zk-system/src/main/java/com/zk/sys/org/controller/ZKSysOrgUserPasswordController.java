/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSysOrgUserPasswordController.java 
* @author Vinson 
* @Package com.zk.sys.org.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 11, 2024 10:42:26 PM 
* @version V1.0 
*/
package com.zk.sys.org.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKStringUtils;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.org.entity.ZKSysOrgUserOptLog.ZKUserOptTypeFlag;
import com.zk.sys.org.service.ZKSysOrgUserOptService;
import com.zk.sys.org.service.ZKSysOrgUserService;
import com.zk.sys.utils.ZKSysUtils;

import jakarta.servlet.http.HttpServletRequest;

/** 
* @ClassName: ZKSysOrgUserPasswordController 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RestController
@RequestMapping(value = "${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/${zk.path.sys.org}/upwd")
public class ZKSysOrgUserPasswordController {

    @Autowired
    ZKSysOrgUserOptService sysOrgUserOptService;

    @Autowired
    ZKSysOrgUserService sysOrgUserService;

    /********************************************************/
    /** 重置密码 ****/
    /********************************************************/
    /**
     * 忘记密码，发送邮件验证码
     *
     * @Title: sendMailVerifyCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 14, 2024 10:17:34 PM
     * @param companyCode
     *            不传时，默认从拥有者公司的个人用户下查找
     * @param mail
     *            邮箱
     * @return
     * @return ZKMsgRes
     */
    @RequestMapping(value = "n/fp/sendMailVerifyCode", method = RequestMethod.POST)
    public ZKMsgRes fpSendMailVerifyCode(@RequestParam(value = "companyCode", required = false) String companyCode,
            @RequestParam("mail") String mail) {
        ZKSecTicket t = ZKSecSecurityUtils.getTikcet(true);
        if (ZKStringUtils.isEmpty(companyCode)) {
            companyCode = ZKSysUtils.getOwnerCompanyCode();
        }
        sysOrgUserOptService.fpSendMailVerifyCode(companyCode, mail, t);
        return ZKMsgRes.asOk();
    }

    // 发送手机验证码
    @RequestMapping(value = "n/fp/sendPhoneVerifyCode", method = RequestMethod.POST)
    public ZKMsgRes fpSendPhoneVerifyCode(@RequestParam(value = "companyCode", required = false) String companyCode,
            @RequestParam("phoneNum") String phoneNum) {
        ZKSecTicket t = ZKSecSecurityUtils.getTikcet(true);
        if (ZKStringUtils.isEmpty(companyCode)) {
            companyCode = ZKSysUtils.getOwnerCompanyCode();
        }
        sysOrgUserOptService.fpSendPhoneVerifyCode(companyCode, phoneNum, t);
        return ZKMsgRes.asOk();
    }

    @RequestMapping(value = "n/fp/sendVerifyCodeAgain")
    public ZKMsgRes sendVerifyCodeAgain(@RequestParam("againFlag") int againFlag) {
        ZKSecTicket tk = ZKSecSecurityUtils.getTikcet();
        this.sysOrgUserOptService.sendVerifyCodeAgainByUser(tk, againFlag);
        return ZKMsgRes.asOk();
    }

    // 忘记密码，校验验证码和修改密码
    @RequestMapping(value = "n/fp/submitVerifyCode", method = RequestMethod.POST)
    public ZKMsgRes fpSubmitVerifyCode(@RequestParam("verifyCode") String verifyCode,
            @RequestParam("newPassword") String newPassword, HttpServletRequest req) {
        // 验证验证码，请求需要带追踪令牌；
        ZKSecTicket t = ZKSecSecurityUtils.getTikcet();
        sysOrgUserOptService.fpSubmitVerifyCode(t, verifyCode, newPassword, req);
        return ZKMsgRes.asOk();
    }

    /********************************************************/
    /** 修改密码 ****/
    /********************************************************/
    // 新密码
    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    public ZKMsgRes changePassword(@RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword, HttpServletRequest req) {
        ZKSysOrgUser sysOrgUser = this.sysOrgUserService.get((String) ZKSecSecurityUtils.getUserId());
        this.sysOrgUserOptService.changePassword(sysOrgUser, oldPassword, newPassword, req);
        return ZKMsgRes.asOk();
    }

    // 设置密码：可指定密码；不指定时，设置为系统配置的默认密码；
    @RequestMapping(value = "resetPwd", method = RequestMethod.POST)
    @ResponseBody
    public ZKMsgRes resetPwd(@RequestParam("userId") String userId,
            @RequestParam(value = "pwd", required = false) String pwd, HttpServletRequest req) {
        int count = 0;
        if (ZKStringUtils.isEmpty(pwd)) {
            pwd = ZKSysUtils.getUserDefaultPwd();
        }
        this.sysOrgUserService.updatePwd(userId, pwd, ZKUserOptTypeFlag.Pwd.company, req);
        return ZKMsgRes.asOk(null, count);
    }
}


