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
 * @Title: ZKSampleRsaAesTransferCipherManagerTest.java 
 * @author Vinson 
 * @Package com.zk.core.encrypt.support 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 3:39:26 PM 
 * @version V1.0   
*/
package com.zk.webmvc.encrypt;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.zk.core.commons.ZKContentType;
import com.zk.core.encrypt.utils.ZKEncryptAesUtils;
import com.zk.core.encrypt.utils.ZKEncryptRsaUtils;
import com.zk.core.encrypt.utils.ZKEncryptUtils;
import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.web.encrypt.ZKSampleRsaAesTransferCipherManager;
import com.zk.core.web.encrypt.ZKTransferCipherManager;
import com.zk.core.web.support.servlet.filter.ZKTransferCipherFilter;
import com.zk.webmvc.helper.ZKWebmvcTestHelperMvcSpringBootMain;
import com.zk.webmvc.helper.controller.ZKWebmvcTestTransferCipherController;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSampleRsaAesTransferCipherManagerTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ZKWebmvcTestHelperMvcSpringBootMain.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ZKSampleRsaAesTransferCipherManagerTest {

    private static Logger log = LogManager.getLogger(ZKSampleRsaAesTransferCipherManagerTest.class);

    @Autowired
    WebApplicationContext wac;

//    @Autowired
    private MockMvc mvc;

    @Value("${zk.path.admin:zk}")
    String adminPath;

    @Value("${zk.path.core:c}")
    String modulePath;

    String baseUrl = "";

    ZKTransferCipherManager transferCipherManager = null;

    static interface ZKRSA_ReqHeader extends ZKSampleRsaAesTransferCipherManager.ZKRSA_ReqHeader {
    }

    @Before
    public void before() {
        try {
            this.baseUrl = "/" + adminPath + "/" + modulePath + "/tc";

//            transferCipherManager = wac.getBean(ZKTransferCipherManager.class);
//            TestCase.assertNotNull(transferCipherManager);

            transferCipherManager = new ZKSampleRsaAesTransferCipherManager();

            ZKTransferCipherFilter transferCipherFilter = new ZKTransferCipherFilter();

            transferCipherFilter.setArTransferCipherManager(transferCipherManager);
            this.mvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(transferCipherFilter, this.baseUrl + "/*")
                    .build();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    static interface RequestParams extends ZKWebmvcTestTransferCipherController.RequestParams {
    }

    static interface msg extends ZKWebmvcTestTransferCipherController.msg {
    }

    @Test
    public void testTransferCipher() {
        try {

            MockHttpServletRequestBuilder mockReqBuilder = null;
            MvcResult mvcResult = null;
            MockHttpServletResponse mockRes = null;
            String resStr = null;
            byte[] resEncData = null;
            Map<String, ?> resMap = null;

            Map<String, Object> reqBodyMap = new HashMap<String, Object>();
            String encParameterStr = null;
            String encReqBodyStr = null;
            int intValue = 3;

            String url = String.format("%s/transferCipher/%s/%s", this.baseUrl, RequestParams.pn_pathVariable1,
                    RequestParams.pn_pathVariable2);

            ZKSampleRsaAesTransferCipherManager sampleRsaAesTc = (ZKSampleRsaAesTransferCipherManager) this.transferCipherManager;

            /*** 不进行加解密 ***/
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            // 添加请求头
            mockReqBuilder.header(RequestParams.pn_req_header, msg.value + RequestParams.pn_req_header);
            // 添加请求属性
            mockReqBuilder.requestAttr(RequestParams.pn_req_attr, msg.value + RequestParams.pn_req_attr);
            // 添加请求参数，数组
            mockReqBuilder.param(RequestParams.pn_strs, msg.value + RequestParams.pn_strs,
                    msg.value + RequestParams.pn_strs);
            mockReqBuilder.param(RequestParams.pn_int, String.valueOf(intValue));
            // 添加请求体 requestBody
            reqBodyMap.put(RequestParams.pn_strs,
                    new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs });
            // 键值对 提交
            // mockReqBuilder.contentType(EnumContentType.X_FORM.getContentType());
            // application/json 提交; 如果需要使用 Request Body；必须以这种方式 提交请求；
            mockReqBuilder.contentType(ZKContentType.JSON_UTF8.getContentType());
            mockReqBuilder.content(ZKJsonUtils.toJsonStr(reqBodyMap));

            // 发起请求
            mvcResult = this.mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk())
//                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            mockRes = mvcResult.getResponse();
            resStr = mockRes.getContentAsString();
            log.info("[^_^:20190625-1121-001] " + resStr);

            resMap = ZKJsonUtils.parseMap(resStr);

            TestCase.assertEquals(msg.normal_transferCipher + RequestParams.pn_pathVariable1,
                    resMap.get(RequestParams.pn_pathVariable1));
            TestCase.assertEquals(msg.normal_transferCipher + RequestParams.pn_pathVariable2,
                    resMap.get(RequestParams.pn_pathVariable2));

            TestCase.assertEquals(msg.normal_transferCipher + msg.value + RequestParams.pn_req_header,
                    resMap.get(RequestParams.pn_req_header));
            TestCase.assertEquals(msg.normal_transferCipher + msg.value + RequestParams.pn_req_attr,
                    resMap.get(RequestParams.pn_req_attr));

            TestCase.assertEquals(
                    msg.normal_transferCipher + ZKJsonUtils.toJsonStr(
                            new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs }),
                    resMap.get(RequestParams.pn_strs));
            TestCase.assertEquals(intValue * intValue, resMap.get(RequestParams.pn_int));
            TestCase.assertEquals(ZKJsonUtils.toJsonStr(reqBodyMap),
                    ZKJsonUtils.toJsonStr(resMap.get(RequestParams.RequestBody)));

            log.info("[^_^:20190625-1121-001] === 不进行加解密请求处理成功 ------------- ");
            /*** 需要进行加解密 ***/
            Map<String, Object> paramMap = new HashMap<>();
            byte[] publicKey = sampleRsaAesTc.getZKRSAKeyByRsaId(null).getPublicKey();
            byte[] dataKey = "疑行无名，疑事无功".getBytes();
            byte[] salt = ZKEncryptUtils.genSalt(dataKey);

            log.info("[^_^:20190626-1632-001-0] 加密前 密钥：" + ZKEncodingUtils.encodeHex(dataKey));
            String encDataKeyStr = ZKEncodingUtils.encodeHex(ZKEncryptRsaUtils.encrypt(dataKey, publicKey));
            log.info("[^_^:20190626-1632-001-1] 加密后 密钥：" + encDataKeyStr);

            /*** post ***/
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            // 添加请求头
            mockReqBuilder.header(RequestParams.pn_req_header, msg.value + RequestParams.pn_req_header);
            // 添加请求属性
            mockReqBuilder.requestAttr(RequestParams.pn_req_attr, msg.value + RequestParams.pn_req_attr);
            // 添加请求参数，数组
            paramMap.put(RequestParams.pn_strs,
                    new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs });
            paramMap.put(RequestParams.pn_int, intValue);
            encParameterStr = ZKJsonUtils.toJsonStr(paramMap);
            log.info("[^_^:20190626-1632-002-0] 加密前 参数：" + encParameterStr);
            encParameterStr = ZKEncodingUtils
                    .encodeHex((ZKEncryptAesUtils.encrypt(encParameterStr.getBytes("utf-8"), dataKey, salt)));
            log.info("[^_^:20190626-1632-002-1] 加密后 参数：" + encParameterStr);
            mockReqBuilder.param(ZKRSA_ReqHeader.pn_parameter, encParameterStr);
            // 添加请求体 requestBody
            reqBodyMap.put(RequestParams.pn_strs,
                    new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs });
            reqBodyMap.put(RequestParams.pn_int, intValue);
            // 键值对 提交
            // mockReqBuilder.contentType(EnumContentType.X_FORM.getContentType());
            // application/json 提交; 如果需要使用 Request Body；必须以这种方式 提交请求；
            mockReqBuilder.contentType(ZKContentType.JSON_UTF8.getContentType());
            encReqBodyStr = ZKJsonUtils.toJsonStr(reqBodyMap);
            log.info("[^_^:20190626-1632-003-0] 加密前 reqBody：" + encReqBodyStr);
            encReqBodyStr = ZKEncodingUtils
                    .encodeHex(ZKEncryptAesUtils.encrypt(encReqBodyStr.getBytes(), dataKey, salt));
            log.info("[^_^:20190626-1632-003-1] 加密后 reqBody：" + encReqBodyStr);
            mockReqBuilder.content(encReqBodyStr);
            // 随意添加一个 rsa ID，测试的传输加解密中是硬编码的一个证书
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_id, "KKK");
            // 添加 rsa 公钥加密后的 数据加密密钥
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_key, encDataKeyStr);
            // 发起请求
            mvcResult = this.mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk())
