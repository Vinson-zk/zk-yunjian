/** 
* Copyright (c) 2012-2025 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSysAuthcUserFilter.java 
* @author Vinson 
* @Package com.zk.sys.sec.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 5, 2025 1:52:29 PM 
* @version V1.0 
*/
package com.zk.sys.sec.filter;

import com.zk.core.utils.ZKStringUtils;
import com.zk.core.utils.ZKUtils;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.token.ZKSecAuthenticationToken;
import com.zk.security.token.ZKSecDefaultAuthcUserToken;
import com.zk.security.web.filter.authc.ZKSecAuthcUserFilter;
import com.zk.security.web.wrapper.ZKSecRequestWrapper;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;
import com.zk.sys.utils.ZKSysUtils;

/** 
* @ClassName: ZKSysAuthcUserFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSysAuthcUserFilter extends ZKSecAuthcUserFilter {

    @Override
    protected ZKSecAuthenticationToken createToken(ZKSecRequestWrapper zkSecReq, ZKSecResponseWrapper zkSecRes)
            throws Exception {

        String companyCode = getCompanyCode(zkSecReq);
        long osType = getOsType(zkSecReq);
        String udid = getUdid(zkSecReq);
        long appType = getAppType(zkSecReq);
        String appId = getAppId(zkSecReq);

        String username = zkSecReq.getCleanParam(ZKSecConstants.PARAM_NAME.Username);
        boolean rememberMe = ZKUtils.isTrue(zkSecReq.getCleanParam(ZKSecConstants.PARAM_NAME.RememberMe));
        String pwd = zkSecReq.getCleanParam(ZKSecConstants.PARAM_NAME.Pwd);
        return new ZKSecDefaultAuthcUserToken(companyCode, username, pwd, rememberMe, osType, udid, appType, appId);

    }

    public static String getCompanyCode(ZKSecRequestWrapper zkReq) {
        String companyCode = zkReq.getCleanParam(ZKSecConstants.PARAM_NAME.CompanyCode);
        if (ZKStringUtils.isEmpty(companyCode)) {
            return ZKSysUtils.getOwnerCompanyCode();
        }
        return companyCode;
    }

}
