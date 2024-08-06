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
* @Title: ZKPayMerchant.java 
* @author Vinson 
* @Package com.zk.wechat.pay.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 19, 2021 5:12:17 PM 
* @version V1.0 
*/
package com.zk.wechat.pay.entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.utils.ZKDateUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKTable;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotNull;

/**
 * 微信商户号
 * 
 * @ClassName: ZKPayMerchant
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@ZKTable(name = "t_wx_merchant", alias = "wxMerchant")
public class ZKPayMerchant extends ZKBaseEntity<String, ZKPayMerchant> {

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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKPayMerchant());
        }
        return sqlHelper;
    }
    
    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    public ZKPayMerchant() {
        super();
    }

    /**
     * mchid 商户号ID 做主键 KEY
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param pkId
     */
    public ZKPayMerchant(String pkId) {
        super(pkId);
    }

    /**
     * 状态；0-启用；1-禁用；
     * 
     * @ClassName: Status
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public static interface Status {
        /**
         * 0-启用；
         */
        public static final int enabled = 0;

        /**
         * 1-禁用；
         */
        public static final int disabled = 1;
    }

    // 商户号接入状态；0-启用；1-禁用；enabled, disabled；
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 9, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_status")
    Integer status;

    // 商户号 APIv3密钥
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_wx_apiv3_aes_key")
    String apiv3AesKey;
    
    // API 密钥
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_wx_api_key")
    String apiKey;

    // 格式为 PKCS12 的 商户API证书存放路径；不要以 “/” 开头；
//    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 256, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_wx_api_cert_path_pkcs12")
    String apiCertPathPkcs12;

    // 商户API证书导出的 pem 格式的证书文件 存放路径；不要以 “/” 开头；
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 256, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_wx_api_cert_path_pem")
    String apiCertPathPem;

    // 商户API证书导出的 pem 格式的私钥文件存放路径；不要以 “/” 开头；
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 256, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_wx_api_cert_path_key_pem")
    String apiCertPathKeyPem;

    // 商户API证书的序列号
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_wx_api_cert_serial_no")
    String apiCertSerialNo;

    // 商户API证书更新时间
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_wx_api_cert_update_date")
    Date apiCertUpdateDate;

    // 商户API证书 生效时间
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_wx_api_cert_effective_time")
    Date apiCertEffectiveTime;

    // 商户API证书过期时间
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_wx_api_cert_expiration_time")
    Date apiCertExpirationTime;

    /**
     * @return apiv3AesKey sa
     */
    public String getApiv3AesKey() {
        return apiv3AesKey;
    }

    /**
     * @param apiv3AesKey
     *            the apiv3AesKey to set
     */
    public void setApiv3AesKey(String apiv3AesKey) {
        this.apiv3AesKey = apiv3AesKey;
    }

    /**
     * @return apiKey sa
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * @param apiKey
     *            the apiKey to set
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * @return apiCertPathPkcs12 sa
     */
    public String getApiCertPathPkcs12() {
        return apiCertPathPkcs12;
    }

    /**
     * @param apiCertPathPkcs12
     *            the apiCertPathPkcs12 to set
     */
    public void setApiCertPathPkcs12(String apiCertPathPkcs12) {
        this.apiCertPathPkcs12 = apiCertPathPkcs12;
    }

    /**
     * @return apiCertPathPem sa
     */
    public String getApiCertPathPem() {
        return apiCertPathPem;
    }


    /**
     * @param apiCertPathPem
     *            the apiCertPathPem to set
     */
    public void setApiCertPathPem(String apiCertPathPem) {
        this.apiCertPathPem = apiCertPathPem;
    }

    /**
     * @return apiCertPathKeyPem sa
     */
    public String getApiCertPathKeyPem() {
        return apiCertPathKeyPem;
    }


    /**
     * @param apiCertPathKeyPem
     *            the apiCertPathKeyPem to set
     */
    public void setApiCertPathKeyPem(String apiCertPathKeyPem) {
        this.apiCertPathKeyPem = apiCertPathKeyPem;
    }

    /**
     * @return apiCertSerialNo sa
     */
    public String getApiCertSerialNo() {
        return apiCertSerialNo;
    }


    /**
     * @param apiCertSerialNo
     *            the apiCertSerialNo to set
     */
    public void setApiCertSerialNo(String apiCertSerialNo) {
        this.apiCertSerialNo = apiCertSerialNo;
    }

    /**
     * @return apiCertUpdateDate sa
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss, timezone = timezone)
    public Date getApiCertUpdateDate() {
        return apiCertUpdateDate;
    }


    /**
     * @param apiCertUpdateDate
     *            the apiCertUpdateDate to set
     */
    public void setApiCertUpdateDate(Date apiCertUpdateDate) {
        this.apiCertUpdateDate = apiCertUpdateDate;
    }

    /**
     * @return status sa
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return apiCertEffectiveTime sa
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss, timezone = timezone)
    public Date getApiCertEffectiveTime() {
        return apiCertEffectiveTime;
    }

    /**
     * @param apiCertEffectiveTime
     *            the apiCertEffectiveTime to set
     */
    public void setApiCertEffectiveTime(Date apiCertEffectiveTime) {
        this.apiCertEffectiveTime = apiCertEffectiveTime;
    }

    /**
     * @return apiCertExpirationTime sa
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss, timezone = timezone)
    public Date getApiCertExpirationTime() {
        return apiCertExpirationTime;
    }

    /**
     * @param apiCertExpirationTime
     *            the apiCertExpirationTime to set
     */
    public void setApiCertExpirationTime(Date apiCertExpirationTime) {
        this.apiCertExpirationTime = apiCertExpirationTime;
    }

}
