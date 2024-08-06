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
* @Title: ZKSecAbstractSecurityManager.java 
* @author Vinson 
* @Package com.zk.security.mgt 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 11:53:05 PM 
* @version V1.0 
*/
package com.zk.security.mgt;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.exception.base.ZKCodeException;
import com.zk.core.exception.base.ZKUnknownException;
import com.zk.security.common.ZKSecFullStrategy;
import com.zk.security.common.ZKSecStrategy;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.realm.ZKSecRealm;
import com.zk.security.rememberMe.ZKSecRememberMeManager;
import com.zk.security.subject.ZKSecDefultSubjectFactory;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.subject.ZKSecSubjectFactory;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.ticket.ZKSecTicketManager;
import com.zk.security.token.ZKSecAuthenticationToken;

/**
 * @ClassName: ZKSecAbstractSecurityManager
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public abstract class ZKSecAbstractSecurityManager implements ZKSecSecurityManager {

    protected Logger logger = LogManager.getLogger(getClass());

    /**
     * 令牌管理
     */
    private ZKSecTicketManager ticketManager;

    /**
     * 记住我管理
     */
    private ZKSecRememberMeManager rememberMeManager;

    /**
     * 认证域
     */
    Set<ZKSecRealm> realmSet;

    /**
     * 认证策略 所有域认证成功，并返回所有身份 或 任意域认证成功，返回任意一个身份；实现接口
     */
    private ZKSecStrategy strategy;

    /**
     * 主要创建工厂 默认 DefultSubjectFactory 创建 DefaultSubject 主体
     */
    protected ZKSecSubjectFactory subjectFactory;

    @Override
    public ZKSecTicketManager getTicketManager() {
        if (this.ticketManager == null) {
            throw new ZKUnknownException("The secManager member variable ticketManager is null");
        }
        return this.ticketManager;
    }

    public void setTicketManager(ZKSecTicketManager ticketManager) {
        this.ticketManager = ticketManager;
    }

    @Override
    public ZKSecRememberMeManager getRememberMeManager() {
        return rememberMeManager;
    }

    public void setRememberMeManager(ZKSecRememberMeManager rememberMeManager) {
        this.rememberMeManager = rememberMeManager;
    }

    public Set<ZKSecRealm> getRealmSet() {
        if (realmSet == null || realmSet.isEmpty()) {
            throw new ZKUnknownException("The realm is NULL!!!");
        }
        return realmSet;
    }

    public void setRealmSet(Set<ZKSecRealm> realmSet) {
        this.realmSet = realmSet;
    }

    public void setRealm(ZKSecRealm realm) {
        if (realmSet == null) {
            realmSet = new LinkedHashSet<>();
        }
        this.realmSet.add(realm);
    }

    public ZKSecStrategy getStrategy() {
        if (strategy == null) {
            strategy = new ZKSecFullStrategy();
        }
        return strategy;
    }

    public void setStrategy(ZKSecStrategy strategy) {
        this.strategy = strategy;
    }

    public ZKSecSubjectFactory getSubjectFactory() {
        if (subjectFactory == null) {
            subjectFactory = new ZKSecDefultSubjectFactory();
        }
        return subjectFactory;
    }

    public void setSubjectFactory(ZKSecSubjectFactory subjectFactory) {
        this.subjectFactory = subjectFactory;
    }

    @Override
    public ZKSecSubject createSubject() {
        return getSubjectFactory().createSubject(this, null);
    }

    /**
     * 登录用户认证
     * 
     * @param token
     * @return
     */
    @Override
    public <ID> boolean login(ZKSecSubject subject, ZKSecAuthenticationToken token) {
        ZKSecPrincipalCollection<ID> pc = null;
        try {
            pc = doAuthentication(subject, token);
            // 登录认证成功
            onSuccessfulLogin(subject, pc, token);
            return true;
        }
        catch(ZKCodeException se) {
            // 登录认证失败
            onFailedLogin(subject, token, se);
            return false;
        }
        catch(Exception e) {
            throw e;
        }
    }

    /**
     * 登录认证成功调用；记住我处理
     * 
     * @param subject
     * @param pc
     * @param token
     */
    public <ID> void onSuccessfulLogin(ZKSecSubject subject, ZKSecPrincipalCollection<ID> pc,
            ZKSecAuthenticationToken token) {
        // 1、将身份放入令牌中
        if (subject.getTicket(true) != null) {
            subject.getTicket(true).setPrincipalCollection(pc);
        }
        // 2、记住我处理
        if (getRememberMeManager() != null) {
            getRememberMeManager().onSuccessfulLogin(subject, token, pc);
        }
    }

    /**
     * 登录认证失败调用；记住我处理
     * 
     * @param subject
     * @param token
     * @param se
     */
    public void onFailedLogin(ZKSecSubject subject, ZKSecAuthenticationToken token, ZKCodeException se) {
        // 1、登录失败，创建一个追踪令牌
        subject.getTicket(true);
        // 2、记住我处理
        if (getRememberMeManager() != null) {
            getRememberMeManager().onFailedLogin(subject, token, se);
        }
        throw se;
    }

    /**
     * 执行登录认证
     * 
     * @param subject
     * @param token
     * @return 返回认证结果的身份
     */
    public abstract <ID> ZKSecPrincipalCollection<ID> doAuthentication(ZKSecSubject subject,
            ZKSecAuthenticationToken token);

    /**
     * 登出
     * 
     * @param subject
     *            主体
     * @return
     */
    @Override
    public void logOut(ZKSecSubject subject) {
        // 1、记住我处理
        if (getRememberMeManager() != null) {
            getRememberMeManager().onLogout(subject);
        }
        // 2、销毁令牌
        ZKSecTicket tk = subject.getTicket(false);
        if (tk != null) {
            this.getTicketManager().dropTicket(tk);
        }
    }

    public abstract void onLogOut(ZKSecSubject subject);

    /**
     * 鉴权，授权
     * 
     * @param pc
     * @param permissionCode
     *            权限代码
     * @return true-鉴权成功；反之鉴权失败
     */
    @Override
    public <ID> boolean checkPermission(ZKSecPrincipalCollection<ID> pc, String permissionCode) {
        if (pc == null || pc.isEmpty()) {
            return false;
        }
        return doCheckPermission(pc, permissionCode);
    }

    /**
     * 鉴定 api接口权限 代码
     * 
     * @param pc
     * @param apiCode
     *            api 代码
     * @return true-鉴定成功；反之鉴定失败
     */
    @Override
    public <ID> boolean checkApiCode(ZKSecPrincipalCollection<ID> pc, String apiCode) {
        if (pc == null || pc.isEmpty()) {
            return false;
        }
        return doCheckApiCode(pc, apiCode);
    }

    /**
     * 执行 鉴权，授权
     * 
     * @param pc
     * @param permissionCode
     *            权限代码
     * @return true-鉴权成功；反之鉴权失败
     */
    public abstract <ID> boolean doCheckPermission(ZKSecPrincipalCollection<ID> pc, String permissionCode);

    /**
     * 鉴定 api接口权限 代码
     * 
     * @param pc
     * @param apiCode
     *            api 接口代码
     * @return true-鉴权成功；反之鉴权失败
     */
    public abstract <ID> boolean doCheckApiCode(ZKSecPrincipalCollection<ID> pc, String apiCode);

    /**
     * 用户在线数量控制策略
     */
    @Override
    public <ID> void doPrincipalsCount(ZKSecPrincipalCollection<ID> pc) {
        this.getStrategy().doStrategyPrincipalsCount(realmSet, pc);
    }

}
