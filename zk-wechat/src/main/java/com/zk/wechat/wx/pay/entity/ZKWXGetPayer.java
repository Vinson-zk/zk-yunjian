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
* @Title: ZKWXGetPayer.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 19, 2021 10:48:15 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zk.wechat.pay.entity.ZKPayGetPayer;

/** 
* @ClassName: ZKWXGetPayer 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ZKWXGetPayer {

    ZKPayGetPayer payGetPayer;

    public ZKWXGetPayer(ZKPayGetPayer payGetPayer) {
        this.payGetPayer = payGetPayer;
    }

    /**
     * 用户标识 openid string[1,128] 是 用户在直连商户appid下的唯一标识。
     * 
     * @return openid sa
     */
    public String getOpenid() {
        return payGetPayer.getOpenid();
    }

}
