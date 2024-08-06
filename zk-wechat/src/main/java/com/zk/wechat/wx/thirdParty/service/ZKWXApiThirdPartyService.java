/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKWXApiThirdPartyService.java 
* @author Vinson 
* @Package com.zk.wechat.wx.thirdParty.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Nov 5, 2021 5:03:41 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.thirdParty.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSONObject;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.utils.ZKHttpApiUtils;
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.wechat.officialAccounts.entity.ZKOfficialAccounts;
import com.zk.wechat.wx.officialAccounts.ZKWXOfficialAccountsConstants;
import com.zk.wechat.wx.officialAccounts.msgBean.ZKWXAccountAuthAccessToken;
import com.zk.wechat.wx.officialAccounts.msgBean.ZKWXJscode2session;
import com.zk.wechat.wx.officialAccounts.msgBean.ZKWXUserAuthAccessToken;
import com.zk.wechat.wx.thirdParty.ZKWXThirdPartyConstants.ConfigKey;
import com.zk.wechat.wx.thirdParty.ZKWXThirdPartyConstants.MsgAttr;
import com.zk.wechat.wx.thirdParty.ZKWXThirdPartyConstants.ParamName;
import com.zk.wechat.wx.thirdParty.msgBean.ZKWXTPComponentAccessToken;
import com.zk.wechat.wx.thirdParty.msgBean.ZKWXTPPreAuthCode;
import com.zk.wechat.wx.utils.ZKWXUtils;