//                  .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            mockRes = mvcResult.getResponse();
            resEncData = mockRes.getContentAsByteArray();
            log.info("[^_^:20190625-1121-002-0] 解密前 响应数据:" + new String(resEncData));
            resStr = new String(
                    ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(new String(resEncData)), dataKey, salt));
            log.info("[^_^:20190625-1121-002-1] 解密后 响应数据： " + resStr);

            resMap = ZKJsonUtils.parseMap(resStr);

            TestCase.assertEquals(msg.normal_transferCipher + RequestParams.pn_pathVariable1,
                    resMap.get(RequestParams.pn_pathVariable1));
            TestCase.assertEquals(msg.normal_transferCipher + RequestParams.pn_pathVariable2,
                    resMap.get(RequestParams.pn_pathVariable2));

            TestCase.assertEquals(msg.normal_transferCipher + msg.value + RequestParams.pn_req_header,
                    resMap.get(RequestParams.pn_req_header));
            TestCase.assertEquals(msg.normal_transferCipher + msg.value + RequestParams.pn_req_attr,
                    resMap.get(RequestParams.pn_req_attr));

            TestCase.assertEquals(
                    msg.normal_transferCipher + ZKJsonUtils.toJsonStr(
                            new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs }),
                    resMap.get(RequestParams.pn_strs));
            TestCase.assertEquals(intValue * intValue, resMap.get(RequestParams.pn_int));
            TestCase.assertEquals(ZKJsonUtils.toJsonStr(reqBodyMap),
                    ZKJsonUtils.toJsonStr(resMap.get(RequestParams.RequestBody)));
            log.info("[^_^:20190625-1121-001] === post 进行加解密请求处理成功 ------------- ");

            /*** delete ***/
            mockReqBuilder = MockMvcRequestBuilders.delete(url);
            // 添加请求头
            mockReqBuilder.header(RequestParams.pn_req_header, msg.value + RequestParams.pn_req_header);
            // 添加请求属性
            mockReqBuilder.requestAttr(RequestParams.pn_req_attr, msg.value + RequestParams.pn_req_attr);
            // 添加请求参数，数组
            paramMap.put(RequestParams.pn_strs,
                    new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs });
            paramMap.put(RequestParams.pn_int, intValue);
            encParameterStr = ZKJsonUtils.toJsonStr(paramMap);
            log.info("[^_^:20190626-1632-002-0] 加密前 参数：" + encParameterStr);
            encParameterStr = ZKEncodingUtils
                    .encodeHex((ZKEncryptAesUtils.encrypt(encParameterStr.getBytes("utf-8"), dataKey, salt)));
            log.info("[^_^:20190626-1632-002-1] 加密后 参数：" + encParameterStr);
            mockReqBuilder.param(ZKRSA_ReqHeader.pn_parameter, encParameterStr);
            // 添加请求体 requestBody
            reqBodyMap.put(RequestParams.pn_strs,
                    new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs });
            reqBodyMap.put(RequestParams.pn_int, intValue);
            // 键值对 提交
            // mockReqBuilder.contentType(EnumContentType.X_FORM.getContentType());
            // application/json 提交; 如果需要使用 Request Body；必须以这种方式 提交请求；
            mockReqBuilder.contentType(ZKContentType.JSON_UTF8.getContentType());
            encReqBodyStr = ZKJsonUtils.toJsonStr(reqBodyMap);
            log.info("[^_^:20190626-1632-003-0] 加密前 reqBody：" + encReqBodyStr);
            encReqBodyStr = ZKEncodingUtils
                    .encodeHex(ZKEncryptAesUtils.encrypt(encReqBodyStr.getBytes(), dataKey, salt));
            log.info("[^_^:20190626-1632-003-1] 加密后 reqBody：" + encReqBodyStr);
            mockReqBuilder.content(encReqBodyStr);
            // 随意添加一个 rsa ID，测试的传输加解密中是硬编码的一个证书
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_id, "KKK");
            // 添加 rsa 公钥加密后的 数据加密密钥
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_key, encDataKeyStr);
            // 发起请求
            mvcResult = this.mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk())
