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
* @Title: ZKSysSecIndexControllerTest.java 
* @author Vinson 
* @Package com.zk.sys.sec.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 20, 2022 5:35:41 PM 
* @version V1.0 
*/
package com.zk.sys.sec.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.web.ZKMsgRes;
import com.zk.security.common.ZKSecConstants;
import com.zk.sys.ZKSysSpringBootMain;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSysSecIndexControllerTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZKSysSpringBootMain.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:/" }, properties = { "spring.config.name=zk,zk.sys" })
public class ZKSysSecIndexControllerTest {

    @LocalServerPort
    private int port;

    @Value("${zk.path.admin}")
    private String adminPath;

    @Value("${zk.path.sys}")
    private String modulePath;

    @Value("${zk.sys.version}")
    private String version;

    private String baseUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void setUp() {
        this.baseUrl = String.format("http://127.0.0.1:%s/%s/%s/%s", port, adminPath, modulePath, version);
    }

    /**
     * @return baseUrl sa
     */
    public String getBaseUrl() {
        return baseUrl;
    }

//    private String login() {
//        String urlPath = "/login";
//        String resStr = "", expectStr = "";
//        ResponseEntity<String> response = null;
//
//        urlPath = "/login?" + ZKSecConstants.PARAM_NAME.Username + "=admin&" + ZKSecConstants.PARAM_NAME.Pwd + "=admin";
//        response = testRestTemplate.postForEntity(this.baseUrl + urlPath, null, String.class);
//        resStr = response.getBody();
//        System.out.println("[^_^:20210706-1820-001] resStr: " + resStr);
//        TestCase.assertNotNull(resStr);
//        expectStr = "not-login";
//        TestCase.assertFalse(expectStr.equals(resStr));
//        return resStr;
//    }

    @Test
    public void testSecLogin() {
        try {

            String urlPath = "/sec/login";
            String resStr = "", expectStr = "";
            ResponseEntity<String> response = null;
            ZKMsgRes res = null;
//            HttpHeaders requestHeaders = null;
//            Map<String, String> requestBody = null;
//            HttpEntity<Map<String, String>> requestEntity = null;
//            
//            // headers
//            requestHeaders = new HttpHeaders();
//            requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
//            requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//            // body
//            // MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
//            requestBody = new HashMap<>();
//            requestBody.put(ZKSecTestConstants.PARAM_NAME.UserName, "admin");
//            requestBody.put(ZKSecTestConstants.PARAM_NAME.Pwd, "adminss");
//            requestEntity = new HttpEntity<>(requestBody, requestHeaders);

            /* zk.sec.000002 密码错误 */
            urlPath = this.baseUrl + urlPath + "?" + ZKSecConstants.PARAM_NAME.Username + "=admin&"
                    + ZKSecConstants.PARAM_NAME.Pwd
                    + "=adminss";
            response = testRestTemplate.postForEntity(urlPath, null, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210706-1820-001] resStr: " + resStr);
            expectStr = "zk.sec.000002";
            res = ZKJsonUtils.jsonStrToObject(resStr, ZKMsgRes.class);
            TestCase.assertEquals(expectStr, res.getCode());

            urlPath = this.baseUrl + urlPath + "?" + ZKSecConstants.PARAM_NAME.Username + "=admin&"
                    + ZKSecConstants.PARAM_NAME.Pwd
                    + "=admin";
            response = testRestTemplate.postForEntity(urlPath, null, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210706-1820-002] resStr: " + resStr);
            expectStr = "not-login";
            TestCase.assertFalse(expectStr.equals(resStr));
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
