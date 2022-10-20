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
* @Title: ZKWXJsPay.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 21, 2021 11:04:34 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay.entity;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import com.zk.wechat.wx.pay.ZKWXPayConstants;
import com.zk.wechat.wx.pay.ZKWXPayUtils;
import com.zk.wechat.wx.utils.ZKWXUtils;

/**
 * jsapi 调起支付的参数对象
 * 
 * @ClassName: ZKWXJsPay
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKWXJsPay {
    
    String appId;
    String timeStamp;
    String nonceStr;
    String prepayId;
    String signType;
    String paySign;

    public ZKWXJsPay(String appId, String prepayId, String timeStamp, ZKWXApiCert apiCert)
            throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        this.nonceStr = ZKWXUtils.getUUID32();
        this.timeStamp = timeStamp;
        this.appId = appId;
        this.prepayId = prepayId;
        this.signType = "RSA";
        
        // 计算签名
        this.sign(apiCert);
    }

    private void sign(ZKWXApiCert apiCert) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {
        String signStr = ZKWXPayUtils.buildJsPaySignStr(this.appId, this.timeStamp, this.nonceStr, this.getPackage());
        this.paySign = ZKWXPayUtils.signSHA256withRSA(signStr.getBytes(), apiCert.getPrivateKey());
    }

    /**
     * 应用ID appId string[1,16] 是 请填写merchant_appid对应的值。示例值：wx8888888888888888
     * 
     * @return appId sa
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 时间戳, 单位秒； timeStamp string[1,32] 是 当前的时间，其他详见时间戳规则。示例值：1414561699
     * 
     * @return timeStamp sa
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * 随机字符串 nonceStr string[1,32] 是
     * 随机字符串，不长于32位。示例值：5K8264ILTKCH16CQ2502SI8ZNMTM67VS
     * 
     * @return nonceStr sa
     */
    public String getNonceStr() {
        return nonceStr;
    }

    /**
     * 订单详情扩展字符串 package string[1,128] 是
     * 统一下单接口返回的prepay_id参数值，提交格式如：prepay_id=***；示例值：prepay_id=wx201410272009395522657a690389285100
     * 
     * @return prepayId sa
     */
    public String getPackage() {
        return String.format("%s=%s", ZKWXPayConstants.ParamName.prepayId, prepayId);
    }

    /**
     * 签名方式 signType string[1,32] 是 签名类型，默认为RSA，仅支持RSA。示例值：RSA
     * 
     * @return signType sa
     */
    public String getSignType() {
        return signType;
    }

    /**
     * 签名 paySign string[1,256] 是
     * 签名，使用字段appId、timeStamp、nonceStr、package计算得出的签名值
     * 
     * @return paySign sa
     */
    public String getPaySign() {
        return paySign;
    }
  

}
