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
* @Title: ZKWebfluxTestController.java 
* @author Vinson 
* @Package com.zk.webflux.helper.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 22, 2024 5:36:05 PM 
* @version V1.0 
*/
package com.zk.webflux.helper.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.ZKMsgRes.ResCodeType;
import com.zk.core.exception.ZKSystemException;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.webflux.helper.entity.ZKWebfluxTestEntity;

import reactor.core.publisher.Mono;

/** 
* @ClassName: ZKWebfluxTestController 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RestController
@RequestMapping("${zk.path.admin:zk}/${zk.path.webflux}")
public class ZKWebfluxTestController {

    public static final String msg_index = "zk webflux 海棠春睡早，杨柳昼眠迟。";

    @Value("${server.port}")
    int zkServerPort;

    /**
     * index
     *
     * @Title: index
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 2, 2024 8:59:14 AM
     * @param exchange
     * @param shReq
     * @param shRes
     * @return
     * @return Mono<String>
     */
    @RequestMapping({ "", "index" })
    public Mono<String> index(ServerWebExchange exchange, ServerHttpRequest shReq, ServerHttpResponse shRes) { // HttpServerRequest hsRes
        String msg = "index.serverPort:" + zkServerPort + "; res msg: " + msg_index;
        System.out.println("[^_^:20190624-1022-001] ZKWebfluxTestController.index -> " + msg);
        try {
            Locale locale = ZKWebUtils.getLocale();
            System.out.println("[^_^:20190624-1022-002] " + locale.toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return Mono.just(msg_index);
    }

    /**
     * get 请求参数
     *
     * @Title: get
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 2, 2024 8:59:04 AM
     * @param param
     * @return
     * @return Mono<String>
     */
    @RequestMapping("get")
    public Mono<String> get(@RequestParam("param") String param) {
        System.out.println("[^_^:20240104-1144-001] ZKWebfluxTestController.get -> param:" + param);
        return Mono.just(msg_index + param);
    }

    /**
     * post 请求参数
     *
     * @Title: post
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 2, 2024 8:58:54 AM
     * @param param
     * @return
     * @return Mono<String>
     */
    @RequestMapping(value = "post", method = RequestMethod.POST)
    public Mono<String> post(@RequestParam("param") String param) {
        System.out.println("[^_^:20240104-1144-002] ZKWebfluxTestController.post -> param:" + param);
        return Mono.just(msg_index + param);
    }

    @RequestMapping(value = "zkMsgRes")
    public ZKMsgRes zkMsgRes(@RequestParam("param") String param, ServerHttpRequest req) {
        System.out.println("[^_^:20240104-1144-012] ZKWebfluxTestController.zkMsgRes -> param:" + param);
        ZKMsgRes ms = ZKMsgRes.asCode(ZKWebUtils.getLocale(req), ResCodeType.system, "zk.000003");
        ms.setData(param);
        return ms;
//        return Mono.just(ms);
//        return Mono.just(ms).delayElement(Duration.ofSeconds(20)).timeout(Duration.ofSeconds(5));
    }

    /**
     * 请求参数以及 requestBody 参数
     *
     * @Title: postJson
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 2, 2024 8:58:28 AM
     * @param param
     * @param entity
     * @return
     * @return Mono<ZKWebfluxTestEntity>
     */
    @RequestMapping(value = "postJson", method = RequestMethod.POST)
    public Mono<ZKWebfluxTestEntity> postJson(@RequestParam("param") String param,
            @RequestBody ZKWebfluxTestEntity entity) {
        System.out.println("[^_^:20240104-1144-003] ZKWebfluxTestController.postJson -> param:" + param);
        entity.setRemark(param);
        return Mono.just(entity);
    }

    /**
     * 请求信息
     *
     * @Title: reqInfo
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 2, 2024 9:01:16 AM
     * @param exchange
     * @param shReq
     * @param param
     * @param pathVariable
     * @return void
     */
    @RequestMapping(value = "reqInfo/{pathVariable}", method = { RequestMethod.POST, RequestMethod.GET })
    public void reqInfo(ServerWebExchange exchange, ServerHttpRequest shReq, @RequestParam(name = "param") String param,
            @PathVariable(name = "pathVariable") String pathVariable) {
        System.out.println("=============================================");
        System.out.println("=== request info ");
        System.out.println("=============================================");
        System.out.println("[^_^:20190905-1149-001] getURI.getPath():                      " + shReq.getURI().getPath());
        System.out.println("[^_^:20190905-1149-001] getURI.getHost():                      " + shReq.getURI().getHost());
        System.out.println("[^_^:20190905-1149-001] getURI.getPort():                      " + shReq.getURI().getPort());
        System.out.println("[^_^:20190905-1149-001] getURI.getScheme():                    " + shReq.getURI().getScheme());
        System.out.println("[^_^:20190905-1149-001] getURI.getRawPath():                   " + shReq.getURI().getRawPath());
        System.out.println("[^_^:20190905-1149-001] getURI.getQuery():                     " + shReq.getURI().getQuery());
        System.out.println("[^_^:20190905-1149-001] getURI.getUserInfo():                  " + shReq.getURI().getUserInfo());

        System.out.println("[^_^:20190905-1149-001] getPath.contextPath().value:           " + shReq.getPath().contextPath().value());
        System.out.println("[^_^:20190905-1149-001] getPath.pathWithinApplication().value: " + shReq.getPath().pathWithinApplication().value());
        
        System.out.println("[^_^:20190905-1149-001] getMethod.name():                      " + shReq.getMethod().name());
        System.out.println("[^_^:20190905-1149-001] getHeaders.getFirst(\"Host\"):           "
                + shReq.getHeaders().getFirst("Host"));

//        =============================================
//        === request info  GET
//        =============================================
//        [^_^:20190905-1149-001] getURI.getPath():                      /zk/x/reqInfo/pathParamValue
//        [^_^:20190905-1149-001] getURI.getHost():                      127.0.0.1
//        [^_^:20190905-1149-001] getURI.getPort():                      55598
//        [^_^:20190905-1149-001] getURI.getScheme():                    http
//        [^_^:20190905-1149-001] getURI.getRawPath():                   /zk/x/reqInfo/pathParamValue
//        [^_^:20190905-1149-001] getURI.getQuery():                     param=paramValue
//        [^_^:20190905-1149-001] getURI.getUserInfo():                  null
//        [^_^:20190905-1149-001] getPath.contextPath().value:           
//        [^_^:20190905-1149-001] getPath.pathWithinApplication().value: /zk/x/reqInfo/pathParamValue
//        [^_^:20190905-1149-001] getMethod.name():                      GET
//        [^_^:20190905-1149-001] getHeaders.getFirst("Host"):           127.0.0.1:55598

//
//        =============================================
//        === request info POST
//        =============================================
//        [^_^:20190905-1149-001] getURI.getPath():                      /zk/x/reqInfo/pathParamValue
//        [^_^:20190905-1149-001] getURI.getHost():                      127.0.0.1
//        [^_^:20190905-1149-001] getURI.getPort():                      55598
//        [^_^:20190905-1149-001] getURI.getScheme():                    http
//        [^_^:20190905-1149-001] getURI.getRawPath():                   /zk/x/reqInfo/pathParamValue
//        [^_^:20190905-1149-001] getURI.getQuery():                     param=paramValue
//        [^_^:20190905-1149-001] getURI.getUserInfo():                  null
//        [^_^:20190905-1149-001] getPath.contextPath().value:           
//        [^_^:20190905-1149-001] getPath.pathWithinApplication().value: /zk/x/reqInfo/pathParamValue
//        [^_^:20190905-1149-001] getMethod.name():                      POST
//        [^_^:20190905-1149-001] getHeaders.getFirst("Host"):           127.0.0.1:55598

    }

    @RequestMapping(value = "exception")
    public Mono<String> exception(@RequestParam("errCode") String errCode) throws Exception {
        System.out.println("[^_^:20240106-0014-001] ZKWebfluxTestController.errCode -> errCode:" + errCode);
        /*
         * zk.0=Successful. zk.1=Sytem error. zk.000003=The project environment is incorrectly configured
         */
        for (String s : new String[] { "zk.0", "zk.1", "zk.000003" }) {
            if (s.equals(errCode)) {
                throw ZKSystemException.as(errCode, null);
            }
        }
        throw new Exception(errCode);
    }

}
