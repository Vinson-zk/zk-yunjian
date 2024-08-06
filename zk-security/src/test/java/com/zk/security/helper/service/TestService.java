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
* @Title: TestService.java 
* @author Vinson 
* @Package com.zk.security.helper.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 3:00:14 PM 
* @version V1.0 
*/
package com.zk.security.helper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zk.security.ticket.ZKSecTicketManager;

/** 
* @ClassName: TestService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
public class TestService {

    @Autowired
    ZKSecTicketManager ticketManager;

    public ZKSecTicketManager getTicketManager() {
        return ticketManager;
    }

}
