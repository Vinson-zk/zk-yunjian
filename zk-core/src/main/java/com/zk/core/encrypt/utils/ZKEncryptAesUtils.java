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
 * @Title: ZKEncryptAesUtils.java 
 * @author Vinson 
 * @Package com.zk.core.crypto.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 1:30:03 PM 
 * @version V1.0   
*/
package com.zk.core.encrypt.utils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.spec.SecretKeySpec;

/** 
* @ClassName: ZKEncryptAesUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKEncryptAesUtils {

    /**
     * 密钥长度
     */
    public static final int keySize = 256;

    /**
     * 迭代次数
     */
    public static final int iterationCount = 100;

    /**
     * 解密
     * 
     * @param data
     *            密文
     * @param key
     *            密钥
     * @param salt
     *            盐，字符串
     * @return 解密后的明文
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static byte[] decrypt(byte[] data, byte[] key, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return ZKEncryptCipherUtils.decrypt(data,
                new SecretKeySpec(ZKEncryptCipherUtils.generatePBESaltKey(key, salt, iterationCount, keySize),
                        ZKEncryptCipherUtils.MODE.AES));
    }

    /**
     * 加密
     * 
     * @param data
     *            明文
     * @param key
     *            密钥
     * @param salt
     *            盐，字符串
     * @return 解密后的密文
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static byte[] encrypt(byte[] data, byte[] key, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return ZKEncryptCipherUtils.encrypt(data,
                new SecretKeySpec(ZKEncryptCipherUtils.generatePBESaltKey(key, salt, iterationCount, keySize),
                        ZKEncryptCipherUtils.MODE.AES));
    }

    /**
     * 解密
     * 
     * @param data
     *            密文
     * @param key
     *            密钥
     * @return 解密后的明文
     */
    public static byte[] decrypt(byte[] data, byte[] key) {
        return ZKEncryptCipherUtils.decrypt(data, new SecretKeySpec(key, ZKEncryptCipherUtils.MODE.AES));
    }

    /**
     * 加密
     * 
     * @param data
     *            明文
     * @param key
     *            密钥
     * @return 解密后的密文
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
        return ZKEncryptCipherUtils.encrypt(data, new SecretKeySpec(key, ZKEncryptCipherUtils.MODE.AES));
    }

}
