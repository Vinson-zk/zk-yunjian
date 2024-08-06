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
* @Title: ZKPayGetBusinessType.java 
* @author Vinson 
* @Package com.zk.wechat.pay.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 20, 2021 8:45:56 AM 
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
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.ZKCoreConstants.ValidationRegexp;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKDateUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * 微信支付-收款时，支持收款的业务的类型维护；
 * 
 * @ClassName: ZKPayGetBusinessType
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@ZKTable(name = "t_wx_pay_get_business_type", alias = "wxPayGetNotify")
public class ZKPayGetBusinessType extends ZKBaseEntity<String, ZKPayGetBusinessType> {

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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKPayGetBusinessType());
        }
        return sqlHelper;
    }

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

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

    public ZKPayGetBusinessType() {
        super();
    }

    public ZKPayGetBusinessType(String pkId) {
        super(pkId);
    }

    // 业务类型代码；全表唯一，也是请求是的路径参数；
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_code")
    String code;

    // 业务类型的名称
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_name", update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    ZKJson name;

    // 状态；0-启用；1-禁用； disabled，enabled
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 9, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_status")
    Integer status;

    // 开启使用日期；null 时不较验；
//    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss, timezone = timezone)
    @ZKColumn(name = "c_start_date")
    Date startDate;

    // 结束使用日期；null 时不较验；
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss, timezone = timezone)
    @ZKColumn(name = "c_end_date")
    Date endDate;

    /**
     * @return code sa
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return name sa
     */
    public ZKJson getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(ZKJson name) {
        this.name = name;
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
     * @return startDate sa
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate
     *            the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return endDate sa
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    // recycle rubbish

}
