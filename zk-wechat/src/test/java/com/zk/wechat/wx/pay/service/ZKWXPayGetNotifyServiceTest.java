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
* @Title: ZKWXPayGetNotifyServiceTest.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 1, 2021 6:57:32 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay.service;
/** 
* @ClassName: ZKWXPayGetNotifyServiceTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/

import org.junit.Test;

import com.zk.wechat.helper.ZKWechatTestHelper;
import com.zk.wechat.pay.entity.ZKPayGetNotify;
import com.zk.wechat.pay.enumType.ZKPayStatus;
import com.zk.wechat.pay.service.ZKPayGetOrderService;

import junit.framework.TestCase;

public class ZKWXPayGetNotifyServiceTest {

    // 测试通知回调
    @Test
    public void testPayNotify() {
        try {
            ZKWXPayGetNotifyService s = ZKWechatTestHelper.getCtx().getBean(ZKWXPayGetNotifyService.class);
            TestCase.assertNotNull(s);

            ZKPayGetOrderService ps = ZKWechatTestHelper.getCtx().getBean(ZKPayGetOrderService.class);
            TestCase.assertNotNull(ps);

            String payGetOrderId, wechatpaySignature, wechatpayTimestamp, wechatpayNonce, wechatpaySerial, bodyStr;

            payGetOrderId = "5465329038476182016";
            wechatpaySignature = "pZgbFxQARNqcUO1HD7qrpcY8T2TVUVwdeyZQw46jyDkvTeiJ+a+nfzQMkthB0u8YxXKCZR4dxa7C7NiZ6csQZRoGnvotEEPcylJn00hzuyTytx4vE5iRBIgVtSm3UmHm2uYsdmCkINd679jl38znpA/rZ/QU+iPcRye5uHNyIYBCdE9s8sWTkbGbPdRMxwIT21VUOK0+5Eo0GwxmvFiMFAMrJO9qby2/Dj+saa52GG/AHWAQOdkC4vHHlComvatMYuR350qn9lxnUVxfYgYDCcK0tXx+e8ldMmfr2wr5bT91vWUEWJF0Lz6qnItlt0pftEbAvI+b18mZ85w//+NvvA==";
            wechatpayTimestamp = "1614594008";
            wechatpayNonce = "iLtWOhU3aDxRtaeTwQL6x90x6Nor75hI";
            wechatpaySerial = "5FDD8A552116CE0D35A6ADC6EA19DBB8FB748507";
            bodyStr = "{\"id\":\"dbdfcc8e-80db-5d9a-ae33-4bbdbc6790b1\",\"create_time\":\"2021-03-01T18:19:46+08:00\",\"resource_type\":\"encrypt-resource\",\"event_type\":\"TRANSACTION.SUCCESS\",\"summary\":\"支付成功\",\"resource\":{\"original_type\":\"transaction\",\"algorithm\":\"AEAD_AES_256_GCM\",\"ciphertext\":\"CvYa64Mnq6V1UEpmu5riOZ6WI2+SkDMJbOTg8Jny3caB20OCVAS5uTnT+BYJ0rcf/aW7xCMQcx9LPEna1VeOuQPBE0IHYCEM7jUcx09+7t3Lgt/VRGZJwH+4tp0FrcakPiw1Lu9yLDLpWp2//G1ipaMuLMTjZfxdO1mB4iHQG+HTgHvQTnSGqPyyGuizsD9hmtF69pijKKwwYXYyQWroUKQ/P8VKoeT5alZ6LiXdEDpghN7Df5OvYobwmffxI4AYiUaQ+9DqzhlNWbJyX3hOWU/ZlVlowlqvra/qEh0kJKkkkcnsdaWdbG52476S08EewdJByJH3rQCX458kq2RuwPtGIiWWor3/A47EK2aM4NbsS3e46JdoJEHjommEsVfQfJ05vA/9XZ39UCJP/PUO3aj9Dz63RAUE+ZC2poOZSY14SRmcqadz2Za7c0jBtosyoCc8Gk/aqlGVa288Y97ACpV0mjhVgsn6MQAmdkfpWTyaCfAVisIhE46dOUWr1R7d+/6iJxrWLWl6sFnqnZFyu+RaZPRVwf+ERVyWFfbkn2fqOpez6Ddy4fmfsyEDsLAB44Q9ZkDT\",\"associated_data\":\"transaction\",\"nonce\":\"WORf7Tvuf1Cf\"}}";

            ZKPayGetNotify res = null;

            // 统一下单
            ps.updatePayStatus(payGetOrderId, ZKPayStatus.NOTPAY);
            res = s.payNotify(payGetOrderId, wechatpaySerial, wechatpaySignature, wechatpayTimestamp, wechatpayNonce,
                    bodyStr);
            TestCase.assertNotNull(res);
            TestCase.assertEquals(ZKPayGetNotify.DisposeStatus.succees, res.getDisposeStatus().intValue());

            ps.updatePayStatus(payGetOrderId, ZKPayStatus.NOTPAY);
            res = s.payNotify(payGetOrderId, null, wechatpaySignature, wechatpayTimestamp, wechatpayNonce, bodyStr);
            TestCase.assertNotNull(res);
            TestCase.assertEquals(ZKPayGetNotify.DisposeStatus.succees, res.getDisposeStatus().intValue());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
