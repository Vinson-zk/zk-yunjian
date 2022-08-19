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
 * 微信开放平台中第三方平台目标授权账号的用户
 * @author 
 * @version 
 */
@ZKTable(name = "t_wx_third_party_auth_account_user", alias = "thirdPartyAuthAccountUser", orderBy = " c_create_date ASC ")
public class ZKThirdPartyAuthAccountUser extends ZKBaseEntity<String, ZKThirdPartyAuthAccountUser> {
	
	static ZKSqlProvider sqlProvider;
	
    @Override 
    public ZKSqlProvider getSqlProvider() {
        return initSqlProvider();
    }
    
    public static ZKSqlProvider initSqlProvider() {
        if(sqlProvider == null) {
            sqlProvider = new ZKSqlProvider(new ZKSqlConvertDelegating(), new ZKThirdPartyAuthAccountUser());
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
	 * 用户的标识，对当前公众号唯一: openid
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_openid", isInsert = true, isUpdate = false, javaType = String.class, isQuery = false)
	String wxOpenid;	
	/**
	 * 用户的昵称: nickname
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_nickname", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxNickname;	
	/**
	 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知: sex
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_wx_sex", isInsert = true, isUpdate = true, javaType = Long.class, isQuery = false)
	Long wxSex;	
	/**
	 * 用户所在城市: city
	 */
	@Length(min = 0, max = 256, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_city", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxCity;	
	/**
	 * 用户所在国家: country
	 */
	@Length(min = 0, max = 128, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_country", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxCountry;	
	/**
	 * 用户所在省份: province
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_province", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxProvince;	
	/**
	 * 用户的语言，简体中文为zh_CN: language
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_language", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxLanguage;	
	/**
	 * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效:  headimgurl
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_headimgurl", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxHeadimgurl;	
	/**
	 * 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间：subscribe_time	
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_subscribe_time", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxSubscribeTime;	
	/**
	 * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段：unionid
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_unionid", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxUnionid;	
	/**
	 * 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注：remark
	 */
	@Length(min = 0, max = 128, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_remark", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxRemark;	
	/**
	 * 用户所在的分组ID（兼容旧的用户分组接口）：groupid
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_groupid", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxGroupid;	
	/**
	 * 用户被打上的标签ID列表：tagid_list
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_tagid_list", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxTagidList;	
	/**
	 * 返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_OTHERS 其他：subscribe_scene
	 */
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_subscribe_scene", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxSubscribeScene;	
	/**
	 * 二维码扫码场景（开发者自定义）：qr_scene
	 */
	@Length(min = 0, max = 128, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_qr_scene", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxQrScene;	
	/**
	 * 二维码扫码场景描述（开发者自定义）：qr_scene_str
	 */
	@Length(min = 0, max = 128, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_wx_qr_scene_str", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
	String wxQrSceneStr;	
	
	public ZKThirdPartyAuthAccountUser() {
		super();
	}

	public ZKThirdPartyAuthAccountUser(String pkId){
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
	 * 用户的昵称: nickname	
	 */	
	public String getWxNickname() {
		return wxNickname;
	}
	
	/**
	 * 用户的昵称: nickname
	 */	
	public void setWxNickname(String wxNickname) {
		this.wxNickname = wxNickname;
	}
	/**
	 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知: sex	
	 */	
	public Long getWxSex() {
		return wxSex;
	}
	
	/**
	 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知: sex
	 */	
	public void setWxSex(Long wxSex) {
		this.wxSex = wxSex;
	}
	/**
	 * 用户所在城市: city	
	 */	
	public String getWxCity() {
		return wxCity;
	}
	
	/**
	 * 用户所在城市: city
	 */	
	public void setWxCity(String wxCity) {
		this.wxCity = wxCity;
	}
	/**
	 * 用户所在国家: country	
	 */	
	public String getWxCountry() {
		return wxCountry;
	}
	
	/**
	 * 用户所在国家: country
	 */	
	public void setWxCountry(String wxCountry) {
		this.wxCountry = wxCountry;
	}
	/**
	 * 用户所在省份: province	
	 */	
	public String getWxProvince() {
		return wxProvince;
	}
	
	/**
	 * 用户所在省份: province
	 */	
	public void setWxProvince(String wxProvince) {
		this.wxProvince = wxProvince;
	}
	/**
	 * 用户的语言，简体中文为zh_CN: language	
	 */	
	public String getWxLanguage() {
		return wxLanguage;
	}
	
	/**
	 * 用户的语言，简体中文为zh_CN: language
	 */	
	public void setWxLanguage(String wxLanguage) {
		this.wxLanguage = wxLanguage;
	}
	/**
	 * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效:  headimgurl	
	 */	
	public String getWxHeadimgurl() {
		return wxHeadimgurl;
	}
	
	/**
	 * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效:  headimgurl
	 */	
	public void setWxHeadimgurl(String wxHeadimgurl) {
		this.wxHeadimgurl = wxHeadimgurl;
	}
	/**
	 * 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间：subscribe_time		
	 */	
	public String getWxSubscribeTime() {
		return wxSubscribeTime;
	}
	
	/**
	 * 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间：subscribe_time	
	 */	
	public void setWxSubscribeTime(String wxSubscribeTime) {
		this.wxSubscribeTime = wxSubscribeTime;
	}
	/**
	 * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段：unionid	
	 */	
	public String getWxUnionid() {
		return wxUnionid;
	}
	
	/**
	 * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段：unionid
	 */	
	public void setWxUnionid(String wxUnionid) {
		this.wxUnionid = wxUnionid;
	}
	/**
	 * 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注：remark	
	 */	
	public String getWxRemark() {
		return wxRemark;
	}
	
	/**
	 * 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注：remark
	 */	
	public void setWxRemark(String wxRemark) {
		this.wxRemark = wxRemark;
	}
	/**
	 * 用户所在的分组ID（兼容旧的用户分组接口）：groupid	
	 */	
	public String getWxGroupid() {
		return wxGroupid;
	}
	
	/**
	 * 用户所在的分组ID（兼容旧的用户分组接口）：groupid
	 */	
	public void setWxGroupid(String wxGroupid) {
		this.wxGroupid = wxGroupid;
	}
	/**
	 * 用户被打上的标签ID列表：tagid_list	
	 */	
	public String getWxTagidList() {
		return wxTagidList;
	}
	
	/**
	 * 用户被打上的标签ID列表：tagid_list
	 */	
	public void setWxTagidList(String wxTagidList) {
		this.wxTagidList = wxTagidList;
	}
	/**
	 * 返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_OTHERS 其他：subscribe_scene	
	 */	
	public String getWxSubscribeScene() {
		return wxSubscribeScene;
	}
	
	/**
	 * 返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，ADD_SCENE_PROFILE_LINK 图文页内名称点击，ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_WECHAT_ADVERTISEMENT 微信广告，ADD_SCENE_OTHERS 其他：subscribe_scene
	 */	
	public void setWxSubscribeScene(String wxSubscribeScene) {
		this.wxSubscribeScene = wxSubscribeScene;
	}
	/**
	 * 二维码扫码场景（开发者自定义）：qr_scene	
	 */	
	public String getWxQrScene() {
		return wxQrScene;
	}
	
	/**
	 * 二维码扫码场景（开发者自定义）：qr_scene
	 */	
	public void setWxQrScene(String wxQrScene) {
		this.wxQrScene = wxQrScene;
	}
	/**
	 * 二维码扫码场景描述（开发者自定义）：qr_scene_str	
	 */	
	public String getWxQrSceneStr() {
		return wxQrSceneStr;
	}
	
	/**
	 * 二维码扫码场景描述（开发者自定义）：qr_scene_str
	 */	
	public void setWxQrSceneStr(String wxQrSceneStr) {
		this.wxQrSceneStr = wxQrSceneStr;
	}
	
	/**
	 * 根据主键类型，重写主键生成；
	 */
	@Override
	protected String genId() {
        return ZKIdUtils.genLongStringId();
    }
	
}