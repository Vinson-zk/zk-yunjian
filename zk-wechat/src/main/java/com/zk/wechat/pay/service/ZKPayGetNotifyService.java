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
* @Title: ZKPayGetNotifyService.java 
* @author Vinson 
* @Package com.zk.wechat.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 19, 2021 11:56:01 PM 
* @version V1.0 
*/
package com.zk.wechat.pay.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.wechat.pay.dao.ZKPayGetNotifyDao;
import com.zk.wechat.pay.entity.ZKPayGetNotify;

/** 
* @ClassName: ZKPayGetNotifyService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
@Transactional(readOnly = true)
public class ZKPayGetNotifyService extends ZKBaseService<String, ZKPayGetNotify, ZKPayGetNotifyDao> {

    @Transactional(readOnly = false)
    public ZKPayGetNotify create(String payGetOrderId, String wechatpaySerial, String wechatpaySignature,
            String wechatpayTimestamp, String wechatpayNonce, String wxBody) {
        ZKPayGetNotify payGetNotify = new ZKPayGetNotify();
        payGetNotify.setPayOrderPkId(payGetOrderId);
        payGetNotify.setWxWechatpaySignature(wechatpaySignature);
        payGetNotify.setWxWechatpayNonce(wechatpayNonce);
        payGetNotify.setWxWechatpayTimestamp(wechatpayTimestamp);
        payGetNotify.setWxWechatpaySerial(wechatpaySerial);
        payGetNotify.setWxBody(wxBody);
        payGetNotify.setDisposeStatus(ZKPayGetNotify.DisposeStatus.undisposed);

        this.save(payGetNotify);
        return payGetNotify;
    }

}
