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
* @Title: ZKSecAccessControlFilter.java 
* @author Vinson 
* @Package com.zk.security.web.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 7:01:01 AM 
* @version V1.0 
*/
package com.zk.security.web.filter;

import com.zk.security.web.wrapper.ZKSecRequestWrapper;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;

/** 
* @ClassName: ZKSecAccessControlFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecAccessControlFilter extends ZKSecPathMatchingFilter {

    public static final String DEFAULT_LOGIN_URL = "/login";

    private String loginUrl = DEFAULT_LOGIN_URL;

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    @Override
    protected boolean onPreHandle(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes, Object mappedValue)
            throws Exception {
        return isAccessAllowed(zkReq, zkRes, mappedValue) || onAccessDenied(zkReq, zkRes, mappedValue);
    }

    /**
     * 是否允许访问；允许与拒绝一个通过即可访问；
     * 
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @throws Exception
     */
    protected abstract boolean isAccessAllowed(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes,
            Object mappedValue)
            throws Exception;

    /**
     * 是否拒绝访问；允许与拒绝一个通过即可访问；
     * 
     * 权限使用拒绝方式，即类似黑名单模式
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected abstract boolean onAccessDenied(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes, Object mappedValue)
            throws Exception;

//    /**
//     * 转发到 登录页面
//     *
//     * @Title: redirectToLogin
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date Jul 29, 2021 5:32:38 PM
//     * @param request
//     * @param response
//     * @throws IOException
//     * @return void
//     */
//    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
//        String loginUrl = getLoginUrl();
//        ZKWebUtils.issueRedirect(request, response, loginUrl);
//    }

}
