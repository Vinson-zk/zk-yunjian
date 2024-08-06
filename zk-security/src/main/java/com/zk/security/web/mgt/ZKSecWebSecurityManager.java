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
* @Title: ZKSecWebSecurityManager.java 
* @author Vinson 
* @Package com.zk.security.web.mgt 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 27, 2021 10:29:38 PM 
* @version V1.0 
*/
package com.zk.security.web.mgt;

import java.io.Serializable;

import com.zk.core.web.cookie.ZKCookie;
import com.zk.core.web.cookie.ZKDefaultCookie;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.mgt.ZKSecDefaultSecurityManager;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.security.web.subject.ZKSecWebSubject;
import com.zk.security.web.subject.ZKSecWebSubjectFactory;
import com.zk.security.web.wrapper.ZKSecRequestWrapper;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;

/**
 * @ClassName: ZKSecWebSecurityManager
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKSecWebSecurityManager extends ZKSecDefaultSecurityManager {

    /**
     * 是否开启 cookie 中存放令牌；true-开启；false-不开启；默认 false 不开启。
     */
    private boolean ticketCookieEnabled = false;

    public ZKSecWebSecurityManager() {
        super.setSubjectFactory(new ZKSecWebSubjectFactory());
    }

    @Override
    public ZKSecWebSubjectFactory getSubjectFactory() {
        if (super.getSubjectFactory() == null) {
            logger.error("subjectFactory is not WebSubjectFactory instantiation");
            return null;
        }
        return (ZKSecWebSubjectFactory) super.getSubjectFactory();
    }

    /**
     * 创建会话主体
     */
    public <ID> ZKSecSubject createSubject(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes) {
        /*
         * 1、确定主体令牌 2、创建主体 3、确定主体身份 4、保存令牌信息
         */
        /*** 1、确定主体令牌 ***/
        Serializable tkId = resolveTicket(zkReq);
        ZKSecTicket tk = null;
        if (tkId != null) {
            tk = this.getTicketManager().getTicket(tkId);
            zkReq.setAttribute(ZKSecConstants.SEC_KEY.SecIsHaveTicket, true);
        }else{
            zkReq.setAttribute(ZKSecConstants.SEC_KEY.SecIsHaveTicket, false);
        }

        /*** 2、创建主体 ***/
        ZKSecSubject subject = this.getSubjectFactory().createSubject(this, zkReq, zkRes, tk);

        /*** 3、确定主体身份 ***/
        ZKSecPrincipalCollection<ID> pc = resolvePrincipal(subject);

        /*** 4、保存主体信息 ***/
        saveToSubject(zkReq, zkRes, subject, pc);

        return subject;
    }

    public void setTicketCookieEnabled(boolean ticketCookieEnabled) {
        this.ticketCookieEnabled = ticketCookieEnabled;
    }

    /**
     * 是否开启 cookie 中存放令牌；
     * 
     * @return true-开启；false-不开启；默认开启
     */
    public boolean isTicketCookieEnabled() {
        return ticketCookieEnabled;
    }

    /**
     * 从请求中取令牌ID
     * 
     * @param request
     * @return
     */
    private Serializable resolveTicket(ZKSecRequestWrapper zkReq) {
        return zkReq.getTikcetId(isTicketCookieEnabled());
    }

    /**
     * 决定身份
     * 
     * @param subject
     * @return
     */
    private <ID> ZKSecPrincipalCollection<ID> resolvePrincipal(ZKSecSubject subject) {
        if (this.getRememberMeManager() != null) {
            return this.getRememberMeManager().getRememberedPrincipals(subject);
        }
        return null;
    }

    /**
     * 保存身份怎么到主体
     * 
     * @param request
     * @param response
     * @param subject
     * @param pc
     */
    private <ID> void saveToSubject(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes, ZKSecSubject subject,
            ZKSecPrincipalCollection<ID> pc) {
        if (subject.getTicket(false) != null && subject.getTicket(false).getPrincipalCollection() != null) {
            // 身份已经存在，不用再保存
            logger.info("[^_^:20180824-1204-001] 身份已经存在，不需要再设置！");
        }
        else {
            if (pc != null) {
                subject.getTicket(true).setPrincipalCollection(pc);
                // 用户在线数处理
                this.doPrincipalsCount(pc);
            }
        }
    }

    @Override
    public void onSetTicketToSubject(ZKSecTicket tk, ZKSecSubject toSubject) {
        if (tk != null && toSubject instanceof ZKSecWebSubject) {
            ZKSecRequestWrapper request = ((ZKSecWebSubject) toSubject).getRequest();
            ZKSecResponseWrapper response = ((ZKSecWebSubject) toSubject).getResponse();
            // 放入请求属性中
            request.setAttribute(ZKSecConstants.PARAM_NAME.TicketId, tk.getTkId());
            // 请求放入响应头中
//            System.out.println("[^_^:20240731-2227-001] PARAM_NAME.TicketId: " + tk.getTkId().toString());
            if (!response.containsHeader(ZKSecConstants.PARAM_NAME.TicketId)) {
                response.setHeader(ZKSecConstants.PARAM_NAME.TicketId, tk.getTkId().toString());
            }
            // 请求放入 cookie 中
            if (isTicketCookieEnabled()) {
                logger.info("[^_^:20180814-2359-001] 令牌ID 开启了存入 Cookie 中; cookie ");
                ZKCookie secCookie = new ZKDefaultCookie(ZKSecConstants.PARAM_NAME.TicketId);
                secCookie.setValue(tk.getTkId().toString());
                secCookie.setPath("/");
                secCookie.setMaxAge(3000);
                secCookie.saveTo(request, response);
            }
        }
    }

    /**
     * 修改最后访问是时间
     */
    protected void updateTicketTime(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes) {
        ZKSecTicket tk = ZKSecSecurityUtils.getTikcet();
        // Subject should never _ever_ be null, but just in case:
        if (tk != null) {
            try {
                tk.updateLastTime();
            }
            catch(Throwable t) {
                logger.error(
                        "ticket.touch() method invocation has failed.  Unable to update the corresponding ticket's update time based on the incoming request.",
                        t);
            }
        }
    }

}
