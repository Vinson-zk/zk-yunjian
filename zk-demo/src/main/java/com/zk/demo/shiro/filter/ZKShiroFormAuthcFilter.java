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
* @Title: ZKShiroFormAuthcFilter.java 
* @author Vinson 
* @Package com.zk.demo.shiro.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 29, 2021 6:54:16 PM 
* @version V1.0 
*/
package com.zk.demo.shiro.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import com.zk.demo.shiro.common.ZKShiroConstants;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/** 
* @ClassName: ZKShiroFormAuthcFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKShiroFormAuthcFilter extends FormAuthenticationFilter {

    private Logger logger = LogManager.getLogger(getClass());

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {

        this.setUsernameParam(ZKShiroConstants.PARAM_NAME.UserName);
        this.setPasswordParam(ZKShiroConstants.PARAM_NAME.Pwd);

        String username = getUsername(request);
        String password = getPassword(request);

        logger.info("[^_^ 20210705-1812-001: 生成 token {username={}, password={}}]", username, password);

        if (password == null) {
            password = "";
        }

        return new UsernamePasswordToken(username, password.toCharArray());

    }

    /**
     * 登录成功
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
            ServletResponse response) throws Exception {
        // 登录成功设置追踪 id
//      request.setAttribute(SecConstants.PARAM_NAME.TicketId, ShiroSecurityUtils.getUserPrincipal().getTicketId());
        return true; // 设置不跳转
    }

    /**
     * 登录失败调用事件
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
            ServletResponse response) {
        // 增加请求地址的登录失败次数
//        ValidUtils.isNeedValidCode(SecurityUtils.getSession().getId().toString(), true, false);
        logger.error("[>_<:20171220-2326-001] 登录失败了，onLoginFailure msg -> {} ", e.getMessage());
        if (e instanceof AuthenticationException) {
//            request.setAttribute(SecConstants.SHIRO_PARAM.MsgCode, e.getMessage());
        }
        else {
            e.printStackTrace();
//            request.setAttribute(SecConstants.SHIRO_PARAM.MsgCode, "1");
        }
        // 跳转到登录页，失败，交由失败 controller 处理
//        try {
//            redirectToLogin(request, response);
//        }
//        catch(IOException e1) {
//            e1.printStackTrace();
//        }
        return true;
    }

}
