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


import com.zk.core.exception.base.ZKCodeException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.token.ZKSecAuthenticationToken;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.security.web.wrapper.ZKSecRequestWrapper;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;

/** 
* @ClassName: ZKSecAuthenticationFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecAuthenticationFilter extends ZKSecAccessControlFilter {

    protected abstract ZKSecAuthenticationToken createToken(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes)
            throws Exception;

    public static String getCompanyCode(ZKSecRequestWrapper zkReq) {
        return zkReq.getCleanParam(ZKSecConstants.PARAM_NAME.CompanyCode);
    }

    public static long getOsType(ZKSecRequestWrapper zkReq) {
        long osType = 0;
        try {
            String dtStr = zkReq.getCleanParam(ZKSecConstants.PARAM_NAME.OsType);
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

    public static String getUdid(ZKSecRequestWrapper zkReq) {
        return zkReq.getCleanParam(ZKSecConstants.PARAM_NAME.DriverUdid);
    }

    public static long getAppType(ZKSecRequestWrapper zkReq) {
        long appType = 0;
        try {
            String dtStr = zkReq.getCleanParam(ZKSecConstants.PARAM_NAME.AppType);
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

    public static String getAppId(ZKSecRequestWrapper zkReq) {
        return zkReq.getCleanParam(ZKSecConstants.PARAM_NAME.appId);
    }

    /**
     * 执行登录
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected boolean executeLogin(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes) throws Exception {
        // 是否为 post 请求
        if (isLoginSubmission(zkReq, zkRes)) {
            ZKSecAuthenticationToken token = createToken(zkReq, zkRes);
            if (token == null) {
                String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken "
                        + "must be created in order to execute a login attempt.";
                throw new IllegalStateException(msg);
            }
            try {
                ZKSecSubject subject = getSubject(zkReq, zkRes);
                subject.login(token);
                return onLoginSuccess(token, subject, zkReq, zkRes);
            }
            catch(ZKCodeException se) {
                return onLoginFailure(token, se, zkReq, zkRes);
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
    protected boolean isLoginSubmission(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes) {
        return zkReq.isLoginSubmission();
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
    protected boolean onLoginSuccess(ZKSecAuthenticationToken token, ZKSecSubject subject, ZKSecRequestWrapper zkReq,
            ZKSecResponseWrapper zkRes) {
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
    protected boolean onLoginFailure(ZKSecAuthenticationToken token, ZKCodeException se, ZKSecRequestWrapper zkReq,
            ZKSecResponseWrapper zkRes) {
        log.error("[>_<:20210805-1137-001] 登录失败 msg -> {} ", se.getMessage());
        setFailureAttribute(zkReq, se);
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
    protected void setFailureAttribute(ZKSecRequestWrapper zkReq, ZKCodeException se) {
        zkReq.setAttribute(getFailureKeyAttribute(), se);
    }

//  @Override
//  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
//          throws Exception {
//      // 判断如果已经成功登录，直接通过挂载，因为不同身份登录判断是否已登录方式不同，所以在各身份登录认证中实现
//      return executeLogin(request, response);
//  }

    @Override
    protected boolean onAccessDenied(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes, Object mappedValue)
            throws Exception {
        return executeLogin(zkReq, zkRes);
    }

    protected ZKSecSubject getSubject(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes) {
        return ZKSecSecurityUtils.getSubject();
    }

}
