/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.file.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.base.commons.ZKTreeSqlHelper;
import com.zk.base.entity.ZKBaseTreeEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKIdUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;

/**
 * 文件目录
 * @author 
 * @version 
 */
@ZKTable(name = "t_file_directory", alias = "fileDirectory", orderBy = " c_create_date ASC ")
public class ZKFileDirectory extends ZKBaseTreeEntity<String, ZKFileDirectory> {
	
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
            sqlHelper = new ZKTreeSqlHelper(new ZKSqlConvertDelegating(), new ZKFileDirectory());
        }
        return sqlHelper;
    }
    
	private static final long serialVersionUID = 1L;

	/**
	 * 状态：0-启用、1-禁用
	 */
	public static interface KeyStatus {
		/**
		 * 0-启用
		 */
		public static final int enabled = 0;

		/**
		 * 1-禁用
		 */
		public static final int disabled = 1;
	}
	
	/**
	 * 集团代码
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_group_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
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
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_company_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String companyCode;	
	/**
	 * 代码，公司代码加全表唯一
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String code;	
	/**
	 * 目录名称
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@NotEmpty(message = "{zk.core.data.validation.notNull}")
	@ZKColumn(name = "c_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	ZKJson name;	
	/**
	 * 状态：0-启用、1-禁用 
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_stauts", isInsert = true, javaType = Integer.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	Integer stauts;	
	
	public ZKFileDirectory() {
		super();
	}

	public ZKFileDirectory(String pkId){
		super(pkId);
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
	 * 代码，公司代码加全表唯一	
	 */	
	public String getCode() {
		return code;
	}
	
	/**
	 * 代码，公司代码加全表唯一
	 */	
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 目录名称	
	 */	
	public ZKJson getName() {
		return name;
	}
	
	/**
	 * 目录名称
	 */	
	public void setName(ZKJson name) {
		this.name = name;
	}
	/**
	 * 状态：0-启用、1-禁用 	
	 */	
	public Integer getStauts() {
		return stauts;
	}
	
	/**
	 * 状态：0-启用、1-禁用 
	 */	
	public void setStauts(Integer stauts) {
		this.stauts = stauts;
	}
	
	/**
	 * 根据主键类型，重写主键生成；
	 */
	@Override
	protected String genId() {
        return ZKIdUtils.genLongStringId();
    }
	
}