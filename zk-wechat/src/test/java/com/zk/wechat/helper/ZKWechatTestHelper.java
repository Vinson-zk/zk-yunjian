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
* @Title: ZKWechatTestHelper.java 
* @author Vinson 
* @Package com.zk.wechat.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 18, 2021 11:28:11 AM 
* @version V1.0 
*/
package com.zk.wechat.helper;
/** 
* @ClassName: ZKWechatTestHelper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.wechat.ZKWechatSpringBootMain;

import junit.framework.TestCase;

public class ZKWechatTestHelper {

    static ConfigurableApplicationContext ctx = null;

    public static ConfigurableApplicationContext getMainCtx() {
        return getCtx();
    }

    public static ConfigurableApplicationContext getCtx() {
        if(ctx == null) {
            ctx = ZKWechatSpringBootMain.run(new String[] {});
        }
        return ctx;
    }

    @Test
    public void testCtx() {
        TestCase.assertNotNull(getCtx());
    }

}
