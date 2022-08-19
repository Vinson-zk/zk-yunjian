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
* @Title: ZKPayGetNotifyServiceTest.java 
* @author Vinson 
* @Package com.zk.wechat.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 22, 2021 4:33:22 PM 
* @version V1.0 
*/
package com.zk.wechat.pay.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.wechat.helper.ZKWechatTestHelper;
import com.zk.wechat.pay.entity.ZKPayGetNotify;

import junit.framework.TestCase;

/** 
* @ClassName: ZKPayGetNotifyServiceTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKPayGetNotifyServiceTest {

    @Test
    public void testCreate() {

        ZKPayGetNotifyService s = ZKWechatTestHelper.getCtx().getBean(ZKPayGetNotifyService.class);
        List<ZKPayGetNotify> dels = new ArrayList<>();
        try {

            ZKPayGetNotify payGetNotify = null;

            payGetNotify = s.create("123", "t-s-no", "t-wechatpaySignature", "t-wechatpayTimestamp",
                    "t-wechatpayNonce", "t-wxBody");
            dels.add(payGetNotify);
            TestCase.assertNotNull(payGetNotify);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            dels.forEach(item -> {
                s.diskDel(item);
            });
        }
    }

}
