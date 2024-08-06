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
* @Title: ZKWXOfficialAccountsNotificationService.java 
* @author Vinson 
* @Package com.zk.wechat.wx.officialAccounts.service 
* @Description: TODO(simple description this file what to do. ) 
* @date May 19, 2022 12:52:47 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.officialAccounts.service;

import org.dom4j.Element;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zk.wechat.wx.aes.ZKAesException;
import com.zk.wechat.wx.officialAccounts.ZKWXOfficialAccountsConstants.MsgAttr;
import com.zk.wechat.wx.officialAccounts.ZKWXOfficialAccountsMsgType;
import com.zk.wechat.wx.officialAccounts.msgBean.ZKWXAccountAuthAccessToken;

/**
 * 公众号和小程序的 事件和消息的通知处理
 * 
 * @ClassName: ZKWXOfficialAccountsNotificationService
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Service
//@Transactional(readOnly = true)
public class ZKWXOfficialAccountsNotificationService {

    @FunctionalInterface
    public static interface GetWXAccountAuthAccessToken {
        ZKWXAccountAuthAccessToken getZKWXAccountAuthAccessToken();
    }

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    @Autowired
    ZKWXOfficialAccountsEventService wxOfficialAccountsEventService;

    // 公众号实现的消息和事件通知
    public void notification(String thirdPartyAppId, String appId, Element rootElement,
            GetWXAccountAuthAccessToken funcGetAccessToken)
            throws ZKAesException {

        String msgTypeStr = rootElement.element(MsgAttr.MsgType)
                .getStringValue();

        ZKWXOfficialAccountsMsgType msgType = ZKWXOfficialAccountsMsgType.valueOf(msgTypeStr);
        switch (msgType) {
            case event:
                // 事件
                wxOfficialAccountsEventService.disposeEvent(thirdPartyAppId, appId, rootElement, funcGetAccessToken);
                break;
            case location:
                // 地理位置消息
                break;
//            case text:
//                // 文本消息
//                break;
//            case image:
//                // 图片消息
//                break;
//            case link:
//                // 链接消息
//                break;
//            case voice:
//                // 语音消息
//                break;
//            case video:
//                // 视频消息
//                break;
//            case shortvideo:
//                // 小视频消息
//                break;
            default:
                log.error("[>_<:20220519-0101-001] 未知的公众号或小程序通知类型！thirdPartyAppId:{}, appId:{}, MsgType: {}",
                        thirdPartyAppId, appId, msgTypeStr);
                break;
        }
    }

}
