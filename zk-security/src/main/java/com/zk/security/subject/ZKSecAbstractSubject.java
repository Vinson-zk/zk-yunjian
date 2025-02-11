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
* @Title: ZKSecAbstractSubject.java 
* @author Vinson 
* @Package com.zk.security.subject 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 7:14:57 PM 
* @version V1.0 
*/
package com.zk.security.subject;

import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.exception.ZKSecAuthenticationException;
import com.zk.core.exception.ZKSystemException;
import com.zk.core.exception.base.ZKUnknownException;
import com.zk.security.annotation.ZKSecApiCode;
import com.zk.security.mgt.ZKSecSecurityManager;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.token.ZKSecAuthenticationToken;
import com.zk.security.utils.ZKSecSecurityUtils;

/** 
* @ClassName: ZKSecAbstractSubject 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecAbstractSubject implements ZKSecSubject {

    protected Logger logger = LogManager.getLogger(getClass());

    private ZKSecTicket ticket;

    protected ZKSecSecurityManager securityManager;

    /**
     * 是否已进行登录认证，默认 false；
     */
    protected boolean authenticated;

    protected ZKSecAbstractSubject(ZKSecSecurityManager securityManager, ZKSecTicket ticket) {
        this.securityManager = securityManager;
        this.ticket = ticket;
        this.authenticated = false;
    }

    @Override
    public boolean checkApiCode(ZKSecApiCode apiCode) {
        return this.checkApiCode(apiCode.value());
    }

    @Override
    public boolean checkApiCode(String apiCode) {
        if (this.getTicketCheckStatus() != null) {
            return getSecurityManager().checkApiCode(this.getPrincipalCollection(), apiCode);
        }
        return false;
    }

    /**
     * 鉴权
     * 
     * @param permissionCode
     *            权限代码
     * @return true-鉴权成功；反之鉴权失败
     */
    @Override
    public boolean checkPermission(String permissionCode) {
        if (this.getTicketCheckStatus() != null) {
            return getSecurityManager().checkPermission(this.ticket.getPrincipalCollection(), permissionCode);
        }
        return false;
    }

    /**
     * 取当前登录用户所有身份
     */
    @Override
    public <ID> ZKSecPrincipalCollection<ID> getPrincipalCollection() {
        if (getTicketCheckStatus() != null) {
            return this.ticket.getPrincipalCollection();
        }
        return null;
    }

    /**
     * 是否已进行登录认证；
     * 
     * @return true-已进行登录认证；false-未进行登录认证；
     */
    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public ZKSecSecurityManager getSecurityManager() {
        if (this.securityManager == null) {
            throw new ZKUnknownException("secManager is null");
        }
        return this.securityManager;
    }

    protected void setTicket(ZKSecTicket ticket) {
        this.ticket = ticket;
        if (ticket == null || !ticket.isValid() || ticket.getType() != ZKSecTicket.KeyType.Security) {
//            if (ticket == null || !ticket.isValid()) {
            logger.warn("set subject ticket to NULL or is not sec ticket");
        }
        else {
            getSecurityManager().onSetTicketToSubject(ticket, this);
        }
    }

    @Override
    public ZKSecTicket getTicket() {
        return this.getTicket(false);
    }

    @Override
    public ZKSecTicket getTicket(boolean isCreate) {
        if ((this.ticket == null || !ticket.isValid()) && isCreate) {
//        if ((this.ticket == null || !ticket.isValid() || ticket.getType() != ZKSecTicket.TYPE.Sec) && isCreate) {
            this.setTicket(getSecurityManager().getTicketManager()
                    .createSecTicket(getSecurityManager().getTicketManager().generateTkId()));
        }
        return this.ticket;
    }

    /**
     * 判断是否有用户身份
     * 
     * @return
     */
    @Override
    public boolean isAuthcUser() {
        if (getTicketCheckStatus() != null) {
            return ZKSecSecurityUtils.getUserPrincipal(ticket) == null ? false : true;
        }
        return false;
    }

    /**
     * 判断是否有 app 身份
     * 
     * @return
     */
    @Override
    public boolean isAuthcDev() {
        if (getTicketCheckStatus() != null) {
            return ZKSecSecurityUtils.getDevPrincipal(ticket) == null ? false : true;
        }
        return false;
    }

    /**
     * 判断是否是微服务间调用
     *
     * @Title: isServer
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 13, 2022 2:33:19 PM
     * @return
     * @return boolean
     */
    @Override
    public boolean isAuthcServer() {
        if (getTicketCheckStatus() != null) {
            return ZKSecSecurityUtils.getServerPrincipal(ticket) == null ? false : true;
        }
        return false;
    }

    /**
     * 登录
     */
    @Override
    public void login(ZKSecAuthenticationToken authenticationToken) {
        if (getSecurityManager().login(this, authenticationToken)) {
            this.authenticated = true;
        }
    }

    /**
     * 登出
     */
    @Override
    public void logout() {
        getSecurityManager().logOut(this);
        this.authenticated = false;
    }

    private ZKSecTicket getTicketCheckStatus() {
        if (this.ticket != null) {
            if (this.ticket.getStatus() == ZKSecTicket.KeyStatus.Stop) {
                if (this.ticket.get(ZKSecTicket.KeyTicketInfo.stop_info_code) != null) {
//                    throw ZKSystemException.as("zk.1",
//                            this.ticket.get(ZKSecTicket.KeyTicketInfo.stop_info_code).toString());
                    throw ZKSecAuthenticationException
                            .as(this.ticket.get(ZKSecTicket.KeyTicketInfo.stop_info_code).toString());
                }
                // 未知的令牌禁用原因
                throw ZKSystemException.as("zk.sec.000008", null);//
            }
        }
        return this.ticket;
    }

    /**
     * @throws Throwable
     *************************************************************************/
    @Override
    public <V> V execute(Callable<V> callable) throws Exception {
        Callable<V> associated = associateWith(callable);
        try {
            return associated.call();
        }
        catch(Exception e) {
            throw e;
        }
    }

    public <V> Callable<V> associateWith(Callable<V> callable) {
        return new ZKSecSubjectCallable<V>(this, callable);
    }

}