//                  .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            mockRes = mvcResult.getResponse();
            resEncData = mockRes.getContentAsByteArray();
            log.info("[^_^:20190625-1121-002-0] 解密前 响应数据:" + new String(resEncData));
            resStr = new String(
                    ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(new String(resEncData)), dataKey, salt));
            log.info("[^_^:20190625-1121-002-1] 解密后 响应数据： " + resStr);

            resMap = ZKJsonUtils.parseMap(resStr);

            TestCase.assertEquals(msg.normal_transferCipher + RequestParams.pn_pathVariable1,
                    resMap.get(RequestParams.pn_pathVariable1));
            TestCase.assertEquals(msg.normal_transferCipher + RequestParams.pn_pathVariable2,
                    resMap.get(RequestParams.pn_pathVariable2));

            TestCase.assertEquals(msg.normal_transferCipher + msg.value + RequestParams.pn_req_header,
                    resMap.get(RequestParams.pn_req_header));
            TestCase.assertEquals(msg.normal_transferCipher + msg.value + RequestParams.pn_req_attr,
                    resMap.get(RequestParams.pn_req_attr));

            TestCase.assertEquals(
                    msg.normal_transferCipher + ZKJsonUtils.toJsonStr(
                            new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs }),
                    resMap.get(RequestParams.pn_strs));
            TestCase.assertEquals(intValue * intValue, resMap.get(RequestParams.pn_int));
            TestCase.assertEquals(ZKJsonUtils.toJsonStr(reqBodyMap),
                    ZKJsonUtils.toJsonStr(resMap.get(RequestParams.RequestBody)));
            log.info("[^_^:20190625-1121-001] === delete 进行加解密请求处理成功 ------------- ");

            /*** put ***/
            mockReqBuilder = MockMvcRequestBuilders.put(url);
            // 添加请求头
            mockReqBuilder.header(RequestParams.pn_req_header, msg.value + RequestParams.pn_req_header);
            // 添加请求属性
            mockReqBuilder.requestAttr(RequestParams.pn_req_attr, msg.value + RequestParams.pn_req_attr);
            // 添加请求参数，数组
            paramMap.put(RequestParams.pn_strs,
                    new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs });
            paramMap.put(RequestParams.pn_int, intValue);
            encParameterStr = ZKJsonUtils.toJsonStr(paramMap);
            log.info("[^_^:20190626-1632-002-0] 加密前 参数：" + encParameterStr);
            encParameterStr = ZKEncodingUtils
                    .encodeHex((ZKEncryptAesUtils.encrypt(encParameterStr.getBytes("utf-8"), dataKey, salt)));
            log.info("[^_^:20190626-1632-002-1] 加密后 参数：" + encParameterStr);
            mockReqBuilder.param(ZKRSA_ReqHeader.pn_parameter, encParameterStr);
            // 添加请求体 requestBody
            reqBodyMap.put(RequestParams.pn_strs,
                    new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs });
            reqBodyMap.put(RequestParams.pn_int, intValue);
            // 键值对 提交
            // mockReqBuilder.contentType(EnumContentType.X_FORM.getContentType());
            // application/json 提交; 如果需要使用 Request Body；必须以这种方式 提交请求；
            mockReqBuilder.contentType(ZKContentType.JSON_UTF8.getContentType());
            encReqBodyStr = ZKJsonUtils.toJsonStr(reqBodyMap);
            log.info("[^_^:20190626-1632-003-0] 加密前 reqBody：" + encReqBodyStr);
            encReqBodyStr = ZKEncodingUtils
                    .encodeHex(ZKEncryptAesUtils.encrypt(encReqBodyStr.getBytes(), dataKey, salt));
            log.info("[^_^:20190626-1632-003-1] 加密后 reqBody：" + encReqBodyStr);
            mockReqBuilder.content(encReqBodyStr);
            // 随意添加一个 rsa ID，测试的传输加解密中是硬编码的一个证书
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_id, "KKK");
            // 添加 rsa 公钥加密后的 数据加密密钥
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_key, encDataKeyStr);
            // 发起请求
            mvcResult = this.mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print()).andReturn();
            mockRes = mvcResult.getResponse();
            resEncData = mockRes.getContentAsByteArray();
            log.info("[^_^:20190625-1121-002-0] 解密前 响应数据:" + new String(resEncData));
            resStr = new String(
                    ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(new String(resEncData)), dataKey, salt));
            log.info("[^_^:20190625-1121-002-1] 解密后 响应数据： " + resStr);

            resMap = ZKJsonUtils.parseMap(resStr);

            TestCase.assertEquals(msg.normal_transferCipher + RequestParams.pn_pathVariable1,
                    resMap.get(RequestParams.pn_pathVariable1));
            TestCase.assertEquals(msg.normal_transferCipher + RequestParams.pn_pathVariable2,
                    resMap.get(RequestParams.pn_pathVariable2));

            TestCase.assertEquals(msg.normal_transferCipher + msg.value + RequestParams.pn_req_header,
                    resMap.get(RequestParams.pn_req_header));
            TestCase.assertEquals(msg.normal_transferCipher + msg.value + RequestParams.pn_req_attr,
                    resMap.get(RequestParams.pn_req_attr));

            TestCase.assertEquals(
                    msg.normal_transferCipher + ZKJsonUtils.toJsonStr(
                            new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs }),
                    resMap.get(RequestParams.pn_strs));
            TestCase.assertEquals(intValue * intValue, resMap.get(RequestParams.pn_int));
            TestCase.assertEquals(ZKJsonUtils.toJsonStr(reqBodyMap),
                    ZKJsonUtils.toJsonStr(resMap.get(RequestParams.RequestBody)));
            log.info("[^_^:20190625-1121-001] === put 进行加解密请求处理成功 ------------- ");
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }

    }

    @Test
    public void testTransferCipherNoReqBody() {
        try {

            MockHttpServletRequestBuilder mockReqBuilder = null;
            MvcResult mvcResult = null;
            MockHttpServletResponse mockRes = null;
            String resStr = null;
            byte[] resEncData = null;
            Map<String, ?> resMap = null;

            String encParameterStr = null;
            int intValue = 3;

            String url = String.format("%s/transferCipherNoReqBody/%s/%s", this.baseUrl, RequestParams.pn_pathVariable1,
                    RequestParams.pn_pathVariable2);

            ZKSampleRsaAesTransferCipherManager sampleRsaAesTc = (ZKSampleRsaAesTransferCipherManager) this.transferCipherManager;

            /*** 不进行加解密 ***/
            mockReqBuilder = MockMvcRequestBuilders.get(url);
            // 添加请求头
            mockReqBuilder.header(RequestParams.pn_req_header, msg.value + RequestParams.pn_req_header);
            // 添加请求属性
            mockReqBuilder.requestAttr(RequestParams.pn_req_attr, msg.value + RequestParams.pn_req_attr);
            // 添加请求参数，数组
            mockReqBuilder.param(RequestParams.pn_strs, msg.value + RequestParams.pn_strs,
                    msg.value + RequestParams.pn_strs);
            mockReqBuilder.param(RequestParams.pn_int, String.valueOf(intValue));

            // 发起请求
            mvcResult = this.mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk())
//                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            mockRes = mvcResult.getResponse();
            resStr = mockRes.getContentAsString();
            log.info("[^_^:20190625-1121-001] " + resStr);

            resMap = ZKJsonUtils.parseMap(resStr);

            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + RequestParams.pn_pathVariable1,
                    resMap.get(RequestParams.pn_pathVariable1));
            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + RequestParams.pn_pathVariable2,
                    resMap.get(RequestParams.pn_pathVariable2));

            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + msg.value + RequestParams.pn_req_header,
                    resMap.get(RequestParams.pn_req_header));
            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + msg.value + RequestParams.pn_req_attr,
                    resMap.get(RequestParams.pn_req_attr));

            TestCase.assertEquals(
                    msg.normal_transferCipherNoReqBody + ZKJsonUtils.toJsonStr(
                            new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs }),
                    resMap.get(RequestParams.pn_strs));
            TestCase.assertEquals(intValue * intValue, resMap.get(RequestParams.pn_int));

            log.info("[^_^:20190625-1121-001] === 不进行加解密请求处理成功 ------------- ");

            /*** 需要进行加解密 ***/
            Map<String, Object> paramMap = new HashMap<>();
            byte[] publicKey = sampleRsaAesTc.getZKRSAKeyByRsaId(null).getPublicKey();
            byte[] dataKey = "疑行无名，疑事无功".getBytes();
            byte[] salt = ZKEncryptUtils.genSalt(dataKey);

            log.info("[^_^:20190626-1632-001-0] 加密前 密钥：" + new String(dataKey));
            String encDataKeyStr = ZKEncodingUtils.encodeHex(ZKEncryptRsaUtils.encrypt(dataKey, publicKey));
            log.info("[^_^:20190626-1632-001-1] 加密后 密钥：" + encDataKeyStr);

            /*** get ***/
            mockReqBuilder = MockMvcRequestBuilders.get(url);
            // 添加请求头
            mockReqBuilder.header(RequestParams.pn_req_header, msg.value + RequestParams.pn_req_header);
            // 添加请求属性
            mockReqBuilder.requestAttr(RequestParams.pn_req_attr, msg.value + RequestParams.pn_req_attr);
            // 添加请求参数，数组
            paramMap.put(RequestParams.pn_strs,
                    new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs });
            paramMap.put(RequestParams.pn_int, intValue);
            encParameterStr = ZKJsonUtils.toJsonStr(paramMap);
            log.info("[^_^:20190626-1632-002-0] 加密前 参数：" + encParameterStr);
            encParameterStr = ZKEncodingUtils
                    .encodeHex((ZKEncryptAesUtils.encrypt(encParameterStr.getBytes("utf-8"), dataKey, salt)));
            log.info("[^_^:20190626-1632-002-1] 加密后 参数：" + encParameterStr);
            mockReqBuilder.param(ZKRSA_ReqHeader.pn_parameter, encParameterStr);

            // 随意添加一个 rsa ID，测试的传输加解密中是硬编码的一个证书
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_id, "KKK");
            // 添加 rsa 公钥加密后的 数据加密密钥
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_key, encDataKeyStr);
            // 发起请求
            mvcResult = this.mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk())
