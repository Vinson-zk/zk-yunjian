/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKTest.java 
* @author Vinson 
* @Package com.zk.core 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 10, 2024 11:03:28 AM 
* @version V1.0 
*/
package com.zk.core;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKTest {

    @Test
    public void test() {
        try {

            System.out.println("[^_^:2023122-0120-001] System.getProperties(): " + System.getProperties().toString());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
