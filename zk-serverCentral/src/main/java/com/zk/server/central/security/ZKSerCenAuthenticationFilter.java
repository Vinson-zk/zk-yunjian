/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKSerCenAuthenticationFilter.java 
 * @author Vinson 
 * @Package com.zk.server.central.security 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:30:25 PM 
 * @version V1.0   
*/
package com.zk.server.central.security;

import java.io.IOException;

import javax.crypto.SecretKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.zk.core.exception.ZKSecAuthenticationException;
import com.zk.core.exception.ZKSystemException;
import com.zk.core.exception.base.ZKCodeException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.utils.ZKCookieUtils;
import com.zk.core.web.utils.ZKServletUtils;
import com.zk.server.central.commons.ZKSerCenUtils;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/** 
* @ClassName: ZKSerCenAuthenticationFilter 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSerCenAuthenticationFilter extends FormAuthenticationFilter {

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    /**
     * 认证时用到 key 或参数名
     * 
     * @ClassName: ZKAuthKeys
     * @Description: TODO(simple description this class what to do.)
     * @author Vinson
     * @version 1.0
     */
    public static interface ZKAuthKeys {

        public static final String KEY_EXCEPTION = "_exception_key_";

//        public static final String KEY_ACCOUNT_REMEMBER_COOKIE = "_remember_account_c_";

        public static final String PARAM_ACCOUNT = "account";

        public static final String PARAM_PASSWORD = "password";

        public static final String PARAM_REMEMBER_ME = "rememberMe";

        public static final String PARAM_REMEMBER_ACCOUNT = "rememberAccount";
    }

    private String accountParam = ZKAuthKeys.PARAM_ACCOUNT;

    private String passwordParam = ZKAuthKeys.PARAM_PASSWORD;

    private String rememberMeParam = ZKAuthKeys.PARAM_REMEMBER_ME;

    private String rememberAccountParam = ZKAuthKeys.PARAM_REMEMBER_ACCOUNT;

    /**
     * 登录异常时，跳转的 url;
     */
    private String loginFailureUrl;

    /**
     * @return loginFailureUrl
     */
    public String getLoginFailureUrl() {
        return loginFailureUrl;
    }

    /**
     * @param loginFailureUrl
     *            the loginFailureUrl to set
     */
    public void setLoginFailureUrl(String loginFailureUrl) {
        this.loginFailureUrl = loginFailureUrl;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        try {
            return super.onAccessDenied(request, response);
        }
        catch(Exception e) {
            return onLoginFailure(null, new AuthenticationException(e), request, response);
        }
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {

//        HttpServletRequest hReq = (HttpServletRequest) request;

        String account = getAccount(request);
        String password = getPassword(request);

        HttpServletRequest hReq = ZKServletUtils.toHttp(request);

        try {
            SecretKey userSecretKey = ZKSerCenUtils.getUserSecretKey(hReq);
            if(userSecretKey != null) {
                // 如果有用户 SecretKey 解密参数
                if (!ZKStringUtils.isEmpty(account)) {
                    account = ZKSerCenUtils.decrypt(account, userSecretKey);
                }
                if (!ZKStringUtils.isEmpty(password)) {
                    password = ZKSerCenUtils.decrypt(password, userSecretKey);
                }
            }
        }
        catch(Exception e) {
            log.error("[>_<:20200103-1023-001] 接口解密失败；");
//            e.printStackTrace();
            throw ZKSystemException.as("zk.ser.cen.000016", e);
        }

        // 处理记住账号和密码；与 isRememberMe() 不同，isRememberMe() 是自动登录。
        if (isRememberAccount(request)) {
            Cookie cookieAccount = ZKCookieUtils.createCookie(ZKAuthKeys.PARAM_REMEMBER_ACCOUNT,
                    account, null);

            ZKCookieUtils.saveToHeader(ZKServletUtils.toHttp(response), cookieAccount);
        }
        else {
            ZKCookieUtils.removeCookie(ZKServletUtils.toHttp(request), ZKServletUtils.toHttp(response),
                    ZKAuthKeys.PARAM_REMEMBER_ACCOUNT, "");
        }

        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        return new UsernamePasswordToken(account, password, rememberMe, host);
    }

    protected String getAccount(ServletRequest request) {
        return WebUtils.getCleanParam(request, getAccountParam());
    }

    @Override
    protected String getPassword(ServletRequest request) {
        return WebUtils.getCleanParam(request, getPasswordParam());
    }

    @Override
    protected boolean isRememberMe(ServletRequest request) {
        return WebUtils.isTrue(request, getRememberMeParam());
    }

    protected boolean isRememberAccount(ServletRequest request) {
        return WebUtils.isTrue(request, getRememberAccountParam());
    }

    /**
     * @return accountParam
     */
    public String getAccountParam() {
        return accountParam;
    }

    /**
     * @param accountParam
     *            the accountParam to set
     */
    public void setAccountParam(String accountParam) {
        this.accountParam = accountParam;
    }

    /**
     * @return passwordParam
     */
    @Override
    public String getPasswordParam() {
        return passwordParam;
    }

    /**
     * @param passwordParam
     *            the passwordParam to set
     */
    @Override
    public void setPasswordParam(String passwordParam) {
        this.passwordParam = passwordParam;
    }

    /**
     * @return rememberMeParam
     */
    @Override
    public String getRememberMeParam() {
        return rememberMeParam;
    }

    /**
     * @param rememberMeParam
     *            the rememberMeParam to set
     */
    @Override
    public void setRememberMeParam(String rememberMeParam) {
        this.rememberMeParam = rememberMeParam;
    }

    /**
     * @return rememberAccountParam
     */
    public String getRememberAccountParam() {
        return rememberAccountParam;
    }

    /**
     * @param rememberAccountParam
     *            the rememberAccountParam to set
     */
    public void setRememberAccountParam(String rememberAccountParam) {
        this.rememberAccountParam = rememberAccountParam;
    }

    /**
     * 登录成功处理
     * <p>
     * Title: onLoginSuccess
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param token
     * @param subject
     * @param request
     * @param response
     * @return
     * @throws Exception
     * @see org.apache.shiro.web.filter.authc.FormAuthenticationFilter#onLoginSuccess(org.apache.shiro.authc.AuthenticationToken,
     *      org.apache.shiro.subject.Subject, javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse)
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
            ServletResponse response) throws Exception {
        // 登录成功不跳转
//        issueSuccessRedirect(request, response);
        // we handled the success redirect directly, prevent the chain from
        // continuing:
        return true;
    }

    /**
     * 登录失败处理
     * <p>
     * Title: onLoginFailure
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     * @see org.apache.shiro.web.filter.authc.FormAuthenticationFilter#onLoginFailure(org.apache.shiro.authc.AuthenticationToken,
     *      org.apache.shiro.authc.AuthenticationException,
     *      javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
            ServletResponse response) {
        log.error("[>_<:20191029-0052-001] 登录失败：Authentication exception -> token: ", token);
        ZKCodeException zkE = null;
        if (e instanceof ZKSerCenAuthenticationException) {
            ZKSerCenAuthenticationException sce = (ZKSerCenAuthenticationException) e;
            zkE = ZKSecAuthenticationException.as(sce.getCode(), sce.getData(), sce.getMsgArgs());
        }
        else if (e.getCause() instanceof ZKCodeException) {
            zkE = (ZKCodeException) e.getCause();
        }
        else {
            // zk.ser.cen.000015=未知的登录错误。
            zkE = ZKSecAuthenticationException.as("zk.ser.cen.000015", e, (Object[]) null);
        }

        request.setAttribute(ZKAuthKeys.KEY_EXCEPTION, zkE);

        if (!ZKStringUtils.isEmpty(getLoginFailureUrl())) {
            try {
                WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());
            }
            catch(IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        return true;
    }

}
