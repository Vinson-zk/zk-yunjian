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
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.security.common.ZKSecConstants;
import com.zk.sys.helper.ZKSysTestHelper;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSysSecIndexControllerTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZKSysTestHelper.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:/" }, properties = { "spring.config.name=zk,zk.sys,zk.sys.env" })
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

    @Test
    public void testSecLogin() {
        try {

            String urlPath = this.baseUrl + "/sec/login", url;
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
            url = urlPath + "?" //
                    + ZKSecConstants.PARAM_NAME.CompanyCode + "=yunjian&" //
                    + ZKSecConstants.PARAM_NAME.Username + "=admin&" //
                    + ZKSecConstants.PARAM_NAME.Pwd + "=adminss";
            response = testRestTemplate.postForEntity(url, null, String.class);
            resStr = response.getBody();
            System.out.println("[^_^:20210706-1820-001] resStr: " + resStr);
            expectStr = "zk.sys.020003";
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertEquals(expectStr, res.getCode());

            url = urlPath + "?" //
                    + ZKSecConstants.PARAM_NAME.CompanyCode + "=yunjian&" //
                    + ZKSecConstants.PARAM_NAME.Username + "=admin&" //
                    + ZKSecConstants.PARAM_NAME.Pwd + "=admin";
            response = testRestTemplate.postForEntity(url, null, String.class);
            resStr = response.getBody();

            System.out.println("[^_^:20210706-1820-002] resStr: " + resStr);
            System.out.println("[^_^:20210706-1820-003] _tkid: "
                    + response.getHeaders().getFirst(ZKSecConstants.PARAM_NAME.TicketId));
            res = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertTrue(res.isOk());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    public static ResponseEntity<String> secLogin(TestRestTemplate testRestTemplate, String url, String companyCode,
            String account, String pwd) {
        try {

            ResponseEntity<String> response = null;

            /* zk.sec.000002 密码错误 */
            url = url + "?" //
                    + ZKSecConstants.PARAM_NAME.CompanyCode + "=" + companyCode + "&" //
                    + ZKSecConstants.PARAM_NAME.Username + "=" + account + "&" //
                    + ZKSecConstants.PARAM_NAME.Pwd + "=" + pwd;
            response = testRestTemplate.postForEntity(url, null, String.class);
            return response;
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
//        return Collections.emptyMap();
        return null;
    }

}
