/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.wechat.thirdParty.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKIdUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKTable;
import com.zk.db.commons.ZKDBQueryType;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.commons.ZKSqlProvider;

/**
 * 微信开放平台中第三方平台的目标授权账号
 * @author 
 * @version 
 */
@ZKTable(name = "t_wx_third_party_auth_account", alias = "thirdPartyAuthAccount", orderBy = " c_create_date ASC ")
public class ZKThirdPartyAuthAccount extends ZKBaseEntity<String, ZKThirdPartyAuthAccount> {
	
	static ZKSqlProvider sqlProvider;
	
    /**
     * 授权方账号类型
     * 
     * 0-未知；1-公众号；2-小程序;
     */
    public static interface AuthAccountType {

        /**
         * 0-未知
         */
        public static final int unknown = 0;

        /**
         * 1-公众号
         */
        public static final int official = 1;

        /**
         * 2-小程序
         */
        public static final int miniProgram = 2;
    }

    @Override 
    public ZKSqlProvider getSqlProvider() {
        return initSqlProvider();
    }
    
    public static ZKSqlProvider initSqlProvider() {
        if(sqlProvider == null) {
            sqlProvider = new ZKSqlProvider(new ZKSqlConvertDelegating(), new ZKThirdPartyAuthAccount());
        }
        return sqlProvider;
    }
    
