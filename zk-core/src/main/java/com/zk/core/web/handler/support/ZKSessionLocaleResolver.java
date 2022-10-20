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
 * @Title: ZKSessionLocaleResolver.java 
 * @author Vinson 
 * @Package com.zk.core.web.handler.support 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 11, 2019 4:31:24 PM 
 * @version V1.0   
*/
package com.zk.core.web.handler.support;

import java.util.Locale;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.zk.core.web.handler.ZKLocaleResolver;

/** 
* @ClassName: ZKSessionLocaleResolver 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSessionLocaleResolver extends SessionLocaleResolver implements ZKLocaleResolver {

    public void setDefaultLocale(@Nullable Locale defaultLocale) {
        super.setDefaultLocale(defaultLocale);
    }

    /**
     * Return the default Locale that this resolver is supposed to fall back to,
     * if any.
     */
    @Nullable
    public Locale getDefaultLocale() {
        return super.getDefaultLocale();
    }

}