//                  .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            mockRes = mvcResult.getResponse();
            resEncData = mockRes.getContentAsByteArray();
            log.info("[^_^:20190625-1121-002-0] 解密前 响应数据:" + new String(resEncData));
            resStr = new String(
                    ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(new String(resEncData)), dataKey, salt));
            log.info("[^_^:20190625-1121-002-1] 解密后 响应数据： " + resStr);

            resMap = ZKJsonUtils.parseMap(resStr);

            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + RequestParams.pn_pathVariable1,
                    resMap.get(RequestParams.pn_pathVariable1));
            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + RequestParams.pn_pathVariable2,
                    resMap.get(RequestParams.pn_pathVariable2));

            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + msg.value + RequestParams.pn_req_header,
                    resMap.get(RequestParams.pn_req_header));
            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + msg.value + RequestParams.pn_req_attr,
                    resMap.get(RequestParams.pn_req_attr));

            TestCase.assertEquals(
                    msg.normal_transferCipherNoReqBody + ZKJsonUtils.toJsonStr(
                            new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs }),
                    resMap.get(RequestParams.pn_strs));
            TestCase.assertEquals(intValue * intValue, resMap.get(RequestParams.pn_int));
            log.info("[^_^:20190625-1121-001] === get 进行加解密请求处理成功 ------------- ");

            /*** post ***/
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            // 添加请求头
            mockReqBuilder.header(RequestParams.pn_req_header, msg.value + RequestParams.pn_req_header);
            // 添加请求属性
            mockReqBuilder.requestAttr(RequestParams.pn_req_attr, msg.value + RequestParams.pn_req_attr);
            // 添加请求参数，数组
            paramMap.put(RequestParams.pn_strs,
                    new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs });
            paramMap.put(RequestParams.pn_int, intValue);
            encParameterStr = ZKJsonUtils.toJsonStr(paramMap);
            log.info("[^_^:20190626-1632-002-0] 加密前 参数：" + encParameterStr);
            encParameterStr = ZKEncodingUtils
                    .encodeHex((ZKEncryptAesUtils.encrypt(encParameterStr.getBytes("utf-8"), dataKey, salt)));
            log.info("[^_^:20190626-1632-002-1] 加密后 参数：" + encParameterStr);
            mockReqBuilder.param(ZKRSA_ReqHeader.pn_parameter, encParameterStr);

            // 随意添加一个 rsa ID，测试的传输加解密中是硬编码的一个证书
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_id, "KKK");
            // 添加 rsa 公钥加密后的 数据加密密钥
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_key, encDataKeyStr);
            // 发起请求
            mvcResult = this.mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk())
