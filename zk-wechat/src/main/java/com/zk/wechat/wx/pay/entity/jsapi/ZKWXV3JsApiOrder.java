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
* @Title: ZKWXJsApiOrder.java 
* @author Vinson 
* @Package com.zk.wechat.wx.pay.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 19, 2021 10:53:29 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.pay.entity.jsapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zk.core.utils.ZKDateUtils;
import com.zk.wechat.pay.entity.ZKPayGetOrder;
import com.zk.wechat.wx.pay.entity.ZKWXGetAmount;
import com.zk.wechat.wx.pay.entity.ZKWXGetPayer;

/**
 * 商户号 jsapi 下单实体
* @ClassName: ZKWXJsApiOrder 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ZKWXV3JsApiOrder {

    ZKPayGetOrder payGetOrder;

    ZKWXGetPayer payer = null;

    ZKWXGetAmount amount = null;

    public ZKWXV3JsApiOrder(ZKPayGetOrder payGetOrder) {
        this.payGetOrder = payGetOrder;
        
        if(payGetOrder.getPayGetAmount() != null) {
            this.amount = new ZKWXGetAmount(payGetOrder.getPayGetAmount());
        }
        if(payGetOrder.getPayGetPayer() != null) {
            this.payer = new ZKWXGetPayer(payGetOrder.getPayGetPayer());
        }
    }

    /**
     * 应用ID appid string[1,32] 是 body
     * 直连商户申请的公众号或移动应用appid。示例值：wxd678efh567hg6787
     * 
     * @return appid sa
     */
    public String getAppid() {
        return payGetOrder.getAppid();
    }

    /**
     * 直连商户号 mchid string[1,32] 是 body 直连商户的商户号，由微信支付生成并下发。 示例值：1230000109
     * 
     * @return mchid sa
     */
    public String getMchid() {
        return payGetOrder.getMchid();
    }

    /**
     * 商品描述 description string[1,127] 是 body 商品描述; 示例值：Image形象店-深圳腾大-QQ公仔
     * 
     * @return description sa
     */
    public String getDescription() {
        return payGetOrder.getDescriptionRename();
    }

    /**
     * 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一
     * 
     * @return out_trade_no sa
     */
    public String getOut_trade_no() {
        return payGetOrder.getPkId();
    }

    /**
     * 订单失效时间，遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss+TIMEZONE，YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC
     * 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日
     * 13点29分35秒。示例值：2018-06-08T10:34:56+08:00
     * 
     * @return time_expire sa
     */
    public String getTime_expire() {
        return ZKDateUtils.formatDate(payGetOrder.getTimeExpire(), ZKDateUtils.DF_yyyy_MM_ddTHH_mm_ssZZ);
    }

    /**
     * 附加数据 attach string[1,128] 否 body 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用;
     * 示例值：自定义数据
     * 
     * @return attach sa
     */
    public String getAttach() {
        return payGetOrder.getAttach();
    }

    /**
     * 通知地址 notify_url string[1,256] 是 body
     * 通知URL必须为直接可访问的URL，不允许携带查询串，要求必须为https地址。格式：URL，示例值：https://www.weixin.qq.com/wxpay/pay.php
     * 
     * @return notify_url sa
     */
    public String getNotify_url() {
        return payGetOrder.getNotifyUrl();
    }

    /**
     * 订单优惠标记 goods_tag string[1,32] 否 body 订单优惠标记; 示例值：WXG
     * 
     * @return goods_tag sa
     */
    public String getGoods_tag() {
        return payGetOrder.getGoodsTag();
    }

    /**
     * +订单金额 amount object 是 body 订单金额信息
     * 
     * @return amount sa
     */
//    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public ZKWXGetAmount getAmount() {
        return amount;
    }

    /**
     * +支付者 payer object 是 body 支付者信息
     * 
     * @return payer sa
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public ZKWXGetPayer getPayer() {
        return payer;
    }

//  +优惠功能   detail  object  否   body 优惠功能
//  +场景信息   scene_info  object  否   body 支付场景描述

}
