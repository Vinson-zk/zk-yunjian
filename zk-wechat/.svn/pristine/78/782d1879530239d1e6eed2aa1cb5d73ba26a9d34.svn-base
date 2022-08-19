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
* @Title: ZKWXThirdPartyAuthService.java 
* @author Vinson 
* @Package com.zk.wechat.wx.thirdParty.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Nov 4, 2021 5:14:38 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.thirdParty.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zk.core.exception.ZKCodeException;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.wechat.common.ZKWechatCacheUtils;
import com.zk.wechat.thirdParty.entity.ZKThirdParty;
import com.zk.wechat.thirdParty.entity.ZKThirdPartyAuthAccount;
import com.zk.wechat.thirdParty.service.ZKThirdPartyAuthAccountService;
import com.zk.wechat.thirdParty.service.ZKThirdPartyService;
import com.zk.wechat.wx.thirdParty.ZKWXThirdPartyConstants.ConfigKey;
import com.zk.wechat.wx.thirdParty.msgBean.ZKWXTPAuthAccountAccessToken;
import com.zk.wechat.wx.thirdParty.msgBean.ZKWXTPComponentAccessToken;
import com.zk.wechat.wx.thirdParty.msgBean.ZKWXTPPreAuthCode;

/** 
* @ClassName: ZKWXThirdPartyAuthService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
public class ZKWXThirdPartyAuthService {

    /**
     * 日志对象
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    ZKWXApiThirdPartyService apiThirdPartyService;

    @Autowired
    ZKThirdPartyAuthAccountService thirdPartyAuthAccountService;

    @Autowired
    ZKThirdPartyService thirdPartyService;

    /**
     * 授权申请
     * 
     * @param byeType
     *            授权方式；0-网页登录授权；1-移动设备打开链接授权；
     * @param bizAppId
     *            目标公众号原始ID，可以不上传；
     * @param thirdPartyAppid
     *            第三方平台账号；为空取项目配置的默认[zk.wechat.wx.thirdParty.default.appid]第三方平台账号
     * @param authType
     *            要授权的帐号类型：1 则商户点击链接后，手机端仅展示公众号、2 表示仅展示小程序，3
     *            表示公众号和小程序都展示。如果为未指定，则默认小程序和公众号都展示。第三方平台开发者可以使用本字段来控制授权的帐号类型
     * @return
     */
    public String genAuthUrl(int byeType, String bizAppId, String thirdPartyAppid, String authType) {

        if (ZKStringUtils.isEmpty(thirdPartyAppid)) {
            thirdPartyAppid = ZKEnvironmentUtils.getString(ConfigKey.thirdPartyDefaultAppid);
            if (ZKStringUtils.isEmpty(thirdPartyAppid)) {
                log.error("[>_<_^:20211104-1719-002] 第三方平台账号 thirdPartyAppid 为空");
                throw new ZKCodeException("zk.wechat.010005", "第三方平台APPID为空，无法完成授权");
            }
            else {
                log.info("[^_^:20211104-1719-001] 第三方平台账号 thirdPartyAppid 为空，取平台默认第三方平台账号：{}", thirdPartyAppid);
            }
        }

        /** 1、取第三接口调用凭证 */
        ZKWXTPComponentAccessToken componentAccessToken = this.getComponentAccessToken(thirdPartyAppid);
        /** 2、申请预授权码 */
        ZKWXTPPreAuthCode preAuthCode = apiThirdPartyService.api_create_preauthcode(thirdPartyAppid,
                componentAccessToken.getAccessToken());
        /** 3、生成授权 url */
        if (ZKStringUtils.isEmpty(authType) || (!"1".equals(authType) && !"2".equals(authType))) {
            authType = "3";
        }
        if (ZKStringUtils.isEmpty(bizAppId)) {
            bizAppId = "";
        }
        // 授权方授权成功回调URL
        StringBuilder redirectUriSB = new StringBuilder();
        redirectUriSB.append(ZKEnvironmentUtils.getString(ConfigKey.zkWechatDomainName));
        redirectUriSB.append("/");
        redirectUriSB.append(ZKEnvironmentUtils.getString(ConfigKey.thirdPartyAuthCallback));
        redirectUriSB.append(thirdPartyAppid);

        String authUrl = null;
        if (byeType == 0) {
            authUrl = ZKEnvironmentUtils.getString(ConfigKey.thirdPartyAuthUrlWeb);
            authUrl = ZKStringUtils.replaceByPoint(authUrl, thirdPartyAppid, preAuthCode.getPreAuthCode(),
                    redirectUriSB.toString(),
                    authType);
        }
        else {
            authUrl = ZKEnvironmentUtils.getString(ConfigKey.thirdPartyAuthUrlApp);
            authUrl = ZKStringUtils.replaceByPoint(authUrl, thirdPartyAppid, preAuthCode.getPreAuthCode(),
                    redirectUriSB.toString(),
                    authType, bizAppId);
        }
        return authUrl;
    }

    /**********************************************************************************************/
    /*** 与微信平台 相关交互处理 */
    /**********************************************************************************************/

    /**
     * 请求微信平台获取第三方平台component_access_token
     *
     * @Title: getComponentAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 5, 2021 5:14:19 PM
     * @param thirdPartyAppid
     * @return
     * @return ZKWXTPComponentAccessToken
     */
    public ZKWXTPComponentAccessToken getComponentAccessToken(String thirdPartyAppid) {
        ZKThirdParty thirdParty = thirdPartyService.get(thirdPartyAppid);

        if (thirdParty == null) {
            log.error("[>_<_^:20211104-1758-001] 第三方平台账号未录入系统，无法获取 ComponentAccessToken");
            throw new ZKCodeException("zk.wechat.010006", "第三方平台账号未录入系统，无法获取 ComponentAccessToken");
        }

        /** 1、从缓存中取 第三方平台 componentAccessToken 并判断是否存在或过期 */
        ZKWXTPComponentAccessToken componentAccessToken = ZKWechatCacheUtils
                .getWXTPComponentAccessToken(thirdPartyAppid);

        /** 1、判断第三方平台 componentAccessToken 是否过期，未过期，直接返回 */
        if (componentAccessToken != null && !componentAccessToken.isExpires()) {
            return componentAccessToken;
        }

        /** 2、判断第三方平台 令牌是否存在，存在继续 */
        String componentVerifyTicket = thirdParty.getWxTicket();
        if (ZKStringUtils.isEmpty(componentVerifyTicket)) {
            log.error("[>_<_^:20211104-1758-002] 第三方平台账号令牌不存在，无法获取 ComponentAccessToken");
            throw new ZKCodeException("zk.wechat.010007", "第三方平台账号令牌不存在，无法获取 ComponentAccessToken");
        }

        /** 3、取 token 并保存 */
        if (componentAccessToken == null) {
            componentAccessToken = apiThirdPartyService.api_component_token(thirdParty.getPkId(),
                    thirdParty.getWxAppSecret(), thirdParty.getWxTicket());

        }
        return componentAccessToken;
    }

    /**
     * 取 目标授权方 authAccountAccessToken
     *
     * @Title: getAuthAccountAccessToken
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 8, 2021 6:57:41 PM
     * @param thirdPartyAppid
     * @param authorizerAppid
     * @return
     * @return ZKWXTPAuthAccountAccessToken
     */
    public ZKWXTPAuthAccountAccessToken getAuthAccountAccessToken(String thirdPartyAppid, String authorizerAppid) {

        ZKThirdPartyAuthAccount thirdPartyAuthAccount = thirdPartyAuthAccountService
                .getByThirdPartyAppidAndAuthorizerAppid(thirdPartyAppid, authorizerAppid);

        if (thirdPartyAuthAccount == null) {
            log.error(
                    "[>_<_^:20211108-1848-001] 目标授权方未向第三方平台授权，无法获取 AuthAccountAccessToken; thirdPartyAppid：{}，authorizerAppid：{}",
                    thirdPartyAppid, authorizerAppid);
            throw new ZKCodeException("zk.wechat.010011",
                    "目标授权方未向第三方平台授权，无法获取 AuthAccountAccessToken");
        }

        /** 1、从缓存中取 目标授权方 authAccountAccessToken 并判断是否存在或过期 */
        ZKWXTPAuthAccountAccessToken authAccountAccessToken = ZKWechatCacheUtils.getWXTPAuthAccountAccessToken(
                ZKWXTPAuthAccountAccessToken.makeIdentification(thirdPartyAppid, authorizerAppid));

        /** 1、判断 目标授权方 authAccountAccessToken 是否过期，未过期，直接返回 */
        if (authAccountAccessToken != null && !authAccountAccessToken.isExpires()) {
            return authAccountAccessToken;
        }

        /** 2、判断 目标授权方刷新 AuthorizerRefreshToken 是否存在，存在继续 */
        if (ZKStringUtils.isEmpty(thirdPartyAuthAccount.getWxAuthorizerRefreshToken())) {
            log.error(
                    "[>_<_^:20211108-1848-002] 目标授权方刷新 AuthorizerRefreshToken 不存在，无法获取 AuthAccountAccessToken; thirdPartyAppid：{}，authorizerAppid：{}",
                    thirdPartyAppid, authorizerAppid);
            throw new ZKCodeException("zk.wechat.010012", "目标授权方刷新 AuthorizerRefreshToken 不存在");
        }

        /** 3、刷新 AuthorizerRefreshToken 并保存 */
        if (authAccountAccessToken == null) {
            authAccountAccessToken = apiThirdPartyService.api_authorizer_token(thirdPartyAppid,
                    this.getComponentAccessToken(thirdPartyAppid).getAccessToken(), authorizerAppid,
                    thirdPartyAuthAccount.getWxAuthorizerRefreshToken());
            // 保存目标授权账号 accessToken 到 缓存
            ZKWechatCacheUtils.putWXTPAuthAccountAccessToken(authAccountAccessToken);
        }
        return authAccountAccessToken;
    }

    /**********************************************************************************************/
    /*** 与微信平台 相关消息处理 */
    /**********************************************************************************************/

    /**
     * 成功授权，或更新授权通知处理
     *
     * @Title: authAuthorizedAndUpdateauthorized
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 9:28:49 AM
     * @param thirdPartyAppid
     * @param authorizerAppid
     * @param authorizationCode
     * @param preAuthCode
     * @return void
     */
    public void authAuthorizedAndUpdateauthorized(String thirdPartyAppid, String authorizerAppid, String authorizationCode,
            String preAuthCode) {
        
        /** 1、先判断 目标授权账号是否已授权 */
        ZKThirdPartyAuthAccount thirdPartyAuthAccount = this.thirdPartyAuthAccountService
                .getByThirdPartyAppidAndAuthorizerAppid(thirdPartyAppid, authorizerAppid);
        if(thirdPartyAuthAccount == null) {
            thirdPartyAuthAccount = new ZKThirdPartyAuthAccount();
            thirdPartyAuthAccount.setWxThirdPartyAppid(thirdPartyAppid);
            thirdPartyAuthAccount.setWxAuthorizerAppid(authorizerAppid);
        }else {
            if(ZKThirdPartyAuthAccount.DEL_FLAG.delete == thirdPartyAuthAccount.getDelFlag()) {
             // 如果已删除，恢复
                thirdPartyAuthAccount.setDelFlag(ZKThirdPartyAuthAccount.DEL_FLAG.normal);
            }
        }
        
        /** 2、取目标授权账号的 accessToken */
        ZKWXTPComponentAccessToken componentAccessToken = this.getComponentAccessToken(thirdPartyAppid);
        ZKWXTPAuthAccountAccessToken authAccountAccessToken = this.apiThirdPartyService.api_query_auth(
                thirdPartyAuthAccount, thirdPartyAppid, componentAccessToken.getAccessToken(), authorizationCode);
        thirdPartyAuthAccount.setWxAuthorizerRefreshToken(authAccountAccessToken.getAuthorizerRefreshToken());
        // 保存目标授权账号 accessToken 到 缓存
        ZKWechatCacheUtils.putWXTPAuthAccountAccessToken(authAccountAccessToken);
        
        /** 3、取目标授权账号的信息 */
        apiThirdPartyService.api_get_authorizer_info(thirdPartyAuthAccount, thirdPartyAppid,
                componentAccessToken.getAccessToken());
        
        this.thirdPartyAuthAccountService.save(thirdPartyAuthAccount);
    }

    /**
     * 目标授权账号取消授权
     *
     * @Title: authUnauthorized
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 8, 2021 6:07:32 PM
     * @param thirdPartyAppid
     * @param authorizerAppid
     * @return void
     */
    public void authUnauthorized(String thirdPartyAppid, String authorizerAppid) {
        ZKThirdPartyAuthAccount thirdPartyAuthAccount = this.thirdPartyAuthAccountService
                .getByThirdPartyAppidAndAuthorizerAppid(thirdPartyAppid, authorizerAppid);
        if (thirdPartyAuthAccount != null) {
            this.thirdPartyAuthAccountService.del(thirdPartyAuthAccount);
        }
    }

}
