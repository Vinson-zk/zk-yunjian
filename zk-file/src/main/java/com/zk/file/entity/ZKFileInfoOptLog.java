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
 * 文件操作记录
 * @author 
 * @version 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_file_info_opt_log", alias = "fileInfoOptLog", orderBy = " c_create_date ASC ")
public class ZKFileInfoOptLog extends ZKBaseEntity<String, ZKFileInfoOptLog> {
	
	static ZKSqlProvider sqlProvider;
	
    @Override 
    public ZKSqlProvider getSqlProvider() {
        return initSqlProvider();
    }
    
    public static ZKSqlProvider initSqlProvider() {
        if(sqlProvider == null) {
            sqlProvider = new ZKSqlProvider(new ZKSqlConvertDelegating(), new ZKFileInfoOptLog());
        }
        return sqlProvider;
    }
    
    private static final long serialVersionUID = 1L;
	
	/**
	 * 文件ID
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_file_id", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	String fileId;	
	/**
	 * 操作类型：0-上传、1-正常、2-失效、3-禁用、4-删除、5-下载/ 查看 
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_opt_type", isInsert = true, isUpdate = true, javaType = Long.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	Long optType;	
	
	public ZKFileInfoOptLog() {
		super();
	}

	public ZKFileInfoOptLog(String pkId){
		super(pkId);
	}
	
	/**
	 * 文件ID	
	 */	
	public String getFileId() {
		return fileId;
	}
	
	/**
	 * 文件ID
	 */	
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	/**
	 * 操作类型：0-上传、1-正常、2-失效、3-禁用、4-删除、5-下载/ 查看 	
	 */	
	public Long getOptType() {
		return optType;
	}
	
	/**
	 * 操作类型：0-上传、1-正常、2-失效、3-禁用、4-删除、5-下载/ 查看 
	 */	
	public void setOptType(Long optType) {
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