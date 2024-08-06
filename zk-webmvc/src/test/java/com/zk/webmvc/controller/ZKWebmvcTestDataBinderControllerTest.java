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
* @Title: ZKWebmvcTestDataBinderControllerTest.java 
* @author Vinson 
* @Package com.zk.webmvc.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 19, 2024 1:01:10 AM 
* @version V1.0 
*/
package com.zk.webmvc.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.web.utils.ZKHtmlUtils;
import com.zk.webmvc.helper.ZKWebmvcTestHelperMvcSpringBootMain;
import com.zk.webmvc.helper.controller.ZKWebmvcTestDataBinderController;
import com.zk.webmvc.helper.entity.ZKWebmvcTestEntity;

import junit.framework.TestCase;

/** 
* @ClassName: ZKWebmvcTestDataBinderControllerTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ZKWebmvcTestHelperMvcSpringBootMain.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ZKWebmvcTestDataBinderControllerTest {

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
        this.baseUrl = String.format("http://127.0.0.1:%s/%s/%s/dataBinder", port,
                ZKEnvironmentUtils.getString("zk.path.admin", "zk"),
                ZKEnvironmentUtils.getString("zk.path.webmvc", "c"));
        System.out.println("[^_^:20191218-1555-001] base url:" + this.baseUrl);
    }

    /*** 测试 各种参数接收的混合参数接口 ***/
    @Test
    public void testHybridParam() {
        try {

            String url = baseUrl + "/hybridParam/" + ZKWebmvcTestDataBinderController.pn_pathVariable1 + "/"
                    + ZKWebmvcTestDataBinderController.pn_pathVariable2;

            testHyBridParamHandler(url, HttpMethod.POST);
            testHyBridParamHandler(url, HttpMethod.GET);
            testHyBridParamHandler(url, HttpMethod.DELETE);
            testHyBridParamHandler(url, HttpMethod.PUT);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }

    }

    @SuppressWarnings("unchecked")
    private void testHyBridParamHandler(String url, HttpMethod method) throws Exception {

        HttpEntity<ZKWebmvcTestEntity> requestEntity;
        ResponseEntity<String> response;
        HttpHeaders headers = null;
        int resStatusCode;
        String resStr;
        ZKMsgRes myResMsg;
        Map<String, ?> resMap;

        ZKWebmvcTestEntity myEntity = null, chileEntity = null;
        Map<String, ZKWebmvcTestEntity> myEntityMap = null;
        MultiValueMap<String, String> paramMap = null;
        String jsonStr = null;

        ZKWebmvcTestDataBinderController.initBinderFlagStr = false;
        ZKWebmvcTestDataBinderController.initBinderFlagEntity = false;
        ZKWebmvcTestDataBinderController.initBinderFlagEntityList = false;
        ZKWebmvcTestDataBinderController.initBinderFlagEntityMap = false;

        myEntity = new ZKWebmvcTestEntity();
        myEntity.setName("名称");
        myEntity.setAge(1);
        myEntity.setStrArray(
                new String[] { ZKWebmvcTestDataBinderController.pn_strs, ZKWebmvcTestDataBinderController.pn_strs });
        myEntity.setStrList(
                Arrays.asList(ZKWebmvcTestDataBinderController.pn_strs, ZKWebmvcTestDataBinderController.pn_strs));

        chileEntity = new ZKWebmvcTestEntity();
        chileEntity.setName("chileEntity.名称");
        chileEntity.setAge(2);
        chileEntity.setStrArray(
                new String[] { ZKWebmvcTestDataBinderController.pn_strs, ZKWebmvcTestDataBinderController.pn_strs });
        chileEntity.setStrList(
                Arrays.asList(ZKWebmvcTestDataBinderController.pn_strs, ZKWebmvcTestDataBinderController.pn_strs));

        myEntity.setList(Arrays.asList(chileEntity, chileEntity));
        myEntityMap = new HashMap<String, ZKWebmvcTestEntity>();
        myEntityMap.put("key1", chileEntity);
        myEntityMap.put("key2", chileEntity);
        myEntity.setMap(myEntityMap);
        jsonStr = ZKJsonUtils.toJsonStr(myEntity);

        /* 请求参数 */
        url = url + "?";
        paramMap = new LinkedMultiValueMap<>();
        // 字符串参数
        paramMap.set(ZKWebmvcTestDataBinderController.pn_str, ZKWebmvcTestDataBinderController.pn_str + "字符串");
        // 字符串 Array 与 List 参数
        paramMap.add(ZKWebmvcTestDataBinderController.pn_strs, ZKWebmvcTestDataBinderController.pn_strs + "-p1");
        paramMap.add(ZKWebmvcTestDataBinderController.pn_strs, ZKWebmvcTestDataBinderController.pn_strs + "-p2");
        // 实体参数
        paramMap.set(ZKWebmvcTestDataBinderController.pn_entity + ".name", "实体数组");
        paramMap.set(ZKWebmvcTestDataBinderController.pn_entity + ".age", "1");
        paramMap.set(ZKWebmvcTestDataBinderController.pn_entity, ZKJsonUtils.toJsonStr(myEntity));
        // 实体 List
        paramMap.set(ZKWebmvcTestDataBinderController.pn_entityList,
                ZKJsonUtils.toJsonStr(Arrays.asList(chileEntity, chileEntity)));
        // 实体 Map
        paramMap.set(ZKWebmvcTestDataBinderController.pn_entityMap, ZKJsonUtils.toJsonStr(myEntityMap));
        // 添加 请求参数

        for (String k : paramMap.keySet()) {
            for (String v : paramMap.get(k)) {
                url = url + k + "=" + ZKHtmlUtils.urlEncode(v) + "&";
            }
        }
        url = url + "1=1";
        System.out.println("[^_^:20240121-2328-001] url: " + url);

        headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_TYPE, ZKContentType.X_FORM_UTF8.getContentType());
        /* request body */
        requestEntity = new HttpEntity<ZKWebmvcTestEntity>(myEntity, headers);
        
        response = this.template.exchange(url, method, requestEntity, String.class);
