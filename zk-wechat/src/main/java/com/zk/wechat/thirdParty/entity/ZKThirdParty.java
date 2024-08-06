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

import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.ZKCoreConstants.ValidationRegexp;
import com.zk.core.utils.ZKDateUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvert;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/** 
* @ClassName: ZKThirdParty 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKTable(name = "t_wx_third_party", alias = "wxThirdParty")
public class ZKThirdParty extends ZKBaseEntity<String, ZKThirdParty> {

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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKThirdParty());
        }
        return sqlHelper;
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

    // 微信第三方平台账号的 app secret
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_wx_app_secret", update = @ZKUpdate(true))
    private String wxAppSecret;

    // 微信平台配置的消息校验 token
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_wx_token", update = @ZKUpdate(true))
    private String wxToken;

    // 微信消息加解密 key
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_wx_aes_key", update = @ZKUpdate(true))
    private String wxAesKey;

    // 第三方平台令牌
    @ZKColumn(name = "c_wx_ticket")
//    @Length(max = 256, message = "{zk.core.data.validation.length.max}")
    private String wxTicket;

    // 第三方平台令牌更新时间
    @ZKColumn(name = "c_wx_ticket_update_date", javaType = Date.class)
    protected Date wxTicketUpdateDate;

    /**
     * 集团代码
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_group_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String groupCode;

    /**
     * 公司ID
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_company_id", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String companyId;

    /**
     * 公司代码
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_company_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String companyCode;

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

    /**
     * 集团代码
     * 
     * @return groupCode sa
     */
    public String getGroupCode() {
        return groupCode;
    }

    /**
     * 集团代码
     * 
     * @param groupCode
     *            the groupCode to set
     */
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    /**
     * 公司ID
     * 
     * @return companyId sa
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * 公司ID
     * 
     * @param companyId
     *            the companyId to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * 公司代码
     * 
     * @return companyCode sa
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * 公司代码
     * 
     * @param companyCode
     *            the companyCode to set
     */
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

}