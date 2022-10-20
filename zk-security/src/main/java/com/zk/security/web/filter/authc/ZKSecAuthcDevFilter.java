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
* @Title: ZKSecAuthcAppFilter.java 
* @author Vinson 
* @Package com.zk.security.web.filter.authc 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 9:16:23 AM 
* @version V1.0 
*/
package com.zk.security.web.filter.authc;
/** 
* @ClassName: ZKSecAuthcAppFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.token.ZKSecAuthenticationToken;
import com.zk.security.token.ZKSecDefaultAuthcDevToken;
import com.zk.security.web.filter.ZKSecAuthenticationFilter;

public class ZKSecAuthcDevFilter extends ZKSecAuthenticationFilter {

    @Override
    protected ZKSecAuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        String companyCode = getCompanyCode(request);
        long osType = getOsType(request);
        String udid = getUdid(request);
        long appType = getAppType(request);
        String appId = getAppId(request);

        String devId = ZKWebUtils.getCleanParam(request, ZKSecConstants.PARAM_NAME.DevId);
        String secretKeyStr = ZKWebUtils.getCleanParam(request, ZKSecConstants.PARAM_NAME.SecretKey);
        char[] secretKey = ZKStringUtils.isEmpty(secretKeyStr)?null:secretKeyStr.toCharArray();
        
        return new ZKSecDefaultAuthcDevToken(companyCode, devId, secretKey, osType, udid, appType, appId);
    }
    
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        // 判断如果已经成功登录，直接通过挂载，因为不同身份登录判断是否已登录方式不同，所以在各身份登录认证中实现
        ZKSecSubject subject = getSubject(request, response);
        return subject.isAuthcDev();
    }

}
