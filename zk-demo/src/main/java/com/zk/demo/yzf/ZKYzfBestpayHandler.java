package com.zk.demo.yzf;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.util.encoders.Base64;

import com.zk.demo.yzf.common.BestpayResult;
import com.zk.demo.yzf.utils.CertificateUtil;
import com.zk.demo.yzf.utils.TradeCertificate;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@SuppressWarnings("deprecation")
public class ZKYzfBestpayHandler {

	/**
     * 字符集编码采用UTF-8
     */
    private static final String ENCODING = "UTF-8";
    /**
     * 生成AES加密所需Key的算法
     */
    private static final String KEY_ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式,Java6.0支持PKCS5Padding填充方式,BouncyCastle支持PKCS7Padding填充方式
     */
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    private static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";

    public static final String SIGNATURE_ALGORITHM_SHA1 = "SHA1withRSA";
    public static final String SIGNATURE_ALGORITHM_SHA256 = "SHA256withRSA";

    public static byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};


    private TradeCertificate tradeCertificate;

    public TradeCertificate getTradeCertificate() {
        return tradeCertificate;
    }

    public void setTradeCertificate(TradeCertificate tradeCertificate) {
        this.tradeCertificate = tradeCertificate;
    }

    public ZKYzfBestpayHandler(TradeCertificate tradeCertificate) {
        this.tradeCertificate = tradeCertificate;
    }

    /**
     * 函数功能：敏感信息加密,AES算法为AES/CBC/PKCS5Padding
     * 参数：key，16位随机字符串
     * 参数：tobeEncoded，待加密的字符串，如银行卡号，V5版中的数字信封明文
     * 参数：iv,AES加密用到的IV值，V3，V4版本接口为固定值1234567892546398，V5版本接口随商户证书一起提供
     * *
     */

    public String encode(String key, String tobeEncoded, String iv) throws Exception {
        //1.AES加密
        Key k = new SecretKeySpec(key.getBytes(ENCODING), KEY_ALGORITHM);
        IvParameterSpec ivs = new IvParameterSpec(iv.getBytes());
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k, ivs);
        byte[] bytes = cipher.doFinal(tobeEncoded.getBytes(ENCODING));
        //2.BASE64编码
        return new String(Base64.encode(bytes));
    }

    /**
     * 敏感信息解密，如银行卡等信息
     */

    public String decode(String key, String tobeDecode, String iv) throws Exception {
        //BASE64解码
        byte[] base64Bytes = Base64.decode(tobeDecode);
        Key k = new SecretKeySpec(key.getBytes(ENCODING), KEY_ALGORITHM);
        IvParameterSpec ivs = new IvParameterSpec(iv.getBytes());
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, k, ivs);
        byte[] bytes = cipher.doFinal(base64Bytes);
        return new String(bytes);

    }

    /**
     * 函数功能 :RSA非对称加密，用于对16位的AES秘钥进行加密,算法为RSA/ECB/PKCS1Padding
     * 参数：TobeEncryptMsg待加密的字符串，如16位AES秘钥
     */

    public String rsaEncode(String TobeEncryptMsg) {
        //获取公钥
        Cipher instance;
        try {
            instance = Cipher.getInstance(RSA_ALGORITHM);
            instance.init(Cipher.ENCRYPT_MODE, tradeCertificate.getBestpayPublicKey());
            byte[] bytes = instance.doFinal(TobeEncryptMsg.getBytes());
            return new String(Base64.encode(bytes));
        } catch (Exception e) {
            throw new RuntimeException("RSA加密失败", e);
        }

    }

    /**
     * 函数功能：使用商户私钥加签
     * 参数：tobeSigned，待加签的字符串，V3，版为json字符串，V4，V5版为用&连接起来的key=value键值对
     * 参数：algorithm,加签算法，V3，V4版本为SHA1withRSA，V5版本为SHA256withRSA
     */

    public String sign(String tobeSigned, String algorithm) throws GeneralSecurityException, UnsupportedEncodingException {
        PrivateKey privateKey = tradeCertificate.getMerchantPrivateKey();
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(tobeSigned.getBytes("UTF-8"));
        byte[] signBytes = signature.sign();
        return new String(Base64.encode(signBytes));
    }

    /**
     * 函数功能：使用翼支付公钥对翼支付响应报文验签
     * 参数：plainText，待验签的字符串，V3为翼支付响应的data对应json传，V4，V5版为将翼支付响应报文中除sign以外的值用&连接起来的key=value键值对
     * 参数：sign：翼支付响应的签文sign
     * 参数：algorithm,加签算法，V3，V4版本为SHA1withRSA，V5版本为SHA256withRSA
     */



    public boolean verify(String plainText, String sign, String algorithm) {

        try {
            PublicKey publicKey = tradeCertificate.getBestpayPublicKey();
            Signature verify = Signature.getInstance(algorithm);
            verify.initVerify(publicKey);
            verify.update(plainText.getBytes(ENCODING));
            return verify.verify(Base64.decode(sign));
        } catch (Exception e) {
            throw new RuntimeException("验签失败", e);
        }
    }

    public String AES_Encode(String str, String key) throws Exception {
        byte[] textBytes = str.getBytes("UTF-8");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), KEY_ALGORITHM);
        Cipher cipher = null;
        cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        return new String(Base64.encode(cipher.doFinal(textBytes)));
    }

    public BestpayResult<String> doService(String Url, String request) {
//        log.info("请求翼支付的URL地址：" + Url);
//        log.info("发送给翼支付的http请求报文:" + request);
        HttpPost httpPost = new HttpPost(Url);
        httpPost.setHeader("Content-Type", "application/json");
        StringEntity se = new StringEntity(request, "utf-8");
        httpPost.setEntity(se);
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        try {
            HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
            int code = httpResponse.getStatusLine().getStatusCode();
            if (200 == code) {
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    String responseStr = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
//                    log.info("收到翼支付的http响应报文:" + responseStr);
                    return new BestpayResult<>(String.valueOf(code), "调用Http服务成功!", responseStr);
                } else {
//                    log.info("收到翼支付的http响应,但报文为空");
                    return new BestpayResult<>(String.valueOf(code), "调用Http服务成功!", null);
                }
            } else {
//                log.info("调用翼支付Http请求失败，失败码为：" + code);
                return new BestpayResult<>(String.valueOf(code), "调用Http服务失败!", null);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BestpayResult<>("999", "调用Http服务失败!位置异常", null);
        } finally {
            if (closeableHttpClient != null) {
                try {
                    closeableHttpClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings({ "resource" })
    public BestpayResult<String> doGetService(String Url, String param) {
        //首先需要先创建一个DefaultHttpClient的实例
        HttpGet httpGet = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            //先创建一个HttpGet对象,传入目标的网络地址,然后调用HttpClient的execute()方法即可:
            httpGet = new HttpGet(new StringBuilder(Url).append("?").append(param).toString());
            HttpResponse response = httpClient.execute(httpGet);
            //通过HttpResponse 的getEntity()方法获取返回信息
            int code = response.getStatusLine().getStatusCode();
            if (code != 200) {
                return new BestpayResult<>(String.valueOf(code), "调用HttpGet请求失败");
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line = br.readLine();
                StringBuilder sb = new StringBuilder();
                while (line != null) {
                    sb.append(line + "\n");
                    line = br.readLine();
                }
                br.close();
                is.close();
                return new BestpayResult<>("200000", "成功", sb.toString());
            }
            return new BestpayResult<>("200999", "HttpGet响应entity内容为空");

        } catch (Exception e) {
            e.printStackTrace();
            return new BestpayResult<>("999999", "HttpGt请求未知异常");
        } finally {
            httpGet.abort();

        }

    }

    @SuppressWarnings({ "resource" })
    public BestpayResult<String> doGetService(String Url) {
        //首先需要先创建一个DefaultHttpClient的实例
        HttpGet httpGet = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            //先创建一个HttpGet对象,传入目标的网络地址,然后调用HttpClient的execute()方法即可:
            httpGet = new HttpGet(Url);
            HttpResponse response = httpClient.execute(httpGet);
            //通过HttpResponse 的getEntity()方法获取返回信息
            int code = response.getStatusLine().getStatusCode();
            if (code != 200) {
                return new BestpayResult<>(String.valueOf(code), "调用HttpGet请求失败");
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line = br.readLine();
                StringBuilder sb = new StringBuilder();
                while (line != null) {
                    sb.append(line + "\n");
                    line = br.readLine();
                }
                br.close();
                is.close();
                return new BestpayResult<>("200000", "成功", sb.toString());
            }
            return new BestpayResult<>("200999", "HttpGet响应entity内容为空");

        } catch (Exception e) {
            e.printStackTrace();
            return new BestpayResult<>("999999", "HttpGt请求未知异常");
        } finally {
            httpGet.abort();

        }

    }

    @SuppressWarnings("unchecked")
    public boolean doVerify(JSONObject response) {
        Iterator<String> sIterator = response.keys();
        StringBuffer sb = new StringBuffer();
        String sign = null;
        while (sIterator.hasNext()) {
            String key = sIterator.next();
            Object value = response.get(key);
            if ("sign".equals(key)) {
                sign = (String) value;
            } else {
                sb.append("&").append(key).append("=").append(value);

            }

        }
        String tobeVerify = sb.substring(1);
        System.out.println(tobeVerify);
//        log.info("待验签tobeVerify：" + tobeVerify);

        try {
            return verify(tobeVerify, sign, ZKYzfBestpayHandler.SIGNATURE_ALGORITHM_SHA1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    //开放平台2.0版本接口验签使用
    @SuppressWarnings("unchecked")
    public boolean doVerifySHA256withRSA(JSONObject response) {

        Iterator<String> sIterator = response.keys();
        StringBuffer sb = new StringBuffer();
        String sign = null;
        TreeMap<String, Object> map = new TreeMap<String, Object>();
        //jsonObject 转换成treeMap key有序
        while (sIterator.hasNext()) {
            String key = sIterator.next();
            Object value = response.get(key);
            map.put(key, value);
            System.out.println("key：" + key +",value：" + value);
        }
        for (Object mapKey : map.keySet()) {
            Object mapValue = map.get(mapKey);
            if ("sign".equals(mapKey)) {
                sign = (String) mapValue;
            } else {
                sb.append("&").append(mapKey).append("=").append(mapValue);

            }
        }
        String tobeVerify = sb.substring(1);
        System.out.println(tobeVerify);
//        log.info("待验签tobeVerify：" + tobeVerify);
        try {
            return verify(tobeVerify, sign, ZKYzfBestpayHandler.SIGNATURE_ALGORITHM_SHA256);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @SuppressWarnings("unchecked")
    public boolean doVerify2(JSONObject response) {

        Iterator<String> sIterator = response.keys();
        StringBuffer sb = new StringBuffer();
        String sign = null;
        TreeMap<String, Object> map = new TreeMap<String, Object>();

        //jsonObject 转换成treeMap key有序
        while (sIterator.hasNext()) {
            String key = sIterator.next();
            Object value = response.get(key);
            if (value instanceof JSONObject) {
                value = sortJSon((JSONObject) value);
            }
            map.put(key, value);
        }

        for (Object mapKey : map.keySet()) {
            Object mapValue = map.get(mapKey);
            if ("sign".equals(mapKey)) {
                sign = (String) mapValue;
            } else {
                sb.append("&").append(mapKey).append("=").append(mapValue);

            }
        }
        String tobeVerify = sb.substring(1);
//        log.info("待验签tobeVerify：" + tobeVerify);

        try {
            return verify(tobeVerify, sign, ZKYzfBestpayHandler.SIGNATURE_ALGORITHM_SHA1);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 对result 里面json 多层嵌套实现每层 进行key字母排序
     *
     * @param json
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private JSONObject sortJSon(JSONObject json) {
        Iterator<String> iteratorKeys = json.keys();
        SortedMap map = new TreeMap();
        while (iteratorKeys.hasNext()) {
            String key = iteratorKeys.next().toString();
            Object value = json.get(key);
            if (value instanceof JSONObject) {
                value = sortJSon((JSONObject) value);
            }
            map.put(key, value);
        }
        JSONObject target = JSONObject.fromObject(map);
        return target;

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean doVerify3(String response) {

        JsonConfig config = new JsonConfig();
        config.setIgnoreDefaultExcludes(true);
        JSONObject jsonObject = JSONObject.fromObject(response, config);
        Iterator<String> sIterator = jsonObject.keys();
        SortedMap map = new TreeMap<String, Object>();
        StringBuffer sb = new StringBuffer();
        String sign = null;
        while (sIterator.hasNext()) {
            String keyTemp = sIterator.next();
            Object valueTemp = jsonObject.get(keyTemp);
            map.put(keyTemp, valueTemp);
        }
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            if ("sign".equals(key)) {
                sign = (String) value;
            } else {
                sb.append("&").append(key).append("=").append(value);
            }
        }
        String tobeVerify = sb.substring(1);
        System.out.println("待验签tobeVerify：" + tobeVerify);

        try {
            return verify(tobeVerify, sign, ZKYzfBestpayHandler.SIGNATURE_ALGORITHM_SHA1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String doSign(TreeMap<String, String> map) {
        StringBuilder signData = new StringBuilder();
        for (String in : map.keySet()) {
            String str = map.get(in);//得到每个key多对用value的值
            signData.append(in).append("=").append(str).append("&");
        }
        String tobeSigned = signData.deleteCharAt(signData.length() - 1).toString();
//        log.info("加签原文：" + tobeSigned);
        try {
            String sign = sign(tobeSigned, ZKYzfBestpayHandler.SIGNATURE_ALGORITHM_SHA256);
            return sign;
        } catch (Exception e) {
//            log.info("加签服务异常");
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 使用公钥RSA加密
     *
     * @param content        明文
     * @param yzf_public_key 公钥
     * @param input_charset  编码格式
     * @return 解密后的字符串
     */
    public static String rsa_encrypt(String content, String yzf_public_key, String input_charset) throws Exception {

        String str = null;
        ByteArrayOutputStream writer = null;
        try {
            PublicKey pubKey = CertificateUtil.getRsaPublicKey(yzf_public_key);

            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            // 设置编码格式
            InputStream ins = new ByteArrayInputStream(content.getBytes(input_charset));
            writer = new ByteArrayOutputStream();
            byte[] buf = new byte[128];
            int bufl;
            while ((bufl = ins.read(buf)) != -1) {
                byte[] block = null;

                if (buf.length == bufl) {
                    block = buf;
                } else {
                    block = new byte[bufl];
                    for (int i = 0; i < bufl; i++) {
                        block[i] = buf[i];
                    }
                }
                writer.write(cipher.doFinal(block));
            }
            str = new String(Base64.encode(writer.toByteArray()));
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                writer = null;
            }
        }
        return str;
    }
	
}
