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
 * @Title: ZKEncryptCipherUtils.java 
 * @author Vinson 
 * @Package com.zk.core.crypto.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 1:31:24 PM 
 * @version V1.0   
*/
package com.zk.core.encrypt.utils;

import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.utils.ZKStringUtils;

/** 
* @ClassName: ZKEncryptCipherUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKEncryptCipherUtils {

    protected static Logger logger = LogManager.getLogger(ZKEncryptCipherUtils.class);

    public static interface MODE {
        /**
         * key size must be equal to 56;
         */
        public static final String DES = "DES";

        /**
         * key size must be equal to 112 or 168
         */
        public static final String DESede = "DESede";

        /**
         * key size must be equal to 112 or 168
         */
        public static final String TripleDES = "TripleDES";

        /**
         * key size must be equal to 128, 192 or 256,but 192 and 256 bits may
         * not be available
         */
        public static final String AES = "AES";

        /**
         * key size must be multiple of 8, and can only range from 32 to 448
         * (inclusive)
         */
        public static final String Blowfish = "Blowfish";

        /**
         * key size must be between 40 and 1024 bits
         */
        public static final String RC2 = "RC2";

        /**
         * key size must be between 40 and 1024 bits
         */
        public static final String RC4 = "RC4";
    }

    /**
     * 生成密钥, key 值长度对应算法 mode 要做改变 mode 算法，可为以下任意一种算法及key值的size对应如下 <br>
     *  
     * DES                  key size must be equal to 56；注意用此方法生成 key 是传的是 56；生成 key 的长度实际是 64位，也就是真正加密的 key 是64位的，所以加盐时要注意处理。
     * DESede(TripleDES)    key size must be equal to 112 or 168 
     * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available 
     * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive) 
     * RC2                  key size must be between 40 and 1024 bits 
     * RC4(ZKCFOUR)         key size must be between 40 and 1024 bits
     * 
     * @param mode
     *            算法
     * @param keySize,
     *            对应密钥长度，与 mode 对应，传O为不指定，自动对应
     * @param seed
     *            随机种子，选传
     * @return SecretKey 生成的 key
     * @throws NoSuchAlgorithmException
     * @throws Exception
     */
    public static SecretKey generateKey(String mode, int keySize, String seed) throws NoSuchAlgorithmException {
        if (ZKStringUtils.isEmpty(mode)) {
            logger.error("[>_<: 20180822-1456-001] 生成密钥失败，mode:{}; is null", mode);
            throw new RuntimeException(String.format("生成密钥失败，mode:{}; is null", mode));
        }
        SecureRandom secureRandom = null;
        if (seed != null) {
            secureRandom = new SecureRandom(ZKEncodingUtils.decodeBase64(seed));
        }
        else {
            secureRandom = new SecureRandom();
        }

        KeyGenerator kg = KeyGenerator.getInstance(mode);

        if (keySize > 0) {
            kg.init(keySize, secureRandom);
        }
        else {
            kg.init(secureRandom);
        }

        SecretKey secretKey = kg.generateKey();
        return secretKey;
    }

    /**
     * 对密钥加盐，用 PBE 与 MD5 模式结合加盐；
     * 
     * @param key
     * @param salt
     * @param iterationCount
     * @param keySize
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static byte[] generatePBESaltKey(byte[] key, byte[] salt, int iterationCount, int keySize)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String pwdEnc = ZKEncodingUtils
                .encodeHex(ZKEncryptUtils.encryptDigest(key, salt, ZKEncryptUtils.DIGEST_MODE.SHA));
        String algorithm = "PBKDF2WithHmacSHA1";
        PBEKeySpec keySpec = new PBEKeySpec(pwdEnc.toCharArray(), salt, iterationCount, keySize);
        return SecretKeyFactory.getInstance(algorithm).generateSecret(keySpec).getEncoded();
    }

    public static byte[] paddingEnc(byte[] data, int paddingSize) {
        if (paddingSize < 1) {
            return data;
        }

        if (data == null) {
            logger.error("[>_<:20180823-1652-001] paddingEnc error; data is null");
            throw new RuntimeException("paddingEnc error; data is null");
        }
        if (data.length < 1) {
            logger.error("[>_<:20180823-1652-002] paddingEnc error; data length eq 0; ");
            throw new RuntimeException("paddingEnc error; data length eq 0;");
        }

        byte[] rs = new byte[data.length + paddingSize];
        int len = 0;
        Random random = new Random();
        byte[] tempByte = new byte[1];
        while (len < rs.length) {
            if (len / 2 < data.length) {
                if (len / 2 < paddingSize) {
                    rs[len] = data[len / 2];
                    ++len;
                    random.nextBytes(tempByte);
                    rs[len] = tempByte[0];
                }
                else {
                    rs[len] = data[len - paddingSize];
                }
            }
            else {
                random.nextBytes(tempByte);
                rs[len] = tempByte[0];
            }
            ++len;
        }
        return rs;
    }

    public static byte[] paddingDec(byte[] data, int paddingSize) {

        if (paddingSize < 1) {
            return data;
        }

        if (data == null) {
            logger.error("[>_<:20180823-1655-001] paddingDec error; data is null");
            throw new RuntimeException("paddingEnc error; data is null");
        }
        if (data.length < 1) {
            logger.error("[>_<:20180823-1655-002] paddingDec error; data length eq 0; ");
            throw new RuntimeException("paddingEnc error; data length eq 0;");
        }
        if (data.length <= paddingSize) {
            logger.error("[>_<:20180823-1655-003] paddingDec error; data length < paddingSize;");
            throw new RuntimeException("paddingDec error; data length < paddingSize;");
        }

        byte[] rs = new byte[data.length - paddingSize];
        int len = 0;
        while (len < rs.length) {
            if (len < paddingSize) {
                rs[len] = data[len * 2];
            }
            else {
                rs[len] = data[len + paddingSize];
            }
            ++len;
        }
        return rs;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 解密
     * 
     * @param data
     *            密文
     * @param secretKey
     *            密钥
     * @param mode
     *            解密算法模式，要与加密算法对应
     * @return 解密后的明文
     */
    public static byte[] decrypt(byte[] data, byte[] secretKey, String mode) {
        return cipher(Cipher.DECRYPT_MODE, mode, data, new SecretKeySpec(secretKey, mode));
    }

    public static byte[] decrypt(byte[] data, SecretKey secretKey) {
        return cipher(Cipher.DECRYPT_MODE, secretKey.getAlgorithm(), data, secretKey);
    }

    /**
     * 加密
     * 
     * @param data
     *            明文
     * @param secretKey
     *            密钥
     * @param mode
     *            加密算法模式，只能用同程算法解密
     * @return 解密后的密文
     */
    public static byte[] encrypt(byte[] data, byte[] secretKey, String mode) {
        return cipher(Cipher.ENCRYPT_MODE, mode, data, new SecretKeySpec(secretKey, mode));
    }

    public static byte[] encrypt(byte[] data, SecretKey secretKey) {
        return cipher(Cipher.ENCRYPT_MODE, secretKey.getAlgorithm(), data, secretKey);
    }

    /**
     * 解密
     * 
     * @param data
     *            密文
     * @param secretKey
     *            密钥
     * @param mode
     *            解密算法模式，要与加密算法对应
     * @return 解密后的明文
     */
    public static byte[] decrypt(byte[] data, byte[] secretKey, String mode, int padding) {
        return paddingDec(cipher(Cipher.DECRYPT_MODE, mode, data, new SecretKeySpec(secretKey, mode)), padding);
    }

    /**
     * 加密
     * 
     * @param data
     *            明文
     * @param secretKey
     *            密钥
     * @param mode
     *            加密算法模式，只能用同程算法解密
     * @return 解密后的密文
     */
    public static byte[] encrypt(byte[] data, byte[] secretKey, String mode, int padding) {
        return cipher(Cipher.ENCRYPT_MODE, mode, paddingEnc(data, padding), new SecretKeySpec(secretKey, mode));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    // cipher 加解密方法汇总
    //////////////////////////////////////////////////////////////////////////////////////////////////////// /////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 加解密方法
     *
     * @Title: cipher
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 19, 2021 3:55:38 PM
     * @param cipherMode
     * @param instanceParameter
     * @param data
     * @param key
     * @param aParam
     * @return
     * @return byte[]
     */
    public static byte[] cipher(int cipherMode, String instanceParameter, byte[] data, Key key,
            AlgorithmParameters aParam) {
        try {
            Cipher cipher = Cipher.getInstance(instanceParameter);
            cipher.init(cipherMode, key, aParam);
            return cipher.doFinal(data);
        }
        catch(GeneralSecurityException e) {
            throw new RuntimeException("cipher 加解密处理失败!", e);
        }
    }

    public static byte[] cipher(int cipherMode, String instanceParameter, byte[] data, Key key,
            AlgorithmParameters aParam, SecureRandom secureRandom) {
        try {
            Cipher cipher = Cipher.getInstance(instanceParameter);
            cipher.init(cipherMode, key, aParam, secureRandom);
            return cipher.doFinal(data);
        }
        catch(GeneralSecurityException e) {
            throw new RuntimeException("cipher 加解密处理失败!", e);
        }
    }

    public static byte[] cipher(int cipherMode, String instanceParameter, byte[] data, Key key) {
        try {
            Cipher cipher = Cipher.getInstance(instanceParameter);
            cipher.init(cipherMode, key);
            return cipher.doFinal(data);
        }
        catch(GeneralSecurityException e) {
            throw new RuntimeException("cipher 加解密处理失败!", e);
        }
    }

    public static byte[] cipher(int cipherMode, String instanceParameter, byte[] data, Key key,
            SecureRandom secureRandom) {
        try {
            Cipher cipher = Cipher.getInstance(instanceParameter);
            cipher.init(cipherMode, key, secureRandom);
            return cipher.doFinal(data);
        }
        catch(GeneralSecurityException e) {
            throw new RuntimeException("cipher 加解密处理失败!", e);
        }
    }

    public static byte[] cipher(int cipherMode, String instanceParameter, byte[] data, Key key,
            AlgorithmParameterSpec apSpec) {
        try {
            Cipher cipher = Cipher.getInstance(instanceParameter);
            cipher.init(cipherMode, key, apSpec);
            return cipher.doFinal(data);
        }
        catch(GeneralSecurityException e) {
            throw new RuntimeException("cipher 加解密处理失败!", e);
        }
    }

    public static byte[] cipher(int cipherMode, String instanceParameter, byte[] data, Key key,
            AlgorithmParameterSpec apSpec, SecureRandom secureRandom) {
        try {
            Cipher cipher = Cipher.getInstance(instanceParameter);
            cipher.init(cipherMode, key, apSpec, secureRandom);
            return cipher.doFinal(data);
        }
        catch(GeneralSecurityException e) {
            throw new RuntimeException("cipher 加解密处理失败!", e);
        }
    }

    public static byte[] cipher(int cipherMode, String instanceParameter, byte[] data, Certificate cer) {
        try {
            Cipher cipher = Cipher.getInstance(instanceParameter);
            cipher.init(cipherMode, cer);
            return cipher.doFinal(data);
        }
        catch(GeneralSecurityException e) {
            throw new RuntimeException("cipher 加解密处理失败!", e);
        }
    }

    public static byte[] cipher(int cipherMode, String instanceParameter, byte[] data, Certificate cer,
            SecureRandom secureRandom) {
        try {
            Cipher cipher = Cipher.getInstance(instanceParameter);
            cipher.init(cipherMode, cer, secureRandom);
            return cipher.doFinal(data);
        }
        catch(GeneralSecurityException e) {
            throw new RuntimeException("cipher 加解密处理失败!", e);
        }
    }


}
