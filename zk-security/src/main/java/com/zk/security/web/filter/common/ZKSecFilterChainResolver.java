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
* @Package com.zk.security.web.filter.common
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 27, 2021 8:17:00 PM 
* @version V1.0 
*/
package com.zk.security.web.filter.common;

import com.zk.security.web.wrapper.ZKSecRequestWrapper;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;

/** 
* @ClassName: ZKSecFilterChainResolver 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecFilterChainResolver {

    ZKSecFilterChain getChain(ZKSecRequestWrapper zkRequest, ZKSecResponseWrapper zkResponse, ZKSecFilterChain chain);
}
