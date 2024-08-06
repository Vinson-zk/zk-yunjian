///** 
//* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
//* address: 
//* All rights reserved. 
//* 
//* This software is the confidential and proprietary information of 
//* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
//* disclose such Confidential Information and shall use it only in 
//* accordance with the terms of the license agreement you entered into 
//* with ZK-Vinson. 
//*
//* @Title: ZKSecCacheTicketManager.java 
//* @author Vinson 
//* @Package com.zk.security.ticket.support.cache 
//* @Description: TODO(simple description this file what to do. ) 
//* @date Feb 14, 2023 8:23:11 PM 
//* @version V1.0 
//*/
//package com.zk.security.ticket.support.cache;
///** 
//* @ClassName: ZKSecCacheTicketManager 
//* @Description: TODO(simple description this class what to do. ) 
//* @author Vinson 
//* @version 1.0 
//*/
//
//import java.io.Serializable;
//import java.util.Date;
//import java.util.List;
//
//import com.zk.security.principal.ZKSecPrincipal;
//import com.zk.security.principal.pc.ZKSecPrincipalCollection;
//import com.zk.security.ticket.ZKSecAbstractTicketManager;
//import com.zk.security.ticket.ZKSecProxyTickerManager;
//import com.zk.security.ticket.ZKSecTicket;
//
//public class ZKSecCacheTicketManager extends ZKSecAbstractTicketManager implements ZKSecProxyTickerManager {
//
//    @Override
//    public ZKSecTicket createTicket(Serializable identification) {
//        // TODO Auto-generated method stub return null;
//    }
//
//    @Override
//    public ZKSecTicket createTicket(Serializable identification, long validTime) {
//        // TODO Auto-generated method stub return null;
//    }
//
//    @Override
//    public ZKSecTicket createSecTicket(Serializable identification) {
//        // TODO Auto-generated method stub return null;
//    }
//
//    @Override
//    public ZKSecTicket createSecTicket(Serializable identification, int securityType) {
//        // TODO Auto-generated method stub return null;
//    }
//
//    @Override
//    public ZKSecTicket createSecTicket(Serializable identification, int securityType, long validTime) {
//        // TODO Auto-generated method stub return null;
//    }
//
//    @Override
//    public int dropTicketByType(int type) {
//        // TODO Auto-generated method stub return 0;
//    }
//
//    @Override
//    public int dropAll() {
//        // TODO Auto-generated method stub return 0;
//    }
//
//    @Override
//    public void validateTickets() {
//        // TODO Auto-generated method stub 
//    }
//
//    @Override
//    public ZKSecTicket getTicket(Serializable identification) {
//        // TODO Auto-generated method stub return null;
//    }
//
//    @Override
//    public int dropTicket(ZKSecTicket ticket) {
//        // TODO Auto-generated method stub return 0;
//    }
//
//    @Override
//    public int dropTicket(Serializable identification) {
//        // TODO Auto-generated method stub return 0;
//    }
//
//    @Override
//    public <ID> List<ZKSecTicket> findTickeByPrincipal(ZKSecPrincipal<ID> principal) {
//        // TODO Auto-generated method stub return null;
//    }
//
//    @Override
//    public <ID> List<ZKSecTicket> findTickeByPrincipal(ZKSecPrincipal<ID> principal, List<ZKSecTicket> filterTickets) {
//        // TODO Auto-generated method stub return null;
//    }
//
//    @Override
//    public void destroy() {
//        // TODO Auto-generated method stub 
//    }
//
//    @Override
//    public int getType(Serializable identification) {
//        // TODO Auto-generated method stub return 0;
//    }
//
//    @Override
//    public int getSecurityType(Serializable identification) {
//        // TODO Auto-generated method stub return 0;
//    }
//
//    @Override
//    public <ID> ZKSecPrincipalCollection<ID> getPrincipalCollection(Serializable identification) {
//        // TODO Auto-generated method stub return null;
//    }
//
//    @Override
//    public <ID> void setPrincipalCollection(Serializable identification,
//            ZKSecPrincipalCollection<ID> principalCollection) {
//        // TODO Auto-generated method stub 
//    }
//
//    @Override
//    public int getStatus(Serializable identification) {
//        // TODO Auto-generated method stub return 0;
//    }
//
//    @Override
//    public void start(Serializable identification) {
//        // TODO Auto-generated method stub 
//    }
//
//    @Override
//    public void stop(Serializable identification) {
//        // TODO Auto-generated method stub 
//    }
//
//    @Override
//    public Date getLastTime(Serializable identification) {
//        // TODO Auto-generated method stub return null;
//    }
//
//    @Override
//    public void updateLastTime(Serializable identification) {
//        // TODO Auto-generated method stub 
//    }
//
//    @Override
//    public long getValidTime(Serializable identification) {
//        // TODO Auto-generated method stub return 0;
//    }
//
//    @Override
//    public void setValidTime(Serializable identification, long validTime) {
//        // TODO Auto-generated method stub 
//    }
//
//    @Override
//    public boolean isValid(Serializable identification) {
//        // TODO Auto-generated method stub return false;
//    }
//
//    @Override
//    public int drop(Serializable identification) {
//        // TODO Auto-generated method stub return 0;
//    }
//
//    @Override
//    public <V> boolean put(Serializable identification, String key, V value) {
//        // TODO Auto-generated method stub return false;
//    }
//
//    @Override
//    public <V> V get(Serializable identification, String key) {
//        // TODO Auto-generated method stub return null;
//    }
//
//    @Override
//    public boolean remove(Serializable identification, String key) {
//        // TODO Auto-generated method stub return false;
//    }
//
//}
