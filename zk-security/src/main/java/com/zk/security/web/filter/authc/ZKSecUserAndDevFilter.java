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
* @Title: ZKSecUserAndDevFilter.java 
* @author Vinson 
* @Package com.zk.security.web.filter.authc 
* @Description: TODO(simple description this file what to do. ) 
* @date May 14, 2022 10:01:11 AM 
* @version V1.0 
*/
package com.zk.security.web.filter.authc;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.zk.security.exception.ZKSecCodeException;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.security.web.filter.ZKSecBaseControlFilter;

/** 
* @ClassName: ZKSecUserAndDevFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecUserAndDevFilter extends ZKSecBaseControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        ZKSecSubject subject = ZKSecSecurityUtils.getSubject();
        if (subject != null) {
            return subject.isAuthcUser() && subject.isAuthcDev();
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        log.error("[>_<:20220514-1001-001] zk.sec.000016 无开发者身份或无用户身份");
        throw new ZKSecCodeException("zk.sec.000016");
//        return false;
    }

}
