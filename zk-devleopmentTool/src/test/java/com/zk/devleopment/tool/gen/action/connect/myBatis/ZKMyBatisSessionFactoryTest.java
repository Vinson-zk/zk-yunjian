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
* @Title: ZKMyBatisSessionFactoryTest.java 
* @author Vinson 
* @Package com.zk.code.generate.connect.myBatis 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 25, 2021 12:09:39 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.action.connect.myBatis;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import com.zk.devleopment.tool.ZKDevleopmentToolTestHelper;
import com.zk.devleopment.tool.gen.action.ZKCodeGenConstant;
import com.zk.devleopment.tool.gen.entity.ZKModule;

import junit.framework.TestCase;

/** 
* @ClassName: ZKMyBatisSessionFactoryTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMyBatisSessionFactoryTest {

    @Test
    public void openSession() {
        try {
            ZKModule zkModule = null;
            SqlSession session = null;

            ZKMyBatisSessionFactory.createSessionFactory(ZKDevleopmentToolTestHelper.config);
            session = ZKMyBatisSessionFactory.openSession();
            TestCase.assertNotNull(session);

            ZKMyBatisSessionFactory.clean();
            zkModule = ZKDevleopmentToolTestHelper.getTestModule();
            ZKMyBatisSessionFactory.createSessionFactory(ZKMyBatisSessionFactory.createDataSource(zkModule),
                    ZKCodeGenConstant.mappers);
            session = ZKMyBatisSessionFactory.openSession();
            TestCase.assertNotNull(session);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
