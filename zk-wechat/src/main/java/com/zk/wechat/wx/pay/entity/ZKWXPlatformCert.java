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
* @Title: ZKWXPlatformCert.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 20, 2021 11:50:29 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay.entity;

import java.security.cert.X509Certificate;
import java.util.Date;

import com.zk.core.utils.ZKDateUtils;

/** 
* @ClassName: ZKWXPlatformCert 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWXPlatformCert {

    // 证书序列号
    String serialNo;

    // X509证书
    X509Certificate cert;

    Date expirationTime;

    public ZKWXPlatformCert(String serialNo, X509Certificate cert, Date expirationTime) {
        this.serialNo = serialNo;
        this.cert = cert;
        this.expirationTime = expirationTime;
    }

    /**
     * @return serialNo sa
     */
    public String getSerialNo() {
        return serialNo;
    }

    /**
     * @return cert sa
     */
    public X509Certificate getCert() {
        return cert;
    }

    /**
     * @return expirationTime sa
     */
    public Date getExpirationTime() {
        return expirationTime;
    }

    // 判断是否过期; true-已过期；false-未过期；
    public boolean isExpiration() {
        if (this.getExpirationTime().getTime() > ZKDateUtils.getToday().getTime()) {
            return false;
        }
        return true;
    }

    // 取离过期还有多少秒
    public long getExpSecond() {
        return -1 * ZKDateUtils.pastSecond(this.getExpirationTime());
    }

}
