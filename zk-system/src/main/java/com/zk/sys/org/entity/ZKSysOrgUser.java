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

import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.commons.ZKSqlProvider;

import com.zk.base.entity.ZKBaseEntity;

/**
 * 用户表
 * @author 
 * @version 
 */
@ZKTable(name = "t_sys_org_user", alias = "sysOrgUser", orderBy = " c_create_date ASC ")
public class ZKSysOrgUser extends ZKBaseEntity<String, ZKSysOrgUser> {
	
	static ZKSqlProvider sqlProvider;
	
    @Override 
    public ZKSqlProvider getSqlProvider() {
        return initSqlProvider();
    }
    
    public static ZKSqlProvider initSqlProvider() {
        if(sqlProvider == null) {
            sqlProvider = new ZKSqlProvider(new ZKSqlConvertDelegating(), new ZKSysOrgUser());
        }
        return sqlProvider;
    }
    
    private static final long serialVersionUID = 1L;
	
	/**
	 * 集团代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_group_code", isInsert = true, isUpdate = false, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String groupCode;	
	/**
	 * 公司ID 
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_company_id", isInsert = true, isUpdate = false, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	String companyId;	
	/**
	 * 公司代码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_company_code", isInsert = true, isUpdate = false, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	String companyCode;	
	/**
	 * 用户类型ID 
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_user_type_id", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	String userTypeId;	
	/**
	 * 用户类型代码
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_user_type_code", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	String userTypeCode;	
	/**
	 * 用户职级ID 
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_rank_id", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	String rankId;	
	/**
	 * 用户职级代码
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_rank_code", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	String rankCode;	
	/**
	 * 用户部门ID 
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_dept_id", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	String deptId;	
	/**
	 * 用户部门代码
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_dept_code", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	String deptCode;	
	/**
	 * 用户账号
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_account", isInsert = true, isUpdate = false, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String account;	
	/**
	 * 用户密码
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_password", isInsert = true, isUpdate = false, javaType = String.class, isQuery = false)
	String password;	
	/**
	 * 密码状态；0-系统密码；1-用户密码；
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_pwd_status", isInsert = true, isUpdate = false, javaType = Long.class, isQuery = false)
	Long pwdStatus;	
	/**
	 * 密码最后修改日期
	 */
	@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
	@JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd, timezone = timezone)
	@ZKColumn(name = "c_pwd_last_update_date", isInsert = true, isUpdate = false, javaType = Date.class, formats = "%Y-%m-%d %H:%i:%S", isQuery = false)
	Date pwdLastUpdateDate;	
	/**
	 * 用户状态; 0-正常; 1-禁用; 2-离职; 3-离职中; 
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_status", isInsert = true, isUpdate = true, javaType = Long.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	Long status;	
	/**
	 * 姓
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_family_name", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String familyName;	
	/**
	 * 名
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_second_name", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String secondName;	
	/**
	 * 昵称
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_nickname", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String nickname;	
	/**
	 * 生日
	 */
	@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
	@JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd, timezone = timezone)
	@ZKColumn(name = "c_birthday", isInsert = true, isUpdate = true, javaType = Date.class, formats = "%Y-%m-%d %H:%i:%S", isQuery = true, queryType = ZKDBQueryType.GT)
	Date birthday;	
	/**
	 * 性别；unknown-保密；male-男；female-女；third_gender-第三性别; 字典项
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_sex", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	String sex;	
	/**
	 * 地址
	 */
	@ZKColumn(name = "c_address", isInsert = true, isUpdate = true, javaType = ZKJson.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	ZKJson address;	
	/**
	 * 固定电话
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_tel_num", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String telNum;	
	/**
	 * 手机
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_phone_num", isInsert = true, isUpdate = false, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String phoneNum;	
	/**
	 * 邮箱
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_mail", isInsert = true, isUpdate = false, javaType = String.class, isQuery = false)
	String mail;	
	/**
	 * 头像
	 */
	@Length(min = 0, max = 512, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_head_photo", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String headPhoto;	
	/**
	 * 将你原图
	 */
	@Length(min = 0, max = 512, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_head_photo_original", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String headPhotoOriginal;	
	/**
	 * QQ
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_qq", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String qq;	
	/**
	 * 微信
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wechat", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wechat;	
	/**
	 * 用户工号
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_job_num", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String jobNum;	
	/**
	 * 入职日期
	 */
	@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
	@JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd, timezone = timezone)
	@ZKColumn(name = "c_join_date", isInsert = true, isUpdate = true, javaType = Date.class, formats = "%Y-%m-%d %H:%i:%S", isQuery = false)
	Date joinDate;	
	/**
	 * 离职日期
	 */
	@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
	@JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd, timezone = timezone)
	@ZKColumn(name = "c_leave_date", isInsert = true, isUpdate = true, javaType = Date.class, formats = "%Y-%m-%d %H:%i:%S", isQuery = false)
	Date leaveDate;	
	/**
	 * 申请离职日期
	 */
	@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
	@JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd, timezone = timezone)
	@ZKColumn(name = "c_apply_leave_date", isInsert = true, isUpdate = true, javaType = Date.class, formats = "%Y-%m-%d %H:%i:%S", isQuery = true, queryType = ZKDBQueryType.EQ)
	Date applyLeaveDate;	
	/**
	 * 来源代码，与来源ID标识唯一
	 */
	@Length(min = 0, max = 11, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_source_code", isInsert = true, isUpdate = false, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	String sourceCode;	
	/**
	 * 来源ID标识，与来源代码唯一
	 */
	@Length(min = 0, max = 11, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_source_id", isInsert = true, isUpdate = false, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.EQ)
	String sourceId;	
	
	public ZKSysOrgUser() {
		super();
	}

	public ZKSysOrgUser(String pkId){
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
	 * 用户类型ID 	
	 */	
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
	 * 密码状态；0-系统密码；1-用户密码；	
	 */	
	public Long getPwdStatus() {
		return pwdStatus;
	}
	
	/**
	 * 密码状态；0-系统密码；1-用户密码；
	 */	
	public void setPwdStatus(Long pwdStatus) {
		this.pwdStatus = pwdStatus;
	}
	/**
	 * 密码最后修改日期	
	 */	
	@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @JsonFormat(pattern = ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss, timezone = timezone)
	public Date getPwdLastUpdateDate() {
		return pwdLastUpdateDate;
	}
	
	/**
	 * 密码最后修改日期
	 */	
	public void setPwdLastUpdateDate(Date pwdLastUpdateDate) {
		this.pwdLastUpdateDate = pwdLastUpdateDate;
	}
	/**
	 * 用户状态; 0-正常; 1-禁用; 2-离职; 3-离职中; 	
	 */	
	public Long getStatus() {
		return status;
	}
	
	/**
	 * 用户状态; 0-正常; 1-禁用; 2-离职; 3-离职中; 
	 */	
	public void setStatus(Long status) {
		this.status = status;
	}
	/**
	 * 姓	
	 */	
	public String getFamilyName() {
		return familyName;
	}
	
	/**
	 * 姓
	 */	
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	/**
	 * 名	
	 */	
	public String getSecondName() {
		return secondName;
	}
	
	/**
	 * 名
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
	protected String genId() {
        return ZKIdUtils.genLongStringId();
    }
	
}