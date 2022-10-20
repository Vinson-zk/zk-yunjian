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
* @Title: ZKSecFilterChainResolver.java 
* @author Vinson 
* @Package com.zk.security.web.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 27, 2021 8:17:00 PM 
* @version V1.0 
*/
package com.zk.security.web.common;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/** 
* @ClassName: ZKSecFilterChainResolver 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecFilterChainResolver {

    ZKSecFilterChain getChain(ServletRequest request, ServletResponse response, FilterChain original);
}
