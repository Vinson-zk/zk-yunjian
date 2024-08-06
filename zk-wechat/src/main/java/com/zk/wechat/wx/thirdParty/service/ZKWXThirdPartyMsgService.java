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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.wechat.thirdParty.service.ZKThirdPartyService;
import com.zk.wechat.wx.aes.ZKAesException;
import com.zk.wechat.wx.thirdParty.ZKWXThirdPartyConstants;
import com.zk.wechat.wx.thirdParty.ZKWXThirdPartyConstants.MsgAttr;
import com.zk.wechat.wx.thirdParty.ZKWXThirdPartyInfoType;

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
    protected Logger log = LogManager.getLogger(getClass());

    @Autowired
    ZKWXThirdPartyAuthService wxThirdPartyAuthService;

    @Autowired
    ZKThirdPartyService thirdPartyService;

    /**
     * 授权消息
     *
     * @Title: authMsgNotification
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 8:58:50 AM
     * @param thirdPartyAppId
     * @param rootElement
     * @throws ZKAesException
     * @return void
     */
    public void authMsgNotification(String thirdPartyAppId, Element rootElement) throws ZKAesException {

        String infoTypeStr = rootElement.element(ZKWXThirdPartyConstants.MsgAttr.info_type)
                .getStringValue();

        ZKWXThirdPartyInfoType infoType = ZKWXThirdPartyInfoType.valueOf(infoTypeStr);
        switch (infoType) {
            case component_verify_ticket:
                // 收到微信平台推送过来的令牌；
                verifyTicketDispose(rootElement);
                break;
            case authorized:
                // 授权成功
                authorizedDispose(rootElement);
                break;
            case unauthorized:
                // 授权取消
                unauthorizedDispose(rootElement);
                break;
            case updateauthorized:
                // 授权变更
                updateauthorizedDispose(rootElement);
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
                throw ZKBusinessException.as("zk.wechat.010002");
            }
            appId = rootElement.element(ZKWXThirdPartyConstants.MsgAttr.ComponentVerifyTicket.AppId)
                    .getStringValue();
            if (ZKStringUtils.isEmpty(appId)) {
                throw ZKBusinessException.as("zk.wechat.010003");
            }
            // 存储 第三方APPID 的 令牌。
            log.info("[^_^:20180907-0829-001] 微信平台向第三方平台发送的令牌；appid:{}; componentVerifyTicket:{}", appId,
                    componentVerifyTicket);
            thirdPartyService.updateTicket(appId, componentVerifyTicket);
        }
        catch(Exception e) {
            e.printStackTrace();
            log.error("[>_<:20180907-0747-001] 处理微信开放平台向第三方开发推送的信息异常！{}", rootElement.asXML());
            throw ZKBusinessException.as("zk.wechat.010004", e, appId);
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

            this.wxThirdPartyAuthService.authAuthorizedAndUpdateauthorized(thirdPartyAppid, authorizerAppid,
                    authorizationCode, preAuthCode);
        }
        catch(Exception e) {
            e.printStackTrace();
            log.error("[>_<:20180913-1704-001] 处理公众号授权成功消息异常！{}", rootElement.asXML());
            throw ZKBusinessException.as("zk.wechat.010010", e);
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
            this.wxThirdPartyAuthService.authUnauthorized(thirdPartyAppid, authorizerAppid);

        }
        catch(Exception e) {
            e.printStackTrace();
            log.error("[>_<:20180913-1704-002] 处理公众号授权取消消息异常！{}", rootElement.asXML());
            throw ZKBusinessException.as("zk.wechat.010009", e);
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

            this.wxThirdPartyAuthService.authAuthorizedAndUpdateauthorized(thirdPartyAppid, authorizerAppid,
                    authorizationCode, preAuthCode);
        }
        catch(Exception e) {
            log.error("[>_<:20180913-1704-003] 处理公众号授权变更消息异常！{}", rootElement.asXML());
            e.printStackTrace();
            throw ZKBusinessException.as("zk.wechat.010008", e);
        }
    }

}
