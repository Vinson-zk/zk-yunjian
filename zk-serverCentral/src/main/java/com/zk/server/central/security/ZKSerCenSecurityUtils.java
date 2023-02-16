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
 * @Title: ZKSerCenSecurityUtils.java 
 * @author Vinson 
 * @Package com.zk.server.central.security 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:30:56 PM 
 * @version V1.0   
*/
package com.zk.server.central.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import com.zk.security.principal.ZKSecUserPrincipal;

/** 
* @ClassName: ZKSerCenSecurityUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSerCenSecurityUtils extends SecurityUtils {

    @SuppressWarnings("unchecked")
    public static ZKSecUserPrincipal<String> getPrincipal() {
        if (SecurityUtils.getSubject() != null) {
            return (ZKSecUserPrincipal<String>) SecurityUtils.getSubject().getPrincipal();
        }
        return null;
    }

    public static Session getSession() {

        if (SecurityUtils.getSubject() != null) {
            return SecurityUtils.getSubject().getSession();
        }
        return null;

    }

}
