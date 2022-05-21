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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zk.core.exception.ZKCodeException;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.ZKMsgRes;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.wechat.thirdParty.entity.ZKThirdParty;
import com.zk.wechat.thirdParty.service.ZKThirdPartyService;
import com.zk.wechat.wx.officialAccounts.service.ZKWXOfficialAccountsNotificationService;
import com.zk.wechat.wx.thirdParty.ZKWXThirdPartyConstants.ConfigKey;
import com.zk.wechat.wx.thirdParty.service.ZKWXThirdPartyAuthService;
import com.zk.wechat.wx.thirdParty.service.ZKWXThirdPartyMsgService;
import com.zk.wechat.wx.thirdParty.service.ZKWXThirdPartyService;
import com.zk.wechat.wx.utils.ZKWXMsgUtils;

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
    protected Logger log = LoggerFactory.getLogger(getClass());

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
     * @return
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

        ZKMsgRes resMsg = ZKMsgRes.asOk(authUrl);
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
     * @return
     * @return String
     */
    @RequestMapping(value = "authReceive/{thirdPartyAppId}")
    @ResponseBody
    public String authNotification(@PathVariable("thirdPartyAppId") String thirdPartyAppId, HttpServletRequest req) {

        // 授权码，过期时间就不用取了，取到授权码，马上取已授权的 “目标微信公众号或目标小程序” 的信息
        try {
            // 1、从请求流中读出字节
            byte[] msgBytes = ZKWebUtils.getBytesByRequest(req);
            // 2、密文
            String encStr = new String(msgBytes, Charset.forName("UTF-8"));
            // 3、解密 xml 消息密文
            ZKThirdParty wxThirdPart = thirdPartyService.get(thirdPartyAppId);
            if (wxThirdPart == null) {
                log.error("[>_<:20210218-1025-001] 此微信第三方平台 thirdPartyAppId:{} ; 尚未与系统对接。", thirdPartyAppId);
                throw new ZKCodeException("zk.wechat.010001", "未对接第三方微信平台");
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
     * @return
     * @return String
     */
    @RequestMapping(value = "event/{thirdPartyAppId}/{appId}")
    @ResponseBody
    public String eventNotification(@PathVariable("thirdPartyAppId") String thirdPartyAppId,
            @PathVariable("appId") String appId, HttpServletRequest req) {

        // 授权码，过期时间就不用取了，取到授权码，马上取已授权的 “目标微信公众号或目标小程序” 的信息
        try {
            // 1、从请求流中读出字节
            byte[] msgBytes = ZKWebUtils.getBytesByRequest(req);
            // 2、密文
            String encStr = new String(msgBytes, Charset.forName("UTF-8"));
            // 3、解密 xml 消息密文
            ZKThirdParty wxThirdPart = thirdPartyService.get(thirdPartyAppId);
            if (wxThirdPart == null) {
                log.error("[>_<:20210219-0055-001] 此微信第三方平台 thirdPartyAppId:{} ; 尚未与系统对接。", thirdPartyAppId);
                throw new ZKCodeException("zk.wechat.010001", "未对接第三方微信平台");
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
     * @return
     * @return String
     */
    @RequestMapping(value = "authUserReceive/{thirdPartyAppId}/{funcKey}")
    public String authUserReceive(@PathVariable("thirdPartyAppId") String thirdPartyAppId,
            @PathVariable("funcKey") String funcKey,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "appid", required = false) String appid, HttpServletRequest req) {

        if (ZKStringUtils.isEmpty(code)) {
            // 用户禁止授权；需要转发到未授权的错误页面
            return "redirect:" + "error/wxuser.noAuth";
        }

        try {
            // 取用户 accessToken
            this.wxThirdPartyService.getUserAccessTokenByAuthCode(thirdPartyAppId, funcKey, code, state, appid);
        }
        catch(Exception e) {
            e.printStackTrace();
            log.error("[>_<:20220519-2357-002] 接收用户授权消息失败；thirdPartyAppId:{}, appid:{}, funcKey:{} ", thirdPartyAppId,
                    appid, funcKey);
        }
        return "success";
    }

}


