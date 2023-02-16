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
* @Title: ZKSecMongoTicketManagerTest.java 
* @author Vinson 
* @Package com.zk.security.ticket.support.mongo 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 29, 2021 10:07:06 AM 
* @version V1.0 
*/
package com.zk.security.ticket.support.mongo;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.security.ticket.ZKSecTestHelperTicketTestCtx;
import com.zk.security.ticket.ZKSecTicketManager;
import com.zk.security.ticket.ZKSecTicketManagerTestHelper;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSecMongoTicketManagerTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecMongoTicketManagerTest {

    public static ZKSecTicketManagerTestHelper ticketManagerTestHelper;

    static {
        try {
            ConfigurableApplicationContext ctx = ZKSecTestHelperTicketTestCtx.run(new String[] {});
            ZKSecTicketManager ticketManager = ctx.getBean(ZKSecMongoTicketManager.class);
            TestCase.assertNotNull(ticketManager);
            ticketManagerTestHelper = new ZKSecTicketManagerTestHelper(ticketManager);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 创建权限令牌
     */
    @Test
    public void testCreateSecTicket() {
        ticketManagerTestHelper.testCreateSecTicket();
    }

    /**
     * 创建普通令牌
     */
    @Test
    public void testCreateTicket() {
        ticketManagerTestHelper.testCreateTicket();
    }

    /**
     * 销毁一个令牌
     */
    @Test
    public void testDropTicket() {
        ticketManagerTestHelper.testDropTicket();
    }

    /**
     * 销毁指定类型令牌
     */
    @Test
    public void testDropTicketByType() {
        ticketManagerTestHelper.testDropTicketByType();
    }

    /**
     * 销毁所有令牌
     */
    @Test
    public void testDropAll() {
        ticketManagerTestHelper.testDropAll();
    }

    /**
     * 刷新令牌
     */
    @Test
    public void testUpdateTicket() {
        ticketManagerTestHelper.testUpdateTicket();
    }

    /**
     * 停用令牌 和 启动令牌
     */
    @Test
    public void testStopAndStart() {
        ticketManagerTestHelper.testStopAndStart();
    }

//    /**
//     * 清理过期令牌
//     */
//    @Test
//    public void testValidateTickets() {
//        // 功能未完善；待补充
////        ticketManagerTestHelper.testValidateTickets();
//    }

    /**
     * 根据身份ID取当前身份所拥有的所有令牌
     */
    @Test
    public void testFindTickeByPrincipal() {
        ticketManagerTestHelper.testFindTickeByPrincipal();
    }

}
