/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.file.entity;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.utils.ZKIdUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

/**
 * 文件信息明细
 * @author 
 * @version 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_file_info", alias = "fileInfo", orderBy = " c_create_date ASC ")
public class ZKFileInfo extends ZKBaseEntity<String, ZKFileInfo> {
	
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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKFileInfo());
        }
        return sqlHelper;
    }
    
    private static final long serialVersionUID = 1L;
	
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
	@ZKColumn(name = "c_company_id", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String companyId;	
	/**
	 * 公司代码
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_company_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String companyCode;	
	/**
	 * ID
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_directory_id", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String directoryId;	
	/**
	 * 目录代码 
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_directory_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String directoryCode;	
	/**
	 * 文件保存标识，全表唯一标识，UUID，与ID 作用重复，生成一个保存以备用
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_save_uuid", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String saveUuid;	
	/**
	 * 文件分组代码，全表唯一，自动生成，UUID；以便附件分组 
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_save_group_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String saveGroupCode;	
	/**
	 * 状态：0-上传、1-正常、2-失效[上传后在指定时间内，没修改为正常时，会自动失效，会删除实际文件]、3-禁用[不能下载，但能查看]
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_stauts", isInsert = true, javaType = Integer.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	Integer stauts;	
	/**
	 * 文件名称 
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_name", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String name;	
	/**
	 * 文件原始名称 
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_original_name", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String originalName;	
	/**
	 * 文件类型 
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_content_type", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String contentType;	
	/**
	 *  文件大小；单位 b
	 */
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_size", isInsert = true, javaType = Long.class)
	Long size;	
	/**
	 *  文件权限类型：0-私有[需要有身份才获取]，1-开放[可以通过开放的接口获取]
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_security_type", isInsert = true, javaType = Integer.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	Integer securityType;	
	/**
	 *  文件作用域：0-普通；1-个人；可以扩展其他作用域，作用域尽量广义一点
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_action_scope", isInsert = true, javaType = Integer.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	Integer actionScope;	
	
	public ZKFileInfo() {
		super();
	}

	public ZKFileInfo(String pkId){
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
	 * ID	
	 */	
	public String getDirectoryId() {
		return directoryId;
	}
	
	/**
	 * ID
	 */	
	public void setDirectoryId(String directoryId) {
		this.directoryId = directoryId;
	}
	/**
	 * 目录代码 	
	 */	
	public String getDirectoryCode() {
		return directoryCode;
	}
	
	/**
	 * 目录代码 
	 */	
	public void setDirectoryCode(String directoryCode) {
		this.directoryCode = directoryCode;
	}
	/**
	 * 文件保存标识，全表唯一标识，UUID，与ID 作用重复，生成一个保存以备用	
	 */	
	public String getSaveUuid() {
		return saveUuid;
	}
	
	/**
	 * 文件保存标识，全表唯一标识，UUID，与ID 作用重复，生成一个保存以备用
	 */	
	public void setSaveUuid(String saveUuid) {
		this.saveUuid = saveUuid;
	}
	/**
	 * 文件分组代码，全表唯一，自动生成，UUID；以便附件分组 	
	 */	
	public String getSaveGroupCode() {
		return saveGroupCode;
	}
	
	/**
	 * 文件分组代码，全表唯一，自动生成，UUID；以便附件分组 
	 */	
	public void setSaveGroupCode(String saveGroupCode) {
		this.saveGroupCode = saveGroupCode;
	}
	/**
	 * 状态：0-上传、1-正常、2-失效[上传后在指定时间来，没修改为正常时，会自动失效，会删除实际文件]、3-禁用[不能下载，但能查看] 	
	 */	
	public Integer getStauts() {
		return stauts;
	}
	
	/**
	 * 状态：0-上传、1-正常、2-失效[上传后在指定时间来，没修改为正常时，会自动失效，会删除实际文件]、3-禁用[不能下载，但能查看] 
	 */	
	public void setStauts(Integer stauts) {
		this.stauts = stauts;
	}
	/**
	 * 文件名称 	
	 */	
	public String getName() {
		return name;
	}
	
	/**
	 * 文件名称 
	 */	
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 文件原始名称 	
	 */	
	public String getOriginalName() {
		return originalName;
	}
	
	/**
	 * 文件原始名称 
	 */	
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	/**
	 * 文件类型 	
	 */	
	public String getContentType() {
		return contentType;
	}
	
	/**
	 * 文件类型 
	 */	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	/**
	 *  文件大小；单位 b	
	 */	
	public Long getSize() {
		return size;
	}
	
	/**
	 *  文件大小；单位 b
	 */	
	public void setSize(Long size) {
		this.size = size;
	}
	/**
	 *  文件权限类型：0-私有[需要有身份才获取]，1-开放[可以通过开放的接口获取]	
	 */	
	public Integer getSecurityType() {
		return securityType;
	}
	
	/**
	 *  文件权限类型：0-私有[需要有身份才获取]，1-开放[可以通过开放的接口获取]
	 */	
	public void setSecurityType(Integer securityType) {
		this.securityType = securityType;
	}
	/**
	 *  文件作用域：0-普通；1-个人；可以扩展其他作用域，作用域尽量广义一点	
	 */	
	public Integer getActionScope() {
		return actionScope;
	}
	
	/**
	 *  文件作用域：0-普通；1-个人；可以扩展其他作用域，作用域尽量广义一点
	 */	
	public void setActionScope(Integer actionScope) {
		this.actionScope = actionScope;
	}
	
	/**
	 * 根据主键类型，重写主键生成；
	 */
	@Override
	protected String genId() {
        return ZKIdUtils.genLongStringId();
    }
	
}