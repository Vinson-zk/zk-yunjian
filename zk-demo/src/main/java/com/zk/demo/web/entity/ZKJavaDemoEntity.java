package com.zk.demo.web.entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlTransient;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKIdUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.entity.ZKDBEntity;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: 测试样例
 * @ClassName ZKJavaDemoEntity
 * @Package com.zk.demo.entity
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-05 00:14:59
 **/
@ZKTable(name = "t_zk_db_test")
public class ZKJavaDemoEntity extends ZKDBEntity<ZKJavaDemoEntity> {

    static ZKDBSqlHelper sqlHelper;

    @SuppressWarnings("unused")
    private final String t_java_demo = "t_java_demo";

    @Transient
    @JsonIgnore
    @Override
    public ZKDBSqlHelper getSqlHelper() {
        return sqlHelper();
    }

    @Transient
    @JsonIgnore
    public static ZKDBSqlHelper sqlHelper() {
        if (sqlHelper == null) {
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKJavaDemoEntity());
        }
        return sqlHelper;
    }

    @Override
    public String getZKDbDelSetSql() {
        return "c_value = \"delete\"";
    }

    private static final long serialVersionUID = 1L;

    public ZKJavaDemoEntity(){}

    public ZKJavaDemoEntity(String pkId){
        this.pkId = pkId;
    }

    @ZKColumn(name = "c_id", isPk=true,
            query = @ZKQuery(value = true, queryType = ZKDBOptComparison.EQ))
    private String pkId;
    @ZKColumn(name = "c_parent_id",
            query = @ZKQuery(value = true, queryType = ZKDBOptComparison.EQ))
    private String parentId;
    @ZKColumn(name = "c_value", update = @ZKUpdate(true),
            query = @ZKQuery(value = true, queryType = ZKDBOptComparison.LIKE))
    private String valueStr;
    @ZKColumn(name = "c_remarks", update = @ZKUpdate(true),
            query = @ZKQuery(value = true, queryType = ZKDBOptComparison.LIKE))
    private String threadStr;
    @ZKColumn(name = "c_int", update = @ZKUpdate(true),
            query = @ZKQuery(value = true, queryType = ZKDBOptComparison.EQ))
    private int valueInt;
    @ZKColumn(name = "c_date", update = @ZKUpdate(true))
    private Date valueDate;

    public String getPkId() {
        return pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getValueStr() {
        return valueStr;
    }

    public void setValueStr(String valueStr) {
        this.valueStr = valueStr;
    }

    public String getThreadStr() {
        return threadStr;
    }

    public void setThreadStr(String threadStr) {
        this.threadStr = threadStr;
    }

    public int getValueInt() {
        return valueInt;
    }

    public void setValueInt(int valueInt) {
        this.valueInt = valueInt;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    @Transient
    @JsonProperty("isNewRecord")
    protected boolean isNewRecord;

    @Transient
    @XmlTransient
    @JsonIgnore
    public boolean isNewRecord() {
        return isNewRecord || pkId == null || "".equals(pkId.toString());
    }

    /**
     * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
     * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     *
     * @param isNewRecord
     */
    public void setNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public void preInsert() {
        if (this.getPkId() == null || "".equals(pkId.toString())) {
            this.setPkId(ZKIdUtils.genLongStringId());
        }
        this.valueDate = ZKDateUtils.getToday();
    }

    public void preUpdate() {
        this.valueDate = ZKDateUtils.getToday();
    }
}
