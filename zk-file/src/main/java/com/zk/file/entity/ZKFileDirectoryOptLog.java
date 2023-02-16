/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.file.entity;

import com.zk.db.commons.ZKDBOptComparison;
import java.lang.String;
import com.zk.core.utils.ZKIdUtils;

import com.zk.db.annotation.ZKQuery;
import org.hibernate.validator.constraints.Range;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.annotation.ZKColumn;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Transient;

import com.zk.db.annotation.ZKTable;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import com.zk.base.entity.ZKBaseEntity;

/**
 * 目录操作记录
 * @author 
 * @version 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_file_directory_opt_log", alias = "fileDirectoryOptLog", orderBy = " c_create_date ASC ")
public class ZKFileDirectoryOptLog extends ZKBaseEntity<String, ZKFileDirectoryOptLog> {
	
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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKFileDirectoryOptLog());
        }
        return sqlHelper;
    }
    
    private static final long serialVersionUID = 1L;
	
	/**
	 * 目录ID
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_directory_id", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String directoryId;	
	/**
	 * 操作类型：0-创建、1-修改、2-启用、3-禁用、4-删除 
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_opt_type", isInsert = true, javaType = Integer.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	Integer optType;	
	
	public ZKFileDirectoryOptLog() {
		super();
	}

	public ZKFileDirectoryOptLog(String pkId){
		super(pkId);
	}
	
	/**
	 * 目录ID	
	 */	
	public String getDirectoryId() {
		return directoryId;
	}
	
	/**
	 * 目录ID
	 */	
	public void setDirectoryId(String directoryId) {
		this.directoryId = directoryId;
	}
	/**
	 * 操作类型：0-创建、1-修改、2-启用、3-禁用、4-删除 	
	 */	
	public Integer getOptType() {
		return optType;
	}
	
	/**
	 * 操作类型：0-创建、1-修改、2-启用、3-禁用、4-删除 
	 */	
	public void setOptType(Integer optType) {
		this.optType = optType;
	}
	
	/**
	 * 根据主键类型，重写主键生成；
	 */
	@Override
	protected String genId() {
        return ZKIdUtils.genLongStringId();
    }
	
}