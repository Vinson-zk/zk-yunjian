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
 * @Title: ZKSerCenCertificateControllerTest.java 
 * @author Vinson 
 * @Package com.zk.server.central.controller 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:32:43 PM 
 * @version V1.0   
*/
package com.zk.server.central.controller;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKOrder;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.commons.data.ZKSortMode;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.server.central.entity.ZKSerCenCertificate;
import com.zk.server.central.helper.ZKSerCenTestRestTemplateMain;
import com.zk.server.central.service.ZKSerCenCertificateService;
import com.zk.server.central.service.ZKSerCenCertificateServiceTest;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSerCenCertificateControllerTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@SuppressWarnings("deprecation")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ZKSerCenTestRestTemplateMain.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:/" }, properties = { "spring.config.name=zk.ser.cen" })
public class ZKSerCenCertificateControllerTest {

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
        this.baseUrl = String.format("http://127.0.0.1:%s/%s/%s/cer", port, adminPath, modulePath);
    }

    @Test
    public void testScPost() {

        ZKSerCenCertificateService zkScs = null;
        List<String> pkIds = new ArrayList<String>();
        try {

            zkScs = ZKWebUtils.getAppCxt().getBean(ZKSerCenCertificateService.class);
            TestCase.assertNotNull(zkScs);

            HttpEntity<Map<String, Object>> requestEntity;
            Map<String, Object> serverCertificateMap = new HashMap<String, Object>();
            ResponseEntity<String> response = null;
            ZKMsgRes zkMsgRes = null;
            ZKSerCenCertificate zkSc = null;

//            // 这是另一种设置请求数据格式的方式，未采用
//            EntityBuilder entityBuilder = EntityBuilder.create();
//            // 设置内容编码格式
////            entityBuilder.setContentEncoding("UTF-8");
//            // 设置请求数据格式为 "application/json;charset=UTF-8"
//            entityBuilder.setContentType(ContentType.APPLICATION_JSON);
//            // 设置内容
//            ByteArrayInputStream contentIns = new ByteArrayInputStream(
//                    ZKJsonUtils.toJsonStr(serverCertificateMap).getBytes());
//            entityBuilder.setStream(DeflateInputStreamFactory.getInstance().create(contentIns));

            // 设置请求数据格式 通过请求头 headers 设置
            HttpHeaders requestHeaders = new HttpHeaders();
            // 设置请求数据格式为 "application/json;charset=UTF-8";
            requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
            requestHeaders.add("locale", "zh_CN");

            /*** "zk.000002" 数据验证失败, [serverName 不能为 null; status 取值超出边界] ***/
            // body
            serverCertificateMap = new HashMap<String, Object>();
            serverCertificateMap.put("status", "3");
            requestEntity = new HttpEntity<>(serverCertificateMap, requestHeaders);
//            response = testRestTemplate.postForEntity(baseUrl + "/sc", entityBuilder.build(), ZKMsgRes.class);
            response = testRestTemplate.postForEntity(baseUrl + "/sc", requestEntity, String.class);
            System.out.println("[^_^:20190829-1734-001] " + response.getBody());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("zk.000002", zkMsgRes.getCode());
            TestCase.assertEquals("数据验证失败", zkMsgRes.getMsg());

            /*** "zk.000002" [status 取值超出边界] 发起请求 ***/
            serverCertificateMap = new HashMap<String, Object>();
            serverCertificateMap.put("status", "3");
            serverCertificateMap.put("serverName", "ZKSerCenCertificateControllerTest");
            requestEntity = new HttpEntity<>(serverCertificateMap, requestHeaders);
//            response = testRestTemplate.postForEntity(baseUrl + "/sc", entityBuilder.build(), ZKMsgRes.class);
            response = testRestTemplate.postForEntity(baseUrl + "/sc", requestEntity, String.class);
            System.out.println("[^_^:20190829-1734-002] " + response.getBody());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("zk.000002", zkMsgRes.getCode());
            TestCase.assertEquals("数据验证失败", zkMsgRes.getMsg());

            /*** 成功保存 ***/
            zkSc = insert(0);

            pkIds.add(zkSc.getPkId());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            if (zkScs != null) {
                for (String pkId : pkIds) {
                    zkScs.diskDel(new ZKSerCenCertificate(pkId));
                }
            }
        }
    }

    // 插入一条，统一方法
    private ZKSerCenCertificate insert(int i) {
        return insert(i, null);
    }

    public static final String serverName = "ZKSerCenCertificateControllerTest.insert_";

    private ZKSerCenCertificate insert(int i, String spare1) {

        HttpEntity<Map<String, Object>> requestEntity;
        Map<String, Object> serverCertificateMap = new HashMap<String, Object>();
        ResponseEntity<String> response = null;
        ZKMsgRes zkMsgRes = null;
        ZKSerCenCertificate zkSc = null;

        // headers
        HttpHeaders requestHeaders = new HttpHeaders();
        // 设置请求数据格式为 "application/json;charset=UTF-8";
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        requestHeaders.add("locale", "zh_CN");

        /*** 成功保存 ***/
        serverCertificateMap = new HashMap<String, Object>();
        serverCertificateMap.put("serverName", serverName + i);
        serverCertificateMap.put("spare1", serverName + spare1);
        requestEntity = new HttpEntity<>(serverCertificateMap, requestHeaders);
        response = testRestTemplate.postForEntity(baseUrl + "/sc", requestEntity, String.class);
        System.out.println("[^_^:2019910-1708-001] " + response.getBody());
        zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
        TestCase.assertEquals("zk.0", zkMsgRes.getCode());
        zkSc = ZKJsonUtils.parseObject(zkMsgRes.getDataStr(), ZKSerCenCertificate.class);

        return zkSc;
    }

    @Test
    public void testScGet() {

        ZKSerCenCertificateService zkScs = null;
        List<String> pkIds = new ArrayList<String>();
        try {

            zkScs = ZKWebUtils.getAppCxt().getBean(ZKSerCenCertificateService.class);
            TestCase.assertNotNull(zkScs);

            ResponseEntity<String> response = null;
            ZKMsgRes zkMsgRes = null;
            ZKSerCenCertificate zkSc = null;

            // headers
            HttpHeaders requestHeaders = new HttpHeaders();
            // 设置请求数据格式为 "application/json;charset=UTF-8";
            requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
            requestHeaders.add("locale", "zh_CN");

            /*** 保存 ***/
            zkSc = insert(0);
            pkIds.add(zkSc.getPkId());

            /*** get ***/
            ZKSerCenCertificate zkScGet = null;
            response = testRestTemplate.getForEntity(baseUrl + "/sc?pkId=" + zkSc.getPkId(), String.class);
            System.out.println("[^_^:2019910-1708-002] " + response.getBody());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("0", zkMsgRes.getCode());
            zkScGet = ZKJsonUtils.parseObject(zkMsgRes.getDataStr(), ZKSerCenCertificate.class);

            TestCase.assertTrue(ZKSerCenCertificateServiceTest.testScVlaid(zkSc, zkScGet));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            if (zkScs != null) {
                for (String pkId : pkIds) {
                    zkScs.diskDel(new ZKSerCenCertificate(pkId));
                }
            }
        }
    }

    @Test
    public void testScDelete() {

        ZKSerCenCertificateService zkScs = null;
        List<String> pkIds = new ArrayList<String>();
        try {

            zkScs = ZKWebUtils.getAppCxt().getBean(ZKSerCenCertificateService.class);
            TestCase.assertNotNull(zkScs);

            ResponseEntity<String> response = null;
            ZKMsgRes zkMsgRes = null;
            ZKSerCenCertificate zkSc = null;

            // headers
            HttpHeaders requestHeaders = new HttpHeaders();
            // 设置请求数据格式为 "application/json;charset=UTF-8";
            requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
            requestHeaders.add("locale", "zh_CN");

            /*** 保存 ***/
            zkSc = insert(0);
            pkIds.add(zkSc.getPkId());

            /*** delete ***/
            ZKSerCenCertificate zkScGet = null;
            HttpHeaders headers = new HttpHeaders();
            MimeType mimeType = MimeTypeUtils.parseMimeType("application/json");
            MediaType mediaType = new MediaType(mimeType.getType(), mimeType.getSubtype(), Charset.forName("UTF-8"));
            // 请求体
            headers.setContentType(mediaType);

            HttpEntity<String> entity = new HttpEntity<>("", headers);
            response = testRestTemplate.exchange(baseUrl + "/sc?pkIds=" + zkSc.getPkId(), HttpMethod.DELETE, entity,
                    String.class);
            System.out.println("[^_^:2019910-1753-001] " + response.getBody());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("0", zkMsgRes.getCode());
            TestCase.assertEquals(1, zkMsgRes.getDataByClass(Integer.class).intValue());

            zkScGet = zkScs.get(zkSc);
            TestCase.assertEquals(ZKBaseEntity.DEL_FLAG.delete, zkScGet.getDelFlag().intValue());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            if (zkScs != null) {
                for (String pkId : pkIds) {
                    zkScs.diskDel(new ZKSerCenCertificate(pkId));
                }
            }
        }
    }

    /**
     * 分页，排序等
     *
     * @Title: testScGets
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 11, 2019 9:25:18 AM
     * @return void
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testScGetPage() {

        ZKSerCenCertificateService zkScs = null;
        List<String> pkIds = new ArrayList<String>();
        try {

            zkScs = ZKWebUtils.getAppCxt().getBean(ZKSerCenCertificateService.class);
            TestCase.assertNotNull(zkScs);

            int count = 23;
//            count = 3;

            ZKSerCenCertificate zkSc = null;
            for (int i = 0; i < count; ++i) {
                zkSc = insert(i, String.valueOf(count - (i / 5)));
                pkIds.add(zkSc.getPkId());
                Thread.sleep(1000);
            }

            int pageNo = 2;
            int pageSize = 8;
            ResponseEntity<String> response = null;
            ZKMsgRes zkMsgRes = null;
            ZKPage<ZKSerCenCertificate> zkScPage = null;

            response = testRestTemplate.getForEntity(String.format(
                    baseUrl + "/scPage?%s=%s&%s=%s&%s=%s&%s=%s&%s=%s&%s=%s&%s=%s", ZKPage.Param_Name.no, pageNo,
                    ZKPage.Param_Name.size, pageSize, ZKOrder.Param_Name.column, "c_spare1", ZKOrder.Param_Name.column,
                    "c_create_date", ZKOrder.Param_Name.mode, ZKSortMode.DESC.getValue(), ZKOrder.Param_Name.mode,
                    ZKSortMode.DESC.getValue(), "serverName", serverName), String.class);

            System.out.println("[^_^:20190911-1400-001] " + response.getBody());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("0", zkMsgRes.getCode());

            zkScPage = ZKJsonUtils.parseObject(zkMsgRes.getDataStr(), ZKPage.class);
//            zkScPage = ZKJsonUtils.parseObject(zkMsgRes.getDataStr(), ZKPage.class, false);
            TestCase.assertEquals(count, zkScPage.getTotalCount());
            TestCase.assertEquals(pageNo, zkScPage.getPageNo());
            TestCase.assertEquals(pageSize, zkScPage.getPageSize());

            TestCase.assertEquals(pageSize, zkScPage.getResult().size());
//            TestCase.assertEquals("c_spare1 DESC,c_create_date DESC", zkScPage.getOrderBySql());

//            HttpEntity<Map<String, Object>> requestEntity;
//            Map<String, Object> serverCertificateMap = new HashMap<String, Object>();
//            // headers
//            HttpHeaders requestHeaders = new HttpHeaders();
//            // 设置请求数据格式为 "application/json;charset=UTF-8";
//            requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
//
//            // body
//            serverCertificateMap = new HashMap<String, Object>();
//            requestEntity = new HttpEntity<>(serverCertificateMap, requestHeaders);
//            response = testRestTemplate.getForEntity(baseUrl + "/sc", requestEntity, String.class);
//            System.out.println("[^_^:20190829-1734-001] " + response.getBody());
//            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
//            TestCase.assertEquals("zk.000002", zkMsgRes.getCode());
//            TestCase.assertEquals("数据验证失败", zkMsgRes.getMsg());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            if (zkScs != null) {
                for (String pkId : pkIds) {
                    zkScs.diskDel(new ZKSerCenCertificate(pkId));
                }
            }
        }
    }

    @Test
    public void testScStatus() {

        ZKSerCenCertificateService zkScs = null;
        List<String> pkIds = new ArrayList<String>();
        try {

            zkScs = ZKWebUtils.getAppCxt().getBean(ZKSerCenCertificateService.class);
            TestCase.assertNotNull(zkScs);

            ResponseEntity<String> response = null;
            ZKMsgRes zkMsgRes = null;
            ZKSerCenCertificate zkSc = null;

            HttpEntity<Map<String, Object>> requestEntity = null;
            // headers
            HttpHeaders requestHeaders = new HttpHeaders();
            // 设置请求数据格式为 "application/json;charset=UTF-8";
            requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
            requestHeaders.add("locale", "zh_CN");

            /*** 保存 ***/
            zkSc = insert(0);
            pkIds.add(zkSc.getPkId());

            zkSc = insert(0);
            pkIds.add(zkSc.getPkId());

            Map<String, Object> serverCertificateMap = new HashMap<String, Object>();
