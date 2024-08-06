/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.file.entity;

import java.util.UUID;

import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zk.base.commons.ZKTreeSqlHelper;
import com.zk.base.entity.ZKBaseTreeEntity;
import com.zk.core.commons.ZKCoreConstants.ValidationRegexp;
import com.zk.core.utils.ZKFileUtils;
import com.zk.core.utils.ZKIdUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * 文件信息明细
 * @author 
 * @version 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_file_info", alias = "fileInfo", orderBy = { "c_type DESC", "c_sort ASC", "c_create_date ASC " })
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

            /**
             * 3-禁用
             */
            public static final int disabled = 3;
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
            public static final int limit = 0;

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
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
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
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
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
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_parent_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String parentCode;  
    /**
     * 文件名称 
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @NotEmpty(message = "{zk.core.data.validation.notNull}")
//    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = "^[a-zA-Z0-9_-][a-zA-Z0-9_\\.-]{0,63}$", message = "{zk.file.data.file.code}")
    @ZKColumn(name = "c_name", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String name;

    /**
     * 文件代码；公司下唯一;
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
//    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = "^[a-zA-Z0-9_-][a-zA-Z0-9_\\.-]{0,63}$", message = "{zk.file.data.file.code}")
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
    @Length(min = 0, max = 256, message = "{zk.core.data.validation.length.max}")
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
    @Range(min = 0, max = 3, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_stauts", isInsert = true, javaType = Long.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Integer stauts;

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
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_save_group_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String saveGroupCode;   
    
    /**
     * 文件权限类型：0-私有[需要有身份才获取]，1-开放[可以通过开放的接口获取]
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 1, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_security_type", isInsert = true, javaType = Long.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Integer securityType;
    
    /**
     * 文件作用域：0-普通；1-个人；可以扩展其他作用域，作用域尽量广义一点
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 1, message = "{zk.core.data.validation.rang.int}")
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
    @Range(min = 0, max = 1, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_type", isInsert = true, javaType = Boolean.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Integer type;
    
    /**
     * 访问文件的相对地址；目录设置为  /
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
    
    /**
     * 文件或目录的一些字段初始化；一般实体对象属性处理完后，根据当前状态初始化未填写字段的默认值。
     * 
     * 1. type: 类型为空设置类型默认为文件；
     * 2. name: 类型为文件时文件名称为空设置默认文件名称UUID
     * 3. code: 类型为文件时文件代码为空设置默认文件名称UUID；  
     * 4. stauts: 默认设置为 0-上传； 
     * 5. saveGroupCode: 保存分组代码为空自动填写UUID；
     * 6. saveUuid: 保存标识为空自动填写UUID；  
     * 7. securityType: 文件权限类型：权限类型为空默认填写权限类型为开放；
     * 8. actionScope: 作用域为空默认填写为普通；
     * 9. size: 为空默认 0；
     * 10. uri: 访问文件的相对地址；为空，默认填写 /
    *
    * @Title: afterAttrSet 
    * @Description: TODO(simple description this method what to do.) 
    * @author Vinson 
    * @date Dec 28, 2023 4:02:58 PM 
    * @return void
     */
    public void afterAttrSet() {

//        1. type: 类型为空设置类型默认为文件；
        if (this.getType() == null) {
            this.setType(ValueKey.Type.file);
        }
//        2. name: 类型为文件时文件名称为空设置默认文件名称UUID
//        3. code: 类型为文件时文件代码为空设置默认文件名称UUID； 
        if (this.getType().intValue() == ValueKey.Type.file) {
            if (ZKStringUtils.isEmpty(this.getName())) {
                this.setName(UUID.randomUUID().toString() + ZKFileUtils.getExtensionName(this.getOriginalName()));
            }
            if (ZKStringUtils.isEmpty(this.getCode())) {
                this.setCode(UUID.randomUUID().toString());
            }
        }
//        4. stauts: 默认设置为 0-上传； 
        if (this.getStauts() == null) {
            this.setStauts(ValueKey.Status.upload);
        }
//        5. saveGroupCode: 保存分组代码为空自动填写UUID；
        if (ZKStringUtils.isEmpty(this.getSaveGroupCode())) {
            // 为空时，自动生成
            this.setSaveGroupCode("G-" + UUID.randomUUID().toString());
        }
//        6. saveUuid: 保存标识为空自动填写UUID；  
        if (ZKStringUtils.isEmpty(this.getSaveUuid())) {
            // 为空时，自动生成
            this.setSaveUuid(UUID.randomUUID().toString());
        }
//        7. securityType: 文件权限类型：权限类型为空默认填写权限类型为开放；
        if (this.getSecurityType() == null) {
            this.setSecurityType(ValueKey.SecurityType.open);
        }
//        8. actionScope: 作用域为空默认填写为普通；
        if (this.getActionScope() == null) {
            this.setActionScope(ValueKey.ActionScope.normal);
        }
//        9. size: 为空默认 0；
        if (this.getSize() == null) {
            this.setSize(0l);
        }
//        10. uri: 访问文件的相对地址；为空，默认填写 /
        if (ZKStringUtils.isEmpty(this.getUri())) {
            this.setUri("/");
        }

    }

    /**
     * 深度考贝
     * 
     * @return
     * @see java.lang.Object#clone()
     */
    public ZKFileInfo zkClone() {
        ZKFileInfo fi = new ZKFileInfo();
        fi.setCompanyId(this.getCompanyId());
        fi.setCompanyCode(this.getCompanyCode());
        fi.setSaveGroupCode(this.getSaveGroupCode());
        fi.setParentId(this.getParentId());
        fi.setParentCode(this.getParentCode());
        return fi;
    }
	
}
