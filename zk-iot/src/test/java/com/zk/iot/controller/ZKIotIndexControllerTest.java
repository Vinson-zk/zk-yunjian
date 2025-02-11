/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKIotIndexControllerTest.java 
* @author Vinson 
* @Package com.zk.iot.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Dec 31, 2024 4:53:34 PM 
* @version V1.0 
*/
package com.zk.iot.controller;

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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.iot.helper.ZKIotTestHelper;

import junit.framework.TestCase;

/** 
* @ClassName: ZKIotIndexControllerTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZKIotTestHelper.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:/" }, properties = { "spring.config.name=zk,zk.iot,zk.iot.env" })
public class ZKIotIndexControllerTest {

    @LocalServerPort
    private int port;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${zk.path.admin}")
    private String adminPath;

    String apiSys = "zk/iot/v1.0";

    private String baseUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Before
    public void setUp() {
        this.baseUrl = String.format("http://127.0.0.1:%s/%s", port, apiSys);
    }

    /**
     * @return baseUrl sa
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    @Test
    public void testIndex() {
        try {

            String url;
            String resStr = "";
            ResponseEntity<String> response = null;
            HttpHeaders requestHeaders = null;
            HttpEntity<Object> requestEntity = null;
            ZKMsgRes res = null;

            /* 默认 zh_CN 中文：post get ******************************************/
            url = this.baseUrl;
            response = testRestTemplate.postForEntity(url, null, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20241231-1820-001.01] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());
            TestCase.assertEquals(
                    "index: " + ZKMsgUtils.getMessage(ZKLocaleUtils.distributeLocale("zh_CN"), "zk.iot.msg.welcome")
                            + " -> " + ZKEnvironmentUtils.getString("spring.application.name"),
                    res.getDataStr());

            url = this.baseUrl;
            response = testRestTemplate.getForEntity(url, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20241231-1820-001.02] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());
            TestCase.assertEquals(
                    "index: " + ZKMsgUtils.getMessage(ZKLocaleUtils.distributeLocale("zh_CN"), "zk.iot.msg.welcome")
                            + " -> " + ZKEnvironmentUtils.getString("spring.application.name"),
                    res.getDataStr());

            String localStr = "";
            /* 指定语言，post get ******************************************/
            url = this.baseUrl;
            localStr = "zh_CN";
            requestHeaders = new HttpHeaders();
            requestHeaders.set(ZKWebUtils.Locale_Flag_In_Header, localStr);
            requestEntity = new HttpEntity<>(null, requestHeaders);
            response = testRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20241231-1820-001] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());
            TestCase.assertEquals(
                    "index: " + ZKMsgUtils.getMessage(ZKLocaleUtils.distributeLocale(localStr), "zk.iot.msg.welcome")
                            + " -> " + ZKEnvironmentUtils.getString("spring.application.name"),
                    res.getDataStr());

            url = this.baseUrl;
            localStr = "zh_CN";
            requestHeaders = new HttpHeaders();
            requestHeaders.set(ZKWebUtils.Locale_Flag_In_Header, localStr);
            requestEntity = new HttpEntity<>(null, requestHeaders);
            response = testRestTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20241231-1820-001] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());
            TestCase.assertEquals(
                    "index: " + ZKMsgUtils.getMessage(ZKLocaleUtils.distributeLocale(localStr), "zk.iot.msg.welcome")
                            + " -> " + ZKEnvironmentUtils.getString("spring.application.name"),
                    res.getDataStr());

            url = this.baseUrl;
            localStr = "en_US";
            requestHeaders = new HttpHeaders();
            requestHeaders.set(ZKWebUtils.Locale_Flag_In_Header, localStr);
            requestEntity = new HttpEntity<>(null, requestHeaders);
            response = testRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20241231-1820-001] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());
            TestCase.assertEquals(
                    "index: " + ZKMsgUtils.getMessage(ZKLocaleUtils.distributeLocale(localStr), "zk.iot.msg.welcome")
                            + " -> " + ZKEnvironmentUtils.getString("spring.application.name"),
                    res.getDataStr());

            url = this.baseUrl;
            localStr = "en_US";
            requestHeaders = new HttpHeaders();
            requestHeaders.set(ZKWebUtils.Locale_Flag_In_Header, localStr);
            requestEntity = new HttpEntity<>(null, requestHeaders);
            response = testRestTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20241231-1820-001] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());
            TestCase.assertEquals(
                    "index: " + ZKMsgUtils.getMessage(ZKLocaleUtils.distributeLocale(localStr), "zk.iot.msg.welcome")
                            + " -> " + ZKEnvironmentUtils.getString("spring.application.name"),
                    res.getDataStr());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
