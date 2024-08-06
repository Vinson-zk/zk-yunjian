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
* @Title: ZKWXPayNotifyController.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 21, 2021 11:21:44 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay.controller;

import java.nio.charset.Charset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.utils.ZKServletUtils;
import com.zk.wechat.wx.pay.ZKWXPayConstants;
import com.zk.wechat.wx.pay.service.ZKWXPayGetNotifyService;

import jakarta.servlet.http.HttpServletRequest;

/** 
* @ClassName: ZKWXPayNotifyController 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RestController
@RequestMapping("${zk.path.admin}/${zk.path.wechat}/${zk.wechat.version}/${zk.path.wechat.wx.pay}/notify")
public class ZKWXPayNotifyController {

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    @Autowired
    ZKWXPayGetNotifyService wxPayGetNotifyService;

    /**
     * 用于接收支付结果通知
     *
     * @Title: authNotification
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 20, 2021 10:58:36 AM
     * @param payGetOrderId
     *            支付的订单ID
     * @param req
     * @return
     * @return String
     */
    @RequestMapping(value = "payNotify/{payGetOrderId}")
    public String authNotification(@PathVariable("payGetOrderId")
    String payGetOrderId, HttpServletRequest req) {

        try {
            // HTTP头 Wechatpay-Signature; 应答签名
            String wechatpaySignature = req.getHeader(ZKWXPayConstants.ZKReqHeader.WechatpaySignature);
            // HTTP头 Wechatpay-Timestamp; 中的应答时间戳。
            String wechatpayTimestamp = req.getHeader(ZKWXPayConstants.ZKReqHeader.WechatpayTimestamp);
            // HTTP头 Wechatpay-Nonce; 中的应答随机串
            String wechatpayNonce = req.getHeader(ZKWXPayConstants.ZKReqHeader.WechatpayNonce);
            // HTTP头 Wechatpay-Serial; 证书序列号
            String wechatpaySerial = req.getHeader(ZKWXPayConstants.ZKReqHeader.WechatpaySerial);

            wechatpaySerial = ZKStringUtils.isEmpty(wechatpaySerial) ? null : wechatpaySerial;

            // 1、从请求流中读出字节
            byte[] msgBytes = ZKServletUtils.read(req);
            // 2、响应数据体
            String bodyStr = new String(msgBytes, Charset.forName("UTF-8"));
            // 3、处理响应
            wxPayGetNotifyService.payNotify(payGetOrderId, wechatpaySerial, wechatpaySignature, wechatpayTimestamp,
                    wechatpayNonce, bodyStr);
        }
        catch(Exception e) {
            e.printStackTrace();
            log.error("[>_<:20210220-1238-001] 接收支付通知时出错了！payGetOrderId:{} ", payGetOrderId);
        }
        return "success";
    }
}
