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
* @Title: ZKPayGetNotify.java 
* @author Vinson 
* @Package com.zk.wechat.pay.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 19, 2021 11:34:31 PM 
* @version V1.0 
*/
package com.zk.wechat.pay.entity;

import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotNull;

/**
 * 微信支付-收款记录的回调通知
 * 
 * @ClassName: ZKPayGetNotify
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@ZKTable(name = "t_wx_pay_get_notify", alias = "wxPayGetNotify")
public class ZKPayGetNotify extends ZKBaseEntity<String, ZKPayGetNotify> {

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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKPayGetNotify());
        }
        return sqlHelper;
    }

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    /**
     * 处理状态；0-未处理；1-处理成功；2-重复通知不处理；3-校验签名异常；
     * 
     * @ClassName: DisposeStatus
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface DisposeStatus {

        /**
         * 0-未处理；
         */
        public static final int undisposed = 0;

        /**
         * 1-处理成功；
         */
        public static final int succees = 1;

        /**
         * 2-重复通知不处理；
         */
        public static final int repetitive = 2;

        /**
         * 3-校验签名异常；
         */
        public static final int signErr = 3;
    }

    public ZKPayGetNotify() {
        super();
    }

    public ZKPayGetNotify(String pkId) {
        super(pkId);
    }

    // 对应支付记录
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 20, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_wx_pay_order_pk_id")
    String payOrderPkId;

    // 微信支付的应答签名 Wechatpay-Signature
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 2048, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_wx_wechatpay_signature")
    String wxWechatpaySignature;

    // HTTP头Wechatpay-Timestamp 中的应答时间戳。
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 32, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_wx_wechatpay_timestam")
    String wxWechatpayTimestamp;

    // HTTP头Wechatpay-Nonce 中的应答随机串
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 32, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_wx_wechatpay_nonce")
    String wxWechatpayNonce;

    // HTTP头 Wechatpay-Serial; 证书序列号
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_wx_wechatpay_serial")
    String wxWechatpaySerial;

    // 通知数据 object 是 通知资源数据
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_wx_body")
    String wxBody;

    // 处理状态；0-未处理；1-处理成功；2-重复通知不处理；3-校验签名异常；
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 9, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_dispose_status", update = @ZKUpdate(true))
    Integer disposeStatus;

    // 解密明文
    @ZKColumn(name = "c_resource", update = @ZKUpdate(true))
    ZKJson resource;

    // 校验签名时，后台系统生成的签名；
    @Length(min = 1, max = 2048, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_signature", update = @ZKUpdate(true))
    String signature;

    /**
     * @return payOrderPkId sa
     */
    public String getPayOrderPkId() {
        return payOrderPkId;
    }


    /**
     * @param payOrderPkId
     *            the payOrderPkId to set
     */
    public void setPayOrderPkId(String payOrderPkId) {
        this.payOrderPkId = payOrderPkId;
    }

    /**
     * @return wxWechatpaySignature sa
     */
    public String getWxWechatpaySignature() {
        return wxWechatpaySignature;
    }


    /**
     * @param wxWechatpaySignature
     *            the wxWechatpaySignature to set
     */
    public void setWxWechatpaySignature(String wxWechatpaySignature) {
        this.wxWechatpaySignature = wxWechatpaySignature;
    }

    /**
     * @return wxWechatpayTimestamp sa
     */
    public String getWxWechatpayTimestamp() {
        return wxWechatpayTimestamp;
    }


    /**
     * @param wxWechatpayTimestamp
     *            the wxWechatpayTimestamp to set
     */
    public void setWxWechatpayTimestamp(String wxWechatpayTimestamp) {
        this.wxWechatpayTimestamp = wxWechatpayTimestamp;
    }

    /**
     * @return wxWechatpayNonce sa
     */
    public String getWxWechatpayNonce() {
        return wxWechatpayNonce;
    }

    /**
     * @param wxWechatpayNonce
     *            the wxWechatpayNonce to set
     */
    public void setWxWechatpayNonce(String wxWechatpayNonce) {
        this.wxWechatpayNonce = wxWechatpayNonce;
    }

    /**
     * @return wxWechatpaySerial sa
     */
    public String getWxWechatpaySerial() {
        return wxWechatpaySerial;
    }

    /**
     * @param wxWechatpaySerial
     *            the wxWechatpaySerial to set
     */
    public void setWxWechatpaySerial(String wxWechatpaySerial) {
        this.wxWechatpaySerial = wxWechatpaySerial;
    }

    /**
     * @return wxBody sa
     */
    public String getWxBody() {
        return wxBody;
    }

    /**
     * @param wxBody
     *            the wxBody to set
     */
    public void setWxBody(String wxBody) {
        this.wxBody = wxBody;
    }

    /**
     * @return disposeStatus sa
     */
    public Integer getDisposeStatus() {
        return disposeStatus;
    }


    /**
     * @param disposeStatus
     *            the disposeStatus to set
     */
    public void setDisposeStatus(Integer disposeStatus) {
        this.disposeStatus = disposeStatus;
    }

    /**
     * @return resource sa
     */
    public ZKJson getResource() {
        return resource;
    }


    /**
     * @param resource
     *            the resource to set
     */
    public void setResource(ZKJson resource) {
        this.resource = resource;
    }

    /**
     * @return signature sa
     */
    public String getSignature() {
        return signature;
    }

    /**
     * @param signature
     *            the signature to set
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

}
