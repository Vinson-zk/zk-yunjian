/**
 * Copyright &copy; All rights reserved.
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
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.ZKCoreConstants.ValidationRegexp;
import com.zk.core.commons.ZKValidationGroup;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKIdUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;
import com.zk.framework.security.userdetails.ZKUser;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * 用户表
 * 
 * @author
 * @version
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_sys_org_user", alias = "sysOrgUser", orderBy = " c_create_date ASC ")
public class ZKSysOrgUser extends ZKBaseEntity<String, ZKSysOrgUser> implements ZKUser<String> {

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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKSysOrgUser());
        }
        return sqlHelper;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 用户状态；0-正常; 1-禁用; 2-离职; 3-离职中;
     */
    public static interface KeyStatus {
        /**
         * 0-正常
         */
        public static final int normal = 0;

        /**
         * 1-撤销
         */
        public static final int disabled = 1;

        /**
         * 2-离职
         */
        public static final int exit = 2;

        /**
         * 3-离职中
         */
        public static final int exiting = 3;

        /**
         * 4-待完善
         */
        public static final int waitPerfect = 4;
    }

    /**
     * 集团代码
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_group_code", isInsert = true, javaType = String.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String groupCode;
    /**
     * 公司ID
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_company_id", isInsert = true, javaType = String.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String companyId;
    /**
     * 公司代码
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_company_code", isInsert = true, javaType = String.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String companyCode;
    /**
     * 用户类型ID
     */
    @JsonInclude(value = Include.NON_EMPTY)
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_user_type_id", isInsert = true, javaType = String.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String userTypeId;
    /**
     * 用户类型代码
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_user_type_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String userTypeCode;
    /**
     * 用户职级ID
     */
    @JsonInclude(value = Include.NON_EMPTY)
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_rank_id", isInsert = true, javaType = String.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String rankId;
    /**
     * 用户职级代码
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_rank_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String rankCode;
    /**
     * 用户部门ID
     */
    @JsonInclude(value = Include.NON_EMPTY)
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_dept_id", isInsert = true, javaType = String.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String deptId;
    /**
     * 用户部门代码
     */
    @JsonInclude(value = Include.NON_EMPTY)
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_dept_code", isInsert = true, javaType = String.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String deptCode;

    /**
     * 用户账号；公司下唯一；以字母开头
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.account, message = "{zk.core.data.validation.account}", groups = {
            ZKValidationGroup.class })
    @ZKColumn(name = "c_account", isInsert = true, javaType = String.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String account;
    
    /**
     * 用户密码，用户密码不能为空，但保存时，不处理密码，由事务中通过修改密码方法来设置密码
     */
