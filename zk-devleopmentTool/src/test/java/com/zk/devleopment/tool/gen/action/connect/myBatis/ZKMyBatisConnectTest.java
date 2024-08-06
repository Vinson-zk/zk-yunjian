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
* @Title: ZKMyBatisConnectTest.java 
* @author Vinson 
* @Package com.zk.code.generate.connect.myBatis 
* @Description: TODO(simple description this file what to do.) 
* @date Mar 18, 2020 2:24:24 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.action.connect.myBatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import com.zk.core.utils.ZKJsonUtils;
import com.zk.devleopment.tool.ZKDevleopmentToolTestHelper;
import com.zk.devleopment.tool.gen.action.ZKCodeGenConstant;
import com.zk.devleopment.tool.gen.entity.ZKModule;

import junit.framework.TestCase;

/** 
* @ClassName: ZKMyBatisConnectTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMyBatisConnectTest {

    @Test
    public void testGetColumnSourceList() {
        try {

            SqlSession session = null;

            ZKMyBatisSessionFactory.clean();
            ZKMyBatisSessionFactory.createSessionFactory(ZKDevleopmentToolTestHelper.config);
            session = ZKMyBatisSessionFactory.openSession();
            TestCase.assertNotNull(session);

            ZKMyBatisConnect mbConnect = new ZKMyBatisConnect(session);
            String tableName = "sys_config";
            String dbType = "mysql";
            List<Map<String, Object>> colList = mbConnect.getColumnSourceList(dbType, tableName);
            TestCase.assertNotNull(colList);
            TestCase.assertEquals(4, colList.size());
            System.out.println("[^_^:20170529-2114-001] pkList.size=" + colList.size());
            System.out.println("[^_^:20170529-2114-001] pkList.0=" + ZKJsonUtils.toJsonStr(colList.get(0)));

            mbConnect.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testGetPkSourceList() {
        try {

            ZKModule zkModule = null;
            SqlSession session = null;

            zkModule = ZKDevleopmentToolTestHelper.getTestModule();

            ZKMyBatisSessionFactory.clean();
            ZKMyBatisSessionFactory.createSessionFactory(ZKMyBatisSessionFactory.createDataSource(zkModule),
                    ZKCodeGenConstant.mappers);
            session = ZKMyBatisSessionFactory.openSession();
            TestCase.assertNotNull(session);

            ZKMyBatisConnect mbConnect = new ZKMyBatisConnect(session);
            String tableName = "sys_config";
            String dbType = "mysql";
            List<String> pkList = mbConnect.getPkSourceList(dbType, tableName);
            TestCase.assertNotNull(pkList);
            TestCase.assertEquals(1, pkList.size());
            System.out.println("[^_^:20170529-2114-002] pkList.size=" + pkList.size());
            System.out.println("[^_^:20170529-2114-002] pkList=" + ZKJsonUtils.toJsonStr(pkList));

            mbConnect.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
