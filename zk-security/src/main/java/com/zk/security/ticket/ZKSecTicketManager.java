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
* @Title: ZKSecTicketManager.java 
* @author Vinson 
* @Package com.zk.security.ticket 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 14, 2021 6:50:56 PM 
* @version V1.0 
*/
package com.zk.security.ticket;

import java.io.Serializable;
import java.util.List;

import com.zk.core.spring.lifecycle.ZKDestroyable;
import com.zk.security.principal.ZKSecPrincipal;

/** 
* @ClassName: ZKSecTicketManager 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecTicketManager extends ZKDestroyable {

    /**
     * 生成令牌标识
     * 
     * @param session
     * @return
     */
    public Serializable generateTkId();

    public Serializable generateTkId(Serializable tkId);

    /**
     * 生成一个普通令牌
     */
    public ZKSecTicket createTicket(Serializable identification);

    /**
     * 生成一个普通令牌
     *
     * @Title: createTicket
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 25, 2021 11:49:48 PM
     * @param identification
     * @param validTime
     *            有效时长，毫秒
     * @return
     * @return ZKSecTicket
     */
    public ZKSecTicket createTicket(Serializable identification, long validTime);

    /**
     * 生成一个权限令牌；默认用户权限令牌
     */
    public ZKSecTicket createSecTicket(Serializable identification);

    /**
     * 生成一个权限令牌；默认用户权限令牌
     *
     * @Title: createSecTicket
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 25, 2021 11:50:05 PM
     * @param identification
     * @param securityType
     *            权限类型，默认是用户类型
     * @return ZKSecTicket
     */
    public ZKSecTicket createSecTicket(Serializable identification, int securityType);

    /**
     * 创建一个权限令牌；默认用户权限令牌
     *
     * @Title: createSecTicket
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 16, 2022 5:49:00 PM
     * @param identification
     * @param securityType
     *            权限类型，默认是用户类型
     * @param validTime
     *            有效时长，毫秒
     * @return ZKSecTicket
     */
    public ZKSecTicket createSecTicket(Serializable identification, int securityType, long validTime);

    /**
     * 销毁指定类型令牌
     */
    public int dropTicketByType(int type);

    /**
     * 销毁所有令牌
     */
    public int dropAll();

    /**
     * 清理过期令牌
     */
    public void validateTickets();

    /**
     * 取一个令牌
     */
    public ZKSecTicket getTicket(Serializable identification);

    /**
     * 销毁一个令牌
     *
     */
    public int dropTicket(ZKSecTicket ticket);

    /**
     * 销毁一个令牌
     *
     */
    public int dropTicket(Serializable identification);

//    /**
//     * 更新一个令牌；包括更新令牌保存的信息
//     * 
//     */
//    public int updateTicket(ZKSecTicket ticket);

    /**
     * 根据身份ID取当前身份所拥有的所有令牌
     */
    public <ID> List<ZKSecTicket> findTickeByPrincipal(ZKSecPrincipal<ID> principal);

    /**
     * 根据身份ID取当前身份所拥有的所有令牌
     * 
     * @param principal
     * @param filterTickets
     *            需要过虑的令牌
     * @return
     */
    public <ID> List<ZKSecTicket> findTickeByPrincipal(ZKSecPrincipal<ID> principal, List<ZKSecTicket> filterTickets);

}
