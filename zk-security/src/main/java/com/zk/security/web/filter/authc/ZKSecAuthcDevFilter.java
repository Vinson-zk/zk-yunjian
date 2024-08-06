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

import com.zk.core.utils.ZKStringUtils;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.token.ZKSecAuthenticationToken;
import com.zk.security.token.ZKSecDefaultAuthcDevToken;
import com.zk.security.web.filter.ZKSecAuthenticationFilter;
import com.zk.security.web.wrapper.ZKSecRequestWrapper;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;

public class ZKSecAuthcDevFilter extends ZKSecAuthenticationFilter {

    @Override
    protected ZKSecAuthenticationToken createToken(ZKSecRequestWrapper zkSecReq, ZKSecResponseWrapper zkSecRes)
            throws Exception {
        String companyCode = getCompanyCode(zkSecReq);
        long osType = getOsType(zkSecReq);
        String udid = getUdid(zkSecReq);
        long appType = getAppType(zkSecReq);
        String appId = getAppId(zkSecReq);

        String devId = zkSecReq.getCleanParam(ZKSecConstants.PARAM_NAME.DevId);
        String secretKeyStr = zkSecReq.getCleanParam(ZKSecConstants.PARAM_NAME.SecretKey);
        char[] secretKey = ZKStringUtils.isEmpty(secretKeyStr)?null:secretKeyStr.toCharArray();
        
        return new ZKSecDefaultAuthcDevToken(companyCode, devId, secretKey, osType, udid, appType, appId);
    }
    
    @Override
    protected boolean isAccessAllowed(ZKSecRequestWrapper request, ZKSecResponseWrapper response, Object mappedValue)
            throws Exception {
        // 判断如果已经成功登录，直接通过挂载，因为不同身份登录判断是否已登录方式不同，所以在各身份登录认证中实现
        ZKSecSubject subject = getSubject(request, response);
        return subject.isAuthcDev();
    }

}
