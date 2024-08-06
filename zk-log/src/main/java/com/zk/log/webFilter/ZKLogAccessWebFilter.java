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
* @Title: ZKLogAccessWebFilter.java 
* @author Vinson 
* @Package com.zk.log.webFilter 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 28, 2023 2:15:20 PM 
* @version V1.0 
*/
package com.zk.log.webFilter;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import com.zk.core.web.support.webFlux.filter.ZKOncePerWebFilter;
import com.zk.log.common.ZKLogUtils;

import reactor.core.publisher.Mono;

/** 
* @ClassName: ZKLogAccessWebFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKLogAccessWebFilter extends ZKOncePerWebFilter {

    /**
     * (not Javadoc)
     * <p>
     * Title: doFilterInternal
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param exchange
     * @param chain
     * @return
     * @see com.zk.core.web.webFilter.ZKWebOncePerFilter#doFilterInternal(org.springframework.web.server.ServerWebExchange,
     *      org.springframework.web.server.WebFilterChain)
     */
    @Override
    protected Mono<Void> doFilterInternal(ServerWebExchange exchange, WebFilterChain chain) {
        Exception ex = null;
        try {
            return chain.filter(exchange);
        }
        catch(Exception e) {
            ex = e;
            throw e;
        } finally {
            // 保存日志
            ZKLogUtils.saveAccessLog(exchange, ex);
        }
    }

}
