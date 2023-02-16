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
* @Title: ZKWXOfficialAccountMsgType.java 
* @author Vinson 
* @Package com.zk.wechat.wx.officialAccounts 
* @Description: TODO(simple description this file what to do. ) 
* @date May 19, 2022 12:38:33 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.officialAccounts;

/**
 * @ClassName: ZKWXOfficialAccountsMsgType
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public enum ZKWXOfficialAccountsMsgType {

    /**
     * 事件
     */
    event("event"),
    /**
     * 文本消息
     */
    text("text"),
    /**
     * 图片消息
     */
    image("image"),
    /**
     * 语音消息
     */
    voice("voice"),
    /**
     * 视频消息
     */
    video("video"),
    /**
     * 小视频消息
     */
    shortvideo("shortvideo"),
    /**
     * 地理位置消息
     */
    location("location"),
    /**
     * 链接消息
     */
    link("link");

    private String type;

    ZKWXOfficialAccountsMsgType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