//                  .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            mockRes = mvcResult.getResponse();
            resEncData = mockRes.getContentAsByteArray();
            log.info("[^_^:20190625-1121-002-0] 解密前 响应数据:" + new String(resEncData));
            resStr = new String(
                    ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(new String(resEncData)), dataKey, salt));
            log.info("[^_^:20190625-1121-002-1] 解密后 响应数据： " + resStr);

            resMap = ZKJsonUtils.parseMap(resStr);

            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + RequestParams.pn_pathVariable1,
                    resMap.get(RequestParams.pn_pathVariable1));
            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + RequestParams.pn_pathVariable2,
                    resMap.get(RequestParams.pn_pathVariable2));

            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + msg.value + RequestParams.pn_req_header,
                    resMap.get(RequestParams.pn_req_header));
            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + msg.value + RequestParams.pn_req_attr,
                    resMap.get(RequestParams.pn_req_attr));

            TestCase.assertEquals(
                    msg.normal_transferCipherNoReqBody + ZKJsonUtils.toJsonStr(
                            new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs }),
                    resMap.get(RequestParams.pn_strs));
            TestCase.assertEquals(intValue * intValue, resMap.get(RequestParams.pn_int));
            log.info("[^_^:20190625-1121-001] === post 进行加解密请求处理成功 ------------- ");

            /*** delete ***/
            mockReqBuilder = MockMvcRequestBuilders.delete(url);
            // 添加请求头
            mockReqBuilder.header(RequestParams.pn_req_header, msg.value + RequestParams.pn_req_header);
            // 添加请求属性
            mockReqBuilder.requestAttr(RequestParams.pn_req_attr, msg.value + RequestParams.pn_req_attr);
            // 添加请求参数，数组
            paramMap.put(RequestParams.pn_strs,
                    new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs });
            paramMap.put(RequestParams.pn_int, intValue);
            encParameterStr = ZKJsonUtils.toJsonStr(paramMap);
            log.info("[^_^:20190626-1632-002-0] 加密前 参数：" + encParameterStr);
            encParameterStr = ZKEncodingUtils
                    .encodeHex((sampleRsaAesTc.encryptData(dataKey, salt, encParameterStr.getBytes("utf-8"))));
            log.info("[^_^:20190626-1632-002-1] 加密后 参数：" + encParameterStr);
            mockReqBuilder.param(ZKRSA_ReqHeader.pn_parameter, encParameterStr);

            // 随意添加一个 rsa ID，测试的传输加解密中是硬编码的一个证书
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_id, "KKK");
            // 添加 rsa 公钥加密后的 数据加密密钥
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_key, encDataKeyStr);
            // 发起请求
            mvcResult = this.mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk())
