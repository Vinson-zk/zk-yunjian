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
* @Title: ZKWXPayJsApiController.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 21, 2021 8:51:13 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zk.core.commons.ZKMsgRes;
import com.zk.wechat.pay.entity.ZKPayGetOrder;
import com.zk.wechat.wx.pay.entity.ZKWXJsPay;
import com.zk.wechat.wx.pay.service.ZKWXPayJsApiService;

/** 
* @ClassName: ZKWXPayJsApiController 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@RestController
@RequestMapping("${zk.path.admin}/${zk.path.wechat}/${zk.wechat.version}/${zk.path.wechat.wx.pay}/jsapi")
public class ZKWXPayJsApiController {
    
    @Autowired
    ZKWXPayJsApiService wxPayJsApiService;

    /**
     * jsapi 统一下单
     *
     * @Title: jsapi
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 21, 2021 8:43:29 PM
     * @param appChannelCode
     *            后台系统的支付渠道代码；其对象，绑定对应的商户号 mchid 和 应用ID appid
     * @param businessCode
     *            支付的收款业务代码；不同业务定义不同接口；0-垃圾分类业务；
     * @param description
     *            是 string[1,127] 商品描述 body 商品描述；示例值：Image形象店-深圳腾大-QQ公仔
     * @param attach
     *            否 string[1,128] 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用；示例值：自定义数据
     * @param goodsTag
     *            否 string[1,32] 订单优惠标记；示例值：WXG
     * @param openid
     *            是string[1,128] 用户标识,
     *            用户在直连商户appid下的唯一标识。示例值：oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
     * @param amountTotal
     *            是 int 订单金额, 订单总金额，单位为分。示例值：100
     * @param amountCurrency
     *            否 string[1,16] 货币类型 CNY：人民币，境内商户号仅支持人民币。示例值：CNY
     * @param businessNo
     *            业务订单号；
     * @param req
     * @return
     * @return String
     * @throws IOException
     * @throws SignatureException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    @RequestMapping(value = "put/{payGroupCode}/{businessCode}", method = RequestMethod.POST)
    public ZKMsgRes put(
            @PathVariable("payGroupCode")String payGroupCode,
            @PathVariable("businessCode")String businessCode,
            @RequestParam("businessNo")String businessNo, 
            @RequestParam("description")String description, 
            @RequestParam(value = "attach", required = false)String attach, 
            @RequestParam(value = "goodsTag", required = false) String goodsTag, 
            @RequestParam("openid")String openid, 
            @RequestParam("amountTotal")int amountTotal, 
            @RequestParam(value = "amountCurrency", required = false, defaultValue = "CNY")String amountCurrency)
            throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, IOException {
        
        // 统一下单
        ZKPayGetOrder payGetOrder = this.wxPayJsApiService.jsapi(payGroupCode, businessCode, businessNo, description,
                attach, goodsTag, openid, amountTotal, amountCurrency);
                
        // 生成 jsApi 调起支付对象
        ZKWXJsPay jsApiPay = this.wxPayJsApiService.genJsApiPayParams(payGetOrder);

        return this.res(payGetOrder, jsApiPay);
    }
    
    protected ZKMsgRes res(ZKPayGetOrder payGetOrder, ZKWXJsPay jsApiPay) {
        Map<String, Object> resData = new HashMap<>();
        resData.put("payGetOrderPkId", payGetOrder.getPkId());
        resData.put("jsApiPay", jsApiPay);
        return ZKMsgRes.asOk(null, resData);
    }

    @RequestMapping(value = "putAgain", method = RequestMethod.POST)
    public ZKMsgRes putAgain(@RequestParam("payGetOrderId")
    String payGetOrderId)
            throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, IOException {
        
        // 统一下单
        ZKPayGetOrder payGetOrder = this.wxPayJsApiService.jsapi(payGetOrderId);
                
        // 生成 jsApi 调起支付对象
        ZKWXJsPay jsApiPay = this.wxPayJsApiService.genJsApiPayParams(payGetOrder);

        return this.res(payGetOrder, jsApiPay);
    }

    @RequestMapping(value = "close", method = RequestMethod.POST)
    public ZKMsgRes close(@RequestParam("payGetOrderId")String payGetOrderId)
            throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, IOException {
        
        // 统一下单
//        ZKPayGetOrder payGetOrder = this.wxPayJsApiService.close(payGetOrderId);
        this.wxPayJsApiService.close(payGetOrderId);

        return ZKMsgRes.asOk(null, null);
    }


}
