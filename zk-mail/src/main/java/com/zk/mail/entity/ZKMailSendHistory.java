/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.mail.entity;

import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKIdUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKTable;
import com.zk.db.commons.ZKDBQueryType;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKSqlProvider;

/**
 * 邮件发送历史
 * @author 
 * @version 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_mail_send_history", alias = "mailSendHistory", orderBy = " c_create_date ASC ")
public class ZKMailSendHistory extends ZKBaseEntity<String, ZKMailSendHistory> {
	
	static ZKSqlProvider sqlProvider;
	
    @Override 
    public ZKSqlProvider getSqlProvider() {
        return initSqlProvider();
    }
    
    public static ZKSqlProvider initSqlProvider() {
        if(sqlProvider == null) {
            sqlProvider = new ZKSqlProvider(new ZKSqlConvertDelegating(), new ZKMailSendHistory());
        }
        return sqlProvider;
    }
    
    private static final long serialVersionUID = 1L;
	
	/**
	 * 集团代码
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_group_code", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String groupCode;	
	/**
	 * 公司ID
	 */
	@Length(min = 0, max = 32, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_company_id", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String companyId;	
	/**
	 * 公司代码
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_company_code", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String companyCode;	
	/**
	 * 邮件类型ID
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_type_id", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	String typeId;	
	/**
	 * 邮件类型代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_type_code", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String typeCode;	
	/**
	 * 发送的邮件模板ID
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_mail_template_id", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String mailTemplateId;	
	/**
	 * 参数 json
	 */
	@ZKColumn(name = "c_params", isInsert = true, isUpdate = true, javaType = ZKJson.class, isQuery = false)
	ZKJson params;	
	/**
	 * 发件邮箱
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_send_address", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String sendAddress;	
	/**
	 * 发送邮件名称
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 512, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_send_name", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String sendName;	
	/**
	 * 发送邮件主题
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 512, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_subject", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String subject;	
	/**
	 * 邮件内容
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 2048, message = "{zk.core.data.validation.length}")
	@ZKColumn(name = "c_content", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String content;	
	/**
	 * 邮件语言
	 */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_locale", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String locale;	
	/**
	 * 发送标识，有时就记录，没有就为空
	 */
	@Length(min = 0, max = 512, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_send_flag", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String sendFlag;	
	
	public ZKMailSendHistory() {
		super();
	}

	public ZKMailSendHistory(String pkId){
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
	 * 发送的邮件模板ID	
	 */	
	public String getMailTemplateId() {
		return mailTemplateId;
	}
	
	/**
	 * 发送的邮件模板ID
	 */	
	public void setMailTemplateId(String mailTemplateId) {
		this.mailTemplateId = mailTemplateId;
	}
	/**
	 * 参数 json	
	 */	
	public ZKJson getParams() {
		return params;
	}
	
	/**
	 * 参数 json
	 */	
	public void setParams(ZKJson params) {
		this.params = params;
	}
	/**
	 * 发件邮箱	
	 */	
	public String getSendAddress() {
		return sendAddress;
	}
	
	/**
	 * 发件邮箱
	 */	
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}
	/**
	 * 发送邮件名称	
	 */	
	public String getSendName() {
		return sendName;
	}
	
	/**
	 * 发送邮件名称
	 */	
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	/**
	 * 发送邮件主题	
	 */	
	public String getSubject() {
		return subject;
	}
	
	/**
	 * 发送邮件主题
	 */	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * 邮件内容	
	 */	
	public String getContent() {
		return content;
	}
	
	/**
	 * 邮件内容
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
	 * 发送标识，有时就记录，没有就为空	
	 */	
	public String getSendFlag() {
		return sendFlag;
	}
	
	/**
	 * 发送标识，有时就记录，没有就为空
	 */	
	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}
	
	/**
	 * 根据主键类型，重写主键生成；
	 */
	@Override
	protected String genId() {
        return ZKIdUtils.genLongStringId();
    }
	
    public static ZKMailSendHistory as(String sendFlag, ZKMailTemplate mailTemplate, Map<String, Object> params)
            throws Exception {
	    ZKMailSendHistory mailSendHistory = new ZKMailSendHistory();
        mailSendHistory.setGroupCode(mailTemplate.getGroupCode());
        mailSendHistory.setCompanyId(mailTemplate.getCompanyId());
        mailSendHistory.setCompanyCode(mailTemplate.getCompanyCode());
        mailSendHistory.setTypeId(mailTemplate.getTypeId());
        mailSendHistory.setTypeCode(mailTemplate.getTypeCode());
        mailSendHistory.setMailTemplateId(mailTemplate.getPkId());
        mailSendHistory.setSendAddress(mailTemplate.getSendAddress());
        mailSendHistory.setSendName(mailTemplate.getSendName(params));
        mailSendHistory.setSubject(mailTemplate.getSubject(params));
        mailSendHistory.setContent(mailTemplate.getContent(params));
	    mailSendHistory.setLocale(mailTemplate.getLocale());  
        mailSendHistory.setParams(ZKJson.parse(ZKJsonUtils.writeObjectJson(params)));
        mailSendHistory.setSendFlag(sendFlag);
        return mailSendHistory;
	}

}