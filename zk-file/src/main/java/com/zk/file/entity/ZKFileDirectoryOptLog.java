/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.file.entity;

import java.lang.String;
import com.zk.core.utils.ZKIdUtils;
import com.zk.db.commons.ZKDBQueryType;

import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKSqlProvider;

import com.zk.base.entity.ZKBaseEntity;

/**
 * 目录操作记录
 * @author 
 * @version 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_file_directory_opt_log", alias = "fileDirectoryOptLog", orderBy = " c_create_date ASC ")
public class ZKFileDirectoryOptLog extends ZKBaseEntity<String, ZKFileDirectoryOptLog> {
	
	static ZKSqlProvider sqlProvider;
	
    @Override 
    public ZKSqlProvider getSqlProvider() {
        return initSqlProvider();
    }
    
    public static ZKSqlProvider initSqlProvider() {
        if(sqlProvider == null) {
            sqlProvider = new ZKSqlProvider(new ZKSqlConvertDelegating(), new ZKFileDirectoryOptLog());
        }
        return sqlProvider;
    }
    
    private static final long serialVersionUID = 1L;
	
	/**
	 * 目录ID
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_directory_id", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	String directoryId;	
	/**
	 * 操作类型：0-创建、1-修改、2-启用、3-禁用、4-删除 
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_opt_type", isInsert = true, isUpdate = true, javaType = Integer.class, isQuery = true, queryType = ZKDBQueryType.EQ)
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