/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.res.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import org.hibernate.validator.constraints.Length;

import com.zk.db.mybatis.commons.ZKDBSqlHelper;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKIdUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKTable;
import com.zk.db.commons.ZKSqlConvertDelegating;
import org.springframework.data.annotation.Transient;

/**
 * 应用系统
 * 
 * @author
 * @version
 */
@ZKTable(name = "t_sys_res_application_system", alias = "sysResApplicationSystem", orderBy = " c_create_date ASC ")
public class ZKSysResApplicationSystem extends ZKBaseEntity<String, ZKSysResApplicationSystem> {

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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKSysResApplicationSystem());
        }
        return sqlHelper;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 应用系统名称
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @NotEmpty(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    ZKJson name;
    /**
     * 应用系统代码，唯一
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_code", isInsert = true, javaType = String.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String code;
    /**
     * 应用系统简称
     */
    @ZKColumn(name = "c_short_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true))
    ZKJson shortName;

    public ZKSysResApplicationSystem() {
        super();
    }

    public ZKSysResApplicationSystem(String pkId) {
        super(pkId);
    }

    /**
     * 应用系统名称
     */
    public ZKJson getName() {
        return name;
    }

    /**
     * 应用系统名称
     */
    public void setName(ZKJson name) {
        this.name = name;
    }

    /**
     * 应用系统代码，唯一
     */
    public String getCode() {
        return code;
    }

    /**
     * 应用系统代码，唯一
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 应用系统简称
     */
    public ZKJson getShortName() {
        return shortName;
    }

    /**
     * 应用系统简称
     */
    public void setShortName(ZKJson shortName) {
        this.shortName = shortName;
    }

    /**
     * 根据主键类型，重写主键生成；
     */
    @Override
    protected String genId() {
        return ZKIdUtils.genLongStringId();
    }

}