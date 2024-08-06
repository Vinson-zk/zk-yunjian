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
* @Title: ZKShiroLogoutFilter.java 
* @author Vinson 
* @Package com.zk.demo.shiro.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 29, 2021 6:54:29 PM 
* @version V1.0 
*/
package com.zk.demo.shiro.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

/** 
* @ClassName: ZKShiroLogoutFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKShiroLogoutFilter extends ZKShiroBaseFilter {

    private static final Logger log = LogManager.getLogger(LogoutFilter.class);

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        Subject subject = getSubject(request, response);
        // try/catch added for SHIRO-298:
        try {
            log.info("[^_^:20210704-2249-001] 用户登出；");
            subject.logout();
            return true;
        }
        catch(SessionException se) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", se);
        }
//        saveRequestAndRedirectToLogin(request, response);
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

}
