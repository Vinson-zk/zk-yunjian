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
* @Title: ZKServletWebFilterChainHandle.java 
* @author Vinson 
* @Package com.zk.core.web.webFilter 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 28, 2023 4:09:43 PM 
* @version V1.0 
*/
package com.zk.core.web.webFilter;
/** 
* @ClassName: ZKServletWebFilterChainHandle 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@FunctionalInterface
public interface ZKServletWebFilterChainHandle {
    void handle();
}
