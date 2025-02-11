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
* @Title: ZKPayGetPayer.java 
* @author Vinson 
* @Package com.zk.wechat.pay.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 19, 2021 9:49:34 PM 
* @version V1.0 
*/
package com.zk.wechat.pay.entity;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKTable;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * 微信支付-收款时，支付用户表，在 jsapi 使用支付时，支付记录才有对应的支付用户
 * 
 * @ClassName: ZKPayGetPayer
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */

@ZKTable(name = "t_wx_pay_get_payer", alias = "wxPayGetPayer")
public class ZKPayGetPayer extends ZKBaseEntity<String, ZKPayGetPayer> {

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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKPayGetPayer());
        }
        return sqlHelper;
    }

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    public ZKPayGetPayer() {
        super();
    }

    public ZKPayGetPayer(String openid) {
        super();
        this.openid = openid;
    }

    public static ZKPayGetPayer as(String openid) {
        return new ZKPayGetPayer(openid);
    }

    // 对应支付记录
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 20, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_wx_pay_order_pk_id")
    String payOrderPkId;

    // 用户标识 openid string[1,128] 是 用户在直连商户appid下的唯一标识。
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 128, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_wx_openid")
    String openid;

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
     * @return openid sa
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @param openid
     *            the openid to set
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

}
