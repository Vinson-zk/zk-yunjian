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
* @Title: ZKDefaultCountSqlParserTest.java 
* @author Vinson 
* @Package com.zk.db.parser 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 6, 2024 11:24:59 PM 
* @version V1.0 
*/
package com.zk.db.parser;

import org.junit.Test;

import com.zk.db.parser.ZKCountSqlParser.ZKModel;

import junit.framework.TestCase;

/** 
* @ClassName: ZKDefaultCountSqlParserTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDefaultCountSqlParserTest {

    @Test
    public void testGetCountSql() {
        try {
            ZKDefaultCountSqlParser zkSqlParser = new ZKDefaultCountSqlParser();
            String expectedSql = null;
            String sourceSql = "", resSql = null;

            sourceSql = "SELECT c_id AS id FROM table_name t WHERE t.c_w IN (?, ?) AND t.c_v = 1 AND t.c_s LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(?, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%') AND JSON_UNQUOTE(JSON_EXTRACT(t.c_j, ?)) ORDER BY t.c_id ASC";

            expectedSql = "SELECT count(0) FROM (" + sourceSql + ") " + ZKCountSqlParser.COUNT_TABLE_ALIAS;
            resSql = zkSqlParser.getCountSql(sourceSql, "count(0)", ZKModel.Simple, false);
            System.out.println("[^_^:20240106-2300-001-1]   sourceSql: " + sourceSql);
            System.out.println("[^_^:20240106-2300-001-1] expectedSql: " + expectedSql);
            System.out.println("[^_^:20240106-2300-001-1]      resSql: " + resSql);
            TestCase.assertEquals(expectedSql, resSql);

            expectedSql = "SELECT count(0) FROM (SELECT c_id AS id FROM table_name t WHERE t.c_w IN (?, ?) AND t.c_v = 1 AND t.c_s LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(?, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%') AND JSON_UNQUOTE(JSON_EXTRACT(t.c_j, ?))) "
                    + ZKCountSqlParser.COUNT_TABLE_ALIAS;
            resSql = zkSqlParser.getCountSql(sourceSql, "count(0)", ZKModel.Simple, true);
            System.out.println("[^_^:20240106-2300-001-2]   sourceSql: " + sourceSql);
            System.out.println("[^_^:20240106-2300-001-2] expectedSql: " + expectedSql);
            System.out.println("[^_^:20240106-2300-001-2]      resSql: " + resSql);
            TestCase.assertEquals(expectedSql, resSql);

            expectedSql = "SELECT count(0) FROM table_name t WHERE t.c_w IN (?, ?) AND t.c_v = 1 AND t.c_s LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(?, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%') AND JSON_UNQUOTE(JSON_EXTRACT(t.c_j, ?)) ORDER BY t.c_id ASC";
            resSql = zkSqlParser.getCountSql(sourceSql, "count(0)", ZKModel.Perfect, false);
            System.out.println("[^_^:20240106-2300-001-3]   sourceSql: " + sourceSql);
            System.out.println("[^_^:20240106-2300-001-3] expectedSql: " + expectedSql);
            System.out.println("[^_^:20240106-2300-001-3]      resSql: " + resSql);
            TestCase.assertEquals(expectedSql, resSql);

            expectedSql = "SELECT count(0) FROM table_name t WHERE t.c_w IN (?, ?) AND t.c_v = 1 AND t.c_s LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(?, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%') AND JSON_UNQUOTE(JSON_EXTRACT(t.c_j, ?))";
            resSql = zkSqlParser.getCountSql(sourceSql, "count(0)", ZKModel.Perfect, true);
            System.out.println("[^_^:20240106-2300-001-4]   sourceSql: " + sourceSql);
            System.out.println("[^_^:20240106-2300-001-4] expectedSql: " + expectedSql);
            System.out.println("[^_^:20240106-2300-001-4]      resSql: " + resSql);
            TestCase.assertEquals(expectedSql, resSql);
            resSql = zkSqlParser.getCountSql(sourceSql);
            System.out.println("[^_^:20240106-2300-001-5]   sourceSql: " + sourceSql);
            System.out.println("[^_^:20240106-2300-001-5] expectedSql: " + expectedSql);
            System.out.println("[^_^:20240106-2300-001-5]      resSql: " + resSql);
            TestCase.assertEquals(expectedSql, resSql);

            // --------------------------------------------------------------------------------------
            System.out.println("[^_^:20240107-2300-001] ----------------------------------------------");
            sourceSql = "SELECT t1.c_id FROM (SELECT t.c_id FROM t_zk_db_test t GROUP BY t.c_id ORDER BY t.c_id) t1 LEFT JOIN t_zk_db_test t2 ON t2.c_id = t1.c_id ORDER BY t2.c_id";
            expectedSql = "SELECT count(0) FROM (" + sourceSql + ") " + ZKCountSqlParser.COUNT_TABLE_ALIAS;
            resSql = zkSqlParser.getCountSql(sourceSql, "count(0)", ZKModel.Simple, false);
            System.out.println("[^_^:20240107-2300-001-1]   sourceSql: " + sourceSql);
            System.out.println("[^_^:20240107-2300-001-1] expectedSql: " + expectedSql);
            System.out.println("[^_^:20240107-2300-001-1]      resSql: " + resSql);
            TestCase.assertEquals(expectedSql, resSql);

            expectedSql = "SELECT count(0) FROM (SELECT t1.c_id FROM (SELECT t.c_id FROM t_zk_db_test t GROUP BY t.c_id) t1 LEFT JOIN t_zk_db_test t2 ON t2.c_id = t1.c_id) "
                    + ZKCountSqlParser.COUNT_TABLE_ALIAS;
            resSql = zkSqlParser.getCountSql(sourceSql, "count(0)", ZKModel.Simple, true);
            System.out.println("[^_^:20240107-2300-001-2]   sourceSql: " + sourceSql);
            System.out.println("[^_^:20240107-2300-001-2] expectedSql: " + expectedSql);
            System.out.println("[^_^:20240107-2300-001-2]      resSql: " + resSql);
            TestCase.assertEquals(expectedSql, resSql);

            expectedSql = "SELECT count(0) FROM (SELECT t.c_id FROM t_zk_db_test t GROUP BY t.c_id ORDER BY t.c_id) t1 LEFT JOIN t_zk_db_test t2 ON t2.c_id = t1.c_id ORDER BY t2.c_id";
            resSql = zkSqlParser.getCountSql(sourceSql, "count(0)", ZKModel.Perfect, false);
            System.out.println("[^_^:20240107-2300-001-3]   sourceSql: " + sourceSql);
            System.out.println("[^_^:20240107-2300-001-3] expectedSql: " + expectedSql);
            System.out.println("[^_^:20240107-2300-001-3]      resSql: " + resSql);
            TestCase.assertEquals(expectedSql, resSql);

            expectedSql = "SELECT count(0) FROM (SELECT t.c_id FROM t_zk_db_test t GROUP BY t.c_id) t1 LEFT JOIN t_zk_db_test t2 ON t2.c_id = t1.c_id";
            resSql = zkSqlParser.getCountSql(sourceSql, "count(0)", ZKModel.Perfect, true);
            System.out.println("[^_^:20240107-2300-001-4]   sourceSql: " + sourceSql);
            System.out.println("[^_^:20240107-2300-001-4] expectedSql: " + expectedSql);
            System.out.println("[^_^:20240107-2300-001-4]      resSql: " + resSql);
            TestCase.assertEquals(expectedSql, resSql);
            resSql = zkSqlParser.getCountSql(sourceSql);
            System.out.println("[^_^:20240106-2300-001-5]   sourceSql: " + sourceSql);
            System.out.println("[^_^:20240106-2300-001-5] expectedSql: " + expectedSql);
            System.out.println("[^_^:20240106-2300-001-5]      resSql: " + resSql);
            TestCase.assertEquals(expectedSql, resSql);

            // --------------------------------------------------------------------------------------
            System.out.println("[^_^:20240107-2300-002] ----------------------------------------------");
            sourceSql = "SELECT t1.c_id, count(t1.c_id) FROM (SELECT t.c_id FROM t_zk_db_test t GROUP BY t.c_id ORDER BY t.c_id) t1 LEFT JOIN t_zk_db_test t2 ON t2.c_id = t1.c_id GROUP BY t1.c_id ORDER BY t2.c_id";
            expectedSql = "SELECT count(0) FROM (" + sourceSql + ") " + ZKCountSqlParser.COUNT_TABLE_ALIAS;
            resSql = zkSqlParser.getCountSql(sourceSql, "count(0)", ZKModel.Simple, false);
            System.out.println("[^_^:20240107-2300-002-1]   sourceSql: " + sourceSql);
            System.out.println("[^_^:20240107-2300-002-1] expectedSql: " + expectedSql);
            System.out.println("[^_^:20240107-2300-002-1]      resSql: " + resSql);
            TestCase.assertEquals(expectedSql, resSql);

            expectedSql = "SELECT count(0) FROM (SELECT t1.c_id, count(t1.c_id) FROM (SELECT t.c_id FROM t_zk_db_test t GROUP BY t.c_id) t1 LEFT JOIN t_zk_db_test t2 ON t2.c_id = t1.c_id GROUP BY t1.c_id) "
                    + ZKCountSqlParser.COUNT_TABLE_ALIAS;
            resSql = zkSqlParser.getCountSql(sourceSql, "count(0)", ZKModel.Simple, true);
            System.out.println("[^_^:20240107-2300-002-2]   sourceSql: " + sourceSql);
            System.out.println("[^_^:20240107-2300-002-2] expectedSql: " + expectedSql);
            System.out.println("[^_^:20240107-2300-002-2]      resSql: " + resSql);
            TestCase.assertEquals(expectedSql, resSql);

            expectedSql = "SELECT count(0) FROM (" + sourceSql + ") " + ZKCountSqlParser.COUNT_TABLE_ALIAS;
            resSql = zkSqlParser.getCountSql(sourceSql, "count(0)", ZKModel.Perfect, false);
            System.out.println("[^_^:20240107-2300-002-3]   sourceSql: " + sourceSql);
            System.out.println("[^_^:20240107-2300-002-3] expectedSql: " + expectedSql);
            System.out.println("[^_^:20240107-2300-002-3]      resSql: " + resSql);
            TestCase.assertEquals(expectedSql, resSql);

            expectedSql = "SELECT count(0) FROM (SELECT t1.c_id, count(t1.c_id) FROM (SELECT t.c_id FROM t_zk_db_test t GROUP BY t.c_id) t1 LEFT JOIN t_zk_db_test t2 ON t2.c_id = t1.c_id GROUP BY t1.c_id) "
                    + ZKCountSqlParser.COUNT_TABLE_ALIAS;
            resSql = zkSqlParser.getCountSql(sourceSql, "count(0)", ZKModel.Perfect, true);
            System.out.println("[^_^:20240107-2300-002-4]   sourceSql: " + sourceSql);
            System.out.println("[^_^:20240107-2300-002-4] expectedSql: " + expectedSql);
            System.out.println("[^_^:20240107-2300-002-4]      resSql: " + resSql);
            TestCase.assertEquals(expectedSql, resSql);
            resSql = zkSqlParser.getCountSql(sourceSql);
            System.out.println("[^_^:20240106-2300-002-5]   sourceSql: " + sourceSql);
            System.out.println("[^_^:20240106-2300-002-5] expectedSql: " + expectedSql);
            System.out.println("[^_^:20240106-2300-002-5]      resSql: " + resSql);
            TestCase.assertEquals(expectedSql, resSql);

            // --------------------------------------------------------------------------------------
            System.out.println("[^_^:20240107-2300-003] ----------------------------------------------");
            sourceSql = "SELECT count(t1.c_id) FROM (SELECT t.c_id FROM t_zk_db_test t GROUP BY t.c_id ORDER BY t.c_id) t1 LEFT JOIN t_zk_db_test t2 ON t2.c_id = t1.c_id ORDER BY t2.c_id";
            expectedSql = "SELECT count(0) FROM (" + sourceSql + ") " + ZKCountSqlParser.COUNT_TABLE_ALIAS;
            resSql = zkSqlParser.getCountSql(sourceSql, "count(0)", ZKModel.Perfect, false);
            System.out.println("[^_^:20240107-2300-003-1]   sourceSql: " + sourceSql);
            System.out.println("[^_^:20240107-2300-003-1] expectedSql: " + expectedSql);
            System.out.println("[^_^:20240107-2300-003-1]      resSql: " + resSql);
            TestCase.assertEquals(expectedSql, resSql);

            expectedSql = "SELECT count(0) FROM (SELECT count(t1.c_id) FROM (SELECT t.c_id FROM t_zk_db_test t GROUP BY t.c_id) t1 LEFT JOIN t_zk_db_test t2 ON t2.c_id = t1.c_id) "
                    + ZKCountSqlParser.COUNT_TABLE_ALIAS;
            resSql = zkSqlParser.getCountSql(sourceSql, "count(0)", ZKModel.Perfect, true);
            System.out.println("[^_^:20240107-2300-003-2]   sourceSql: " + sourceSql);
            System.out.println("[^_^:20240107-2300-003-2] expectedSql: " + expectedSql);
            System.out.println("[^_^:20240107-2300-003-2]      resSql: " + resSql);
            TestCase.assertEquals(expectedSql, resSql);
            resSql = zkSqlParser.getCountSql(sourceSql);
            System.out.println("[^_^:20240106-2300-003-2]   sourceSql: " + sourceSql);
            System.out.println("[^_^:20240106-2300-003-2] expectedSql: " + expectedSql);
            System.out.println("[^_^:20240106-2300-003-2]      resSql: " + resSql);
            TestCase.assertEquals(expectedSql, resSql);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

//    @Test
//    public void testGetPageSql() {
//        try {
//            ZKDialect dialect = new ZKMySqlDialect();
//            String expectedSql = null;
//            String sourceSql = "", resSql = null;
//
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//            TestCase.assertTrue(false);
//        }
//    }

}
