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
 * @Title: ZKRSAKey.java 
 * @author Vinson 
 * @Package com.zk.core.crypto 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 1:33:49 PM 
 * @version V1.0   
*/
package com.zk.core.encrypt;

import com.zk.core.utils.ZKEncodingUtils;

/** 
* @ClassName: ZKRSAKey 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKRSAKey {

    /**
     * 公钥原生字节数组
     */
    byte[] publicKey;

    /**
     * 私钥原生字节数组
     */
    byte[] privateKey;

    public ZKRSAKey(byte[] publicKey, byte[] privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /**
     * @return publicKey 公钥原生字节数组
     */
    public byte[] getPublicKey() {
        return publicKey;
    }

    /**
     * @return privateKey 私钥原生字节数组
     */
    public byte[] getPrivateKey() {
        return privateKey;
    }

    /**
     * @return publicKey 公钥字节通 base64 编码后，生成字符串返回
     */
    public String getPublicKeyStr() {
        return ZKEncodingUtils.encodeHex(publicKey);
    }

    /**
     * @return privateKey 私钥字节通 base64 编码后，生成字符串返回
     */
    public String getPrivateKeyStr() {
        return ZKEncodingUtils.encodeHex(privateKey);
    }

}