//                  .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            mockRes = mvcResult.getResponse();
            resEncData = mockRes.getContentAsByteArray();
            log.info("[^_^:20190625-1121-002-0] 解密前 响应数据:" + new String(resEncData));
            resStr = new String(
                    ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(new String(resEncData)), dataKey, salt));
            log.info("[^_^:20190625-1121-002-1] 解密后 响应数据： " + resStr);

            resMap = ZKJsonUtils.parseMap(resStr);

            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + RequestParams.pn_pathVariable1,
                    resMap.get(RequestParams.pn_pathVariable1));
            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + RequestParams.pn_pathVariable2,
                    resMap.get(RequestParams.pn_pathVariable2));

            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + msg.value + RequestParams.pn_req_header,
                    resMap.get(RequestParams.pn_req_header));
            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + msg.value + RequestParams.pn_req_attr,
                    resMap.get(RequestParams.pn_req_attr));

            TestCase.assertEquals(
                    msg.normal_transferCipherNoReqBody + ZKJsonUtils.toJsonStr(
                            new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs }),
                    resMap.get(RequestParams.pn_strs));
            TestCase.assertEquals(intValue * intValue, resMap.get(RequestParams.pn_int));
            log.info("[^_^:20190625-1121-001] === delete 进行加解密请求处理成功 ------------- ");

            /*** put ***/
            mockReqBuilder = MockMvcRequestBuilders.put(url);
            // 添加请求头
            mockReqBuilder.header(RequestParams.pn_req_header, msg.value + RequestParams.pn_req_header);
            // 添加请求属性
            mockReqBuilder.requestAttr(RequestParams.pn_req_attr, msg.value + RequestParams.pn_req_attr);
            // 添加请求参数，数组
            paramMap.put(RequestParams.pn_strs,
                    new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs });
            paramMap.put(RequestParams.pn_int, intValue);
            encParameterStr = ZKJsonUtils.toJsonStr(paramMap);
            log.info("[^_^:20190626-1632-002-0] 加密前 参数：" + encParameterStr);
            encParameterStr = ZKEncodingUtils
                    .encodeHex((sampleRsaAesTc.encryptData(dataKey, salt, encParameterStr.getBytes("utf-8"))));
            log.info("[^_^:20190626-1632-002-1] 加密后 参数：" + encParameterStr);
            mockReqBuilder.param(ZKRSA_ReqHeader.pn_parameter, encParameterStr);

            // 随意添加一个 rsa ID，测试的传输加解密中是硬编码的一个证书
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_id, "KKK");
            // 添加 rsa 公钥加密后的 数据加密密钥
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_key, encDataKeyStr);
            // 发起请求
            mvcResult = this.mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print()).andReturn();
            mockRes = mvcResult.getResponse();
            resEncData = mockRes.getContentAsByteArray();
            log.info("[^_^:20190625-1121-002-0] 解密前 响应数据:" + new String(resEncData));
            resStr = new String(
                    ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(new String(resEncData)), dataKey, salt));
            log.info("[^_^:20190625-1121-002-1] 解密后 响应数据： " + resStr);

            resMap = ZKJsonUtils.parseMap(resStr);

            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + RequestParams.pn_pathVariable1,
                    resMap.get(RequestParams.pn_pathVariable1));
            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + RequestParams.pn_pathVariable2,
                    resMap.get(RequestParams.pn_pathVariable2));

            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + msg.value + RequestParams.pn_req_header,
                    resMap.get(RequestParams.pn_req_header));
            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + msg.value + RequestParams.pn_req_attr,
                    resMap.get(RequestParams.pn_req_attr));

            TestCase.assertEquals(
                    msg.normal_transferCipherNoReqBody + ZKJsonUtils.toJsonStr(
                            new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs }),
                    resMap.get(RequestParams.pn_strs));
            TestCase.assertEquals(intValue * intValue, resMap.get(RequestParams.pn_int));
            log.info("[^_^:20190625-1121-001] === put 进行加解密请求处理成功 ------------- ");

            /*** get application/x-www-form-urlencoded ***/
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            // 添加请求头
            mockReqBuilder.header(RequestParams.pn_req_header, msg.value + RequestParams.pn_req_header);
            // 添加请求属性
            mockReqBuilder.requestAttr(RequestParams.pn_req_attr, msg.value + RequestParams.pn_req_attr);
            // 添加请求参数，数组
            paramMap.put(RequestParams.pn_strs,
                    new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs });
            paramMap.put(RequestParams.pn_int, intValue);
            encParameterStr = ZKJsonUtils.toJsonStr(paramMap);
            log.info("[^_^:20190626-1632-002-0] 加密前 参数：" + encParameterStr);
            encParameterStr = ZKEncodingUtils
                    .encodeHex((sampleRsaAesTc.encryptData(dataKey, salt, encParameterStr.getBytes("utf-8"))));
            log.info("[^_^:20190626-1632-002-1] 加密后 参数：" + encParameterStr);
            mockReqBuilder.param(ZKRSA_ReqHeader.pn_parameter, encParameterStr);

            // 随意添加一个 rsa ID，测试的传输加解密中是硬编码的一个证书
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_id, "KKK");
            // 添加 rsa 公钥加密后的 数据加密密钥
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_key, encDataKeyStr);
            mockReqBuilder.contentType(ZKContentType.X_FORM.getContentType());
            // 发起请求
            mvcResult = this.mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print()).andReturn();
            mockRes = mvcResult.getResponse();
            resEncData = mockRes.getContentAsByteArray();
            log.info("[^_^:20190625-1121-002-0] 解密前 响应数据:" + new String(resEncData));
            resStr = new String(
                    ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(new String(resEncData)), dataKey, salt));
            log.info("[^_^:20190625-1121-002-1] 解密后 响应数据： " + resStr);

            resMap = ZKJsonUtils.parseMap(resStr);

            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + RequestParams.pn_pathVariable1,
                    resMap.get(RequestParams.pn_pathVariable1));
            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + RequestParams.pn_pathVariable2,
                    resMap.get(RequestParams.pn_pathVariable2));

            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + msg.value + RequestParams.pn_req_header,
                    resMap.get(RequestParams.pn_req_header));
            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + msg.value + RequestParams.pn_req_attr,
                    resMap.get(RequestParams.pn_req_attr));

            TestCase.assertEquals(
                    msg.normal_transferCipherNoReqBody + ZKJsonUtils.toJsonStr(
                            new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs }),
                    resMap.get(RequestParams.pn_strs));
            TestCase.assertEquals(intValue * intValue, resMap.get(RequestParams.pn_int));
            log.info("[^_^:20190625-1121-001] === put 进行加解密请求处理成功 ------------- ");

            /*** post application/x-www-form-urlencoded ***/
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            // 添加请求头
            mockReqBuilder.header(RequestParams.pn_req_header, msg.value + RequestParams.pn_req_header);
            // 添加请求属性
            mockReqBuilder.requestAttr(RequestParams.pn_req_attr, msg.value + RequestParams.pn_req_attr);
            // 添加请求参数，数组
            paramMap.put(RequestParams.pn_strs,
                    new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs });
            paramMap.put(RequestParams.pn_int, intValue);
            encParameterStr = ZKJsonUtils.toJsonStr(paramMap);
            log.info("[^_^:20190626-1632-002-0] 加密前 参数：" + encParameterStr);
            encParameterStr = ZKEncodingUtils
                    .encodeHex((sampleRsaAesTc.encryptData(dataKey, salt, encParameterStr.getBytes())));
            log.info("[^_^:20190626-1632-002-1] 加密后 参数：" + encParameterStr);
            mockReqBuilder.param(ZKRSA_ReqHeader.pn_parameter, encParameterStr);

            // 随意添加一个 rsa ID，测试的传输加解密中是硬编码的一个证书
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_id, "KKK");
            // 添加 rsa 公钥加密后的 数据加密密钥
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_key, encDataKeyStr);
            mockReqBuilder.contentType(ZKContentType.X_FORM.getContentType());
            // 发起请求
            mvcResult = this.mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print()).andReturn();
            mockRes = mvcResult.getResponse();
            resEncData = mockRes.getContentAsByteArray();
            log.info("[^_^:20190625-1121-002-0] 解密前 响应数据:" + new String(resEncData));
            resStr = new String(
                    ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(new String(resEncData)), dataKey, salt));
            log.info("[^_^:20190625-1121-002-1] 解密后 响应数据： " + resStr);

            resMap = ZKJsonUtils.parseMap(resStr);

            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + RequestParams.pn_pathVariable1,
                    resMap.get(RequestParams.pn_pathVariable1));
            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + RequestParams.pn_pathVariable2,
                    resMap.get(RequestParams.pn_pathVariable2));

            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + msg.value + RequestParams.pn_req_header,
                    resMap.get(RequestParams.pn_req_header));
            TestCase.assertEquals(msg.normal_transferCipherNoReqBody + msg.value + RequestParams.pn_req_attr,
                    resMap.get(RequestParams.pn_req_attr));

            TestCase.assertEquals(
                    msg.normal_transferCipherNoReqBody + ZKJsonUtils.toJsonStr(
                            new String[] { msg.value + RequestParams.pn_strs, msg.value + RequestParams.pn_strs }),
                    resMap.get(RequestParams.pn_strs));
            TestCase.assertEquals(intValue * intValue, resMap.get(RequestParams.pn_int));
            log.info("[^_^:20190625-1121-001] === post application/x-www-form-urlencoded 进行加解密请求处理成功 ------------- ");

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }

    }

    @Test
    public void testTransferCipherNo() {
        try {

            MockHttpServletRequestBuilder mockReqBuilder = null;
            MvcResult mvcResult = null;
            MockHttpServletResponse mockRes = null;
            String resStr = null;
            byte[] resEncData = null;

            String url = String.format("%s/transferCipherNo", this.baseUrl);

            ZKSampleRsaAesTransferCipherManager sampleRsaAesTc = (ZKSampleRsaAesTransferCipherManager) this.transferCipherManager;

            /*** 不进行加解密 ***/
            mockReqBuilder = MockMvcRequestBuilders.get(url);

            // 发起请求
            mvcResult = this.mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk())
//                    .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            mockRes = mvcResult.getResponse();
            resStr = mockRes.getContentAsString();
            log.info("[^_^:20190625-1121-001] " + resStr);
            log.info("[^_^:20190625-1121-001] === 不进行加解密请求处理成功 ------------- ");

            /*** 需要进行加解密 ***/
            byte[] publicKey = sampleRsaAesTc.getZKRSAKeyByRsaId(null).getPublicKey();
            byte[] dataKey = "疑行无名，疑事无功".getBytes();
            byte[] salt = ZKEncryptUtils.genSalt(dataKey);

            log.info("[^_^:20190626-1632-001-0] 加密前 密钥：" + new String(dataKey));
            String encDataKeyStr = ZKEncodingUtils.encodeHex(ZKEncryptRsaUtils.encrypt(dataKey, publicKey));
            log.info("[^_^:20190626-1632-001-1] 加密后 密钥：" + encDataKeyStr);

            /*** get ***/
            mockReqBuilder = MockMvcRequestBuilders.get(url);
            // 随意添加一个 rsa ID，测试的传输加解密中是硬编码的一个证书
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_id, "KKK");
            // 添加 rsa 公钥加密后的 数据加密密钥
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_key, encDataKeyStr);
            // 发起请求
            mvcResult = this.mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk())
