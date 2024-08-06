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
* @Title: ZKWXApiCert.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 20, 2021 11:44:59 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay.entity;

import java.security.PrivateKey;

/** 
* @ClassName: ZKWXApiCert 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWXApiCert {

    // 证书序列号
    String serialNo;

    // 根据商户ID 取商户对应的证书私钥
    PrivateKey privateKey;

    public ZKWXApiCert(String serialNo, PrivateKey privateKey) {
        this.serialNo = serialNo;
        this.privateKey = privateKey;
    }

    /**
     * @return serialNo sa
     */
    public String getSerialNo() {
        return serialNo;
    }

    /**
     * @return privateKey sa
     */
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

}
