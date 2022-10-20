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
* @Title: ZKWXPayServiceTest.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 23, 2021 10:02:34 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay.service;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Test;

import com.zk.core.utils.ZKEncodingUtils;
import com.zk.wechat.helper.ZKWechatTestHelper;
import com.zk.wechat.wx.pay.entity.ZKWXApiCert;
import com.zk.wechat.wx.pay.entity.ZKWXPlatformCert;

import junit.framework.TestCase;

/** 
* @ClassName: ZKWXPayServiceTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWXPayServiceTest {

    public static void main(String[] args) throws ParseException, IOException {
        ZKWXPayService s = ZKWechatTestHelper.getCtx().getBean(ZKWXPayService.class);

        String mchid = "1606675880";
        s.getPlatformCert(mchid);
    }

    @Test
    public void testGetPlatformCert() {
        try {
            ZKWXPayService s = ZKWechatTestHelper.getCtx().getBean(ZKWXPayService.class);
            TestCase.assertNotNull(s);

            String mchid = "1606675880";
            ZKWXPlatformCert cert;

            cert = s.getPlatformCert(mchid);

            TestCase.assertNotNull(cert);

//            String path = ZKWechatUtils.getFilePath("test/del/file.pem");
//            File f = new File(path);
//            ZKFileUtils.createFile(f);
//            ZKFileUtils.writeFile("tests".getBytes(), f, false);
//            TestCase.assertNotNull(path);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testGetApiCertKey() {
        try {
            ZKWXPayService s = ZKWechatTestHelper.getCtx().getBean(ZKWXPayService.class);
            TestCase.assertNotNull(s);

            String mchid = "1606675880";
            ZKWXApiCert key;

            String apiCertSerialNo = "49D449B97E493D95D6A11FED5B6B1715746CB6B0";
            String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC0kicKaPBVSVFZkGTCsA2Co9IKeA4q/C2ewU5XdXguMHQW5gjik7hbhVURJxRzer6D9Q0Xif/sCw5I83+FUr5UIBTv/Y32Avbl72Ta7m23AjFSicMjecpNl4vku1r/wfaJNHQRvekF4fGNLmUqZ/xYgik7/LfPTQ1lI1qRIJ0j9n25duPylFMVX1beH9ZQyMpsOJG4mhNXXqTvLVbrY0bRNEh0VtHK9gwK8eOVi3mS/6WcHqssi1U0GBsbiQurWCX1/b2gXQ1Q4fA2mMWw50Gm/ytTaS3f3bYjWb5SGZSZloPWwx7457WIuKBGBVHRFtZWxIFdBAvb1POb4ca9tSnHAgMBAAECggEAAmLUBT0xv87CNm7EBhaRdyJ3ChBgD41ZhPmE8X7p4aJeRlcv0t6k1wDCCQH9MTF0BwZb7+2w5guXWrts+fQH89CgtjMhhxiu7oblTEdOR9bNlAuh+nwHglL4VENthmenvFRnHDzoMV209Wqhh5fsK7c8mEBUpXadMLLw5W4K5Yi8nbDe7yiqdOIJ7bqrhbHiR9FLfKGUEr7vaPR7mvM2eq/1B1+TR15B3gFZrLiRuUWmN3x0PaSfijC9jwp2mk2u0GKxWHo8e71bCQJQytZfxJlqfN3vx/aQCoDKRITRrMACz5vCIrGjqytkS0EiUZY4ZJVCkECLDbMNms8AtIG6cQKBgQDbAKKVn6YZPK2J6CqELdmyLU5yxjf9Ijw8eH3tBQp1tdDXPcs7lpEUb9EkobNQYfMiJefeJ0cFn7qvqV9YG/aleDJMF5+2WdAtc8wuCqpg2kS49wT06/dasMJJWtyTCkIqogiwaSPuTRgvWbO6GlxVHJfSXMw/h90fFmfF1tq2kwKBgQDTE3A0TSL1ZdnDkd1NTH7E+K80QLN3qeYOoUj3Xrb9enJOh11HSv+AsTwJbPkjU4Z0787Tfs23ZPvxIJFFXaI0y2QPC4YSY+b2rkxbAPEFwlPX98IJF9zlep+BlfTRzFGFqQf1OhqUvWHuElN+E/uLob3JKK9uQG0ZJO1vmwxsfQKBgDvNaJhDCDBMCXdnFnURsCifhpSA3DkbaCd7H6fhgIG0LFsaXKzg6K0T+6BtJ3IDzUi94RsgzpwSif/92DNPL4Lv14w/xzUcQWSoAlFwSCY6dBmuGoRnCxyQt/0f+quMWZUauhSyIUWpd7k2XYWsPRpgvHSNMpkGmLMsgxnbGc5RAoGAMdgBQxiEDNcEWX50d/B8bx8aoIg22ScxwHqttFyVcb4ciBbHHDLFqyovNzqYipMKyUpn2OG0yn36ULKUG4FE8JqnAjaCbo1Q2SfHuCW8xNNi4TkMhQExlK8kQ4GZ0oEhLyL/UhVqeX1PRtzlAnzNheen3XzSEIL+QFXMC/LdODECgYEAyyE+OhP5fxrUxSa8sigTlahRv57t7yUHGk0g6zLoqSnIaSlVXh94o+hNfJmwKI/lr2fJL62LlU/oJaF2bWpSzYTbgDx6Ux5zCs5pe67L/He28Co8ZO7N1TlTvmoPd99KlE+WumRzBj0Es3MOOnRFcvFuFWQyb3qeDf07JRN9Iow=";

            key = s.getApiCertKey(mchid);

            TestCase.assertEquals(apiCertSerialNo, key.getSerialNo());
            TestCase.assertEquals(privateKey, ZKEncodingUtils.encodeBase64ToStr(key.getPrivateKey().getEncoded()));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testCheckSign() {
        try {
            ZKWXPayService s = ZKWechatTestHelper.getCtx().getBean(ZKWXPayService.class);
            TestCase.assertNotNull(s);

            String mchid = null;
            String wechatpaySignature, wechatpayTimestamp, wechatpayNonce, bodyStr, wechatpaySerial;
            boolean res = false;

            mchid = "1606675880";
            wechatpaySignature = "yoFMHXvQ4CbRMjtHX0hgp2imb07v4LAsG/hScumwAN8HDqboYVp43RuPZIiM22gPxx++kWy2lW/yuRCeMWHVa0HvwhLFjMlF2ZiCeEokME5UipIdFdtvDqmQT2L3K/xB/Cf2qN5/UWgt4dDC6sCTS+rxR+ALodBk76PeM0ZtbkHQwHbwNJTaj19/eFR/7sq7h9CYX9O/NK2PJDUpDxmNlqNiGd6ZsfQHI3JrIbttpD5o73iXIuPKqdqFq8ljj6Nh0JxZSzIGXHl6ogH6oO8eXC9SLKgYUgGm41ilk6MakJYD6sDTLt/SOhxYHBhcP1wp08awW7BHKDnjmhvTakOvRg==";
            wechatpayTimestamp = "1614592068";
            wechatpayNonce = "PzQNXPeYqgvmqN4z1vvTl5lQNmHEV1ke";
            wechatpaySerial = "5FDD8A552116CE0D35A6ADC6EA19DBB8FB748507";
            bodyStr = "{\"id\":\"752fbfe0-bbf0-5ce0-b8f0-1217d8f5f745\",\"create_time\":\"2021-03-01T17:47:43+08:00\",\"resource_type\":\"encrypt-resource\",\"event_type\":\"TRANSACTION.SUCCESS\",\"summary\":\"支付成功\",\"resource\":{\"original_type\":\"transaction\",\"algorithm\":\"AEAD_AES_256_GCM\",\"ciphertext\":\"xrv0anG/ooNkxgQJj8RpHZMdFfFe8JMmtfS8iko1NaU3fkrppkLyNwz3f+ORicLjJ5qJItpN1JoO6hPTrvxc/dQSgZMgvvthBY0SJV2R8qwpcBW2cfdJKfZKcYaBCPMvSFkCWMS4mpgnWqFScDho3gT6PKCtTVlIeektOmg47j+BhdSmREc9NxOo2peNR70A+AGBaa6UCiasETMwwR+cfY9+FiWn47smhEB9PAuZ3N9+mBnIznghEhsq2rhHxkCGw0eBX13KlgAmAvFM0+EYJPRWkMWPIl1bcWcysIP4jM8QnddruURhVKs2VhKqQbPyJMjEdXpkA2t09TusCVrPyClhE6f8qq3s8znE0EoFchsLrRnpUYb0PakYAgWCQ9x+PnpuJvifAB6bTCqX3LKFiqc5l7bHAIDGmj/FK4mzfiX9YH2GUaz4m1DzFNjVAPAqoSrcn2xVcbMwy53QW1VS8lre+SiV1Y4TICiXcnvMupuSrxJY26oKAFQiqviK0BK1f8DEd3S/LuvYAcnvF3ssfFiEf8g590FTnCgNMKP0ZIJB5Qvu6LxmELdLzeQux5T+5Tk=\",\"associated_data\":\"transaction\",\"nonce\":\"fLuBVZEw7RgZ\"}}";
            res = s.checkSign(mchid, wechatpaySerial, wechatpaySignature, wechatpayTimestamp, wechatpayNonce, bodyStr);
            TestCase.assertTrue(res);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
