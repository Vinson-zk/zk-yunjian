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
* @Title: ZKSecUserFilter.java 
* @author Vinson 
* @Package com.zk.security.web.filter.authc 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 9:09:55 AM 
* @version V1.0 
*/
package com.zk.security.web.filter.authc;

import com.zk.core.exception.ZKSecAuthenticationException;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.security.web.filter.ZKSecBaseControlFilter;
import com.zk.security.web.wrapper.ZKSecRequestWrapper;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;

/** 
* @ClassName: ZKSecUserFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecUserFilter extends ZKSecBaseControlFilter {

    @Override
    protected boolean isAccessAllowed(ZKSecRequestWrapper request, ZKSecResponseWrapper response, Object mappedValue)
            throws Exception {
        ZKSecSubject subject = ZKSecSecurityUtils.getSubject();
        if (subject != null) {
            return subject.isAuthcUser();
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ZKSecRequestWrapper request, ZKSecResponseWrapper response, Object mappedValue) {
        String server = request.getPathWithinApplication();
//        if (request instanceof HttpServletRequest) {
//            server = ZKWebUtils.toHttp(request).getServletPath();
//        }
//        else {
//            server = request.getServerName();
//        }

        Object isHaveTicket = request.getAttribute(ZKSecConstants.SEC_KEY.SecIsHaveTicket);

        if (isHaveTicket != null && ((boolean) isHaveTicket) == true) {
            log.error("[^_^:20221013-1703-001] 用户登录过期，请重新登录；{}", server);
            // 请求中带有令牌，抛出异常； zk.sec.000020=登录已过期，请重新登录
            throw ZKSecAuthenticationException.as("zk.sec.000020", null);
        }else {
            log.error("[^_^:20221013-1703-001] 用户未登录；{}", server);
            // 用户未登录，抛出异常；zk.sec.000004=用户未登录
            throw ZKSecAuthenticationException.as("zk.sec.000004", null);
        }
    }

}
