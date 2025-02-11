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
* @Title: ZKPayGetOrderHistory.java 
* @author Vinson 
* @Package com.zk.wechat.pay.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 22, 2021 9:38:33 AM 
* @version V1.0 
*/
package com.zk.wechat.pay.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKTable;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;
import com.zk.wechat.pay.enumType.ZKPayGetChannel;
import com.zk.wechat.pay.enumType.ZKPayStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlTransient;

/** 
* @ClassName: ZKPayGetOrderHistory 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKTable(name = "t_wx_pay_get_order_history", alias = "wxPayGetOrderHistory")
public class ZKPayGetOrderHistory extends ZKBaseEntity<String, ZKPayGetOrderHistory> {

    static ZKDBSqlHelper sqlHelper;

    @Transient
    @XmlTransient
    @JsonIgnore
    @Override
    public ZKDBSqlHelper getSqlHelper() {
        return sqlHelper();
    }

    public static ZKDBSqlHelper sqlHelper() {
        if (sqlHelper == null) {
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKPayGetOrderHistory());
        }
        return sqlHelper;
    }

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    public ZKPayGetOrderHistory() {
        super();
    }

    public ZKPayGetOrderHistory(String pkId) {
        super(pkId);
    }

    public static ZKPayGetOrderHistory as(ZKPayGetOrder payGetOrder) {
        ZKPayGetOrderHistory e = new ZKPayGetOrderHistory();
        // 一个 payGetOrder 只会有一条历史记录
        e.setPayGetOrderId(payGetOrder.getPkId());
        e.setAppid(payGetOrder.getAppid());
        e.setAttach(payGetOrder.getAttach());
        e.setBusinessCode(payGetOrder.getBusinessCode());
        e.setBusinessNo(payGetOrder.getBusinessNo());
        e.setDelFlag(payGetOrder.getDelFlag());
        e.setDescriptionRename(payGetOrder.getDescriptionRename());
        e.setGoodsTag(payGetOrder.getGoodsTag());
        e.setMchid(payGetOrder.getMchid());
        e.setNotifyUrl(payGetOrder.getNotifyUrl());
        e.setPayGroupCode(payGetOrder.getPayGroupCode());
        e.setpDesc(payGetOrder.getpDesc());
        e.setPrepayId(payGetOrder.getPrepayId());
        e.setPrepayIdDate(payGetOrder.getPrepayIdDate());
        e.setRemarks(payGetOrder.getRemarks());
        e.setSpare1(payGetOrder.getSpare1());
        e.setSpare2(payGetOrder.getSpare2());
        e.setSpare3(payGetOrder.getSpare3());
        e.setSpareJson(payGetOrder.getSpareJson());
        e.setPayStatus(payGetOrder.getPayStatus());
        e.setTimeExpire(payGetOrder.getTimeExpire());
        e.setVersion(payGetOrder.getVersion());
        e.setWxChannel(payGetOrder.getWxChannel());
        e.setWxResStatusCode(payGetOrder.getWxResStatusCode());
        e.setPaySignDate(payGetOrder.getPaySignDate());
        e.setHistoryDate(payGetOrder.getCreateDate());
        return e;
    }

    // 支付订单号，可关联查询之前支付订单的其他信息。
    @Length(min = 1, max = 20, message = "{zk.core.data.validation.length}")
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_pay_get_order_id")
    protected String payGetOrderId;

    // 应用ID appid string[1,32] 是 body
    // 直连商户申请的公众号或移动应用appid。示例值：wxd678efh567hg6787
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 32, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_wx_appid")
    String appid;

    // 直连商户号 mchid string[1,32] 是 body 直连商户的商户号，由微信支付生成并下发。 示例值：1230000109
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 32, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_wx_mchid")
    String mchid;

    // 商品描述 description string[1,127] 是 body 商品描述; 示例值：Image形象店-深圳腾大-QQ公仔
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 127, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_wx_description")
    String descriptionRename; // 防止不同系统中此字符是保留关键字

    // 交易结束时间 time_expire string[1,64] 否 body
    // 订单失效时间，遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss+TIMEZONE，YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC
    // 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日
    // 13点29分35秒。示例值：2018-06-08T10:34:56+08:00
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_wx_time_expire")
    Date timeExpire;

    // 附加数据 attach string[1,128] 否 body 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用;
    // 示例值：自定义数据
    @Length(min = 1, max = 128, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_wx_attach")
    String attach;

    // 通知地址 notify_url string[1,256] 是 body
    // 通知URL必须为直接可访问的URL，不允许携带查询串，要求必须为https地址。
    // 格式：URL，示例值：https://www.weixin.qq.com/wxpay/pay.php
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 256, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_wx_notify_url")
    String notifyUrl;

    // 订单优惠标记 goods_tag string[1,32] 否 body 订单优惠标记; 示例值：WXG
    @Length(min = 1, max = 32, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_wx_goods_tag")
    String goodsTag;

    // 统一下单时，微信平台响应状态码
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_wx_res_status_code")
    String wxResStatusCode;

    // 微信生成的预支付交易会话标识。用于后续接口调用中使用，该值有效期为2小时；示例值：wx201410272009395522657a690389285100
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_wx_prepay_id")
    String prepayId;

    // 预支付交易会话标识的更新时间
    @ZKColumn(name = "c_wx_prepay_id_date")
    Date prepayIdDate;

    // 调起支付签名生成时间；
    @ZKColumn(name = "c_pay_sign_date")
    String paySignDate;

    // +订单金额 amount object 是 body 订单金额信息
    @Transient
    ZKPayGetAmount payGetAmount;

    // +支付者 payer object 是 body 支付者信息
    @Transient
    ZKPayGetPayer payGetPayer;

    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////

    // 支付状态
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_pay_status")
    ZKPayStatus payStatus;

    // 微信支付平台-支付渠道；JSAPI, H5, APP, NATIVE, MINIPROGRAM;
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_wx_channel")
    ZKPayGetChannel wxChannel;

    // 支付关系组代码；绑定对应的商户号 mchid 和 应用ID appid
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 32, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_pay_group_code")
    String payGroupCode;

    // 支付的收款业务代码；不同业务定义不同接口；0-垃圾分类业务；
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 16, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_business_code")
    String businessCode;

    // 业务订单号
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_business_no")
    String businessNo;

    // 业务订单创建时间
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_history_date")
    Date historyDate;

    /**
     * @return payGetOrderId sa
     */
    public String getPayGetOrderId() {
        return payGetOrderId;
    }

    /**
     * @param payGetOrderId
     *            the payGetOrderId to set
     */
    public void setPayGetOrderId(String payGetOrderId) {
        this.payGetOrderId = payGetOrderId;
    }

    /**
     * @return appid sa
     */
    public String getAppid() {
        return appid;
    }

    /**
     * @param appid
     *            the appid to set
     */
    public void setAppid(String appid) {
        this.appid = appid;
    }

    /**
     * @return mchid sa
     */
    public String getMchid() {
        return mchid;
    }

    /**
     * @param mchid
     *            the mchid to set
     */
    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    /**
     * @return descriptionRename sa
     */
    public String getDescriptionRename() {
        return descriptionRename;
    }

    /**
     * @param descriptionRename
     *            the descriptionRename to set
     */
    public void setDescriptionRename(String descriptionRename) {
        this.descriptionRename = descriptionRename;
    }

    /**
     * @return timeExpire sa
     */
    public Date getTimeExpire() {
        return timeExpire;
    }

    /**
     * @param timeExpire
     *            the timeExpire to set
     */
    public void setTimeExpire(Date timeExpire) {
        this.timeExpire = timeExpire;
    }

    /**
     * @return attach sa
     */
    public String getAttach() {
        return attach;
    }

    /**
     * @param attach
     *            the attach to set
     */
    public void setAttach(String attach) {
        this.attach = attach;
    }

    /**
     * 
     * @return notifyUrl sa
     */
    public String getNotifyUrl() {
        return notifyUrl;
    }

    /**
     * @param notifyUrl
     *            the notifyUrl to set
     */
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    /**
     * @return goodsTag sa
     */
    public String getGoodsTag() {
        return goodsTag;
    }

    /**
     * @param goodsTag
     *            the goodsTag to set
     */
    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }

    /**
     * @return payGetAmount sa
     */
    public ZKPayGetAmount getPayGetAmount() {
        return payGetAmount;
    }

    /**
     * @param payGetAmount
     *            the payGetAmount to set
     */
    public void setPayGetAmount(ZKPayGetAmount payGetAmount) {
        this.payGetAmount = payGetAmount;
    }

    /**
     * @return payGetPayer sa
     */
    public ZKPayGetPayer getPayGetPayer() {
        return payGetPayer;
    }

    /**
     * @param payGetPayer
     *            the payGetPayer to set
     */
    public void setPayGetPayer(ZKPayGetPayer payGetPayer) {
        this.payGetPayer = payGetPayer;
    }

    /**
     * @return payStatus sa
     */
    public ZKPayStatus getPayStatus() {
        return payStatus;
    }

    /**
     * @param payStatus
     *            the payStatus to set
     */
    public void setPayStatus(ZKPayStatus payStatus) {
        this.payStatus = payStatus;
    }

    /**
     * @return businessNo sa
     */
    public String getBusinessNo() {
        return businessNo;
    }

    /**
     * @param businessNo
     *            the businessNo to set
     */
    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    /**
     * @return prepayId sa
     */
    public String getPrepayId() {
        return prepayId;
    }

    /**
     * @param prepayId
     *            the prepayId to set
     */
    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    /**
     * @return prepayIdDate sa
     */
    public Date getPrepayIdDate() {
        return prepayIdDate;
    }

    /**
     * @param prepayIdDate
     *            the prepayIdDate to set
     */
    public void setPrepayIdDate(Date prepayIdDate) {
        this.prepayIdDate = prepayIdDate;
    }

    /**
     * @return wxResStatusCode sa
     */
    public String getWxResStatusCode() {
        return wxResStatusCode;
    }

    /**
     * @param wxResStatusCode
     *            the wxResStatusCode to set
     */
    public void setWxResStatusCode(String wxResStatusCode) {
        this.wxResStatusCode = wxResStatusCode;
    }

    /**
     * @return businessCode sa
     */
    public String getBusinessCode() {
        return businessCode;
    }

    /**
     * @param businessCode
     *            the businessCode to set
     */
    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    /**
     * @return wxChannel sa
     */
    public ZKPayGetChannel getWxChannel() {
        return wxChannel;
    }

    /**
     * @param wxChannel
     *            the wxChannel to set
     */
    public void setWxChannel(ZKPayGetChannel wxChannel) {
        this.wxChannel = wxChannel;
    }

    /**
     * @return payGroupCode sa
     */
    public String getPayGroupCode() {
        return payGroupCode;
    }

    /**
     * @param payGroupCode
     *            the payGroupCode to set
     */
    public void setPayGroupCode(String payGroupCode) {
        this.payGroupCode = payGroupCode;
    }

    /**
     * @return paySignDate sa
     */
    public String getPaySignDate() {
        return paySignDate;
    }

    /**
     * @param paySignDate
     *            the paySignDate to set
     */
    public void setPaySignDate(String paySignDate) {
        this.paySignDate = paySignDate;
    }

    /**
     * @return historyDate sa
     */
    public Date getHistoryDate() {
        return historyDate;
    }

    /**
     * @param historyDate
     *            the historyDate to set
     */
    public void setHistoryDate(Date historyDate) {
        this.historyDate = historyDate;
    }

}