//            serverCertificateMap.put("status", "3");
            requestEntity = new HttpEntity<>(serverCertificateMap, requestHeaders);

            /*** zk.000002 ***/
            response = testRestTemplate.postForEntity(
                    String.format(baseUrl + "/scStatus?pkIds=%s&pkIds=%s&status=%s", pkIds.get(0), pkIds.get(1), 3),
                    requestEntity, String.class);
            System.out.println("[^_^:2019911-2354-001] " + response.getBody());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("zk.000002", zkMsgRes.getCode());
            TestCase.assertEquals("数字必须在[0,1]之间！", zkMsgRes.getDataStr());

            /*** 禁用 ***/
            response = testRestTemplate.postForEntity(
                    String.format(baseUrl + "/scStatus?pkIds=%s&pkIds=%s&status=%s", pkIds.get(0), pkIds.get(1), 1),
                    requestEntity, String.class);
            System.out.println("[^_^:2019911-2354-002] " + response.getBody());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("0", zkMsgRes.getCode());
            TestCase.assertEquals(2, zkMsgRes.getDataByClass(Integer.class).intValue());
            zkSc = zkScs.get(new ZKSerCenCertificate(pkIds.get(0)));
            TestCase.assertEquals(ZKSerCenCertificate.StatusType.Disabled, zkSc.getStatus().intValue());
            zkSc = zkScs.get(new ZKSerCenCertificate(pkIds.get(1)));
            TestCase.assertEquals(ZKSerCenCertificate.StatusType.Disabled, zkSc.getStatus().intValue());
            /*** 启用 ***/
            response = testRestTemplate.postForEntity(
                    String.format(baseUrl + "/scStatus?pkIds=%s&status=%s", pkIds.get(1), 0), requestEntity,
                    String.class);
            System.out.println("[^_^:2019911-2354-003] " + response.getBody());
            zkMsgRes = ZKJsonUtils.parseObject(response.getBody(), ZKMsgRes.class);
            TestCase.assertEquals("0", zkMsgRes.getCode());
            TestCase.assertEquals(1, zkMsgRes.getDataByClass(Integer.class).intValue());
            zkSc = zkScs.get(new ZKSerCenCertificate(pkIds.get(0)));
            TestCase.assertEquals(ZKSerCenCertificate.StatusType.Disabled, zkSc.getStatus().intValue());
            zkSc = zkScs.get(new ZKSerCenCertificate(pkIds.get(1)));
            TestCase.assertEquals(ZKSerCenCertificate.StatusType.Enable, zkSc.getStatus().intValue());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            if (zkScs != null) {
                for (String pkId : pkIds) {
                    zkScs.diskDel(new ZKSerCenCertificate(pkId));
                }
            }
        }
    }

}
