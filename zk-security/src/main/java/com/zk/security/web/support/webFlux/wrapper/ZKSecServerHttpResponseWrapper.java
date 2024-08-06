/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSecServerHttpResponseWrapper.java 
* @author Vinson 
* @Package com.zk.security.web.support.webFlux.wrapper 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 21, 2024 11:56:14 PM 
* @version V1.0 
*/
package com.zk.security.web.support.webFlux.wrapper;

import org.springframework.web.server.ServerWebExchange;

import com.zk.core.web.support.webFlux.wrapper.ZKServerHttpResponseWrapper;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;

/** 
* @ClassName: ZKSecServerHttpResponseWrapper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecServerHttpResponseWrapper extends ZKServerHttpResponseWrapper implements ZKSecResponseWrapper {

    public ZKSecServerHttpResponseWrapper(ServerWebExchange exchange) {
        super(exchange); // TODO Auto-generated constructor stub
    }


}
