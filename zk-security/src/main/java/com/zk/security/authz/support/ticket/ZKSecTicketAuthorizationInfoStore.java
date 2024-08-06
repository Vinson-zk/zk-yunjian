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
* @Title: ZKSecTicketAuthorizationInfoStore.java 
* @author Vinson 
* @Package com.zk.security.authz.support.ticket 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 3, 2021 12:37:38 PM 
* @version V1.0 
*/
package com.zk.security.authz.support.ticket;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.security.authz.ZKSecAuthorizationInfo;
import com.zk.security.authz.ZKSecAuthorizationInfoStore;
import com.zk.security.ticket.ZKSecTicket;

/**
 * 将权限信息存放令牌中，权限信息时效和令牌相同；
 * 
 * @ClassName: ZKSecTicketAuthorizationInfoStore
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKSecTicketAuthorizationInfoStore implements ZKSecAuthorizationInfoStore {

    protected Logger logger = LogManager.getLogger(this.getClass());

    public static interface AttrKeyName {
        /**
         * 权限信息数据 key
         */
        public static final String AuthorizationInfo = "AuthorizationInfo";
    }

    @Override
    public ZKSecAuthorizationInfo getZKSecAuthorizationInfo(ZKSecTicket ticket, String realmName) {
        return ticket.get(AttrKeyName.AuthorizationInfo);
    }

    @Override
    public void setZKSecAuthorizationInfo(ZKSecTicket ticket, String realmName,
            ZKSecAuthorizationInfo authorizationInfo) {
        ticket.put(AttrKeyName.AuthorizationInfo, authorizationInfo);
    }

}
