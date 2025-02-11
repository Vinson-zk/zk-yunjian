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
* @Title: ZKCoreHandlerFunction.java 
* @author Vinson 
* @Package com.zk.core.web.support.webFlux.handler.function 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 10, 2025 6:06:49 PM 
* @version V1.0 
*/
package com.zk.core.web.support.webFlux.handler.function;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

/** 
* @ClassName: ZKCoreHandlerFunction 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCoreHandlerFunction {

    public Mono<ServerResponse> handleFavicon(ServerRequest request) {
//        return ServerResponse.ok().bodyValue("favicon.ico");
        return Mono.justOrEmpty(null);
    }

}
