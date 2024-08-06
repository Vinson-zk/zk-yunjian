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
* @Title: ZKSecStrategy.java 
* @author Vinson 
* @Package com.zk.security.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 8:00:28 PM 
* @version V1.0 
*/
package com.zk.security.common;

import java.util.Set;

import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.realm.ZKSecRealm;
import com.zk.security.token.ZKSecAuthenticationToken;

/** 
* @ClassName: ZKSecStrategy 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecStrategy {

    /**
     * 认证策略
     */
    <ID> ZKSecPrincipalCollection<ID> doStrategyAuthentication(Set<ZKSecRealm> realmSet,
            ZKSecAuthenticationToken token);

    /**
     * 用户在线数量控制策略
     */
    <ID> void doStrategyPrincipalsCount(Set<ZKSecRealm> realmSet, ZKSecPrincipalCollection<ID> pc);
}
