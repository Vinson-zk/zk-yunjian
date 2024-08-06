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
* @Title: ZKSHA1.java 
* @author Vinson 
* @Package com.zk.wechat.wx.aes 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 18, 2021 3:09:38 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.aes;

import java.security.MessageDigest;
import java.util.Arrays;

import com.qq.weixin.mp.aes.AesException;

/**
 * 计算公众平台的消息签名接口.
 * 
 * 重写 com.qq.weixin.mp.aes 下的类，以实现支持开放平台的加解密
 * 
 * @ClassName: ZKSHA1
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKSHA1 {

    /**
     * 用SHA1算法生成安全签名
     * 
     * @param token
     *            票据
     * @param timestamp
     *            时间戳
     * @param nonce
     *            随机字符串
     * @param encrypt
     *            密文
     * @return 安全签名
     * @throws AesException
     */
    public static String getSHA1(String token, String timestamp, String nonce, String encrypt) throws ZKAesException {
        try {
            String[] array = new String[] { token, timestamp, nonce, encrypt };
            StringBuffer sb = new StringBuffer();
            // 字符串排序
            Arrays.sort(array);
            for (int i = 0; i < 4; i++) {
                sb.append(array[i]);
            }
            String str = sb.toString();
            // SHA1签名生成
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();

            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new ZKAesException(AesException.ComputeSignatureError);
        }
    }

}
