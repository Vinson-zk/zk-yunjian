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
* @Title: ZKWXPayJsApiServiceTest.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 23, 2021 5:35:14 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay.service;

import org.junit.Test;

import com.zk.core.exception.ZKCodeException;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.wechat.helper.ZKWechatTestHelper;
import com.zk.wechat.pay.entity.ZKPayGetOrder;
import com.zk.wechat.pay.service.ZKPayGetOrderService;
import com.zk.wechat.wx.pay.entity.ZKWXJsPay;

import junit.framework.TestCase;

/** 
* @ClassName: ZKWXPayJsApiServiceTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWXPayJsApiServiceTest {

    // 统一下单
    @Test
    public void testPutOrder() {
        try {
            ZKWXPayJsApiService s = ZKWechatTestHelper.getCtx().getBean(ZKWXPayJsApiService.class);
            TestCase.assertNotNull(s);

//            String mchid = "1606675880";
            String payGroupCode = "gfafl";
            String businessCode = "gc";
            String businessNo = "3";
            String description = "垃圾上门回收";
            String attach = null;
            String goodsTag = null;
            String openid = "oT9Kv4jwXL_641ukjvJvbxw_9o7M";
            int amountTotal = 93;
            String amountCurrency = "CNY";

            ZKPayGetOrder payGetOrder = null;
            try {
                // 统一下单
                payGetOrder = s.jsapi(payGroupCode, businessCode, businessNo, description, attach,
                        goodsTag, openid, amountTotal, amountCurrency);
                TestCase.assertNotNull(payGetOrder);
            }
            catch(ZKCodeException e) {
                TestCase.assertEquals("wx.400.APPID_MCHID_NOT_MATCH", e.getCode());
            }

            // 生成调起支付
            ZKWXJsPay jsPay = s.genJsApiPayParams(payGetOrder);
            System.out.println("[^_^:20210223-1744-001] jsPay: " + ZKJsonUtils.writeObjectJson(jsPay));
            TestCase.assertNotNull(jsPay);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 重新支付
    @Test
    public void testPutOrderAgain() {
        try {
            ZKWXPayJsApiService s = ZKWechatTestHelper.getCtx().getBean(ZKWXPayJsApiService.class);
            TestCase.assertNotNull(s);

            ZKPayGetOrderService ss = ZKWechatTestHelper.getCtx().getBean(ZKPayGetOrderService.class);
            TestCase.assertNotNull(s);

            ZKPayGetOrder pgo = null;
            String businessCode = "gc";
            String businessNo = "3";
            pgo = ss.getByBusiness(businessCode, businessNo);
            String orderId = pgo.getPkId();

            // 统一下单
            ZKPayGetOrder payGetOrder = s.jsapi(orderId);
            TestCase.assertNotNull(payGetOrder);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 关闭订单
    @Test
    public void testClose() {
        try {
            ZKWXPayJsApiService s = ZKWechatTestHelper.getCtx().getBean(ZKWXPayJsApiService.class);
            TestCase.assertNotNull(s);

            String orderId = "5464954058554999296";

            // 统一下单
            ZKPayGetOrder payGetOrder = s.close(orderId);
            TestCase.assertNotNull(payGetOrder);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
