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
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zk.security.helper.service.TestService;
import com.zk.security.ticket.ZKSecTicketManager;
import com.zk.security.utils.ZKSecTestHelperTestCtx;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSecTestHelper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecTestHelper {

    public static final String[] config_sprint_paths = new String[] { "classpath:test_spring_ctx.xml" };
    private static FileSystemXmlApplicationContext ctxSpring = null;
    public static FileSystemXmlApplicationContext getCtxSpring(){
        if(ctxSpring == null){
            ctxSpring = new FileSystemXmlApplicationContext(config_sprint_paths);
        }
        return ctxSpring;
    }

    private static ConfigurableApplicationContext ctxSecWeb = null;
    public static ConfigurableApplicationContext getCtxSecWeb(){
        if(ctxSecWeb == null){
            ctxSecWeb = ZKSecTestHelperSpringBootMain.run(new String[]{});
        }
        return ctxSecWeb;
    }

    private static ConfigurableApplicationContext ctxSec = null;
    public static ConfigurableApplicationContext getCtxSec(){
        if(ctxSec == null){
            ctxSec = ZKSecTestHelperTestCtx.run(new String[]{});
        }
        return ctxSec;
    }

    @Test
    public void test() {
        try {
            TestCase.assertNotNull(getCtxSpring());
            TestCase.assertNotNull(getCtxSecWeb());
            TestCase.assertNotNull(getCtxSec());

            ZKSecTicketManager tm = getCtxSecWeb().getBean("ticketManager", ZKSecTicketManager.class);
            TestCase.assertNotNull(tm);

            TestService ts = getCtxSecWeb().getBean(TestService.class);
            TestCase.assertNotNull(ts.getTicketManager());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
