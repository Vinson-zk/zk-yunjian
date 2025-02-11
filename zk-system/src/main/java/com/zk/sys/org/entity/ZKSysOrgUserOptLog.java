/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSysOrgUserOptLog.java 
* @author Vinson 
* @Package com.zk.sys.org.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 21, 2024 7:03:46 PM 
* @version V1.0 
*/
package com.zk.sys.org.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.commons.ZKDBMapInfo;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKDBQueryCol;
import com.zk.db.commons.ZKDBQueryWhere;
import com.zk.db.commons.ZKSqlConvert;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBQueryScript;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlTransient;

/** 
* @ClassName: ZKSysOrgUserOptLog 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_sys_org_user_opt_log", alias = "sysOrgUserOptLog", orderBy = " c_create_date DESC ")
public class ZKSysOrgUserOptLog extends ZKBaseEntity<String, ZKSysOrgUserOptLog> {

    static ZKDBSqlHelper sqlHelper;

    @Override
    public ZKDBSqlHelper getSqlHelper() {
        return sqlHelper();
    }

    @Transient
    @XmlTransient
    @JsonIgnore
    public static ZKDBSqlHelper sqlHelper() {
        if (sqlHelper == null) {
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKSysOrgUserOptLog());
        }
        return sqlHelper;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 用户操作方式
     */
    public static interface ZKUserOptType {
        // 0-基础信息修改；
        public static final int base = 0;

        // 1-修改账号；
        public static final int account = 1;

        // 2-修改邮箱；
        public static final int mail = 2;

        // 3-修改手机；
        public static final int phone = 3;

        // 4-修改工号；
        public static final int jobnum = 4;

        // 5-修改密码；
        public static final int pwd = 5;

        // 6-修改状态；
        public static final int status = 6;

        // 7-登录
        public static final int login = 7;

    }

    /**
     * 用户操作方式下对应的操作标识，即怎么操作的
     */
    public static interface ZKUserOptTypeFlag {
        // 0-基础信息修改；
        public static interface Base {
            // 0-公司修改；
            public static final int company = 0;
            // 1-个人修改；
            public static final int self = 1;
            // 2-账号注册；
            public static final int account = 2;
            // 3-邮箱注册；
            public static final int mail = 3;
            // 4-手机注册；
            public static final int phoneNum = 4;
            // 5-第三方系统注册；
            public static final int thirdSys = 5;
        }

        // 1-修改账号；
        public static interface Account {
            // 0-公司修改；
            public static final int company = 0;
            // 1-个人修改；
            public static final int self = 1;
            // 2-系统生成
            public static final int system = 2;
        }

        // 2-修改邮箱；
        public static interface Mail {
            // 0-公司修改；
            public static final int company = 0;
            // 1-个人修改；
            public static final int self = 1;
        }

        // 3-修改手机；
        public static interface Phone {
            // 0-公司修改；
            public static final int company = 0;
            // 1-个人修改；
            public static final int self = 1;
        }

        // 4-修改工号；
        public static interface Jobnum {
            // 0-公司修改；
            public static final int company = 0;
        }

        // 5-修改密码；
        public static interface Pwd {
            // 0-公司修改；
            public static final int company = 0;
            // 1-个人修改；
            public static final int self = 1;
            // 2-系统设置；
            public static final int system = 2;
            
        }

        // 6-修改状态；
        public static interface Status {
            // 0-公司修改；
            public static final int company = 0;
            // 1-个人修改；
            public static final int self = 1;

        }

        // 7-用户登录；
        public static interface Login {
            // 0-正常登录；
            public static final int self = 0;
        }

    }

    public static ZKSysOrgUserOptLog as(String userId, String optIp, String optIpDesc, int optType, int optTypeFlag,
            ZKJson optContent) {
        ZKSysOrgUserOptLog el = new ZKSysOrgUserOptLog();
        el.setUserId(userId);
        el.setOptIp(optIp);
        el.setOptIpDesc(optIpDesc);
        el.setOptType(optType);
        el.setOptTypeFlag(optTypeFlag);
        el.setOptContent(optContent);
        return el;
    }

    /**
     * 用户ID
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_user_id", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String userId;

    /**
     * 修改类型；详见：ZKUserOptType
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 9, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_opt_type", isInsert = true, javaType = Integer.class)
    Integer optType;

    /**
     * 修改标识；修改类型下的2级标识；详见：ZKUserOptTypeFlag
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 9, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_opt_type_flag", isInsert = true, javaType = Integer.class)
    Integer optTypeFlag;

    // 操作 IP
    @Length(min = 1, max = 32, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_opt_ip", isInsert = true, javaType = String.class)
    String optIp;

    // 操作 IP 说明
    @Length(min = 1, max = 256, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_opt_ip_dsec", isInsert = true, javaType = String.class)
    String optIpDesc;

    // 操作内容
//    @NotEmpty(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_opt_content", isInsert = true, javaType = ZKJson.class)
    ZKJson optContent;

    // 查询辅助字段
    @Transient
    @JsonIgnore
    @XmlTransient
    Date startDate;

    @Transient
    @JsonIgnore
    @XmlTransient
    Date endDate;

    /**
     * @return userId sa
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return optIp sa
     */
    public String getOptIp() {
        return optIp;
    }


    /**
     * @param optIp
     *            the optIp to set
     */
    public void setOptIp(String optIp) {
        this.optIp = optIp;
    }

    /**
     * @return optIpDesc sa
     */
    public String getOptIpDesc() {
        return optIpDesc;
    }

    /**
     * @param optIpDesc
     *            the optIpDesc to set
     */
    public void setOptIpDesc(String optIpDesc) {
        this.optIpDesc = optIpDesc;
    }

    /**
     * @return optType sa
     */
    public Integer getOptType() {
        return optType;
    }

    /**
     * @param optType
     *            the optType to set
     */
    public void setOptType(Integer optType) {
        this.optType = optType;
    }

    /**
     * @return optTypeFlag sa
     */
    public Integer getOptTypeFlag() {
        return optTypeFlag;
    }

    /**
     * @param optTypeFlag
     *            the optTypeFlag to set
     */
    public void setOptTypeFlag(Integer optTypeFlag) {
        this.optTypeFlag = optTypeFlag;
    }

    /**
     * @return optContent sa
     */
    public ZKJson getOptContent() {
        return optContent;
    }

    /**
     * @param optContent
     *            the optContent to set
     */
    public void setOptContent(ZKJson optContent) {
        this.optContent = optContent;
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

    // 取 where 条件；实体定义可以定制；在 生成的 sql；注意：末尾加空格
    @Override
    @Transient
    @JsonIgnore
    @XmlTransient
    public ZKDBQueryWhere getZKDbWhere(ZKSqlConvert sqlConvert, ZKDBMapInfo mapInfo) {
        ZKDBQueryWhere where = super.getZKDbWhere(sqlConvert, mapInfo);
        where.put(ZKDBQueryScript.asIf(
                ZKDBQueryCol.as(ZKDBOptComparison.GTE, "c_create_date", "startDate", Date.class, null, false), 2,
                "startDate", Date.class));
        where.put(ZKDBQueryScript.asIf(
                ZKDBQueryCol.as(ZKDBOptComparison.LTE, "c_create_date", "endDate", Date.class, null, false), 2,
                "endDate", Date.class));
        return where;
    }

}


