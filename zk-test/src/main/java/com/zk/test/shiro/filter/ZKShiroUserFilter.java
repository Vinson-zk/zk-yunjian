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
* @Title: ZKShiroUserFilter.java 
* @author Vinson 
* @Package com.zk.test.shiro.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 29, 2021 6:54:42 PM 
* @version V1.0 
*/
package com.zk.test.shiro.filter;

import static org.apache.shiro.util.StringUtils.split;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zk.core.exception.ZKCodeException;

/** 
* @ClassName: ZKShiroUserFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKShiroUserFilter extends ZKShiroBaseFilter {

    private static final Logger log = LoggerFactory.getLogger(LogoutFilter.class);

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        if (isLoginRequest(request, response)) {
            // 登录请求不做校验
            return true;
        }
        Subject subject = getSubject(request, response);
        return subject.isAuthenticated() || subject.isRemembered();
//        return subject != null && subject.getPrincipal() != null;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        log.error("[>_<:20210705-2030-001] 用户未登录");
        throw ZKCodeException.as("zk.test.000004", "用户未登录");
    }

    @Override
    public Filter processPathConfig(String path, String config) {
        String[] values = null;
        if (config != null) {
            values = split(config);
        }

        this.appliedPaths.put(path, values);
        return this;
    }

}
