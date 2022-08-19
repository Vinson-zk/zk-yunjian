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
* @Title: ZKWXThirdPartyMsgService.java 
* @author Vinson 
* @Package com.zk.wechat.wx.thirdParty.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 18, 2021 10:37:24 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.thirdParty.service;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zk.core.exception.ZKCodeException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.wechat.thirdParty.entity.ZKThirdParty;
import com.zk.wechat.thirdParty.service.ZKThirdPartyService;
import com.zk.wechat.wx.aes.ZKAesException;
import com.zk.wechat.wx.thirdParty.ZKWXThirdPartyConstants;
import com.zk.wechat.wx.thirdParty.ZKWXThirdPartyConstants.MsgAttr;
import com.zk.wechat.wx.thirdParty.ZKWXThirdPartyInfoType;
import com.zk.wechat.wx.thirdParty.ZKWXThirdPartyMsgUtils;

/** 
* @ClassName: ZKWXThirdPartyMsgService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
//@Transactional(readOnly = true)
public class ZKWXThirdPartyMsgService {

    /**
     * 日志对象
     */
    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    ZKThirdPartyService thirdPartyService;

    @Autowired
    ZKWXThirdPartyAuthService thirdPartyAuthService;

    /**
     * 授权消息
     *
     * @Title: authMsgNotification
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 8:58:50 AM
     * @param thirdPartyAppId
     * @param encStr
     * @throws ZKAesException
     * @return void
     */
    public void authMsgNotification(String thirdPartyAppId, String encStr) throws ZKAesException {
        ZKThirdParty wxThirdPart = thirdPartyService.get(thirdPartyAppId);
        if (wxThirdPart == null) {
            log.error("[>_<:20210218-1025-001] 此微信第三方平台 thirdPartyAppId:{} ; 尚未与系统对接。", thirdPartyAppId);
            throw new ZKCodeException("zk.wechat.010001", "未对接第三方微信平台");
        }

        // 解密 xml 密文
        Document notificationDoc = ZKWXThirdPartyMsgUtils.getDocumentMsg(wxThirdPart.getWxToken(),
                wxThirdPart.getWxAesKey(), wxThirdPart.getPkId(), encStr);

        String infoTypeStr = notificationDoc.getRootElement().element(ZKWXThirdPartyConstants.MsgAttr.info_type)
                .getStringValue();

        ZKWXThirdPartyInfoType infoType = ZKWXThirdPartyInfoType.valueOf(infoTypeStr);
        switch (infoType) {
            case component_verify_ticket:
                // 收到微信平台推送过来的令牌；
                verifyTicketDispose(notificationDoc.getRootElement());
                break;
            case authorized:
                // 授权成功
                authorizedDispose(notificationDoc.getRootElement());
                break;
            case unauthorized:
                // 授权取消
                unauthorizedDispose(notificationDoc.getRootElement());
                break;
            case updateauthorized:
                // 授权变更
                updateauthorizedDispose(notificationDoc.getRootElement());
                break;
            default:
                log.error("[>_<:20180906-2025-001] 授权通知中，未知的消息类型！thirdPartyAppId:{}, infoType:{}", thirdPartyAppId,
                        infoType.getInfoType());
                break;
        }
    }

    // 第三方平台接收令牌处理
    public void verifyTicketDispose(Element rootElement) {
        String appId = "";
        try {
            String componentVerifyTicket = rootElement
                    .element(ZKWXThirdPartyConstants.MsgAttr.ComponentVerifyTicket.ComponentVerifyTicket)
                    .getStringValue();
            if (ZKStringUtils.isEmpty(componentVerifyTicket)) {
                throw new ZKCodeException("zk.wechat.010002", "微信平台推送的令牌为空");
            }
            appId = rootElement.element(ZKWXThirdPartyConstants.MsgAttr.ComponentVerifyTicket.AppId)
                    .getStringValue();
            if (ZKStringUtils.isEmpty(appId)) {
                throw new ZKCodeException("zk.wechat.010003", "微信平台推送的令牌时APPID为空");
            }
            // 存储 第三方APPID 的 令牌。
            log.info("[^_^:20180907-0829-001] 微信平台向第三方平台发送的令牌；appid:{}; componentVerifyTicket:{}", appId,
                    componentVerifyTicket);
            thirdPartyService.updateTicket(appId, componentVerifyTicket);
        }
        catch(Exception e) {
            e.printStackTrace();
            log.error("[>_<:20180907-0747-001] 处理微信开放平台向第三方开发推送的信息异常！{}", rootElement.asXML());
            throw new ZKCodeException("zk.wechat.010004", "处理微信开放平台向第三方开发者推送的令牌信息异常", new String[] { appId },
                    e.getMessage());
        }
    }

    /**
     * 授权成功
     *
     * @Title: thirdTicketDispose
     * @Description: TODO(simple description this method what to do.)
     * @author bs
     * @date 2018年9月7日 上午8:32:40
     * @param notificationDoc
     *            消息体的 xml
     * @return void
     */
    public void authorizedDispose(Element rootElement) {
        try {
            // <xml>
            // <AppId>第三方平台appid</AppId>
            // <CreateTime>1413192760</CreateTime>
            // <InfoType>authorized</InfoType>
            // <AuthorizerAppid>公众号appid</AuthorizerAppid>
            // <AuthorizationCode>授权码（code）</AuthorizationCode>
            // <AuthorizationCodeExpiredTime>过期时间</AuthorizationCodeExpiredTime>
            // <PreAuthCode>预授权码</PreAuthCode>
            // <xml>
            String thirdPartyAppid = rootElement.element(MsgAttr.AuthorizedInfo.AppId).getStringValue();
            String authorizerAppid = rootElement.element(MsgAttr.AuthorizedInfo.AuthorizerAppid).getStringValue();
            String authorizationCode = rootElement.element(MsgAttr.AuthorizedInfo.AuthorizationCode).getStringValue();
            String preAuthCode = rootElement.element(MsgAttr.AuthorizedInfo.PreAuthCode).getStringValue();

            this.thirdPartyAuthService.authAuthorizedAndUpdateauthorized(thirdPartyAppid, authorizerAppid,
                    authorizationCode, preAuthCode);
        }
        catch(Exception e) {
            e.printStackTrace();
            log.error("[>_<:20180913-1704-001] 处理公众号授权成功消息异常！{}", rootElement.asXML());
            throw new ZKCodeException("zk.wechat.010010", "处理公众号授权成功消息异常", e.getMessage());
        }
    }

    /**
     * 目标授权账号 取消授权
     *
     * @Title: thirdTicketDispose
     * @Description: TODO(simple description this method what to do.)
     * @author bs
     * @date 2018年9月7日 上午8:32:40
     * @param notificationDoc
     *            消息体的 xml
     * @return void
     */
    public void unauthorizedDispose(Element rootElement) {
        try {

            // <xml>
            // <AppId>第三方平台appid</AppId>
            // <CreateTime>1413192760</CreateTime>
            // <InfoType>unauthorized</InfoType>
            // <AuthorizerAppid>公众号appid</AuthorizerAppid>
            // </xml>

            String thirdPartyAppid = rootElement.element(MsgAttr.AuthorizedInfo.AppId).getStringValue();
            String authorizerAppid = rootElement.element(MsgAttr.AuthorizedInfo.AuthorizerAppid).getStringValue();
            this.thirdPartyAuthService.authUnauthorized(thirdPartyAppid, authorizerAppid);

        }
        catch(Exception e) {
            e.printStackTrace();
            log.error("[>_<:20180913-1704-002] 处理公众号授权取消消息异常！{}", rootElement.asXML());
            throw new ZKCodeException("zk.wechat.010009", "处理公众号授权取消消息异常", e.getMessage());
        }
    }

    /**
     * 授权变更
     *
     * @Title: thirdTicketDispose
     * @Description: TODO(simple description this method what to do.)
     * @author bs
     * @date 2018年9月7日 上午8:32:40
     * @param notificationDoc
     *            消息体的 xml
     * @return void
     */
    public void updateauthorizedDispose(Element rootElement) {
        try {
            // <xml>
            // <AppId>第三方平台appid</AppId>
            // <CreateTime>1413192760</CreateTime>
            // <InfoType>updateauthorized</InfoType>
            // <AuthorizerAppid>公众号appid</AuthorizerAppid>
            // <AuthorizationCode>授权码（code）</AuthorizationCode>
            // <AuthorizationCodeExpiredTime>过期时间</AuthorizationCodeExpiredTime>
            // <PreAuthCode>预授权码</PreAuthCode>
            // <xml>

            String thirdPartyAppid = rootElement.element(MsgAttr.AuthorizedInfo.AppId).getStringValue();
            String authorizerAppid = rootElement.element(MsgAttr.AuthorizedInfo.AuthorizerAppid).getStringValue();
            String authorizationCode = rootElement.element(MsgAttr.AuthorizedInfo.AuthorizationCode).getStringValue();
            String preAuthCode = rootElement.element(MsgAttr.AuthorizedInfo.PreAuthCode).getStringValue();

            this.thirdPartyAuthService.authAuthorizedAndUpdateauthorized(thirdPartyAppid, authorizerAppid,
                    authorizationCode, preAuthCode);
        }
        catch(Exception e) {
            log.error("[>_<:20180913-1704-003] 处理公众号授权变更消息异常！{}", rootElement.asXML());
            e.printStackTrace();
            throw new ZKCodeException("zk.wechat.010008", "处理公众号授权变更消息异常", e.getMessage());
        }
    }

}
