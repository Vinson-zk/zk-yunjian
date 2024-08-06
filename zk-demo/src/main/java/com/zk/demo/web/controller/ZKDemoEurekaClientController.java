/**   
 * Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * ZK-Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with ZK-Vinson. 
 *
 * @Title: ZKDemoEurekaClientController.java 
 * @author Vinson 
 * @Package com.zk.demo.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Mar 11, 2020 9:53:53 PM 
 * @version V1.0   
*/
package com.zk.demo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/** 
* @ClassName: ZKDemoEurekaClientController 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@RestController
@RequestMapping("${zk.path.admin}/${zk.path.demo}/eurekaClient/demo")
public class ZKDemoEurekaClientController {

    public static final String name = "ZKSerCenEurekaClientApp";

    @Value("${server.port}")
    String port;

    @Value("${zk.path.admin}")
    String adminPath;

    @Value("${zk.path.demo}")
    String demoPath;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping
    public String index() {
        return name + ":" + port;
    }

    @RequestMapping("self")
    public String self() {
        String url = String.format("http://zk-demo-eureka-client-app/%s/%s/eurekaClient/demo", adminPath, demoPath);
        System.out.println("[^_^:20200311-1016-001] ZKSerCenEurekaClientApp.url:" + url);
        String s = restTemplate.getForObject(url, String.class);
        return s;
    }

}
