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
* @Title: ZKSecWebSubject.java 
* @author Vinson 
* @Package com.zk.security.web.subject 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 27, 2021 10:28:04 PM 
* @version V1.0 
*/
package com.zk.security.web.subject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zk.security.subject.ZKSecDefaultSubject;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.web.mgt.ZKSecWebSecurityManager;

/** 
* @ClassName: ZKSecWebSubject 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecWebSubject extends ZKSecDefaultSubject {

    public ZKSecWebSubject(ZKSecWebSecurityManager securityManager, HttpServletRequest request,
            HttpServletResponse response, ZKSecTicket tk) {
        super(securityManager, tk);
        // 如果权限管理 webSecManager，response，request  为空，抛出 SecException 异常
        this.request = request;
        this.response = response;
        if(tk != null){
            this.setTicket(tk);
        }
    }

    @Override
    public ZKSecWebSecurityManager getSecurityManager() {
        return (ZKSecWebSecurityManager) super.getSecurityManager();
    }

    protected HttpServletRequest request;

    protected HttpServletResponse response;

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

}
