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
 * @Title: ZKEncryptCipherUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.core.crypto.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 4:15:16 PM 
 * @version V1.0   
*/
package com.zk.core.encrypt.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Test;

import com.zk.core.encrypt.utils.ZKEncryptCipherUtils.MODE;
import com.zk.core.utils.ZKEncodingUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKEncryptCipherUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKEncryptCipherUtilsTest {


    @Test
    public void test() {
        try {
//            5113011990010181111
//            8c2da4c769828fcfa77aedb690999cf9
//            80cfe03525bb2b8d43d62ff369e95334cd1facfe4bbb800c
            String encryptStr; // 密文
            String secretKeyStr; // 密钥
            String decryptStr;  // 明文
            String resStr;

            encryptStr = "80cfe03525bb2b8d43d62ff369e95334cd1facfe4bbb800c";
            secretKeyStr = "8c2da4c769828fcfa77aedb690999cf9";
            decryptStr = "5113011990010181111";
            System.out.println("[^_^:20221213-1936-001] secretKeyStr.length: " + ZKEncodingUtils.decodeHex(secretKeyStr).length);

            DESKeySpec desKeySpec = new DESKeySpec(secretKeyStr.getBytes());

            resStr = ZKEncodingUtils.encodeHex(ZKEncryptCipherUtils.encrypt(decryptStr.getBytes(),
                    desKeySpec.getKey(),
                    MODE.DES));
            System.out.println("[^_^:20221213-1931-001] res: " + resStr);
            TestCase.assertEquals(encryptStr, resStr);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    private void encAndDec(String decryptStr, String mode, int keysize) throws Exception {
        String encryptStr;
        SecretKey secretKey;

        secretKey = ZKEncryptCipherUtils.generateKey(mode, keysize, null);
        encryptStr = ZKEncodingUtils
                .encodeHex(ZKEncryptCipherUtils.encrypt(decryptStr.getBytes(), secretKey.getEncoded(), mode));
        System.out.println("^_^:2171013-1342-001 secretKey->" + secretKey + ";   encryptStr->" + encryptStr);
        TestCase.assertEquals(decryptStr, new String(
                ZKEncryptCipherUtils.decrypt(ZKEncodingUtils.decodeHex(encryptStr), secretKey.getEncoded(), mode)));

        encryptStr = ZKEncodingUtils.encodeHex(ZKEncryptCipherUtils.encrypt(decryptStr.getBytes(), secretKey));
        System.out.println("^_^:2171013-1342-002 secretKey->" + secretKey + ";   encryptStr->" + encryptStr);
        TestCase.assertEquals(decryptStr,
                new String(ZKEncryptCipherUtils.decrypt(ZKEncodingUtils.decodeHex(encryptStr), secretKey)));
    }

    @Test
    public void testEncAndDec() {
        try {
            String decryptStr;
            int keysize = 0;

            decryptStr = "测试加Cipher 加解密！";
            // DES key size must be equal to 56
            keysize = 56;
            System.out.println(ZKEncryptCipherUtils.MODE.DES + " ----------------------------");
            encAndDec(decryptStr, ZKEncryptCipherUtils.MODE.DES, keysize);

            // DESede(TripleDES) key size must be equal to 112 or 168
            keysize = 112;
            System.out.println(ZKEncryptCipherUtils.MODE.DESede + " ----------------------------");
            encAndDec(decryptStr, ZKEncryptCipherUtils.MODE.DESede, keysize);

            System.out.println(ZKEncryptCipherUtils.MODE.TripleDES + " ----------------------------");
            encAndDec(decryptStr, ZKEncryptCipherUtils.MODE.TripleDES, keysize);

            // AES key size must be equal to 128, 192 or 256,but 192 and 256
            // bits may not be available
            keysize = 128;
            System.out.println(ZKEncryptCipherUtils.MODE.AES + " ----------------------------");
            encAndDec(decryptStr, ZKEncryptCipherUtils.MODE.AES, keysize);

            // Blowfish key size must be multiple of 8, and can only range from
            // 32 to 448 (inclusive)
            keysize = 32;
            System.out.println(ZKEncryptCipherUtils.MODE.Blowfish + " ----------------------------");
            encAndDec(decryptStr, ZKEncryptCipherUtils.MODE.Blowfish, keysize);

            // RC2 key size must be between 40 and 1024 bits
            keysize = 128;
            System.out.println(ZKEncryptCipherUtils.MODE.RC2 + " ----------------------------");
            encAndDec(decryptStr, ZKEncryptCipherUtils.MODE.RC2, keysize);

            // RC4(ZKCFOUR) key size must be between 40 and 1024 bits
            keysize = 128;
            System.out.println(ZKEncryptCipherUtils.MODE.RC4 + " ----------------------------");
            encAndDec(decryptStr, ZKEncryptCipherUtils.MODE.RC4, keysize);

            // DESede(TripleDES) key size must be equal to 112 or 168
            keysize = 112;
            System.out.println(ZKEncryptCipherUtils.MODE.DESede + " ----------------------------");
            encAndDec(decryptStr, ZKEncryptCipherUtils.MODE.DESede, keysize);

            System.out.println(ZKEncryptCipherUtils.MODE.TripleDES + " ----------------------------");
            encAndDec(decryptStr, ZKEncryptCipherUtils.MODE.TripleDES, keysize);

            // AES key size must be equal to 128, 192 or 256,but 192 and 256
            // bits may not be available
            keysize = 128;
            System.out.println(ZKEncryptCipherUtils.MODE.AES + " ----------------------------");
            encAndDec(decryptStr, ZKEncryptCipherUtils.MODE.AES, keysize);

            // Blowfish key size must be multiple of 8, and can only range from
            // 32 to 448 (inclusive)
            keysize = 32;
            System.out.println(ZKEncryptCipherUtils.MODE.Blowfish + " ----------------------------");
            encAndDec(decryptStr, ZKEncryptCipherUtils.MODE.Blowfish, keysize);

            // RC2 key size must be between 40 and 1024 bits
            keysize = 128;
            System.out.println(ZKEncryptCipherUtils.MODE.RC2 + " ----------------------------");
            encAndDec(decryptStr, ZKEncryptCipherUtils.MODE.RC2, keysize);

            // RC4(ZKCFOUR) key size must be between 40 and 1024 bits
            keysize = 128;
            System.out.println(ZKEncryptCipherUtils.MODE.RC4 + " ----------------------------");
            encAndDec(decryptStr, ZKEncryptCipherUtils.MODE.RC4, keysize);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    private void encAndDecPadding(String decryptStr, String mode, int keysize, int padding) throws Exception {
        String encryptStr;
        SecretKey secretKey;

        secretKey = ZKEncryptCipherUtils.generateKey(mode, keysize, null);
        encryptStr = ZKEncodingUtils
                .encodeHex(ZKEncryptCipherUtils.encrypt(decryptStr.getBytes(), secretKey.getEncoded(), mode, padding));
        System.out.println("^_^:2171013-1342-001 secretKey->" + secretKey + ";   encryptStr->" + encryptStr);
        TestCase.assertEquals(decryptStr, new String(ZKEncryptCipherUtils.decrypt(ZKEncodingUtils.decodeHex(encryptStr),
                secretKey.getEncoded(), mode, padding)));
    }

    @Test
    public void testEncAndDecPadding() {
        try {
            String decryptStr;
            int keysize = 0;
            int padding = 100000;

            decryptStr = "测试加Cipher 加解密！";
            // DES key size must be equal to 56
            keysize = 56;
            System.out.println(ZKEncryptCipherUtils.MODE.DES + " ----------------------------");
            encAndDecPadding(decryptStr, ZKEncryptCipherUtils.MODE.DES, keysize, padding);

            // DESede(TripleDES) key size must be equal to 112 or 168
            keysize = 112;
            System.out.println(ZKEncryptCipherUtils.MODE.DESede + " ----------------------------");
            encAndDecPadding(decryptStr, ZKEncryptCipherUtils.MODE.DESede, keysize, padding);

            System.out.println(ZKEncryptCipherUtils.MODE.TripleDES + " ----------------------------");
            encAndDecPadding(decryptStr, ZKEncryptCipherUtils.MODE.TripleDES, keysize, padding);

            // AES key size must be equal to 128, 192 or 256,but 192 and 256
            // bits may not be available
            keysize = 128;
            System.out.println(ZKEncryptCipherUtils.MODE.AES + " ----------------------------");
            encAndDecPadding(decryptStr, ZKEncryptCipherUtils.MODE.AES, keysize, padding);

            // Blowfish key size must be multiple of 8, and can only range from
            // 32 to 448 (inclusive)
            keysize = 32;
            System.out.println(ZKEncryptCipherUtils.MODE.Blowfish + " ----------------------------");
            encAndDecPadding(decryptStr, ZKEncryptCipherUtils.MODE.Blowfish, keysize, padding);

            // RC2 key size must be between 40 and 1024 bits
            keysize = 128;
            System.out.println(ZKEncryptCipherUtils.MODE.RC2 + " ----------------------------");
            encAndDecPadding(decryptStr, ZKEncryptCipherUtils.MODE.RC2, keysize, padding);

            // RC4(ZKCFOUR) key size must be between 40 and 1024 bits
            keysize = 128;
            System.out.println(ZKEncryptCipherUtils.MODE.RC4 + " ----------------------------");
            encAndDecPadding(decryptStr, ZKEncryptCipherUtils.MODE.RC4, keysize, padding);

            // DESede(TripleDES) key size must be equal to 112 or 168
            keysize = 112;
            System.out.println(ZKEncryptCipherUtils.MODE.DESede + " ----------------------------");
            encAndDecPadding(decryptStr, ZKEncryptCipherUtils.MODE.DESede, keysize, padding);

            System.out.println(ZKEncryptCipherUtils.MODE.TripleDES + " ----------------------------");
            encAndDecPadding(decryptStr, ZKEncryptCipherUtils.MODE.TripleDES, keysize, padding);

            // AES key size must be equal to 128, 192 or 256,but 192 and 256
            // bits may not be available
            keysize = 128;
            System.out.println(ZKEncryptCipherUtils.MODE.AES + " ----------------------------");
            encAndDecPadding(decryptStr, ZKEncryptCipherUtils.MODE.AES, keysize, padding);

            // Blowfish key size must be multiple of 8, and can only range from
            // 32 to 448 (inclusive)
            keysize = 32;
            System.out.println(ZKEncryptCipherUtils.MODE.Blowfish + " ----------------------------");
            encAndDecPadding(decryptStr, ZKEncryptCipherUtils.MODE.Blowfish, keysize, padding);

            // RC2 key size must be between 40 and 1024 bits
            keysize = 128;
            System.out.println(ZKEncryptCipherUtils.MODE.RC2 + " ----------------------------");
            encAndDecPadding(decryptStr, ZKEncryptCipherUtils.MODE.RC2, keysize, padding);

            // RC4(ZKCFOUR) key size must be between 40 and 1024 bits
            keysize = 128;
            System.out.println(ZKEncryptCipherUtils.MODE.RC4 + " ----------------------------");
            encAndDecPadding(decryptStr, ZKEncryptCipherUtils.MODE.RC4, keysize, padding);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 测试加密是否混淆，同一数据源，密钥 加密出密文不一样，并可解密
     */
    @Test
    public void testCipherPadding() {
        try {
            String decryptStr;
            int keysize = 0;
            int padding = 0;

            decryptStr = "测试加Cipher 加解密！";
            // AES key size must be equal to 128, 192 or 256,but 192 and 256
            // bits may not be available
            keysize = 128;
            decryptStr = "测试加Cipher 加解密！";
            String encryptStr;
            System.out.println(ZKEncryptCipherUtils.MODE.AES + " padding ----------------------------");
            SecretKey secretKey = ZKEncryptCipherUtils.generateKey(ZKEncryptCipherUtils.MODE.AES, keysize, null);

            padding = 1;
            encryptStr = ZKEncodingUtils.encodeHex(ZKEncryptCipherUtils.encrypt(decryptStr.getBytes(),
                    secretKey.getEncoded(), ZKEncryptCipherUtils.MODE.AES, padding));
            System.out.println("encryptStr:" + encryptStr);
            encryptStr = ZKEncodingUtils.encodeHex(ZKEncryptCipherUtils.encrypt(decryptStr.getBytes(),
                    secretKey.getEncoded(), ZKEncryptCipherUtils.MODE.AES, padding));
            System.out.println("encryptStr:" + encryptStr);
            TestCase.assertFalse(encryptStr.equals(ZKEncodingUtils.encodeHex(ZKEncryptCipherUtils
                    .encrypt(decryptStr.getBytes(), secretKey.getEncoded(), ZKEncryptCipherUtils.MODE.AES, padding))));
            System.out.println(
                    "decryptStr:" + new String(ZKEncryptCipherUtils.decrypt(ZKEncodingUtils.decodeHex(encryptStr),
                            secretKey.getEncoded(), ZKEncryptCipherUtils.MODE.AES, padding)));
            TestCase.assertEquals(decryptStr,
                    new String(ZKEncryptCipherUtils.decrypt(ZKEncodingUtils.decodeHex(encryptStr),
                            secretKey.getEncoded(), ZKEncryptCipherUtils.MODE.AES, padding)));

            padding = decryptStr.getBytes().length;
            encryptStr = ZKEncodingUtils.encodeHex(ZKEncryptCipherUtils.encrypt(decryptStr.getBytes(),
                    secretKey.getEncoded(), ZKEncryptCipherUtils.MODE.AES, padding));
            System.out.println("encryptStr:" + encryptStr);
            TestCase.assertFalse(encryptStr.equals(ZKEncodingUtils.encodeHex(ZKEncryptCipherUtils
                    .encrypt(decryptStr.getBytes(), secretKey.getEncoded(), ZKEncryptCipherUtils.MODE.AES, padding))));
            System.out.println(
                    "decryptStr:" + new String(ZKEncryptCipherUtils.decrypt(ZKEncodingUtils.decodeHex(encryptStr),
                            secretKey.getEncoded(), ZKEncryptCipherUtils.MODE.AES, padding)));
            TestCase.assertEquals(decryptStr,
                    new String(ZKEncryptCipherUtils.decrypt(ZKEncodingUtils.decodeHex(encryptStr),
                            secretKey.getEncoded(), ZKEncryptCipherUtils.MODE.AES, padding)));

            padding = 10000;
            encryptStr = ZKEncodingUtils.encodeHex(ZKEncryptCipherUtils.encrypt(decryptStr.getBytes(),
                    secretKey.getEncoded(), ZKEncryptCipherUtils.MODE.AES, padding));
            System.out.println("encryptStr:" + encryptStr);
            TestCase.assertFalse(encryptStr.equals(ZKEncodingUtils.encodeHex(ZKEncryptCipherUtils
                    .encrypt(decryptStr.getBytes(), secretKey.getEncoded(), ZKEncryptCipherUtils.MODE.AES, padding))));
            System.out.println(
                    "decryptStr:" + new String(ZKEncryptCipherUtils.decrypt(ZKEncodingUtils.decodeHex(encryptStr),
                            secretKey.getEncoded(), ZKEncryptCipherUtils.MODE.AES, padding)));
            TestCase.assertEquals(decryptStr,
                    new String(ZKEncryptCipherUtils.decrypt(ZKEncodingUtils.decodeHex(encryptStr),
                            secretKey.getEncoded(), ZKEncryptCipherUtils.MODE.AES, padding)));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * java 与 js 加解密字符串对比
     * 
     * @throws Exception
     */
    @Test
    public void testJsAndJava() throws Exception {

//      String mode = "AES";
//      String secretKey = "76c92fef8f025a82f9d15f3c11c4d395";
//      String decryptStr = "测试 java 与 js 加解密字符串结果对比！";
//      String encryptStr = "570d37e8f44369bdd75ff3085ba61fdab9aee86fe22c28285c0f6df04f2cc4a157dd40569262ea62ca83c6ae386795d7f5eeedd6510e47b813b62376bf0ed535";
//      
//      TestCase.assertEquals(encryptStr, ZKEncodingUtils.encodeHex(ZKEncryptCipherUtils.encrypt(decryptStr.getBytes(), secretKey, mode)));
//      TestCase.assertEquals(decryptStr, new String(ZKEncryptCipherUtils.decrypt(ZKEncodingUtils.decodeHex(encryptStr), secretKey, mode)));
//      
//      byte[] zk = ZKEncryptCipherUtils.decrypt(ZKEncodingUtils.decodeHex(encryptStr), secretKey, mode);
//      System.out.println("[^_^:20171014-1437-001]" + new String(ZKEncodingUtils.encodeBase64(zk), "utf8"));
//      zk = ZKEncodingUtils.decodeHex(encryptStr);
//      System.out.println("[^_^:20171014-1437-002]" + new String(ZKEncodingUtils.encodeBase64(zk), "utf8"));
//      System.out.println("[^_^:20171014-1437-003]" + ZKEncodingUtils.encodeBase62(zk));
//      
//      
//      encryptStr = "e280208b180e0e38dba8aca64c96cd0f4fd7bf3808eedc93e7b9e8c7e7d5dac4621eb2ef8fc7a6aa904d113e9a36d64960d32d7e2aa8a84702381229d6a53690faacc488d70391b83d6bf247b3b82ce5";
//      decryptStr = "{\"id\":[\"1\",\"测试 java 与 js 加解密字符串结果对比！\"],\"index\":1}";
//      Map<String, Object> map = new HashMap<>();
//      map.put("id", new String[]{"1", "测试 java 与 js 加解密字符串结果对比！"});
//      map.put("index", 1l);
//      decryptStr = JsonUtils.writeObjectJson(map);
//      System.out.println("[^_^:20171014-1437-004]" + decryptStr);
//      TestCase.assertEquals(encryptStr, ZKEncodingUtils.encodeHex(ZKEncryptCipherUtils.encrypt(decryptStr.getBytes(), secretKey, mode)));
//      encryptStr = ZKEncodingUtils.encodeHex(ZKEncryptCipherUtils.encrypt(decryptStr.getBytes(), secretKey, mode));
//      System.out.println("[^_^:20171014-1437-005]" + encryptStr);
//      
//      encryptStr = "e280208b180e0e38dba8aca64c96cd0f4fd7bf3808eedc93e7b9e8c7e7d5dac4621eb2ef8fc7a6aa904d113e9a36d64960d32d7e2aa8a84702381229d6a53690faacc488d70391b83d6bf247b3b82ce5";
//      decryptStr = "{\"id\":[\"1\",\"测试 java 与 js 加解密字符串结果对比！\"],\"index\":1}";
//      TestCase.assertEquals(decryptStr, new String(ZKEncryptCipherUtils.decrypt(ZKEncodingUtils.decodeHex(encryptStr), secretKey, mode)));
//      decryptStr = new String(ZKEncryptCipherUtils.decrypt(ZKEncodingUtils.decodeHex(encryptStr), secretKey, mode));
//      System.out.println("[^_^:20171014-1437-006]" + decryptStr);

    }

    @Test
    public void testGenerateKey() {
        try {

            SecretKey secretKey = null;
            String keyStr = null;
            String mode = null;
            int keysize = 0;
            String keySeed = null;

            mode = ZKEncryptCipherUtils.MODE.DES;
            keysize = 56;
            System.out.println(ZKEncryptCipherUtils.MODE.DES + "------------------------------------------- ");
            keySeed = "test";
            secretKey = ZKEncryptCipherUtils.generateKey(mode, keysize, keySeed);
            keyStr = ZKEncodingUtils.encodeHex(secretKey.getEncoded());
            System.out.println(String.format("keySeed:%s; algorithm:%s; format:%s; key:%s", keySeed,
                    secretKey.getAlgorithm(), secretKey.getFormat(), keyStr));
            secretKey = new SecretKeySpec(ZKEncodingUtils.decodeHex(keyStr), mode);
            keyStr = ZKEncodingUtils.encodeHex(secretKey.getEncoded());
            System.out.println(String.format("keySeed:%s; algorithm:%s; format:%s; key:%s", keySeed,
                    secretKey.getAlgorithm(), secretKey.getFormat(), keyStr));

            secretKey = ZKEncryptCipherUtils.generateKey(mode, keysize, keySeed);
            keyStr = ZKEncodingUtils.encodeHex(secretKey.getEncoded());
            System.out.println(String.format("keySeed:%s; algorithm:%s; format:%s; key:%s", keySeed,
                    secretKey.getAlgorithm(), secretKey.getFormat(), keyStr));
            secretKey = new SecretKeySpec(ZKEncodingUtils.decodeHex(keyStr), mode);
            keyStr = ZKEncodingUtils.encodeHex(secretKey.getEncoded());
            System.out.println(String.format("keySeed:%s; algorithm:%s; format:%s; key:%s", keySeed,
                    secretKey.getAlgorithm(), secretKey.getFormat(), keyStr));

            mode = ZKEncryptCipherUtils.MODE.AES;
            keysize = 128;
            System.out.println(ZKEncryptCipherUtils.MODE.AES + "------------------------------------------- ");
            keySeed = "test";
            secretKey = ZKEncryptCipherUtils.generateKey(mode, keysize, keySeed);
            keyStr = ZKEncodingUtils.encodeHex(secretKey.getEncoded());
            System.out.println(String.format("keySeed:%s; algorithm:%s; format:%s; key:%s", keySeed,
                    secretKey.getAlgorithm(), secretKey.getFormat(), keyStr));
            secretKey = new SecretKeySpec(ZKEncodingUtils.decodeHex(keyStr), mode);
            keyStr = ZKEncodingUtils.encodeHex(secretKey.getEncoded());
            System.out.println(String.format("keySeed:%s; algorithm:%s; format:%s; key:%s", keySeed,
                    secretKey.getAlgorithm(), secretKey.getFormat(), keyStr));

            secretKey = ZKEncryptCipherUtils.generateKey(mode, keysize, keySeed);
            keyStr = ZKEncodingUtils.encodeHex(secretKey.getEncoded());
            System.out.println(String.format("keySeed:%s; algorithm:%s; format:%s; key:%s", keySeed,
                    secretKey.getAlgorithm(), secretKey.getFormat(), keyStr));
            secretKey = new SecretKeySpec(ZKEncodingUtils.decodeHex(keyStr), mode);
            keyStr = ZKEncodingUtils.encodeHex(secretKey.getEncoded());
            System.out.println(String.format("keySeed:%s; algorithm:%s; format:%s; key:%s", keySeed,
                    secretKey.getAlgorithm(), secretKey.getFormat(), keyStr));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    //////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        try {

//          AESTest();
//          DesTest();
//          PBETest();
//          KeySaltTest();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void PBETest() throws Exception {

        String keySeed = null;
        byte[] salt;
        byte[] psSalt;
        SecureRandom secureRandom = null;
        int keySize = 128;
        int iterationCount = 27692;
        PBEKeySpec keySpec;
        SecretKey saltKey;
        SecretKeyFactory f;
        String pwd;
        String algorithm;

        String decryptStr;
        String encryptStr;

        keySeed = "keySeed";
        // 生成盐
        System.out.println("生成 salt --------------------------------------------------------");
        secureRandom = new SecureRandom(keySeed.getBytes());
        salt = secureRandom.generateSeed(80);
        System.out.println(String.format("keySeed:%s; algorithm:%s; salt:%s", keySeed, secureRandom.getAlgorithm(),
                ZKEncodingUtils.encodeHex(salt)));

        pwd = "sadfsadfas";
        // 加盐
        System.out.println("加盐 salt --------------------------------------------------------");
        keySpec = new PBEKeySpec(pwd.toCharArray(), salt, iterationCount, keySize);
        System.out.println(String.format("pwd:%s; salt:%s; IterationCount:%s; KeyLength:%s",
                new String(keySpec.getPassword()), ZKEncodingUtils.encodeHex(keySpec.getSalt()),
                keySpec.getIterationCount(), keySpec.getKeyLength()));
        algorithm = "PBEWITHMD5andDES";
        f = SecretKeyFactory.getInstance(algorithm);
        saltKey = f.generateSecret(keySpec);
        System.out.println(String.format("algorithm:%s; algorithm:%s; format:%s; key:%s", algorithm,
                saltKey.getAlgorithm(), saltKey.getFormat(), ZKEncodingUtils.encodeHex(saltKey.getEncoded())));

        decryptStr = "学习一次 - PBE";
        // 密钥、算法都加盐
        psSalt = secureRandom.generateSeed(8);
        System.out.println(String.format("keySeed:%s; algorithm:%s; psSalt:%s", keySeed, secureRandom.getAlgorithm(),
                ZKEncodingUtils.encodeHex(psSalt)));
        PBEParameterSpec ps = new PBEParameterSpec(psSalt, 100);
        System.out.println("密钥 " + algorithm + " 加盐 --------------------------------------------------------");
        encryptStr = ZKEncodingUtils
                .encodeHex(ZKEncryptCipherUtils.cipher(Cipher.ENCRYPT_MODE, saltKey.getAlgorithm(), decryptStr.getBytes(), saltKey, ps));
        System.out.println("encryptStr:" + encryptStr);
        System.out.println("decryptStr:" + new String(
                ZKEncryptCipherUtils.cipher(Cipher.DECRYPT_MODE, saltKey.getAlgorithm(),
                        ZKEncodingUtils.decodeHex(encryptStr), saltKey, ps)));

    }

    public static void AESTest() throws Exception {
        String mode = MODE.AES;
        int keySize = 128;
        SecretKey secretKey = null;
        String keySeed = null;
        byte[] salt;
        SecureRandom secureRandom = null;
        int iterationCount = 27692;
        PBEKeySpec keySpec;
        SecretKey saltKey;
        SecretKeyFactory f;
        String algorithm;

        String decryptStr;
        String encryptStr;

        // 生成 key
        System.out.println("生成 key --------------------------------------------------------");
        keySeed = "keySeed";
        secretKey = ZKEncryptCipherUtils.generateKey(mode, keySize, keySeed);
        System.out.println(String.format("keySeed:%s; algorithm:%s; format:%s; key:%s", keySeed,
                secretKey.getAlgorithm(), secretKey.getFormat(), ZKEncodingUtils.encodeHex(secretKey.getEncoded())));

        // 生成盐
        System.out.println("生成 salt --------------------------------------------------------");
        secureRandom = new SecureRandom(keySeed.getBytes());
        salt = secureRandom.generateSeed(8);
        System.out.println(String.format("keySeed:%s; algorithm:%s; salt:%s", keySeed, secureRandom.getAlgorithm(),
                ZKEncodingUtils.encodeHex(salt)));

        // 加盐
        System.out.println("密钥 加盐 --------------------------------------------------------");
        keySpec = new PBEKeySpec(ZKEncodingUtils.encodeHex(secretKey.getEncoded()).toCharArray(), salt, iterationCount,
                keySize);
        System.out.println(String.format("pwd:%s; salt:%s; IterationCount:%s; KeyLength:%s",
                new String(keySpec.getPassword()), ZKEncodingUtils.encodeHex(keySpec.getSalt()),
                keySpec.getIterationCount(), keySpec.getKeyLength()));

        algorithm = "PBKDF2WithHmacSHA1";
        f = SecretKeyFactory.getInstance(algorithm);
        saltKey = f.generateSecret(keySpec);
        System.out.println(String.format("algorithm:%s; algorithm:%s; format:%s; key:%s", algorithm,
                saltKey.getAlgorithm(), saltKey.getFormat(), ZKEncodingUtils.encodeHex(saltKey.getEncoded())));

        decryptStr = "学习一次 - AES";

        // 不加盐加解密
        System.out.println("不加盐加解密 --------------------------------------------------------");
        encryptStr = ZKEncodingUtils
                .encodeHex(ZKEncryptCipherUtils.cipher(Cipher.ENCRYPT_MODE, secretKey.getAlgorithm(),
                        decryptStr.getBytes(), secretKey));
        System.out.println("encryptStr:" + encryptStr);
        System.out.println("decryptStr:" + new String(
                ZKEncryptCipherUtils.cipher(Cipher.DECRYPT_MODE, secretKey.getAlgorithm(),
                        ZKEncodingUtils.decodeHex(encryptStr), secretKey)));

        // 密钥 PBKDF2WithHmacSHA1 加盐
        secretKey = new SecretKeySpec(saltKey.getEncoded(), mode);
        System.out.println("密钥 " + algorithm + " 加盐 --------------------------------------------------------");
        encryptStr = ZKEncodingUtils
                .encodeHex(ZKEncryptCipherUtils.cipher(Cipher.ENCRYPT_MODE, secretKey.getAlgorithm(),
                        decryptStr.getBytes(), secretKey));
        System.out.println("encryptStr:" + encryptStr);
        System.out.println("decryptStr:" + new String(
                ZKEncryptCipherUtils.cipher(Cipher.DECRYPT_MODE, secretKey.getAlgorithm(),
                        ZKEncodingUtils.decodeHex(encryptStr), secretKey)));

    }

    public static void DesTest() throws Exception {
        String mode = MODE.DES;
        int keySize = 56;
        SecretKey secretKey = null;
        String keySeed = null;
        byte[] salt;
        SecureRandom secureRandom = null;
        int iterationCount = 27692;
        PBEKeySpec keySpec;
        SecretKey saltKey;
        SecretKeyFactory f;

        String decryptStr;
        String encryptStr;

        // 生成 key
        System.out.println("生成 key --------------------------------------------------------");
        keySeed = "keySeed";
        secretKey = ZKEncryptCipherUtils.generateKey(mode, keySize, keySeed);
        System.out.println(String.format("keySeed:%s; algorithm:%s; format:%s; key:%s", keySeed,
                secretKey.getAlgorithm(), secretKey.getFormat(), ZKEncodingUtils.encodeHex(secretKey.getEncoded())));

        // 生成盐
        System.out.println("生成 salt --------------------------------------------------------");
        secureRandom = new SecureRandom(keySeed.getBytes());
        salt = secureRandom.generateSeed(8);
        System.out.println(String.format("keySeed:%s; algorithm:%s; salt:%s", keySeed, secureRandom.getAlgorithm(),
                ZKEncodingUtils.encodeHex(salt)));

        // 加盐
        System.out.println("密钥 加盐 --------------------------------------------------------");
        keySpec = new PBEKeySpec(ZKEncodingUtils.encodeHex(secretKey.getEncoded()).toCharArray(), salt, iterationCount,
                64);
        System.out.println(String.format("pwd:%s; salt:%s; IterationCount:%s; KeyLength:%s",
                new String(keySpec.getPassword()), ZKEncodingUtils.encodeHex(keySpec.getSalt()),
                keySpec.getIterationCount(), keySpec.getKeyLength()));

        String algorithm = "PBKDF2WithHmacSHA1";
        f = SecretKeyFactory.getInstance(algorithm);
        saltKey = f.generateSecret(keySpec);
        System.out.println(String.format("algorithm:%s; algorithm:%s; format:%s; key:%s", algorithm,
                saltKey.getAlgorithm(), saltKey.getFormat(), ZKEncodingUtils.encodeHex(saltKey.getEncoded())));

        decryptStr = "学习一次 - DES";

        // 不加盐加解密
        System.out.println("不加盐加解密 --------------------------------------------------------");
        encryptStr = ZKEncodingUtils
                .encodeHex(ZKEncryptCipherUtils.cipher(Cipher.ENCRYPT_MODE, secretKey.getAlgorithm(),
                        decryptStr.getBytes(), secretKey));
        System.out.println("encryptStr:" + encryptStr);
        System.out.println("decryptStr:" + new String(
                ZKEncryptCipherUtils.cipher(Cipher.DECRYPT_MODE, secretKey.getAlgorithm(),
                        ZKEncodingUtils.decodeHex(encryptStr), secretKey)));

        // 密钥 PBKDF2WithHmacSHA1 加盐
        secretKey = new SecretKeySpec(saltKey.getEncoded(), mode);
        System.out.println("密钥 " + algorithm + " 加盐 --------------------------------------------------------");
        encryptStr = ZKEncodingUtils
                .encodeHex(ZKEncryptCipherUtils.cipher(Cipher.ENCRYPT_MODE, secretKey.getAlgorithm(),
                        decryptStr.getBytes(), secretKey));
        System.out.println("encryptStr:" + encryptStr);
        System.out.println("decryptStr:" + new String(
                ZKEncryptCipherUtils.cipher(Cipher.DECRYPT_MODE, secretKey.getAlgorithm(),
                        ZKEncodingUtils.decodeHex(encryptStr), secretKey)));

        // 偏移量
        DESKeySpec desKeySpec = new DESKeySpec(secretKey.getEncoded(), 0);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(mode);
        secretKey = secretKeyFactory.generateSecret(desKeySpec);
        System.out.println("密钥偏移量 --------------------------------------------------------");
        encryptStr = ZKEncodingUtils
                .encodeHex(ZKEncryptCipherUtils.cipher(Cipher.ENCRYPT_MODE, secretKey.getAlgorithm(),
                        decryptStr.getBytes(), secretKey));
        System.out.println("encryptStr:" + encryptStr);
        System.out.println("decryptStr:" + new String(
                ZKEncryptCipherUtils.cipher(Cipher.DECRYPT_MODE, secretKey.getAlgorithm(),
                        ZKEncodingUtils.decodeHex(encryptStr), secretKey)));
    }

    public static void KeySaltTest() throws Exception {
        String mode = MODE.DES;
        int keySize = 56;
        SecretKey secretKey = null;
        String keySeed = null;

        // 生成 key
        System.out.println("生成 key --------------------------------------------------------");
        keySeed = "keySeed";
        secretKey = ZKEncryptCipherUtils.generateKey(mode, keySize, keySeed);
        System.out.println(String.format("keySeed:%s; algorithm:%s; format:%s; key:%s", keySeed,
                secretKey.getAlgorithm(), secretKey.getFormat(), ZKEncodingUtils.encodeHex(secretKey.getEncoded())));

        byte[] salt;
        SecureRandom secureRandom = null;
        int iterationCount = 27692;
        PBEKeySpec keySpec;
        SecretKey saltKey;
        SecretKeyFactory f;

        // 生成盐
        System.out.println("生成 salt --------------------------------------------------------");
        secureRandom = new SecureRandom(keySeed.getBytes());
        salt = secureRandom.generateSeed(80);
        System.out.println(String.format("keySeed:%s; algorithm:%s; salt:%s", keySeed, secureRandom.getAlgorithm(),
                ZKEncodingUtils.encodeHex(salt)));

        String pwd = "sf";
        String algorithm;

        // PBE 加盐
        algorithm = "PBKDF2WithHmacSHA1";
        System.out.println("密钥 " + algorithm + " 加盐 --------------------------------------------------------");
        keySpec = new PBEKeySpec(pwd.toCharArray(), salt, iterationCount, 64);
        System.out.println(String.format("pwd:%s; salt:%s; IterationCount:%s; KeyLength:%s",
                new String(keySpec.getPassword()), ZKEncodingUtils.encodeHex(keySpec.getSalt()),
                keySpec.getIterationCount(), keySpec.getKeyLength()));
        f = SecretKeyFactory.getInstance(algorithm);
        saltKey = f.generateSecret(keySpec);
        System.out.println(String.format("algorithm:%s; algorithm:%s; format:%s; key:%s", algorithm,
                saltKey.getAlgorithm(), saltKey.getFormat(), ZKEncodingUtils.encodeHex(saltKey.getEncoded())));

        keySpec = new PBEKeySpec(pwd.toCharArray(), salt, iterationCount, 64);
        f = SecretKeyFactory.getInstance(algorithm);
        saltKey = f.generateSecret(keySpec);
        System.out.println(String.format("algorithm:%s; algorithm:%s; format:%s; key:%s", algorithm,
                saltKey.getAlgorithm(), saltKey.getFormat(), ZKEncodingUtils.encodeHex(saltKey.getEncoded())));

        // 其他加盐
        System.out.println("其他加盐 --------------------------------------------------------");
        String saltStr = "tyuiohlkjl";
        String pwdEnc = null;
        pwdEnc = ZKEncodingUtils.encodeHex(
                ZKEncryptUtils.encryptDigest(pwd.getBytes(), saltStr.getBytes(), ZKEncryptUtils.DIGEST_MODE.SHA));
        System.out.println(String.format("pwd:%s; saltStr:%s; pwdEnc:%s;", pwd, saltStr, pwdEnc));
        pwdEnc = ZKEncodingUtils.encodeHex(
                ZKEncryptUtils.encryptDigest(pwd.getBytes(), saltStr.getBytes(), ZKEncryptUtils.DIGEST_MODE.SHA));
        System.out.println(String.format("pwd:%s; saltStr:%s; pwdEnc:%s;", pwd, saltStr, pwdEnc));
        algorithm = "PBKDF2WithHmacSHA1";
        keySpec = new PBEKeySpec(pwdEnc.toCharArray(), saltStr.getBytes(), iterationCount, 64);
        f = SecretKeyFactory.getInstance(algorithm);
        saltKey = f.generateSecret(keySpec);
        System.out.println(String.format("algorithm:%s; algorithm:%s; format:%s; key:%s", algorithm,
                saltKey.getAlgorithm(), saltKey.getFormat(), ZKEncodingUtils.encodeHex(saltKey.getEncoded())));

    }

}
