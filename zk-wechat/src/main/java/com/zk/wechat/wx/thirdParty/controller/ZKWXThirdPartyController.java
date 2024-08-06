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
* @Title: ZKWXThirdPartyController.java 
* @author Vinson 
* @Package com.zk.wechat.wx.thirdParty.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Nov 4, 2021 5:03:06 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.thirdParty.controller;

import java.nio.charset.Charset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.ZKContentType;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.utils.ZKServletUtils;
import com.zk.security.ticket.ZKSecTicketManager;
import com.zk.wechat.officialAccounts.entity.ZKOfficialAccountsUser;
import com.zk.wechat.platformBusiness.entity.ZKFuncKeyConfig;
import com.zk.wechat.platformBusiness.service.ZKFuncKeyConfigService;
import com.zk.wechat.thirdParty.entity.ZKThirdParty;
import com.zk.wechat.thirdParty.service.ZKThirdPartyService;
import com.zk.wechat.wx.officialAccounts.service.ZKWXOfficialAccountsNotificationService;
import com.zk.wechat.wx.thirdParty.ZKWXThirdPartyConstants.ConfigKey;
import com.zk.wechat.wx.thirdParty.service.ZKWXThirdPartyAuthService;
import com.zk.wechat.wx.thirdParty.service.ZKWXThirdPartyMsgService;
import com.zk.wechat.wx.thirdParty.service.ZKWXThirdPartyService;
import com.zk.wechat.wx.utils.ZKWXMsgUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 第三方开放平台的一些方法
 * 
 * @ClassName: ZKWXThirdPartyController
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */

@Controller
@RequestMapping("${zk.path.admin}/${zk.path.wechat}/${zk.wechat.version}/${zk.path.wechat.wx.thirdParty}")
public class ZKWXThirdPartyController {

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    ZKWXThirdPartyMsgService wxThirdPartyMsgService;

    @Autowired
    ZKWXThirdPartyAuthService wxThirdPartyAuthService;

    @Autowired
    ZKWXThirdPartyService wxThirdPartyService;

    @Autowired
    ZKWXOfficialAccountsNotificationService wxOfficialAccountsNotificationService;

    @Autowired
    ZKThirdPartyService thirdPartyService;

    @Autowired
    ZKSecTicketManager tm;

//    @Autowired
//    ZKFuncKeyTypeService funcKeyTypeService;

    @Autowired
    ZKFuncKeyConfigService funcKeyConfigService;


    /**
     * 目标授权账号授权申请；
     *
     * @Title: auth
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 18, 2022 10:04:49 AM
     * @param companyCode
     * @param byeType
     *            授权方式；0-网页登录授权；1-移动设备打开链接授权；
     * @param bizAppid
     *            授权方原始ID
     * @param thirdPartyAppid
     * @param authType
     *            要授权的帐号类型：1 则商户点击链接后，手机端仅展示公众号、2 表示仅展示小程序，3 表示公众号和小程序都展示。如果为未指定，则默认小程序和公众号都展示。第三方平台开发者可以使用本字段来控制授权的帐号类型
     * @param req
     * @param res
     * @return ZKMsgRes
     */
    @RequestMapping(value = "auth", method = RequestMethod.POST)
    @ResponseBody
    public ZKMsgRes auth(@RequestParam(value = "companyCode") String companyCode,
            @RequestParam(value = "byeType", required = false, defaultValue = "0") int byeType,
            @RequestParam(value = "bizAppid", required = false) String bizAppid,
            @RequestParam(value = "thirdPartyAppid", required = false, defaultValue = "") String thirdPartyAppid,
            @RequestParam(value = "authType", required = false, defaultValue = "") String authType,
            HttpServletRequest req, HttpServletResponse res) {

        String authUrl = this.wxThirdPartyAuthService.genAuthUrl(companyCode, byeType, bizAppid, thirdPartyAppid,
                authType);

        ZKMsgRes resMsg = ZKMsgRes.asOk(null, authUrl);
        return resMsg;
        // return "redirect:" + authUrl;
        // return "forward:" + authUrl;
    }
    
    /**
     * 目标授权账号，授权成功回调函数
     *
     * @Title: callBack
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 8:51:28 AM
     * @param thirdPartyAppid
     * @param companyCode
     * @param req
     * @return String
     */
    @RequestMapping(value = "authCallBack/{thirdPartyAppid}/{companyCode}")
    // @ResponseBody
    public String authCallBack(@PathVariable(name = "thirdPartyAppid") String thirdPartyAppid,
            @PathVariable(name = "companyCode") String companyCode, HttpServletRequest req) {

        // 授权码，过期时间就不用取了，取到授权码，马上取已授权的 “目标微信公众号或目标小程序” 的信息
//        String authCode = WebUtils.getCleanParam(req, WXConstants.WxMsgAttribute.AuthorizationRedirect.auth_code);
//        this.thirdAuthService.authCallBack(companyCode, authCode);
        return "redirect:" + ZKEnvironmentUtils.getString(ConfigKey.zkWechatDomainName)
                + ZKEnvironmentUtils.getString(ConfigKey.thirdPartyAuthCallbackRedirect);
    }

