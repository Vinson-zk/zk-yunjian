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
 * @Title: ZKEncryptUtils.java 
 * @author Vinson 
 * @Package com.zk.core.crypto.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 1:33:12 PM 
 * @version V1.0   
*/
package com.zk.core.encrypt.utils;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * 
 * HmacMD5 HmacSHA1 HmacSHA256 HmacSHA384 HmacSHA512 加密
 * 
 * 生成 md5 唯一值；
 * 
 * @ClassName: ZKEncryptUtils
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKEncryptUtils {

    protected static Logger logger = LogManager.getLogger(ZKEncryptUtils.class);

    /**
     * 用公开函数和密钥产生一个固定长度的值作为认证标识
     * 
     * @ClassName: HMAC_MODE
     * @Description: TODO(描述这个类)
     * @author zk
     * @date Aug 23, 2018 2:00:58 PM
     *
     */
    public static interface HMAC_MODE {

        public static final String HmacMD5 = "HmacMD5";

        public static final String HmacSHA1 = "HmacSHA1";

        public static final String HmacSHA256 = "HmacSHA256";

        public static final String HmacSHA384 = "HmacSHA384";

        public static final String HmacSHA512 = "HmacSHA512";
    }

    /**
     * MD5后都能生成唯一的MD5值
     * 
     * @ClassName: DIGEST_MODE
     * @Description: TODO(描述这个类)
     * @author zk
     * @date Aug 23, 2018 2:00:27 PM
     *
     */
    public static interface DIGEST_MODE {

        public static final String SHA = "SHA";

        public static final String SHA_1 = "SHA-1";

        public static final String MD5 = "MD5";
    }

    /**
     * MAC算法加密数据; 用公开函数和密钥产生一个固定长度的值作为认证标识
     * 
     * @param data
     *            需要加密数据
     * @param key
     *            密钥，
     * @param hmacMode，加密算法
     *            * MAC算法可选以下多种算法
     * 
     *            <pre>
     *  
     * HmacMD5  
     * HmacSHA1  
     * HmacSHA256  
     * HmacSHA384  
     * HmacSHA512
     *            </pre>
     * 
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static byte[] encryptHmac(byte[] data, byte[] key, String hmacMode)
            throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKey secretKey = new SecretKeySpec(key, hmacMode);
        Mac mac = Mac.getInstance(hmacMode);
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    /**
     * 加密数据; MD5后都能生成唯一的MD5值
     * 
     * @param data
     *            加密数据
     * @param salt
     *            可先参数
     * @param digestMode
     *            加密算法 *可选以下多种算法
     * 
     *            <pre>
     *  
     * SHA(SHA-1)
     * MD5
     *            </pre>
     * 
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] encryptDigest(byte[] data, byte[] salt, String digestMode) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(digestMode);
        if (salt != null) {
            digest.update(salt);
//        }
//        else {
//            digest.update(data);
        }
        return digest.digest(data);
    }

    /**
     * 给 key 加盐；
     *
     * @Title: genSalt
     * @Description: 给 key 加盐；
     * @author Vinson
     * @date Jun 26, 2019 4:17:42 PM
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     * @return byte[]
     */
    public static byte[] genSalt(byte[] key) throws NoSuchAlgorithmException {
        MessageDigest crypt = MessageDigest.getInstance(DIGEST_MODE.SHA_1);
        crypt.reset();
        crypt.update(key);
        // sha1 = byteToHex(crypt.digest());
        return crypt.digest();
    }

    public static byte[] md5Encode(byte[] sources) {
        try {
            MessageDigest md5 = MessageDigest.getInstance(DIGEST_MODE.MD5);
            return md5.digest(sources);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
