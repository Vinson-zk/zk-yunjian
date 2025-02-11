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
* @Title: ZKPlatformCert.java 
* @author Vinson 
* @Package com.zk.wechat.pay.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 21, 2021 8:41:05 AM 
* @version V1.0 
*/
package com.zk.wechat.pay.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.utils.ZKDateUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * 平台证书维护
 * 
 * @ClassName: ZKPlatformCert
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@ZKTable(name = "t_wx_cert_platform", alias = "wxPlatformCert", orderBy = { "c_wx_cert_effective_time desc",
        "c_wx_cert_expiration_time desc" })
public class ZKPlatformCert extends ZKBaseEntity<String, ZKPlatformCert> {

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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKPlatformCert());
        }
        return sqlHelper;
    }

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    public ZKPlatformCert() {
        super();
    }

    public ZKPlatformCert(String pkId) {
        super(pkId);
    }

    public static final String defaultCertPath = "wxPlatform/cer/{0}_{1}.pem";

    // 微信商户号的 mchid
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_wx_mchid", query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String mchid;

    // 平台证书存放路径； 不要以 “/” 开头。
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 128, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_wx_cert_path")
    String certPath;

    // 平台证书的序列号
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_wx_cert_serial_no")
    String certSerialNo;

    // 平台证书 生效时间
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_wx_cert_effective_time")
    Date certEffectiveTime;

    // 平台证书过期时间
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_wx_cert_expiration_time")
    Date certExpirationTime;

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
     * @return certPath sa
     */
    public String getCertPath() {
        return certPath;
    }

    /**
     * @param certPath
     *            the certPath to set
     */
    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }

    /**
     * @return certSerialNo sa
     */
    public String getCertSerialNo() {
        return certSerialNo;
    }

    /**
     * @param certSerialNo
     *            the certSerialNo to set
     */
    public void setCertSerialNo(String certSerialNo) {
        this.certSerialNo = certSerialNo;
    }

    /**
     * @return certEffectiveTime sa
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss, timezone = timezone)
    public Date getCertEffectiveTime() {
        return certEffectiveTime;
    }

    /**
     * @param certEffectiveTime
     *            the certEffectiveTime to set
     */
    public void setCertEffectiveTime(Date certEffectiveTime) {
        this.certEffectiveTime = certEffectiveTime;
    }

    /**
     * @return certExpirationTime sa
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss, timezone = timezone)
    public Date getCertExpirationTime() {
        return certExpirationTime;
    }

    /**
     * @param certExpirationTime
     *            the certExpirationTime to set
     */
    public void setCertExpirationTime(Date certExpirationTime) {
        this.certExpirationTime = certExpirationTime;
    }

    // 判断是否过期; true-已过期；false-未过期；
    public boolean isExpiration() {
        if (this.getCertExpirationTime().getTime() > ZKDateUtils.getToday().getTime()) {
            return false;
        }
        return true;
    }

    // 取离过期还有多少秒
    public long getExpSecond() {
        return -1 * ZKDateUtils.pastSecond(this.getCertExpirationTime());
    }

}
