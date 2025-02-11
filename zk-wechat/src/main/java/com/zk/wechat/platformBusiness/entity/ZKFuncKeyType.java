/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.wechat.platformBusiness.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * 微信平台，功能 key 类型
 * 
 * @author
 * @version
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_wx_pb_func_key_type", alias = "funcKeyType", orderBy = " c_create_date ASC ")
public class ZKFuncKeyType extends ZKBaseEntity<String, ZKFuncKeyType> {

    static ZKDBSqlHelper sqlHelper;

    @Override
    public ZKDBSqlHelper getSqlHelper() {
        return sqlHelper();
    }

    public static ZKDBSqlHelper sqlHelper() {
        if (sqlHelper == null) {
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKFuncKeyType());
        }
        return sqlHelper;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 状态；0-启用；1-禁用；
     */
    public static interface KeyStatus {
        /**
         * 0-启用
         */
        public static final int normal = 0;

        /**
         * 1-禁用
         */
        public static final int disabled = 1;
    }

    /**
     * 功能类型代码；全表唯一
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_func_type_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String funcTypeCode;

    /**
     * 功能类型名称
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @NotEmpty(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_func_type_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    ZKJson funcTypeName;

    /**
     * 状态；0-启用；1-禁用；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 9, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_status", isInsert = true, javaType = Integer.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Integer status;

    /**
     * 功能类型说明
     */
    @ZKColumn(name = "c_func_type_desc", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true))
    ZKJson funcTypeDesc;

    public ZKFuncKeyType() {
        super();
    }

    public ZKFuncKeyType(String pkId) {
        super(pkId);
    }

    /**
     * 功能类型代码；全表唯一
     */
    public String getFuncTypeCode() {
        return funcTypeCode;
    }

    /**
     * 功能类型代码；全表唯一
     */
    public void setFuncTypeCode(String funcTypeCode) {
        this.funcTypeCode = funcTypeCode;
    }

    /**
     * 功能类型名称
     */
    public ZKJson getFuncTypeName() {
        return funcTypeName;
    }

    /**
     * 功能类型名称
     */
    public void setFuncTypeName(ZKJson funcTypeName) {
        this.funcTypeName = funcTypeName;
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
     * 功能类型说明
     */
    public ZKJson getFuncTypeDesc() {
        return funcTypeDesc;
    }

    /**
     * 功能类型说明
     */
    public void setFuncTypeDesc(ZKJson funcTypeDesc) {
        this.funcTypeDesc = funcTypeDesc;
    }

    /**
     * 根据主键类型，重写主键生成；
     */
    @Override
	@JsonIgnore
	public String genId() {
        return ZKIdUtils.genLongStringId();
    }

}