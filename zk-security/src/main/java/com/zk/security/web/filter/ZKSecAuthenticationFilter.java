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
* @Title: ZKSecAuthenticationFilter.java 
* @author Vinson 
* @Package com.zk.security.web.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 7:01:39 AM 
* @version V1.0 
*/
package com.zk.security.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.exception.ZKSecCodeException;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.token.ZKSecAuthenticationToken;
import com.zk.security.utils.ZKSecSecurityUtils;

/** 
* @ClassName: ZKSecAuthenticationFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecAuthenticationFilter extends ZKSecAccessControlFilter {

    public static final String POST_METHOD = "POST";

    protected abstract ZKSecAuthenticationToken createToken(ServletRequest request, ServletResponse response)
            throws Exception;

    protected String getCompanyCode(ServletRequest request) {
        return ZKWebUtils.getCleanParam(request, ZKSecConstants.PARAM_NAME.CompanyCode);
    }

    protected long getOsType(ServletRequest request) {
        long osType = 0;
        try {
            String dtStr = ZKWebUtils.getCleanParam(request, ZKSecConstants.PARAM_NAME.OsType);
            if (ZKStringUtils.isEmpty(dtStr)) {
                osType = 0;
            }
            else {
                osType = Long.valueOf(dtStr);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            osType = 0;
        }
        return osType;
    }

    protected String getUdid(ServletRequest request) {
        return ZKWebUtils.getCleanParam(request, ZKSecConstants.PARAM_NAME.DriverUdid);
    }

    protected long getAppType(ServletRequest request) {
        long appType = 0;
        try {
            String dtStr = ZKWebUtils.getCleanParam(request, ZKSecConstants.PARAM_NAME.AppType);
            if (ZKStringUtils.isEmpty(dtStr)) {
                appType = 0;
            }
            else {
                appType = Long.valueOf(dtStr);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            appType = 0;
        }
        return appType;
    }

    protected String getAppId(ServletRequest request) {
        return ZKWebUtils.getCleanParam(request, ZKSecConstants.PARAM_NAME.appId);
    }

    /**
     * 执行登录
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        // 是否为 post 请求
        if (isLoginSubmission(request, response)) {
            ZKSecAuthenticationToken token = createToken(request, response);
            if (token == null) {
                String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken "
                        + "must be created in order to execute a login attempt.";
                throw new IllegalStateException(msg);
            }
            try {
                ZKSecSubject subject = getSubject(request, response);
                subject.login(token);
                return onLoginSuccess(token, subject, request, response);
            }
            catch(ZKSecCodeException se) {
                return onLoginFailure(token, se, request, response);
            }
        }
        else {
            return true;
        }
    }

    /**
     * 仅 post 请求才会执行登录拦截处理
     * 
     * @param request
     * @param response
     * @return
     */
    protected boolean isLoginSubmission(ServletRequest request, ServletResponse response) {
        return (request instanceof HttpServletRequest)
                && ZKWebUtils.toHttp(request).getMethod().equalsIgnoreCase(POST_METHOD);
    }

    /**
     * 登录成功后处理
     * 
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     */
    protected boolean onLoginSuccess(ZKSecAuthenticationToken token, ZKSecSubject subject, ServletRequest request,
            ServletResponse response) {
        // 用户在线数量处理
        subject.getSecurityManager().doPrincipalsCount(subject.getPrincipalCollection());
        return true;
    }

    /**
     * 登录失败后处理
     * 
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    protected boolean onLoginFailure(ZKSecAuthenticationToken token, ZKSecCodeException se, ServletRequest request,
            ServletResponse response) {
        log.error("[>_<:20210805-1137-001] 登录失败 msg -> {} ", se.getMessage());
        setFailureAttribute(request, se);
        return true;
    }

    /**
     * 取登录失败异常在 request 中的对应的 key
     * 
     * @return
     */
    protected String getFailureKeyAttribute() {
        return ZKSecConstants.SEC_KEY.SecException;
    }

    /**
     * 向 request 设置登录失败的异常
     * 
     * @param request
     * @param ae
     */
    protected void setFailureAttribute(ServletRequest request, ZKSecCodeException se) {
        request.setAttribute(getFailureKeyAttribute(), se);
    }

//  @Override
//  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
//          throws Exception {
//      // 判断如果已经成功登录，直接通过挂载，因为不同身份登录判断是否已登录方式不同，所以在各身份登录认证中实现
//      return executeLogin(request, response);
//  }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return executeLogin(request, response);
    }

    protected ZKSecSubject getSubject(ServletRequest request, ServletResponse response) {
        return ZKSecSecurityUtils.getSubject();
    }

}
