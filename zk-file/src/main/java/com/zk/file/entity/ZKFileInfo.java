/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.file.entity;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zk.base.commons.ZKTreeSqlHelper;
import com.zk.base.entity.ZKBaseTreeEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKIdUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;

/**
 * 文件信息明细
 * @author 
 * @version 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_file_info", alias = "fileInfo", orderBy = " c_type DESC, c_sort ASC, c_create_date ASC ")
public class ZKFileInfo extends ZKBaseTreeEntity<String, ZKFileInfo> {
	
    static ZKTreeSqlHelper sqlHelper;

	@Transient
    @XmlTransient
    @JsonIgnore
    @Override
    public ZKTreeSqlHelper getTreeSqlHelper() {
        return sqlTreeHelper();
    }

	@Transient
    @XmlTransient
    @JsonIgnore
    public static ZKTreeSqlHelper sqlTreeHelper() {
        if (sqlHelper == null) {
            sqlHelper = new ZKTreeSqlHelper(new ZKSqlConvertDelegating(), new ZKFileInfo());
        }
        return sqlHelper;
    }
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 
     */
    public static interface ValueKey {
        // 状态：0-上传、1-正常、2-失效[上传后在指定时间来，没修改为正常时，会自动失效，会删除实际文件]、3-禁用[不能下载，但能查看]
        public static interface Status {
            /**
             * 0-上传
             */
            public static final int upload = 0;

            /**
             * 1-正常
             */
            public static final int normal = 1;
            
            /**
             * 2-失效
             */
            public static final int invalid = 2;
        }
        // 数据类型；0-文件；1-目录；文件不能做父节点；即 c_type = 0 的数据，不能做为父节点；
        public static interface Type {
            /**
             * 0-文件
             */
            public static final int file = 0;

            /**
             * 1-目录
             */
            public static final int document = 1;
        }

        // 文件权限类型：0-私有[需要有身份才获取]，1-开放[可以通过开放的接口获取]
        public static interface SecurityType {
            /**
             * 0-私有[需要有身份才获取]
             */
            public static final int personal = 0;

            /**
             * 1-开放[可以通过开放的接口获取]
             */
            public static final int open = 1;
        }

        // 文件作用域：0-普通；1-个人；可以扩展其他作用域，作用域尽量广义一点
        public static interface ActionScope {
            /**
             * 0-普通
             */
            public static final int normal = 0;

            /**
             * 1-个人
             */
            public static final int personal = 1;
        }

    }

    /**
     * 集团代码
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
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
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_company_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String companyCode; 
    /**
     * 父目录ID；文件不能做父节点；即 c_type = 0 的数据，不能做为父节点；
     */
//    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_parent_id", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String parentId;    
    /**
     * 父目录代码；文件不能做父节点；即 c_type = 0 的数据，不能做为父节点；
     */