    /**
     * 授权事件接收URL
     * 
     * 用于接收取消授权通知、授权成功通知、授权更新通知，也用于接收ticket，ticket是验证平台方的重要凭据。
     *
     * @Title: authNotification
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 4, 2021 5:01:11 PM
     * @param appId
     *            第三方平台账号
     * @param req
     * @return String
     */
    @RequestMapping(value = "authReceive/{thirdPartyAppId}")
    @ResponseBody
    public String authNotification(@PathVariable("thirdPartyAppId") String thirdPartyAppId, HttpServletRequest req) {

        // 授权码，过期时间就不用取了，取到授权码，马上取已授权的 “目标微信公众号或目标小程序” 的信息
        try {
            // 1、从请求流中读出字节
            byte[] msgBytes = ZKServletUtils.read(req);
            // 2、密文
            String encStr = new String(msgBytes, Charset.forName("UTF-8"));
            // 3、解密 xml 消息密文
            ZKThirdParty wxThirdPart = thirdPartyService.get(thirdPartyAppId);
            if (wxThirdPart == null) {
                log.error("[>_<:20210218-1025-001] 此微信第三方平台 thirdPartyAppId:{} ; 尚未与系统对接。", thirdPartyAppId);
                throw ZKBusinessException.as("zk.wechat.010001");
            }

            //
            Document notificationDoc = ZKWXMsgUtils.getDocumentMsg(wxThirdPart.getWxToken(), wxThirdPart.getWxAesKey(),
                    wxThirdPart.getPkId(), encStr);

            wxThirdPartyMsgService.authMsgNotification(thirdPartyAppId, notificationDoc.getRootElement());
        }
        catch(Exception e) {
            e.printStackTrace();
            log.error("[>_<:20180906-1948-001] 接收取消授权通知、授权成功通知、授权更新通知、ticket 时出错了！appId:{} ", thirdPartyAppId);
        }
        return "success";
    }
    
    /********************************************************************************************/
    /********************************************************************************************/

    /**
     * 接收公众号或小程序的消息与事件接收URL
     *
     * @Title: eventNotification
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 4, 2021 5:11:02 PM
     * @param thirdPartyAppId
     *            第三方平台 appId，在第三方平台配置是，配置在路径中；
     * @param appId
     *            公众号或小程序的 appId
     * @param req
     * @return String
     */
    @RequestMapping(value = "event/{thirdPartyAppId}/{appId}")
    @ResponseBody
    public String eventNotification(@PathVariable("thirdPartyAppId") String thirdPartyAppId,
            @PathVariable("appId") String appId, HttpServletRequest req) {

        // 授权码，过期时间就不用取了，取到授权码，马上取已授权的 “目标微信公众号或目标小程序” 的信息
        try {
            // 1、从请求流中读出字节
            byte[] msgBytes = ZKServletUtils.read(req);
            // 2、密文
            String encStr = new String(msgBytes, Charset.forName("UTF-8"));
            // 3、解密 xml 消息密文
            ZKThirdParty wxThirdPart = thirdPartyService.get(thirdPartyAppId);
            if (wxThirdPart == null) {
                log.error("[>_<:20210219-0055-001] 此微信第三方平台 thirdPartyAppId:{} ; 尚未与系统对接。", thirdPartyAppId);
                throw ZKBusinessException.as("zk.wechat.010001", "未对接第三方微信平台");
            }

            Document notificationDoc = ZKWXMsgUtils.getDocumentMsg(wxThirdPart.getWxToken(), wxThirdPart.getWxAesKey(),
                    wxThirdPart.getPkId(), encStr);

            wxOfficialAccountsNotificationService.notification(thirdPartyAppId, appId, notificationDoc.getRootElement(),
                    () -> {
                        return this.wxThirdPartyService.getAccountAuthAccessToken(thirdPartyAppId, appId);
                    });
        }
        catch(Exception e) {
            e.printStackTrace();
            log.error("[>_<:20220519-2357-001] 接收第三方平台代收的公众号或小程序事件/消息失败；thirdPartyAppId：{}，appId：{}", thirdPartyAppId,
                    appId);
        }
        return "success";
    }

