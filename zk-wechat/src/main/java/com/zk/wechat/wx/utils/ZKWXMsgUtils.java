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
* @Title: ZKWXMsgUtils.java 
* @author Vinson 
* @Package com.zk.wechat.wx.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date May 19, 2022 12:24:58 AM 
* @version V1.0 
*/
package com.zk.wechat.wx.utils;

import org.dom4j.Document;

import com.zk.core.utils.ZKXmlUtils;
import com.zk.wechat.wx.aes.ZKAesException;

/**
 * 通用消息处理
 * 
 * @ClassName: ZKWXMsgUtils
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKWXMsgUtils {

    /**
     * 从消息 xml 密文中取出消息的解密结果字条串
     *
     * @Title: getStringMsg
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 18, 2021 3:37:15 PM
     * @param token
     *            微信平台配置的消息校验 token
     * @param aesKey
     *            微信消息加解密 key
     * @param appId
     *            微信第三方平台账号的 app id
     * @param msgXmlEncStr
     *            xml 密文
     * @throws ZKAesException
     * @return String
     */
    public static String getStringMsg(String token, String aesKey, String appId, String msgXmlEncStr)
            throws ZKAesException {
        // 解密
        return ZKWXUtils.decryptXml(token, aesKey, appId, msgXmlEncStr);

    }

    /**
     * 从消息 xml 密文中取出消息的解密结果 xml Document
     *
     * @Title: getDocumentMsg
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 18, 2021 3:35:17 PM
     * @param token
     *            微信平台配置的消息校验 token
     * @param aesKey
     *            微信消息加解密 key
     * @param appId
     *            微信第三方平台账号的 app id
     * @param msgXmlEncStr
     *            xml 密文
     * @throws ZKAesException
     * @return Document
     */
    public static Document getDocumentMsg(String token, String aesKey, String appId, String msgXmlEncStr)
            throws ZKAesException {
        // 转化为 Document
        return ZKXmlUtils.getDocument(getStringMsg(token, aesKey, appId, msgXmlEncStr));
    }

}
