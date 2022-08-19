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
* @Title: ZKWXThirdPartyAuthServiceTest.java 
* @author Vinson 
* @Package com.zk.wechat.wx.thirdParty.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Nov 7, 2021 4:12:34 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.thirdParty.service;

import org.junit.Test;

import com.zk.core.utils.ZKJsonUtils;
import com.zk.wechat.common.ZKWechatCacheUtils;
import com.zk.wechat.helper.ZKWechatTestHelper;
import com.zk.wechat.wx.thirdParty.msgBean.ZKWXTPComponentAccessToken;

import junit.framework.TestCase;

/** 
* @ClassName: ZKWXThirdPartyAuthServiceTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWXThirdPartyAuthServiceTest {

    @Test
    public void testGenAuthUrl() {

        String thirdPartyAppid;
        String authRul;
        ZKWXTPComponentAccessToken componentAccessToken;

        thirdPartyAppid = "wxd125ec7e6a71797b";

        try {
            ZKWXThirdPartyAuthService s = ZKWechatTestHelper.getMainCtx().getBean(ZKWXThirdPartyAuthService.class);

            System.out.println("-----------------------------");
            authRul = s.genAuthUrl(0, null, thirdPartyAppid, null);
            TestCase.assertNotNull(authRul);
            System.out.println("[^_^:20211107-1614-001] authRul: " + authRul);

            componentAccessToken = ZKWechatCacheUtils.getWXTPComponentAccessToken(thirdPartyAppid);
            TestCase.assertNotNull(componentAccessToken);

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