    /**
     * 若用户禁止授权，则重定向后不会带上 code 参数，仅会带上 state 参数
     * 
     * 用户授权链接示例：
     * 
     * 第三方平台代为实现：https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE&component_appid=component_appid#wechat_redirect
     * 微信公众号自已实现：https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=123#wechat_redirect
     *
     * @Title: authUserReceive
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 20, 2022 11:26:38 AM
     * @param thirdPartyAppId
     * @param funcKey
     * @param code
     * @param state
     * @param appid
     * @param req
     * @return String
     */
    @RequestMapping(value = "authUserReceive/{thirdPartyAppId}/{funcKey}")
    public String authUserReceive(@PathVariable("thirdPartyAppId") String thirdPartyAppId,
            @PathVariable("funcKey") String funcKey,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "appid", required = false) String appid, HttpServletRequest hReq,
            HttpServletResponse hRes) {

        if (ZKStringUtils.isEmpty(code)) {
            // 用户禁止授权；需要转发到未授权的错误页面
            log.error("[>_<:20220523-1003-001] 用户未授权；thirdPartyAppId:{}, appid:{}, funcKey:{}, state:{}, code:{}",
                    thirdPartyAppId, appid, funcKey, state, code);
            return "redirect:" + "error/wxuser.noAuth";
//            ZKWebUtils.redirectUrl(hReq, hRes, "error/wxuser.noAuth");
        }

        ZKMsgRes res = null;
        try {
            // 取用户 accessToken
            ZKOfficialAccountsUser officialAccountsUser = this.wxThirdPartyService
                    .getUserAccessTokenByAuthCode(thirdPartyAppId, appid, funcKey, code, state);

            // 查询配置的funckey 对应的重定向地址
            ZKFuncKeyConfig funcKeyConfig = funcKeyConfigService.getByFuncKey(funcKey, ZKBaseEntity.DEL_FLAG.normal);
            if (funcKeyConfig != null) {
                if (funcKeyConfig.getStatus().intValue() == ZKFuncKeyConfig.KeyStatus.normal) {
                    String redirectUrl = funcKeyConfig.getRedirectProxyUrl();
                    StringBuffer redirectSB = new StringBuffer();
                    redirectSB = redirectSB.append("redirect:");
                    redirectSB.append(funcKeyConfig.getRedirectProxyUrl());
                    if (redirectUrl.indexOf("?") > -1) {
                        redirectSB.append("&wxid=");
                    }
                    else {
                        redirectSB.append("?wxid=");
                    }
                    redirectSB.append(officialAccountsUser.getPkId());
                    // 国际化语言
                    redirectSB.append("&locale=");
                    redirectSB.append(officialAccountsUser.getWxLanguage());
                    // 关注公众号状态
                    redirectSB.append("&subscribe=");
                    redirectSB.append(officialAccountsUser.getWxSubscribe());
                    // 功能 funcKey
                    redirectSB.append("&funcKey=");
                    redirectSB.append(funcKey);
                    // openId
                    redirectSB.append("&openid=");
                    redirectSB.append(officialAccountsUser.getWxOpenid());
                    
//                    // 还是无微信服务身份
//                    HttpSession session = hReq.getSession();
//                    ZKSecTicket tk = this.getServerTk();
//                    session.setAttribute(ZKSecConstants.PARAM_NAME.TicketId, tk == null ? "" : tk.getTkId());
                    
                    return redirectSB.toString();
                }
                else {
//                    ZKWebUtils.redirectUrl(request, response, url);
                    // 此功能已被禁用。重写向到统一的功能禁用页 zk.wechat.110004=功能已停用，请联系管理员
                    res = ZKMsgRes.asCode("zk.wechat.110004");
                }
            }
            else {
                // 功能不存在 zk.wechat.110005=功能不存在，请联系管理员
                res = ZKMsgRes.asCode(null, "zk.wechat.110005");
            }

        }
        catch(Exception e) {
            e.printStackTrace();
            log.error("[>_<:20220519-2357-002] 接收用户授权消息失败；thirdPartyAppId:{}, appid:{}, funcKey:{} ", thirdPartyAppId,
                    appid, funcKey);
            // zk.wechat.110006=系统异常，请联系管理员
            res = ZKMsgRes.asCode("zk.wechat.110006");
        }
        StringBuffer resStrSB = new StringBuffer();
        resStrSB.append("<font size=\"24\">");
        resStrSB.append(res.toString());
        resStrSB.append("</font>");
        ZKServletUtils.renderString(hRes, resStrSB.toString(), ZKContentType.XHTML_UTF8.getContentType());
        return null;
    }

    /********************************************************/
    /**
     * 小程序登录取 openid
     *
     * @Title: authUserReceive
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 23, 2022 10:27:26 AM
     * @param thirdPartyAppId
     * @param authAppId
     * @param jsCode
     * @param req
     * @return String
     */
    @RequestMapping(value = "miniprogram/jscode2session/{thirdPartyAppId}/{authAppId}/{funcKey}", method = RequestMethod.POST)
    public String authUserReceive(@PathVariable("thirdPartyAppId") String thirdPartyAppId,
            @PathVariable(value = "authAppId") String authAppId,
            @PathVariable(value = "funcKey", required = false) String funcKey,
            @RequestParam(value = "jsCode") String jsCode,
            HttpServletRequest req, HttpServletResponse hRes) {

        if (ZKStringUtils.isEmpty(jsCode)) {
            log.error(
                    "[>_<_^:20220523-1013-002] 小程序取用户，取 openid 失败; jsCode 为空；thirdPartyAppid：{}，authAppId：{}，jsCode：{}",
                    thirdPartyAppId, authAppId, jsCode);
            throw ZKBusinessException.as("zk.wechat.010019");
        }

        // 取小程序用户的 openid sessionkey Unionid
        ZKOfficialAccountsUser officialAccountsUser = this.wxThirdPartyService.getUserJscode2session(thirdPartyAppId,
                authAppId, jsCode);
        ZKMsgRes res = null;
        if (ZKStringUtils.isEmpty(funcKey)) {
            res = ZKMsgRes.asOk(null, officialAccountsUser);
        }
        else {
            // 查询配置的funckey 对应的重定向地址
            ZKFuncKeyConfig funcKeyConfig = funcKeyConfigService.getByFuncKey(funcKey, ZKBaseEntity.DEL_FLAG.normal);
            if (funcKeyConfig != null) {
                if (funcKeyConfig.getStatus().intValue() == ZKFuncKeyConfig.KeyStatus.normal) {
                    String redirectUrl = funcKeyConfig.getRedirectProxyUrl();
                    StringBuffer redirectSB = new StringBuffer();
                    redirectSB = redirectSB.append("redirect:");
                    redirectSB.append(funcKeyConfig.getRedirectProxyUrl());
                    if (redirectUrl.indexOf("?") > -1) {
                        redirectSB.append("&wxid=");
                    }
                    else {
                        redirectSB.append("?wxid=");
                    }
                    redirectSB.append(officialAccountsUser.getPkId());
                    // 国际化语言
                    redirectSB.append("&locale=");
                    redirectSB.append(officialAccountsUser.getWxLanguage());
                    // 功能 funcKey
                    redirectSB.append("&funcKey=");
                    redirectSB.append(funcKey);
                    // openId
                    redirectSB.append("&openid=");
                    redirectSB.append(officialAccountsUser.getWxOpenid());

//                    // 还是无微信服务身份
//                    HttpSession session = hReq.getSession();
//                    ZKSecTicket tk = this.getServerTk();
//                    session.setAttribute(ZKSecConstants.PARAM_NAME.TicketId, tk == null ? "" : tk.getTkId());

                    return redirectSB.toString();
                }
                else {
//                    ZKWebUtils.redirectUrl(request, response, url);
                    // 此功能已被禁用。重写向到统一的功能禁用页 zk.wechat.110004=功能已停用，请联系管理员
                    res = ZKMsgRes.asCode("zk.wechat.110004");
                }
            }
            else {
                // 功能不存在 zk.wechat.110005=功能不存在，请联系管理员
                res = ZKMsgRes.asCode("zk.wechat.110005");
            }
        }
        if (res != null) {
            ZKServletUtils.renderString(hRes, res.toString(), ZKContentType.XHTML_UTF8.getContentType());
        }
        return null;
    }

