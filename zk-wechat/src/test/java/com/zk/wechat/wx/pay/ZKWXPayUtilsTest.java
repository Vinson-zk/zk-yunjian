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
* @Title: ZKWXPayUtilsTest.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 22, 2021 10:47:52 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay;

import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.junit.Test;

import com.zk.core.utils.ZKEncodingUtils;

import junit.framework.TestCase;
import okhttp3.HttpUrl;

/** 
* @ClassName: ZKWXPayUtilsTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWXPayUtilsTest {

    @Test
    public void test() {
        try {
            String api = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi?teste=ere23要";
            HttpUrl url = HttpUrl.parse(api);
            System.out.println(url.encodedPath());
            System.out.println(url.encodedQuery());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testGetCertificate() {
        try {

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void getCerttInfo() {
        try {

//          欢迎使用微信支付！
//          附件中的三份文件（证书pkcs12格式、证书pem格式、证书密钥pem格式）,为接口中强制要求时需携带的证书文件。
//          证书属于敏感信息，请妥善保管不要泄露和被他人复制。
//          不同开发语言下的证书格式不同，以下为说明指引：
//              证书pkcs12格式（apiclient_cert.p12）
//                  包含了私钥信息的证书文件，为p12(pfx)格式，由微信支付签发给您用来标识和界定您的身份
//                  部分安全性要求较高的API需要使用该证书来确认您的调用身份
//                  windows上可以直接双击导入系统，导入过程中会提示输入证书密码，证书密码默认为您的商户号（如：1900006031）
//              证书pem格式（apiclient_cert.pem）
//                  从apiclient_cert.p12中导出证书部分的文件，为pem格式，请妥善保管不要泄漏和被他人复制
//                  部分开发语言和环境，不能直接使用p12文件，而需要使用pem，所以为了方便您使用，已为您直接提供
//                  您也可以使用openssl命令来自己导出：openssl pkcs12 -clcerts -nokeys -in apiclient_cert.p12 -out apiclient_cert.pem
//              证书密钥pem格式（apiclient_key.pem）
//                  从apiclient_cert.p12中导出密钥部分的文件，为pem格式
//                  部分开发语言和环境，不能直接使用p12文件，而需要使用pem，所以为了方便您使用，已为您直接提供
//                  您也可以使用openssl命令来自己导出：openssl pkcs12 -nocerts -in apiclient_cert.p12 -out apiclient_key.pem
//          备注说明：  
//                  由于绝大部分操作系统已内置了微信支付服务器证书的根CA证书,  2018年3月6日后, 不再提供CA证书文件（rootca.pem）下载 

            String merchantId, apiCertSerialNo, pathCertP12, pathApiCert, pathCertKey;
//            String apiCertKey;

            X509Certificate x509cert;
            Certificate cert;
            PublicKey publicKey = null;
            PrivateKey privateKey = null;
            KeyStore ks = null;

            merchantId = "1606675880";
//            apiCertKey = "c8417b30fe294565a8caa57d7b053c1a";
            apiCertSerialNo = "49D449B97E493D95D6A11FED5B6B1715746CB6B0";
            pathCertP12 = "/Users/bs/bs_temp/1606675880_20210222_cert/apiclient_cert.p12";
            pathApiCert = "/Users/bs/bs_temp/1606675880_20210222_cert/apiclient_cert.pem";
            pathCertKey = "/Users/bs/bs_temp/1606675880_20210222_cert/apiclient_key.pem";

            System.out.println("source info ================================= ");
            System.out.println(apiCertSerialNo);
            System.out.println(new BigInteger(ZKEncodingUtils.decodeHex(apiCertSerialNo)));
            System.out.println("cert p12 info ================================= ");
            // 创建 pkcs12 类型 keyStore
            ks = ZKWXPayUtils.getPkcs12KeyStore(pathCertP12, merchantId.toCharArray());

            System.out.println("keystore type=" + ks.getType());
            Enumeration<String> enumeration = ks.aliases();
            String certAlias = null; // "tenpay certificate";
            while (enumeration.hasMoreElements()) {
                String keyAlias = (String) enumeration.nextElement();
                if (certAlias == null) {
                    certAlias = keyAlias;
                }
                System.out.println("keyAlias=[" + keyAlias + "]");
            }
            publicKey = ks.getCertificate(certAlias).getPublicKey();
            privateKey = (PrivateKey) ks.getKey(certAlias, merchantId.toCharArray());
            cert = ks.getCertificate(certAlias);
            System.out.println("certAlias=" + certAlias);
            System.out.println("cert.type=" + cert.getType());
            System.out.println("cert.other: " + ((X509Certificate) cert).getSubjectDN());
            System.out.println("cert.other: " + ((X509Certificate) cert).getNotBefore()); // 有效期开始时间
            System.out.println("cert.other: " + ((X509Certificate) cert).getNotAfter()); // 有效期结束时间
            System.out.println("cert.PublicKey=" + ZKEncodingUtils.encodeBase64ToStr(cert.getPublicKey().getEncoded()));
            System.out.println("keyS.PublicKey=" + ZKEncodingUtils.encodeBase64ToStr(publicKey.getEncoded()));
            System.out.println("keyS.PrivateKey=" + ZKEncodingUtils.encodeBase64ToStr(privateKey.getEncoded()));

            System.out.println("x509cert info ================================= ");
            x509cert = ZKWXPayUtils.getX509Certificate(pathApiCert);
            System.out.println("sxxx.CertSerialNo=" + new BigInteger(ZKEncodingUtils.decodeHex(apiCertSerialNo)));
            System.out.println("x509.CertSerialNo=" + x509cert.getSerialNumber()); // 证书序列号
            System.out.println("sxxx0x16.CertSerialNo=" + apiCertSerialNo);
            System.out.println("x5090x16.CertSerialNo=" + x509cert.getSerialNumber().toString(16)); // 证书序列号0x16
            System.out.println(
                    "x5090x16.CertSerialNo=" + ZKEncodingUtils.encodeHex(x509cert.getSerialNumber().toByteArray())); // 证书序列号0x16
            System.out.println("x5090.other: " + x509cert.getSubjectDN());
            System.out.println("x5090.other: " + x509cert.getNotBefore()); // 有效期开始时间
            System.out.println("x5090.other: " + x509cert.getNotAfter()); // 有效期结束时间
            System.out.println("----------------------------------- ");
            System.out.println("keyS.PublicKey=" + ZKEncodingUtils.encodeBase64ToStr(publicKey.getEncoded()));
            System.out.println(
                    "x509.PublicKey=" + ZKEncodingUtils.encodeBase64ToStr(x509cert.getPublicKey().getEncoded())); // 证书公钥
            System.out.println("----------------------------------- ");

            System.out.println("certKey ================================= ");
            System.out.println("keyF.PrivateKey="
                    + ZKEncodingUtils.encodeBase64ToStr(ZKWXPayUtils.getRSAPrivateKey(pathCertKey).getEncoded())); // 证书私钥
            System.out.println("keyS.PrivateKey=" + ZKEncodingUtils.encodeBase64ToStr(privateKey.getEncoded()));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
