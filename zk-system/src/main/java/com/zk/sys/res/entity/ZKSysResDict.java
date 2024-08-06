/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.res.entity;

import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.base.commons.ZKTreeSqlHelper;
import com.zk.base.entity.ZKBaseTreeEntity;
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

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * 字典表
 * 
 * @author
 * @version
 */
@ZKTable(name = "t_sys_res_dict", alias = "sysResDict", orderBy = " c_type_code ASC, c_create_date ASC ")
public class ZKSysResDict extends ZKBaseTreeEntity<String, ZKSysResDict> {

    static ZKTreeSqlHelper sqlHelper;

    @Transient
    @XmlTransient
    @JsonIgnore
    @Override
    public ZKTreeSqlHelper getTreeSqlHelper() {
        return sqlHelper();
    }

    @Transient
    @XmlTransient
    @JsonIgnore
    public static ZKTreeSqlHelper sqlHelper() {
        if (sqlHelper == null) {
            sqlHelper = new ZKTreeSqlHelper(new ZKSqlConvertDelegating(), new ZKSysResDict());
        }
        return sqlHelper;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 类型代码
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_type_code", isInsert = true, javaType = String.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String typeCode;
    /**
     * 字典代码
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_dict_code", isInsert = true, javaType = String.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String dictCode;
    /**
     * 字典名称
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @NotEmpty(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_dict_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    ZKJson dictName;

    /**
     * 字典说明
     */
    @ZKColumn(name = "c_dict_desc", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true))
    ZKJson dictDesc;

    /**
     * 字典类型；查询明细时，会级联查询出来，service 中 get 时，不会级联查询
     */
    @Transient
    ZKSysResDictType dictType;

    // 查询辅助字段
    @Transient
    @JsonIgnore
    @XmlTransient
    String searchValue;

    public ZKSysResDict() {
        super();
    }

    public ZKSysResDict(String pkId) {
        super(pkId);
    }

    /**
     * 类型代码
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * 类型代码
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * 字典代码
     */
    public String getDictCode() {
        return dictCode;
    }

    /**
     * 字典代码
     */
    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    /**
     * 字典名称
     */
    public ZKJson getDictName() {
        return dictName;
    }

    /**
     * 字典名称
     */
    public void setDictName(ZKJson dictName) {
        this.dictName = dictName;
    }

    /**
     * 字典说明
     */
    public ZKJson getDictDesc() {
        return dictDesc;
    }

    /**
     * 字典说明
     */
    public void setDictDesc(ZKJson dictDesc) {
        this.dictDesc = dictDesc;
    }

    /**
     * 根据主键类型，重写主键生成；
     */
    @Override
    protected String genId() {
        return ZKIdUtils.genLongStringId();
    }

    /**
     * @return dictType sa
     */
    public ZKSysResDictType getDictType() {
        return dictType;
    }

    /**
     * @param dictType
     *            the dictType to set
     */
    public void setDictType(ZKSysResDictType dictType) {
        this.dictType = dictType;
    }

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
//        ZKDBQueryWhere where = sqlConvert.resolveQueryCondition(mapInfo);
        ZKDBQueryWhere where = super.getZKDbWhereTree(sqlConvert, mapInfo);
        // 制作一个根据名称和代码同时查询的 查询条件，用过度 java 属性 searchValue 为传参数值
        ZKDBQueryWhere sWhere = ZKDBQueryWhere.asOr("(", ")",
            ZKDBQueryCol.as(ZKDBOptComparison.LIKE, "c_dict_name", "searchValue", String.class, null, false),
            ZKDBQueryCol.as(ZKDBOptComparison.LIKE, "c_dict_code", "searchValue", String.class, null, false));

        where.put(ZKDBQueryScript.asIf(sWhere, 0, "searchValue", String.class));
        return where;
    }

}
