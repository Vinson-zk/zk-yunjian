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
* @Title: ZKSecDefultSubjectFactory.java 
* @author Vinson 
* @Package com.zk.security.subject 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 7:41:26 PM 
* @version V1.0 
*/
package com.zk.security.subject;

import com.zk.security.mgt.ZKSecSecurityManager;
import com.zk.security.ticket.ZKSecTicket;

/** 
* @ClassName: ZKSecDefultSubjectFactory 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecDefultSubjectFactory implements ZKSecSubjectFactory {

    /**
     * 创建一个主体
     * 
     * @return
     */
    @Override
    public ZKSecSubject createSubject(ZKSecSecurityManager securityManager, ZKSecTicket tk) {
        return new ZKSecDefaultSubject(securityManager, tk);
    }

}
