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
 * @Title: ZKViewVariateInterceptor.java 
 * @author Vinson 
 * @Package com.zk.server.central.interceptor 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:24:16 PM 
 * @version V1.0   
*/
package com.zk.server.central.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import com.zk.core.web.utils.ZKWebUtils;

/** 
* @ClassName: ZKViewVariateInterceptor 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKViewVariateInterceptor implements HandlerInterceptor {

    /**
     * 日志对象
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

//    @Override
//    public boolean preHandle(HttpServletRequest hReq, HttpServletResponse hRes, Object arg) throws Exception {
//        return true;
//    }

    @Override
    public void postHandle(HttpServletRequest hReq, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            LocaleResolver localeResolver = ZKWebUtils.getLocaleResolver();
            if (localeResolver != null) {
                modelAndView.addObject(ZKWebUtils.Locale_Flag_In_Header,
                        localeResolver.resolveLocale(hReq).toLanguageTag());
            }
        }
    }

//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
//            @Nullable Exception ex) throws Exception {
//    }

}
