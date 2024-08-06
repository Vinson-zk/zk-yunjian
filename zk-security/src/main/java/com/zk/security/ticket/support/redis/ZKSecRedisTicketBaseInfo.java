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
* @Title: ZKSecRedisTicketBaseInfo.java 
* @author Vinson 
* @Package com.zk.security.ticket.support.redis 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 17, 2021 10:44:56 AM 
* @version V1.0 
*/
package com.zk.security.ticket.support.redis;

import java.io.Serializable;
import java.util.Date;

import com.zk.core.utils.ZKDateUtils;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;

/** 
* @ClassName: ZKSecRedisTicketBaseInfo 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecRedisTicketBaseInfo implements Serializable {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    String tkId;

    int type;

    int securityType;

    int status;

    long validTime;

    Date lastTime;

    ZKSecPrincipalCollection<?> principalCollection;

    public ZKSecRedisTicketBaseInfo() {
    }

    public ZKSecRedisTicketBaseInfo(String tkId, int type, int securityType, int status, long validTime) {
        this.tkId = tkId;
        this.type = type;
        this.securityType = securityType;
        this.status = status;
        this.validTime = validTime;
        this.lastTime = ZKDateUtils.getToday();
    }

    /**
     * @return tkId sa
     */
    public String getTkId() {
        return tkId;
    }

    /**
     * @param tkId
     *            the tkId to set
     */
    public void setTkId(String tkId) {
        this.tkId = tkId;
    }

    /**
     * @return type sa
     */
    public int getType() {
        return type;
    }

    /**
     * @return securityType sa
     */
    public int getSecurityType() {
        return securityType;
    }

    /**
     * @return status sa
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return validTime sa
     */
    public long getValidTime() {
        return validTime;
    }

    /**
     * @return lastTime sa
     */
    public Date getLastTime() {
        return lastTime;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @param securityType
     *            the securityType to set
     */
    public void setSecurityType(int securityType) {
        this.securityType = securityType;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @param validTime
     *            the validTime to set
     */
    public void setValidTime(long validTime) {
        this.validTime = validTime;
    }

    /**
     * @param lastTime
     *            the lastTime to set
     */
    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    /**
     * @return pc sa
     */
    @SuppressWarnings("unchecked")
    public <ID> ZKSecPrincipalCollection<ID> getPrincipalCollection() {
        return (ZKSecPrincipalCollection<ID>) principalCollection;
    }

    /**
     * @param PrincipalCollection
     *            the pc to set
     */
    public <ID> void setPrincipalCollection(ZKSecPrincipalCollection<ID> principalCollection) {
        this.principalCollection = principalCollection;
    }

    public boolean isValid() {
//        if (validTime > 0 && lastTime != null && ((lastTime.getTime() + validTime) < System.currentTimeMillis())) {
        if (lastTime != null && ((lastTime.getTime() + validTime) < System.currentTimeMillis())) {
            // 令牌过期
            return false;
        }
        return true;
    }

}
