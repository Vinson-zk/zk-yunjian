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
* @Title: DataInitTest.java 
* @author Vinson 
* @Package com.zk.wechat.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 22, 2021 9:28:04 PM 
* @version V1.0 
*/
package com.zk.wechat.pay.service;

import java.util.Date;

import org.junit.Test;

import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.wechat.helper.ZKWechatTestHelper;
import com.zk.wechat.pay.entity.ZKPayGetBusinessType;
import com.zk.wechat.pay.entity.ZKPayGroup;
import com.zk.wechat.pay.entity.ZKPayMerchant;

import junit.framework.TestCase;

/** 
* @ClassName: DataInitTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class DataInitTest {

    // 创建 商户号
    @Test
    public void testInitMerchant() {
        ZKPayMerchantService s = ZKWechatTestHelper.getCtx().getBean(ZKPayMerchantService.class);
        TestCase.assertNotNull(s);
        try {

            ZKPayMerchant zkMerchant = null;
            int intRes = 0;
            String merchantId, apiv3AesKey, apiKey, apiCertSerialNo, apiCertPathPkcs12, apiCertPathPem,
                    apiCertPathKeyPem;
            Date apiCertExpirationTime;


            zkMerchant = new ZKPayMerchant();
            merchantId = "1606675880";
            apiv3AesKey = "2a5bb7eb1a784e339b1fc248f7650586";
            apiKey = "c8417b30fe294565a8caa57d7b053c1a";
            apiCertPathPkcs12 = "1606675880_20210222_cert/apiclient_cert.p12";
            apiCertPathPem = "1606675880_20210222_cert/apiclient_cert.pem";
            apiCertPathKeyPem = "1606675880_20210222_cert/apiclient_key.pem";
            apiCertSerialNo = "49D449B97E493D95D6A11FED5B6B1715746CB6B0";
            apiCertExpirationTime = ZKDateUtils.parseDate("2026-02-22", ZKDateUtils.DF_yyyy_MM_dd);
            // /Users/bs/bs_temp/1606675880_20210222_cert/apiclient_cert.pem
            zkMerchant.setNewRecord(true);
            zkMerchant.setPkId(merchantId);
            zkMerchant.setApiv3AesKey(apiv3AesKey);
            zkMerchant.setApiKey(apiKey);
            zkMerchant.setApiCertPathPkcs12(apiCertPathPkcs12);
            zkMerchant.setApiCertPathPem(apiCertPathPem);
            zkMerchant.setApiCertPathKeyPem(apiCertPathKeyPem);
            zkMerchant.setApiCertUpdateDate(new Date());
            zkMerchant.setApiCertEffectiveTime(new Date());
            zkMerchant.setApiCertSerialNo(apiCertSerialNo);
            zkMerchant.setApiCertExpirationTime(apiCertExpirationTime);
            zkMerchant.setStatus(ZKPayMerchant.Status.disabled);

            intRes = s.save(zkMerchant);
            TestCase.assertEquals(1, intRes);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 初始化 支付业务类型
    @Test
    public void testInitBusiness() {
        ZKPayGetBusinessTypeService s = ZKWechatTestHelper.getCtx().getBean(ZKPayGetBusinessTypeService.class);
        TestCase.assertNotNull(s);
        try {

            ZKPayGetBusinessType zkPayGetBusinessType = null;
            int intRes = 0;
            zkPayGetBusinessType = new ZKPayGetBusinessType();
            zkPayGetBusinessType.setCode("gc"); // 垃圾分类回收
            zkPayGetBusinessType.setName(new ZKJson());
            zkPayGetBusinessType.getName().put(ZKLocaleUtils.getLocale().toString(), "垃圾分类回收");
            zkPayGetBusinessType.setStatus(ZKPayGetBusinessType.Status.enabled);
            intRes = s.save(zkPayGetBusinessType);
            TestCase.assertEquals(1, intRes);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 创建 支付关联组
    @Test
    public void testInitPayGroup() {
        ZKPayGroupService s = ZKWechatTestHelper.getCtx().getBean(ZKPayGroupService.class);
        TestCase.assertNotNull(s);
        try {

            ZKPayGroup payGroup = null;
            int intRes = 0;
            payGroup = new ZKPayGroup();
            payGroup.setWxMchid("1606675880");
            payGroup.setWxAppId("wx2947219f7cffd21e");
            payGroup.setCode("gfafl"); // 垃圾分类回收
            payGroup.setName(new ZKJson());
            payGroup.getName().put(ZKLocaleUtils.getLocale().toString(), "垃圾分类回收");
            payGroup.setStatus(ZKPayGroup.Status.enabled);
            intRes = s.save(payGroup);
            TestCase.assertEquals(1, intRes);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
