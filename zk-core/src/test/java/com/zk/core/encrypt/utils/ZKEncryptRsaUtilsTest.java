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
 * @Title: ZKEncryptRsaUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.core.crypto.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 4:17:21 PM 
 * @version V1.0   
*/
package com.zk.core.encrypt.utils;

import java.security.SecureRandom;

import org.junit.Test;

import com.zk.core.encrypt.ZKRSAKey;
import com.zk.core.utils.ZKEncodingUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKEncryptRsaUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKEncryptRsaUtilsTest {

    @Test
    public void test() {
        try {
            String sourceStr = "清风生酒舍，晧月照书窗";
            byte[] encBytes = null; // 密文
            String decStr = ""; // 解密后的明文

            ZKRSAKey testZKRSAKey = ZKEncryptRsaUtils.genZKRSAKey(2048, new SecureRandom());

            System.out.println("[^_^:20190617-1706-001] publicKey: " + testZKRSAKey.getPublicKeyStr() + "  privateKey: "
                    + testZKRSAKey.getPrivateKeyStr());

            /*** 直接使用 key 原生字节数组加解密 ***/
            encBytes = ZKEncryptRsaUtils.encrypt(sourceStr.getBytes("UTF-8"), testZKRSAKey.getPublicKey());
            System.out.println("[^_^:20190617-1706-002-1] 公钥加密的密文：" + ZKEncodingUtils.encodeBase64(encBytes));

            decStr = new String(ZKEncryptRsaUtils.decrypt(encBytes, testZKRSAKey.getPrivateKey()), "UTF-8");
            System.out.println("[^_^:20190617-1706-003-1] 私钥解密后的明文：" + decStr);
            TestCase.assertEquals(sourceStr, decStr);

            /*** 使用 key 编码后的字符串加解密 ***/
            encBytes = ZKEncryptRsaUtils.encrypt(sourceStr.getBytes("UTF-8"), testZKRSAKey.getPublicKeyStr());
            System.out.println("[^_^:20190617-1706-002-2] 公钥加密的密文：" + ZKEncodingUtils.encodeBase64(encBytes));

            decStr = new String(ZKEncryptRsaUtils.decrypt(encBytes, testZKRSAKey.getPrivateKeyStr()), "UTF-8");
            System.out.println("[^_^:20190617-1706-003-2] 私钥解密后的明文：" + decStr);
            TestCase.assertEquals(sourceStr, decStr);

            /*** 使用 key 原生字节数组加密 编码后的字符串KEY解密 ***/
            encBytes = ZKEncryptRsaUtils.encrypt(sourceStr.getBytes("UTF-8"), testZKRSAKey.getPublicKey());
            System.out.println("[^_^:20190617-1706-002-3] 公钥加密的密文：" + ZKEncodingUtils.encodeBase64(encBytes));

            decStr = new String(ZKEncryptRsaUtils.decrypt(encBytes, testZKRSAKey.getPrivateKeyStr()), "UTF-8");
            System.out.println("[^_^:20190617-1706-003-3] 私钥解密后的明文：" + decStr);
            TestCase.assertEquals(sourceStr, decStr);

            /*** 使用 key 编码后的字符串KEY加密，原生字节数组解密 ***/
            encBytes = ZKEncryptRsaUtils.encrypt(sourceStr.getBytes("UTF-8"), testZKRSAKey.getPublicKeyStr());
            System.out.println("[^_^:20190617-1706-002-4] 公钥加密的密文：" + ZKEncodingUtils.encodeBase64(encBytes));

            decStr = new String(ZKEncryptRsaUtils.decrypt(encBytes, testZKRSAKey.getPrivateKey()), "UTF-8");
            System.out.println("[^_^:20190617-1706-003-4] 私钥解密后的明文：" + decStr);
            TestCase.assertEquals(sourceStr, decStr);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
