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
* @Title: ZKWXOfficialAccountsEventService.java 
* @author Vinson 
* @Package com.zk.wechat.wx.officialAccounts.service 
* @Description: TODO(simple description this file what to do. ) 
* @date May 19, 2022 1:05:33 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.officialAccounts.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zk.core.exception.base.ZKCodeException;
import com.zk.core.utils.ZKDateUtils;
import com.zk.wechat.officialAccounts.entity.ZKOfficialAccountsUser;
import com.zk.wechat.officialAccounts.service.ZKOfficialAccountsUserService;
import com.zk.wechat.wx.officialAccounts.ZKWXOfficialAccountsConstants.MsgAttr;
import com.zk.wechat.wx.officialAccounts.ZKWXOfficialAccountsMsgEventType;
import com.zk.wechat.wx.officialAccounts.service.ZKWXOfficialAccountsNotificationService.GetWXAccountAuthAccessToken;

/**
 * @ClassName: ZKWXOfficialAccountsEventService
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Service
public class ZKWXOfficialAccountsEventService {
    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    @Autowired
    ZKWXOfficialAccountsUserService wxOfficialAccountsUserService;

    @Autowired
    ZKOfficialAccountsUserService officialAccountsUserService;

    /**
     * 处理公众号或小程序 上报的事件
     *
     * @Title: disposeEvent
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 20, 2022 9:33:12 AM
     * @param thirdPartyAppId
     * @param appId
     * @param rootElement
     * @param funcGetAccessToken
     * @return void
     */
    public void disposeEvent(String thirdPartyAppId, String appId, Element rootElement,
            GetWXAccountAuthAccessToken funcGetAccessToken) {
        String msgEventStr = rootElement.element(MsgAttr.Event)
                .getStringValue();

        ZKWXOfficialAccountsMsgEventType msgEvent = ZKWXOfficialAccountsMsgEventType.valueOf(msgEventStr);
        switch (msgEvent) {
            case subscribe:
                // 用户关注(订阅)时的事件推送
                this.eventSubscribe(thirdPartyAppId, appId, rootElement, funcGetAccessToken);
                break;
            case unsubscribe:
                // 用户取消关注(取消订阅)时的事件推送
                this.eventUnsubscribe(thirdPartyAppId, appId, rootElement, funcGetAccessToken);
                break;
//            case SCAN:
//                // 扫描带参数二维码事件: 用户已关注时的事件推送
//                break;
//            case LOCATION:
//                // 上报地理位置事件;用户上报地理位置事件消息，即用户上报 GPS 事件消息
//                break;
//            case VIEW:
//                // 点击菜单跳转链接时的事件推送
//                break;
//            case CLICK:
//                // 点击菜单拉取消息时的事件推送
//                break;
            default:
                log.error("[>_<:20220519-0101-002] 未知的公众号或小程序 上报的事件类型！thirdPartyAppId:{}, appId:{}, msgEvent: {}",
                        thirdPartyAppId, appId, msgEventStr);
                break;
        }
    }

    // 关注关注事件，含 扫描带参数二维码事件： 用户未关注时，进行关注后的事件推送
    public void eventSubscribe(String thirdPartyAppId, String appId, Element rootElement,
            GetWXAccountAuthAccessToken funcGetAccessToken) {
//        // 开发者微信号
//        String toUserName = rootElement.element(MsgAttr.ToUserName).getStringValue();
        // openId
        String fromUserName = rootElement.element(MsgAttr.FromUserName).getStringValue();
        // 消息创建时间 （整型）
        String createTime = rootElement.element(MsgAttr.CreateTime).getStringValue();
//        // 事件 KEY 值，qrscene_为前缀，后面为二维码的参数值
//        String eventKey = rootElement.element(MsgAttr.EventKey).getStringValue();
//        // 二维码的ticket，可用来换取二维码图片
//        String ticket = rootElement.element(MsgAttr.Ticket).getStringValue();
        
//        ZKOfficialAccountsUser officialAccountsUser = this.wxOfficialAccountsUserService.getUserInfo(thirdPartyAppId,
//                appId, fromUserName, funcGetAccessToken.getZKWXAccountAuthAccessToken().getAccessToken());

        String accessTokenStr = funcGetAccessToken.getZKWXAccountAuthAccessToken().getAccessToken();
        ZKOfficialAccountsUser officialAccountsUser = this.officialAccountsUserService.getByOpenId(thirdPartyAppId,
                appId, fromUserName);
        if (officialAccountsUser == null) {
            officialAccountsUser = new ZKOfficialAccountsUser();
            // 设置为公众号关注时创建
            officialAccountsUser.setWxChannel(ZKOfficialAccountsUser.KeyWxCannel.officialAccount);
        }
        try {
            this.wxOfficialAccountsUserService.getUserUnionID(officialAccountsUser, thirdPartyAppId, appId,
                    fromUserName, accessTokenStr);
//            this.wxOfficialAccountsUserService.getUserBaseInfo(officialAccountsUser, thirdPartyAppId, appId,
//                    fromUserName, accessTokenStr);
        }
        catch(ZKCodeException e) {
            log.error("[>_<:20220523-1428-001] 用户关注公众号事件，取用户信息失败；thirdPartyAppId：{}，appId：{}，openid:{}",
                    thirdPartyAppId, appId, fromUserName);
            e.printStackTrace();
            officialAccountsUser.setWxOpenid(fromUserName);
            this.wxOfficialAccountsUserService.putUserInfo(thirdPartyAppId, appId, officialAccountsUser);
        }

        officialAccountsUser.setWxSubscribeTimeStr(createTime);
        officialAccountsUser.setWxSubscribeDate(ZKDateUtils.parseDate(Long.valueOf(createTime) * 1000));
        this.officialAccountsUserService.save(officialAccountsUser);
    }

    // 取消关注事件
    public void eventUnsubscribe(String thirdPartyAppId, String appId, Element rootElement,
            GetWXAccountAuthAccessToken funcGetAccessToken) {
//        // 开发者微信号
//        String toUserName = rootElement.element(MsgAttr.ToUserName).getStringValue();
        // openId
        String fromUserName = rootElement.element(MsgAttr.FromUserName).getStringValue();
//        // 消息创建时间 （整型）
//        String createTime = rootElement.element(MsgAttr.CreateTime).getStringValue();
        ZKOfficialAccountsUser officialAccountsUser = this.officialAccountsUserService.getByOpenId(thirdPartyAppId,
                appId, fromUserName);
        if (officialAccountsUser != null) {
            officialAccountsUser.setWxSubscribe(ZKOfficialAccountsUser.KeyWxSubscribe.unsubscribe);
            this.officialAccountsUserService.save(officialAccountsUser);
        }
        else {
            log.error("[>_<:20220520-1241-001] 用户不存在，未关注公众号或在平台接入前已关注时取消关注；thirdPartyAppId：{}，appId：{}，openid:{}",
                    thirdPartyAppId, appId, fromUserName);
        }
    }

//    // 扫描带参数二维码事件: 用户已关注时的事件推送
//    public void eventScan(String thirdPartyAppId, String appId, Element rootElement,
//            GetWXAccountAuthAccessToken funcGetAccessToken) {
//        // 开发者微信号
//        String toUserName = rootElement.element(MsgAttr.ToUserName).getStringValue();
//        // openId
//        String fromUserName = rootElement.element(MsgAttr.FromUserName).getStringValue();
//        // 消息创建时间 （整型）
//        String createTime = rootElement.element(MsgAttr.CreateTime).getStringValue();
//        // 事件 KEY 值，是一个32位无符号整数，即创建二维码时的二维码scene_id
//        String eventKey = rootElement.element(MsgAttr.EventKey).getStringValue();
//        // 二维码的ticket，可用来换取二维码图片
//        String ticket = rootElement.element(MsgAttr.Ticket).getStringValue();
//        
//    }

}
