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
* @Title: ZKSysSecControllerTest.java 
* @author Vinson 
* @Package com.zk.gateway.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 1, 2024 8:47:46 AM 
* @version V1.0 
*/
package com.zk.gateway.controller;

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
import com.zk.core.utils.ZKJsonUtils;
import com.zk.gateway.ZKGatewaySpringBootMain;
import com.zk.security.common.ZKSecConstants;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSysSecControllerTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZKGatewaySpringBootMain.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:/" }, properties = { "spring.config.name=zk,zk.gateway,zk.gateway.env" })
public class ZKSysSecControllerTest {

    @LocalServerPort
    private int port;

    @Value("${zk.path.admin}")
    private String adminPath;

    String apiSys = "apiSys/zk/sys/v1.0";

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
    public void testSecLogin() {
        try {

            String url;
            String resStr = "";
            ResponseEntity<String> response = null;
            HttpHeaders requestHeaders = null, resHeaders;
            HttpEntity<Object> requestEntity = null;
            ZKMsgRes res = null;

            String companyCode = "yunjian";
            String account = "admin";
            String pwd = "admin";
            String tkId = "";

            /* 登录 ******************************************/
            url = this.baseUrl + "/sec/login" + "?" //
                    + ZKSecConstants.PARAM_NAME.CompanyCode + "=" + companyCode + "&" //
                    + ZKSecConstants.PARAM_NAME.Username + "=" + account + "&" //
                    + ZKSecConstants.PARAM_NAME.Pwd + "=" + pwd;
            response = testRestTemplate.postForEntity(url, null, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210706-1820-001] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());
            resHeaders = response.getHeaders();
            tkId = resHeaders.getFirst(ZKSecConstants.PARAM_NAME.TicketId);
            System.out.println("[^_^:20210706-1820-002] tk: " + tkId);

            /* 取用户信息 ******************************************/
            url = this.baseUrl + "/sec/loginUserInfo";
            requestHeaders = new HttpHeaders();
            requestHeaders.set(ZKSecConstants.PARAM_NAME.TicketId, tkId);
            requestEntity = new HttpEntity<>(null, requestHeaders);
            response = testRestTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210706-1822-001] resStr: " + resStr);
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());
            resHeaders = response.getHeaders();
            System.out.println("[^_^:20210706-1822-002] headers: " + ZKJsonUtils.toJsonStr(resHeaders));
            System.out.println("[^_^:20210706-1822-003] headers.tk.size: "
                    + resHeaders.get(ZKSecConstants.PARAM_NAME.TicketId).size());
            TestCase.assertEquals(1, resHeaders.get(ZKSecConstants.PARAM_NAME.TicketId).size());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
