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
* @Title: ZKSysOrgUserEditLog.java 
* @author Vinson 
* @Package com.zk.sys.org.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 21, 2024 7:03:46 PM 
* @version V1.0 
*/
package com.zk.sys.org.entity;

import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotNull;

/** 
* @ClassName: ZKSysOrgUserEditLog 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_sys_org_user_edit_log", alias = "sysOrgUserEditLog", orderBy = " c_create_date DESC ")
public class ZKSysOrgUserEditLog extends ZKBaseEntity<String, ZKSysOrgUserEditLog> {

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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKSysOrgUserEditLog());
        }
        return sqlHelper;
    }

    private static final long serialVersionUID = 1L;

    public static interface ZKUserEditType {
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

    }

    public static interface ZKUserEditFlag {
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

    }

    public static ZKSysOrgUserEditLog as(String userId, int editType, int editFlag) {
        ZKSysOrgUserEditLog el = new ZKSysOrgUserEditLog();
        el.setUserId(userId);
        el.setEditType(editType);
        el.setEditFlag(editFlag);
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
     * 修改类型；详见：ZKUserEditType
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 9, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_edit_type", isInsert = true, javaType = Integer.class)
    Integer editType;

    /**
     * 修改标识；修改类型下的2级标识；详见：ZKUserEditFlag
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 9, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_edit_flag", isInsert = true, javaType = Integer.class)
    Integer editFlag;

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
     * @return editType sa
     */
    public Integer getEditType() {
        return editType;
    }

    /**
     * @param editType
     *            the editType to set
     */
    public void setEditType(Integer editType) {
        this.editType = editType;
    }

    /**
     * @return editFlag sa
     */
    public Integer getEditFlag() {
        return editFlag;
    }

    /**
     * @param editFlag
     *            the editFlag to set
     */
    public void setEditFlag(Integer editFlag) {
        this.editFlag = editFlag;
    }

}
