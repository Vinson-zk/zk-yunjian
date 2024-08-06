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
* @Title: TestDemo.java 
* @author Vinson 
* @Package com.zk.db 
* @Description: TODO(simple description this file what to do. ) 
* @date Sep 10, 2020 11:15:01 AM 
* @version V1.0 
*/
package com.zk.db;

import com.zk.db.helper.dao.ZKDBTestDao;
import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: TestDemoTest
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class TestDemoTest {

    @Test
    public void test() {

        try {
            System.out.println("[^_^:20220928-1121-001]" + ZKDBTestDao.class.getName());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
