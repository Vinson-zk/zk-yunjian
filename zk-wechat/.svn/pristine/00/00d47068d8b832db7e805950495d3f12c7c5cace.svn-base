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
* @Title: ZKWXThirdPartyMsgUtils.java 
* @author Vinson 
* @Package com.zk.wechat.wx.thirdParty 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 10, 2021 3:55:00 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.thirdParty;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;

import com.zk.core.utils.ZKStringUtils;
import com.zk.core.utils.ZKXmlUtils;
import com.zk.wechat.wx.aes.ZKAesException;
import com.zk.wechat.wx.common.ZKWXUtils;

/** 
* @ClassName: ZKWXThirdPartyMsgUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWXThirdPartyMsgUtils {
    
    /**
     * 解析授权方的权限
     */
    public static String getFunc(List<Map<String, Map<String, Integer>>> funcMaps, String separator) {

        if (funcMaps == null) {
            return null;
        }

        String func = "";
        Map<String, Integer> funcscopeCategoryMap = null;
        for (Map<String, Map<String, Integer>> funcMap : funcMaps) {
            funcscopeCategoryMap = null;
            funcscopeCategoryMap = funcMap
                    .get(ZKWXThirdPartyConstants.MsgAttr.AuthorizationInfo.FuncInfo._name);
            if (funcscopeCategoryMap == null) {
                continue;
            }
            if (funcscopeCategoryMap.get(
                    ZKWXThirdPartyConstants.MsgAttr.AuthorizationInfo.FuncInfo.FuncscopeCategory.id) != null) {
                func += funcscopeCategoryMap
                        .get(ZKWXThirdPartyConstants.MsgAttr.AuthorizationInfo.FuncInfo.FuncscopeCategory.id)
                        .toString() + separator;
            }

        }

        return ZKStringUtils.isEmpty(func) ? null : func.substring(0, func.length() - 1);
    }

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
    public static String getStringMsg(String token, String aesKey, String appId,
            String msgXmlEncStr) throws ZKAesException {
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
    public static Document getDocumentMsg(String token, String aesKey, String appId,
            String msgXmlEncStr) throws ZKAesException {
        // 转化为 Document
        return ZKXmlUtils.getDocument(getStringMsg(token, aesKey, appId, msgXmlEncStr));
    }

}
