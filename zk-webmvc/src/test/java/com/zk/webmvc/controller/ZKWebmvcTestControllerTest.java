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
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;

import com.zk.core.commons.ZKContentType;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.web.utils.ZKWebUtils;
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
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ZKWebmvcTestHelperMvcSpringBootMain.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
    private TestRestTemplate testRestTemplate;

    @Before
    public void setUp() {
        this.baseUrl = String.format("http://127.0.0.1:%s/%s/%s", port,
                ZKEnvironmentUtils.getString("zk.path.admin", "zk"),
                ZKEnvironmentUtils.getString("zk.path.webmvc", "c"));
        System.out.println("[^_^:20191218-1555-001] base url:" + this.baseUrl);
    }

    // index 测试一个简单返回
    @Test
    public void testIndex() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, ZKContentType.X_FORM_UTF8.toContentTypeStr());

            for (HttpMessageConverter<?> o : testRestTemplate.getRestTemplate().getMessageConverters()) {
                System.out.println(
                        "[^_^:20240122-2323-001] MessageConverter: " + o.getClass().getSimpleName() + "   --------- ");
                for (MediaType oo : o.getSupportedMediaTypes()) {
                    String mt = "";
                    if(oo != null) {
                        mt = mt + oo.getSubtype() + ";";
                        mt = mt + (oo.getCharset() == null ? "" : oo.getCharset().toString());
                    }
                    System.out.println("[^_^:20240122-2323-001] " + mt);
                }
            }

            String resStr = "";
            ResponseEntity<String> response = testRestTemplate.getForEntity(this.baseUrl, String.class, headers);
            resStr = response.getBody();
            System.out.println("[^_^:20190624-1022-001] ZKWebmvcTestControllerTest.index: " + resStr);
            TestCase.assertEquals(ZKWebmvcTestController.msg_index, resStr);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // get 请求，测试一个 url 参数请求与返回
    @Test
    public void testGet() {
        try {
            String resStr = "";
            String url, param;
            ResponseEntity<String> response;
            int resStatusCode;

            param = "俨骖騑于上路，访风景于崇阿。";
            url = baseUrl + "/get?param=" + param;

            response = testRestTemplate.getForEntity(url, String.class);
            resStatusCode = response.getStatusCode().value();
            TestCase.assertEquals(200, resStatusCode);
            resStr = response.getBody();
            System.out.println("[^_^:20230131-0858-021] resStr: " + resStr);
            TestCase.assertEquals(ZKWebmvcTestController.msg_index + param, resStr);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // post 请求，测试一个 url 参数请求与返回
    @Test
    public void testPost() {
        try {
            String resStr = "";
            String url, param;
            ResponseEntity<String> response;
            int resStatusCode;

            param = "望长安于日下，目吴会于云间。";
            url = baseUrl + "/post?param=" + param;

            response = testRestTemplate.postForEntity(url, null, String.class);
            resStatusCode = response.getStatusCode().value();
            TestCase.assertEquals(200, resStatusCode);
            resStr = response.getBody();
            System.out.println("[^_^:20230131-0858-022] resStr: " + resStr);
            TestCase.assertEquals(ZKWebmvcTestController.msg_index + param, resStr);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // ZkMsgRes 响应返回测试，以及消息国际化测试
    @Test
    public void testZkMsgRes() {
        try {
            String resStr = "";
            String url, param;
            ZKMsgRes ms = null;
            ResponseEntity<String> response;
            int resStatusCode;

            param = "桂殿兰宫，穷岛屿之萦回。";
            url = baseUrl + "/zkMsgRes?param=" + param;

            // GET 无请求头
            response = testRestTemplate.getForEntity(url, String.class);
            resStatusCode = response.getStatusCode().value();
            TestCase.assertEquals(200, resStatusCode);
            resStr = response.getBody();
            System.out.println("[^_^:20230131-0858-023] resStr: " + resStr);
            ms = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertEquals("zk.000003", ms.getCode());
            TestCase.assertEquals("项目环境配置错误。", ms.getMsg());
            TestCase.assertEquals(param, ms.getDataStr());

            // POST 无请求头
            response = testRestTemplate.postForEntity(url, null, String.class);
            resStatusCode = response.getStatusCode().value();
            TestCase.assertEquals(200, resStatusCode);
            resStr = response.getBody();
            System.out.println("[^_^:20230131-0858-022] resStr: " + resStr);
            ms = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertEquals("zk.000003", ms.getCode());
            TestCase.assertEquals("项目环境配置错误。", ms.getMsg());
            TestCase.assertEquals(param, ms.getDataStr());

            // ==================================================
            HttpEntity<String> requestEntity;
            HttpHeaders headers = null;
            headers = new HttpHeaders();
            headers.set(ZKWebUtils.Locale_Flag_In_Header, "en-US");
            requestEntity = new HttpEntity<String>("", headers);

            // GET 添加请求头，国际化英语
            response = testRestTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            resStatusCode = response.getStatusCode().value();
            TestCase.assertEquals(200, resStatusCode);
            resStr = response.getBody();
            System.out.println("[^_^:20230131-0858-023] resStr: " + resStr);
            ms = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertEquals("zk.000003", ms.getCode());
            TestCase.assertEquals("The project environment is incorrectly configured.", ms.getMsg());
            TestCase.assertEquals(param, ms.getDataStr());

            // POST 添加请求头，国际化英语
            response = testRestTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            resStatusCode = response.getStatusCode().value();
            TestCase.assertEquals(200, resStatusCode);
            resStr = response.getBody();
            System.out.println("[^_^:20230131-0858-023] resStr: " + resStr);
            ms = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertEquals("zk.000003", ms.getCode());
            TestCase.assertEquals("The project environment is incorrectly configured.", ms.getMsg());
            TestCase.assertEquals(param, ms.getDataStr());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testPostJson() {
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
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            resStatusCode = response.getStatusCode().value();
            TestCase.assertEquals(200, resStatusCode);
            resStr = response.getBody();
//            TestCase.assertEquals(ZKJsonUtils.toJsonStr(entity), resStr);
            System.out.println("[^_^:20230131-0858-001] entity->json: " + ZKJsonUtils.toJsonStr(entity));
            System.out.println("[^_^:20230131-0858-002] resStr: " + resStr);
            te = ZKJsonUtils.parseObject(resStr, ZKWebmvcTestEntity.class);
            TestCase.assertEquals(entity.getAge(), te.getAge());
            TestCase.assertEquals(entity.getName(), te.getName());
            TestCase.assertEquals(param, te.getRemark());
            
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testReqInfo() {
        try {
            String url = this.baseUrl + "/reqInfo/pathParamValue?param=paramValue";
            ResponseEntity<String> response;
            int resStatusCode;
            HttpEntity<Object> requestEntity;
            Map<String, Object> urlVariables;

            requestEntity = new HttpEntity<Object>(null, null);
            urlVariables = new HashMap<>();
            urlVariables.put("pathVariable", "pathParamValue");
            urlVariables.put("param", "paramValue");

            response = testRestTemplate.getForEntity(url, String.class, urlVariables);
            resStatusCode = response.getStatusCode().value();
            TestCase.assertEquals(200, resStatusCode);

            response = testRestTemplate.postForEntity(url, requestEntity, String.class, urlVariables);
            resStatusCode = response.getStatusCode().value();
            TestCase.assertEquals(200, resStatusCode);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testException() {
        try {
            HttpEntity<String> requestEntity;
            ResponseEntity<String> response;
            HttpHeaders headers = new HttpHeaders();
            String url, errCode = "";
            String resStr = "";
            ZKMsgRes ms = null;
            int resStatusCode;
            
            /*
             * zk.0=Successful. 
             * zk.1=Sytem error. 
             * zk.000003=The project environment is incorrectly configured.
             * 
             * zk.0=成功
             * zk.1=系统异常
             * zk.000003=项目环境配置错误
             */

            // GET zk.000003
            errCode = "zk.000003";
            url = this.baseUrl + "/exception?errCode=" + errCode;
            response = testRestTemplate.getForEntity(url, String.class, headers);
            resStatusCode = response.getStatusCode().value();
            TestCase.assertEquals(200, resStatusCode);
            resStr = response.getBody();
            System.out.println("[^_^:20240106-0017-011] ZKWebmvcTestControllerTest.exception: " + resStr);
            ms = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertEquals(errCode, ms.getCode());
            TestCase.assertEquals("项目环境配置错误。", ms.getMsg());

            headers = new HttpHeaders();
            headers.set(ZKWebUtils.Locale_Flag_In_Header, "en-US");
            requestEntity = new HttpEntity<String>("", headers);
            url = this.baseUrl + "/exception?errCode=" + errCode;
            response = testRestTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            resStatusCode = response.getStatusCode().value();
            TestCase.assertEquals(200, resStatusCode);
            resStr = response.getBody();
            System.out.println("[^_^:20240106-0017-011] ZKWebmvcTestControllerTest.exception: " + resStr);
            ms = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertEquals(errCode, ms.getCode());
            TestCase.assertEquals("The project environment is incorrectly configured.", ms.getMsg());

            // POST zk.1
            errCode = "zk.1";
            headers = new HttpHeaders();
            headers.set(ZKWebUtils.Locale_Flag_In_Header, "zh-CN");
            requestEntity = new HttpEntity<String>("", headers);
            url = this.baseUrl + "/exception?errCode=" + errCode;
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            resStatusCode = response.getStatusCode().value();
            TestCase.assertEquals(200, resStatusCode);
            resStr = response.getBody();
            System.out.println("[^_^:20240106-0017-011] ZKWebmvcTestControllerTest.exception: " + resStr);
            ms = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertEquals(errCode, ms.getCode());
            TestCase.assertEquals("系统异常。", ms.getMsg());

            headers = new HttpHeaders();
            headers.set(ZKWebUtils.Locale_Flag_In_Header, "en-US");
            requestEntity = new HttpEntity<String>("", headers);
            url = this.baseUrl + "/exception?errCode=" + errCode;
            response = testRestTemplate.postForEntity(url, requestEntity, String.class);
            resStatusCode = response.getStatusCode().value();
            TestCase.assertEquals(200, resStatusCode);
            resStr = response.getBody();
            System.out.println("[^_^:20240106-0017-011] ZKWebmvcTestControllerTest.exception: " + resStr);
            ms = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
            TestCase.assertEquals(errCode, ms.getCode());
            TestCase.assertEquals("Sytem error.", ms.getMsg());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
