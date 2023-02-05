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
* @Title: ZKSecHelperControllerTest.java 
* @author Vinson 
* @Package com.zk.security.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 4, 2021 10:52:46 AM 
* @version V1.0 
*/
package com.zk.security.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.web.ZKMsgRes;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.helper.ZKSecTestHelperSpringBootMain;
import com.zk.security.helper.controller.ZKSecHelperController;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSecHelperControllerTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(classes = ZKSecTestHelperSpringBootMain.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
        locations = { "classpath:/" }, properties = { "spring.config.name=test.zk.sec" }
)
public class ZKSecHelperControllerTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void setUp() {
        this.baseUrl = "http://127.0.0.1:" + port;
    }

    private String privateUrlPath = "/sec/h/c";

    @Test
    public void testSecNone() {
        try {

            String urlPath = "/none";
            String resStr = "", expectStr = "";
            ResponseEntity<String> response = null;

            response = testRestTemplate.getForEntity(this.baseUrl + this.privateUrlPath + urlPath, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210704-1005-001] resStr: " + resStr);
            expectStr = ZKSecHelperController.class.getName();
            TestCase.assertEquals(expectStr, resStr);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    private String login() {
        String urlPath = "/login";
        String resStr = "", expectStr = "";
        ResponseEntity<String> response = null;

        urlPath = "/login?" + ZKSecConstants.PARAM_NAME.Username + "=admin&" + ZKSecConstants.PARAM_NAME.Pwd
                + "=admin";
        response = testRestTemplate.postForEntity(this.baseUrl + this.privateUrlPath + urlPath, null, String.class);
        resStr = response.getBody();
        System.out.println("[^_^:20210706-1820-001] resStr: " + resStr);
        TestCase.assertNotNull(resStr);
        expectStr = "not-login";
        TestCase.assertFalse(expectStr.equals(resStr));
        return resStr;
    }

    @Test
    public void testSecLogin() {
        try {

            String urlPath = "/login";
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
            urlPath = "/login?" + ZKSecConstants.PARAM_NAME.Username + "=admin&" + ZKSecConstants.PARAM_NAME.Pwd
                    + "=adminss";
            response = testRestTemplate.postForEntity(this.baseUrl + this.privateUrlPath + urlPath, null, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210706-1820-001] resStr: " + resStr);
            expectStr = "zk.sec.000002";
            res = ZKJsonUtils.jsonStrToObject(resStr, ZKMsgRes.class);
            TestCase.assertEquals(expectStr, res.getCode());

            urlPath = "/login?" + ZKSecConstants.PARAM_NAME.Username + "=admin&" + ZKSecConstants.PARAM_NAME.Pwd
                    + "=admin";
            response = testRestTemplate.postForEntity(this.baseUrl + this.privateUrlPath + urlPath, null,
                    String.class);
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
    public void testSecLogout() {
        try {

            String urlPath = "/logout";
            String resStr = "", expectStr = "";
            ResponseEntity<String> response = null;

            response = testRestTemplate.getForEntity(this.baseUrl + this.privateUrlPath + urlPath, String.class);
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
    public void testSecUser() {
        try {

            String urlPath = "/user";
            String resStr = "", expectStr = "";
            ResponseEntity<String> response = null;
            String sid;
            ZKMsgRes res = null;

            response = testRestTemplate.getForEntity(this.baseUrl + this.privateUrlPath + urlPath, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210705-1819-001] resStr: " + resStr);
            res = ZKJsonUtils.jsonStrToObject(resStr, ZKMsgRes.class);
            TestCase.assertEquals("zk.sec.000004", res.getCode());

            sid = this.login();
            urlPath = "/user?" + ZKSecConstants.PARAM_NAME.TicketId + "=" + sid;
            response = testRestTemplate.getForEntity(this.baseUrl + this.privateUrlPath + urlPath, String.class);
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
    public void testSecZkSecApiCode() {
        try {

            String urlPath = "/zkSecApiCode";
            String resStr = "", expectStr = "";
            ResponseEntity<String> response = null;
            String sid = this.login();
            ZKMsgRes res = null;

            /* 权限正常 */
            urlPath = "/zkSecApiCode?" + ZKSecConstants.PARAM_NAME.TicketId + "=" + sid;
            response = testRestTemplate.getForEntity(this.baseUrl + this.privateUrlPath + urlPath, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210708-1935-004] resStr: " + resStr);
            expectStr = "ok:zkSecApiCode";
            TestCase.assertEquals(expectStr, resStr);

            /* zk.sec.000003 没有操作权限 */
            urlPath = "/noZkSecApiCode?" + ZKSecConstants.PARAM_NAME.TicketId + "=" + sid;
            response = testRestTemplate.getForEntity(this.baseUrl + this.privateUrlPath + urlPath, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210708-1935-005] resStr: " + resStr);
            res = ZKJsonUtils.jsonStrToObject(resStr, ZKMsgRes.class);
            expectStr = "zk.sec.000003";
            TestCase.assertEquals(expectStr, res.getCode());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