    private static final long serialVersionUID = 1L;
	
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
	@ZKColumn(name = "c_wx_third_party_appid", isInsert = true, isUpdate = false, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String wxThirdPartyAppid;	
	/**
	 * 微信第三方平台，目标授权方账号
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_authorizer_appid", isInsert = true, isUpdate = false, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String wxAuthorizerAppid;	
	/**
	 * 微信第三方平台，目标授权方账号类型；0-未知；1-公众号；2-小程序
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_wx_auth_account_type", isInsert = true, isUpdate = false, javaType = Long.class, isQuery = false)
    Integer wxAuthAccountType;

	/**
	 * 微信第三方平台，目标授权方账号的刷新令牌，获取授权信息时得到
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_authorizer_refresh_token", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxAuthorizerRefreshToken;	
	/**
	 * 目标授权方账号。授权的权限集
	 */
	@ZKColumn(name = "c_wx_func_info", isInsert = true, isUpdate = true, javaType = ZKJson.class, isQuery = false)
    ZKJson wxFuncInfo;

	/**
	 * 昵称: nick_name
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_nick_name", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String wxNickName;	
	/**
	 * 头像: head_img
	 */
	@Length(min = 0, max = 128, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_head_img", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxHeadImg;	
	/**
	 * 授权方在微信平台的类型: service_type_info
	 */
	@ZKColumn(name = "c_wx_service_type_info", isInsert = true, isUpdate = true, javaType = Long.class, isQuery = false)
    ZKJson wxServiceTypeInfo;

	/**
	 * 认证类型:  verify_type_info
	 */
	@ZKColumn(name = "c_wx_verify_type_info", isInsert = true, isUpdate = true, javaType = Long.class, isQuery = false)
    ZKJson wxVerifyTypeInfo;

	/**
	 * 原始 ID：user_name	
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 128, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_user_name", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxUserName;	
	/**
	 * 主体名称：principal_name
	 */
	@Length(min = 0, max = 128, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_principal_name", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxPrincipalName;	
	/**
	 * 用以了解功能的开通状况：business_info
	 */
	@ZKColumn(name = "c_wx_business_info", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
    ZKJson wxBusinessInfo;

	/**
	 * 二维码图片的 URL，开发者最好自行也进行保存：qrcode_url
	 */
	@Length(min = 0, max = 128, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_qrcode_url", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxQrcodeUrl;	
	/**
	 * 公众号所设置的微信号，可能为空：alias
	 */
	@Length(min = 0, max = 128, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_alias", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxAlias;	
	/**
	 * 小程序帐号介绍，可能为空：signature
	 */
	@Length(min = 0, max = 128, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_signature", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxSignature;	
	
	public ZKThirdPartyAuthAccount() {
		super();
	}

	public ZKThirdPartyAuthAccount(String pkId){
		super(pkId);
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
	 * 微信第三方平台，目标授权方账号类型；0-未知；1-公众号；2-小程序	
	 */	
    public Integer getWxAuthAccountType() {
		return wxAuthAccountType;
	}
	
	/**
	 * 微信第三方平台，目标授权方账号类型；0-未知；1-公众号；2-小程序
	 */	
    public void setWxAuthAccountType(Integer wxAuthAccountType) {
		this.wxAuthAccountType = wxAuthAccountType;
	}
	/**
	 * 微信第三方平台，目标授权方账号的刷新令牌，获取授权信息时得到	
	 */	
	public String getWxAuthorizerRefreshToken() {
		return wxAuthorizerRefreshToken;
	}
	
	/**
	 * 微信第三方平台，目标授权方账号的刷新令牌，获取授权信息时得到
	 */	
	public void setWxAuthorizerRefreshToken(String wxAuthorizerRefreshToken) {
		this.wxAuthorizerRefreshToken = wxAuthorizerRefreshToken;
	}
	/**
	 * 目标授权方账号。授权的权限集	
	 */	
    public ZKJson getWxFuncInfo() {
		return wxFuncInfo;
	}
	
	/**
	 * 目标授权方账号。授权的权限集
	 */	
    public void setWxFuncInfo(ZKJson wxFuncInfo) {
		this.wxFuncInfo = wxFuncInfo;
	}
	/**
	 * 昵称: nick_name	
	 */	
	public String getWxNickName() {
		return wxNickName;
	}
	
	/**
	 * 昵称: nick_name
	 */	
	public void setWxNickName(String wxNickName) {
		this.wxNickName = wxNickName;
	}
	/**
	 * 头像: head_img	
	 */	
	public String getWxHeadImg() {
		return wxHeadImg;
	}
	
	/**
	 * 头像: head_img
	 */	
	public void setWxHeadImg(String wxHeadImg) {
		this.wxHeadImg = wxHeadImg;
	}
	/**
	 * 授权方在微信平台的类型: service_type_info	
	 */	
    public ZKJson getWxServiceTypeInfo() {
		return wxServiceTypeInfo;
	}
	
	/**
	 * 授权方在微信平台的类型: service_type_info
	 */	
    public void setWxServiceTypeInfo(ZKJson wxServiceTypeInfo) {
		this.wxServiceTypeInfo = wxServiceTypeInfo;
	}
	/**
	 * 认证类型:  verify_type_info	
	 */	
    public ZKJson getWxVerifyTypeInfo() {
		return wxVerifyTypeInfo;
	}
	
	/**
	 * 认证类型:  verify_type_info
	 */	
    public void setWxVerifyTypeInfo(ZKJson wxVerifyTypeInfo) {
		this.wxVerifyTypeInfo = wxVerifyTypeInfo;
	}
	/**
	 * 原始 ID：user_name		
	 */	
	public String getWxUserName() {
		return wxUserName;
	}
	
	/**
	 * 原始 ID：user_name	
	 */	
	public void setWxUserName(String wxUserName) {
		this.wxUserName = wxUserName;
	}
	/**
	 * 主体名称：principal_name	
	 */	
	public String getWxPrincipalName() {
		return wxPrincipalName;
	}
	
	/**
	 * 主体名称：principal_name
	 */	
	public void setWxPrincipalName(String wxPrincipalName) {
		this.wxPrincipalName = wxPrincipalName;
	}
	/**
	 * 用以了解功能的开通状况：business_info	
	 */	
    public ZKJson getWxBusinessInfo() {
		return wxBusinessInfo;
	}
	
	/**
	 * 用以了解功能的开通状况：business_info
	 */	
    public void setWxBusinessInfo(ZKJson wxBusinessInfo) {
		this.wxBusinessInfo = wxBusinessInfo;
	}
	/**
	 * 二维码图片的 URL，开发者最好自行也进行保存：qrcode_url	
	 */	
	public String getWxQrcodeUrl() {
		return wxQrcodeUrl;
	}
	
	/**
	 * 二维码图片的 URL，开发者最好自行也进行保存：qrcode_url
	 */	
	public void setWxQrcodeUrl(String wxQrcodeUrl) {
		this.wxQrcodeUrl = wxQrcodeUrl;
	}
	/**
	 * 公众号所设置的微信号，可能为空：alias	
	 */	
	public String getWxAlias() {
		return wxAlias;
	}
	
	/**
	 * 公众号所设置的微信号，可能为空：alias
	 */	
	public void setWxAlias(String wxAlias) {
		this.wxAlias = wxAlias;
	}
	/**
	 * 小程序帐号介绍，可能为空：signature	
	 */	
	public String getWxSignature() {
		return wxSignature;
	}
	
	/**
	 * 小程序帐号介绍，可能为空：signature
	 */	
	public void setWxSignature(String wxSignature) {
		this.wxSignature = wxSignature;
	}
	
	/**
	 * 根据主键类型，重写主键生成；
	 */
	@Override
	protected String genId() {
        return ZKIdUtils.genLongStringId();
    }
	
}