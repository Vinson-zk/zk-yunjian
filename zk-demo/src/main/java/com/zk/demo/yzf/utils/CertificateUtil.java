/**
 * Bestpay.com.cn Inc.
 * Copyright (c) 2011-2018 All Rights Reserved.
 */
package com.zk.demo.yzf.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;

import org.bouncycastle.util.encoders.Base64;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * DESC:证书工具包，加载交易证书
 * Author: yangbo2018
 * Date:   20/8/11
 * Time:   10:35
 */
@SuppressWarnings("restriction")
public class CertificateUtil {
    public static final String KEYSTORETYPE_JKS="JKS";
    public static final String KEYSTORETYPE_PKCS12="PKCS12";
    public static TradeCertificate getTradeCertificate(String properties, String keyStroeType) {
        PropertiesUtil propertiesUtil = new PropertiesUtil(properties);
        String merchantCertificatePath = propertiesUtil.get("merchantCertificatePath");
        String merchantCertificatePwd = propertiesUtil.get("merchantCertificatePwd");
        String bestpayCertificatePath = propertiesUtil.get("bestpayCertificatePath");
        String iv=propertiesUtil.get("iv");
        PrivateKey merchantPrivateKey = null;
        PublicKey bestpayPublicKey = null;

        ClassLoader cl = CertificateUtil.class.getClassLoader();
        InputStream fiKeyFile = cl.getResourceAsStream(merchantCertificatePath);
        merchantPrivateKey=getMerchantPirvateKey(fiKeyFile, merchantCertificatePwd, keyStroeType);
        InputStream certfile = cl.getResourceAsStream(bestpayCertificatePath);
        bestpayPublicKey= getPublicKey(certfile);
        return new TradeCertificate(merchantPrivateKey,bestpayPublicKey,iv);
    }

    /**
     * 证书类型为“存量数据"类型证书类型，flag为true,当flag为false时同 TradeCertificate getTradeCertificate(String properties, String keyStroeType)
     * */

    public static TradeCertificate getTradeCertificate(String properties, String keyStroeType, boolean flag) {
        if(!flag){
            return getTradeCertificate(properties,keyStroeType);
        }
        PropertiesUtil propertiesUtil = new PropertiesUtil(properties);
        String merchantCertificatePath = propertiesUtil.get("merchantCertificatePath");
        String merchantCertificatePwd = propertiesUtil.get("merchantCertificatePwd");
        String bestpayCertificatePath = propertiesUtil.get("bestpayCertificatePath");
        String iv=propertiesUtil.get("iv");
        PrivateKey merchantPrivateKey = null;
        PublicKey bestpayPublicKey = null;

        ClassLoader cl = CertificateUtil.class.getClassLoader();
        InputStream fiKeyFile = cl.getResourceAsStream(merchantCertificatePath);
        merchantPrivateKey=getMerchantPirvateKey(fiKeyFile, merchantCertificatePwd, keyStroeType);
        bestpayPublicKey= base64StrToCert(bestpayCertificatePath);
        return new TradeCertificate(merchantPrivateKey,bestpayPublicKey,iv);
    }

    /**
     * 获取公钥
     */
    private static PublicKey getPublicKey(InputStream pubKey) {
        X509Certificate x509cert = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            x509cert = (X509Certificate) cf.generateCertificate(pubKey);
        } catch (CertificateException e) {
            if (pubKey != null) {
                try {
                    pubKey.close();
                } catch (IOException e1) {
                    throw new RuntimeException("文件流关闭异常", e1);
                }
            }
            throw new RuntimeException("初始化公钥异常", e);
        }
        return x509cert.getPublicKey();
    }

    /**
     * 获取商户私钥
     */
    private static PrivateKey getMerchantPirvateKey(InputStream priKey, String keyPassword,String keyStoreType) {
        String keyAlias;
        PrivateKey privateKey = null;
        try {
            KeyStore ks = KeyStore.getInstance(keyStoreType);
            ks.load(priKey, keyPassword.toCharArray());
            Enumeration<?> myEnum = ks.aliases();
            while (myEnum.hasMoreElements()) {
                keyAlias = (String) myEnum.nextElement();
                if (ks.isKeyEntry(keyAlias)) {
                    privateKey= (PrivateKey) ks.getKey(keyAlias, keyPassword.toCharArray());
                    break;
                }
            }
        } catch (Exception e) {
            if (priKey != null) {
                try {
                    priKey.close();
                } catch (IOException e1) {
                    throw new RuntimeException("流关闭异常", e1);
                }
            }
            throw new RuntimeException("加载私钥失败", e);
        }

        if (privateKey == null) {
            throw new RuntimeException("私钥不存在");
        }

        return privateKey;
    }

    public static PublicKey base64StrToCert(String base64Cert) {
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream streamCert = new ByteArrayInputStream(
                    new BASE64Decoder().decodeBuffer(base64Cert));

            X509Certificate cert = (X509Certificate) factory.generateCertificate(streamCert);
            if (cert == null) {
                throw new GeneralSecurityException("将cer从base64转换为对象失败");
            }
            return cert.getPublicKey();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String certToBase64(PublicKey publicKey){
        byte[] keyBytes = publicKey.getEncoded();
        String s = (new BASE64Encoder()).encode(keyBytes);
        return s;
    }

    /**
     * 得到公钥
     *
     * @param key 加密字符串（经过base64编码）
     * @throws Exception
     */
    public static PublicKey getRsaPublicKey(String key) throws Exception {

        byte[] keyBytes;
        keyBytes = Base64.decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        return publicKey;
    }

}