//    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_parent_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String parentCode;  
    /**
     * 文件名称 
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @NotEmpty(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    ZKJson name;    
    /**
     * 文件代码；公司下唯一
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String code;    
    /**
     * 文件原始名称 
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_original_name", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
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
    @ZKColumn(name = "c_size", isInsert = true, javaType = Long.class, update = @ZKUpdate(true))
    Long size;  
    /**
     * 状态：0-上传、1-正常、2-失效[上传后在指定时间来，没修改为正常时，会自动失效，会删除实际文件]、3-禁用[不能下载，但能查看] 
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_stauts", isInsert = true, javaType = Long.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Long stauts;    
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
    @ZKColumn(name = "c_save_group_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String saveGroupCode;   
    
    /**
     * 文件权限类型：0-私有[需要有身份才获取]，1-开放[可以通过开放的接口获取]
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_security_type", isInsert = true, javaType = Long.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Integer securityType;
    
    /**
     * 文件作用域：0-普通；1-个人；可以扩展其他作用域，作用域尽量广义一点
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_action_scope", isInsert = true, javaType = Long.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Integer actionScope;

    /**
     * 排序
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_sort", isInsert = true, javaType = Long.class, update = @ZKUpdate(true))
    Integer sort;

    /**
     * 数据类型；0-文件；1-目录；文件不能做父节点；即 c_type = 0 的数据，不能做为父节点；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_type", isInsert = true, javaType = Boolean.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Integer type;
    
    /**
     * 访问文件的相对地址；目录时设置为 /
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_uri", isInsert = true, update = @ZKUpdate(true), javaType = Boolean.class)
    String uri;

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
     * 父目录ID；文件不能做父节点；即 c_type = 0 的数据，不能做为父节点； 
     */ 
    public String getParentId() {
        return parentId;
    }
    
    /**
     * 父目录ID；文件不能做父节点；即 c_type = 0 的数据，不能做为父节点；
     */ 
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    /**
     * 父目录代码；文件不能做父节点；即 c_type = 0 的数据，不能做为父节点； 
     */ 
    public String getParentCode() {
        return parentCode;
    }
    
    /**
     * 父目录代码；文件不能做父节点；即 c_type = 0 的数据，不能做为父节点；
     */ 
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
    /**
     * 文件名称     
     */ 
    public ZKJson getName() {
        return name;
    }
    
    /**
     * 文件名称 
     */ 
    public void setName(ZKJson name) {
        this.name = name;
    }
    /**
     * 文件代码；公司下唯一   
     */ 
    public String getCode() {
        return code;
    }
    
    /**
     * 文件代码；公司下唯一
     */ 
    public void setCode(String code) {
        this.code = code;
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
     * 状态：0-上传、1-正常、2-失效[上传后在指定时间来，没修改为正常时，会自动失效，会删除实际文件]、3-禁用[不能下载，但能查看]   
     */ 
    public Long getStauts() {
        return stauts;
    }
    
    /**
     * 状态：0-上传、1-正常、2-失效[上传后在指定时间来，没修改为正常时，会自动失效，会删除实际文件]、3-禁用[不能下载，但能查看] 
     */ 
    public void setStauts(Long stauts) {
        this.stauts = stauts;
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
     * 排序   
     */ 
    public Integer getSort() {
        return sort;
    }
    
    /**
     * 排序
     */ 
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    /**
     * 数据类型；0-文件；1-目录；文件不能做父节点；即 c_type = 0 的数据，不能做为父节点；    
     */ 
    public Integer getType() {
        return type;
    }
    
    /**
     * 数据类型；0-文件；1-目录；文件不能做父节点；即 c_type = 0 的数据，不能做为父节点；
     */ 
    public void setType(Integer type) {
        this.type = type;
    }
    
    /**
     * @return uri sa
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri
     *            the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * 根据主键类型，重写主键生成；
     */
    @Override
    protected String genId() {
        return ZKIdUtils.genLongStringId();
    }

    public void initAttr() {
        // saveUuid 文件保存标识，全表唯一标识，UUID，与ID 作用重复，生成一个保存以备用
        if (ZKStringUtils.isEmpty(this.getSaveUuid())) {
            // 为空时，自动生成
            this.setSaveUuid(UUID.randomUUID().toString());
        }
        // saveGroupCode 文件分组代码，全表唯一，自动生成，UUID；以便附件分组
        if (ZKStringUtils.isEmpty(this.getSaveGroupCode())) {
            // 为空时，自动生成
            this.setSaveGroupCode(UUID.randomUUID().toString());
        }
        // securityType 文件权限类型：0-私有[需要有身份才获取]，1-开放[可以通过开放的接口获取]
        if (this.getSecurityType() == null) {
            this.setSecurityType(ValueKey.SecurityType.open);
        }
        // actionScope 文件作用域：0-普通；1-个人；可以扩展其他作用域，作用域尽量广义一点
        if (this.getActionScope() == null) {
            this.setActionScope(ValueKey.ActionScope.normal);
        }
        // 目录的 uri 设置为 /
        if (ValueKey.Type.document == this.getType().intValue() && ZKStringUtils.isEmpty(this.getUri())) {
            this.setUri("/");
        }
    }
	
}