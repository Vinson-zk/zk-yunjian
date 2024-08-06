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
* @Title: ZKSecCookieRememberMemanager.java 
* @author Vinson 
* @Package com.zk.security.web.rememberMe 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 27, 2021 7:24:54 PM 
* @version V1.0 
*/
package com.zk.security.web.rememberMe;

import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.web.cookie.ZKCookie;
import com.zk.core.web.cookie.ZKDefaultCookie;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.rememberMe.ZKSecAbstractRememberMeManager;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.token.ZKSecAuthenticationToken;
import com.zk.security.web.subject.ZKSecWebSubject;
import com.zk.security.web.wrapper.ZKSecRequestWrapper;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;

/** 
* @ClassName: ZKSecCookieRememberMemanager 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecCookieRememberMemanager extends ZKSecAbstractRememberMeManager {

    public static final String DELETED_COOKIE_VALUE = "deleteMe";

    private ZKCookie rememberCookie;

    /**
     * 设置记住我天数；
     */
    public int rememberMeDay = 10;

    public ZKSecCookieRememberMemanager() {
        rememberCookie = new ZKDefaultCookie();
        rememberCookie.setName(ZKSecConstants.PARAM_NAME.RememberMe);
        rememberCookie.setHttpOnly(true);
        rememberCookie.setMaxAge(60 * 60 * 24 * rememberMeDay);
    }

    public void setRememberMeDay(int rememberMeDay) {
        this.rememberMeDay = rememberMeDay;
    }

    public ZKCookie getCookie() {
        return this.rememberCookie;
    }

    @Override
    protected void forgetIdentity(ZKSecSubject subject) {
        if (subject instanceof ZKSecWebSubject) {
            ZKSecRequestWrapper request = ((ZKSecWebSubject) subject).getRequest();
            ZKSecResponseWrapper response = ((ZKSecWebSubject) subject).getResponse();
            getCookie().removeFrom(request, response);
        }
    }

    @Override
    protected void rememberIdentity(ZKSecSubject subject, ZKSecAuthenticationToken token, byte[] serialized) {
        // TODO Auto-generated method stub
        if (subject instanceof ZKSecWebSubject) {
            ZKSecRequestWrapper request = ((ZKSecWebSubject) subject).getRequest();
            ZKSecResponseWrapper response = ((ZKSecWebSubject) subject).getResponse();

            String base64 = new String(ZKEncodingUtils.encodeBase64(serialized));

            ZKCookie template = getCookie(); // the class attribute is really a
                                           // template for the outgoing cookies
            ZKCookie rememberCookie = new ZKDefaultCookie(template);
            rememberCookie.setValue(base64);
            rememberCookie.saveTo(request, response);
        }
    }

    @Override
    protected byte[] getRememberedSerializedIdentity(ZKSecSubject subject) {
        if (subject instanceof ZKSecWebSubject) {
            ZKSecRequestWrapper request = ((ZKSecWebSubject) subject).getRequest();
            ZKSecResponseWrapper response = ((ZKSecWebSubject) subject).getResponse();
            if (!isIdentityRemoved(request, response)) {
                String base64 = getCookie().readValue(request, response);

                if (base64 != null) {
                    if (DELETED_COOKIE_VALUE.equals(base64)) {
                        return null;
                    }
                    return ZKEncodingUtils.decodeBase64(base64.getBytes());
                }
                else {
                    // no cookie set - new site visitor?
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 判断记住的身份是否移除
     * 
     * @param request
     * @param response
     * @return
     */
    private boolean isIdentityRemoved(ZKSecRequestWrapper request, ZKSecResponseWrapper response) {
        if (request != null) {
            String IDENTITY_REMOVED_KEY = this.getClass().getName() + "_IDENTITY_REMOVED_KEY";
            Boolean removed = (Boolean) request.getAttribute(IDENTITY_REMOVED_KEY);
            return removed != null && removed;
        }
        return false;
    }

}
