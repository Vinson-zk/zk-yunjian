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
* @Title: ZKWXPayUtils.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 19, 2021 12:56:12 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay;

import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.utils.ZKFileUtils;
import com.zk.core.utils.ZKStreamUtils;

import okhttp3.HttpUrl;

/** 
* @ClassName: ZKWXPayUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWXPayUtils {
    
    /**
     * 日志对象
     */
    protected static Logger log = LogManager.getLogger(ZKWXPayUtils.class);

    public static interface Key {
        public static final String sign = "sign";
    }

    public static void main(String[] args) {
        String api = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi?teste=ere23要";
        HttpUrl url = HttpUrl.parse(api);
        System.out.println(url.encodedPath());
        System.out.println(url.encodedQuery());
    }

    /**
     * jsapi 调起支付时；所需要签名的 签名串
     *
     * @Title: buildJsPaySignStr
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 19, 2021 2:21:04 PM
     * @param appId
     *            小程序appId
     * @param timestamp
     *            时间戳；格林威治时间1970年01月01日00时00分00秒(北京时间1970年01月01日08时00分00秒)起至现在的总秒数，作为请求时间戳。(10位数字)。
     * @param nonceStr
     *            随机字符串
     * @param packageStr
     *            订单详情扩展字符串；示例值：prepay_id=wx201410272009395522657a690389285100
     * @return
     * @return String
     */
    public static String buildJsPaySignStr(String appId, String timestamp, String nonceStr, String packageStr) {
        return buildSignStr(appId, timestamp, nonceStr, packageStr);
    }

    /**
     * 支付请求头签名时；所需要签名的 签名串
     *
     * @Title: buildRequestSignStr
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 19, 2021 2:59:19 PM
     * @param method
     * @param apiUrl
     * @param timestamp
     * @param nonceStr
     * @param body
     * @return
     * @return String
     */
    public static String buildRequestSignStr(String method, String apiUrl, String timestamp, String nonceStr,
            String body) {
        HttpUrl url = HttpUrl.parse(apiUrl);
        String canonicalUrl = url.encodedPath();
        if (url.encodedQuery() != null) {
            canonicalUrl += "?" + url.encodedQuery();
        }
        return buildSignStr(method, canonicalUrl, timestamp, nonceStr, body);
    }

    /**
     * 接收微信平台应答时，响应请求的签名验证时；所需要签名的 签名串
     *
     * @Title: buildResponseSignStr
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 19, 2021 3:25:24 PM
     * @param wechatpayTimestamp
     *            HTTP头Wechatpay-Timestamp 中的应答时间戳。
     * @param wechatpayNonce
     *            HTTP头Wechatpay-Nonce 中的应答随机串
     * @param responseBody
     *            应答主体（response Body）
     * @return String
     */
    public static String buildResponseSignStr(String wechatpayTimestamp, String wechatpayNonce, String responseBody) {
        return buildSignStr(wechatpayTimestamp, wechatpayNonce, responseBody);
    }

    /**
     * 构造支付签名串
     *
     * @Title: buildSignStr
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 19, 2021 2:45:48 PM
     * @param strs
     * @return
     * @return String
     */
    private static String buildSignStr(String... strs) {
        StringBuffer sb = new StringBuffer();
        for (String s : strs) {
//            if (!ZKStringUtils.isEmpty(s)) {
            sb.append(s).append("\n");
//            }
        }
        return sb.toString();
    }

    /****************************************************************/
    /** 微信支付签名与签名验证 *********************************************/
    /****************************************************************/

    /**
     * 根据私钥，生成 SHA256withRSA 方式的签名
     *
     * @Title: sign
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 19, 2021 2:24:54 PM
     * @param message
     *            生成签名的数据字节
     * @param privateKey
     *            商户证书中的私钥
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @return String 经过Base64编码的签名字符串
     */
    public static String signSHA256withRSA(byte[] message, PrivateKey privateKey) {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(privateKey);
            sign.update(message);
            return ZKEncodingUtils.encodeBase64ToStr(sign.sign());
        }
        catch(NoSuchAlgorithmException e) {
            throw new RuntimeException("签名参数错误", e);
        }
        catch(InvalidKeyException e) {
            throw new RuntimeException("无效的私钥", e);
        }
        catch(SignatureException e) {
            throw new RuntimeException("生成签名异常", e);
        }
    }

    /**
     * 校验签名
     *
     * @Title: signVerifySHA256withRSA
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 20, 2021 5:34:06 PM
     * @param wechatpaySignature
     *            微信平台签名字符串，使用Base64进行解码，得到应答签名。
     * @param message
     *            校验消息；
     * @param certificate
     *            微信平台证书
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @return boolean true-校验成功；false-校验失败；
     */
    public static boolean signVerifySHA256withRSA(String wechatpaySignature, byte[] message, Certificate certificate) {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initVerify(certificate);
            sign.update(message);
            return sign.verify(ZKEncodingUtils.decodeBase64(wechatpaySignature));
        }
        catch(NoSuchAlgorithmException e) {
            throw new RuntimeException("PKCS12证书参数错误", e);
        }
        catch(SignatureException e) {
            throw new RuntimeException("验证签名失败", e);
        }
        catch(InvalidKeyException e) {
            throw new RuntimeException("无效的证书", e);

        }
    }

    /****************************************************************/
    /** 微信支付中 处理证书或密钥一些方法，有参考微信官网提供的样例 ***********/
    /****************************************************************/
    
    public static interface KeyStoreType {
        public static final String PKCS12 = "PKCS12";
    }

    public static interface CertType {
        public static final String X509 = "X509";
    }

    public static interface KeyAlgorithm {
        public static final String RSA = "RSA";
    }


    /**
     * 读取 PKCS12 证书；加载返回的 KeyStore 中，包含 证书/公钥、私钥；
     * 
     * 不建议使用这个方法，建议使用 openssl 工具，从【pkcs12格式证书】中导出【pem格式证书】证书和【pem格式证书密钥】
     * 
     * 然后分别调用 getX509Certificate 和 getPrivateKey 来分别加载
     * 
     * 导出 pem格式证书 openssl pkcs12 -clcerts -nokeys -in apiclient_cert.p12 -out
     * apiclient_cert.pem
     * 
     * 导出 pem格式证书密钥 openssl pkcs12 -nocerts -in apiclient_cert.p12 -out
     * apiclient_key.pem
     *
     * @Title: getPkcs12KeyStore
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 23, 2021 8:53:39 AM
     * @param certPath
     * @param pwd
     * @return
     * @return KeyStore
     * @throws FileNotFoundException
     */
    public static KeyStore getPkcs12KeyStore(String certPath, char[] pwd) throws FileNotFoundException {
        return getKeyStore(KeyStoreType.PKCS12, certPath, pwd);
    }

    // 从证书中加载 密钥 库
    public static KeyStore getKeyStore(String keyStoreType, String certPath, char[] pwd) throws FileNotFoundException {
        BufferedInputStream bis = null;
        try {
            InputStream fis = new FileInputStream(certPath);
            bis = new BufferedInputStream(fis);
            KeyStore ks = KeyStore.getInstance(keyStoreType);
            ks.load(fis, pwd);
            return ks;
        }
        catch(FileNotFoundException e) {
            throw e;
        }
        catch(KeyStoreException e) {
            throw new RuntimeException("PKCS12证书加载异常", e);
        }
        catch(NoSuchAlgorithmException e) {
            throw new RuntimeException("PKCS12证书参数错误", e);
        }
        catch(CertificateException e) {
            throw new RuntimeException("PKCS12证书异常", e);
        }
        catch(IOException e) {
            throw new RuntimeException("文件读取异常", e);
        } finally {
            if (bis != null) {
                ZKStreamUtils.closeStream(bis);
            }
        }
    }

    /**
     * 获取 X509 证书，一般用于读取微信平台证书
     * 
     * 参考资料
     * https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay7_0.shtml
     *
     * @param certPath
     *            证书文件路径 (required)
     * @return X509证书
     * @throws FileNotFoundException
     */
    public static X509Certificate getX509Certificate(String certPath) throws FileNotFoundException {

        try {
            X509Certificate cert = getCertificate(CertType.X509, certPath);
            cert.checkValidity();
            return cert;
        }
        catch(CertificateExpiredException e) {
            throw new RuntimeException("证书已过期", e);
        }
        catch(CertificateNotYetValidException e) {
            throw new RuntimeException("证书尚未生效", e);
        }
    }

    // 读取证书
    @SuppressWarnings("unchecked")
    public static <T> T getCertificate(String certType, String certPath) throws FileNotFoundException {

        BufferedInputStream bis = null;

        try {
            File f =  new File(certPath);
            if(!f.exists() || f.isDirectory()) {
                // 文件不存在
                
            }
            InputStream fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);
            CertificateFactory cf = CertificateFactory.getInstance(certType);
            Certificate cert = cf.generateCertificate(bis);
            return (T) cert;
        }
        catch(CertificateException e) {
            throw new RuntimeException("无效的证书文件", e);
        } finally {
            if (bis != null) {
                ZKStreamUtils.closeStream(bis);
            }
        }
    }

    /**
     * 获取证书私钥
     * 
     * 参考资料
     * https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay7_1.shtml
     *
     * @param certPath
     *            私钥文件路径 (required)
     * @return 私钥对象
     * @throws IOException
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static PrivateKey getRSAPrivateKey(String certPath) throws FileNotFoundException {

        try {
            String keyContent = new String(ZKFileUtils.readFile(new File(certPath)), "utf-8");
            keyContent = keyContent.replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            return getPrivateKey(KeyAlgorithm.RSA, ZKEncodingUtils.decodeBase64(keyContent));
        }
        catch(UnsupportedEncodingException e) {
            throw new RuntimeException("API密钥字符编码异常", e);
        }
        catch(FileNotFoundException e) {
            throw e;
        }
        catch(IOException e) {
            throw new RuntimeException("API密钥加载异常", e);
        }

    }

    // 将 bytes 加载成 私钥；
    public static PrivateKey getPrivateKey(String algorithm, byte[] base64Bytes) {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(new PKCS8EncodedKeySpec(base64Bytes));
        }
        catch(NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        }
        catch(InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }
    
    /****************************************************************/
    /** 微信支付中 参考微信官网的一些方法 ********************************/
    /****************************************************************/

    static final int KEY_LENGTH_BYTE = 32;

    static final int TAG_LENGTH_BIT = 128;

    /**
     * 回调内容解密
     * 
     * 参考资料
     * https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_2.shtml
     *
     * @Title: decryptToString
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 20, 2021 5:08:20 PM
     * @param ciphertext
     * @param apiv3Aeskey
     * @param associatedData
     * @param nonce
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     * @return String
     */
    public static String decryptToString(String ciphertext, byte[] apiv3Aeskey, byte[] associatedData, byte[] nonce) {
        try {
            return new String(decrypt(ciphertext, apiv3Aeskey, associatedData, nonce), "utf-8");
        }
        catch(UnsupportedEncodingException e) {
            throw new RuntimeException("回调解密字符编码异常", e);
        }
    }

    public static byte[] decrypt(String ciphertext, byte[] apiv3Aeskey, byte[] associatedData, byte[] nonce) {
        try {
            if (apiv3Aeskey.length != KEY_LENGTH_BYTE) {
                log.error("[>_<:20210223-1448-001] 无效的ApiV3Key，长度必须为32个字节; length:{} - {} ", apiv3Aeskey.length,
                        apiv3Aeskey);
                throw new IllegalArgumentException("无效的ApiV3Key，长度必须为32个字节");
            }

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            SecretKeySpec key = new SecretKeySpec(apiv3Aeskey, "AES");
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce);

            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associatedData);

            return cipher.doFinal(ZKEncodingUtils.decodeBase64(ciphertext));

        }
        catch(NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException("回调解密参数错误", e);
        }
        catch(InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException("无效 aes 密钥", e);
        }
        catch(IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalArgumentException("回调解密 aes 密钥串的长度错误", e);
        }
    }

    /**
     * 敏感信息加密
     * 
     * 参考资料
     * https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_3.shtml
     *
     * @Title: rsaEncryptOAEP
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 20, 2021 5:10:19 PM
     * @param message
     * @param certificate
     * @return
     * @throws IllegalBlockSizeException
     * @throws IOException
     * @return String
     */
    public static String rsaEncryptOAEPToString(String message, X509Certificate certificate)
            throws IllegalBlockSizeException, IOException {
        return ZKEncodingUtils.encodeBase64ToStr(rsaEncryptOAEP(message, certificate));
//      Base64.getEncoder().encodeToString(cipherdata);
    }

    public static byte[] rsaEncryptOAEP(String message, X509Certificate certificate) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());

            byte[] data = message.getBytes("utf-8");
            return cipher.doFinal(data);
        }
        catch(NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("当前Java环境不支持RSA v1.5/OAEP", e);
        }
        catch(InvalidKeyException e) {
            throw new RuntimeException("无效的证书", e);
        }
        catch(IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("加密原串的长度不能超过214字节");
        }
        catch(UnsupportedEncodingException e) {
            throw new RuntimeException("加密字符编码异常", e);
        }
    }

    /**
     * 敏感信息解密
     *
     * 参考资料
     * https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay4_3.shtml
     * 
     * @Title: rsaDecryptOAEP
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 20, 2021 5:10:40 PM
     * @param ciphertext
     * @param privateKey
     * @return
     * @throws BadPaddingException
     * @throws IOException
     * @return String
     */
    public static String rsaDecryptOAEPToString(String ciphertext, PrivateKey privateKey) {
        try {
            return new String(rsaDecryptOAEP(ciphertext, privateKey), "utf-8");
        }
        catch(UnsupportedEncodingException e) {
            throw new RuntimeException("解密字符编码异常", e);
        }
    }

    public static byte[] rsaDecryptOAEP(String ciphertext, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] data = ZKEncodingUtils.decodeBase64(ciphertext);
//                    Base64.getDecoder().decode(ciphertext);
            return cipher.doFinal(data);
        }
        catch(NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA v1.5/OAEP", e);
        }
        catch(InvalidKeyException e) {
            throw new RuntimeException("无效的私钥", e);
        }
        catch(BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException("解密失败");
        }
    }

}
