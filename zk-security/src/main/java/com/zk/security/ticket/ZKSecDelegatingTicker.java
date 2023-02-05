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
* @Title: ZKSecDelegatingTicker.java 
* @author Vinson 
* @Package com.zk.security.ticket 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 23, 2021 6:03:01 PM 
* @version V1.0 
*/
package com.zk.security.ticket;

import java.io.Serializable;
import java.util.Date;

import com.zk.security.principal.pc.ZKSecPrincipalCollection;

/** 
* @ClassName: ZKSecDelegatingTicker 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecDelegatingTicker extends ZKSecAbstractTicket {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = -3671621723172966520L;

    ZKSecProxyTickerManager tickerManager;

    public ZKSecDelegatingTicker(ZKSecProxyTickerManager proxyTickerManager, Serializable key) {
        super(key);
        this.tickerManager = proxyTickerManager;
    }

    /**
     * @return tickerManager sa
     */
    public ZKSecProxyTickerManager getTickerManager() {
        return tickerManager;
    }

    /**
     * @param tickerManager
     *            the tickerManager to set
     */
    public void setTickerManager(ZKSecProxyTickerManager tickerManager) {
        this.tickerManager = tickerManager;
    }

    @Override
    public int getType() {
        return this.getTickerManager().getType(this.getTkId());
    }

    @Override
    public int getSecurityType() {
        return this.getTickerManager().getSecurityType(this.getTkId());
    }

    @Override
    public <ID> ZKSecPrincipalCollection<ID> getPrincipalCollection() {
        return this.getTickerManager().getPrincipalCollection(this.getTkId());
    }

    @Override
    public <ID> void setPrincipalCollection(ZKSecPrincipalCollection<ID> principalCollection) {
        this.getTickerManager().setPrincipalCollection(this.getTkId(), principalCollection);
    }

    @Override
    public int getStatus() {
        return this.getTickerManager().getStatus(this.getTkId());
    }

    @Override
    public void stop() {
        this.getTickerManager().stop(this.getTkId());
    }

    @Override
    public void start() {
        this.getTickerManager().start(this.getTkId());
    }

    @Override
    public Date getLastTime() {
        return this.getTickerManager().getLastTime(this.getTkId());
    }

    @Override
    public void updateLastTime() {
        this.getTickerManager().updateLastTime(this.getTkId());
    }

    @Override
    public long getValidTime() {
        return this.getTickerManager().getValidTime(this.getTkId());
    }

    @Override
    public void setValidTime(long validTime) {
        this.getTickerManager().setValidTime(this.getTkId(), validTime);
    }

    @Override
    public boolean isValid() {
        return this.getTickerManager().isValid(this.getTkId());
    }

    @Override
    public int drop() {
        return this.getTickerManager().drop(this.getTkId());
    }

    @Override
    public <V> boolean put(String key, V value) {
        return this.getTickerManager().put(this.getTkId(), key, value);
    }

    @Override
    public <V> V get(String key) {
        return this.getTickerManager().get(this.getTkId(), key);
    }

    @Override
    public boolean remove(String key) {
        return this.getTickerManager().remove(this.getTkId(), key);
    }

}