/** 
* @ClassName: ZKWXApiThirdPartyService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
public class ZKWXApiThirdPartyService {

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    /**
     * 请求微信平台获取第三方平台component_access_token
     *
     * @Title: api_component_token
     * @Description: TODO(simple description this method what to do.)
     * @author bs
     * @date 2018年9月12日 上午11:52:55
     * @param thirdPartyAppid
     *            第三方开发APPID
     * @param thirdAppsecret
     *            第三方开发设置加解密KEY，与微信开方平台设置相同
     * @param componentVerifyTicket
     *            第三方有效令牌
     * @return JSONObject
     */
    public ZKWXTPComponentAccessToken api_component_token(String thirdPartyAppid, String thirdAppsecret,
            String componentVerifyTicket) {
        // https://api.weixin.qq.com/cgi-bin/component/api_component_token
        String url = ZKEnvironmentUtils.getString(ConfigKey.api_component_token);
        StringBuilder params = new StringBuilder();
        params.append("{\"");
        params.append(ParamName.ComponentAccessToken.component_appid).append("\":\"");
        params.append(thirdPartyAppid).append("\",\"");
        params.append(ParamName.ComponentAccessToken.component_appsecret).append("\":\"");
        params.append(thirdAppsecret).append("\",\"");
        params.append(ParamName.ComponentAccessToken.component_verify_ticket).append("\":\"");
        params.append(componentVerifyTicket);
        params.append("\"}");

        StringBuffer outStringBuffer = new StringBuffer();
        // 检验请求响应码
        int resStatusCode = ZKHttpApiUtils.postJson(url, params.toString(), null, outStringBuffer);
        ZKWXUtils.checkResStatusCode(resStatusCode);
        String resStr = outStringBuffer.toString();
        JSONObject resJson = JSONObject.parseObject(resStr);
        // 检验请求响应结果
        ZKWXUtils.checkJsonMsg(resJson);

        ZKWXTPComponentAccessToken componentAccessToken = new ZKWXTPComponentAccessToken(thirdPartyAppid);

        componentAccessToken.setExpiresIn(resJson.containsKey(MsgAttr.ComponentAccessToken.expires_in)
                ? resJson.getIntValue(MsgAttr.ComponentAccessToken.expires_in)
                : 0);
        componentAccessToken.setAccessToken(resJson.getString(MsgAttr.ComponentAccessToken.component_access_token));

        return componentAccessToken;

    }

    /**
     * 请求微信开放平台获取第三方平台预授权码
     *
     * @Title: api_create_preauthcode
     * @Description: TODO(simple description this method what to do.)
     * @author bs
     * @date 2018年9月12日 上午11:53:36
     * @param thirdPartyAppid
     *            第三方开发者APPID
     * @param componentAccessToken
     *            第三方有效的接口调用凭证
     * @return ZKWXTPPreAuthCode
     */
    public ZKWXTPPreAuthCode api_create_preauthcode(ZKSysOrgCompany company, String thirdPartyAppid,
            String componentAccessToken) {
        // https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token={0}
        String url = ZKEnvironmentUtils.getString(ConfigKey.api_create_preauthcode);
        url = ZKStringUtils.replaceByPoint(url, componentAccessToken);
        StringBuilder params = new StringBuilder();
        params.append("{\"");
        params.append(ParamName.PreAuthCode.component_appid).append("\":\"");
        params.append(thirdPartyAppid);
        params.append("\"}");

        StringBuffer outStringBuffer = new StringBuffer();
        int resStatusCode = ZKHttpApiUtils.postJson(url, params.toString(), null, outStringBuffer);
        // 检验请求响应码
        ZKWXUtils.checkResStatusCode(resStatusCode);
        String resStr = outStringBuffer.toString();
        JSONObject resJson = JSONObject.parseObject(resStr);
        // 检验请求响应结果
        ZKWXUtils.checkJsonMsg(resJson);

        String preAuthCodeStr = resJson.getString(MsgAttr.PreAuthCode.pre_auth_code);
        int expiresIn = resJson.containsKey(MsgAttr.PreAuthCode.expires_in)
                ? resJson.getIntValue(MsgAttr.PreAuthCode.expires_in)
                : 0;
        
        ZKWXTPPreAuthCode preAuthCode = new ZKWXTPPreAuthCode(company, thirdPartyAppid, preAuthCodeStr,
                expiresIn);

        return preAuthCode;
    }

    /**
     * 请求微信开放平台，获取公众号或小程序授权信息、接口调用凭证、刷新凭证
     *
     * @Title: api_query_auth
     * @Description: TODO(simple description this method what to do.)
     * @author bs
     * @date 2018年9月12日 上午11:54:09
     * @param outThirdPartyAuthAccount
     *            授权方实体；
     * @param thirdPartyAppid
     *            第三方开发者APPID
     * @param componentAccessToken
     *            第三方有效的接口调用凭证
     * @param authorizationCode
     *            授权码
     * @return ZKWXAccountAuthAccessToken
     */
    public ZKWXAccountAuthAccessToken api_query_auth(ZKOfficialAccounts outThirdPartyAuthAccount,
            String thirdPartyAppid, String componentAccessToken, String authorizationCode) {
        // https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token={0}
        String url = ZKEnvironmentUtils.getString(ConfigKey.api_query_auth);
        url = ZKStringUtils.replaceByPoint(url, componentAccessToken);
        StringBuilder params = new StringBuilder();
        params.append("{\"");
        params.append(ParamName.AuthorizationInfo.component_appid).append("\":\"");
        params.append(thirdPartyAppid).append("\",\"");
        params.append(ParamName.AuthorizationInfo.authorization_code).append("\":\"");
        params.append(authorizationCode);
        params.append("\"}");

        StringBuffer outStringBuffer = new StringBuffer();
        int resStatusCode = ZKHttpApiUtils.postJson(url, params.toString(), null, outStringBuffer);
        // 检验请求响应码
        ZKWXUtils.checkResStatusCode(resStatusCode);
        String resStr = outStringBuffer.toString();
        JSONObject resJson = JSONObject.parseObject(resStr);
        // 检验请求响应结果
        ZKWXUtils.checkJsonMsg(resJson);

        JSONObject authorizationInfoJson = resJson.getJSONObject(MsgAttr.AuthorizationInfo._name);

        String authorizerAppid = authorizationInfoJson.getString(MsgAttr.AuthorizationInfo.authorizer_appid);

        ZKWXAccountAuthAccessToken authAccountAccessToken = new ZKWXAccountAuthAccessToken(thirdPartyAppid,
                authorizerAppid);
        authAccountAccessToken.setAuthorizerRefreshToken(
                authorizationInfoJson.getString(MsgAttr.AuthorizationInfo.authorizer_refresh_token));
        authAccountAccessToken
                .setAccessToken(authorizationInfoJson.getString(MsgAttr.AuthorizationInfo.authorizer_access_token));
        authAccountAccessToken.setExpiresIn(authorizationInfoJson.containsKey(MsgAttr.AuthorizationInfo.expires_in)
                ? authorizationInfoJson.getIntValue(MsgAttr.AuthorizationInfo.expires_in)
                : 0);

        // 权限集列表
        ZKJson funcJson = new ZKJson();
        funcJson.put("_v", authorizationInfoJson.getJSONArray(MsgAttr.AuthorizationInfo.FuncInfo._name));
        outThirdPartyAuthAccount.setWxFuncInfo(funcJson);
        // 这个类型设置还有待商榷，目前无法区分是公众号还是小程序授权
        outThirdPartyAuthAccount.setWxAuthAccountType(ZKOfficialAccounts.KeyAuthAccountType.official);
        // 第三方平台授权
        outThirdPartyAuthAccount.setAddType(ZKOfficialAccounts.KeyAddType.thridPartyAuth);

        return authAccountAccessToken;
    }

    /**
     * 请求微信开放平台，刷新公众号或小程序接口调用凭证
     *
     * @Title: api_authorizer_token
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 5, 2021 3:48:08 PM
     * @param thirdPartyAppid
     *            第三方开发者APPID
     * @param componentAccessToken
     *            第三方有效的接口调用凭证
     * @param authorizerAppid
     *            目标授权方 appid
     * @param authorizerRefreshToken
     *            目标授权方 刷新 Token
     * @return
     * @return ZKWXAccountAuthAccessToken
     */
    public ZKWXAccountAuthAccessToken api_authorizer_token(String thirdPartyAppid, String componentAccessToken,
            String authorizerAppid, String authorizerRefreshToken) {
        // https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token={0}
        String url = ZKEnvironmentUtils.getString(ConfigKey.api_authorizer_token);
        url = ZKStringUtils.replaceByPoint(url, componentAccessToken);
        StringBuilder params = new StringBuilder();
        params.append("{\"");
        params.append(ParamName.RefreshAuthorizerInfo.component_appid).append("\":\"");
        params.append(thirdPartyAppid).append("\",\"");
        params.append(ParamName.RefreshAuthorizerInfo.authorizer_appid).append("\":\"");
        params.append(authorizerAppid).append("\",\"");
        params.append(ParamName.RefreshAuthorizerInfo.authorizer_refresh_token).append("\":\"");
        params.append(authorizerRefreshToken);
        params.append("\"}");

        StringBuffer outStringBuffer = new StringBuffer();
        int resStatusCode = ZKHttpApiUtils.postJson(url, params.toString(), null, outStringBuffer);
        // 检验请求响应码
        ZKWXUtils.checkResStatusCode(resStatusCode);
        String resStr = outStringBuffer.toString();
        JSONObject resJson = JSONObject.parseObject(resStr);
        // 检验请求响应结果
        ZKWXUtils.checkJsonMsg(resJson);

        ZKWXAccountAuthAccessToken authAccountAccessToken = new ZKWXAccountAuthAccessToken(thirdPartyAppid,
                authorizerAppid);
        authAccountAccessToken.setAuthorizerRefreshToken(
                resJson.getString(MsgAttr.AuthorizationInfo.authorizer_refresh_token));
        authAccountAccessToken
                .setAccessToken(resJson.getString(MsgAttr.AuthorizationInfo.authorizer_access_token));
        authAccountAccessToken.setExpiresIn(resJson.containsKey(MsgAttr.AuthorizationInfo.expires_in)
            ? resJson.getIntValue(MsgAttr.AuthorizationInfo.expires_in)
                : 0);

        return authAccountAccessToken;
    }

    /**
     * 请求微信开放平台，获取授权公众号基本信息
     *
     * @Title: api_get_authorizer_info
     * @Description: TODO(simple description this method what to do.)
     * @author bs
     * @date 2018年9月12日 上午11:55:37
     * @param wechatAuthAccount
     *            授权方实体，携带授权方ID
     * @param thirdPartyAppid
     *            第三方开发者APPID
     * @param componentAccessToken
     *            第三方有效的接口调用凭证
     * @param authorizationAppid
     *            授权公众号的授权APPID，注意不是授权公众号的原始 ID
     */
    public void api_get_authorizer_info(ZKOfficialAccounts thirdPartyAuthAccount, String thirdPartyAppid,
            String componentAccessToken) {
        // https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token={0}
        String url = ZKEnvironmentUtils.getString(ConfigKey.api_get_authorizer_info);
        url = ZKStringUtils.replaceByPoint(url, componentAccessToken);
        StringBuilder params = new StringBuilder();
        params.append("{\"");
        params.append(ParamName.AuthorizerInfo.component_appid).append("\":\"");
        params.append(thirdPartyAppid).append("\",\"");
        params.append(ParamName.AuthorizerInfo.authorizer_appid).append("\":\"");
        params.append(thirdPartyAuthAccount.getWxAccountAppid());
        params.append("\"}");

        StringBuffer outStringBuffer = new StringBuffer();
        int resStatusCode = ZKHttpApiUtils.postJson(url, params.toString(), null, outStringBuffer);
        // 检验请求响应码
        ZKWXUtils.checkResStatusCode(resStatusCode);
        String resStr = outStringBuffer.toString();
        JSONObject resJson = JSONObject.parseObject(resStr);
        // 检验请求响应结果
        ZKWXUtils.checkJsonMsg(resJson);

        JSONObject accountInfoJson = resJson.getJSONObject(MsgAttr.AuthorizerInfo._name);

        thirdPartyAuthAccount.setWxNickName(accountInfoJson.getString(MsgAttr.AuthorizerInfo.nick_name));
        thirdPartyAuthAccount.setWxHeadImg(accountInfoJson.getString(MsgAttr.AuthorizerInfo.head_img));
        thirdPartyAuthAccount.setWxServiceTypeInfo(
                ZKJson.parse(accountInfoJson.getJSONObject(MsgAttr.AuthorizerInfo.service_type_info)));
        thirdPartyAuthAccount.setWxVerifyTypeInfo(
                ZKJson.parse(accountInfoJson.getJSONObject(MsgAttr.AuthorizerInfo.verify_type_info)));
        thirdPartyAuthAccount.setWxUserName(accountInfoJson.getString(MsgAttr.AuthorizerInfo.user_name));
        thirdPartyAuthAccount.setWxPrincipalName(accountInfoJson.getString(MsgAttr.AuthorizerInfo.principal_name));
        thirdPartyAuthAccount
                .setWxBusinessInfo(ZKJson.parse(accountInfoJson.getJSONObject(MsgAttr.AuthorizerInfo.business_info)));
        thirdPartyAuthAccount.setWxQrcodeUrl(accountInfoJson.getString(MsgAttr.AuthorizerInfo.qrcode_url));
        thirdPartyAuthAccount.setWxAlias(accountInfoJson.getString(MsgAttr.AuthorizerInfo.alias));
        thirdPartyAuthAccount.setWxSignature(accountInfoJson.getString(MsgAttr.AuthorizerInfo.signature));

        ZKJson funcJson = new ZKJson();
        funcJson.put("_v", resJson.getJSONObject(MsgAttr.AuthorizationInfo._name)
                .getJSONArray(MsgAttr.AuthorizationInfo.FuncInfo._name));
        thirdPartyAuthAccount.setWxFuncInfo(funcJson);

        // {
        // "authorizer_info": {
        // "nick_name": "微信SDK Demo Special",
        // "head_img": "http://wx.qlogo.cn/mmopen/GPy",
        // "service_type_info": { "id": 2 },
        // "verify_type_info": { "id": 0 },
        // "user_name":"gh_eb5e3a772040",
        // "principal_name":"腾讯计算机系统有限公司",
        // "business_info": {"open_store": 0, "open_scan": 0, "open_pay": 0,
        // "open_card": 0, "open_shake": 0},
        // "alias":"paytest01",
        // "qrcode_url":"URL",
        // },
        // "authorization_info": {
        // "authorization_appid": "wxf8b4f85f3a794e77",
        // "func_info": [
        // { "funcscope_category": { "id": 1 } },
        // { "funcscope_category": { "id": 2 } },
        // { "funcscope_category": { "id": 3 } }]
        // }
        // }
    }

    /**
     * 通过授权 code 获取用户 AccessToken
     *
     * @Title: api_user_auth_access_token
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 19, 2022 12:03:07 PM
     * @param thirdAppId
     * @param componentAccessToken
     * @param authAppId
     * @param code
     * @return
     * @return ZKWXUserAuthAccessToken
     */
    public ZKWXUserAuthAccessToken api_official_accounts_user_auth_access_token(String thirdPartyAppid,
            String componentAccessToken, String authAppId, String code) {
        // https://api.weixin.qq.com/sns/oauth2/component/access_token?appid={0}&code={1}&grant_type=authorization_code&component_appid={2}&component_access_token={3}
        String url = ZKEnvironmentUtils.getString(ConfigKey.api_official_accounts_user_auth_access_token);
        url = ZKStringUtils.replaceByPoint(url, authAppId, code, thirdPartyAppid, componentAccessToken);

        StringBuffer outStringBuffer = new StringBuffer();
        int resStatusCode = ZKHttpApiUtils.get(url, null, outStringBuffer);
        // 检验请求响应码
        ZKWXUtils.checkResStatusCode(resStatusCode);
        String resStr = outStringBuffer.toString();
        JSONObject resJson = JSONObject.parseObject(resStr);
        // 检验请求响应结果
        ZKWXUtils.checkJsonMsg(resJson);

        String openid = resJson.getString(ZKWXOfficialAccountsConstants.MsgAttr.access_tokenInfo.openid);
        ZKWXUserAuthAccessToken userAuthAccessToken = new ZKWXUserAuthAccessToken(thirdPartyAppid, authAppId, openid);
        userAuthAccessToken
                .setAccessToken(resJson.getString(ZKWXOfficialAccountsConstants.MsgAttr.access_tokenInfo.access_token));
        userAuthAccessToken.setRefreshToken(
                resJson.getString(ZKWXOfficialAccountsConstants.MsgAttr.access_tokenInfo.refresh_token));
        userAuthAccessToken
                .setExpiresIn(resJson.getIntValue(ZKWXOfficialAccountsConstants.MsgAttr.access_tokenInfo.expires_in));
        userAuthAccessToken.setScope(resJson.getString(ZKWXOfficialAccountsConstants.MsgAttr.access_tokenInfo.scope));
        return userAuthAccessToken;
    }

    /**
     * 刷新 用户授权 token
     *
     * @Title: api_user_auth_refresh_token
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 19, 2022 12:59:55 PM
     * @param thirdAppId
     * @param componentAccessToken
     * @param authAppId
     * @param refreshToken
     * @return
     * @return ZKWXUserAuthAccessToken
     */
    public ZKWXUserAuthAccessToken api_official_accounts_user_auth_refresh_token(String thirdPartyAppid,
            String componentAccessToken,
            String authAppId, String refreshToken) {
        // https://api.weixin.qq.com/sns/oauth2/component/refresh_token?appid={0}&grant_type=refresh_token&component_appid={1}&component_access_token={2}&refresh_token={3}
        String url = ZKEnvironmentUtils.getString(ConfigKey.api_official_accounts_user_auth_refresh_token);
        url = ZKStringUtils.replaceByPoint(url, authAppId, thirdPartyAppid, componentAccessToken, refreshToken);

        StringBuffer outStringBuffer = new StringBuffer();
        int resStatusCode = ZKHttpApiUtils.get(url, null, outStringBuffer);
        // 检验请求响应码
        ZKWXUtils.checkResStatusCode(resStatusCode);
        String resStr = outStringBuffer.toString();
        JSONObject resJson = JSONObject.parseObject(resStr);
        // 检验请求响应结果
        ZKWXUtils.checkJsonMsg(resJson);

        String openid = resJson.getString(ZKWXOfficialAccountsConstants.MsgAttr.access_tokenInfo.openid);
        ZKWXUserAuthAccessToken userAuthAccessToken = new ZKWXUserAuthAccessToken(thirdPartyAppid, authAppId, openid);
        userAuthAccessToken
                .setAccessToken(resJson.getString(ZKWXOfficialAccountsConstants.MsgAttr.access_tokenInfo.access_token));
        userAuthAccessToken.setRefreshToken(
                resJson.getString(ZKWXOfficialAccountsConstants.MsgAttr.access_tokenInfo.refresh_token));
        userAuthAccessToken
                .setExpiresIn(resJson.getIntValue(ZKWXOfficialAccountsConstants.MsgAttr.access_tokenInfo.expires_in));
        userAuthAccessToken.setScope(resJson.getString(ZKWXOfficialAccountsConstants.MsgAttr.access_tokenInfo.scope));
        return userAuthAccessToken;
    }

    /**
     * 小程序登录 第三方平台开发者的服务器使用登录凭证（code）以及第三方平台的 component_access_token 可以代替小程序实现登录功能 获取 session_key 和 openid。
     *
     * @Title: api_miniprogram_jscode2session
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 23, 2022 10:01:08 AM
     * @param thirdPartyAppid
     * @param componentAccessToken
     * @param authAppId
     * @param jsCode
     * @return ZKWXJscode2session
     */
    public ZKWXJscode2session api_miniprogram_jscode2session(String thirdPartyAppid, String componentAccessToken,
            String authAppId, String jsCode) {
        // https://api.weixin.qq.com/sns/component/jscode2session?appid={0}&js_code={1}&grant_type=authorization_code&component_appid={2}&component_access_token={3}
        String url = ZKEnvironmentUtils.getString(ConfigKey.api_miniprogram_user_jscode2session);
        url = ZKStringUtils.replaceByPoint(url, authAppId, jsCode, thirdPartyAppid, componentAccessToken);

        StringBuffer outStringBuffer = new StringBuffer();
        int resStatusCode = ZKHttpApiUtils.get(url, null, outStringBuffer);
        // 检验请求响应码
        ZKWXUtils.checkResStatusCode(resStatusCode);
        String resStr = outStringBuffer.toString();
        JSONObject resJson = JSONObject.parseObject(resStr);
        // 检验请求响应结果
        ZKWXUtils.checkJsonMsg(resJson);
        
        ZKWXJscode2session jscode2session = new ZKWXJscode2session();
        
        jscode2session.setOpenid(resJson.getString(ZKWXOfficialAccountsConstants.MsgAttr.jscode2session.openid));
        jscode2session
                .setSessionKey(resJson.getString(ZKWXOfficialAccountsConstants.MsgAttr.jscode2session.session_key));
        jscode2session.setUnionid(resJson.getString(ZKWXOfficialAccountsConstants.MsgAttr.jscode2session.unionid));

        return jscode2session;
    }

}
