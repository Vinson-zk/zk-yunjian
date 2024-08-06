package com.zk.wechat.wx.pay.entity.nativeapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zk.core.utils.ZKDateUtils;
import com.zk.wechat.pay.entity.ZKPayGetOrder;
import com.zk.wechat.wx.pay.entity.ZKWXGetAmount;

/**
 * 未实现持久化设计，只是实现了调起支付设置
 * 商户号-服务商 native 下单实体
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description:
 * @ClassName ZKWXV3PartnerNativeApiOrder
 * @Package com.zk.wechat.wx.pay.entity.nativeapi
 * @PROJECT zk-yunjian
 * @Author bs
 * @DATE 2022-10-27 14:14:04
 **/
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ZKWXV3PartnerNativeApiOrder {

    ZKPayGetOrder payGetOrder;

    ZKWXGetAmount amount = null;

    private ZKWXV3PartnerNativeApiOrder(){

    }

    public static ZKWXV3PartnerNativeApiOrder asByZKPayGetOrder(ZKPayGetOrder payGetOrder) {
        ZKWXV3PartnerNativeApiOrder apiOrder = new ZKWXV3PartnerNativeApiOrder();
        apiOrder.payGetOrder = payGetOrder;

        if(payGetOrder.getPayGetAmount() != null) {
            apiOrder.amount = new ZKWXGetAmount(payGetOrder.getPayGetAmount());
        }
        return apiOrder;
    }

    /** 正式做为功能时，此字段应该引用持久化内容，不需要再 **/
    private String sp_appid;
    private String sp_machid;
    private String sub_appid;
    private String sub_machid;

    public void setSp_appid(String sp_appid) {
        this.sp_appid = sp_appid;
    }
    public void setSp_machid(String sp_machid) {
        this.sp_machid = sp_machid;
    }
    public void setSub_appid(String sub_appid) {
        this.sub_appid = sub_appid;
    }
    public void setSub_machid(String sub_machid) {
        this.sub_machid = sub_machid;
    }
    // -----------------------------

    /**
     * 服务商应用ID	sp_appid	string[1,32]	是	body
     * 服务商申请的公众号appid。 示例值：wx8888888888888888
     *
     * @return appid sa
     */
    public String getSp_appid() {
        return sp_appid;
    }

    /**
     * 服务商户号	sp_mchid	string[1,32]	是	body
     * 服务商户号，由微信支付生成并下发 示例值：1230000109
     * @return mchid sa
     */
    public String getSp_mchid() {
        return sp_machid;
    }

    /**
     * 子商户应用ID	sub_appid	string[1,32]	否	body
     * 子商户申请的公众号appid。示例值：wxd678efh567hg6999
     * @return
     */
    public String getSub_appid() {
        return sub_appid;
    }

    /**
     * 子商户号	sub_mchid	string[1,32]	是	body
     * 子商户的商户号，由微信支付生成并下发。 示例值：1900000109
     * @return
     */
    public String getSub_mchid() {
        return sub_machid;
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

//  +优惠功能   detail  object  否   body 优惠功能
//  +场景信息   scene_info  object  否   body 支付场景描述

}
