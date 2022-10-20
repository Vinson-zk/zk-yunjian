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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zk.security.exception.ZKSecUnknownException;
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

    protected static Logger log = LoggerFactory.getLogger(ZKSecSecurityUtils.class);

    private static ZKSecSecurityManager securityManager;

    /**
     * 取当前登录用户所有身份
     */
    public static ZKSecPrincipalCollection getPrincipalCollection() {
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
    public static <T> ZKSecUserPrincipal<T> getUserPrincipal() {
        return getUserPrincipal(getTikcet());
    }

    @SuppressWarnings("unchecked")
    public static <T> ZKSecUserPrincipal<T> getUserPrincipal(ZKSecTicket tk) {
        if (tk != null) {
            return (ZKSecUserPrincipal<T>) getPrincipalByType(tk, ZKSecPrincipal.KeyType.User);
        }
        return null;
    }

    /**
     * 取当前 开发者 身份
     * 
     * @return
     */
    public static ZKSecDevPrincipal<?> getAppPrincipal() {
        return getDevPrincipal(getTikcet());
    }

    public static ZKSecDevPrincipal<?> getDevPrincipal(ZKSecTicket tk) {
        if (tk != null) {
            return (ZKSecDevPrincipal<?>) getPrincipalByType(tk, ZKSecPrincipal.KeyType.Developer);
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
    public static ZKSecPrincipal<?> getServerPrincipal(ZKSecTicket tk) {
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
    protected static ZKSecPrincipal<?> getPrincipalByType(ZKSecTicket tk, int type) {
        if (tk != null) {
            ZKSecPrincipalCollection pc = tk.getPrincipalCollection();
            if (pc != null && !pc.isEmpty()) {
                for (ZKSecPrincipal<?> p : pc.asSet()) {
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
        try {
            if (getSubject() != null) {
                return getSubject().getTicket(false);
            }
        }
        catch(Exception e) {
            log.error("[>_<:20220524-1022-001 取用户令牌失败！");
            e.printStackTrace();
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
            String msg = "No SecManager accessible to the calling code, either bound to the "
                    + ZKSecThreadContext.class.getName()
                    + " or as a vm static singleton.  This is an invalid application "
                    + "configuration.";
            throw new ZKSecUnknownException(msg);
        }
        return secManager;
    }

}
