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
 * @Title: ZKSerCenIndexControllerTest.java 
 * @author Vinson 
 * @Package com.zk.server.central.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:32:02 PM 
 * @version V1.0   
*/
package com.zk.server.central.controller;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.web.utils.ZKCookieUtils;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.server.central.ZKSerCenSpringBootMain;
import com.zk.server.central.security.ZKSerCenAuthenticationFilter;

import jakarta.servlet.http.Cookie;
import junit.framework.TestCase;

/**
 * 使用 MockMvc 测试，方便视图断言
 * 
 * @ClassName: ZKSerCenIndexControllerTest
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ZKSerCenSpringBootMain.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:/" }, properties = { "spring.config.name=zk.ser.cen" })
public class ZKSerCenIndexControllerTest {

    @LocalServerPort
    private int port;

    @Value("${zk.path.admin}")
    private String adminPath;

    @Value("${zk.path.serCen}")
    private String modulePath;

    private String baseUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void setUp() {
        this.baseUrl = String.format("http://127.0.0.1:%s/%s/%s", port, adminPath, modulePath);
    }

    @Test
    public void testAnon() {

        try {
            ResponseEntity<String> response = null;
            HttpEntity<String> requestEntity;
            String url = baseUrl + "/anon";
            HttpHeaders requestHeaders = new HttpHeaders();
            // 设置请求数据格式为
            // "application/json;charset=UTF-8"、"application/x-www-form-urlencoded;
            // charset=UTF-8"
//            requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
            requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String msg = ZKSerCenIndexController.msg
                    + ZKEnvironmentUtils.getString("spring.application.name", "zk server central");

            // get
            response = testRestTemplate.getForEntity(url, String.class);
            TestCase.assertEquals(msg, response.getBody());

            // post
            requestHeaders.add(ZKWebUtils.Locale_Flag_In_Header, "en_US");
            requestEntity = new HttpEntity<String>("", requestHeaders);
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            TestCase.assertEquals(msg, response.getBody());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testIndex() {

        try {
            ResponseEntity<String> response = null;
            String url = null;
            String localStr = "";
            HttpEntity<String> requestEntity;
            String params;
            ZKMsgRes zkMsgRes = null;
            HttpHeaders requestHeaders = new HttpHeaders();
            // 设置请求数据格式为
            // "application/json;charset=UTF-8"
            // "application/x-www-form-urlencoded;charset=UTF-8"
//            requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
            requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            /*** 未登录，转发 ***/
            url = baseUrl;
            response = testRestTemplate.getForEntity(url, String.class);
            TestCase.assertTrue(response.getStatusCode().is3xxRedirection());

            /*** 登录 ***/
            String user = "admin", password = "admin";
            url = baseUrl + "/l/login";
            params = String.format("%s=%s&%s=%s", ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_ACCOUNT, user,
                    ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_PASSWORD, password);
            requestHeaders.remove(ZKWebUtils.Locale_Flag_In_Header);
            localStr = "en_US";
            requestHeaders.add(ZKWebUtils.Locale_Flag_In_Header, localStr);
            requestEntity = new HttpEntity<String>(params, requestHeaders);
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            TestCase.assertEquals(200, response.getStatusCode().value());
            System.out.println("[^_^:20191230-1706-001] " + response.getBody());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("zk.0", zkMsgRes.getCode());
            TestCase.assertEquals("Successful.", zkMsgRes.getMsg());

            /*** session 处理 JSESSIONID ***/
            String JSESSIONID = "";
            List<String> setCookies = null;
            Cookie[] cookies = null;
            Cookie sessionCookie = null;

            //
            JSESSIONID = "JSESSIONID";
            setCookies = response.getHeaders().get(ZKCookieUtils.SET_COOKIE);
            cookies = ZKCookieUtils.parseCookieStrs(setCookies);
            sessionCookie = ZKCookieUtils.getCookie(cookies, JSESSIONID, null);

            url = baseUrl;
            requestHeaders.add(ZKCookieUtils.COOKIE, ZKCookieUtils.formatCookie(sessionCookie));
            requestEntity = new HttpEntity<String>("", requestHeaders);
//            response = testRestTemplate.getForEntity(url, String.class);
            response = testRestTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            TestCase.assertTrue(response.getStatusCode().is2xxSuccessful());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testData() {

        try {
            ResponseEntity<String> response = null;
            String url = baseUrl;
            String localStr = "";
            HttpEntity<String> requestEntity;
            String params;
            ZKMsgRes zkMsgRes = null;
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            url = baseUrl + "/data";

            /*** 未登录，转发 ***/
            response = testRestTemplate.getForEntity(url, String.class);
            TestCase.assertTrue(response.getStatusCode().is3xxRedirection());

            /*** 登录 ***/
            String user = "admin", password = "admin";
            params = String.format("%s=%s&%s=%s", ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_ACCOUNT, user,
                    ZKSerCenAuthenticationFilter.ZKAuthKeys.PARAM_PASSWORD, password);
            localStr = "en_US";
            requestHeaders.remove(ZKWebUtils.Locale_Flag_In_Header);
            requestHeaders.add(ZKWebUtils.Locale_Flag_In_Header, localStr);
            requestEntity = new HttpEntity<String>(params, requestHeaders);
            response = testRestTemplate.postForEntity(baseUrl + "/l/login", requestEntity, String.class);
            TestCase.assertEquals(200, response.getStatusCode().value());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("zk.0", zkMsgRes.getCode());
            TestCase.assertEquals("Successful.", zkMsgRes.getMsg());

            /*** session 处理 JSESSIONID ***/
            String JSESSIONID = "";
            List<String> setCookies = null;
            Cookie[] cookies = null;
            Cookie sessionCookie = null;

            JSESSIONID = "JSESSIONID";
            setCookies = response.getHeaders().get(ZKCookieUtils.SET_COOKIE);
            cookies = ZKCookieUtils.parseCookieStrs(setCookies);
            sessionCookie = ZKCookieUtils.getCookie(cookies, JSESSIONID, null);

            // en_US 请求
            requestHeaders.add(ZKCookieUtils.COOKIE, ZKCookieUtils.formatCookie(sessionCookie));
            localStr = "en_US";
            requestHeaders.remove(ZKWebUtils.Locale_Flag_In_Header);
            requestHeaders.add(ZKWebUtils.Locale_Flag_In_Header, localStr);
            requestEntity = new HttpEntity<String>("", requestHeaders);
            response = testRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            TestCase.assertTrue(response.getStatusCode().is2xxSuccessful());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("zk.0", zkMsgRes.getCode());
            TestCase.assertEquals("Successful.", zkMsgRes.getMsg());
            TestCase.assertEquals(ZKSerCenIndexController.msg, zkMsgRes.getDataStr());

            // zh_CN 请求
            requestHeaders.add(ZKCookieUtils.COOKIE, ZKCookieUtils.formatCookie(sessionCookie));
            localStr = "zh_CN";
            requestHeaders.remove(ZKWebUtils.Locale_Flag_In_Header);
            requestHeaders.add(ZKWebUtils.Locale_Flag_In_Header, localStr);
            requestEntity = new HttpEntity<String>("", requestHeaders);
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            TestCase.assertTrue(response.getStatusCode().is2xxSuccessful());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("zk.0", zkMsgRes.getCode());
            TestCase.assertEquals("成功", zkMsgRes.getMsg());
            TestCase.assertEquals(ZKSerCenIndexController.msg, zkMsgRes.getDataStr());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
