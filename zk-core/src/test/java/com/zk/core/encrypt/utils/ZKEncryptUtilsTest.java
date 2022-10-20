/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKEncryptUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.core.crypto.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 4:20:30 PM 
 * @version V1.0   
*/
package com.zk.core.encrypt.utils;

import java.util.Arrays;

import org.junit.Test;

import com.zk.core.utils.ZKEncodingUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKEncryptUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKEncryptUtilsTest {

    /**
     * * MAC算法可选以下多种算法
     * 
     * <pre>
     *  
     * HmacMD5  
     * HmacSHA1  
     * HmacSHA256  
     * HmacSHA384  
     * HmacSHA512
     * </pre>
     */
    @Test
    public void testEncryptHmac() {
        try {
            byte[] source;
            byte[] key;
            String hmacMode;
            byte[] result;

            source = "测试用 key 加密生成密文!".getBytes();
            key = "测试密钥".getBytes();

            hmacMode = "HmacMD5";
            System.out.println(hmacMode + " key->" + new String(key)
                    + " ------------------------------------------------------------------------");
            result = ZKEncryptUtils.encryptHmac(source, key, hmacMode);
            System.out.println(String.format("[^_^: 20171013-0807-001] result->%s", ZKEncodingUtils.encodeHex(result)));
            TestCase.assertNotNull(ZKEncodingUtils.encodeHex(result));
            TestCase.assertTrue(Arrays.equals(result, ZKEncryptUtils.encryptHmac(source, key, hmacMode)));
            TestCase.assertFalse(
                    Arrays.equals(result, ZKEncryptUtils.encryptHmac(source, "测试密钥2".getBytes(), hmacMode)));
            hmacMode = "HmacSHA1";
            System.out.println(hmacMode + " key->" + new String(key)
                    + " ------------------------------------------------------------------------");
            result = ZKEncryptUtils.encryptHmac(source, key, hmacMode);
            System.out.println(String.format("[^_^: 20171013-0807-001] result->%s", ZKEncodingUtils.encodeHex(result)));
            TestCase.assertNotNull(ZKEncodingUtils.encodeHex(result));
            TestCase.assertTrue(Arrays.equals(result, ZKEncryptUtils.encryptHmac(source, key, hmacMode)));
            TestCase.assertFalse(
                    Arrays.equals(result, ZKEncryptUtils.encryptHmac(source, "测试密钥2".getBytes(), hmacMode)));
            hmacMode = "HmacSHA256";
            System.out.println(hmacMode + " key->" + new String(key)
                    + " ------------------------------------------------------------------------");
            result = ZKEncryptUtils.encryptHmac(source, key, hmacMode);
            System.out.println(String.format("[^_^: 20171013-0807-001] result->%s", ZKEncodingUtils.encodeHex(result)));
            TestCase.assertNotNull(ZKEncodingUtils.encodeHex(result));
            TestCase.assertTrue(Arrays.equals(result, ZKEncryptUtils.encryptHmac(source, key, hmacMode)));
            TestCase.assertFalse(
                    Arrays.equals(result, ZKEncryptUtils.encryptHmac(source, "测试密钥2".getBytes(), hmacMode)));
            hmacMode = "HmacSHA384";
            System.out.println(hmacMode + " key->" + new String(key)
                    + " ------------------------------------------------------------------------");
            result = ZKEncryptUtils.encryptHmac(source, key, hmacMode);
            System.out.println(String.format("[^_^: 20171013-0807-001] result->%s", ZKEncodingUtils.encodeHex(result)));
            TestCase.assertNotNull(ZKEncodingUtils.encodeHex(result));
            TestCase.assertTrue(Arrays.equals(result, ZKEncryptUtils.encryptHmac(source, key, hmacMode)));
            TestCase.assertFalse(
                    Arrays.equals(result, ZKEncryptUtils.encryptHmac(source, "测试密钥2".getBytes(), hmacMode)));
            hmacMode = "HmacSHA512";
            System.out.println(hmacMode + " key->" + new String(key)
                    + " ------------------------------------------------------------------------");
            result = ZKEncryptUtils.encryptHmac(source, key, hmacMode);
            System.out.println(String.format("[^_^: 20171013-0807-001] result->%s", ZKEncodingUtils.encodeHex(result)));
            TestCase.assertNotNull(ZKEncodingUtils.encodeHex(result));
            TestCase.assertTrue(Arrays.equals(result, ZKEncryptUtils.encryptHmac(source, key, hmacMode)));
            TestCase.assertFalse(
                    Arrays.equals(result, ZKEncryptUtils.encryptHmac(source, "测试密钥2".getBytes(), hmacMode)));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * *可选以下多种算法
     * 
     * <pre>
     *  
     * SHA(SHA-1)
     * MD5
     * </pre>
     */
    @Test
    public void testEncryptDigest() {
        try {
            byte[] data;
            byte[] salt;
            String digestMode;
            byte[] result;

            data = "测试用 key 加密生成密文!".getBytes();

            digestMode = "SHA";
            System.out
                    .println(digestMode + " ------------------------------------------------------------------------");
            result = ZKEncryptUtils.encryptDigest(data, null, digestMode);
            System.out.println(String.format("[^_^: 20171013-0807-002] result->%s", ZKEncodingUtils.encodeHex(result)));
            TestCase.assertNotNull(ZKEncodingUtils.encodeHex(result));
            TestCase.assertTrue(Arrays.equals(result, ZKEncryptUtils.encryptDigest(data, null, digestMode)));
            digestMode = "MD5";
            System.out
                    .println(digestMode + " ------------------------------------------------------------------------");
            result = ZKEncryptUtils.encryptDigest(data, null, digestMode);
            System.out.println(String.format("[^_^: 20171013-0807-002] result->%s", ZKEncodingUtils.encodeHex(result)));
            TestCase.assertNotNull(ZKEncodingUtils.encodeHex(result));
            TestCase.assertTrue(Arrays.equals(result, ZKEncryptUtils.encryptDigest(data, null, digestMode)));

            salt = "测试盐".getBytes();
            digestMode = "SHA";
            System.out.println(digestMode + " salt->" + new String(salt)
                    + " ------------------------------------------------------------------------");
            result = ZKEncryptUtils.encryptDigest(data, salt, digestMode);
            System.out.println(String.format("[^_^: 20171013-0807-002] result->%s", ZKEncodingUtils.encodeHex(result)));
            TestCase.assertNotNull(ZKEncodingUtils.encodeHex(result));
            TestCase.assertTrue(Arrays.equals(result, ZKEncryptUtils.encryptDigest(data, salt, digestMode)));
            TestCase.assertFalse(
                    Arrays.equals(result, ZKEncryptUtils.encryptDigest(data, "测试密钥2".getBytes(), digestMode)));
            digestMode = "MD5";
            System.out.println(digestMode + " salt->" + new String(salt)
                    + " ------------------------------------------------------------------------");
            result = ZKEncryptUtils.encryptDigest(data, salt, digestMode);
            System.out.println(String.format("[^_^: 20171013-0807-002] result->%s", ZKEncodingUtils.encodeHex(result)));
            TestCase.assertNotNull(ZKEncodingUtils.encodeHex(result));
            TestCase.assertTrue(Arrays.equals(result, ZKEncryptUtils.encryptDigest(data, salt, digestMode)));
            TestCase.assertFalse(
                    Arrays.equals(result, ZKEncryptUtils.encryptDigest(data, "测试密钥2".getBytes(), digestMode)));

            digestMode = "SHA";
            data = "测试 javag 与 JS 加密结果对比".getBytes();
            System.out.println(digestMode + " salt->" + new String(data)
                    + " ------------------------------------------------------------------------");
            result = ZKEncryptUtils.encryptDigest(data, null, digestMode);
            System.out.println(String.format("[^_^: 20171013-0807-002] '测试 javag 与 JS 加密结果对比' -> %s",
                    ZKEncodingUtils.encodeHex(result)));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testMd5Encode() {
        try {

            // admin:21232f297a57a5a743894a0e4a801fc3
            String source = "";
            byte[] result;

//            source = "gf@sW2022";
            source = "test";
            result = ZKEncryptUtils.md5Encode(source.getBytes());
            TestCase.assertNotNull(result);

            System.out.println(" ------------------------------------------------------------------------");
            System.out.println(
                    String.format("[^_^: 2022-0704-001] '测试 md5Encode' -> %s", ZKEncodingUtils.encodeHex(result)));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
