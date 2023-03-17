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
* @Title: ZKSecPrincipalDefaultService.java 
* @author Vinson 
* @Package com.zk.security.service.support 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 2, 2023 7:56:21 PM 
* @version V1.0 
*/
package com.zk.security.service;

import com.zk.security.principal.ZKSecDevPrincipal;
import com.zk.security.principal.ZKSecPrincipal;
import com.zk.security.principal.ZKSecUserPrincipal;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.utils.ZKSecSecurityUtils;

/**
 * @ClassName: ZKSecPrincipalDefaultService
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKSecDefaultPrincipalService implements ZKSecPrincipalService {

    /**
     * (not Javadoc)
     * 
     * @param <ID>
     * @return
     * @see com.zk.security.service.ZKSecPrincipalService#getPrincipalCollection()
     */
    @Override
    public <ID> ZKSecPrincipalCollection<ID> getPrincipalCollection() {
        return ZKSecSecurityUtils.getPrincipalCollection();
    }

    /**
     * (not Javadoc)
     * 
     * @param <ID>
     * @return
     * @see com.zk.security.service.ZKSecPrincipalService#getUserPrincipal()
     */
    @Override
    public <ID> ZKSecUserPrincipal<ID> getUserPrincipal() {
        return ZKSecSecurityUtils.getUserPrincipal();
    }

    /**
     * (not Javadoc)
     * 
     * @param <ID>
     * @param tk
     * @return
     * @see com.zk.security.service.ZKSecPrincipalService#getUserPrincipal(com.zk.security.ticket.ZKSecTicket)
     */
    @Override
    public <ID> ZKSecUserPrincipal<ID> getUserPrincipal(ZKSecTicket tk) {
        return ZKSecSecurityUtils.getUserPrincipal(tk);
    }

    /**
     * (not Javadoc)
     * 
     * @param <ID>
     * @return
     * @see com.zk.security.service.ZKSecPrincipalService#getAppPrincipal()
     */
    @Override
    public <ID> ZKSecDevPrincipal<ID> getAppPrincipal() {
        return ZKSecSecurityUtils.getAppPrincipal();
    }

    /**
     * (not Javadoc)
     * 
     * @param <ID>
     * @param tk
     * @return
     * @see com.zk.security.service.ZKSecPrincipalService#getDevPrincipal(com.zk.security.ticket.ZKSecTicket)
     */
    @Override
    public <ID> ZKSecDevPrincipal<ID> getDevPrincipal(ZKSecTicket tk) {
        return ZKSecSecurityUtils.getDevPrincipal(tk);
    }

    /**
     * (not Javadoc)
     * 
     * @param <ID>
     * @param tk
     * @return
     * @see com.zk.security.service.ZKSecPrincipalService#getServerPrincipal(com.zk.security.ticket.ZKSecTicket)
     */
    @Override
    public <ID> ZKSecPrincipal<ID> getServerPrincipal(ZKSecTicket tk) {
        return ZKSecSecurityUtils.getServerPrincipal(tk);
    }

    /**
     * (not Javadoc)
     * 
     * @param <ID>
     * @param tk
     * @param type
     * @return
     * @see com.zk.security.service.ZKSecPrincipalService#getPrincipalByType(com.zk.security.ticket.ZKSecTicket, int)
     */
    @Override
    public <ID> ZKSecPrincipal<ID> getPrincipalByType(ZKSecTicket tk, int type) {
        return ZKSecSecurityUtils.getPrincipalByType(tk, type);
    }

    /**
     * (not Javadoc)
     * 
     * @param <ID>
     * @return
     * @see com.zk.security.service.ZKSecPrincipalService#getUserId()
     */
    @Override
    public <ID> ID getUserId() {
        return ZKSecSecurityUtils.getUserId();
    }

    /**
     * (not Javadoc)
     * 
     * @param <ID>
     * @return
     * @see com.zk.security.service.ZKSecPrincipalService#getCompanyId()
     */
    @Override
    public <ID> ID getCompanyId() {
        return ZKSecSecurityUtils.getCompanyId();
    }

    /**
     * (not Javadoc)
     * 
     * @return
     * @see com.zk.security.service.ZKSecPrincipalService#getCompanyCode()
     */
    @Override
    public String getCompanyCode() {
        return ZKSecSecurityUtils.getCompanyCode();
    }

    /**
     * (not Javadoc)
     * 
     * @return
     * @see com.zk.security.service.ZKSecPrincipalService#getGroupCode()
     */
    @Override
    public String getGroupCode() {
        return ZKSecSecurityUtils.getGroupCode();
    }

    /**
     * (not Javadoc)
     * 
     * @return
     * @see com.zk.security.service.ZKSecPrincipalService#getTikcet()
     */
    @Override
    public ZKSecTicket getTikcet() {
        return ZKSecSecurityUtils.getTikcet();
    }

}
