/** 
* Copyright (c) 2012-2025 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKWebUtilsTest.java 
* @author Vinson 
* @Package com.zk.core.web.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 8, 2025 5:41:08 PM 
* @version V1.0 
*/
package com.zk.core.web.utils;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKWebUtilsTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWebUtilsTest {

    @Test
    public void testCleanInetAddressByRemoteAddr() {
        try {
            String ip, resIp, expectedIp;

            ip = null;
            expectedIp = null;
            resIp = ZKWebUtils.cleanInetAddressByRemoteAddr(ip);
            TestCase.assertEquals(expectedIp, resIp);

            ip = "";
            expectedIp = "";
            resIp = ZKWebUtils.cleanInetAddressByRemoteAddr(ip);
            TestCase.assertEquals(expectedIp, resIp);

            ip = "localhost";
            expectedIp = "127.0.0.1";
            resIp = ZKWebUtils.cleanInetAddressByRemoteAddr(ip);
            TestCase.assertEquals(expectedIp, resIp);

            ip = "127.0.0.1";
            expectedIp = "127.0.0.1";
            resIp = ZKWebUtils.cleanInetAddressByRemoteAddr(ip);
            TestCase.assertEquals(expectedIp, resIp);

            ip = "0:0:0:0:0:0:0:1";
            expectedIp = "127.0.0.1";
            resIp = ZKWebUtils.cleanInetAddressByRemoteAddr(ip);
            TestCase.assertEquals(expectedIp, resIp);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }

    }

}