//    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_password", isInsert = false, javaType = String.class)
    String password;

    /**
     * 用户状态; 0-正常; 1-禁用; 2-离职; 3-离职中;
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 9, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_status", isInsert = true, javaType = Long.class, // update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Integer status;
    /**
     * 姓
     */
    @NotNull(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.CustomModel.class })
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_family_name", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String familyName;
    /**
     * 名
     */
    @NotNull(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.CustomModel.class })
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_second_name", isInsert = true, javaType = String.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String secondName;
    /**
     * 昵称
     */
    @NotNull(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.CustomModel.class })
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_nickname", isInsert = true, javaType = String.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String nickname;
    /**
     * 生日
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd, timezone = timezone)
    @ZKColumn(name = "c_birthday", isInsert = true, javaType = Date.class, formats = "%Y-%m-%d %H:%i:%S",
        update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.GT))
    Date birthday;
    /**
     * 性别；unknown-保密；male-男；female-女；third_gender-第三性别; 字典项
     */
    @NotNull(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.CustomModel.class })
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_sex", isInsert = true, javaType = String.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String sex;
    /**
     * 地址
     */
    @ZKColumn(name = "c_address", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true),
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    ZKJson address;
    /**
     * 固定电话
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_tel_num", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
    String telNum;

    /**
     * 手机；公司下唯一；
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @NotNull(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.CustomModel.class })
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.phoneNum, message = "{zk.core.data.validation.phone.num}", groups = {
            ZKValidationGroup.CustomModel.class })
    @ZKColumn(name = "c_phone_num", isInsert = true, javaType = String.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String phoneNum;

    /**
     * 邮箱；公司下唯一；
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @NotNull(message = "{zk.core.data.validation.notNull}", groups = { ZKValidationGroup.CustomModel.class })
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.mail, message = "{zk.core.data.validation.mail}", groups = {
            ZKValidationGroup.CustomModel.class })
    @ZKColumn(name = "c_mail", isInsert = true, javaType = String.class)
    String mail;
    /**
     * 头像
     */
    @Length(min = 0, max = 512, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_head_photo", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
    String headPhoto;
    /**
     * 将你原图
     */
    @Length(min = 0, max = 512, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_head_photo_original", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
    String headPhotoOriginal;
    /**
     * QQ
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_qq", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
    String qq;
    /**
     * 微信
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_wechat", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
    String wechat;
    /**
     * 用户工号
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_job_num", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
    String jobNum;
    /**
     * 入职日期
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd, timezone = timezone)
    @ZKColumn(name = "c_join_date", isInsert = true, javaType = Date.class, update = @ZKUpdate(true),
        formats = "%Y-%m-%d %H:%i:%S")
    Date joinDate;
    /**
     * 离职日期
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd, timezone = timezone)
    @ZKColumn(name = "c_leave_date", isInsert = true, javaType = Date.class, update = @ZKUpdate(true),
        formats = "%Y-%m-%d %H:%i:%S")
    Date leaveDate;
    /**
     * 申请离职日期
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd, timezone = timezone)
    @ZKColumn(name = "c_apply_leave_date", isInsert = true, javaType = Date.class, formats = "%Y-%m-%d %H:%i:%S",
        update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    Date applyLeaveDate;
    /**
     * 来源代码，与来源ID标识唯一
     */
    @Length(min = 0, max = 11, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_source_code", isInsert = true, javaType = String.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String sourceCode;
    /**
     * 来源ID标识，与来源代码唯一
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @Length(min = 0, max = 11, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_source_id", isInsert = true, javaType = String.class,
        query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    String sourceId;

    public ZKSysOrgUser() {
        super();
    }

    public ZKSysOrgUser(String pkId) {
        super(pkId);
    }

    @XmlTransient
    @Transient
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    ZKSysOrgCompany company;

    /**
     * 集团代码
     */
    @Override
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
    @Override
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
    @Override
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
     * 用户类型ID
     */
    @Override
    public String getUserTypeId() {
        return userTypeId;
    }

    /**
     * 用户类型ID
     */
    public void setUserTypeId(String userTypeId) {
        this.userTypeId = userTypeId;
    }

    /**
     * 用户类型代码
     */
    @Override
    public String getUserTypeCode() {
        return userTypeCode;
    }

    /**
     * 用户类型代码
     */
    public void setUserTypeCode(String userTypeCode) {
        this.userTypeCode = userTypeCode;
    }

    /**
     * 用户职级ID
     */
    @Override
    public String getRankId() {
        return rankId;
    }

    /**
     * 用户职级ID
     */
    public void setRankId(String rankId) {
        this.rankId = rankId;
    }

    /**
     * 用户职级代码
     */
    @Override
    public String getRankCode() {
        return rankCode;
    }

    /**
     * 用户职级代码
     */
    public void setRankCode(String rankCode) {
        this.rankCode = rankCode;
    }

    /**
     * 用户部门ID
     */
    @Override
    public String getDeptId() {
        return deptId;
    }

    /**
     * 用户部门ID
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**
     * 用户部门代码
     */
    @Override
    public String getDeptCode() {
        return deptCode;
    }

    /**
     * 用户部门代码
     */
    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    /**
     * 用户账号
     */
    @Override
    public String getAccount() {
        return account;
    }

    /**
     * 用户账号
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 用户密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 用户密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 用户状态; 0-正常; 1-禁用; 2-离职; 3-离职中;
     */
    @Override
    public Integer getStatus() {
        return status;
    }

    /**
     * 用户状态; 0-正常; 1-禁用; 2-离职; 3-离职中;
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return familyName sa
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * @param familyName
     *            the familyName to set
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     * @return secondName sa
     */
    public String getSecondName() {
        return secondName;
    }


    /**
     * @param secondName
     *            the secondName to set
     */
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    /**
     * 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 生日
     */
    @Override
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss, timezone = timezone)
    public Date getBirthday() {
        return birthday;
    }

    /**
     * 生日
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * 性别；unknown-保密；male-男；female-女；third_gender-第三性别; 字典项
     */
    @Override
    public String getSex() {
        return sex;
    }

    /**
     * 性别；unknown-保密；male-男；female-女；third_gender-第三性别; 字典项
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 地址
     */
    public ZKJson getAddress() {
        return address;
    }

    /**
     * 地址
     */
    public void setAddress(ZKJson address) {
        this.address = address;
    }

    /**
     * 固定电话
     */
    public String getTelNum() {
        return telNum;
    }

    /**
     * 固定电话
     */
    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }

    /**
     * 手机
     */
    @Override
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * 手机
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    /**
     * 邮箱
     */
    @Override
    public String getMail() {
        return mail;
    }

    /**
     * 邮箱
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * 头像
     */
    public String getHeadPhoto() {
        return headPhoto;
    }

    /**
     * 头像
     */
    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }

    /**
     * 将你原图
     */
    public String getHeadPhotoOriginal() {
        return headPhotoOriginal;
    }

    /**
     * 将你原图
     */
    public void setHeadPhotoOriginal(String headPhotoOriginal) {
        this.headPhotoOriginal = headPhotoOriginal;
    }

    /**
     * QQ
     */
    public String getQq() {
        return qq;
    }

    /**
     * QQ
     */
    public void setQq(String qq) {
        this.qq = qq;
    }

    /**
     * 微信
     */
    public String getWechat() {
        return wechat;
    }

    /**
     * 微信
     */
    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    /**
     * 用户工号
     */
    @Override
    public String getJobNum() {
        return jobNum;
    }

    /**
     * 用户工号
     */
    public void setJobNum(String jobNum) {
        this.jobNum = jobNum;
    }

    /**
     * 入职日期
     */
    @Override
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss, timezone = timezone)
    public Date getJoinDate() {
        return joinDate;
    }

    /**
     * 入职日期
     */
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    /**
     * 离职日期
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss, timezone = timezone)
    public Date getLeaveDate() {
        return leaveDate;
    }

    /**
     * 离职日期
     */
    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    /**
     * 申请离职日期
     */
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss, timezone = timezone)
    public Date getApplyLeaveDate() {
        return applyLeaveDate;
    }

    /**
     * @return company sa
     */
    public ZKSysOrgCompany getCompany() {
        return company;
    }

    /**
     * @param company
     *            the company to set
     */
    public void setCompany(ZKSysOrgCompany company) {
        this.company = company;
    }

    /**
     * 申请离职日期
     */
    public void setApplyLeaveDate(Date applyLeaveDate) {
        this.applyLeaveDate = applyLeaveDate;
    }

    /**
     * 来源代码，与来源ID标识唯一
     */
    public String getSourceCode() {
        return sourceCode;
    }

    /**
     * 来源代码，与来源ID标识唯一
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
	@JsonIgnore
	public String genId() {
        return ZKIdUtils.genLongStringId();
    }

}
