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
* @Title: ZKSecTestHelper.java 
* @author Vinson 
* @Package com.zk.security 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 2:52:07 PM 
* @version V1.0 
*/
package com.zk.security.helper;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.core.commons.ZKEnvironment;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.security.helper.service.TestService;
import com.zk.security.ticket.ZKSecTicketManager;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSecTestHelper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecTestHelper {


    private static ConfigurableApplicationContext ctxSec = null;
    public static ConfigurableApplicationContext getCtxSec(){
        if(ctxSec == null){
            ctxSec = ZKSecTestHelperSpringBootMain.run(new String[] {});
            ZKEnvironmentUtils.setZKEnvironment(new ZKEnvironment(ctxSec));
        }
        return ctxSec;
    }

    @Test
    public void test() {
        try {
            TestCase.assertNotNull(getCtxSec());

            ZKSecTicketManager tm = getCtxSec().getBean("ticketManager", ZKSecTicketManager.class);
            TestCase.assertNotNull(tm);

            TestService ts = getCtxSec().getBean(TestService.class);
            TestCase.assertNotNull(ts.getTicketManager());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
