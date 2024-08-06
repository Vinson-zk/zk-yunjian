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
 * @Title: ZKWebmvcTestController.java 
 * @author Vinson 
 * @Package com.zk.core.helper.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 12, 2019 5:20:25 PM 
 * @version V1.0   
*/
package com.zk.webmvc.helper.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.ZKMsgRes.ResCodeType;
import com.zk.core.exception.ZKSystemException;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.webmvc.helper.entity.ZKWebmvcTestEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reactor.core.publisher.Mono;

/**
 * @ClassName: ZKWebmvcTestController
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
@RestController
@RequestMapping("${zk.path.admin:zk}/${zk.path.webmvc:c}")
public class ZKWebmvcTestController {

    public static final String msg_index = "zk mvc 暮色渔舟归，江海一色平。";

    @Value("${zk.webmvc.server.port:8080}")
    int zkServerPort;

    /**
     * index
     *
     * @Title: index
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 2, 2024 8:59:39 AM
     * @param hReq
     * @param hRes
     * @return
     * @return String
     */
    @RequestMapping({ "", "index" })
    public String index(HttpServletRequest hReq, HttpServletResponse hRes) {
        System.out.println("[^_^:20190624-1023-001] index.zkServerPort:" + zkServerPort + "; res msg: " + msg_index);
        Locale locale = ZKWebUtils.getLocale();
        System.out.println("[^_^:20190624-1023-011] " + locale.toString());
        return msg_index;
    }

    /**
     * get 请求参数
     *
     * @Title: get
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 2, 2024 8:59:43 AM
     * @param param
     * @return
     * @return String
     */
    @RequestMapping("get")
    public String get(@RequestParam("param") String param) {
        System.out.println("[^_^:20191213-1144-001] ZKHttpApiUtilsController.get -> param:" + param);
        return msg_index + param;
    }

    /**
     * post 请求参数
     *
     * @Title: post
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 2, 2024 8:59:51 AM
     * @param param
     * @return
     * @return String
     */
    @RequestMapping(value = "post", method = RequestMethod.POST)
    public String post(@RequestParam("param") String param) {
        System.out.println("[^_^:20191213-1144-002] ZKHttpApiUtilsController.post -> param:" + param);
        return msg_index + param;
    }

    @RequestMapping(value = "zkMsgRes")
    public ZKMsgRes zkMsgRes(@RequestParam("param") String param) {
        System.out.println("[^_^:20191213-1144-012] ZKHttpApiUtilsController.zkMsgRes -> param:" + param);
        ZKMsgRes ms = ZKMsgRes.asCode(null, ResCodeType.system, "zk.000003");
        ms.setData(param);
        return ms;
    }

    /**
     * 请求参数和requestBody 参数
     *
     * @Title: postJson
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 2, 2024 8:59:54 AM
     * @param param
     * @param entity
     * @return
     * @return ZKWebmvcTestEntity
     */
    @RequestMapping(value = "postJson", method = RequestMethod.POST)
    public ZKWebmvcTestEntity postJson(@RequestParam("param") String param, @RequestBody ZKWebmvcTestEntity entity) {
        System.out.println("[^_^:20191213-1144-003] ZKHttpApiUtilsController.postJson -> param:" + param);
        entity.setRemark(param);
        return entity;
    }

    /**
     * request 请求信息
     *
     * @Title: reqInfo
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 2, 2024 8:59:59 AM
     * @param req
     * @param param
     * @param pathVariable
     * @return void
     */
    @RequestMapping(value = "reqInfo/{pathVariable}", method = { RequestMethod.POST, RequestMethod.GET })
    public void reqInfo(HttpServletRequest req, @RequestParam(name = "param") String param,
            @PathVariable(name = "pathVariable") String pathVariable) {
        System.out.println("=============================================");
        System.out.println("=== request info ");
        System.out.println("=============================================");
        System.out.println("[^_^:20190905-1149-001] req.getContextPath():      " + req.getContextPath());
        System.out.println("[^_^:20190905-1149-001] req.getMethod():           " + req.getMethod());
        System.out.println("[^_^:20190905-1149-001] req.getQueryString():      " + req.getQueryString());
        System.out.println("[^_^:20190905-1149-001] req.getProtocol():         " + req.getProtocol());
        System.out.println("[^_^:20190905-1149-001] req.getPathInfo():         " + req.getPathInfo());
        System.out.println("[^_^:20190905-1149-001] req.getPathTranslated():   " + req.getPathTranslated());
        System.out.println("[^_^:20190905-1149-001] req.getServletPath():      " + req.getServletPath());
        System.out.println("[^_^:20190905-1149-001] req.getRemoteAddr():       " + req.getRemoteAddr());
        System.out.println("[^_^:20190905-1149-001] req.getRemoteHost():       " + req.getRemoteHost());
        System.out.println("[^_^:20190905-1149-001] req.getLocalAddr():        " + req.getLocalAddr());
        System.out.println("[^_^:20190905-1149-001] req.getLocalName():        " + req.getLocalName());
        System.out.println("[^_^:20190905-1149-001] req.getLocalPort():        " + req.getLocalPort());
        System.out.println("[^_^:20190905-1149-001] req.getServerName():       " + req.getServerName());
        System.out.println("[^_^:20190905-1149-001] req.getServerPort():       " + req.getServerPort());
        System.out.println("[^_^:20190905-1149-001] req.getScheme():           " + req.getScheme());
        System.out.println("[^_^:20190905-1149-001] req.getRequestURI():       " + req.getRequestURI());
        System.out.println("[^_^:20190905-1149-001] req.getHeader(\"Host\"):   " + req.getHeader("Host"));

//        =============================================
//        === request info 
//        =============================================
//        [^_^:20190905-1149-001] req.getContextPath():      
//        [^_^:20190905-1149-001] req.getMethod():           GET
//        [^_^:20190905-1149-001] req.getQueryString():      param=paramValue
//        [^_^:20190905-1149-001] req.getProtocol():         HTTP/1.1
//        [^_^:20190905-1149-001] req.getPathInfo():         null
//        [^_^:20190905-1149-001] req.getPathTranslated():   null
//        [^_^:20190905-1149-001] req.getServletPath():      /zk/c/reqInfo/pathParamValue
//        [^_^:20190905-1149-001] req.getRemoteAddr():       127.0.0.1
//        [^_^:20190905-1149-001] req.getRemoteHost():       127.0.0.1
//        [^_^:20190905-1149-001] req.getLocalAddr():        127.0.0.1
//        [^_^:20190905-1149-001] req.getLocalName():        localhost
//        [^_^:20190905-1149-001] req.getLocalPort():        54165
//        [^_^:20190905-1149-001] req.getServerName():       127.0.0.1
//        [^_^:20190905-1149-001] req.getServerPort():       54165
//        [^_^:20190905-1149-001] req.getScheme():           http
//        [^_^:20190905-1149-001] req.getRequestURI():       /zk/c/reqInfo/pathParamValue
//        [^_^:20190905-1149-001] req.getHeader("Host"):     127.0.0.1:54165
//
//        =============================================
//        === request info 
//        =============================================
//        [^_^:20190905-1149-001] req.getContextPath():      
//        [^_^:20190905-1149-001] req.getMethod():           POST
//        [^_^:20190905-1149-001] req.getQueryString():      param=paramValue
//        [^_^:20190905-1149-001] req.getProtocol():         HTTP/1.1
//        [^_^:20190905-1149-001] req.getPathInfo():         null
//        [^_^:20190905-1149-001] req.getPathTranslated():   null
//        [^_^:20190905-1149-001] req.getServletPath():      /zk/c/reqInfo/pathParamValue
//        [^_^:20190905-1149-001] req.getRemoteAddr():       127.0.0.1
//        [^_^:20190905-1149-001] req.getRemoteHost():       127.0.0.1
//        [^_^:20190905-1149-001] req.getLocalAddr():        127.0.0.1
//        [^_^:20190905-1149-001] req.getLocalName():        localhost
//        [^_^:20190905-1149-001] req.getLocalPort():        54165
//        [^_^:20190905-1149-001] req.getServerName():       127.0.0.1
//        [^_^:20190905-1149-001] req.getServerPort():       54165
//        [^_^:20190905-1149-001] req.getScheme():           http
//        [^_^:20190905-1149-001] req.getRequestURI():       /zk/c/reqInfo/pathParamValue
//        [^_^:20190905-1149-001] req.getHeader("Host"):     127.0.0.1:54165
    }

    @RequestMapping(value = "exception")
    public Mono<String> exception(@RequestParam("errCode") String errCode) throws Exception {
        System.out.println("[^_^:20240106-0014-011] ZKWebfluxTestController.errCode -> errCode:" + errCode);
        /*
         * zk.0=Successful. 
         * zk.1=Sytem error. 
         * zk.000003=The project environment is incorrectly configured.
         * 
         * zk.0=成功
         * zk.1=系统异常
         * zk.000003=项目环境配置错误
         */
        for (String s : new String[] { "zk.0", "zk.1", "zk.000003" }) {
            if (s.equals(errCode)) {
                throw ZKSystemException.as(errCode, null);
            }
        }
        throw new Exception(errCode);
    }

}
