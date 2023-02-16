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
 * @Title: ZKWebmvcTestControllerTest.java 
 * @author Vinson 
 * @Package com.zk.webmvc.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 3:20:22 PM 
 * @version V1.0   
*/
package com.zk.webmvc.controller;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.webmvc.helper.ZKWebmvcChildConfiguration;
import com.zk.webmvc.helper.ZKWebmvcParentConfiguration;
import com.zk.webmvc.helper.ZKWebmvcTestHelperMvcSpringBootMain;
import com.zk.webmvc.helper.controller.ZKWebmvcTestController;
import com.zk.webmvc.helper.entity.ZKWebmvcTestEntity;

import junit.framework.TestCase;

/**
 * @ClassName: ZKWebmvcTestControllerTest
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
@SuppressWarnings("deprecation")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ZKWebmvcParentConfiguration.class,
        ZKWebmvcChildConfiguration.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ZKWebmvcTestControllerTest {

    static {
        try {
            ZKWebmvcTestHelperMvcSpringBootMain.exit();
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }

    }

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() {
        this.baseUrl = String.format("http://127.0.0.1:%s/%s/%s", port,
                ZKEnvironmentUtils.getString("zk.path.admin", "zk"),
                ZKEnvironmentUtils.getString("zk.path.core", "c"));
        System.out.println("[^_^:20191218-1555-001] base url:" + this.baseUrl);
    }

    @Test
    public void testIndex() {
        try {
            String resStr = "";
            ResponseEntity<String> response = template.getForEntity(this.baseUrl, String.class, new HashMap<>());
            resStr = response.getBody();
            System.out.println("[^_^:20190624-1022-001] ZKWebmvcTestControllerTest.index: " + resStr);
            TestCase.assertEquals(ZKWebmvcTestController.msg_index, resStr);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testHello() {
        try {
            String resStr = "";
            ZKWebmvcTestEntity entity, te;
            String url = this.baseUrl + "/postJson", param;
            ResponseEntity<String> response;
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<ZKWebmvcTestEntity> requestEntity;
            int resStatusCode;
            
            entity = new ZKWebmvcTestEntity();
            entity.setName("name 三尺剑，六钧弓，岭北对江东。");
            entity.setAge(99);
            param = "你回眸一笑，三秋却如一日。";
            url = baseUrl + "/postJson?param=" + param;
            
            requestEntity = new HttpEntity<ZKWebmvcTestEntity>(entity, headers);
            response = template.postForEntity(url, requestEntity, String.class);
            resStatusCode = response.getStatusCodeValue();
            TestCase.assertEquals(200, resStatusCode);
            resStr = response.getBody();
            entity.setRemark(param);
//            TestCase.assertEquals(ZKJsonUtils.writeObjectJson(entity), resStr);
            System.out.println("[^_^:20230131-0858-001] entity->json: " + ZKJsonUtils.writeObjectJson(entity));
            System.out.println("[^_^:20230131-0858-002] resStr: " + resStr);
            te = ZKJsonUtils.jsonStrToObject(resStr, ZKWebmvcTestEntity.class);
            TestCase.assertEquals(entity.getAge(), te.getAge());
            TestCase.assertEquals(entity.getName(), te.getName());
            TestCase.assertEquals(entity.getRemark(), te.getRemark());
            
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
