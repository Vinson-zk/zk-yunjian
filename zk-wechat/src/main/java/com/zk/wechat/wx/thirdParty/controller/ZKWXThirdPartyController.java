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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.web.ZKMsgRes;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.wechat.wx.thirdParty.ZKWXThirdPartyConstants.ConfigKey;
import com.zk.wechat.wx.thirdParty.service.ZKWXThirdPartyAuthService;
import com.zk.wechat.wx.thirdParty.service.ZKWXThirdPartyMsgService;

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
    public String authNotification(@PathVariable("thirdPartyAppId")String thirdPartyAppId, HttpServletRequest req) {

        // 授权码，过期时间就不用取了，取到授权码，马上取已授权的 “目标微信公众号或目标小程序” 的信息
        try {
            // 1、从请求流中读出字节
            byte[] msgBytes = ZKWebUtils.getBytesByRequest(req);
            // 2、密文
            String encStr = new String(msgBytes, Charset.forName("UTF-8"));
            // 3、处理消息密文
            wxThirdPartyMsgService.authMsgNotification(thirdPartyAppId, encStr);
        }
        catch(Exception e) {
            e.printStackTrace();
            log.error("[>_<:20180906-1948-001] 接收取消授权通知、授权成功通知、授权更新通知、ticket 时出错了！appId:{} ", thirdPartyAppId);
        }
        return "success";
    }
    
    /**
     * 目标授权账号授权申请；
     *
     * @Title: auth
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 8:55:56 AM
     * @param bizAppid
     *            授权方原始ID
     * @param byeType
     *            授权方式；0-网页登录授权；1-移动设备打开链接授权；
     * @param req
     * @param res
     * @return ZKMsgRes
     */
    @RequestMapping(value = "auth", method = RequestMethod.POST)
    @ResponseBody
    public ZKMsgRes auth(@RequestParam(value = "bizAppid", required = false)String bizAppid, 
            @RequestParam(value = "byeType", required = false, defaultValue = "0")int byeType, 
            HttpServletRequest req, HttpServletResponse res) {

        String authUrl = this.wxThirdPartyAuthService.genAuthUrl(byeType, bizAppid, "", "");

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
     * @param req
     * @return
     * @return String
     */
    @RequestMapping(value = "authCallBack/{thirdPartyAppid}")
    // @ResponseBody
    public String authCallBack(@PathVariable(name = "thirdPartyAppid") String thirdPartyAppid, HttpServletRequest req) {

        // 授权码，过期时间就不用取了，取到授权码，马上取已授权的 “目标微信公众号或目标小程序” 的信息
//        String authCode = WebUtils.getCleanParam(req, WXConstants.WxMsgAttribute.AuthorizationRedirect.auth_code);
//        this.thirdAuthService.authCallBack(companyCode, authCode);
        return "redirect:" + ZKEnvironmentUtils.getString(ConfigKey.zkWechatDomainName, "/");
    }
    
    /**
     * 消息与事件接收URL
     *
     * @Title: eventNotification
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 4, 2021 5:11:02 PM
     * @param APPID
     * @param req
     * @return
     * @return String
     */
    @RequestMapping(value = "event/{APPID}")
    @ResponseBody
    public String eventNotification(@PathVariable("APPID")
    String APPID, HttpServletRequest req) {

        // 授权码，过期时间就不用取了，取到授权码，马上取已授权的 “目标微信公众号或目标小程序” 的信息
        try {
//            // 1、从请求流中读出字节
//            byte[] msgBytes = ZKWebUtils.getBytesByRequest(req);
//            // 2、密文
//            String encStr = new String(msgBytes, Charset.forName("UTF-8"));
//            // 3、处理消息密文
//            wxThirdPartyMsgService.authNotification(thirdPartyAppId, encStr);
        }
        catch(Exception e) {
            e.printStackTrace();
//            log.error("[>_<:20180906-1948-001] 接收取消授权通知、授权成功通知、授权更新通知、ticket 时出错了！appId:{} ", thirdPartyAppId);
        }
        return "success";
    }

}