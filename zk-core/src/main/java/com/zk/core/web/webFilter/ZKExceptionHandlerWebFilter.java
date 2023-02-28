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
* @Title: ZKExceptionHandlerWebFilter.java 
* @author Vinson 
* @Package com.zk.core.web.webFilter 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 28, 2023 11:37:45 AM 
* @version V1.0 
*/
package com.zk.core.web.webFilter;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.web.ZKWebConstants.HeaderKey;
import com.zk.core.web.ZKWebConstants.HeaderValue;

import reactor.core.publisher.Mono;

/** 
* @ClassName: ZKExceptionHandlerWebFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKExceptionHandlerWebFilter extends ZKOncePerWebFilter {

    /**
     * (not Javadoc)
     * 
     * @param exchange
     * @param chain
     * @return
     * @see com.zk.core.web.webFilter.ZKWebOncePerFilter#doFilterInternal(org.springframework.web.server.ServerWebExchange,
     *      org.springframework.web.server.WebFilterChain)
     */
    @Override
    protected Mono<Void> doFilterInternal(ServerWebExchange exchange, WebFilterChain chain) {
        
        ServerHttpResponse hRes = exchange.getResponse();

        Exception ex = null;
        try {
            return chain.filter(exchange);
        }
        catch(Exception e) {
            ex = e;
        } finally {
            if (ex != null) {
                /* 使用response返回 */
//                exchange.getResponse().setStatusCode(HttpStatus.OK);
                hRes.setStatusCode(HttpStatus.OK); // 设置状态码
                hRes.getHeaders().add(HeaderKey.contentType, HeaderValue.APPLICATION_JSON_UTF8_VALUE); // 设置ContentType
//                hRes.setCharacterEncoding("UTF-8"); // 避免乱码
                hRes.getHeaders().add(HeaderKey.cacheControl, "no-cache, must-revalidate");
                ZKMsgRes msgRes = ZKExceptionsUtils.as(ex);
                log.error("[>_<:20230214-2357-001] {} 请求处理结果异常 -> status code:{}; msg:{}; exception class:{}; ",
                        this.getClass().getSimpleName(), hRes.getStatusCode().value(), msgRes.toString(), ex.getClass().getName());
                ex.printStackTrace();
//                Publisher p = new Publisher(msgRes);
//                Mono<ZKMsgRes> m = Mono.just(msgRes);
//                hRes.writeAndFlushWith(m);
//                return m;
            }
        }
        return null;
    }

}
