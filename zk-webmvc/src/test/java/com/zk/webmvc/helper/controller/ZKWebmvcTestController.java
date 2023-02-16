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

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.webmvc.helper.entity.ZKWebmvcTestEntity;

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

    @Value("${zk.server.port:8080}")
    int zkServerPort;

    @RequestMapping({ "", "index" })
    public String index(HttpServletResponse hRes) {
        System.out.println("[^_^:20190624-1022-001] index.zkServerPort:" + zkServerPort + "; res msg: " + msg_index);
        return msg_index;
    }

    @RequestMapping("get")
    public String get(@RequestParam("param") String param) {
        System.out.println("[^_^:20191213-1144-001] ZKHttpApiUtilsController.get -> param:" + param);
        return msg_index + param;
    }

    @RequestMapping(value = "post", method = RequestMethod.POST)
    public String post(@RequestParam("param") String param) {
        System.out.println("[^_^:20191213-1144-002] ZKHttpApiUtilsController.post -> param:" + param);
        return msg_index + param;
    }

    @RequestMapping(value = "postJson", method = RequestMethod.POST)
    public ZKWebmvcTestEntity postJson(@RequestParam("param") String param, @RequestBody ZKWebmvcTestEntity entity) {
        System.out.println("[^_^:20191213-1144-003] ZKHttpApiUtilsController.postJson -> param:" + param);
        entity.setRemark(param);
        return entity;
    }

}
