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

import com.zk.core.utils.ZKStringUtils;

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
}
