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
* @Title: ZKSecWebSubjectFactory.java 
* @author Vinson 
* @Package com.zk.security.web.subject 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 27, 2021 11:31:28 PM 
* @version V1.0 
*/
package com.zk.security.web.subject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zk.security.mgt.ZKSecSecurityManager;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.subject.ZKSecSubjectFactory;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.web.mgt.ZKSecWebSecurityManager;

/** 
* @ClassName: ZKSecWebSubjectFactory 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecWebSubjectFactory implements ZKSecSubjectFactory {

    /**
     * 创建一个主体
     * 
     * @return
     */
    @Override
    public ZKSecSubject createSubject(ZKSecSecurityManager securityManager, ZKSecTicket tk) {
        return null;
    }

    public ZKSecWebSubject createSubject(ZKSecWebSecurityManager securityManager, HttpServletRequest request,
            HttpServletResponse response, ZKSecTicket tk) {
        // TODO Auto-generated method stub
        return new ZKSecWebSubject(securityManager, request, response, tk);
    }

}