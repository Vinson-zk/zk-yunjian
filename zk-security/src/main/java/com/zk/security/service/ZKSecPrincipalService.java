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
* @Title: ZKSecPrincipalService.java 
* @author Vinson 
* @Package com.zk.security.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 2, 2023 4:57:57 PM 
* @version V1.0 
*/
package com.zk.security.service;

import com.zk.security.principal.ZKSecDevPrincipal;
import com.zk.security.principal.ZKSecPrincipal;
import com.zk.security.principal.ZKSecUserPrincipal;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.ticket.ZKSecTicket;

/** 
* @ClassName: ZKSecPrincipalService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecPrincipalService {

    /**
     * 取当前登录用户所有身份
     */
    <ID> ZKSecPrincipalCollection<ID> getPrincipalCollection();

    /**
     * 取当前登录用户身份
     * 
     * @return
     */
    <ID> ZKSecUserPrincipal<ID> getUserPrincipal();

    <ID> ZKSecUserPrincipal<ID> getUserPrincipal(ZKSecTicket tk);

    /**
     * 取当前 开发者 身份
     * 
     * @return
     */
    <ID> ZKSecDevPrincipal<ID> getAppPrincipal();

    <ID> ZKSecDevPrincipal<ID> getDevPrincipal(ZKSecTicket tk);

    /**
     * 取微服务身份
     *
     * @Title: getServerPrincipal
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 14, 2022 8:47:00 AM
     * @param tk
     * @return
     * @return ZKSecDevPrincipal<?>
     */
    <ID> ZKSecPrincipal<ID> getServerPrincipal(ZKSecTicket tk);

    /**
     * 取指定类型的身份
     * 
     * @param type
     * @return
     */
    <ID> ZKSecPrincipal<ID> getPrincipalByType(ZKSecTicket tk, int type);

    <ID> ID getUserId();

    <ID> ID getCompanyId();

    String getCompanyCode();

    String getGroupCode();

    // 取当前令牌
    ZKSecTicket getTikcet();

}
