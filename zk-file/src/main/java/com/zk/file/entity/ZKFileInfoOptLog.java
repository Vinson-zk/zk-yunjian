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
 * 文件操作记录
 * @author 
 * @version 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_file_info_opt_log", alias = "fileInfoOptLog", orderBy = " c_create_date ASC ")
public class ZKFileInfoOptLog extends ZKBaseEntity<String, ZKFileInfoOptLog> {
	
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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKFileInfoOptLog());
        }
        return sqlHelper;
    }
    
    private static final long serialVersionUID = 1L;
	
	/**
	 * 文件ID
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_file_id", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String fileId;	
	/**
	 * 操作类型：0-上传、1-正常、2-失效、3-禁用、4-删除、5-下载/ 查看 
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_opt_type", isInsert = true, javaType = Long.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
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