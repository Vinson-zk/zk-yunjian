/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.settings.entity;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.ZKCoreConstants.ValidationRegexp;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKIdUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBMapInfo;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKDBQueryCol;
import com.zk.db.commons.ZKDBQueryWhere;
import com.zk.db.commons.ZKSqlConvert;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBQueryScript;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * 配置项分组，集合
 * 
 * @author
 * @version
 */
@ZKTable(name = "t_sys_set_collection", alias = "sysSetCollection", orderBy = " c_create_date ASC ")
public class ZKSysSetCollection extends ZKBaseEntity<String, ZKSysSetCollection> {

    static ZKDBSqlHelper sqlHelper;

    @Transient
    @XmlTransient
    @JsonIgnore
    @Override
    public ZKDBSqlHelper getSqlHelper() {
        return sqlHelper();
    }

    @Transient
    @XmlTransient
    @JsonIgnore
    public static ZKDBSqlHelper sqlHelper() {
        if (sqlHelper == null) {
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKSysSetCollection());
        }
        return sqlHelper;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 配置项组别名称
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @NotEmpty(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    ZKJson name;
    /**
     * 配置项组别代码；全表唯一；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String code;
    /**
     * 配置项组别说明
     */
    @ZKColumn(name = "c_set_desc", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    ZKJson setDesc;
    /**
     * 集团代码
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_group_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String groupCode;
    /**
     * 公司代码
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_company_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String companyCode;

    public ZKSysSetCollection() {
        super();
    }

    public ZKSysSetCollection(String pkId) {
        super(pkId);
    }

    /**
     * 配置项组别名称
     */
    public ZKJson getName() {
        return name;
    }

    /**
     * 配置项组别名称
     */
    public void setName(ZKJson name) {
        this.name = name;
    }

    /**
     * 配置项组别代码；全表唯一；
     */
    public String getCode() {
        return code;
    }

    /**
     * 配置项组别代码；全表唯一；
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 配置项组别说明
     */
    public ZKJson getSetDesc() {
        return setDesc;
    }

    /**
     * 配置项组别说明
     */
    public void setSetDesc(ZKJson setDesc) {
        this.setDesc = setDesc;
    }

    /**
     * 集团代码
     */
    public String getGroupCode() {
        return groupCode;
    }

    /**
     * 集团代码
     */
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    /**
     * 公司代码
     */
    public String getCompamyCode() {
        return companyCode;
    }

    /**
     * 公司代码
     */
    public void setCompamyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    /**
     * 根据主键类型，重写主键生成；
     */
    @Override
	@JsonIgnore
	public String genId() {
        return ZKIdUtils.genLongStringId();
    }

    // 查询辅助字段
    @Transient
    @JsonIgnore
    @XmlTransient
    String searchValue;

    /**
     * @return searchValue sa
     */
    public String getSearchValue() {
        return searchValue;
    }

    /**
     * @param searchValue
     *            the searchValue to set
     */
    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    // 取 where 条件；实体定义可以定制；在 生成的 sql；注意：末尾加空格
    @Override
    @Transient
    @JsonIgnore
    @XmlTransient
    public ZKDBQueryWhere getZKDbWhere(ZKSqlConvert sqlConvert, ZKDBMapInfo mapInfo) {
        ZKDBQueryWhere where = sqlConvert.resolveQueryCondition(mapInfo);
        // 制作一个根据名称和代码同时查询的 查询条件，用过度 java 属性 searchValue 为传参数值
        ZKDBQueryWhere sWhere = ZKDBQueryWhere.asOr("(", ")",
            ZKDBQueryCol.as(ZKDBOptComparison.LIKE, "c_name", "searchValue", String.class, null, false),
            ZKDBQueryCol.as(ZKDBOptComparison.LIKE, "c_code", "searchValue", String.class, null, false));

        where.put(ZKDBQueryScript.asIf(sWhere, 0, "searchValue", String.class));
        return where;
    }

}