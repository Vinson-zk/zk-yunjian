/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.org.entity;

import com.zk.core.utils.ZKDateUtils;
import java.util.Date;
import java.lang.String;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKIdUtils;
import com.zk.db.commons.ZKDBQueryType;

import org.hibernate.validator.constraints.Range;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;

import com.zk.base.commons.ZKTreeSqlProvider;
import com.zk.base.entity.ZKBaseTreeEntity;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.commons.ZKSqlConvertDelegating;

/**
 * 公司表
 * @author 
 * @version 
 */
@ZKTable(name = "t_sys_org_company", alias = "sysOrgCompany", orderBy = " c_create_date ASC ")
public class ZKSysOrgCompany extends ZKBaseTreeEntity<String, ZKSysOrgCompany> {
	
	static ZKTreeSqlProvider sqlProvider;

    @Override
    public ZKTreeSqlProvider getTreeSqlProvider() {
        return initSqlProvider();
    }

    public static ZKTreeSqlProvider initSqlProvider() {
        if (sqlProvider == null) {
            sqlProvider = new ZKTreeSqlProvider(new ZKSqlConvertDelegating(), new ZKSysOrgCompany());
        }
        return sqlProvider;
    }
    
	private static final long serialVersionUID = 1L;
	
	/**
	 * 集团代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_group_code", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String groupCode;	
	/**
	 * 公司代码；全表唯一
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_code", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String code;	
	/**
	 * 公司名称
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@NotEmpty(message = "{zk.core.data.validation.notNull}")
	@ZKColumn(name = "c_name", isInsert = true, isUpdate = true, javaType = ZKJson.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	ZKJson name;	
	/**
	 * 公司 logo
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_logo", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String logo;	
	/**
	 * 公司 logo 原图
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_logo_original", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String logoOriginal;	
	/**
	 * 公司传真号
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_fax_num", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String faxNum;	
	/**
	 * 公司固定电话号
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_tel_num", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String telNum;	
	/**
	 * 公司手机号
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_phone_num", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String phoneNum;	
	/**
	 * 公司邮箱
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_mail", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String mail;	
	/**
	 * 公司法人
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_legal_person", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String legalPerson;	
	/**
	 * 公司法人证件类型; 字典项；
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_cert_type", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String certType;	
	/**
	 * 公司法人证件号
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_cert_num", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String certNum;	
	/**
	 * 公司状态；0-正常；1-禁用；2-审核中；
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_status", isInsert = true, isUpdate = true, javaType = Long.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	Long status;	
	/**
	 * 公司地址
	 */
	@ZKColumn(name = "c_address", isInsert = true, isUpdate = true, javaType = ZKJson.class, isQuery = false)
	ZKJson address;	
	/**
	 * 公司成立日期
	 */
	@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
	@JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd, timezone = timezone)
	@ZKColumn(name = "c_found_date", isInsert = true, isUpdate = true, javaType = Date.class, formats = "%Y-%m-%d %H:%i:%S", isQuery = true, queryType = ZKDBQueryType.GT)
	Date foundDate;	
	/**
	 * 公司注册日期
	 */
	@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
	@JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd, timezone = timezone)
	@ZKColumn(name = "c_register_date", isInsert = true, isUpdate = true, javaType = Date.class, formats = "%Y-%m-%d %H:%i:%S", isQuery = false)
	Date registerDate;	
	/**
	 * 公司注册地址
	 */
	@ZKColumn(name = "c_register_address", isInsert = true, isUpdate = true, javaType = ZKJson.class, isQuery = false)
	ZKJson registerAddress;	
	/**
	 * 公司注册机构
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_register_authority", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String registerAuthority;	
	/**
	 * 公司注册号
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_register_num", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String registerNum;	
	/**
	 *  公司简介
	 */
	@ZKColumn(name = "c_short_desc", isInsert = true, isUpdate = true, javaType = ZKJson.class, isQuery = false)
	ZKJson shortDesc;	
	/**
	 * 公司来源代码；与来源ID标识唯一；
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_source_code", isInsert = true, isUpdate = false, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	String sourceCode;	
	/**
	 * 来源ID标识，与来源代码唯一
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_source_id", isInsert = true, isUpdate = false, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	String sourceId;	
	
	public ZKSysOrgCompany() {
		super();
	}

	public ZKSysOrgCompany(String pkId){
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
	 * 公司代码；全表唯一	
	 */	
	public String getCode() {
		return code;
	}
	
	/**
	 * 公司代码；全表唯一
	 */	
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 公司名称	
	 */	
	public ZKJson getName() {
		return name;
	}
	
	/**
	 * 公司名称
	 */	
	public void setName(ZKJson name) {
		this.name = name;
	}
	/**
	 * 公司 logo	
	 */	
	public String getLogo() {
		return logo;
	}
	
	/**
	 * 公司 logo
	 */	
	public void setLogo(String logo) {
		this.logo = logo;
	}
	/**
	 * 公司 logo 原图	
	 */	
	public String getLogoOriginal() {
		return logoOriginal;
	}
	
	/**
	 * 公司 logo 原图
	 */	
	public void setLogoOriginal(String logoOriginal) {
		this.logoOriginal = logoOriginal;
	}
	/**
	 * 公司传真号	
	 */	
	public String getFaxNum() {
		return faxNum;
	}
	
	/**
	 * 公司传真号
	 */	
	public void setFaxNum(String faxNum) {
		this.faxNum = faxNum;
	}
	/**
	 * 公司固定电话号	
	 */	
	public String getTelNum() {
		return telNum;
	}
	
	/**
	 * 公司固定电话号
	 */	
	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}
	/**
	 * 公司手机号	
	 */	
	public String getPhoneNum() {
		return phoneNum;
	}
	
	/**
	 * 公司手机号
	 */	
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	/**
	 * 公司邮箱	
	 */	
	public String getMail() {
		return mail;
	}
	
	/**
	 * 公司邮箱
	 */	
	public void setMail(String mail) {
		this.mail = mail;
	}
	/**
	 * 公司法人	
	 */	
	public String getLegalPerson() {
		return legalPerson;
	}
	
	/**
	 * 公司法人
	 */	
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	/**
	 * 公司法人证件类型; 字典项；	
	 */	
	public String getCertType() {
		return certType;
	}
	
	/**
	 * 公司法人证件类型; 字典项；
	 */	
	public void setCertType(String certType) {
		this.certType = certType;
	}
	/**
	 * 公司法人证件号	
	 */	
	public String getCertNum() {
		return certNum;
	}
	
	/**
	 * 公司法人证件号
	 */	
	public void setCertNum(String certNum) {
		this.certNum = certNum;
	}
	/**
	 * 公司状态；0-正常；1-禁用；2-审核中；	
	 */	
	public Long getStatus() {
		return status;
	}
	
	/**
	 * 公司状态；0-正常；1-禁用；2-审核中；
	 */	
	public void setStatus(Long status) {
		this.status = status;
	}
	/**
	 * 公司地址	
	 */	
	public ZKJson getAddress() {
		return address;
	}
	
	/**
	 * 公司地址
	 */	
	public void setAddress(ZKJson address) {
		this.address = address;
	}
	/**
	 * 公司成立日期	
	 */	
	public Date getFoundDate() {
		return foundDate;
	}
	
	/**
	 * 公司成立日期
	 */	
	public void setFoundDate(Date foundDate) {
		this.foundDate = foundDate;
	}
	/**
	 * 公司注册日期	
	 */	
	public Date getRegisterDate() {
		return registerDate;
	}
	
	/**
	 * 公司注册日期
	 */	
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	/**
	 * 公司注册地址	
	 */	
	public ZKJson getRegisterAddress() {
		return registerAddress;
	}
	
	/**
	 * 公司注册地址
	 */	
	public void setRegisterAddress(ZKJson registerAddress) {
		this.registerAddress = registerAddress;
	}
	/**
	 * 公司注册机构	
	 */	
	public String getRegisterAuthority() {
		return registerAuthority;
	}
	
	/**
	 * 公司注册机构
	 */	
	public void setRegisterAuthority(String registerAuthority) {
		this.registerAuthority = registerAuthority;
	}
	/**
	 * 公司注册号	
	 */	
	public String getRegisterNum() {
		return registerNum;
	}
	
	/**
	 * 公司注册号
	 */	
	public void setRegisterNum(String registerNum) {
		this.registerNum = registerNum;
	}
	/**
	 *  公司简介	
	 */	
	public ZKJson getShortDesc() {
		return shortDesc;
	}
	
	/**
	 *  公司简介
	 */	
	public void setShortDesc(ZKJson shortDesc) {
		this.shortDesc = shortDesc;
	}
	/**
	 * 公司来源代码；与来源ID标识唯一；	
	 */	
	public String getSourceCode() {
		return sourceCode;
	}
	
	/**
	 * 公司来源代码；与来源ID标识唯一；
	 */	
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	/**
	 * 来源ID标识，与来源代码唯一	
	 */	
	public String getSourceId() {
		return sourceId;
	}
	
	/**
	 * 来源ID标识，与来源代码唯一
	 */	
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
	/**
	 * 根据主键类型，重写主键生成；
	 */
	@Override
	protected String genId() {
        return ZKIdUtils.genLongStringId();
    }
	
}