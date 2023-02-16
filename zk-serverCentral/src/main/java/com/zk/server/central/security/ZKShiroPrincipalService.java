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
* @Title: ZKShiroPrincipalService.java 
* @author Vinson 
* @Package com.zk.server.central.security 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 11, 2023 11:02:32 AM 
* @version V1.0 
*/
package com.zk.server.central.security;

import com.zk.security.principal.ZKSecDevPrincipal;
import com.zk.security.principal.ZKSecPrincipal;
import com.zk.security.principal.ZKSecUserPrincipal;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.service.ZKSecPrincipalService;
import com.zk.security.ticket.ZKSecTicket;

/** 
* @ClassName: ZKShiroPrincipalService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKShiroPrincipalService implements ZKSecPrincipalService {

    @Override
    public <ID> ZKSecUserPrincipal<ID> getUserPrincipal(ZKSecTicket tk) {
        return null;
    }

    @Override
    public <ID> ZKSecUserPrincipal<ID> getUserPrincipal() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <ID> ID getUserId() {
        return (ID) "0";
    }

    @Override
    public ZKSecTicket getTikcet() {
        return null;
    }

    @Override
    public <ID> ZKSecPrincipal<ID> getServerPrincipal(ZKSecTicket tk) {
        return null;
    }

    @Override
    public <ID> ZKSecPrincipalCollection<ID> getPrincipalCollection() {
        return null;
    }

    @Override
    public <ID> ZKSecPrincipal<ID> getPrincipalByType(ZKSecTicket tk, int type) {
        return null;
    }

    @Override
    public String getGroupCode() {
        return null;
    }

    @Override
    public <ID> ZKSecDevPrincipal<ID> getDevPrincipal(ZKSecTicket tk) {
        return null;
    }

    @Override
    public <ID> ID getCompanyId() {
//        SecurityUtils.getSubject().getPrincipal()
        return null;
    }

    @Override
    public String getCompanyCode() {
        return null;
    }

    @Override
    public <ID> ZKSecDevPrincipal<ID> getAppPrincipal() {
        return null;
    }

}
