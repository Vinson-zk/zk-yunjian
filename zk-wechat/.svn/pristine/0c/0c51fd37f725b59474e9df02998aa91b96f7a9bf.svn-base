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
* @Title: ZKWXApiPayUtilsTest.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 23, 2021 11:31:28 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay;

import java.security.PrivateKey;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKWXApiPayUtilsTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWXApiPayUtilsTest {

    @Test
    public void testDowloadCerts() {
        try {
            String resStr;
            String certUrl = "https://api.mch.weixin.qq.com/v3/certificates";
            String merchantId, apiCertSerialNo, pathCertKey;
//            String pathCertP12, pathApiCert;

            merchantId = "1606675880";
//          apiCertKey = "c8417b30fe294565a8caa57d7b053c1a";
            apiCertSerialNo = "49D449B97E493D95D6A11FED5B6B1715746CB6B0";
//            pathCertP12 = "/Users/bs/bs_temp/1606675880_20210222_cert/apiclient_cert.p12";
//            pathApiCert = "/Users/bs/bs_temp/1606675880_20210222_cert/apiclient_cert.pem";
            pathCertKey = "/Users/bs/bs_temp/1606675880_20210222_cert/apiclient_key.pem";
            
//            X509Certificate x509cert;
//            PublicKey publicKey = null;
            PrivateKey privateKey = null;
            
            /////
            privateKey = ZKWXPayUtils.getRSAPrivateKey(pathCertKey);
            
            resStr = ZKWXApiPayUtils.dowloadCerts(certUrl, merchantId, privateKey, apiCertSerialNo);
            System.out.println(resStr);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
