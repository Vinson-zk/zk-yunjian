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
* @Title: ZKWXOfficialAccountsMsgEventType.java 
* @author Vinson 
* @Package com.zk.wechat.wx.officialAccounts 
* @Description: TODO(simple description this file what to do. ) 
* @date May 19, 2022 12:40:40 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.officialAccounts;

/**
 * @ClassName: ZKWXOfficialAccountsMsgEventType
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public enum ZKWXOfficialAccountsMsgEventType {

    /**
     * 用户关注(订阅)时的事件推送
     */
    subscribe("subscribe"),
    /**
     * 用户取消关注(取消订阅)时的事件推送
     */
    unsubscribe("unsubscribe"),
    /**
     * 用户已关注时的事件推送
     */
    SCAN("SCAN"),
    /**
     * 点击菜单拉取消息时的事件推送
     */
    CLICK("CLICK"),
    /**
     * 点击菜单跳转链接时的事件推送
     */
    VIEW("VIEW"),
    /**
     * 上报地理位置事件;用户上报地理位置事件消息，即用户上报 GPS 事件消息
     */
    LOCATION("LOCATION");

    private String type;

    ZKWXOfficialAccountsMsgEventType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
