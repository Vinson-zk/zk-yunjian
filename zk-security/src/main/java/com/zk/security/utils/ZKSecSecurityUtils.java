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
* @Title: ZKSecSecurityUtils.java 
* @author Vinson 
* @Package com.zk.security.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 6:42:11 PM 
* @version V1.0 
*/
package com.zk.security.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.exception.base.ZKUnknownException;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.security.mgt.ZKSecSecurityManager;
import com.zk.security.principal.ZKSecDevPrincipal;
import com.zk.security.principal.ZKSecPrincipal;
import com.zk.security.principal.ZKSecUserPrincipal;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.thread.ZKSecThreadContext;
import com.zk.security.ticket.ZKSecTicket;

/** 
* @ClassName: ZKSecSecurityUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecSecurityUtils {

    protected static Logger log = LogManager.getLogger(ZKSecSecurityUtils.class);

    private static ZKSecSecurityManager securityManager;

    /**
     * 取当前登录用户所有身份
     */
    public static <ID> ZKSecPrincipalCollection<ID> getPrincipalCollection() {
        if (getSubject() != null) {
            return getSubject().getPrincipalCollection();
        }
        return null;
    }

    /**
     * 取当前登录用户身份
     * 
     * @return
     */
    public static <ID> ZKSecUserPrincipal<ID> getUserPrincipal() {
        return getUserPrincipal(getTikcet());
    }

    @SuppressWarnings("unchecked")
    public static <ID> ZKSecUserPrincipal<ID> getUserPrincipal(ZKSecTicket tk) {
        if (tk != null) {
            return (ZKSecUserPrincipal<ID>) getPrincipalByType(tk, ZKSecPrincipal.KeyType.User);
        }
        return null;
    }

    /**
     * 取当前 开发者 身份
     * 
     * @return
     */
    public static <ID> ZKSecDevPrincipal<ID> getAppPrincipal() {
        return getDevPrincipal(getTikcet());
    }

    @SuppressWarnings("unchecked")
    public static <ID> ZKSecDevPrincipal<ID> getDevPrincipal(ZKSecTicket tk) {
        if (tk != null) {
            return (ZKSecDevPrincipal<ID>) getPrincipalByType(tk, ZKSecPrincipal.KeyType.Developer);
        }
        return null;
    }

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
    public static <ID> ZKSecPrincipal<ID> getServerPrincipal(ZKSecTicket tk) {
        if (tk != null) {
            return getPrincipalByType(tk, ZKSecPrincipal.KeyType.Distributed_server);
        }
        return null;
    }

    /**
     * 取指定类型的身份
     * 
     * @param type
     * @return
     */
    public static <ID> ZKSecPrincipal<ID> getPrincipalByType(ZKSecTicket tk, int type) {
        if (tk != null) {
            ZKSecPrincipalCollection<ID> pc = tk.getPrincipalCollection();
            if (pc != null && !pc.isEmpty()) {
                for (ZKSecPrincipal<ID> p : pc.asSet()) {
                    if (p.getType() == type) {
                        return p;
                    }
                }
            }
        }
        return null;
    }

    public static <ID> ID getUserId() {
        ZKSecUserPrincipal<ID> u = getUserPrincipal();
        if (u != null) {
            return u.getPkId();
        }
        return null;
    }

    public static <ID> ID getCompanyId() {
        ZKSecUserPrincipal<ID> u = getUserPrincipal();
        if (u != null) {
            return u.getCompanyId();
        }
        return null;
    }

    public static String getCompanyCode() {
        ZKSecUserPrincipal<?> u = getUserPrincipal();
        if (u != null) {
            return u.getCompanyCode();
        }
        return null;
    }

    public static String getGroupCode() {
        ZKSecUserPrincipal<?> u = getUserPrincipal();
        if (u != null) {
            return u.getGroupCode();
        }
        return null;
    }

    // 取当前令牌
    public static ZKSecTicket getTikcet() {
        return getTikcet(false);
    }

    public static ZKSecTicket getTikcet(boolean isCreate) {
        try {
            if (getSubject() != null) {
                return getSubject().getTicket(isCreate);
            }
        }
        catch(Exception e) {
            log.error("[>_<:20220524-1022-001 取用户令牌失败！");
            throw e;
        }
        return null;
    }

    public static ZKSecSubject getSubject() {
        ZKSecSubject subject = ZKSecThreadContext.getSubject();
        if (subject == null) {
            subject = getSecurityManager().createSubject();
            if (subject != null) {
                ZKSecThreadContext.bind(subject);
            }
            else {
                log.error("[>_<: 20220516-1420-001] 无会话 subject，创建 subject 类型与环境不匹配！");
            }
        }
        return subject;
    }

    public static void setSecurityManager(ZKSecSecurityManager securityManager) {
        ZKSecSecurityUtils.securityManager = securityManager;
    }

    public static ZKSecSecurityManager getSecurityManager() {
        ZKSecSecurityManager secManager = ZKSecThreadContext.getSecurityManager();
        if (secManager == null) {
            secManager = ZKSecSecurityUtils.securityManager;
        }
        if (secManager == null) {
            secManager = ZKEnvironmentUtils.getApplicationContext().getBean(ZKSecSecurityManager.class);
        }
        if (secManager == null) {
            String msg = "No SecManager accessible to the calling code, either bound to the "
                    + ZKSecThreadContext.class.getName()
                    + " or as a vm static singleton.  This is an invalid application "
                    + "configuration.";
            throw new ZKUnknownException(msg);
        }
        return secManager;
    }

}
