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
* @Title: ZKSecAbstractPrincipal.java 
* @author Vinson 
* @Package com.zk.security.principal 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 14, 2021 6:54:04 PM 
* @version V1.0 
*/
package com.zk.security.principal;

import java.io.Serializable;

/** 
* @ClassName: ZKSecAbstractPrincipal 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecAbstractPrincipal<ID> implements ZKSecPrincipal<ID> {

    private static final long serialVersionUID = 1L;

    /**
     * 是否为主要的身份
     */
    boolean primary = false;

    // 令牌 ID
    Serializable ticketId;

    // 唯一标识, 与用户表ID应该对应
    ID pkId;

    // 类型
    int type;

    /**
     * 终端设备类型
     * 
     */
    long osType;

    /**
     * 设备UDID
     * 
     */
    String udid;

    /**
     * 应用类型
     */
    long appType;

    /**
     * 应该唯一标识
     */
    String appId;

    public ZKSecAbstractPrincipal(ID pkId, int type, long osType, String udid, long appType, String appId) {
        this.pkId = pkId;
        this.type = type;
        this.osType = osType;
        this.udid = udid;
        this.osType = osType;
        this.appId = appId;
    }

    /**
     * 令牌 ID
     * 
     * @return
     */
    public void setTicketId(Serializable ticketId) {
        this.ticketId = ticketId;
    }

    /**
     * 令牌 ID
     * 
     * @return
     */
    @Override
    public Serializable getTicketId() {
        return ticketId;
    }

    /**
     * 唯一标识, 与用户表ID应该对应
     * 
     * @return
     */
    @Override
    public ID getPkId() {
        return pkId;
    }

    /**
     * 类型
     * 
     * @return
     */
    @Override
    public int getType() {
        return type;
    }

    /**
     * 是否为主要的身份
     */
    @Override
    public boolean isPrimary() {
        return primary;
    }

    /**
     * 是否为主要的身份
     */
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    @Override
    public long getOsType() {
        return this.osType;
    }

    @Override
    public String getUdid() {
        return this.udid;
    }

    @Override
    public long getAppType() {
        return this.appType;
    }

    @Override
    public String getAppId() {
        return this.appId;
    }

}
