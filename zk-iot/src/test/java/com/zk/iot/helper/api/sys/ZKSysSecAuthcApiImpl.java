/** 
* Copyright (c) 2012-2025 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSysSecAuthcApiImpl.java 
* @author Vinson 
* @Package com.zk.iot.helper.api.sec 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 3, 2025 5:35:31 PM 
* @version V1.0 
*/
package com.zk.iot.helper.api.sys;

import com.zk.core.commons.ZKMsgRes;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.security.authz.ZKSecSimpleAuthorizationInfo;
import com.zk.sys.sec.api.ZKSysSecAuthcApi;

/** 
* @ClassName: ZKSysSecAuthcApiImpl 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSysSecAuthcApiImpl implements ZKSysSecAuthcApi {

    @Override
    public ZKMsgRes getUserAuthc(String systemCode) {
        ZKSecSimpleAuthorizationInfo zkAuthInfo = new ZKSecSimpleAuthorizationInfo();

        return ZKMsgRes.asOk(ZKJsonUtils.toJsonStr(zkAuthInfo));
    }

}
