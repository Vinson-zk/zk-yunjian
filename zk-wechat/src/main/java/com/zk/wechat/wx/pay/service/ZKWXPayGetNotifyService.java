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
* @Title: ZKWXPayGetNotifyService.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 20, 2021 9:44:23 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSONObject;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.wechat.pay.entity.ZKPayGetNotify;
import com.zk.wechat.pay.entity.ZKPayGetOrder;
import com.zk.wechat.pay.enumType.ZKPayGetChannel;
import com.zk.wechat.pay.enumType.ZKPayStatus;
import com.zk.wechat.pay.service.ZKPayGetNotifyService;
import com.zk.wechat.pay.service.ZKPayGetOrderService;
import com.zk.wechat.wx.pay.ZKWXPayConstants;
import com.zk.wechat.wx.pay.ZKWXPayUtils;

/** 
* @ClassName: ZKWXPayGetNotifyService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
public class ZKWXPayGetNotifyService {

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    @Autowired
    ZKPayGetNotifyService payGetNotifyService;

    @Autowired
    ZKPayGetOrderService payGetOrderService;

    @Autowired
    ZKWXPayService wxPayService;

    /**
     * 处理支付通知
     * 
     * @Title: payNotify
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 20, 2021 12:36:35 PM
     * @param payGetOrderId
     *            支付的订单ID
     * @param wechatpaySerial
     *            HTTP头 Wechatpay-Serial; 证书序列号
     * @param wechatpaySignature
     *            HTTP头 Wechatpay-Signature; 应答签名
     * @param wechatpayTimestamp
     *            HTTP头 Wechatpay-Timestamp; 中的应答时间戳。
     * @param wechatpayNonce
     *            HTTP头 Wechatpay-Nonce; 中的应答随机串
     * @param bodyStr
     *            响应数据体
     * @return void
     */
    public ZKPayGetNotify payNotify(String payGetOrderId, String wechatpaySerial, String wechatpaySignature,
            String wechatpayTimestamp, String wechatpayNonce, String bodyStr) {
        try {
            ZKPayGetOrder payGetOrder = payGetOrderService.get(new ZKPayGetOrder(payGetOrderId));
            if (payGetOrder == null) {
                log.error("[>_<:20210220-1406-001] 支付订单:[{}] 不存在；", payGetOrderId);
                return null;
            }

            if (ZKPayStatus.SUCCESS == payGetOrder.getPayStatus()) {
                // 如果状态是支付成功，则为重复通知，不处理；
                log.info("[^_^:20210220-1406-002] 支付订单:[{}] 已成功支付，重复通知，不处理；encStr：{}", payGetOrderId, bodyStr);
                return null;
            }

            // 创建通知记录
            ZKPayGetNotify payGetNotify = payGetNotifyService.create(payGetOrderId, wechatpaySerial, wechatpaySignature,
                    wechatpayTimestamp, wechatpayNonce, bodyStr);

            // 校验签名
            if (this.wxPayService.checkSign(payGetOrder.getMchid(), wechatpaySerial, wechatpaySignature,
                    wechatpayTimestamp, wechatpayNonce, bodyStr)) {
                // ----- 签名校验成功
                JSONObject obj = ZKJsonUtils.parseJSONObject(bodyStr);
                // 取 resource 密码
                JSONObject resourceObj = obj.getJSONObject(ZKWXPayConstants.MsgAttr.Notify.resource._name);
                // ----- 取数据加密相关内容
////              加密算法类型   algorithm   string[1,32]    是   对开启结果数据进行加密的加密算法，目前只支持AEAD_AES_256_GCM；示例值：AEAD_AES_256_GCM
//                String algorithm = resourceObj.getString(ZKWXPayConstants.MsgAttr.Notify.resource.algorithm);
//              数据密文    ciphertext  string[1,1048576]   是   Base64编码后的开启/停用结果数据密文；示例值：sadsadsadsad
                String ciphertext = resourceObj.getString(ZKWXPayConstants.MsgAttr.EncKey.ciphertext);
//              附加数据    associated_data string[1,16]    否   附加数据；示例值：fdasfwqewlkja484w
                String associatedData = resourceObj.getString(ZKWXPayConstants.MsgAttr.EncKey.associatedData);
////              原始类型    original_type   string[1,16]    是   原始回调类型，为transaction；示例值：transaction
//                String originalType = resourceObj.getString(ZKWXPayConstants.MsgAttr.Notify.resource.originalType);
//              随机串 nonce   string[1,16]    是   加密使用的随机串；示例值：fdasflkja484w
                String nonce = resourceObj.getString(ZKWXPayConstants.MsgAttr.EncKey.nonce);

                // ----- 解密 byte[] apiv3Aeskey, byte[] associatedData, byte[] nonce

                String decStr = ZKWXPayUtils.decryptToString(ciphertext,
                        this.wxPayService.getApiv3AesKey(payGetOrder.getMchid()), associatedData.getBytes(),
                        nonce.getBytes());
                JSONObject decObj = ZKJsonUtils.parseJSONObject(decStr);
                // 交易状态 trade_state string[1,32] 是 交易状态，枚举值：SUCCESS：支付成功; REFUND：转入退款; NOTPAY：未支付; CLOSED：已关闭; REVOKED：已撤销（付款码支付）; USERPAYING：用户支付中（付款码支付）; PAYERROR：支付失败(其他原因，如银行返回失败)
                ZKPayStatus tradeState = ZKPayStatus.valueOf(decObj.getString(ZKWXPayConstants.MsgAttr.Notify.resource.tradeState));
             // 交易类型 trade_type string[1,16] 否 交易类型，枚举值：JSAPI：公众号支付;NATIVE：扫码支付; APP：APP支付; MICROPAY：付款码支付; MWEB：H5支付;FACEPAY：刷脸支付
                ZKPayGetChannel tradeType = ZKPayGetChannel.valueOf(decObj.getString(ZKWXPayConstants.MsgAttr.Notify.resource.tradeType));

                this.payGetOrderService.updatePayChannel(payGetOrder.getPkId(), tradeState, tradeType);
                payGetNotify.setDisposeStatus(ZKPayGetNotify.DisposeStatus.succees);
                payGetNotify.setResource(ZKJsonUtils.parseZKJson(decStr));
                payGetNotifyService.save(payGetNotify);
            }
            else {
                // 签名校验失败
                payGetNotify.setDisposeStatus(ZKPayGetNotify.DisposeStatus.signErr);
                payGetNotifyService.save(payGetNotify);
            }
            return payGetNotify;
        }
        catch(Exception e) {
            log.error(
                    "[>_<:20210220-1558-001] 处理支付通知失败；payGetOrderId: {}; wechatpaySignature: {}, wechatpayTimestamp: {}, wechatpayNonce: {}, bodyStr: {}",
                    payGetOrderId, wechatpaySignature, wechatpayTimestamp, wechatpayNonce, bodyStr);
            e.printStackTrace();
        }
        return null;

    }

}
