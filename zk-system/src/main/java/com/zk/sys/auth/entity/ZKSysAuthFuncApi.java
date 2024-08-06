/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.auth.entity;

import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.ZKCoreConstants.ValidationRegexp;
import com.zk.core.utils.ZKIdUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * 权限-功能API接口表
 * @author 
 * @version 
 */
@ZKTable(name = "t_sys_auth_func_api", alias = "sysAuthFuncApi", orderBy = " c_create_date ASC ")
public class ZKSysAuthFuncApi extends ZKBaseEntity<String, ZKSysAuthFuncApi> {
	
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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKSysAuthFuncApi());
        }
        return sqlHelper;
    }
    
    private static final long serialVersionUID = 1L;
	
	/**
	 * 权限 
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_auth_id", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String authId;	
	/**
	 * 功能API接口ID 
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_func_api_id", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String funcApiId;	
	/**
	 * 功能API接口 代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
	@ZKColumn(name = "c_func_api_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String funcApiCode;	

    /**
     * 功能API接口 系统代码
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_system_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String systemCode;
	
	public ZKSysAuthFuncApi() {
		super();
	}

	public ZKSysAuthFuncApi(String pkId){
		super(pkId);
	}
	
	/**
	 * 权限 	
	 */	
	public String getAuthId() {
		return authId;
	}
	
	/**
	 * 权限 
	 */	
	public void setAuthId(String authId) {
		this.authId = authId;
	}
	/**
	 * 功能API接口ID 	
	 */	
	public String getFuncApiId() {
		return funcApiId;
	}
	
	/**
	 * 功能API接口ID 
	 */	
	public void setFuncApiId(String funcApiId) {
		this.funcApiId = funcApiId;
	}
	/**
	 * 功能API接口 代码	
	 */	
	public String getFuncApiCode() {
		return funcApiCode;
	}
	
	/**
	 * 功能API接口 代码
	 */	
	public void setFuncApiCode(String funcApiCode) {
		this.funcApiCode = funcApiCode;
	}
	
	/**
	 * 根据主键类型，重写主键生成；
	 */
	@Override
	protected String genId() {
        return ZKIdUtils.genLongStringId();
    }

    /**
     * @return systemCode sa
     */
    public String getSystemCode() {
        return systemCode;
    }

    /**
     * @param systemCode
     *            the systemCode to set
     */
    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
	
}