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
* @Title: ZKSecValidUtilsTest.java 
* @author Vinson 
* @Package com.zk.security.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 3:07:20 PM 
* @version V1.0 
*/
package com.zk.security.utils;

import org.junit.Test;

import com.zk.security.helper.ZKSecTestHelper;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSecValidUtilsTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecValidUtilsTest {

    @Test
    public void testLoginFailNum() {
        try {
            TestCase.assertNotNull(ZKSecTestHelper.getCtxSec());

            String host = "127.0.1.1";
            int num = 7;
            ZKSecValidUtils.putLoginFailNum(host, num);
            TestCase.assertEquals(7, ZKSecValidUtils.getLoginFailNum(host));
            ZKSecValidUtils.cleanLoginFailNum(host);
            TestCase.assertEquals(0, ZKSecValidUtils.getLoginFailNum(host));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testCode() {
        try {
            TestCase.assertNotNull(ZKSecTestHelper.getCtxSec());
            String tkId = "test_token_ticket";
            String code = "code";
            ZKSecValidUtils.putValidCode(tkId, code);
            TestCase.assertEquals(code, ZKSecValidUtils.getValidCode(tkId));
            ZKSecValidUtils.cleanValidCodeCache(tkId);
            TestCase.assertNull(ZKSecValidUtils.getValidCode(tkId));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
