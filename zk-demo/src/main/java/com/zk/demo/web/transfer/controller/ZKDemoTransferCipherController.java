/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKDemoTransferCipherController.java 
 * @author Vinson 
 * @Package com.zk.demo.transfer.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 5:01:55 PM 
 * @version V1.0   
*/
package com.zk.demo.web.transfer.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.web.encrypt.ZKSampleRsaAesTransferCipherManager;
import com.zk.demo.web.commons.ZKDemoConstants;
import com.zk.demo.web.entity.ZKDemoEntity;

import jakarta.servlet.http.HttpServletRequest;

/** 
* @ClassName: ZKDemoTransferCipherController 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@RestController
@RequestMapping("${zk.path.admin:zk}/${zk.path.core:demo}/tc")
public class ZKDemoTransferCipherController {

    private Logger log = LogManager.getLogger(getClass());

    @Autowired
    private ZKSampleRsaAesTransferCipherManager zkSampleRsaAesTransferCipherManager;

    private interface RequestParams extends ZKDemoConstants.RequestParams {
    }

    // 取 rsa publicKey
    @RequestMapping("rsa")
    public String rsa() {
        String publicKey = zkSampleRsaAesTransferCipherManager.getZKRSAKeyByRsaId(null).getPublicKeyStr();
        log.info("[^_^:20190629-1626-001] publicKey:" + publicKey);
        return publicKey;
    }

    // post delete put 有请求参数与请求体；路径参数，请求头等
    @RequestMapping("index/{" + RequestParams.pn_pathVariable1 + "}/{" + RequestParams.pn_pathVariable2 + "}")
    public Map<String, ?> transferCipher(@PathVariable(RequestParams.pn_pathVariable1) String variable1,
            @PathVariable(name = RequestParams.pn_pathVariable2) String variable2,
            @RequestParam(value = RequestParams.pn_strs, required = true) String[] strs,
            @RequestParam(value = RequestParams.pn_int, required = true) int intValue,
            @RequestHeader(value = RequestParams.pn_req_header, required = false) String header,
            @RequestBody ZKDemoEntity demoEntity, HttpServletRequest req) {

        Map<String, Object> resMap = new HashMap<String, Object>();

        resMap.put(RequestParams.pn_pathVariable1, variable1);
        resMap.put(RequestParams.pn_pathVariable2, variable2);
        resMap.put(RequestParams.pn_strs, ZKJsonUtils.toJsonStr(strs));
        resMap.put(RequestParams.pn_int, intValue);
        resMap.put(RequestParams.RequestBody, demoEntity);
        resMap.put(RequestParams.pn_req_header, req.getHeader(RequestParams.pn_req_header));
//        resMap.put(RequestParams.pn_req_attr, req.getAttribute(RequestParams.pn_req_attr));

        log.info("[^_^:20190625-0902-001] 测试传输加解密方法，index getRequestURI:" + req.getRequestURI());
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String eStr = headerNames.nextElement();
            log.info("[^_^:20190625-0903-001] header." + eStr + ": " + req.getHeader(eStr).toString());
        }

        log.info("[^_^:20190625-0903-002] resMap:" + ZKJsonUtils.toJsonStr(resMap));

        return resMap;
    }

    // get post delete pub 无请求参数也无请求体
    @RequestMapping("noReqBody/{" + RequestParams.pn_pathVariable1 + "}/{" + RequestParams.pn_pathVariable2 + "}")
    public Map<String, ?> noReqBody(@PathVariable(RequestParams.pn_pathVariable1) String variable1,
            @PathVariable(name = RequestParams.pn_pathVariable2) String variable2,
            @RequestParam(value = RequestParams.pn_strs, required = true) String[] strs,
            @RequestParam(value = RequestParams.pn_int, required = true) int intValue,
            @RequestHeader(value = RequestParams.pn_req_header, required = false) String header,
            HttpServletRequest req) {

        Map<String, Object> resMap = new HashMap<String, Object>();

        resMap.put(RequestParams.pn_pathVariable1, variable1);
        resMap.put(RequestParams.pn_pathVariable2, variable2);
        resMap.put(RequestParams.pn_strs, ZKJsonUtils.toJsonStr(strs));
        resMap.put(RequestParams.pn_int, intValue);
        resMap.put(RequestParams.pn_req_header, req.getHeader(RequestParams.pn_req_header));
//        resMap.put(RequestParams.pn_req_attr, req.getAttribute(RequestParams.pn_req_attr));

        log.info("[^_^:20190627-0902-001] 测试传输加解密方法，noReqBody getRequestURI:" + req.getRequestURI());
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String eStr = headerNames.nextElement();
            log.info("[^_^:20190627-0903-001] header." + eStr + ": " + req.getHeader(eStr).toString());
        }

        log.info("[^_^:20190627-0907-002] resMap:" + ZKJsonUtils.toJsonStr(resMap));

        return resMap;
    }

    // get post delete put 无请求参数
    @RequestMapping("no")
    public void no() {

        log.info("[^_^:20190627-1439-001] 无参数，无响应数据 接口");

    }

}
