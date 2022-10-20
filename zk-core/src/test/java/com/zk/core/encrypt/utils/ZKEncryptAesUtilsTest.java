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
 * @Title: ZKEncryptAesUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.core.crypto.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 3:46:31 PM 
 * @version V1.0   
*/
package com.zk.core.encrypt.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Test;

import com.zk.core.utils.ZKEncodingUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKEncryptAesUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKEncryptAesUtilsTest {

    /**
     * 
     */
    @Test
    public void test() {
        try {
            byte[] key;
            byte[] iv = "ihaierForTodo_Iv".getBytes();
            String decryptStr = "莫雪白头老。.,;'[]\\|}{:\"?><“”·1234567890-=【】】、‘’；、。，~！@#￥%……&*（））——+{}}|“”：《》》？;''";
            String encryptStr = "e408a663ba897674ac7877aacd4a26614633ceafc93316458d9f98d70e4f5cbab2863b3a35edf8480dbb1c3f9e2f39cd06a356e2e3a09f5f4431991dcd0ebf0a6ceb9d2d9b1b0dd24bf158711a696d403324ca01af2d3603abef9d3782f6cd1098962534f81a2b0ccf4c5342630fae87b3fc4b8af3816692cc5a208256331c7ade1c1ba118044be1623a8ac45b372f551493a1a2231c6742dc8d7aecfa4f484a";
            String targetStr = "";
            SecureRandom secureRandom = new SecureRandom();
            byte[] res;

//            SecretKey secretKey = ZKEncryptCipherUtils.generateKey(ZKEncryptCipherUtils.MODE.AES, 128, null);
//            key = secretKey.getEncoded();

            key = ZKEncodingUtils.decodeHex("e0aae4575fd08d899d485771e7be4eb0");

            System.out.println("[^_^:20200109-0903-000] 无偏移 =============================== ");
            System.out.println("[^_^:20200109-0903-000] key: " + ZKEncodingUtils.encodeHex(key));
            // 加密
            targetStr = ZKEncodingUtils.encodeHex(ZKEncryptAesUtils.encrypt(decryptStr.getBytes(), key));
            System.out.println("[^_^:20200109-0903-000] encrypt: " + targetStr);
            TestCase.assertEquals(encryptStr, targetStr);
            // 解密
            targetStr = new String(ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(encryptStr), key));
            System.out.println("[^_^:20200109-0903-000] decrypt: " + targetStr);
            TestCase.assertEquals(decryptStr, targetStr);
            
            encryptStr = "db4eff595bf8f3cd319e7fedcd776437cfb5a9a0baa5710c4988472e2607f81998fb59e13263ec7cec0c6437cca9218df07197d3aaf0957631ef7dcb5d394e178988b9a5ce05e9b5f51e5aab054deeca40dfbc9ce1877e3543bdf4e65eae0f2c7cb4d2b867883a7087b2184e7f44db1aae574b781ef3a77e0f18891dfe5f59864951f0bd5ffb162ddb10811dd532b958fc0726193102f04bb5566131110e999a";
            System.out.println("[^_^:20200109-0903-001] 偏移  =============================== ");
            System.out.println("[^_^:20200109-0903-001] key: " + ZKEncodingUtils.encodeHex(key));
            System.out.println("[^_^:20200109-0903-001] iv: " + new String(iv));
            // 加密
            targetStr = ZKEncodingUtils
                    .encodeHex(ZKEncryptCipherUtils.cipher(Cipher.ENCRYPT_MODE, "AES/CBC/PKCS5Padding",
                            decryptStr.getBytes(), new SecretKeySpec(key, ZKEncryptCipherUtils.MODE.AES),
                    new IvParameterSpec(iv), secureRandom));
            System.out.println("[^_^:20200109-0903-001] encrypt: " + targetStr);
            TestCase.assertEquals(encryptStr, targetStr);
            // 解密
            targetStr = new String(ZKEncryptCipherUtils.cipher(Cipher.DECRYPT_MODE, "AES/CBC/PKCS5Padding",
                    ZKEncodingUtils.decodeHex(encryptStr), new SecretKeySpec(key, ZKEncryptCipherUtils.MODE.AES),
                    new IvParameterSpec(iv), null));
            System.out.println("[^_^:20200109-0903-001] decrypt: " + targetStr);
            TestCase.assertEquals(decryptStr, targetStr);

            String jsEncryptHexStr = "";
            System.out.println("[^_^:20200109-0903-002] 解密 js 加密的密文  =============================== ");
            
            /* js 与 java 互相加解密，注：
                key：十六位作为密钥（前后端必须一致）
                iv：十六位作为密钥偏移量（前后端必须一致）
                算法：AES/CBC/PKCS7Padding
                注意点：JAVA本身不支持PKCS7，只支持PKCS5，但是通过实验，两者即使不一样，也可以进行解密
             * 
             * js 加密：
             * js 中的 sStr 为明文内容，与 java 中的 decryptStr 相同
                    dStr = CryptoJS.enc.Utf8.parse(sStr);
                    // console.log("dStr", dStr); 
                    eStr = CryptoJS.AES.encrypt(dStr, _key, {
                        iv: _iv,
                        mode: CryptoJS.mode.CBC,
                        padding: CryptoJS.pad.Pkcs7
                    });
                    // console.log("eStr", eStr); 
                    str = eStr.ciphertext.toString();
                    console.log(str);
             * 
             * js 解密：
             * js 中的 eStr 为密文内容，与 java 中的 encryptStr 相同，同为 hex 编码字符串
                    str = CryptoJS.enc.Hex.parse(eStr);
                    str = {ciphertext:str};
                    dStr = CryptoJS.AES.decrypt(str, _key, {
                        iv: _iv,
                        mode: CryptoJS.mode.CBC,
                        padding: CryptoJS.pad.Pkcs7
                    });
                    str = dStr.toString(CryptoJS.enc.Utf8);
                    console.log(str); 
             */
            jsEncryptHexStr = "db4eff595bf8f3cd319e7fedcd776437cfb5a9a0baa5710c4988472e2607f81998fb59e13263ec7cec0c6437cca9218df07197d3aaf0957631ef7dcb5d394e178988b9a5ce05e9b5f51e5aab054deeca40dfbc9ce1877e3543bdf4e65eae0f2c7cb4d2b867883a7087b2184e7f44db1aae574b781ef3a77e0f18891dfe5f59864951f0bd5ffb162ddb10811dd532b958fc0726193102f04bb5566131110e999a";
            System.out.println("[^_^:20200109-0903-001] key: " + ZKEncodingUtils.encodeHex(key));
            System.out.println("[^_^:20200109-0903-001] iv: " + new String(iv));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, ZKEncryptCipherUtils.MODE.AES),
                    new IvParameterSpec(iv), secureRandom);
            res = cipher.doFinal(ZKEncodingUtils.decodeHex(jsEncryptHexStr));
            targetStr = new String(res);
            System.out.println("[^_^:20200109-0903-001] decrypt: " + targetStr);
            TestCase.assertEquals(decryptStr, targetStr);
            
            targetStr = new String(ZKEncryptCipherUtils.cipher(Cipher.DECRYPT_MODE, "AES/CBC/PKCS5Padding",
                    ZKEncodingUtils.decodeHex(jsEncryptHexStr), new SecretKeySpec(key, ZKEncryptCipherUtils.MODE.AES),
                    new IvParameterSpec(iv), null));
            System.out.println("[^_^:20200109-0903-001] decrypt: " + targetStr);
            TestCase.assertEquals(decryptStr, targetStr);
                    
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * salt 不同，同一明文加密文不同
     */
    @Test
    public void testEncAndDec() {
        try {
            byte[] key;
            byte[] salt;
            String decryptStr;

            String encryptStr;

            key = "秋风那更夜鸣蛩".getBytes();
            salt = "野渡燕穿杨柳雨".getBytes();
            decryptStr = "秋雨潇潇，漫烂黄花都满径；春风袅袅，扶疏绿竹正盈窗。";
            encryptStr = ZKEncodingUtils.encodeHex(ZKEncryptAesUtils.encrypt(decryptStr.getBytes(), key, salt));
            TestCase.assertEquals(encryptStr,
                    ZKEncodingUtils.encodeHex(ZKEncryptAesUtils.encrypt(decryptStr.getBytes(), key, salt)));
            salt = "芳池鱼戏芰荷风".getBytes();
            // salt 不同，同一明文加密文不同
            TestCase.assertFalse(encryptStr
                    .equals(ZKEncodingUtils.encodeHex(ZKEncryptAesUtils.encrypt(decryptStr.getBytes(), key, salt))));
            encryptStr = ZKEncodingUtils.encodeHex(ZKEncryptAesUtils.encrypt(decryptStr.getBytes(), key, salt));
            // 要使用对应密码与盐方可解密
            TestCase.assertEquals(decryptStr,
                    new String(ZKEncryptAesUtils.decrypt(ZKEncodingUtils.decodeHex(encryptStr), key, salt)));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }
}
