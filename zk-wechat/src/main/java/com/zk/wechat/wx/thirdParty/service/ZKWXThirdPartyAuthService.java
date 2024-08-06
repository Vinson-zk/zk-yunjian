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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.wechat.common.ZKWechatCacheUtils;
import com.zk.wechat.officialAccounts.entity.ZKOfficialAccounts;
import com.zk.wechat.officialAccounts.service.ZKOfficialAccountsService;
import com.zk.wechat.thirdParty.service.ZKThirdPartyService;
import com.zk.wechat.wx.officialAccounts.msgBean.ZKWXAccountAuthAccessToken;
import com.zk.wechat.wx.thirdParty.ZKWXThirdPartyConstants.ConfigKey;
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
    protected Logger log = LogManager.getLogger(getClass());

    @Autowired
    ZKThirdPartyService thirdPartyService;

    @Autowired
    ZKOfficialAccountsService officialAccountsService;

    @Autowired
    ZKWXThirdPartyService wxThirdPartyService;

    @Autowired
    ZKWXApiThirdPartyService wxApiThirdPartyService;

    /**
     * 授权申请
     * 
     * @param companyCode
     *            授权方公司代码
     * 
     * @param byeType
     *            授权方式；0-网页登录授权；1-移动设备打开链接授权；
     * @param bizAppId
     *            目标公众号原始ID，可以不上传；
     * @param thirdPartyAppid
     *            第三方平台账号；为空取项目配置的默认[zk.wechat.wx.thirdParty.default.appid]第三方平台账号
     * @param authType
     *            要授权的帐号类型：1 则商户点击链接后，手机端仅展示公众号、2 表示仅展示小程序，3 表示公众号和小程序都展示。如果为未指定，则默认小程序和公众号都展示。第三方平台开发者可以使用本字段来控制授权的帐号类型
     * @return
     */
    public String genAuthUrl(String companyCode, int byeType, String bizAppId, String thirdPartyAppid,
            String authType) {
        ZKSysOrgCompany company = this.thirdPartyService.getCompanyByCode(companyCode);
        if (company == null) {
            log.error("[>_<:20220518-0020-001] 目标公司[]不存在", companyCode);
            throw ZKBusinessException.as("zk.wechat.010013", null, companyCode);
        }

        if (ZKStringUtils.isEmpty(thirdPartyAppid)) {
            thirdPartyAppid = ZKEnvironmentUtils.getString(ConfigKey.thirdPartyDefaultAppid);
            if (ZKStringUtils.isEmpty(thirdPartyAppid)) {
                log.error("[>_<_^:20211104-1719-002] 第三方平台账号 thirdPartyAppid 为空");
                throw ZKBusinessException.as("zk.wechat.010005");
            }
            else {
                log.info("[^_^:20211104-1719-001] 第三方平台账号 thirdPartyAppid 为空，取平台默认第三方平台账号：{}", thirdPartyAppid);
            }
        }

        /** 1、取第三接口调用凭证 */
        ZKWXTPComponentAccessToken componentAccessToken = wxThirdPartyService.getComponentAccessToken(thirdPartyAppid);
        /** 2、申请预授权码 */
        ZKWXTPPreAuthCode preAuthCode = wxApiThirdPartyService.api_create_preauthcode(company, thirdPartyAppid,
                componentAccessToken.getAccessToken());
//        if (preAuthCode == null) {
//            // 这里不需要判断了，在 apiThirdPartyService.api_create_preauthcode 如果返回取预授权码异常，会抛出异常
//        }
        // 保存预授权码到缓存
        ZKWechatCacheUtils.putWXTPPreAuthCode(preAuthCode);
        /** 3、生成授权 url */
        if (ZKStringUtils.isEmpty(authType) || (!"1".equals(authType) && !"2".equals(authType))) {
            authType = "3";
        }
        if (ZKStringUtils.isEmpty(bizAppId)) {
            bizAppId = "";
        }
        // 授权方授权成功回调URL，此 url 域名需要与开放平台设置的发起域名[即发起请求的域名]相同；
        StringBuilder redirectUriSB = new StringBuilder();
        redirectUriSB.append(ZKEnvironmentUtils.getString(ConfigKey.zkWechatDomainName));
        redirectUriSB.append("/");
        redirectUriSB.append(ZKEnvironmentUtils.getString(ConfigKey.thirdPartyAuthCallback));
        redirectUriSB.append(thirdPartyAppid);
        redirectUriSB.append("/");
        redirectUriSB.append(companyCode);

        String authUrl = null;
        if (byeType == 0) {
            authUrl = ZKEnvironmentUtils.getString(ConfigKey.url_auth_web_componentloginpage);
            authUrl = ZKStringUtils.replaceByPoint(authUrl, thirdPartyAppid, preAuthCode.getPreAuthCode(),
                    redirectUriSB.toString(),
                    authType);
        }
        else {
            authUrl = ZKEnvironmentUtils.getString(ConfigKey.url_auth_app_bindcomponent);
            authUrl = ZKStringUtils.replaceByPoint(authUrl, thirdPartyAppid, preAuthCode.getPreAuthCode(),
                    redirectUriSB.toString(),
                    authType, bizAppId);
        }
        return authUrl;
    }

    /**********************************************************************************************/
    /*** 与微信平台 相关交互处理 */
    /**********************************************************************************************/



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
//    @Transactional(readOnly = false)
    public void authAuthorizedAndUpdateauthorized(String thirdPartyAppid, String authorizerAppid, String authorizationCode,
            String preAuthCode) {
        
        /** 1、先判断 目标授权账号是否已授权 */
        ZKOfficialAccounts thirdPartyAuthAccount = this.officialAccountsService.getByAccountAppid(thirdPartyAppid,
                authorizerAppid);
        if(thirdPartyAuthAccount == null) {
            thirdPartyAuthAccount = new ZKOfficialAccounts();
            thirdPartyAuthAccount.setWxThirdPartyAppid(thirdPartyAppid);
            thirdPartyAuthAccount.setWxAccountAppid(authorizerAppid);
        }else {
            if (ZKBaseEntity.DEL_FLAG.delete == thirdPartyAuthAccount.getDelFlag()) {
                // 如果已删除，恢复
                // thirdPartyAuthAccount.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);
                this.officialAccountsService.restore(thirdPartyAuthAccount);
            }
        }
        
        /** 2、根据授权码设置目标授权账号的公司信息 */
        // 取缓存中的预授权码实体
        ZKWXTPPreAuthCode zkPreAuthCode = ZKWechatCacheUtils
                .getWXTPPreAuthCode(ZKWXTPPreAuthCode.makeIdentification(thirdPartyAppid, preAuthCode));
        if (zkPreAuthCode == null) {
            log.error("[>_<:20220518-0047-001] zk.wechat.010014=预授权码已过期或不存在");
            throw ZKBusinessException.as("zk.wechat.010014");
        }
        else {
            // 设置公司信息
            thirdPartyAuthAccount.setGroupCode(zkPreAuthCode.getCompany().getGroupCode());
            thirdPartyAuthAccount.setCompanyId(zkPreAuthCode.getCompany().getPkId());
            thirdPartyAuthAccount.setCompanyCode(zkPreAuthCode.getCompany().getCode());
        }

        /** 3、取目标授权账号的 accessToken */
        ZKWXTPComponentAccessToken componentAccessToken = wxThirdPartyService.getComponentAccessToken(thirdPartyAppid);
        ZKWXAccountAuthAccessToken authAccountAccessToken = this.wxApiThirdPartyService.api_query_auth(
                thirdPartyAuthAccount, thirdPartyAppid, componentAccessToken.getAccessToken(), authorizationCode);
        thirdPartyAuthAccount.setWxAuthorizerRefreshToken(authAccountAccessToken.getAuthorizerRefreshToken());
        // 保存目标授权账号 accessToken 到 缓存
        ZKWechatCacheUtils.putWXAccountAuthAccessToken(authAccountAccessToken);
        
        /** 4、取目标授权账号的信息 */
        wxApiThirdPartyService.api_get_authorizer_info(thirdPartyAuthAccount, thirdPartyAppid,
                componentAccessToken.getAccessToken());
        
        this.officialAccountsService.save(thirdPartyAuthAccount);
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
        ZKOfficialAccounts thirdPartyAuthAccount = this.officialAccountsService.getByAccountAppid(thirdPartyAppid,
                authorizerAppid);
        if (thirdPartyAuthAccount != null) {
            this.officialAccountsService.del(thirdPartyAuthAccount);
        }
    }

}
