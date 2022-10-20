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
* @Title: ZKPlatformCertServiceTest.java 
* @author Vinson 
* @Package com.zk.wechat.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 23, 2021 10:35:30 AM 
* @version V1.0 
*/
package com.zk.wechat.pay.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.zk.wechat.helper.ZKWechatTestHelper;
import com.zk.wechat.pay.entity.ZKPlatformCert;

import junit.framework.TestCase;

/** 
* @ClassName: ZKPlatformCertServiceTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKPlatformCertServiceTest {

    @Test
    public void testDml() {

        ZKPlatformCertService s = ZKWechatTestHelper.getCtx().getBean(ZKPlatformCertService.class);
        TestCase.assertNotNull(s);
        List<ZKPlatformCert> dels = new ArrayList<>();
        try {
            int resInt = 0;
            ZKPlatformCert pCert = null;

            pCert = new ZKPlatformCert();
            pCert.setMchid("t-mchid");
            pCert.setCertSerialNo("t-setCertSerialNo");
            pCert.setCertPath("t-setCertPath");
            pCert.setCertEffectiveTime(new Date());
            pCert.setCertExpirationTime(new Date());

            resInt = s.save(pCert);
            dels.add(pCert);
            TestCase.assertEquals(1, resInt);

            TestCase.assertNotNull(s.getBySerial(pCert.getMchid(), pCert.getCertSerialNo()));

            TestCase.assertNotNull(s.getCertByMchid(pCert.getMchid()));

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

    @Test
    public void testGetCertByMchid() {

        ZKPlatformCertService s = ZKWechatTestHelper.getCtx().getBean(ZKPlatformCertService.class);
        TestCase.assertNotNull(s);
        try {


        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }

    }


}
