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
* @Title: ZKShiroControllerTest.java 
* @author Vinson 
* @Package com.zk.demo.shiro.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 29, 2021 6:56:25 PM 
* @version V1.0 
*/
package com.zk.demo.shiro.controller;

import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.demo.shiro.ZKShiroTestHelperSpringBootMain;
import com.zk.demo.shiro.common.ZKShiroConstants;

import junit.framework.TestCase;


/** 
* @ClassName: ZKShiroControllerTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(classes = ZKShiroTestHelperSpringBootMain.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:/" }, properties = { "spring.config.name=zk.sec" })
public class ZKShiroControllerTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void setUp() {
        this.baseUrl = "http://127.0.0.1:" + port;
    }

    private String privateUrlPath = "/test/h/c";

    private String login() {
        String url = "/login";
        String resStr = "", expectStr = "";
        ResponseEntity<String> response = null;

        url = this.baseUrl + this.privateUrlPath + "/login?" + ZKShiroConstants.PARAM_NAME.UserName + "=admin&"
                + ZKShiroConstants.PARAM_NAME.Pwd
                + "=admin";
        response = testRestTemplate.postForEntity(url, null, String.class);
        resStr = response.getBody();
        System.out.println("[^_^:20210706-1820-001] resStr: " + resStr);
        TestCase.assertNotNull(resStr);
        expectStr = "not-login";
        TestCase.assertFalse(expectStr.equals(resStr));
        return resStr;
    }

    @Test
    public void testShiroNone() {
        try {

            String url = "/none";
            String resStr = "", expectStr = "";
            ResponseEntity<String> response = null;

            url = this.baseUrl + this.privateUrlPath + "/none";
            System.out.println("[^_^:20240318-2245-001] url: " + url);
            response = testRestTemplate.getForEntity(url, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210704-1005-001] resStr: " + resStr);
            expectStr = ZKShiroController.class.getName();
            TestCase.assertEquals(expectStr, resStr);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testShiroLogin() {
        try {

            String url = "/login";
            String resStr = "", expectStr = "";
            ResponseEntity<String> response = null;
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

            url = this.baseUrl + this.privateUrlPath + "/login?" + ZKShiroConstants.PARAM_NAME.UserName + "=admin&"
                    + ZKShiroConstants.PARAM_NAME.Pwd + "=adminss";
            System.out.println("[^_^:20240318-2244-001] url: " + url);
            response = testRestTemplate.postForEntity(url, null, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210706-1820-001] resStr: " + resStr);
            expectStr = "not-login";
            TestCase.assertEquals(expectStr, resStr);

            url = this.baseUrl + this.privateUrlPath + "/login?" + ZKShiroConstants.PARAM_NAME.UserName + "=admin&"
                    + ZKShiroConstants.PARAM_NAME.Pwd + "=admin";
            System.out.println("[^_^:20240318-2244-001] url: " + url);
            response = testRestTemplate.postForEntity(url, null, String.class);
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

    @Test
    public void testShiroLogout() {
        try {

            String url = this.baseUrl + "/logout";
            String resStr = "", expectStr = "";
            ResponseEntity<String> response = null;

            url = this.baseUrl + this.privateUrlPath + "/logout";
            System.out.println("[^_^:20240318-2243-001] url: " + url);
            response = testRestTemplate.getForEntity(url, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210705-1819-001] resStr: " + resStr);
            expectStr = "logout";
            TestCase.assertEquals(expectStr, resStr);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testShiroUser() {
        try {

            String url = "/user";
            String resStr = "", expectStr = "";
            ResponseEntity<String> response = null;
            String sid;
            ZKMsgRes res = null;

            url = this.baseUrl + this.privateUrlPath + "/user";
            System.out.println("[^_^:20240318-2242-001] url: " + url);

            response = testRestTemplate.getForEntity(url, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210705-1819-001] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertEquals("zk.test.000004", res.getCode());

            sid = this.login();
            url = this.baseUrl + this.privateUrlPath + "/user?" + ShiroHttpSession.DEFAULT_SESSION_ID_NAME + "=" + sid;
            System.out.println("[^_^:20240318-2242-002] url: " + url);

            response = testRestTemplate.getForEntity(url, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210705-1819-002] resStr: " + resStr);
            expectStr = "user";
            TestCase.assertEquals(expectStr, resStr);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testShiroPermissionAndRole() {
        try {

            String url = "/permissionAndRole";
            String resStr = "", expectStr = "";
            ResponseEntity<String> response = null;
            String sid = this.login();

            url = this.baseUrl + this.privateUrlPath + "/permission?" + ShiroHttpSession.DEFAULT_SESSION_ID_NAME + "="
                    + sid;
            System.out.println("[^_^:20240318-2241-001] url: " + url);

            response = testRestTemplate.getForEntity(url, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210705-0704-001] resStr: " + resStr);
            expectStr = "permission";
            TestCase.assertEquals(expectStr, resStr);

            url = this.baseUrl + this.privateUrlPath + "/role?" + ShiroHttpSession.DEFAULT_SESSION_ID_NAME + "=" + sid;
            System.out.println("[^_^:20240318-2241-002] url: " + url);
            response = testRestTemplate.getForEntity(url, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210705-0704-002] resStr: " + resStr);
            expectStr = "role";
            TestCase.assertEquals(expectStr, resStr);

            url = this.baseUrl + this.privateUrlPath + "/permissionAndRole?" + ShiroHttpSession.DEFAULT_SESSION_ID_NAME
                    + "=" + sid;
            System.out.println("[^_^:20240318-2241-003] url: " + url);
            response = testRestTemplate.getForEntity(url, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210705-0704-003] resStr: " + resStr);
            expectStr = "permissionAndRole";
            TestCase.assertEquals(expectStr, resStr);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testShiroNoPermissionAndRole() {
        try {

            String url = "/noPermissionAndRole";
            String resStr = "";
            ResponseEntity<String> response = null;
            String sid = this.login();
            ZKMsgRes mr = null;

            url = this.baseUrl + this.privateUrlPath + "/noPermissionAndRole?"
                    + ShiroHttpSession.DEFAULT_SESSION_ID_NAME + "=" + sid;
            System.out.println("[^_^:20240318-2240-001] url: " + url);

            response = testRestTemplate.getForEntity(url, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210705-0704-004] resStr: " + resStr);

            mr = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertEquals("zk.test.000003", mr.getCode());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
