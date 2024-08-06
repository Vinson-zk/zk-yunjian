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
* @Title: ZKPayStatus.java 
* @author Vinson 
* @Package com.zk.wechat.pay.enumType 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 22, 2021 2:37:20 PM 
* @version V1.0 
*/
package com.zk.wechat.pay.enumType;
/** 
* @ClassName: ZKPayStatus 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public enum ZKPayStatus {

    /**
     * 创建
     */
    CREATE,
    /**
     * 支付成功;
     */
    SUCCESS,
    /**
     * 转入退款;
     */
    REFUND,
    /**
     * 未支付;
     */
    NOTPAY,
    /**
     * 已关闭;
     */
    CLOSED,
    /**
     * 已撤销（付款码支付）;
     */
    REVOKED,
    /**
     * 用户支付中（付款码支付）;
     */
    USERPAYING,
    /**
     * 支付失败(其他原因，如银行返回失败)
     */
    PAYERROR;

}
