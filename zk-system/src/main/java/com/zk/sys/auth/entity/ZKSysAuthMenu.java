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
 * 权限-菜单资源表
 * @author 
 * @version 
 */
@ZKTable(name = "t_sys_auth_menu", alias = "sysAuthMenu", orderBy = " c_create_date ASC ")
public class ZKSysAuthMenu extends ZKBaseEntity<String, ZKSysAuthMenu> {
	
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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKSysAuthMenu());
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
	 * 菜单ID 
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_menu_id", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String menuId;	
	/**
	 * 菜单代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
	@ZKColumn(name = "c_menu_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String menuCode;	
	
	public ZKSysAuthMenu() {
		super();
	}

	public ZKSysAuthMenu(String pkId){
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
	 * 菜单ID 	
	 */	
	public String getMenuId() {
		return menuId;
	}
	
	/**
	 * 菜单ID 
	 */	
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	/**
	 * 菜单代码	
	 */	
	public String getMenuCode() {
		return menuCode;
	}
	
	/**
	 * 菜单代码
	 */	
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	
	/**
	 * 根据主键类型，重写主键生成；
	 */
	@Override
	protected String genId() {
        return ZKIdUtils.genLongStringId();
    }
}

