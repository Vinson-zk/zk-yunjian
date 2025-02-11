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
* @Title: ZKSysSecIndexController.java 
* @author Vinson 
* @Package com.zk.sys.common.sec.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 20, 2022 4:39:49 PM 
* @version V1.0 
*/
package com.zk.sys.sec.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.exception.base.ZKCodeException;
import com.zk.core.web.utils.ZKServletUtils;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.org.entity.ZKSysOrgUserOptLog;
import com.zk.sys.org.service.ZKSysOrgCompanyService;
import com.zk.sys.org.service.ZKSysOrgUserOptLogService;
import com.zk.sys.org.service.ZKSysOrgUserService;
import com.zk.sys.sec.common.ZKSysSecConstants;

import jakarta.servlet.http.HttpServletRequest;

/** 
* @ClassName: ZKSysSecIndexController 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/

@RestController
@RequestMapping("${zk.path.admin}/${zk.path.sys}/${zk.sys.version}/sec")
public class ZKSysSecIndexController {

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    @Autowired
    ZKSysOrgCompanyService orgCompanyService;

    @Autowired
    ZKSysOrgUserService orgUserService;

    @Autowired
    ZKSysOrgUserOptLogService sysOrgUserOptLogService;

    /**
     * 查询当前登录信息
     * 
     * 
     *
     * @Title: sysOrgUserSelf
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 26, 2022 5:44:00 PM
     * @return
     * @return ZKMsgRes
     */
    @RequestMapping(value = "loginUserInfo", method = RequestMethod.GET)
    public ZKMsgRes sysOrgUserSelf() {
        ZKSysOrgUser sysOrgUser = this.orgUserService.getDetail((String) ZKSecSecurityUtils.getUserId());
        Map<String, Object> resMap = new HashMap<>();
        resMap.put(ZKSysSecConstants.KeyWebLogin.user, sysOrgUser);
        return ZKMsgRes.asOk(null, resMap);
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
    public ZKMsgRes login(HttpServletRequest hReq) {
        ZKSecSubject subject = ZKSecSecurityUtils.getSubject();
        ZKSecTicket tk = subject.getTicket();
//        if (subject != null && subject.isAuthenticated() && subject.isAuthcUser()) {
        if (subject != null && subject.isAuthcUser() && tk != null) {
            sysOrgUserOptLogService.optLogLogin(ZKSecSecurityUtils.getUserId(),
                    ZKSysOrgUserOptLog.ZKUserOptTypeFlag.Login.self, ZKJson.parse("{\"resFlag\": 1}"), hReq);
            log.info("[^_^:20220420-1641-001] 用户[{}]登录登录成功！", ZKSecSecurityUtils.getUserPrincipal().getUsername());
            Map<String, Object> resMap = new HashMap<>();
            resMap.put(ZKSecConstants.PARAM_NAME.TicketId, tk.getTkId().toString());
//            ZKSecUserPrincipal<String> principal = ZKSecSecurityUtils.getUserPrincipal();
//            ZKSysOrgUser user = this.orgUserService.getDetail(principal.getPkId());
//            resMap.put(ZKSysSecConstants.KeyWebLogin.company, company);
//            resMap.put(ZKSysSecConstants.KeyWebLogin.user, user);
            return ZKMsgRes.asOk(null, resMap);
        }

        // resFlag = -1，登录失败
        String username = ZKServletUtils.getCleanParam(hReq, ZKSecConstants.PARAM_NAME.Username);
        sysOrgUserOptLogService.optLogLogin(username, ZKSysOrgUserOptLog.ZKUserOptTypeFlag.Login.self,
                ZKJson.parse("{\"resFlag\": -1}"), hReq);
        log.info("[^_^:20220420-1641-002] 用户 [{}] 登录失败！", username);
        ZKCodeException se = (ZKCodeException) hReq.getAttribute(ZKSecConstants.SEC_KEY.SecException);
        if (se != null) {
            return ZKMsgRes.as(null, se);
        }

        return ZKMsgRes.asSysErr();
    }

    @RequestMapping("logout")
    public String logout() {
        log.info("[^_^:20220420-1641-003] 用户登出！");
        return "logout";
    }

}
