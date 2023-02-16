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
* @Title: ZKSecAuthorizationInfo.java 
* @author Vinson 
* @Package com.zk.security.authz 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 10, 2021 8:24:32 AM 
* @version V1.0 
*/
package com.zk.security.authz;

import java.io.Serializable;
import java.util.Collection;

/** 
* @ClassName: ZKSecAuthorizationInfo 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecAuthorizationInfo extends Serializable {

    // Api 接口代码
    Collection<String> getApiCodes();

    // 权限定义，权限代码
    Collection<String> getAuthCodes();

}
