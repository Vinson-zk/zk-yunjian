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
* @Title: ZKWXTPComponentAccessToken.java 
* @author Vinson 
* @Package com.zk.wechat.wx.thirdParty.msgBean 
* @Description: TODO(simple description this file what to do. ) 
* @date Nov 5, 2021 8:22:16 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.thirdParty.msgBean;

import com.zk.wechat.wx.common.ZKWXAbstractAccessToken;

/**
 * 第三方平台账号的 ComponentAccessToken
 * 
 * @ClassName: ZKWXTPComponentAccessToken
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKWXTPComponentAccessToken extends ZKWXAbstractAccessToken {

    /**
     * 
     */
    private static final long serialVersionUID = 3341987048341857897L;

    /**
     * 第三方 appId
     */
    private String thirdPartyAppid;

    public ZKWXTPComponentAccessToken(String thirdPartyAppid) {
        this.thirdPartyAppid = thirdPartyAppid;
    }

    /**
     * 过期判断时，偏移量，单位秒；正数为提前多少秒；负数为延迟多少秒;
     * 
     * @return
     * @see com.zk.wechat.wx.common.ZKWXAbstractAccessToken#getOffsetExpiresTime()
     */
    @Override
    protected int getOffsetExpiresTime() {
        // TODO Auto-generated method stub
        return 100;
    }

    /**
     * @return thirdPartyAppid sa
     */
    public String getThirdPartyAppid() {
        return thirdPartyAppid;
    }

}
