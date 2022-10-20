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
 * @Title: ZKCoreControolerTest.java 
 * @author Vinson 
 * @Package com.zk.core.web.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 3:20:22 PM 
 * @version V1.0   
*/
package com.zk.core.web.controller;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.zk.core.helper.ZKCoreTestHelperMvcSpringBootMain;
import com.zk.core.helper.configuration.ZKCoreChildConfiguration;
import com.zk.core.helper.configuration.ZKCoreParentConfiguration;
import com.zk.core.helper.controller.ZKCoreControoler;
import com.zk.core.utils.ZKEnvironmentUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKCoreControolerTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ZKCoreParentConfiguration.class,
        ZKCoreChildConfiguration.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ZKCoreControolerTest {

    static {
        try {
            ZKCoreTestHelperMvcSpringBootMain.exit();
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
    public void testHello() {
        try {
            String resStr = "";
            ResponseEntity<String> response = template.getForEntity(this.baseUrl, String.class,
                    new HashMap<>());
            resStr = response.getBody();
            System.out.println("[^_^:20190624-1022-001] ZKCoreControolerTest.index: " + resStr);
            TestCase.assertEquals(ZKCoreControoler.msg_index, resStr);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
