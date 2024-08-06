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
* @Title: ZKSecDefaultSecurityManager.java 
* @author Vinson 
* @Package com.zk.security.mgt 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 27, 2021 12:02:04 AM 
* @version V1.0 
*/
package com.zk.security.mgt;

import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.realm.ZKSecRealm;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.token.ZKSecAuthenticationToken;

/** 
* @ClassName: ZKSecDefaultSecurityManager 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecDefaultSecurityManager extends ZKSecAbstractSecurityManager {

    /**
     * 认证，登录
     * 
     * @param token
     * @return
     */
    @Override
    public <ID> ZKSecPrincipalCollection<ID> doAuthentication(ZKSecSubject subject, ZKSecAuthenticationToken token) {
        ZKSecPrincipalCollection<ID> pc = this.getStrategy().doStrategyAuthentication(this.getRealmSet(), token);
        if (pc != null && !pc.isEmpty()) {
            // 登录成功
            return pc;
        }
        return null;
    }

    @Override
    public <ID> boolean doCheckPermission(ZKSecPrincipalCollection<ID> pc, String permissionCode) {
        boolean cp = false;
        for (ZKSecRealm realm : this.getRealmSet()) {
            if (pc != null && !pc.isEmpty()) {
                cp = realm.checkPermission(pc, permissionCode);
                if (cp) {
                    break;
                }
            }
        }
        return cp;
    }

    @Override
    public <ID> boolean doCheckApiCode(ZKSecPrincipalCollection<ID> pc, String apiCode) {
        boolean cp = false;
        for (ZKSecRealm realm : this.getRealmSet()) {
            if (pc != null && !pc.isEmpty()) {
                cp = realm.checkApiCode(pc, apiCode);
                if (cp) {
                    break;
                }
            }
        }
        return cp;
    }

    @Override
    public void onSetTicketToSubject(ZKSecTicket ticket, ZKSecSubject toSubject) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLogOut(ZKSecSubject subject) {
        // TODO Auto-generated method stub

    }

}