//                  .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            mockRes = mvcResult.getResponse();
            resEncData = mockRes.getContentAsByteArray();
            log.info("[^_^:20190625-1121-002-0] 解密前 响应数据:" + new String(resEncData));
            resStr = new String(
                    ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(new String(resEncData)), dataKey, salt));
            log.info("[^_^:20190625-1121-002-1] 解密后 响应数据： " + resStr);
            log.info("[^_^:20190625-1121-001] === get 进行加解密请求处理成功 ------------- ");

            /*** post ***/
            mockReqBuilder = MockMvcRequestBuilders.post(url);
            // 随意添加一个 rsa ID，测试的传输加解密中是硬编码的一个证书
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_id, "KKK");
            // 添加 rsa 公钥加密后的 数据加密密钥
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_key, encDataKeyStr);
            // 发起请求
            mvcResult = this.mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk())
//                  .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            mockRes = mvcResult.getResponse();
            resEncData = mockRes.getContentAsByteArray();
            log.info("[^_^:20190625-1121-002-0] 解密前 响应数据:" + new String(resEncData));
            resStr = new String(
                    ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(new String(resEncData)), dataKey, salt));
            log.info("[^_^:20190625-1121-002-1] 解密后 响应数据： " + resStr);
            log.info("[^_^:20190625-1121-001] === post 进行加解密请求处理成功 ------------- ");

            /*** delete ***/
            mockReqBuilder = MockMvcRequestBuilders.delete(url);
            // 随意添加一个 rsa ID，测试的传输加解密中是硬编码的一个证书
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_id, "KKK");
            // 添加 rsa 公钥加密后的 数据加密密钥
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_key, encDataKeyStr);
            // 发起请求
            mvcResult = this.mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk())
