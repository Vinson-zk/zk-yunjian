/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.entity.org;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zk.base.commons.ZKTreeSqlHelper;
import com.zk.base.entity.ZKBaseTreeEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKIdUtils;
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

/**
 * 公司表
 * 
 * @author
 * @version
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

    /**
     * 公司状态；0-正常；1-禁用；2-审核中；
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
         * 2-审核中
         */
        public static final int auditIng = 2;
    }

    /**
     * 集团代码
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_group_code", isInsert = true, javaType = String.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String groupCode;
    /**
     * 公司代码；全表唯一
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_code", isInsert = true, javaType = String.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String code;
    /**
     * 公司名称
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @NotEmpty(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_name", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    ZKJson name;
    /**
     * 公司 logo
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_logo", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
    String logo;
    /**
     * 公司 logo 原图
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_logo_original", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
    String logoOriginal;
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
     * 公司手机号
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_phone_num", isInsert = true, javaType = String.class)
    String phoneNum;
    /**
     * 公司邮箱
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_mail", isInsert = true, javaType = String.class)
    String mail;
    /**
     * 公司法人
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_legal_person", isInsert = true, javaType = String.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String legalPerson;

    /**
     * 公司法人证件类型; 字典项-字典项ID；
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_cert_type", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
    String certType;
    /**
     * 公司法人证件号
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_cert_num", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
    String certNum;
    /**
     * 公司状态；0-正常；1-禁用；2-审核中；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_status", isInsert = true, javaType = Integer.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Integer status;

    /**
     * 公司地址
     */
    @ZKColumn(name = "c_address", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true))
    ZKJson address;
    /**
     * 公司成立日期
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd, timezone = timezone)
    @ZKColumn(name = "c_found_date", isInsert = true, javaType = Date.class, formats = "%Y-%m-%d %H:%i:%S",
        update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.GT))
    Date foundDate;
    /**
     * 公司注册日期
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd, timezone = timezone)
    @ZKColumn(name = "c_register_date", isInsert = true, javaType = Date.class, update = @ZKUpdate(true),
        formats = "%Y-%m-%d %H:%i:%S")
    Date registerDate;
    /**
     * 公司注册地址
     */
    @ZKColumn(name = "c_register_address", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true))
    ZKJson registerAddress;
    /**
     * 公司注册机构
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_register_authority", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
    String registerAuthority;
    /**
     * 公司注册号
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_register_num", isInsert = true, javaType = String.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String registerNum;
    /**
     * 公司简介
     */
    @ZKColumn(name = "c_short_desc", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true))
    ZKJson shortDesc;
    /**
     * 公司来源代码；与来源ID标识唯一；
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_source_code", isInsert = true, javaType = String.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String sourceCode;
    /**
     * 来源ID标识，与来源代码唯一
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_source_id", isInsert = true, javaType = String.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String sourceId;

    public ZKSysOrgCompany() {
        super();
    }

    public ZKSysOrgCompany(String pkId) {
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
    public Integer getStatus() {
        return status;
    }

    /**
     * 公司状态；0-正常；1-禁用；2-审核中；
     */
    public void setStatus(Integer status) {
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
     * 公司简介
     */
    public ZKJson getShortDesc() {
        return shortDesc;
    }

    /**
     * 公司简介
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