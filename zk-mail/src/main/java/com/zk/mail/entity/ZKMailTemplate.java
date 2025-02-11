/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.mail.entity;

import java.util.Map;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.ZKCoreConstants.ValidationRegexp;
import com.zk.core.utils.ZKFreeMarkersUtils;
import com.zk.core.utils.ZKIdUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * 发送邮件的模板
 * @author 
 * @version 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_mail_template", alias = "mailTemplate", orderBy = " c_create_date ASC ")
//@Alias("ZKMailTemplate")
public class ZKMailTemplate extends ZKBaseEntity<String, ZKMailTemplate> {
	
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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKMailTemplate());
        }
        return sqlHelper;
    }
    
    private static final long serialVersionUID = 1L;

    /**
     * 状态；0-启用；1-禁用；
     */
    public static interface KeyStatus {
        /**
         * 0-启用
         */
        public static final int normal = 0;

        /**
         * 1-禁用
         */
        public static final int disabled = 1;
    }

    /**
     * 邮件语言，ENUM(‘default’, zh_CN', 'en_US', 'zh_TW')
     */
    public static interface KeyLocale {
        /**
         * 默认
         */
        public static final String _default = "_default";

        /**
         * 简体中文
         */
        public static final String zh_CN = "zh_CN";

        /**
         * 英语 English
         */
        public static final String en_US = "en_US";

        /**
         * 繁体中文
         */
        public static final String zh_TW = "zh_TW";
    }
	
	/**
	 * 集团代码
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
	@ZKColumn(name = "c_group_code", isInsert = true, javaType = String.class)
	String groupCode;	
	/**
	 * 公司ID
	 */
	@Length(min = 0, max = 32, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_company_id", isInsert = true, javaType = String.class)
	String companyId;	
	/**
	 * 公司代码
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
	@ZKColumn(name = "c_company_code", isInsert = true, javaType = String.class)
	String companyCode;	
	/**
	 * 邮件类型ID
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_type_id", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String typeId;	
	/**
	 * 邮件类型代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
	@ZKColumn(name = "c_type_code", isInsert = true, javaType = String.class)
	String typeCode;	

	/**
     * 邮件语言，ENUM(‘default’, zh_CN', 'en_US', 'zh_TW')
     */
    @Length(min = 0, max = 16, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_locale", isInsert = true, javaType = String.class)
    String locale;

    /**
     * 状态：0-启用；1-禁用；
     */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_status", isInsert = true, javaType = Integer.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	Integer status;	
	/**
	 * 发件邮箱，默认使用 发送邮件服务器发送账号
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_send_address", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
	String sendAddress;	
	/**
	 * 发送邮件名称，可以使用 ${替换参数}
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 512, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_send_name", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
	String sendName;	
	/**
	 * 发送邮件主题，可以使用 ${替换参数}
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 512, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_subject", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
	String subject;	
	/**
	 * 邮件内容，可以使用 ${替换参数}
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 2048, message = "{zk.core.data.validation.length}")
	@ZKColumn(name = "c_content", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
	String content;	
	
	public ZKMailTemplate() {
		super();
	}

	public ZKMailTemplate(String pkId){
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
	 * 邮件类型ID	
	 */	
	public String getTypeId() {
		return typeId;
	}
	
	/**
	 * 邮件类型ID
	 */	
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	/**
	 * 邮件类型代码	
	 */	
	public String getTypeCode() {
		return typeCode;
	}
	
	/**
	 * 邮件类型代码
	 */	
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	/**
	 * 状态：0-启用；1-禁用；	
	 */	
	public Integer getStatus() {
		return status;
	}
	
	/**
	 * 状态：0-启用；1-禁用；
	 */	
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 发件邮箱，默认使用 发送邮件服务器发送账号	
	 */	
	public String getSendAddress() {
		return sendAddress;
	}
	
	/**
	 * 发件邮箱，默认使用 发送邮件服务器发送账号
	 */	
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}
	/**
	 * 发送邮件名称，可以使用 ${替换参数}	
	 */	
	public String getSendName() {
		return sendName;
	}
	
	/**
	 * 发送邮件名称，可以使用 ${替换参数}
	 */	
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	/**
	 * 发送邮件主题，可以使用 ${替换参数}	
	 */	
	public String getSubject() {
		return subject;
	}
	
	/**
	 * 发送邮件主题，可以使用 ${替换参数}
	 */	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * 邮件内容，可以使用 ${替换参数}	
	 */	
	public String getContent() {
		return content;
	}
	
	/**
	 * 邮件内容，可以使用 ${替换参数}
	 */	
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 邮件语言	
	 */	
	public String getLocale() {
		return locale;
	}
	
	/**
	 * 邮件语言
	 */	
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	/**
	 * 根据主键类型，重写主键生成；
	 */
	@Override
	@JsonIgnore
	public String genId() {
        return ZKIdUtils.genLongStringId();
    }
	
    @Transient
    @XmlTransient
    @JsonIgnore
    public String getSendName(Map<String, Object> params) throws Exception {
        if (ZKStringUtils.isEmpty(this.getSendName())) {
            return "";
        }
        return ZKFreeMarkersUtils.renderString(this.getSendName(), params);
    }

    @Transient
    @XmlTransient
    @JsonIgnore
    public String getSubject(Map<String, Object> params) throws Exception {
        if (ZKStringUtils.isEmpty(this.getSubject())) {
            return "";
        }
        return ZKFreeMarkersUtils.renderString(this.getSubject(), params);
    }

    @Transient
    @XmlTransient
    @JsonIgnore
    public String getContent(Map<String, Object> params) throws Exception {
        if (ZKStringUtils.isEmpty(this.getContent())) {
            return "";
        }
        return ZKFreeMarkersUtils.renderString(this.getContent(), params);
    }
}

