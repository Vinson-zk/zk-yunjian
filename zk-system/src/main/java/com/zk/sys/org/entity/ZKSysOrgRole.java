/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.org.entity;

import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
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

/**
 * 角色表
 * @author 
 * @version 
 */
@ZKTable(name = "t_sys_org_role", alias = "sysOrgRole", orderBy = " c_create_date ASC ")
public class ZKSysOrgRole extends ZKBaseEntity<String, ZKSysOrgRole> {
	
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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKSysOrgRole());
        }
        return sqlHelper;
    }
    
    private static final long serialVersionUID = 1L;

    /**
     * 角色状态；0-正常；1-撤销；
     */
    public static interface KeyStatus {
        /**
         * 0-正常
         */
        public static final int normal = 0;

        /**
         * 1-撤销
         */
        public static final int disabled = 1;
    }

    /**
     * 角色类型；0-公司级角色；1-部门级角色，指定为某个部门的角色；
     */
    public static interface KeyType {
        /**
         * 0-公司级角色
         */
        public static final int company = 0;

        /**
         * 1-部门级角色，指定为某个部门的角色；
         */
        public static final int dept = 1;
    }
	
	/**
	 * 集团代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
	@ZKColumn(name = "c_group_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String groupCode;	
	/**
	 * 公司ID 
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_company_id", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String companyId;	
	/**
	 * 公司代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
	@ZKColumn(name = "c_company_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String companyCode;	
	/**
	 * 角色代码;公司下唯一
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
	@ZKColumn(name = "c_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String code;	
	/**
	 * 角色名称
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@NotEmpty(message = "{zk.core.data.validation.notNull}")
	@ZKColumn(name = "c_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	ZKJson name;	
	
    /**
     * 角色类型；0-公司级角色；1-部门级角色，指定为某个部门的角色；
     */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_type", isInsert = true, javaType = Long.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Integer type;

	/**
	 * 部门ID 
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_dept_id", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String deptId;	
	/**
	 * 部门代码
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_dept_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String deptCode;	
	/**
	 * 角色状态；0-正常；1-禁用；
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 9, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_status", isInsert = true, javaType = Long.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Integer status;

	/**
	 *  角色简介
	 */
	@ZKColumn(name = "c_short_desc", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true))
	ZKJson shortDesc;	
	/**
	 * 来源代码；与来源ID标识唯一；
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
	@ZKColumn(name = "c_source_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String sourceCode;	
	/**
	 * 来源ID标识，与来源代码唯一
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_source_id", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String sourceId;	
	
	public ZKSysOrgRole() {
		super();
	}

	public ZKSysOrgRole(String pkId){
		super(pkId);
	}
	
    // 查询辅助字段
    @Transient
    @JsonIgnore
    @XmlTransient
    String searchValue;

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
	 * 公司ID 	
	 */	
	public String getCompanyId() {
		return companyId;
	}
	
	/**
	 * 公司ID 
	 */	
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	/**
	 * 公司代码	
	 */	
	public String getCompanyCode() {
		return companyCode;
	}
	
	/**
	 * 公司代码
	 */	
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	/**
	 * 角色代码;公司下唯一	
	 */	
	public String getCode() {
		return code;
	}
	
	/**
	 * 角色代码;公司下唯一
	 */	
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 角色名称	
	 */	
	public ZKJson getName() {
		return name;
	}
	
	/**
	 * 角色名称
	 */	
	public void setName(ZKJson name) {
		this.name = name;
	}
	/**
	 * 角色类型；0-正常；1-部门角色，指定为某个部门的角色；	
	 */	
    public Integer getType() {
		return type;
	}
	
	/**
	 * 角色类型；0-正常；1-部门角色，指定为某个部门的角色；
	 */	
    public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 部门ID 	
	 */	
	public String getDeptId() {
		return deptId;
	}
	
	/**
	 * 部门ID 
	 */	
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	/**
	 * 部门代码	
	 */	
	public String getDeptCode() {
		return deptCode;
	}
	
	/**
	 * 部门代码
	 */	
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	/**
	 * 角色状态；0-正常；1-禁用；	
	 */	
    public Integer getStatus() {
		return status;
	}
	
	/**
	 * 角色状态；0-正常；1-禁用；
	 */	
    public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 *  角色简介	
	 */	
	public ZKJson getShortDesc() {
		return shortDesc;
	}
	
	/**
	 *  角色简介
	 */	
	public void setShortDesc(ZKJson shortDesc) {
		this.shortDesc = shortDesc;
	}
	/**
	 * 来源代码；与来源ID标识唯一；	
	 */	
	public String getSourceCode() {
		return sourceCode;
	}
	
	/**
	 * 来源代码；与来源ID标识唯一；
	 */	
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	/**
	 * 来源ID标识，与来源代码唯一	
	 */	
	public String getSourceId() {
		return sourceId;
	}
	
	/**
	 * 来源ID标识，与来源代码唯一
	 */	
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
	/**
	 * 根据主键类型，重写主键生成；
	 */
	@Override
	protected String genId() {
        return ZKIdUtils.genLongStringId();
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
		ZKDBQueryWhere where = sqlConvert.resolveQueryCondition(mapInfo);
        // 制作一个根据名称和代码同时查询的 查询条件，用过度 java 属性 searchValue 为传参数值
		ZKDBQueryWhere sWhere = ZKDBQueryWhere.asOr("(", ")",
				ZKDBQueryCol.as(ZKDBOptComparison.LIKE, "c_name", "searchValue", String.class, null, false),
				ZKDBQueryCol.as(ZKDBOptComparison.LIKE, "c_code", "searchValue", String.class, null, false));

		where.put(ZKDBQueryScript.asIf(sWhere, 0, "searchValue", String.class));
        return where;
    }
}
