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
 * @Title: ZKLocaleInterceptor.java 
 * @author Vinson 
 * @Package com.zk.core.web.interceptor 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 3:03:52 PM 
 * @version V1.0   
*/
package com.zk.core.web.interceptor;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;

import com.zk.core.web.utils.ZKWebUtils;

/** 
* @ClassName: ZKLocaleInterceptor 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKLocaleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest hReq, HttpServletResponse hRes, Object arg) throws Exception {

        Locale locale = ZKWebUtils.getLocaleByRequest(hReq);
        // 如果请求中未按系统要求指定语言，则不指定语言，取系统默认语言，要 spring_context.xml 国际化中有配置
        LocaleResolver localeResolver = ZKWebUtils.getLocaleResolver(ZKWebUtils.getAppCxt(), hReq);
        if (locale == null && localeResolver != null) {
            locale = localeResolver.resolveLocale(hReq);
        }

        if (locale != null) {
            if (localeResolver == null) {
                throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
            }

            ZKWebUtils.setLocale(locale, localeResolver, hReq, hRes);
        }
        return true;
    }

}
