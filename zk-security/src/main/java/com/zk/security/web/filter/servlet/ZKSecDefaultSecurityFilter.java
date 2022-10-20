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
* @Title: ZKSecDefaultSecurityFilter.java 
* @author Vinson 
* @Package com.zk.security.web.filter.servlet 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 11:42:49 AM 
* @version V1.0 
*/
package com.zk.security.web.filter.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zk.security.web.common.ZKSecFilterChainResolver;
import com.zk.security.web.mgt.ZKSecWebSecurityManager;

/** 
* @ClassName: ZKSecDefaultSecurityFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecDefaultSecurityFilter extends ZKSecSecurityAbstractFilter {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public ZKSecDefaultSecurityFilter(ZKSecWebSecurityManager securityManager,
            ZKSecFilterChainResolver filterChainResolver) {
        super(securityManager, filterChainResolver);
    }

}
