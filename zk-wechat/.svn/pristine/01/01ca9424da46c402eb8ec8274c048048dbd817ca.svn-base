/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.wechat.thirdParty.entity;

import java.lang.String;
import com.zk.core.utils.ZKIdUtils;
import com.zk.db.commons.ZKDBQueryType;

import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.commons.ZKSqlProvider;

import com.zk.base.entity.ZKBaseEntity;

/**
 * 微信开放平台中第三方平台目标授权账号的用户上报地理位置
 * @author 
 * @version 
 */
@ZKTable(name = "t_wx_third_party_auth_account_user_gps", alias = "thirdPartyAuthAccountUserGps", orderBy = " c_create_date ASC ")
public class ZKThirdPartyAuthAccountUserGps extends ZKBaseEntity<String, ZKThirdPartyAuthAccountUserGps> {
	
	static ZKSqlProvider sqlProvider;
	
    @Override 
    public ZKSqlProvider getSqlProvider() {
        return initSqlProvider();
    }
    
    public static ZKSqlProvider initSqlProvider() {
        if(sqlProvider == null) {
            sqlProvider = new ZKSqlProvider(new ZKSqlConvertDelegating(), new ZKThirdPartyAuthAccountUserGps());
        }
        return sqlProvider;
    }
    
    private static final long serialVersionUID = 1L;
	
	/**
	 * 目标授权用户主键
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_third_party_auth_account_user_pk_id", isInsert = true, isUpdate = false, javaType = String.class, isQuery = false)
	String thirdPartyAuthAccountUserPkId;	
	/**
	 * 数据域平台标识；第三方数据绑定标识
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_data_space_platform", isInsert = true, isUpdate = false, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String dataSpacePlatform;	
	/**
	 * 数据域分组标识；第三方数据绑定标识
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_data_space_group", isInsert = true, isUpdate = false, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String dataSpaceGroup;	
	/**
	 * 数据域拥有标识；第三方数据绑定标识
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_data_space_owner", isInsert = true, isUpdate = false, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String dataSpaceOwner;	
	/**
	 * 微信第三方平台 Appid
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_third_party_appid", isInsert = true, isUpdate = false, javaType = String.class, isQuery = false)
	String wxThirdPartyAppid;	
	/**
	 * 微信第三方平台，目标授权方账号
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_authorizer_appid", isInsert = true, isUpdate = false, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String wxAuthorizerAppid;	
	/**
	 * 用户的标识，对当前公众号唯一: openid
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_openid", isInsert = true, isUpdate = false, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String wxOpenid;	
	/**
	 * 地理位置纬度: Latitude
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_latitude", isInsert = true, isUpdate = false, javaType = String.class, isQuery = false)
	String wxLatitude;	
	/**
	 * 地理位置经度: Longitude
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_longitude", isInsert = true, isUpdate = false, javaType = String.class, isQuery = false)
	String wxLongitude;	
	/**
	 * 地理位置精度: Precision
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_precision", isInsert = true, isUpdate = false, javaType = String.class, isQuery = false)
	String wxPrecision;	
	/**
	 * 上报时间: CreateTime
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_wx_create_time", isInsert = true, isUpdate = false, javaType = Long.class, isQuery = false)
	Long wxCreateTime;	
	
	public ZKThirdPartyAuthAccountUserGps() {
		super();
	}

	public ZKThirdPartyAuthAccountUserGps(String pkId){
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
	protected String genId() {
        return ZKIdUtils.genLongStringId();
    }
	
}