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
* @Title: ZKBaseHelperTreeEntity.java 
* @author Vinson 
* @Package com.zk.base.helper.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 18, 2022 5:07:17 PM 
* @version V1.0 
*/
package com.zk.base.helper.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.base.commons.ZKTreeSqlHelper;
import com.zk.base.entity.ZKBaseTreeEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlTransient;

/** 
* @ClassName: ZKBaseHelperTreeEntity 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKTable(name = "t_zk_db_test", alias = "t")
public class ZKBaseHelperTreeEntity extends ZKBaseTreeEntity<String, ZKBaseHelperTreeEntity> {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    static ZKTreeSqlHelper sqlHelper;

    /**
     * 
     * @return
     * @see com.zk.base.entity.ZKBaseTreeEntity#getTreeSqlHelper()
     */
    @Override
    public ZKTreeSqlHelper getTreeSqlHelper() {
        return sqlHelper();
    }

    public static ZKTreeSqlHelper sqlHelper() {
        if (sqlHelper == null) {
            sqlHelper = new ZKTreeSqlHelper(new ZKSqlConvertDelegating(), new ZKBaseHelperTreeEntity());
        }
        return sqlHelper;
    }

    @ZKColumn(name = "c_id_2", isPk = true, query = @ZKQuery(true))
    String id2;

    @ZKColumn(name = "c_value", update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String value;

    @ZKColumn(name = "c_remarks", update = @ZKUpdate(true),
            query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
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

    @ZKColumn(name = "c_id", isPk = true, query = @ZKQuery(value = true, isCaseSensitive = true))
    @NotNull(message = "{zk.core.data.validation.notNull}")
    public String getPkId() {
        return super.getPkId();
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getRemarks() {
        return remarks;
    }

    @Override
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

    @Transient
    @JsonIgnore
    @XmlTransient
    @ZKColumn(name = "c_remarks", isResult = false, isInsert = false, javaType = List.class,
            query = @ZKQuery(queryType = ZKDBOptComparison.IN, isCaseSensitive = false))
    public List<String> getListRemarks() {
        return this.getParamByName("ListRemarks");
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////
    // //////////////////////////////////////////////////////////////////////////////////////////////////
    // //////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    @ZKColumn(isInsert = false, isResult = false)
    public Long getVersion() {
        return super.getVersion();
    }

    @Override
    @ZKColumn(isInsert = false, isResult = false)
    public String getCreateUserId() {
        return super.getCreateUserId();
    }

    @ZKColumn(isInsert = false, isResult = false)
    public Date getCreateDate() {
        return super.getCreateDate();
    }

    @Override
    @ZKColumn(isInsert = false, isResult = false)
    public String getUpdateUserId() {
        return super.getUpdateUserId();
    }

    @ZKColumn(isInsert = false, isResult = false)
    public Date getUpdateDate() {
        return super.getUpdateDate();
    }

    @ZKColumn(isInsert = false, isResult = false)
    public Integer getDelFlag() {
        return super.getDelFlag();
    }

    @ZKColumn(isInsert = false, isResult = false)
    public String getSpare1() {
        return super.getSpare1();
    }

    @ZKColumn(isInsert = false, isResult = false)
    public String getSpare2() {
        return super.getSpare2();
    }

    @ZKColumn(isInsert = false, isResult = false)
    public String getSpare3() {
        return super.getSpare3();
    }

    @ZKColumn(isInsert = false, isResult = false)
    public ZKJson getSpareJson() {
        return super.getSpareJson();
    }

    @ZKColumn(isInsert = false, isResult = false)
    public ZKJson getpDesc() {
        return super.getpDesc();
    }

}
