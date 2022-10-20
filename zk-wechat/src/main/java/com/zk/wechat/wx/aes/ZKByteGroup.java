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
* @Title: ZKByteGroup.java 
* @author Vinson 
* @Package com.zk.wechat.wx.aes 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 18, 2021 3:20:37 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.aes;

import java.util.ArrayList;

/**
 * 重写 com.qq.weixin.mp.aes 下的类，以实现支持开放平台的加解密
 * 
 * @ClassName: ZKByteGroup
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKByteGroup {

    ArrayList<Byte> byteContainer = new ArrayList<Byte>();

    public byte[] toBytes() {
        byte[] bytes = new byte[byteContainer.size()];
        for (int i = 0; i < byteContainer.size(); i++) {
            bytes[i] = byteContainer.get(i);
        }
        return bytes;
    }

    public ZKByteGroup addBytes(byte[] bytes) {
        for (byte b : bytes) {
            byteContainer.add(b);
        }
        return this;
    }

    public int size() {
        return byteContainer.size();
    }

}
