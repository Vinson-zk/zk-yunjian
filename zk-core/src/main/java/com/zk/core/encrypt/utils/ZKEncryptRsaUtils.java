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
 * @Title: ZKEncryptRsaUtils.java 
 * @author Vinson 
 * @Package com.zk.core.crypto.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 1:32:45 PM 
 * @version V1.0   
*/
package com.zk.core.encrypt.utils;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.zk.core.encrypt.ZKRSAKey;
import com.zk.core.utils.ZKEncodingUtils;

/** 
* @ClassName: ZKEncryptRsaUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKEncryptRsaUtils {

    /**
     * 生成 RSAKeyPair
     *
     * @Title: genRSAKeyPair
     * @Description: 生成 RSAKeyPair，一个包含 公钥与私角的 KeyPair
     * @author Vinson
     * @date Jun 19, 2019 3:59:02 PM
     * @param keysize
     * @param random
     * @return
     * @throws NoSuchAlgorithmException
     * @return KeyPair
     */
    private static KeyPair genRSAKeyPair(int keysize, SecureRandom random) throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-2048位
//        keyPairGen.initialize(1024, new SecureRandom());
        keyPairGen.initialize(keysize, random);
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();

        return keyPair;

    }

    /**
     * 生成一对 RSA 加解密 公钥私钥
     *
     * @Title: genZKRSAKey
     * @Description: 生成一对 RSA 加解密 公钥私钥
     * @author Vinson
     * @date Jun 19, 2019 4:28:15 PM
     * @param keysize
     * @param random
     * @return
     * @throws NoSuchAlgorithmException
     * @return ZKRSAKey
     */
    public static ZKRSAKey genZKRSAKey(int keysize, SecureRandom random) throws NoSuchAlgorithmException {

        KeyPair keyPair = genRSAKeyPair(keysize, random);

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic(); // 得到公钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate(); // 得到私钥

        return new ZKRSAKey(publicKey.getEncoded(), privateKey.getEncoded());

    }

    /**
     * 公钥字节生成 RSA 公钥
     *
     * @Title: getRSAPublicKeyByByte
     * @Description: 根据公钥的字节数组，生成 RSAPublicKey
     * @author Vinson
     * @date Jun 19, 2019 1:55:52 PM
     * @param publicKey
     *            公钥的原生字节
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @return RSAPublicKey
     */
    private static RSAPublicKey getRSAPublicKeyByByte(byte[] publicKey)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
        return (RSAPublicKey) pubKey;
    }

    /**
     * 私钥字节生成 RSA 私钥
     *
     * @Title: getRSAPrivateKeyByByte
     * @Description: 根据私钥的字节数组，生成 RSAPrivateKey
     * @author Vinson
     * @date Jun 19, 2019 1:58:29 PM
     * @param privateKey
     *            私钥的原生字节
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @return RSAPrivateKey
     */
    private static RSAPrivateKey getRSAPrivateKeyByByte(byte[] privateKey)
            throws InvalidKeySpecException, NoSuchAlgorithmException {

        // 用 X509EncodedKeySpec 生成私钥会报无效 keySpec 的错误：InvalidKeySpecException:
        // Only RSAPrivate(Crt)KeySpec and PKCS8EncodedKeySpec supported for RSA
        // private keys
//        只能用 RSAPrivate(Crt)KeySpec and PKCS8EncodedKeySpec 生成 RSA 私钥
//        PrivateKey pubKey = KeyFactory.getInstance("RSA").generatePrivate(new X509EncodedKeySpec(privateKey));
        PrivateKey pubKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKey));

        return (RSAPrivateKey) pubKey;
    }

    /**
     * 公钥，加密
     *
     * @Title: encrypt
     * @Description: 使用公钥给内容加密
     * @author Vinson
     * @date Jun 19, 2019 2:13:16 PM
     * @param content
     *            需要加密的内容，字节数组
     * @param publicKeyHexStr
     *            公钥字符串；注意通过 hex 位编码后，生成的字符串
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @return byte[]
     */
    public static byte[] encrypt(byte[] content, String publicKeyHexStr)
            throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException {
        return encrypt(content, ZKEncodingUtils.decodeHex(publicKeyHexStr));
    }

    /**
     * 公钥，加密
     *
     * @Title: encrypt
     * @Description: 使用公钥给内容加密
     * @author Vinson
     * @date Jun 19, 2019 4:14:14 PM
     * @param content
     *            需要加密的内容，字节数组
     * @param publicKey
     *            公钥；注意是原生字节，不是通过编码后的字节
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     * @return byte[]
     */
    public static byte[] encrypt(byte[] content, byte[] publicKey)
            throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException {
        RSAPublicKey pubKey = getRSAPublicKeyByByte(publicKey);
        // RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(content);
    }

    /**
     * 私钥，解密
     *
     * @Title: decrypt
     * @Description: 使用私钥解密公钥加密的内容
     * @author Vinson
     * @date Jun 19, 2019 2:13:40 PM
     * @param encContent
     *            公钥加密的内容
     * @param privateKeyHexStr
     *            私钥字符串；注意通过 hex 位编码后，生成的字符串
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @return byte[]
     */
    public static byte[] decrypt(byte[] encContent, String privateKeyHexStr)
            throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException {
        return decrypt(encContent, ZKEncodingUtils.decodeHex(privateKeyHexStr));
    }

    /**
     * 
     *
     * @Title: decrypt
     * @Description: 使用私钥解密公钥加密的内容
     * @author Vinson
     * @date Jun 19, 2019 4:12:01 PM
     * @param encContent
     *            公钥加密的内容
     * @param privateKey
     *            私钥字节；注意是原生字节，不是通过编码后的字节
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @return byte[]
     */
    public static byte[] decrypt(byte[] encContent, byte[] privateKey)
            throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException {
        RSAPrivateKey priKey = getRSAPrivateKeyByByte(privateKey);
        // RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return cipher.doFinal(encContent);
    }

}
