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
* @Title: ZKPKCS7Encoder.java 
* @author Vinson 
* @Package com.zk.wechat.wx.aes 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 18, 2021 3:21:02 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.aes;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 重写 com.qq.weixin.mp.aes 下的类，以实现支持开放平台的加解密
 * 
 * @ClassName: ZKPKCS7Encoder
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKPKCS7Encoder {

    static Charset CHARSET = StandardCharsets.UTF_8;

    static int BLOCK_SIZE = 32;

    /**
     * 获得对明文进行补位填充的字节.
     * 
     * @param count
     *            需要进行填充补位操作的明文字节个数
     * @return 补齐用的字节数组
     */
    static byte[] encode(int count) {
        // 计算需要填充的位数
        int amountToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
        if (amountToPad == 0) {
            amountToPad = BLOCK_SIZE;
        }
        // 获得补位所用的字符
        char padChr = chr(amountToPad);
        String tmp = new String();
        for (int index = 0; index < amountToPad; index++) {
            tmp += padChr;
        }
        return tmp.getBytes(CHARSET);
    }

    /**
     * 删除解密后明文的补位字符
     * 
     * @param decrypted
     *            解密后的明文
     * @return 删除补位字符后的明文
     */
    static byte[] decode(byte[] decrypted) {
        int pad = (int) decrypted[decrypted.length - 1];
        if (pad < 1 || pad > 32) {
            pad = 0;
        }
        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }

    /**
     * 将数字转化成ASCII码对应的字符，用于对明文进行补码
     * 
     * @param a
     *            需要转化的数字
     * @return 转化得到的字符
     */
    static char chr(int a) {
        byte target = (byte) (a & 0xFF);
        return (char) target;
    }

}
