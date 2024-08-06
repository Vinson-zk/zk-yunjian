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
* @Title: ZKWebfluxTestTransferCipherController.java 
* @author Vinson 
* @Package com.zk.webflux.helper.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 25, 2024 12:16:28 AM 
* @version V1.0 
*/
package com.zk.webflux.helper.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.zk.core.utils.ZKJsonUtils;

/**
 * @ClassName: ZKWebfluxTestTransferCipherController
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@RestController
@RequestMapping("${zk.path.admin:zk}/${zk.path.webflux}/tc")
public class ZKWebfluxTestTransferCipherController {

    /**
     * 测试时的一些参数名称
     */
    public static interface RequestParams {

        public static final String pn_pathVariable1 = "pn_pathVariable1";

        public static final String pn_pathVariable2 = "pn_pathVariable2";

        public static final String pn_int = "pn_int";

        public static final String pn_strs = "pn_strs";

        public static final String pn_req_header = "pn_req_header";

        public static final String RequestBody = "RequestBody";

        public static final String pn_req_attr = "pn_req_attr";

    }

    public static interface msg {

        public static final String normal_transferCipher = "normal transferCipher -> ";

        public static final String normal_transferCipherNoReqBody = "normal transferCipherNoReqBody -> ";

        public static final String value = "值 -> ";
    }

    @RequestMapping("transferCipher/{" + RequestParams.pn_pathVariable1 + "}/{" + RequestParams.pn_pathVariable2 + "}")
    public Map<String, ?> transferCipher(@PathVariable(RequestParams.pn_pathVariable1) String variable1,
            @PathVariable(name = RequestParams.pn_pathVariable2) String variable2,
            @RequestParam(value = RequestParams.pn_strs, required = true) String[] strs,
            @RequestParam(value = RequestParams.pn_int, required = true) int intValue,
            @RequestHeader(value = RequestParams.pn_req_header, required = false) String header,
            @RequestBody Map<String, ?> bodyParam, ServerHttpRequest req, ServerWebExchange exchange) {

        Map<String, Object> resMap = new HashMap<String, Object>();

        resMap.put(RequestParams.pn_pathVariable1, msg.normal_transferCipher + variable1);
        resMap.put(RequestParams.pn_pathVariable2, msg.normal_transferCipher + variable2);
        resMap.put(RequestParams.pn_strs, msg.normal_transferCipher + ZKJsonUtils.toJsonStr(strs));
        resMap.put(RequestParams.pn_int, intValue * intValue);
        resMap.put(RequestParams.RequestBody, bodyParam);
        resMap.put(RequestParams.pn_req_header, msg.normal_transferCipher + req.getHeaders().getFirst(RequestParams.pn_req_header));
        resMap.put(RequestParams.pn_req_attr,
                msg.normal_transferCipher + exchange.getAttribute((RequestParams.pn_req_attr)));

        System.out.println("[^_^:20190625-0902-001] 测试传输加解密方法，transferCipher getRequestURI:" + req.getURI().getPath());
        Set<String> headerNames = req.getHeaders().keySet();
        Iterator<String> headerNamesIterator = headerNames.iterator();
        while (headerNamesIterator.hasNext()) {
            String eStr = headerNamesIterator.next();
            System.out.println("[^_^:20190625-0903-001] header." + eStr + ": " + req.getHeaders().getFirst(eStr));
        }

        System.out.println("[^_^:20190625-0903-002] resMap:" + ZKJsonUtils.toJsonStr(resMap));

        return resMap;
    }

    @RequestMapping("transferCipherNoReqBody/{" + RequestParams.pn_pathVariable1 + "}/{"
            + RequestParams.pn_pathVariable2 + "}")
    public Map<String, ?> transferCipherNoReqBody(@PathVariable(RequestParams.pn_pathVariable1) String variable1,
            @PathVariable(name = RequestParams.pn_pathVariable2) String variable2,
            @RequestParam(value = RequestParams.pn_strs, required = true) String[] strs,
            @RequestParam(value = RequestParams.pn_int, required = true) int intValue,
            @RequestHeader(value = RequestParams.pn_req_header, required = false) String header,
            ServerHttpRequest req, ServerWebExchange exchange) {

        Map<String, Object> resMap = new HashMap<String, Object>();

        resMap.put(RequestParams.pn_pathVariable1, msg.normal_transferCipherNoReqBody + variable1);
        resMap.put(RequestParams.pn_pathVariable2, msg.normal_transferCipherNoReqBody + variable2);
        resMap.put(RequestParams.pn_strs, msg.normal_transferCipherNoReqBody + ZKJsonUtils.toJsonStr(strs));
        resMap.put(RequestParams.pn_int, intValue * intValue);
        resMap.put(RequestParams.pn_req_header,
                msg.normal_transferCipherNoReqBody + req.getHeaders().getFirst(RequestParams.pn_req_header));
        resMap.put(RequestParams.pn_req_attr,
                msg.normal_transferCipherNoReqBody + exchange.getAttribute(RequestParams.pn_req_attr));

        System.out.println("[^_^:20190627-0902-001] 测试传输加解密方法，transferCipher getRequestURI:" + req.getURI().getPath());

        Set<String> headerNames = req.getHeaders().keySet();
        Iterator<String> headerNamesIterator = headerNames.iterator();
        while (headerNamesIterator.hasNext()) {
            String eStr = headerNamesIterator.next();
            System.out.println(
                    "[^_^:20190627-0903-001] header." + eStr + ": " + req.getHeaders().getFirst(eStr).toString());
        }

        System.out.println("[^_^:20190627-0907-002] resMap:" + ZKJsonUtils.toJsonStr(resMap));

        return resMap;
    }

    @RequestMapping("transferCipherNo")
    public void transferCipherNo() {
        System.out.println("[^_^:20190627-1439-001] 无参数，无响应数据 接口");
    }

}


