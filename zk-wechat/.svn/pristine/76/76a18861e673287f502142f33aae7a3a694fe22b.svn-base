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
* @Title: ZKThirdParty.java 
* @author Vinson 
* @Package com.zk.wechat.thirdParty.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 18, 2021 10:39:20 AM 
* @version V1.0 
*/
package com.zk.wechat.thirdParty.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.utils.ZKDateUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKTable;
import com.zk.db.commons.ZKSqlConvert;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.commons.ZKSqlProvider;

/** 
* @ClassName: ZKThirdParty 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKTable(name = "t_wx_third_party", alias = "wxThirdParty")
public class ZKThirdParty extends ZKBaseEntity<String, ZKThirdParty> {

    static ZKSqlProvider sqlProvider;

    @Transient
    @XmlTransient
    @JsonIgnore
    @Override
    public ZKSqlProvider getSqlProvider() {
        return sqlProvider();
    }

    public static ZKSqlProvider sqlProvider() {
        if (sqlProvider == null) {
            sqlProvider = new ZKSqlProvider(new ZKSqlConvertDelegating(), new ZKThirdParty());
        }
        return sqlProvider;
    }
    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    protected static ZKSqlConvert sqlConvert;

    public ZKThirdParty() {
        super();
    }

    public ZKThirdParty(String appId) {
        super(appId);
    }

//  @ZKColumn(name = "c_name", isUpdate = true, isQuery = false, javaType = ZKJson.class, isCaseSensitive = false, queryType = ZKDBQueryType.LIKE)

    // 微信第三方平台账号的 app secret
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_wx_app_secret", isUpdate = false, isQuery = false)
    private String wxAppSecret;

    // 微信平台配置的消息校验 token
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_wx_token", isUpdate = false, isQuery = false)
    private String wxToken;

    // 微信消息加解密 key
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_wx_aes_key", isUpdate = false, isQuery = false)
    private String wxAesKey;

    // 第三方平台令牌
    @ZKColumn(name = "c_wx_ticket", isUpdate = false, isQuery = false)
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    private String wxTicket;

    // 第三方平台令牌更新时间
    @ZKColumn(name = "c_wx_ticket_update_date", javaType = Date.class)
    protected Date wxTicketUpdateDate;

    /**
     * @return wxAppSecret sa
     */
    public String getWxAppSecret() {
        return wxAppSecret;
    }

    /**
     * @param wxAppSecret
     *            the wxAppSecret to set
     */
    public void setWxAppSecret(String wxAppSecret) {
        this.wxAppSecret = wxAppSecret;
    }

    /**
     * @return wxToken sa
     */
    public String getWxToken() {
        return wxToken;
    }

    /**
     * @param wxToken
     *            the wxToken to set
     */
    public void setWxToken(String wxToken) {
        this.wxToken = wxToken;
    }

    /**
     * @return wxAesKey sa
     */
    public String getWxAesKey() {
        return wxAesKey;
    }

    /**
     * @param wxAesKey
     *            the wxAesKey to set
     */
    public void setWxAesKey(String wxAesKey) {
        this.wxAesKey = wxAesKey;
    }

    /**
     * @return wxTicket sa
     */
    public String getWxTicket() {
        return wxTicket;
    }

    /**
     * @param wxTicket
     *            the wxTicket to set
     */
    public void setWxTicket(String wxTicket) {
        this.wxTicket = wxTicket;
    }

    /**
     * @return wxTicketUpdateDate sa
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss, timezone = timezone)
    public Date getWxTicketUpdateDate() {
        return wxTicketUpdateDate;
    }

    /**
     * @param wxTicketUpdateDate
     *            the wxTicketUpdateDate to set
     */
    public void setWxTicketUpdateDate(Date wxTicketUpdateDate) {
        this.wxTicketUpdateDate = wxTicketUpdateDate;
    }

    /**
     * 过期判断时，偏移量，单位秒；正数为提前多少秒；负数为延迟多少秒;
     *
     * @Title: getOffsetExpiresTime
     * @Description: TODO(simple description this method what to do.)
     * @author bs
     * @date 2018年9月13日 下午12:32:56
     * @return
     * @return int
     */
    protected int getOffsetExpiresTime() {
        return 60;
    }

}