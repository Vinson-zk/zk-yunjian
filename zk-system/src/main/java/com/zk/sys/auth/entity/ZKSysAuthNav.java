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
 * 权限-导航栏目表
 * @author 
 * @version 
 */
@ZKTable(name = "t_sys_auth_nav", alias = "sysAuthNav", orderBy = " c_create_date ASC ")
public class ZKSysAuthNav extends ZKBaseEntity<String, ZKSysAuthNav> {
	
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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKSysAuthNav());
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
	 * 导航栏目
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_nav_id", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String navId;	
	/**
	 * 导航栏目代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
	@ZKColumn(name = "c_nav_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String navCode;	
	
	public ZKSysAuthNav() {
		super();
	}

	public ZKSysAuthNav(String pkId){
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
	 * 导航栏目	
	 */	
	public String getNavId() {
		return navId;
	}
	
	/**
	 * 导航栏目
	 */	
	public void setNavId(String navId) {
		this.navId = navId;
	}
	/**
	 * 导航栏目代码	
	 */	
	public String getNavCode() {
		return navCode;
	}
	
	/**
	 * 导航栏目代码
	 */	
	public void setNavCode(String navCode) {
		this.navCode = navCode;
	}
	
	/**
	 * 根据主键类型，重写主键生成；
	 */
	@Override
	protected String genId() {
        return ZKIdUtils.genLongStringId();
    }
	
}