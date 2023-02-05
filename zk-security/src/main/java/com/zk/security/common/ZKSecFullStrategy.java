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
* @Title: ZKSecFullStrategy.java 
* @author Vinson 
* @Package com.zk.security.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 11:48:18 PM 
* @version V1.0 
*/
package com.zk.security.common;

import java.util.Set;

import com.zk.security.principal.pc.ZKSecDefaultPrincipalCollection;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.realm.ZKSecRealm;
import com.zk.security.token.ZKSecAuthenticationToken;

/** 
* @ClassName: ZKSecFullStrategy 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecFullStrategy implements ZKSecStrategy {

    /**
     * 认证策略
     * 
     * 此策略为所有认证通过，并返回所有身份
     */
    @Override
    public <ID> ZKSecPrincipalCollection<ID> doStrategyAuthentication(Set<ZKSecRealm> realmSet,
            ZKSecAuthenticationToken token) {
        // TODO Auto-generated method stub
        ZKSecPrincipalCollection<ID> pc = new ZKSecDefaultPrincipalCollection<ID>();
        for (ZKSecRealm realm : realmSet) {
            ZKSecPrincipalCollection<ID> tPc = realm.authentication(token);
            if (tPc != null && !tPc.isEmpty()) {
                pc.addAll(tPc);
            }
        }
        return pc.size() > 0 ? pc : null;
    }

    /**
     * 用户在线数量控制策略
     * 
     * 此策略为所有身份在对应域在线数量都满足要求，方可通过
     * 
     */
    @Override
    public <ID> void doStrategyPrincipalsCount(Set<ZKSecRealm> realmSet, ZKSecPrincipalCollection<ID> pc) {
        if (pc != null) {
            for (ZKSecRealm realm : realmSet) {
                realm.doLimitPrincipalTicketCount(pc);
            }
        }
    }

}
