/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.res.entity;

import javax.xml.bind.annotation.XmlTransient;

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
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * 字典类型表
 * 
 * @author
 * @version
 */
@ZKTable(name = "t_sys_res_dict_type", alias = "sysResDictType", orderBy = " c_create_date ASC ")
public class ZKSysResDictType extends ZKBaseEntity<String, ZKSysResDictType> {

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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKSysResDictType());
        }
        return sqlHelper;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 字典类型代码；全表唯一
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_type_code", isInsert = true, javaType = String.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String typeCode;

    /**
     * 字典类型名称
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @NotEmpty(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_type_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    ZKJson typeName;

    /**
     * 字典类型说明
     */
    @ZKColumn(name = "c_type_desc", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true))
    ZKJson typeDesc;

    public ZKSysResDictType() {
        super();
    }

    public ZKSysResDictType(String pkId) {
        super(pkId);
    }

    /**
     * 字典类型代码；全表唯一
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * 字典类型代码；全表唯一
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * 字典类型名称
     */
    public ZKJson getTypeName() {
        return typeName;
    }

    /**
     * 字典类型名称
     */
    public void setTypeName(ZKJson typeName) {
        this.typeName = typeName;
    }

    /**
     * 字典类型说明
     */
    public ZKJson getTypeDesc() {
        return typeDesc;
    }

    /**
     * 字典类型说明
     */
    public void setTypeDesc(ZKJson typeDesc) {
        this.typeDesc = typeDesc;
    }

    /**
     * 根据主键类型，重写主键生成；
     */
    @Override
    protected String genId() {
        return ZKIdUtils.genLongStringId();
    }

}