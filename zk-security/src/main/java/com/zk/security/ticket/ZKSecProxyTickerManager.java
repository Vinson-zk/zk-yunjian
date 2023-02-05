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
* @Title: ZKSecProxyTickerManager.java 
* @author Vinson 
* @Package com.zk.security.ticket 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 23, 2021 6:43:18 PM 
* @version V1.0 
*/
package com.zk.security.ticket;

import java.io.Serializable;
import java.util.Date;

import com.zk.security.principal.pc.ZKSecPrincipalCollection;

/** 
* @ClassName: ZKSecProxyTickerManager 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecProxyTickerManager extends ZKSecTicketManager {

    /**
     * 取令牌类型, 0 普通令牌；1-security令牌
     * 
     * @return
     */
    public int getType(Serializable identification);

    /**
     * 取令牌的权限类型，0-用户权限类型；1-微服务权限类型；
     */
    public int getSecurityType(Serializable identification);

    /**
     * 取拥有的身分
     * 
     * @return
     */
    public <ID> ZKSecPrincipalCollection<ID> getPrincipalCollection(Serializable identification);

    /**
     * 设置拥有者标识
     */
    public <ID> void setPrincipalCollection(Serializable identification,
            ZKSecPrincipalCollection<ID> principalCollection);

    /**
     * 取令牌状态 0-正常 1-未启用
     */
    public int getStatus(Serializable identification);

    /**
     * 启用令牌，设置状态为 0
     */
    public void start(Serializable identification);

    /**
     * 停用令牌，设置状态为 0
     */
    public void stop(Serializable identification);

    /**
     * 最后更新时间戳
     */
    public Date getLastTime(Serializable identification);

    /**
     * 更新最后修改时间
     */
    public void updateLastTime(Serializable identification);

    /**
     * 有效时长，毫秒
     * 
     * @return
     */
    public long getValidTime(Serializable identification);

    /**
     * 设置令牌有效时长，毫秒
     */
    public void setValidTime(Serializable identification, long validTime);

    /**
     * 判断令牌是否有效，过期与未起用为无效
     * 
     * @return true-有效；false-无效；
     */
    public boolean isValid(Serializable identification);

    /**
     * 销毁令牌
     */
    public int drop(Serializable identification);

//  /**
//   * 触发更新
//   */
//  public void touchUpdate();

    /**
     * 设置令牌所携带的信息
     */
    public <V> boolean put(Serializable identification, String key, V value);

    /**
     * 取令牌所携带的信息
     */
    public <V> V get(Serializable identification, String key);

    /**
     * 删除一个令牌所携带的信息
     * 
     * @param key
     */
    public boolean remove(Serializable identification, String key);

}
