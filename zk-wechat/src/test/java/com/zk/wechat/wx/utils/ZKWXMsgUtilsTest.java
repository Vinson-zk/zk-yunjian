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
* @Title: ZKWXMsgUtilsTest.java 
* @author Vinson 
* @Package com.zk.wechat.wx.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date May 19, 2022 12:28:33 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.utils;

import org.dom4j.Document;
import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKWXMsgUtilsTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWXMsgUtilsTest {

    @Test
    public void testGetDocumentMsg() {

        String appId, appSecret, token, aesKey;

        // token: gxfz-20210210-Vinson
        // encodingAesKey: devGxfzds120210210d2doscefrdj3soiuedxdo4qwe
        // appId: wxd125ec7e6a71797b
        // source:
        // <xml>
        // <AppId><![CDATA[wxd125ec7e6a71797b]]></AppId>
        // <Encrypt><![CDATA[ZJ145OXPGbIuCh53zgV7ZluNjY1UZPF2+dBRnH/cPygeSXaJW7bt8A8tmb6JjSHqTzM8eXp2uhsK/3TRl+7/E2HXNEKucYKhRtYM7RRaOOYT7mNLXdQPHoTeVqZcOSayBqnRD43DoTzMPTxPhyFE2OhJPDRXMSH4qgoIHxKjPX6la+3jy6CbkYr2hWOj65nJy9anAfZ2B7MBAPNZoE0aYLcxyLmDh9g3Gf3hikJmxwRVRPLuLwy1rItmVtwV1yQOsssjUwh6ver0xvKR8LdhweAAvhXNktFDenYp4hy70GpHxhuHhNDAPK3s6C8fcpoc+xjudOKxGRfWEocuOeMf8tFUxcdxzpkQVrsVLtcv520HtQJHphXZZSdhSPA4dAn/2ZoFeTHQdpt/V3yyl7hIeapfEd5NZnINKSJWsrmBqIxJKsp9xzW3FLqBFj6A9hFv6ku0eyKFBmEEsg8hTdk6Zg==]]></Encrypt>
        // </xml>

        String encStr = "<xml>" + " <AppId><![CDATA[wxd125ec7e6a71797b]]></AppId>"
                + "    <Encrypt><![CDATA[ZJ145OXPGbIuCh53zgV7ZluNjY1UZPF2+dBRnH/cPygeSXaJW7bt8A8tmb6JjSHqTzM8eXp2uhsK/3TRl+7/E2HXNEKucYKhRtYM7RRaOOYT7mNLXdQPHoTeVqZcOSayBqnRD43DoTzMPTxPhyFE2OhJPDRXMSH4qgoIHxKjPX6la+3jy6CbkYr2hWOj65nJy9anAfZ2B7MBAPNZoE0aYLcxyLmDh9g3Gf3hikJmxwRVRPLuLwy1rItmVtwV1yQOsssjUwh6ver0xvKR8LdhweAAvhXNktFDenYp4hy70GpHxhuHhNDAPK3s6C8fcpoc+xjudOKxGRfWEocuOeMf8tFUxcdxzpkQVrsVLtcv520HtQJHphXZZSdhSPA4dAn/2ZoFeTHQdpt/V3yyl7hIeapfEd5NZnINKSJWsrmBqIxJKsp9xzW3FLqBFj6A9hFv6ku0eyKFBmEEsg8hTdk6Zg==]]></Encrypt>"
                + "</xml>";

        // 密文对应的微信平台的参数

        appId = "wxd125ec7e6a71797b";
        appSecret = "86329bf361fea8dc11cff9dbc980757b";
        token = "gxfz-20210210-Vinson";
        aesKey = "devGxfzds120210210d2doscefrdj3soiuedxdo4qwe";

        try {
            System.out.println("-----------------------------");
            System.out.println(encStr);
            System.out.println("-----------------------------");
            System.out.println(String.format(" appId: %s\n appSecret: %s\n token: %s\n aesKey: %s", appId, appSecret,
                    token, aesKey));
            System.out.println("-----------------------------");
            Document rootE = ZKWXMsgUtils.getDocumentMsg(token, aesKey, appId, encStr);
            TestCase.assertNotNull(rootE);
            System.out.println(rootE.toString());
            System.out.println("-----------------------------");

            TestCase.assertEquals(appId, rootE.getRootElement().element("AppId").getStringValue());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
