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
* @Title: ZKSecAuthorizationInfoStore.java 
* @author Vinson 
* @Package com.zk.security.authz 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 3, 2021 8:29:14 AM 
* @version V1.0 
*/
package com.zk.security.authz;

import com.zk.security.ticket.ZKSecTicket;

/**
 * 权限存储，要注意存储的有效时间；
 * 
 * @ClassName: ZKSecAuthorizationInfoStore
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public interface ZKSecAuthorizationInfoStore {

    ZKSecAuthorizationInfo getZKSecAuthorizationInfo(ZKSecTicket ticket, String realmName);

    void setZKSecAuthorizationInfo(ZKSecTicket ticket, String realmName, ZKSecAuthorizationInfo authorizationInfo);

}
