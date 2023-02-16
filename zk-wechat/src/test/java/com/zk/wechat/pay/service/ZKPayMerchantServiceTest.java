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
* @Title: ZKPayMerchantServiceTest.java 
* @author Vinson 
* @Package com.zk.wechat.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 20, 2021 2:21:19 PM 
* @version V1.0 
*/
package com.zk.wechat.pay.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.wechat.helper.ZKWechatTestHelper;
import com.zk.wechat.pay.entity.ZKPayGetBusinessType;
import com.zk.wechat.pay.entity.ZKPayMerchant;

import junit.framework.TestCase;

/**
 * @ClassName: ZKPayMerchantServiceTest
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKPayMerchantServiceTest {

    @Test
    public void testDml() {
        ZKPayMerchantService s = ZKWechatTestHelper.getCtx().getBean(ZKPayMerchantService.class);
        TestCase.assertNotNull(s);
        List<ZKPayMerchant> dels = new ArrayList<>();
        try {

            ZKPayMerchant zkMerchant = null;

            int intRes = 0;
            String pkId = null;
            String apiv3AesKey, certMyPrivatePath;

            zkMerchant = new ZKPayMerchant();
            try {
                s.save(zkMerchant);
                TestCase.assertTrue(false);
            }
            catch(ZKValidatorException e) {

            }

            apiv3AesKey = "t-apiv3AesKey";
            certMyPrivatePath = "t-CertMyPrivatePath";

            zkMerchant = new ZKPayMerchant();
            zkMerchant.setApiv3AesKey("t-apiv3AesKey");
            zkMerchant.setApiCertPathPem("t-CertMyPrivatePath");
            zkMerchant.setApiCertPathKeyPem(certMyPrivatePath);
            zkMerchant.setApiCertUpdateDate(new Date());
            zkMerchant.setApiCertSerialNo("t-apiCertSerialNo");
            zkMerchant.setApiKey("t-apiKey");
            zkMerchant.setApiCertEffectiveTime(new Date());
            zkMerchant.setApiCertExpirationTime(new Date());
            zkMerchant.setStatus(ZKPayMerchant.Status.disabled);
            intRes = s.save(zkMerchant);
            dels.add(zkMerchant);
            pkId = zkMerchant.getPkId();
            TestCase.assertEquals(1, intRes);

            // get
            zkMerchant = s.get(zkMerchant);
            System.out.println("[^_^:20210220-1022-002] zkMerchant: " + ZKJsonUtils.writeObjectJson(zkMerchant));
            TestCase.assertEquals(ZKPayMerchant.Status.disabled, zkMerchant.getStatus().intValue());
            TestCase.assertEquals(apiv3AesKey, zkMerchant.getApiv3AesKey());
            TestCase.assertEquals(certMyPrivatePath, zkMerchant.getApiCertPathKeyPem());

            // 删除
            zkMerchant = new ZKPayMerchant(pkId);
            intRes = s.del(zkMerchant);
            TestCase.assertEquals(1, intRes);
            zkMerchant = s.get(zkMerchant);
            TestCase.assertEquals(ZKPayGetBusinessType.DEL_FLAG.delete, zkMerchant.getDelFlag().intValue());

            // 物理删除
            zkMerchant = new ZKPayMerchant(pkId);
            intRes = s.diskDel(zkMerchant);
            TestCase.assertEquals(1, intRes);
            zkMerchant = s.get(zkMerchant);
            TestCase.assertNull(zkMerchant);

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
