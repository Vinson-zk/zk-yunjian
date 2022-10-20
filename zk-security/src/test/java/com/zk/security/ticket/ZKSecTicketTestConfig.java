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
* @Title: ZKSecTicketTestConfig.java 
* @author Vinson 
* @Package com.zk.security.ticket 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 3:03:57 PM 
* @version V1.0 
*/
package com.zk.security.ticket;

import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSecTicketTestConfig 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecTicketTestConfig {

    public static final String config_path = "classpath:ticket/test_ticket_context.xml";

    public static FileSystemXmlApplicationContext ctx;

    static {
        try {
            ctx = new FileSystemXmlApplicationContext(config_path);
            TestCase.assertNotNull(ctx);
            MongoTemplate mongoTemplate = ctx.getBean(MongoTemplate.class);
            TestCase.assertNotNull(mongoTemplate);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void test() {
        TestCase.assertTrue(true);
    }

}
