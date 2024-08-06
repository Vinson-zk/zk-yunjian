/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKSerCenCertificate.java 
 * @author Vinson 
 * @Package com.zk.server.central.entity 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:21:03 PM 
 * @version V1.0   
*/
package com.zk.server.central.entity;

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
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotNull;

/** 
* @ClassName: ZKSerCenCertificate 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@ZKTable(name = "t_sc_server_certificate", orderBy = " c_pk_id ASC")
public class ZKSerCenCertificate extends ZKBaseEntity<String, ZKSerCenCertificate> {

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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKSerCenCertificate());
        }
        return sqlHelper;
    }

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;


    public static interface StatusType {
        /**
         * 正常
         */
        public static final int Enable = 0;

        /**
         * 禁用
         */
        public static final int Disabled = 1;

    }

    // 有效期结束开始日期
    @ZKColumn(name = "c_valid_start_date", update = @ZKUpdate(true))
    private Date validStartDate;

    // 有效期结束日期
    @ZKColumn(name = "c_valid_end_date", update = @ZKUpdate(true))
    private Date validEndDate;

    // 证书状态；0-正常；1-禁用
    @NotNull
    @Range(min = 0, max = 1, message = "{data.validation.rang.int:0:1}")
    @ZKColumn(name = "c_status")
    private Integer status;

    // 服务名称
    @NotNull(message = "{data.validation.notNull}")
    @Length(max = 100, message = "{data.validation.length:0:100}")
    @ZKColumn(name = "c_server_name", update = @ZKUpdate(false),
            query = @ZKQuery(value = true, queryType = ZKDBOptComparison.LIKE))
    private String serverName;

    // 公钥
    @ZKColumn(name = "c_public_key")
    private String publicKey;

    // 私钥
    @ZKColumn(name = "c_private_key")
    private String privateKey;

    /** 查询 ***************** **/
    @ZKColumn(name = "c_valid_start_date", isResult = false, isInsert = false, javaType = Date.class,
            query = @ZKQuery(value = true, queryType = ZKDBOptComparison.GTE))
    public Date getValidStartDateBegin() {
        return this.getParamByName("validStartDateBegin");
    }

    @ZKColumn(name = "c_valid_start_date", isResult = false, isInsert = false, javaType = Date.class,
            query = @ZKQuery(value = true, queryType = ZKDBOptComparison.LTE))
    public Date getValidStartDateEnd() {
        return this.getParamByName("validStartDateEnd");
    }

    @ZKColumn(name = "c_valid_end_date", isResult = false, isInsert = false, javaType = Date.class,
            query = @ZKQuery(value = true, queryType = ZKDBOptComparison.GTE))
    public Date getValidEndDateBegin() {
        return this.getParamByName("validEndDateBegin");
    }

    @ZKColumn(name = "c_valid_end_date", isResult = false, isInsert = false, javaType = Date.class,
            query = @ZKQuery(value = true, queryType = ZKDBOptComparison.LTE))
    public Date getValidEndDateEnd() {
        return this.getParamByName("validEndDateEnd");
    }

    public ZKSerCenCertificate() {
        super();
    }

    public ZKSerCenCertificate(String pkId) {
        super(pkId);
    }


    /**
     * @return validStartDate
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd, timezone = timezone)
    public Date getValidStartDate() {
        return validStartDate;
    }

    /**
     * @param validStartDate
     *            the validStartDate to set
     */
    public void setValidStartDate(Date validStartDate) {
        this.validStartDate = validStartDate;
    }

    /**
     * @return validEndDate
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd, timezone = timezone)
    public Date getValidEndDate() {
        return validEndDate;
    }

    /**
     * @param validEndDate
     *            the validEndDate to set
     */
    public void setValidEndDate(Date validEndDate) {
        this.validEndDate = validEndDate;
    }

    /**
     * @return status
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
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
     * @return serverName
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public String getServerName() {
        return serverName;
    }

    /**
     * @param serverName
     *            the serverName to set
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * @return publicKey
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * @param publicKey
     *            the publicKey to set
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * @return privateKey
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * @param privateKey
     *            the privateKey to set
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public void preInsert() {
        super.preInsert();
        if (this.status == null) {
            this.status = StatusType.Enable;
        }
    }

    @Override
    public void preUpdate() {
        super.preUpdate();
        if (this.status == null) {
            this.status = StatusType.Enable;
        }
    }

}
