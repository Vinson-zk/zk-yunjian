/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.wechat.officialAccounts.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.ZKCoreConstants.ValidationRegexp;
import com.zk.core.utils.ZKIdUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * 用户上报地理位置
 * 
 * @author
 * @version
 */
@ZKTable(name = "t_wx_official_accounts_user_gps", alias = "officialAccountsUserGps", orderBy = " c_create_date ASC ")
public class ZKOfficialAccountsUserGps extends ZKBaseEntity<String, ZKOfficialAccountsUserGps> {
	
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
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKOfficialAccountsUserGps());
        }
        return sqlHelper;
    }
    
    private static final long serialVersionUID = 1L;
	
	/**
	 * 目标授权用户主键
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_third_party_auth_account_user_pk_id", isInsert = true, javaType = String.class)
	String thirdPartyAuthAccountUserPkId;	
	/**
	 * 数据域平台标识；第三方数据绑定标识
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_data_space_platform", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String dataSpacePlatform;	
	/**
	 * 数据域分组标识；第三方数据绑定标识
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_data_space_group", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String dataSpaceGroup;	
	/**
	 * 数据域拥有标识；第三方数据绑定标识
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_data_space_owner", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String dataSpaceOwner;	
	/**
	 * 微信第三方平台 Appid
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_third_party_appid", isInsert = true, javaType = String.class)
	String wxThirdPartyAppid;	
	/**
	 * 微信第三方平台，目标授权方账号
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_authorizer_appid", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String wxAuthorizerAppid;	
	/**
	 * 用户的标识，对当前公众号唯一: openid
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_openid", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String wxOpenid;	
	/**
	 * 地理位置纬度: Latitude
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_latitude", isInsert = true, javaType = String.class)
	String wxLatitude;	
	/**
	 * 地理位置经度: Longitude
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_longitude", isInsert = true, javaType = String.class)
	String wxLongitude;	
	/**
	 * 地理位置精度: Precision
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_precision", isInsert = true, javaType = String.class)
	String wxPrecision;	
	/**
	 * 上报时间: CreateTime
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_wx_create_time", isInsert = true, javaType = Long.class)
	Long wxCreateTime;	
	
    /**
     * 集团代码
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_group_code", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
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

	public ZKOfficialAccountsUserGps() {
		super();
	}

	public ZKOfficialAccountsUserGps(String pkId){
		super(pkId);
	}
	
	/**
	 * 目标授权用户主键	
	 */	
	public String getThirdPartyAuthAccountUserPkId() {
		return thirdPartyAuthAccountUserPkId;
	}
	
	/**
	 * 目标授权用户主键
	 */	
	public void setThirdPartyAuthAccountUserPkId(String thirdPartyAuthAccountUserPkId) {
		this.thirdPartyAuthAccountUserPkId = thirdPartyAuthAccountUserPkId;
	}
	/**
	 * 数据域平台标识；第三方数据绑定标识	
	 */	
	public String getDataSpacePlatform() {
		return dataSpacePlatform;
	}
	
	/**
	 * 数据域平台标识；第三方数据绑定标识
	 */	
	public void setDataSpacePlatform(String dataSpacePlatform) {
		this.dataSpacePlatform = dataSpacePlatform;
	}
	/**
	 * 数据域分组标识；第三方数据绑定标识	
	 */	
	public String getDataSpaceGroup() {
		return dataSpaceGroup;
	}
	
	/**
	 * 数据域分组标识；第三方数据绑定标识
	 */	
	public void setDataSpaceGroup(String dataSpaceGroup) {
		this.dataSpaceGroup = dataSpaceGroup;
	}
	/**
	 * 数据域拥有标识；第三方数据绑定标识	
	 */	
	public String getDataSpaceOwner() {
		return dataSpaceOwner;
	}
	
	/**
	 * 数据域拥有标识；第三方数据绑定标识
	 */	
	public void setDataSpaceOwner(String dataSpaceOwner) {
		this.dataSpaceOwner = dataSpaceOwner;
	}
	/**
	 * 微信第三方平台 Appid	
	 */	
	public String getWxThirdPartyAppid() {
		return wxThirdPartyAppid;
	}
	
	/**
	 * 微信第三方平台 Appid
	 */	
	public void setWxThirdPartyAppid(String wxThirdPartyAppid) {
		this.wxThirdPartyAppid = wxThirdPartyAppid;
	}
	/**
	 * 微信第三方平台，目标授权方账号	
	 */	
	public String getWxAuthorizerAppid() {
		return wxAuthorizerAppid;
	}
	
	/**
	 * 微信第三方平台，目标授权方账号
	 */	
	public void setWxAuthorizerAppid(String wxAuthorizerAppid) {
		this.wxAuthorizerAppid = wxAuthorizerAppid;
	}
	/**
	 * 用户的标识，对当前公众号唯一: openid	
	 */	
	public String getWxOpenid() {
		return wxOpenid;
	}
	
	/**
	 * 用户的标识，对当前公众号唯一: openid
	 */	
	public void setWxOpenid(String wxOpenid) {
		this.wxOpenid = wxOpenid;
	}
	/**
	 * 地理位置纬度: Latitude	
	 */	
	public String getWxLatitude() {
		return wxLatitude;
	}
	
	/**
	 * 地理位置纬度: Latitude
	 */	
	public void setWxLatitude(String wxLatitude) {
		this.wxLatitude = wxLatitude;
	}
	/**
	 * 地理位置经度: Longitude	
	 */	
	public String getWxLongitude() {
		return wxLongitude;
	}
	
	/**
	 * 地理位置经度: Longitude
	 */	
	public void setWxLongitude(String wxLongitude) {
		this.wxLongitude = wxLongitude;
	}
	/**
	 * 地理位置精度: Precision	
	 */	
	public String getWxPrecision() {
		return wxPrecision;
	}
	
	/**
	 * 地理位置精度: Precision
	 */	
	public void setWxPrecision(String wxPrecision) {
		this.wxPrecision = wxPrecision;
	}
	/**
	 * 上报时间: CreateTime	
	 */	
	public Long getWxCreateTime() {
		return wxCreateTime;
	}
	
	/**
	 * 上报时间: CreateTime
	 */	
	public void setWxCreateTime(Long wxCreateTime) {
		this.wxCreateTime = wxCreateTime;
	}
	
	/**
	 * 根据主键类型，重写主键生成；
	 */
	@Override
	@JsonIgnore
	public String genId() {
        return ZKIdUtils.genLongStringId();
    }

    /**
     * 集团代码
     * 
     * @return groupCode sa
     */
    public String getGroupCode() {
        return groupCode;
    }

    /**
     * 集团代码
     * 
     * @param groupCode
     *            the groupCode to set
     */
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    /**
     * 公司ID
     * 
     * @return companyId sa
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * 公司ID
     * 
     * @param companyId
     *            the companyId to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * 公司代码
     * 
     * @return companyCode sa
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * 公司代码
     * 
     * @param companyCode
     *            the companyCode to set
     */
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }
	
}