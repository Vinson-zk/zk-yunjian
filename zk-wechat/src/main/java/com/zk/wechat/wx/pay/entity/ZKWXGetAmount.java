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
* @Title: ZKWXGetAmount.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 19, 2021 10:50:40 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zk.wechat.pay.entity.ZKPayGetAmount;

/** 
* @ClassName: ZKWXGetAmount 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ZKWXGetAmount {

    ZKPayGetAmount payGetAmount;

    public ZKWXGetAmount(ZKPayGetAmount payGetAmount) {
        this.payGetAmount = payGetAmount;
    }

    /**
     * 总金额 total int 是 订单总金额，单位为分。
     * 
     * @return total sa
     */
    public int getTotal() {
        return payGetAmount.getTotal();
    }

    /**
     * 货币类型 currency string[1,16] 否 CNY：人民币，境内商户号仅支持人民币; 示例值：CNY。
     * 
     * @return currency sa
     */
    public String getCurrency() {
        return payGetAmount.getCurrency().name();
    }

}
