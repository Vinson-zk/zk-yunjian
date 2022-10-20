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
* @Title: ZKWXApiThirdPartyServiceTest.java 
* @author Vinson 
* @Package com.zk.wechat.wx.thirdParty.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Nov 7, 2021 9:46:34 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.thirdParty.service;

import org.junit.Test;

import com.zk.core.utils.ZKJsonUtils;
import com.zk.wechat.helper.ZKWechatTestHelper;
import com.zk.wechat.wx.thirdParty.msgBean.ZKWXTPComponentAccessToken;

import junit.framework.TestCase;

/** 
* @ClassName: ZKWXApiThirdPartyServiceTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWXApiThirdPartyServiceTest {

    @Test
    public void testApi_component_token() {

        String thirdPartyAppId;
        String thirdAppsecret;
        String componentVerifyTicket;

        ZKWXTPComponentAccessToken componentAccessToken;

        thirdPartyAppId = "wxd125ec7e6a71797b";
        thirdAppsecret = "86329bf361fea8dc11cff9dbc980757b";
        componentVerifyTicket = "ticket@@@D2JkIyohj1NQBfzwi9tZwSa6UUvc5Z_u0RIOO7inq73Rqq2I3x6OySmE4VYjUxXEJj4hWho3tLVReMVphn_vVQ";

        try {
            ZKWXApiThirdPartyService s = ZKWechatTestHelper.getMainCtx().getBean(ZKWXApiThirdPartyService.class);

            System.out.println("-----------------------------");
            componentAccessToken = s.api_component_token(thirdPartyAppId, thirdAppsecret, componentVerifyTicket);

            TestCase.assertNotNull(componentAccessToken);
            TestCase.assertTrue(
                    componentAccessToken.getAccessToken() != null && !"".equals(componentAccessToken.getAccessToken()));

            System.out.println("[^_^:20211105-1701-001] componentAccessToken: "
                    + ZKJsonUtils.writeObjectJson(componentAccessToken));
            System.out.println("-----------------------------");

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
