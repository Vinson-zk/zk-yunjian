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
* @Title: ZKConvertUtilsTest.java 
* @author Vinson 
* @Package com.zk.code.generate.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 24, 2021 6:12:37 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.action;

import org.junit.Test;

import com.zk.devleopment.tool.ZKDevleopmentToolTestHelper;
import com.zk.devleopment.tool.gen.entity.ZKColInfo;
import com.zk.devleopment.tool.gen.entity.ZKModule;
import com.zk.devleopment.tool.gen.entity.ZKTableInfo;

import junit.framework.TestCase;

/** 
* @ClassName: ZKConvertUtilsTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKConvertUtilsTest {

    @Test
    public void testConvert() {

        ZKModule zkModule;
        ZKTableInfo zkTableInfo;
        String namePrefix;

        zkModule = ZKDevleopmentToolTestHelper.getTestModule();
        zkTableInfo = ZKDevleopmentToolTestHelper.getTestTable(zkModule);

        namePrefix = zkModule.getModulePrefix() + zkModule.getModuleNameCap() + zkTableInfo.getSubModuleName();
        zkModule.setIsRemovePrefix(true);
        ZKConvertUtils.convertTableInfo(zkModule, zkTableInfo);
        TestCase.assertEquals(namePrefix + "TestTableGen", zkTableInfo.getClassName());

        zkModule.setIsRemovePrefix(false);
        ZKConvertUtils.convertTableInfo(zkModule, zkTableInfo);
        TestCase.assertEquals(namePrefix + "TTestTableGen", zkTableInfo.getClassName());
    }

    /**
     * 测试用表名制作 java 类名
     */
    @Test
    public void testConvertTableName() {
        try {
            String tableName = "t_student";
            String className = "Student";
            String transferName = "";

            tableName = "t_student";
            className = "Student";
            transferName = "";
            transferName = ZKConvertUtils.convertTableName("t_", "", tableName);
            System.out.println("[^_^:201705271153-001] " + transferName);
            TestCase.assertEquals(className, transferName);

            tableName = "t_student_aaa";
            className = "StudentAaa";
            transferName = "";
            transferName = ZKConvertUtils.convertTableName("t_", "", tableName);
            System.out.println("[^_^:201705271153-002] " + transferName);
            TestCase.assertEquals(className, transferName);

            tableName = "d_student_aaa";
            className = "DStudentAaa";
            transferName = "";
            transferName = ZKConvertUtils.convertTableName("t_", "", tableName);
            System.out.println("[^_^:201705271153-003] " + transferName);
            TestCase.assertEquals(className, transferName);

            tableName = "_student_aaa";
            className = "StudentAaa";
            transferName = "";
            transferName = ZKConvertUtils.convertTableName("_", "", tableName);
            System.out.println("[^_^:201705271153-004] " + transferName);
            TestCase.assertEquals(className, transferName);

            tableName = "t_cds_student_aaa";
            className = "StudentAaa";
            transferName = "";
            transferName = ZKConvertUtils.convertTableName("t_cds_", "", tableName);
            System.out.println("[^_^:201705271153-005] " + transferName);
            TestCase.assertEquals(className, transferName);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 测试用字段名制作 java 类属性名
     */
    @Test
    public void testConvertColName() {
        try {
            String columnName = "c_name";
            String attributeName = "name";
            String transferName = "";

            columnName = "c_name";
            attributeName = "name";
            transferName = "";
            transferName = ZKConvertUtils.convertColName("c_", columnName);
            System.out.println("[^_^:201705271153-004] " + transferName);
            TestCase.assertEquals(attributeName, transferName);

            columnName = "c_name_ddd";
            attributeName = "nameDdd";
            transferName = "";
            transferName = ZKConvertUtils.convertColName("c_", columnName);
            System.out.println("[^_^:201705271153-005] " + transferName);
            TestCase.assertEquals(attributeName, transferName);

            columnName = "_c_name_ddd";
            attributeName = "cNameDdd";
            transferName = "";
            transferName = ZKConvertUtils.convertColName("c_", columnName);
            System.out.println("[^_^:201705271153-006] " + transferName);
            TestCase.assertEquals(attributeName, transferName);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 测试根据字段 jdbc 类型制作 java 属性类型
     */
    @Test
    public void testConvertJdbcType() {
        try {
            ZKColInfo zkCol = new ZKColInfo();
            String jdbcType = "";
            String attributeType = "";
            String transferType = "";

            jdbcType = "VARCHAR(200)";
            attributeType = "String";
            ZKConvertUtils.convertColClass(jdbcType, zkCol);
            transferType = zkCol.getAttrType();
            System.out.println("[^_^:201705271153-007] " + transferType);
            TestCase.assertEquals(attributeType, transferType);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