//        response = this.template.postForEntity(url, requestEntity, String.class);
        resStatusCode = response.getStatusCode().value();
        TestCase.assertEquals(200, resStatusCode);
        resStr = response.getBody();
        System.out.println("[^_^:20240120-2230-001] resStr: " + resStr);
        myResMsg = ZKJsonUtils.parseObject(resStr, ZKMsgRes.class);
        TestCase.assertNotNull(myResMsg);
        TestCase.assertEquals("zk.0", myResMsg.getCode());

        resMap = myResMsg.getData();

        // 路径参数
        TestCase.assertEquals(ZKWebmvcTestDataBinderController.pn_pathVariable1,
                resMap.get(ZKWebmvcTestDataBinderController.pn_pathVariable1));
        TestCase.assertEquals(ZKWebmvcTestDataBinderController.pn_pathVariable2,
                resMap.get(ZKWebmvcTestDataBinderController.pn_pathVariable2));

        // 所有 键值对 参数
//        TestCase.assertEquals(,
//                ZKJsonUtils.toJsonStr(
//                        resMap.get(DataBinderController.pn_allParamMap)));
        // 字符串
        TestCase.assertEquals(ZKWebmvcTestDataBinderController.pn_str + "字符串",
                resMap.get(ZKWebmvcTestDataBinderController.pn_str));
        // 字符 Array
        List<String> strs = (List<String>) resMap.get(ZKWebmvcTestDataBinderController.pn_strs);
        TestCase.assertEquals(ZKWebmvcTestDataBinderController.pn_strs + "-p1", strs.get(0));
        TestCase.assertEquals(ZKWebmvcTestDataBinderController.pn_strs + "-p2", strs.get(1));

        // 字符 List
        List<String> strList = (List<String>) resMap.get(ZKWebmvcTestDataBinderController.pn_strList);
        TestCase.assertEquals(ZKWebmvcTestDataBinderController.pn_strs + "-p1", strList.get(0));
        TestCase.assertEquals(ZKWebmvcTestDataBinderController.pn_strs + "-p2", strList.get(1));

        // 实体、实体 List、实体 Map 数据类型绑定
        TestCase.assertNotNull(resMap.get(ZKWebmvcTestDataBinderController.pn_entity));
        TestCase.assertNotNull(resMap.get(ZKWebmvcTestDataBinderController.pn_entityMap));
        TestCase.assertNotNull(resMap.get(ZKWebmvcTestDataBinderController.pn_entityList));
        TestCase.assertTrue(ZKWebmvcTestDataBinderController.initBinderFlagStr);
        TestCase.assertTrue(ZKWebmvcTestDataBinderController.initBinderFlagEntity);
        TestCase.assertTrue(ZKWebmvcTestDataBinderController.initBinderFlagEntityList);
        TestCase.assertTrue(ZKWebmvcTestDataBinderController.initBinderFlagEntityMap);

        // request body 参数
        if (method != HttpMethod.GET) {
            TestCase.assertEquals(jsonStr,
                    ZKJsonUtils.toJsonStr(resMap.get(ZKWebmvcTestDataBinderController.pn_bodyParam)));
        }
    }

}
