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
* @Title: ZKWXUtilsTest.java 
* @author Vinson 
* @Package com.zk.wechat.thirdParty.wx.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 18, 2021 1:40:58 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.common;

import java.nio.charset.Charset;
import java.util.UUID;

import org.dom4j.Element;
import org.junit.Test;

import com.zk.core.encrypt.utils.ZKEncryptUtils;
import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.utils.ZKXmlUtils;
import com.zk.wechat.wx.utils.ZKWXUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKWXUtilsTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWXUtilsTest {

    @Test
    public void testGet32UUID() {
        try {
            String nonceStr = null;

            nonceStr = UUID.randomUUID().toString();
            System.out.println(" nonceStr.length: " + nonceStr.length() + "; nonceStr:" + nonceStr);

            nonceStr = UUID.randomUUID().toString();
            nonceStr = nonceStr.replaceAll("-", "");
            System.out.println(" nonceStr.length: " + nonceStr.length() + "; nonceStr:" + nonceStr);
            
            nonceStr = ZKWXUtils.getUUID32();
            System.out.println(" nonceStr.length: " + nonceStr.length() + "; nonceStr:" + nonceStr);
            TestCase.assertEquals(32, nonceStr.length());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }

    }

    @Test
    public void testDecryptXml() {

        String appId, appSecret, token, aesKey;

        // 密文字节 hex 编码
        String hexEncStr = "3c786d6c3e0a202020203c41707049643e3c215b43444154415b7778333439326431653830336234393336635d5d3e3c2f41707049643e0a202020203c456e63727970743e3c215b43444154415b74776e74656a78544c6c74376d3767785256726b6a30756e7466356368314e385944703038433647544a6e6e3335323675354566333635724331334b44646355426453662f33742b7179564f5135354b4e336e784f4d4231496b4c5734313150732b57536445344c51384656336d452b395566357139712b41317737493635395953386c4d504b7a3767594c536443744b64344379766c473161456e493249522f70705a7779594f7a59546f624e355477733248772f38656f2f596a516432376e4e613266736b7250563639534251474c584c653039743175504d386a62613962645864777644513253725a68433364344d566573506b506d6b76357a344c446b6f704f2b486257674d7a7048582b6f30355139766c466e384d64616d68453567467553634f6e6e7264634242747132594734364f7267327a32644a686a786d64666c4f6e7a48744c794c72464836736e4763755854436b4a51326442494539745a7a682f56364e545057473155755435485337623573734969794c374476703744305a5a687375753478375a452f4952534d794538364e427370777a4f6b306a554334314a736e766f77623173316243714a5a2f3462392f495539786c6a636a794d6739656a31776661694c4d55637369766254773d3d5d5d3e3c2f456e63727970743e0a3c2f786d6c3e0a";
        // 密文
        String encStr = new String(ZKEncodingUtils.decodeHex(hexEncStr), Charset.forName("UTF-8"));
        System.out.println("encStr: " + encStr);
        encStr = "<xml>\n" + "    <AppId><![CDATA[wx3492d1e803b4936c]]></AppId>\n"
                + "    <Encrypt><![CDATA[twntejxTLlt7m7gxRVrkj0untf5ch1N8YDp08C6GTJnn3526u5Ef365rC13KDdcUBdSf/3t+qyVOQ55KN3nxOMB1IkLW411Ps+WSdE4LQ8FV3mE+9Uf5q9q+A1w7I659YS8lMPKz7gYLSdCtKd4CyvlG1aEnI2IR/ppZwyYOzYTobN5Tws2Hw/8eo/YjQd27nNa2fskrPV69SBQGLXLe09t1uPM8jba9bdXdwvDQ2SrZhC3d4MVesPkPmkv5z4LDkopO+HbWgMzpHX+o05Q9vlFn8MdamhE5gFuScOnnrdcBBtq2YG46Org2z2dJhjxmdflOnzHtLyLrFH6snGcuXTCkJQ2dBIE9tZzh/V6NTPWG1UuT5HS7b5ssIiyL7Dvp7D0ZZhsuu4x7ZE/IRSMyE86NBspwzOk0jUC41Jsnvowb1s1bCqJZ/4b9/IU9xljcjyMg9ej1wfaiLMUcsivbTw==]]></Encrypt>\n"
                + "</xml>\n";
        TestCase.assertEquals(encStr, new String(ZKEncodingUtils.decodeHex(hexEncStr), Charset.forName("UTF-8")));

        // token: gxfz-20210210-Vinson
        // encodingAesKey: devGxfzds120210210d2doscefrdj3soiuedxdo4qwe
        // appId: wxd125ec7e6a71797b
        // source: 
        // <xml>
        // <AppId><![CDATA[wxd125ec7e6a71797b]]></AppId>
        // <Encrypt><![CDATA[ZJ145OXPGbIuCh53zgV7ZluNjY1UZPF2+dBRnH/cPygeSXaJW7bt8A8tmb6JjSHqTzM8eXp2uhsK/3TRl+7/E2HXNEKucYKhRtYM7RRaOOYT7mNLXdQPHoTeVqZcOSayBqnRD43DoTzMPTxPhyFE2OhJPDRXMSH4qgoIHxKjPX6la+3jy6CbkYr2hWOj65nJy9anAfZ2B7MBAPNZoE0aYLcxyLmDh9g3Gf3hikJmxwRVRPLuLwy1rItmVtwV1yQOsssjUwh6ver0xvKR8LdhweAAvhXNktFDenYp4hy70GpHxhuHhNDAPK3s6C8fcpoc+xjudOKxGRfWEocuOeMf8tFUxcdxzpkQVrsVLtcv520HtQJHphXZZSdhSPA4dAn/2ZoFeTHQdpt/V3yyl7hIeapfEd5NZnINKSJWsrmBqIxJKsp9xzW3FLqBFj6A9hFv6ku0eyKFBmEEsg8hTdk6Zg==]]></Encrypt>
        // </xml>

        encStr = "<xml>" + " <AppId><![CDATA[wxd125ec7e6a71797b]]></AppId>"
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
            System.out.println(
                    String.format(" appId: %s\n appSecret: %s\n token: %s\n aesKey: %s", appId, appSecret, token,
                            aesKey));
            System.out.println("-----------------------------");
            String decStr = ZKWXUtils.decryptXml(token, aesKey, appId, encStr);
            TestCase.assertNotNull(decStr);
            System.out.println(decStr);
            System.out.println("-----------------------------");

            Element rootE = ZKXmlUtils.getDocument(decStr).getRootElement();
            TestCase.assertEquals(appId, rootE.element("AppId").getStringValue());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testMd5Encode() {

        String str, encStr;

        try {
            str = "JSDF0W95183R7HDSAJFHKASFDJASFW1#$$^$%&";
            System.out.println("-----------------------------");
            encStr = ZKWXUtils.Md5Encode(str.getBytes());
            System.out.println(encStr);
            encStr = ZKEncodingUtils.encodeHex(ZKEncryptUtils.md5Encode(str.getBytes()));
            System.out.println(encStr);
            encStr = ZKEncodingUtils
                    .encodeHex(ZKEncryptUtils.encryptDigest(str.getBytes(), null, ZKEncryptUtils.DIGEST_MODE.MD5));
            System.out.println(encStr);
            System.out.println("-----------------------------");
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

//    @Test
//    public void testDoingJsSignature() {
//
//        TestCase.assertNotNull(TestConfig.getSpringCtx());
//        ThirdService thirdService = TestConfig.getSpringCtx().getBean(ThirdService.class);
//
//        // 密文对应的微信平台参数
//        String thirdDevAppId = TestConfig.thirdDevAppId;
//        String authAppId = TestConfig.authorizerAppId;
//
//        try {
//            JsTicket jsTicket = thirdService.getJsTicket(thirdDevAppId, authAppId);
//            TestCase.assertNotNull(jsTicket);
//            JsConfig jsConfig = null;
//            jsConfig = WXMsgUtils.doingJsSignature(jsTicket.getAccessToken(), "http://mp.weixin.qq.com?params=param");
//            TestCase.assertNotNull(jsConfig);
//
//            System.out.println("-----------------------------");
//            System.out.println("=== " + JsonUtils.writeObjectJson(jsConfig));
//
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//            TestCase.assertTrue(false);
//        }
//    }

}