//                  .andDo(MockMvcResultHandlers.print())
                    .andReturn();
            mockRes = mvcResult.getResponse();
            resEncData = mockRes.getContentAsByteArray();
            log.info("[^_^:20190625-1121-002-0] 解密前 响应数据:" + new String(resEncData));
            resStr = new String(
                    ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(new String(resEncData)), dataKey, salt));
            log.info("[^_^:20190625-1121-002-1] 解密后 响应数据： " + resStr);
            log.info("[^_^:20190625-1121-001] === delete 进行加解密请求处理成功 ------------- ");

            /*** put ***/
            mockReqBuilder = MockMvcRequestBuilders.put(url);
            // 随意添加一个 rsa ID，测试的传输加解密中是硬编码的一个证书
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_id, "KKK");
            // 添加 rsa 公钥加密后的 数据加密密钥
            mockReqBuilder.header(ZKRSA_ReqHeader.pn_key, encDataKeyStr);
            // 发起请求
            mvcResult = this.mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print()).andReturn();
            mockRes = mvcResult.getResponse();
            resEncData = mockRes.getContentAsByteArray();
            log.info("[^_^:20190625-1121-002-0] 解密前 响应数据:" + new String(resEncData));
            resStr = new String(
                    ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(new String(resEncData)), dataKey, salt));
            log.info("[^_^:20190625-1121-002-1] 解密后 响应数据： " + resStr);
            log.info("[^_^:20190625-1121-001] === put 进行加解密请求处理成功 ------------- ");

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }

    }

    /*** 测试方法模版 ***/
    @Test
    public void test() {
        try {

//            MockHttpServletRequestBuilder mockReqBuilder = null;
//            MvcResult mvcResult = null;
//            MockHttpServletResponse mockRes = null;
//            String resStr = null;
//
//            String url = "";
//
//            mockReqBuilder = MockMvcRequestBuilders.get(url);
//
//            mvcResult = this.mvc.perform(mockReqBuilder).andExpect(MockMvcResultMatchers.status().isOk())
////                    .andDo(MockMvcResultHandlers.print())
//                    .andReturn();
//            mockRes = mvcResult.getResponse();
//            resStr = mockRes.getContentAsString();
//            log.info("[^_^:20190320-0856-001] " + resStr);
////            myResMsg = JsonUtils.jsonStrToObject(resStr, MyResMsg.class);
////            TestCase.assertEquals("0", myResMsg.getErrCode());
//            TestCase.assertNotNull(resStr);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }

    }

}
