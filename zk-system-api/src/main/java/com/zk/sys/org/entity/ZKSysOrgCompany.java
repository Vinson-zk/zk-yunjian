/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSysOrgCompany.java 
* @author Vinson 
* @Package com.zk.sys.org.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 19, 2024 5:26:25 PM 
* @version V1.0 
*/
package com.zk.sys.org.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zk.base.commons.ZKTreeSqlHelper;
import com.zk.base.entity.ZKBaseTreeEntity;
import com.zk.core.commons.ZKCoreConstants.ValidationRegexp;
import com.zk.core.commons.ZKValidationGroup;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKIdUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBMapInfo;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKDBQueryCol;
import com.zk.db.commons.ZKDBQueryWhere;
import com.zk.db.commons.ZKSqlConvert;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBQueryScript;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.xml.bind.annotation.XmlTransient;

/** 
* @ClassName: ZKSysOrgCompany 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_sys_org_company", alias = "sysOrgCompany", orderBy = " c_create_date ASC ")
public class ZKSysOrgCompany extends ZKBaseTreeEntity<String, ZKSysOrgCompany> {

    static ZKTreeSqlHelper sqlHelper;

    @Transient
    @XmlTransient
    @JsonIgnore
    @Override
    public ZKTreeSqlHelper getTreeSqlHelper() {
        return sqlHelper();
    }

    @Transient
    @XmlTransient
    @JsonIgnore
    public static ZKTreeSqlHelper sqlHelper() {
        if (sqlHelper == null) {
            sqlHelper = new ZKTreeSqlHelper(new ZKSqlConvertDelegating(), new ZKSysOrgCompany());
        }
        return sqlHelper;
    }

    private static final long serialVersionUID = 1L;

    public static final String certSeparator = ",";

    /**
     * 公司状态；0-正常；1-禁用；2-上级公司审核中；3-平台审核中；4-待提交；
     */
    public static interface KeyStatus {
        /**
         * 0-正常
         */
        public static final int normal = 0;

        /**
         * 1-禁用
         */
        public static final int disabled = 1;

        /**
         * 2-上级公司审核中
         */
        public static final int auditCompanyIng = 2;

        /**
         * 3-平台审核中
         */
        public static final int auditPlatformIng = 3;

        /**
         * 4-待提交审核信息
         */
        public static final int waitSubmit = 4;

//        /**
//         * 5-待验证验证码，此状态不会入库
//         */
//        public static final int waitVerifyCode = 5;

    }

    // 下一状态
    public int statusNext() {
        if (this.getStatus().intValue() == KeyStatus.waitSubmit && ZKStringUtils.isEmpty(this.getParentId())) {
            // 平台公司，直接平台审核
            return KeyStatus.auditPlatformIng;
        }
        else if (this.getStatus().intValue() == KeyStatus.waitSubmit) {
            return KeyStatus.auditCompanyIng;
        }
        else if (this.getStatus().intValue() == KeyStatus.auditCompanyIng) {
            return KeyStatus.auditPlatformIng;
        }
        else if (this.getStatus().intValue() == KeyStatus.auditPlatformIng) {
            return KeyStatus.normal;
        }
        log.error("[>_<:20240707-2339-001] zk.sys.010017=公司非审核状态，请联系管理员；companyCode:{}", this.getCode());
        throw ZKBusinessException.as("zk.sys.010017");
    }

    // 前一状态
    public int statusPrev() {
        if (this.getStatus().intValue() == KeyStatus.auditPlatformIng && ZKStringUtils.isEmpty(this.getParentId())) {
            // 平台公司，直接回到待提交
            return KeyStatus.waitSubmit;
        }
        else if (this.getStatus().intValue() == KeyStatus.auditPlatformIng) {
            return KeyStatus.auditCompanyIng;
        }
        else if (this.getStatus().intValue() == KeyStatus.auditCompanyIng) {
            return KeyStatus.waitSubmit;
        }
        log.error("[>_<:20240707-2339-001] zk.sys.010017=公司非审核状态，请联系管理员；companyCode:{}", this.getCode());
        throw ZKBusinessException.as("zk.sys.010017");
    }

    /**
     * 公司状态；0-正常；1-禁用；2-上级公司审核中；3-平台审核中；4-待提交；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.Insert.class })
    @Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_status", isInsert = true, javaType = Integer.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Integer status;

    /**
     * 集团代码; 一级公司唯一
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_group_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String groupCode;

    /**
     * 公司代码；全表唯一
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String code;

    /**
     * 公司手机号
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.all_phoneNum, message = "{zk.core.data.validation.phone.num.all}")
    @ZKColumn(name = "c_phone_num", isInsert = true, javaType = String.class)
    String phoneNum;

    /**
     * 公司邮箱
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.mail, message = "{zk.core.data.validation.mail}")
    @ZKColumn(name = "c_mail", isInsert = true, javaType = String.class)
    String mail;

    /**
     * 公司传真号
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_fax_num", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
    String faxNum;

    /**
     * 公司固定电话号
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_tel_num", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
    String telNum;

    /**
     * 公司地址
     */
    @ZKColumn(name = "c_address", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true))
    ZKJson address;

    /**
     * 公司名称
     */
    @NotNull(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.Audit.class })
    @NotEmpty(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.Audit.class })
    @ZKColumn(name = "c_name", isInsert = true, javaType = ZKJson.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    ZKJson name;

    /**
     * 公司简介
     */
    @NotNull(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.Audit.class })
    @NotEmpty(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.Audit.class })
    @ZKColumn(name = "c_short_desc", isInsert = true, javaType = ZKJson.class)
    ZKJson shortDesc;

    /**
     * 公司 logo 正常公司不能为空；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.AuditTwo.class })
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_logo", isInsert = false, javaType = String.class)
    String logo;

    /**
     * 公司法人，正常公司不能为空；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.Audit.class })
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}", groups = {
            ZKValidationGroup.Audit.class })
    @ZKColumn(name = "c_legal_person", isInsert = false, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String legalPerson;

    /**
     * 公司法人证件类型; [字典项ID-cert_type]；正常公司不能为空；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.Audit.class })
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_legal_cert_type", isInsert = false, javaType = String.class)
    String legalCertType;

    /**
     * 公司法人证件号，正常公司不能为空；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.Audit.class })
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_legal_cert_num", isInsert = false, javaType = String.class)
    String legalCertNum;

    /**
     * 公司法人证件照片; 2张照片；存放文件保存UUID，使用英文逗号分隔；正常公司不能为空；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.AuditTwo.class })
    @Length(min = 0, max = 1024, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_legal_cert_photo", isInsert = false, javaType = String.class)
    String legalCertPhoto;

    /**
     * 公司注册日期，需要与证件日期一致；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.Audit.class })
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd, timezone = timezone)
    @ZKColumn(name = "c_register_date", isInsert = false, javaType = Date.class, formats = "%Y-%m-%d %H:%i:%S")
    Date registerDate;

    /**
     * 公司证件类型[字典项ID-company_cert_type]；与公司证件号唯一；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.Audit.class })
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_company_cert_type", isInsert = false, javaType = String.class)
    String companyCertType;

    /**
     * 公司证件号，类型为营业执照时，对应统一信用社会编码；与公司证件类型唯一；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.Audit.class })
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_company_cert_num", isInsert = false, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String companyCertNum;

    /**
     * 公司证件照片；存放文件UUID；1张照片；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.AuditTwo.class })
    @Length(min = 0, max = 1024, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_company_cert_photo", isInsert = false, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String companyCertPhoto;

    /**
     * 公司来源代码；与来源ID标识唯一；
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_source_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String sourceCode;

    /**
     * 来源ID标识，与来源代码唯一
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_source_id", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String sourceId;

    public ZKSysOrgCompany() {
        super();
    }

    public ZKSysOrgCompany(String pkId) {
        super(pkId);
    }

    /**
     * @return groupCode sa
     */
    public String getGroupCode() {
        return groupCode;
    }

    /**
     * @param groupCode
     *            the groupCode to set
     */
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    /**
     * @return code sa
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return status sa
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return phoneNum sa
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * @param phoneNum
     *            the phoneNum to set
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    /**
     * @return mail sa
     */
    public String getMail() {
        return mail;
    }

    /**
     * @param mail
     *            the mail to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * @return faxNum sa
     */
    public String getFaxNum() {
        return faxNum;
    }

    /**
     * @param faxNum
     *            the faxNum to set
     */
    public void setFaxNum(String faxNum) {
        this.faxNum = faxNum;
    }

    /**
     * @return telNum sa
     */
    public String getTelNum() {
        return telNum;
    }

    /**
     * @param telNum
     *            the telNum to set
     */
    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    /**
     * @return address sa
     */
    public ZKJson getAddress() {
        return address;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(ZKJson address) {
        this.address = address;
    }

    /**
     * @return name sa
     */
    public ZKJson getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(ZKJson name) {
        this.name = name;
    }

    /**
     * @return shortDesc sa
     */
    public ZKJson getShortDesc() {
        return shortDesc;
    }

    /**
     * @param shortDesc
     *            the shortDesc to set
     */
    public void setShortDesc(ZKJson shortDesc) {
        this.shortDesc = shortDesc;
    }

    /**
     * @return logo sa
     */
    public String getLogo() {
        return logo;
    }

    /**
     * @param logo
     *            the logo to set
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * @return legalPerson sa
     */
    public String getLegalPerson() {
        return legalPerson;
    }

    /**
     * @param legalPerson
     *            the legalPerson to set
     */
    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    /**
     * @return legalCertType sa
     */
    public String getLegalCertType() {
        return legalCertType;
    }

    /**
     * @param legalCertType
     *            the legalCertType to set
     */
    public void setLegalCertType(String legalCertType) {
        this.legalCertType = legalCertType;
    }

    /**
     * @return legalCertNum sa
     */
    public String getLegalCertNum() {
        return legalCertNum;
    }

    /**
     * @param legalCertNum
     *            the legalCertNum to set
     */
    public void setLegalCertNum(String legalCertNum) {
        this.legalCertNum = legalCertNum;
    }

    /**
     * @return legalCertPhoto sa
     */
    public String getLegalCertPhoto() {
        return legalCertPhoto;
    }

    /**
     * @param legalCertPhoto
     *            the legalCertPhoto to set
     */
    public void setLegalCertPhoto(String legalCertPhoto) {
        this.legalCertPhoto = legalCertPhoto;
    }

    /**
     * @return registerDate sa
     */
    public Date getRegisterDate() {
        return registerDate;
    }

    /**
     * @param registerDate
     *            the registerDate to set
     */
    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    /**
     * @return companyCertType sa
     */
    public String getCompanyCertType() {
        return companyCertType;
    }

    /**
     * @param companyCertType
     *            the companyCertType to set
     */
    public void setCompanyCertType(String companyCertType) {
        this.companyCertType = companyCertType;
    }

    /**
     * @return companyCertNum sa
     */
    public String getCompanyCertNum() {
        return companyCertNum;
    }

    /**
     * @param companyCertNum
     *            the companyCertNum to set
     */
    public void setCompanyCertNum(String companyCertNum) {
        this.companyCertNum = companyCertNum;
    }

    /**
     * @return companyCertPhoto sa
     */
    public String getCompanyCertPhoto() {
        return companyCertPhoto;
    }

    /**
     * @param companyCertPhoto
     *            the companyCertPhoto to set
     */
    public void setCompanyCertPhoto(String companyCertPhoto) {
        this.companyCertPhoto = companyCertPhoto;
    }

    /**
     * @return sourceCode sa
     */
    public String getSourceCode() {
        return ZKStringUtils.isEmpty(sourceCode) ? null : sourceCode;
    }

    /**
     * @param sourceCode
     *            the sourceCode to set
     */
    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    /**
     * @return sourceId sa
     */
    public String getSourceId() {
        return ZKStringUtils.isEmpty(sourceId) ? null : sourceId;
    }

    /**
     * @param sourceId
     *            the sourceId to set
     */
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * 根据主键类型，重写主键生成；
     */
    @Override
	@JsonIgnore
	public String genId() {
        return ZKIdUtils.genLongStringId();
    }

    // 查询辅助字段
    @Transient
    @JsonIgnore
    @XmlTransient
    String searchValue;

    /**
     * @return searchValue sa
     */
    public String getSearchValue() {
        return searchValue;
    }

    /**
     * @param searchValue
     *            the searchValue to set
     */
    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    // 取 where 条件；实体定义可以定制；在 生成的 sql；注意：末尾加空格
    @Override
    @Transient
    @JsonIgnore
    @XmlTransient
    public ZKDBQueryWhere getZKDbWhere(ZKSqlConvert sqlConvert, ZKDBMapInfo mapInfo) {
        ZKDBQueryWhere where = super.getZKDbWhereTree(sqlConvert, mapInfo);
        // 制作一个根据名称和代码同时查询的 查询条件，用过度 java 属性 searchValue 为传参数值
        ZKDBQueryWhere sWhere = ZKDBQueryWhere.asOr("(", ")",
                ZKDBQueryCol.as(ZKDBOptComparison.LIKE, "c_name", "searchValue", String.class, null, false),
                ZKDBQueryCol.as(ZKDBOptComparison.LIKE, "c_code", "searchValue", String.class, null, false));

        where.put(ZKDBQueryScript.asIf(sWhere, 0, "searchValue", String.class));
        return where;
    }

}

