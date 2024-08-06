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
 * @Title: ZKDemoTransferCipherMain.java 
 * @author Vinson 
 * @Package com.zk.demo.transfer.sample 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 5:04:09 PM 
 * @version V1.0   
*/
package com.zk.demo.web.transfer.sample;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import com.zk.core.encrypt.utils.ZKEncryptAesUtils;
import com.zk.core.encrypt.utils.ZKEncryptRsaUtils;
import com.zk.core.encrypt.utils.ZKEncryptUtils;
import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKParamsUtils;
import com.zk.core.utils.ZKStreamUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.encrypt.ZKSampleRsaAesTransferCipherManager;
import com.zk.core.web.utils.ZKHttpApiUtils;
import com.zk.demo.web.commons.ZKDemoConstants;
import com.zk.demo.web.entity.ZKDemoEntity;

/** 
* @ClassName: ZKDemoTransferCipherMain 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDemoTransferCipherMain {

    private static Logger log = LogManager.getLogger(ZKDemoTransferCipherMain.class);

    private static final String baseUrl = "http://127.0.0.1:8080/zk/demo/tc";

    private interface RequestParams extends ZKDemoConstants.RequestParams {
    }

    private static byte[] publicKey;

    public static void main(String[] args) {
        try {
            System.out.println("===== SampleMain begin =============== ");
            System.out.println("===== SampleMain begin =============== ");
            System.out.println("===== SampleMain begin =============== ");

            System.out.println("------------- PublicKey ");
            System.out.println("------------- PublicKey ");
            testPublicKey();

            System.out.println("------------- testIndex ");
            System.out.println("------------- testIndex ");
            testIndex();

            System.out.println("------------- testNoReqBody ");
            System.out.println("------------- testNoReqBody ");
            testNoReqBody();

            System.out.println("------------- testNo 无响应 ");
            System.out.println("------------- testNo 无响应 ");
            testNo();

            System.out.println("===== SampleMain over =============== ");
            System.out.println("===== SampleMain over =============== ");
            System.out.println("===== SampleMain over =============== ");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    static interface ZKRSA_ReqHeader extends ZKSampleRsaAesTransferCipherManager.ZKRSA_ReqHeader {
    }

    // 取 rsa 公钥
    public static void testPublicKey() {

        byte[] bytes = null;
        ByteArrayOutputStream byteOs = new ByteArrayOutputStream();
        int statusCode = -1;
        String url = String.format("%s/rsa", baseUrl);
        statusCode = ZKHttpApiUtils.get(url, null, byteOs, null);

        bytes = byteOs.toByteArray();
        log.info("[^_^:20190629-1628-001] statusCode:" + statusCode);
        log.info("[^_^:20190629-1628-001] rsa:" + ZKEncodingUtils.encodeHex(bytes));

        publicKey = ZKEncodingUtils.decodeHex(new String(bytes));
        ZKStreamUtils.closeStream(byteOs);

    }

    // post delete put 有请求参数与请求体；路径参数，请求头等接口的 加解密 demo
    public static void testIndex() throws Exception {

        int statusCode = -1;
        ByteArrayOutputStream byteOs = null;
        String resStr = null;
        byte[] bytes = null;

        String url = String.format("%s/index/%s/%s", baseUrl, RequestParams.pn_pathVariable1,
                RequestParams.pn_pathVariable2);

        String[] pnStrs = new String[] { RequestParams.pn_req_attr, RequestParams.pn_req_attr };
        Integer pnInt = 3;

        Map<String, Object> parameterMap = new HashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        ZKDemoEntity demoEntity = new ZKDemoEntity();

        /*** 不进行加解密 ***/
        parameterMap.put(RequestParams.pn_strs, pnStrs);
        parameterMap.put(RequestParams.pn_int, pnInt.toString());

        headerMap.put(RequestParams.pn_req_header, RequestParams.pn_req_header);

        demoEntity.setName("name");
        demoEntity.setAge(2);
        ZKDemoEntity[] ds = new ZKDemoEntity[] { new ZKDemoEntity("ns_1", 2), new ZKDemoEntity("ns_1", 2) };
        demoEntity.setDemoEntityArray(ds);
        demoEntity.setDemoEntityList(Arrays.asList(ds));

        demoEntity.setDemoEntityMap(new HashMap<>());
        demoEntity.getDemoEntityMap().put("m1", new ZKDemoEntity("n_map_1", 2));
        demoEntity.getDemoEntityMap().put("m2", new ZKDemoEntity("n_map_2", 3));
        demoEntity.getDemoEntityMap().put("m3", new ZKDemoEntity("n_map_3", 4));

        byteOs = new ByteArrayOutputStream();
        statusCode = ZKHttpApiUtils.postJson(url + "?" + ZKParamsUtils.convertStringParamter(parameterMap),
                ZKJsonUtils.toJsonStr(demoEntity), null, headerMap, byteOs, null);
        resStr = new String(byteOs.toByteArray());
        log.info("[^_^:20190629-1121-001] statusCode:" + statusCode);
        log.info("[^_^:20190629-1121-001] resStr:" + resStr);
        ZKStreamUtils.closeStream(byteOs);

        /*** 进行加解密 ***/
        String parameterStr = ZKJsonUtils.toJsonStr(parameterMap);
        String demoEntityStr = ZKJsonUtils.toJsonStr(demoEntity);
        byte[] dataKey = "故知者作法，而愚者制焉；贤者更礼，而不肖者拘焉".getBytes();
        byte[] salt = ZKEncryptUtils.genSalt(dataKey);

        String encParameterStr = null;
        String encReqBodyStr = null;

        log.info("[^_^:20190629-1121-002] demo rsa publicKey:" + ZKEncodingUtils.encodeHex(publicKey));
        // 用 rsa public key; 加密数据加密密钥
        String encDataKeyStr = ZKEncodingUtils.encodeHex(ZKEncryptRsaUtils.encrypt(dataKey, publicKey));

        // 添加加解密相关信息 rsa 数据加解密 key; 随意添加一个 rsa ID，Demo 中传输加解密中是硬编码的一个证书
        headerMap.put(ZKRSA_ReqHeader.pn_id, "rsa");
        // 添加 rsa 公钥加密后的 数据加密密钥
        headerMap.put(ZKRSA_ReqHeader.pn_key, encDataKeyStr);

        log.info("[^_^:20190701-1015-001] 参数 加密前：" + parameterStr);
        // 用数据加密密钥加密参数
        encParameterStr = ZKEncodingUtils
                .encodeHex(ZKEncryptAesUtils.encrypt(parameterStr.getBytes("utf-8"), dataKey, salt));
        log.info("[^_^:20190701-1015-002] 参数 加密后：" + encParameterStr);
        log.info("[^_^:20190701-1016-001] body 加密前：" + demoEntityStr);
        // 用数据加密密钥加密请求体数据
        encReqBodyStr = ZKEncodingUtils.encodeHex(ZKEncryptAesUtils.encrypt(demoEntityStr.getBytes(), dataKey, salt));
        log.info("[^_^:20190701-1016-002] body 加密后：" + encReqBodyStr);
        byteOs = new ByteArrayOutputStream();
        statusCode = ZKHttpApiUtils.postJson(
                String.format("%s?%s=%s", url, ZKRSA_ReqHeader.pn_parameter, encParameterStr), encReqBodyStr, null,
                headerMap, byteOs, null);

        bytes = byteOs.toByteArray();
        ZKStreamUtils.closeStream(byteOs);
        resStr = new String(bytes);
        log.info("[^_^:20190701-1022-001] 响应数据 未解密：" + resStr);
        resStr = new String(ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(new String(bytes)), dataKey, salt));
        log.info("[^_^:20190701-1022-002] 响应数据 解密：" + resStr);

    }

    // get post delete pub 无请求参数也无请求体；路径参数，请求头等接口的 加解密 demo
    public static void testNoReqBody() throws Exception {

        int statusCode = -1;
        ByteArrayOutputStream byteOs = null;
        String resStr = null;
        byte[] bytes = null;

        String url = String.format("%s/noReqBody/%s/%s", baseUrl, RequestParams.pn_pathVariable1,
                RequestParams.pn_pathVariable2);

        String[] pnStrs = new String[] { RequestParams.pn_req_attr, RequestParams.pn_req_attr };
        Integer pnInt = 3;

        Map<String, Object> parameterMap = new HashMap<>();
        Map<String, String> headerMap = new HashMap<>();

        /*** 不进行加解密 ***/
        parameterMap.put(RequestParams.pn_strs, pnStrs);
        parameterMap.put(RequestParams.pn_int, pnInt.toString());

        headerMap.put(RequestParams.pn_req_header, RequestParams.pn_req_header);

        byteOs = new ByteArrayOutputStream();
        statusCode = ZKHttpApiUtils.get(url + "?" + ZKParamsUtils.convertStringParamter(parameterMap), headerMap,
                byteOs, null);

        bytes = byteOs.toByteArray();
        ZKStreamUtils.closeStream(byteOs);
        resStr = new String(bytes);

        log.info("[^_^:20190629-1121-001] statusCode:" + statusCode);
        log.info("[^_^:20190629-1121-001] resStr:" + resStr);

        /*** 进行加解密 ***/
        String parameterStr = ZKJsonUtils.toJsonStr(parameterMap);
        byte[] dataKey = "故知者作法，而愚者制焉；贤者更礼，而不肖者拘焉".getBytes();
        byte[] salt = ZKEncryptUtils.genSalt(dataKey);

        String encParameterStr = null;

        log.info("[^_^:20190629-1121-002] demo rsa publicKey:" + ZKEncodingUtils.encodeHex(publicKey));
        // 用 rsa public key; 加密数据加密密钥
        String encDataKeyStr = ZKEncodingUtils.encodeHex(ZKEncryptRsaUtils.encrypt(dataKey, publicKey));

        // 添加加解密相关信息 rsa 数据加解密 key; 随意添加一个 rsa ID，Demo 中传输加解密中是硬编码的一个证书
        headerMap.put(ZKRSA_ReqHeader.pn_id, "rsa");
        // 添加 rsa 公钥加密后的 数据加密密钥
        headerMap.put(ZKRSA_ReqHeader.pn_key, encDataKeyStr);

        log.info("[^_^:20190701-1015-001] 参数 加密前：" + parameterStr);
        // 用数据加密密钥加密参数
        encParameterStr = ZKEncodingUtils
                .encodeHex(ZKEncryptAesUtils.encrypt(parameterStr.getBytes("utf-8"), dataKey, salt));
        log.info("[^_^:20190701-1015-002] 参数 加密后：" + encParameterStr);
        byteOs = new ByteArrayOutputStream();
        statusCode = ZKHttpApiUtils.get(String.format("%s?%s=%s", url, ZKRSA_ReqHeader.pn_parameter, encParameterStr),
                headerMap, byteOs, null);

        bytes = byteOs.toByteArray();
        ZKStreamUtils.closeStream(byteOs);
        resStr = new String(bytes);
        log.info("[^_^:20190701-1022-001] 响应数据 未解密：" + resStr);
        resStr = new String(ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(new String(bytes)), dataKey, salt));
        log.info("[^_^:20190701-1022-002] 响应数据 解密：" + resStr);

        byteOs = new ByteArrayOutputStream();
        statusCode = ZKHttpApiUtils.post(String.format("%s?%s=%s", url, ZKRSA_ReqHeader.pn_parameter, encParameterStr),
                (String) null, null, null, headerMap, null, byteOs, null);

        bytes = byteOs.toByteArray();
        ZKStreamUtils.closeStream(byteOs);
        resStr = new String(bytes);
        log.info("[^_^:20190701-1022-001] 响应数据 未解密：" + resStr);
        resStr = new String(ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(new String(bytes)), dataKey, salt));
        log.info("[^_^:20190701-1022-002] 响应数据 解密：" + resStr);

    }

    // get post delete pub 无请求参数也无请求体；路径参数，请求头等接口的 加解密 demo
    public static void testNo() throws Exception {

        int statusCode = -1;
        ByteArrayOutputStream byteOs = null;
        String resStr = null;
        byte[] bytes = null;

        Map<String, String> headerMap = new HashMap<>();

        String url = String.format("%s/no", baseUrl);

        /*** 不进行加解密 ***/
        byteOs = new ByteArrayOutputStream();
        statusCode = ZKHttpApiUtils.get(url, null, byteOs, null);

        bytes = byteOs.toByteArray();
        ZKStreamUtils.closeStream(byteOs);
        resStr = new String(bytes);

        log.info("[^_^:20190629-1121-001] statusCode:" + statusCode);
        log.info("[^_^:20190629-1121-001] resStr:" + resStr);

        /*** 进行加解密 ***/
        byte[] dataKey = "故知者作法，而愚者制焉；贤者更礼，而不肖者拘焉".getBytes();
        byte[] salt = ZKEncryptUtils.genSalt(dataKey);

        log.info("[^_^:20190629-1121-002] demo rsa publicKey:" + ZKEncodingUtils.encodeHex(publicKey));
        // 用 rsa public key; 加密数据加密密钥
        String encDataKeyStr = ZKEncodingUtils.encodeHex(ZKEncryptRsaUtils.encrypt(dataKey, publicKey));

        // 添加加解密相关信息 rsa 数据加解密 key; 随意添加一个 rsa ID，Demo 中传输加解密中是硬编码的一个证书
        headerMap.put(ZKRSA_ReqHeader.pn_id, "rsa");
        // 添加 rsa 公钥加密后的 数据加密密钥
        headerMap.put(ZKRSA_ReqHeader.pn_key, encDataKeyStr);

        byteOs = new ByteArrayOutputStream();
        statusCode = ZKHttpApiUtils.get(url, headerMap, byteOs, null);

        bytes = byteOs.toByteArray();
        ZKStreamUtils.closeStream(byteOs);
        resStr = new String(bytes);
//        Assert.isTrue(ZKStringUtils.isEmpty(resStr), "期望结果为空字符串，但实际是：" + resStr);
        log.info("[^_^:20190701-1022-001] 响应数据 未解密：" + resStr);
        resStr = new String(ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(new String(bytes)), dataKey, salt));
        Assert.isTrue(ZKStringUtils.isEmpty(resStr), "期望结果为空字符串，但实际是：" + resStr);
        log.info("[^_^:20190701-1022-002] 响应数据 解密：" + resStr);

    }

}
