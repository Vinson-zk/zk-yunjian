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
* @Title: ZKGetTableInfoUtilsTest.java 
* @author Vinson 
* @Package com.zk.code.generate.tableInfo 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 25, 2021 2:26:31 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.action.tableInfo;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.zk.devleopment.tool.ZKDevleopmentToolTestHelper;
import com.zk.devleopment.tool.gen.entity.ZKColInfo;
import com.zk.devleopment.tool.gen.entity.ZKModule;

import junit.framework.TestCase;

/** 
* @ClassName: ZKGetTableInfoUtilsTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKGetTableInfoUtilsTest {

    @Test
    public void testGetTables() {
        try {
            ZKModule zkModule;
            List<Map<String, Object>> zkTableInfos;

            zkModule = ZKDevleopmentToolTestHelper.getTestModule();
            zkTableInfos = ZKGetTableInfoUtils.getDbTableInfos(zkModule, null);

            TestCase.assertFalse(zkTableInfos.isEmpty());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testGetColInfos() {
        try {

            ZKModule zkModule;
            String tableName;
            List<Map<String, Object>> dbColInfos;
            ZKColInfo col;
            List<String> pkList;

            zkModule = ZKDevleopmentToolTestHelper.getTestModule();
            tableName = "sys_config";
            dbColInfos = ZKGetTableInfoUtils.getDbColInfos(zkModule, tableName);
            pkList = ZKGetTableInfoUtils.getPkSourceList(zkModule, tableName);

            TestCase.assertNotNull(dbColInfos);
            TestCase.assertEquals(4, dbColInfos.size());
            for (Map<String, Object> dbColInfo : dbColInfos) {
                col = ZKGetTableInfoUtils.makeColInfo(dbColInfo, pkList);

                if ("variable".equals(col.getColName())) {
                    TestCase.assertFalse(col.getColIsNull());
                    TestCase.assertTrue(col.getColIsPK());
                }
                if ("value".equals(col.getColName())) {
                    TestCase.assertTrue(col.getColIsNull());
                    TestCase.assertFalse(col.getColIsPK());
                }
                if ("set_time".equals(col.getColName())) {
                    TestCase.assertTrue(col.getColIsNull());
                    TestCase.assertFalse(col.getColIsPK());
                }
                if ("set_by".equals(col.getColName())) {
                    TestCase.assertTrue(col.getColIsNull());
                    TestCase.assertFalse(col.getColIsPK());
                }
            }

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