//    /**
//     * 取微服务令牌
//     *
//     * @Title: getServerTk
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date Jul 3, 2022 12:21:09 AM
//     * @return
//     * @return ZKSecTicket
//     */
//    public ZKSecTicket getServerTk() {
//        ZKSecPrincipalCollection sPc = new ZKSecDefaultPrincipalCollection();
//        // 取微服务间令牌
//        String sTkId = tm.generateTkId().toString();
////        String sTkId = (String) tm.generateTkId(appName);
//        ZKSecTicket sTk = tm.getTicket(sTkId);
//        if (sTk == null) {
//            // 微服务还未有令牌，创建微服务令牌
//            sTk = tm.createSecTicket(sTkId, ZKSecTicket.KeySecurityType.Server);
//            // 创建一个微服务身份；
//            ZKSecDefaultServerPrincipal<String> sP = new ZKSecDefaultServerPrincipal<String>(appName,
//                    ZKSecPrincipal.OS_TYPE.UNKNOWN, appName, ZKSecPrincipal.KeyType.Distributed_server, appName);
//            sP.setPrimary(true);
//
//            sPc.add("distributedRealm", sP);
//            log.info("[^_^:20220609-0925-001] 服务[{}]请求其他微服务，创建微服务身份", this.appName);
//        }
//        return sTk;
//    }

}


