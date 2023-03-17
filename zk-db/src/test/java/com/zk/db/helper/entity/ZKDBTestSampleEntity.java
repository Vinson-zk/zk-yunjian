package com.zk.db.helper.entity;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKDateUtils;
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
 * @Description: 测试 entity
 * @ClassName ZKDBTestSampleEntity
 * @Package com.zk.db.helper.entity
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-27 16:40:31
 **/
@ZKTable(name = "t_zk_db_test")
public class ZKDBTestSampleEntity extends ZKDBEntity<ZKDBTestSampleEntity> {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    static ZKDBSqlHelper sqlHelper;

    @Override
    public ZKDBSqlHelper getSqlHelper() {
        return sqlHelper();
    }

    public static ZKDBSqlHelper sqlHelper(){
        if(sqlHelper == null){
            sqlHelper = ZKDBSqlHelper.as(new ZKSqlConvertDelegating(), new ZKDBTestSampleEntity());
        }
        return sqlHelper;
    }

    public ZKDBTestSampleEntity(){}

    public ZKDBTestSampleEntity(String id){
        this.id = id;
    }

    String id;

    @ZKColumn(name = "c_id_2", isPk = true, query = @ZKQuery(true))
    String id2;

    String value;

    @ZKColumn(name = "c_remarks", update = @ZKUpdate(true),
            query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String remarks;

    @ZKColumn(name = "c_json", javaType = ZKJson.class, update = @ZKUpdate(true),
            query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    ZKJson json;

    @ZKColumn(name = "c_date", javaType = Date.class, update = @ZKUpdate(true))
    Date mDate;

    @ZKColumn(name = "c_boolean", javaType = Boolean.class, update = @ZKUpdate(true),
            query = @ZKQuery(queryType = ZKDBOptComparison.EQ, testRule = 2))
    Boolean mBoolean;

    @ZKColumn(name = "c_int", javaType = Long.class,
            update = @ZKUpdate(isForce = true, isCondition = true, setSql = "c_int = c_int + 1"),
            query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Long mInt;

    @ZKColumn(name = "c_parent_id", query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String parentId;

    /********* 查询字段 ***************/

    @Transient
    @JsonIgnore
    @XmlTransient
    @ZKColumn(name = "c_int", isResult = false, isInsert = false, javaType = Object[].class,
            query = @ZKQuery(queryType = ZKDBOptComparison.IN))
    private Long[] mInts;

    @Transient
    @JsonIgnore
    @XmlTransient
    @ZKColumn(name = "c_date", formats = { "%Y-%m-%d", ZKDateUtils.DF_yyyy_MM_dd }, isResult = false,
            isInsert = false, javaType = Date.class, update = @ZKUpdate(value = false),
            query = @ZKQuery(queryType = ZKDBOptComparison.GTE))
    public Date getStartDate() {
        return this.getParamByName("startDate");
    }

    @Transient
    @JsonIgnore
    @XmlTransient
    @ZKColumn(name = "c_date", formats = { "%Y-%m-%d %H:%i", ZKDateUtils.DF_yyyy_MM_dd_HH_mm },
            isResult = false, isInsert = false, javaType = Date.class,
            query = @ZKQuery(queryType = ZKDBOptComparison.LTE))
    public Date getEndDate() {
        return this.getParamByName("endDate");
    }

    @Transient
    @JsonIgnore
    @XmlTransient
    @ZKColumn(name = "c_int", isResult = false, isInsert = false, javaType = List.class,
            query = @ZKQuery(queryType = ZKDBOptComparison.IN, isCaseSensitive = false))
    public List<String> getmIntStrs() {
        return this.getParamByName("mIntStrs");
    }

    /******************************** */

    @ZKColumn(name = "c_id", isPk = true, query = @ZKQuery(true))
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    @ZKColumn(name = "c_value", update = @ZKUpdate(true), query = @ZKQuery(true))
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ZKJson getJson() {
        return json;
    }

    public void setJson(ZKJson json) {
        this.json = json;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public Boolean getmBoolean() {
        return mBoolean;
    }

    public void setmBoolean(Boolean mBoolean) {
        this.mBoolean = mBoolean;
    }

    public Long getmInt() {
        return mInt;
    }

    public void setmInt(Long mInt) {
        this.mInt = mInt;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Long[] getmInts() {
        return mInts;
    }

    public void setmInts(Long[] mInts) {
        this.mInts = mInts;
    }

    @Override
    public String getZKDbDelSetSql() {
        return "c_int = #{mInt}, c_date = #{mDate}";
    }

}
