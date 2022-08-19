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
* @Title: ZKThirdPartyServiceTest.java 
* @author Vinson 
* @Package com.zk.wechat.thirdParty.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 18, 2021 12:08:01 PM 
* @version V1.0 
*/
package com.zk.wechat.thirdParty.service;

import org.junit.Test;

import com.zk.wechat.helper.ZKWechatTestHelper;
import com.zk.wechat.thirdParty.entity.ZKThirdParty;

import junit.framework.TestCase;

/** 
* @ClassName: ZKThirdPartyServiceTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKThirdPartyServiceTest {

    @Test
    public void testUpdate() {
        try {
            ZKThirdPartyService s = ZKWechatTestHelper.getCtx().getBean(ZKThirdPartyService.class);
            TestCase.assertNotNull(s);

            ZKThirdParty thirdParty = null;
            int intRes = 0;
            String appid = null, appSecret = null, token = null, aesKey = null, tiket = "test-tiket",
                    accessToken = "accessToken";

            appid = "test-appid";
            appSecret = "appSecret";
            token = "token";
            aesKey = "aeskey";

            thirdParty = s.get(appid);
            TestCase.assertEquals(appSecret, thirdParty.getWxAppSecret());
            TestCase.assertEquals(token, thirdParty.getWxToken());
            TestCase.assertEquals(aesKey, thirdParty.getWxAesKey());
//            TestCase.assertNull(thirdParty.getWxTicket());
//            TestCase.assertNull(thirdParty.getWxAccessToken());

            intRes = s.updateTicket(appid, tiket);
            TestCase.assertEquals(1, intRes);

            intRes = s.updateAccessToken(appid, accessToken, 9);
            TestCase.assertEquals(1, intRes);

            thirdParty = s.get(appid);
            TestCase.assertEquals(appSecret, thirdParty.getWxAppSecret());
            TestCase.assertEquals(token, thirdParty.getWxToken());
            TestCase.assertEquals(aesKey, thirdParty.getWxAesKey());
            TestCase.assertEquals(tiket, thirdParty.getWxTicket());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
