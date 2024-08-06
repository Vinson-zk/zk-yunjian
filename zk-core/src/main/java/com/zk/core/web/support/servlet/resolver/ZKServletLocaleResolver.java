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
* @Title: ZKServletLocaleResolver.java 
* @author Vinson 
* @Package com.zk.core.web.support.servlet.resolver 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 2, 2024 5:26:57 PM 
* @version V1.0 
*/
package com.zk.core.web.support.servlet.resolver;

import java.util.Locale;

import org.springframework.lang.Nullable;

import com.zk.core.web.resolver.ZKLocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** 
* @ClassName: ZKServletLocaleResolver 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKServletLocaleResolver extends ZKLocaleResolver {

    Locale resolveLocale(HttpServletRequest request);

    void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale);

}
