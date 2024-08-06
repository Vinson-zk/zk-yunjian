/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.wechat.officialAccounts.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.ZKCoreConstants.ValidationRegexp;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKIdUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * 授权账号，公众号或小程序
 * 
 * @author
 * @version
 */
@ZKTable(name = "t_wx_official_accounts", alias = "officialsAccount", orderBy = " c_create_date ASC ")
public class ZKOfficialAccounts extends ZKBaseEntity<String, ZKOfficialAccounts> {
	
    /**
     * 授权方账号类型
     * 
     * 0-未知；1-公众号；2-小程序;
     */
    public static interface KeyAuthAccountType {

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

    /**
     * 添加方式；
     * 
     * 0-第三方平台授权；
     */
    public static interface KeyAddType {

        /**
         * 0-第三方平台授权；
         */
        public static final int thridPartyAuth = 0;

    }

	static ZKDBSqlHelper sqlHelper;

    @Override 
    public ZKDBSqlHelper getSqlHelper() {
        return sqlHelper();
    }
    
    public static ZKDBSqlHelper sqlHelper() {
        if(sqlHelper == null) {
			sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKOfficialAccounts());
        }
        return sqlHelper;
    }
    
    private static final long serialVersionUID = 1L;
	
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

    /**
     * 添加方式；0-第三方平台授权；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 9, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_add_type", isInsert = true, javaType = Integer.class)
    Integer addType;

    /**
     * 微信第三方平台 Appid
     */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_third_party_appid", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String wxThirdPartyAppid;	
	/**
	 * 微信第三方平台，目标授权方账号
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_wx_account_appid", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    String wxAccountAppid;

	/**
	 * 微信第三方平台，目标授权方账号类型；0-未知；1-公众号；2-小程序
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_wx_auth_account_type", isInsert = true, javaType = Long.class)
    Integer wxAuthAccountType;

	/**
	 * 微信第三方平台，目标授权方账号的刷新令牌，获取授权信息时得到
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_authorizer_refresh_token", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
	String wxAuthorizerRefreshToken;	
	/**
	 * 目标授权方账号。授权的权限集
	 */
	@ZKColumn(name = "c_wx_func_info", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true))
    ZKJson wxFuncInfo;

	/**
	 * 昵称: nick_name
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_nick_name", isInsert = true, javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
	String wxNickName;	
	/**
	 * 头像: head_img
	 */
    @Length(min = 0, max = 512, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_head_img", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
	String wxHeadImg;	
	/**
	 * 授权方在微信平台的类型: service_type_info
	 */
	@ZKColumn(name = "c_wx_service_type_info", isInsert = true, javaType = Long.class, update = @ZKUpdate(true))
    ZKJson wxServiceTypeInfo;

	/**
	 * 认证类型:  verify_type_info
	 */
	@ZKColumn(name = "c_wx_verify_type_info", isInsert = true, javaType = Long.class, update = @ZKUpdate(true))
    ZKJson wxVerifyTypeInfo;

	/**
	 * 原始 ID：user_name	
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 128, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_user_name", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
	String wxUserName;	
	/**
	 * 主体名称：principal_name
	 */
	@Length(min = 0, max = 128, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_principal_name", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
	String wxPrincipalName;	
	/**
	 * 用以了解功能的开通状况：business_info
	 */
	@ZKColumn(name = "c_wx_business_info", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
    ZKJson wxBusinessInfo;

	/**
	 * 二维码图片的 URL，开发者最好自行也进行保存：qrcode_url
	 */
    @Length(min = 0, max = 512, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_qrcode_url", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
	String wxQrcodeUrl;	
	/**
	 * 公众号所设置的微信号，可能为空：alias
	 */
	@Length(min = 0, max = 128, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_alias", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
	String wxAlias;	
	/**
	 * 小程序帐号介绍，可能为空：signature
	 */
	@Length(min = 0, max = 128, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_signature", isInsert = true, javaType = String.class, update = @ZKUpdate(true))
	String wxSignature;	
	
	public ZKOfficialAccounts() {
		super();
	}

	public ZKOfficialAccounts(String pkId){
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

    /**
     * @return addType sa
     */
    public Integer getAddType() {
        return addType;
    }

    /**
     * @param addType
     *            the addType to set
     */
    public void setAddType(Integer addType) {
        this.addType = addType;
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
     * 微信，目标授权方账号 ，公众号或小程序 appid
     */	
    public String getWxAccountAppid() {
        return wxAccountAppid;
	}
	
	/**
     * 微信，目标授权方账号 ，公众号或小程序 appid
     */	
    public void setWxAccountAppid(String wxAccountAppid) {
        this.wxAccountAppid = wxAccountAppid;
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
