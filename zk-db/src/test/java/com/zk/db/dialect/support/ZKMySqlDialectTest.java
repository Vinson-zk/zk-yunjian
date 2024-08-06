/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKMySqlDialectTest.java 
 * @author Vinson 
 * @Package com.zk.db.dialect.support 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 11:21:11 AM 
 * @version V1.0   
*/
package com.zk.db.dialect.support;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.db.dialect.ZKDialect;

import junit.framework.TestCase;

/** 
* @ClassName: ZKMySqlDialectTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMySqlDialectTest {

    @SuppressWarnings("deprecation")
    @Test
    public void testReplaceLikeParam() {
        try {
            ZKMySqlDialect dialect = new ZKMySqlDialect();

            String str, expectedStr;

//          str = " select * from talbe t where t.column1 LIKE #{obj.column1} and t.column2 LIKE #{obj.column2} ";
//          expectedStr = " select * from talbe t where t.column1 LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{obj.column1}, '%', '\\%'), '_', '\\_'), '\\\\', '\\\\\\\\'), '%') and t.column2 LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{obj.column2}, '%', '\\%'), '_', '\\_'), '\\\\', '\\\\\\\\'), '%') ";
            str = " select * from talbe t where t.column1 LIKE ? and t.column2 LIKE ? ";
            expectedStr = " select * from talbe t where t.column1 LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(?, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%') and t.column2 LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(?, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%') ";
            System.out.println("[^_^:20180109-0039-001]\n" + dialect.replaceLikeParam(str));
            TestCase.assertEquals(expectedStr, dialect.replaceLikeParam(str));

//          str = "select * from talbe where column1 LIKE #{column1} and column2 LIKE #{column2}";
//          expectedStr = "select * from talbe where column1 LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{column1}, '%', '\\%'), '_', '\\_'), '\\\\', '\\\\\\\\'), '%') and column2 LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(#{column2}, '%', '\\%'), '_', '\\_'), '\\\\', '\\\\\\\\'), '%')";
            str = "select * from talbe where column1 LIKE ? and column2 LIKE ?";
            expectedStr = "select * from talbe where column1 LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(?, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%') and column2 LIKE CONCAT('%', REPLACE(REPLACE(REPLACE(?, '\\\\', '\\\\\\\\'), '_', '\\_'), '%', '\\%'), '%')";
            System.out.println("[^_^:20180109-0039-002]\n" + dialect.replaceLikeParam(str));
            TestCase.assertEquals(expectedStr, dialect.replaceLikeParam(str));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testGetInsertStr() {
        try {
            ZKDialect dialect = new ZKMySqlDialect();
            String expectedStr = "";
            ZKJson json = null;
            String str = "";

            str = (String) dialect.getInsertParam(json);
            expectedStr = null;
            TestCase.assertEquals(expectedStr, str);

            json = new ZKJson();
            expectedStr = null;
            str = (String) dialect.getInsertParam(json);
//          System.out.println("==== 请将结果做成sql【SELECT JSON_MERGE_PATCH('{}', [result str]) 】执行且对比 ：expectedStr、str");
            System.out.println(expectedStr);
            System.out.println(str);
            TestCase.assertEquals(expectedStr, str);

            String specialStr = "~!@#$%^&*()_+{}|:\"<>?`1234567890-=[]\\;',./'--";
            json.put("k0_null", null);
            json.put("k1_String", "v1[" + specialStr + "]v1--end");
            json.put("k2_Date", ZKDateUtils.parseDate("2018-03-11 21:53:59"));
            json.put("k3_int", 3);
            json.put("k4_long", 4l);
            json.put("k5_float", 5.5f);
            json.put("k6_double", 6.6d);
            json.put("k7_BigDecimal", new BigDecimal("7.7"));
            json.put("k8_BigInteger", new BigInteger("8"));
            json.put("k9_boolean", true);
            json.put("k10_String_s", new String[] { specialStr, specialStr });
            json.put("k10_Integer_s", new Integer[] { 1, 2 });
            ZKJson json2 = new ZKJson();
            json2.put("jk1", "jv1[" + specialStr + "]jv1--end");
            json2.put("jk2", new String[] { specialStr, specialStr });
            json.put("jk1_json", json2);
            ZKJson json3 = new ZKJson();
            json.put("jk2_json_empty", json3);

            expectedStr = "'{\"k6_double\":6.6,\"k3_int\":3,\"k9_boolean\":\"true\",\"k2_Date\":1520776439000,\"k10_Integer_s\":\"[1,2]\",\"k7_BigDecimal\":7.7,\"k10_String_s\":\"[\\\\\"~!@#$%^&*()_+\\{\\}|:\\\\\\\\\\\\\"<>?`1234567890-=[]\\\\\\\\\\\\\\\\;\\',./\\'--\\\\\",\\\\\"~!@#$%^&*()_+\\{\\}|:\\\\\\\\\\\\\"<>?`1234567890-=[]\\\\\\\\\\\\\\\\;\\',./\\'--\\\\\"]\",\"k5_float\":5.5,\"k1_String\":\"v1[~!@#$%^&*()_+\\{\\}|:\\\\\"<>?`1234567890-=[]\\\\\\\\;\\',./\\'--]v1--end\",\"k4_long\":4,\"k8_BigInteger\":8,\"jk1_json\":{\"jk2\":\"[\\\\\"~!@#$%^&*()_+\\{\\}|:\\\\\\\\\\\\\"<>?`1234567890-=[]\\\\\\\\\\\\\\\\;\\',./\\'--\\\\\",\\\\\"~!@#$%^&*()_+\\{\\}|:\\\\\\\\\\\\\"<>?`1234567890-=[]\\\\\\\\\\\\\\\\;\\',./\\'--\\\\\"]\",\"jk1\":\"jv1[~!@#$%^&*()_+\\{\\}|:\\\\\"<>?`1234567890-=[]\\\\\\\\;\\',./\\'--]jv1--end\"}}'";
            expectedStr = ZKJsonUtils.toJsonStr(json);
            str = (String) dialect.getInsertParam(json);
            System.out.println("==== 请将结果做成sql【SELECT JSON_MERGE_PATCH('{}', [result str]) 】执行且对比 ：expectedStr、str");
            System.out.println(expectedStr);
            System.out.println(str);
            TestCase.assertEquals(expectedStr, str);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testGetUpdateStr() {
        try {
            ZKDialect dialect = new ZKMySqlDialect();
            String expectedStr = null;
            ZKJson json = null;
            String str = "";

            str = (String) dialect.getUpdateParam(json);
            TestCase.assertEquals(str, null);

            json = new ZKJson();
            expectedStr = null;
            str = (String) dialect.getUpdateParam(json);
//          System.out.println("==== 请将结果做成sql【SELECT JSON_MERGE_PATCH('{}', [result str])】执行且对比 ：expectedStr、str");
            System.out.println(expectedStr);
            System.out.println(str);
            TestCase.assertEquals(expectedStr, str);

            String specialStr = "~!@#$%^&*()_+{}|:\"<>?`1234567890-=[]\\;',./'--";
            json.put("k0_null", null);
            json.put("k1_String", "v1[" + specialStr + "]v1--end");
            json.put("k2_Date", ZKDateUtils.parseDate("2018-03-11 21:53:59"));
            json.put("k3_int", 3);
            json.put("k4_long", 4l);
            json.put("k5_float", 5.5f);
            json.put("k6_double", 6.6d);
            json.put("k7_BigDecimal", new BigDecimal("7.7"));
            json.put("k8_BigInteger", new BigInteger("8"));
            json.put("k9_boolean", true);
            json.put("k10_String_s", new String[] { specialStr, specialStr });
            json.put("k10_Integer_s", new Integer[] { 1, 2 });
            ZKJson json2 = new ZKJson();
            json2.put("jk1", "jv1[" + specialStr + "]jv1--end");
            json2.put("jk2", new String[] { specialStr, specialStr });
            json.put("jk1_json", json2);
            ZKJson json3 = new ZKJson();
            json.put("jk2_json_empty", json3);

            expectedStr = "'{\"k6_double\":6.6,\"k3_int\":3,\"k9_boolean\":\"true\",\"k2_Date\":1520776439000,\"k10_Integer_s\":\"[1,2]\",\"k7_BigDecimal\":7.7,\"k10_String_s\":\"[\\\\\"~!@#$%^&*()_+\\{\\}|:\\\\\\\\\\\\\"<>?`1234567890-=[]\\\\\\\\\\\\\\\\;\\',./\\'--\\\\\",\\\\\"~!@#$%^&*()_+\\{\\}|:\\\\\\\\\\\\\"<>?`1234567890-=[]\\\\\\\\\\\\\\\\;\\',./\\'--\\\\\"]\",\"k5_float\":5.5,\"k1_String\":\"v1[~!@#$%^&*()_+\\{\\}|:\\\\\"<>?`1234567890-=[]\\\\\\\\;\\',./\\'--]v1--end\",\"k4_long\":4,\"k8_BigInteger\":8,\"jk1_json\":{\"jk2\":\"[\\\\\"~!@#$%^&*()_+\\{\\}|:\\\\\\\\\\\\\"<>?`1234567890-=[]\\\\\\\\\\\\\\\\;\\',./\\'--\\\\\",\\\\\"~!@#$%^&*()_+\\{\\}|:\\\\\\\\\\\\\"<>?`1234567890-=[]\\\\\\\\\\\\\\\\;\\',./\\'--\\\\\"]\",\"jk1\":\"jv1[~!@#$%^&*()_+\\{\\}|:\\\\\"<>?`1234567890-=[]\\\\\\\\;\\',./\\'--]jv1--end\"}}'";
            expectedStr = ZKJsonUtils.toJsonStr(json);
            str = (String) dialect.getUpdateParam(json);
            System.out.println("==== 请将结果做成sql【SELECT JSON_MERGE_PATCH('{}', [result str])】执行且对比 ：expectedStr、str");
            System.out.println(expectedStr);
            System.out.println(str);
            TestCase.assertEquals(expectedStr, str);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testGetSelectStr() {
        try {
            ZKDialect dialect = new ZKMySqlDialect();
            String expectedStr = null;
            ZKJson json = null;
            String str = "";

            str = (String) dialect.getSelectParam(json);
            TestCase.assertEquals(str, null);

            json = new ZKJson();
            expectedStr = null;
            str = (String) dialect.getSelectParam(json);
//          System.out.println("==== 请将结果做成sql【SELECT JSON_CONTAINS('{}', [result str])】执行且对比 ：expectedStr、str");
            System.out.println(expectedStr);
            System.out.println(dialect.getSelectParam(json));
            TestCase.assertEquals(expectedStr, str);

            String specialStr = "~!@#$%^&*()_+{}|:\"<>?`1234567890-=[]\\;',./'--";
            json.put("k0_null", null);
            json.put("k1_String", "v1[" + specialStr + "]v1--end");
            json.put("k2_Date", ZKDateUtils.parseDate("2018-03-11 21:53:59"));
            json.put("k3_int", 3);
            json.put("k4_long", 4l);
            json.put("k5_float", 5.5f);
            json.put("k6_double", 6.6d);
            json.put("k7_BigDecimal", new BigDecimal("7.7"));
            json.put("k8_BigInteger", new BigInteger("8"));
            json.put("k9_boolean", true);
            json.put("k10_String_s", new String[] { specialStr, specialStr });
            json.put("k10_Integer_s", new Integer[] { 1, 2 });
            ZKJson json2 = new ZKJson();
            json2.put("jk1", "jv1[" + specialStr + "]jv1--end");
            json2.put("jk2", new String[] { specialStr, specialStr });
            json.put("jk1_json", json2);
            ZKJson json3 = new ZKJson();
            json.put("jk2_json_empty", json3);

            expectedStr = "'jv1[~!@#$%^&*()_+\\{\\}|:\\\\\"<>?`1234567890-=[]\\\\\\\\;\\',./\\'--]jv1--end', '$.jk1_json.jk1'";
            expectedStr = ZKJsonUtils.toJsonStr(json);
            str = (String) dialect.getSelectParam(json);
            System.out.println("==== 请将结果做成sql【SELECT JSON_CONTAINS('{}', [result str])】执行且对比 ：expectedStr、str");
            System.out.println(expectedStr);
            System.out.println(str);
            TestCase.assertEquals(expectedStr, str);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testIsPage() {
        try {
            String s = "SELECT \n" + "t.uuid AS \"uuid\" \n"
                    + " ? AND json_extract(t.name, concat('$.', ?)) LIKE #{test} \n and LIKE #{test} \n and LIKE  concat('%', REPLACE(REPLACE(#{test})";
            ZKMySqlDialect mySqlDialect = new ZKMySqlDialect();

            String str = s;
            System.out.println("======  " + mySqlDialect.isPage(str) + "\n" + str);
            TestCase.assertFalse(mySqlDialect.isPage(str));

            str = s + "limit";
            System.out.println("======  " + mySqlDialect.isPage(str) + "\n" + str);
            TestCase.assertFalse(mySqlDialect.isPage(str));

            str = s + " limit";
            System.out.println("======  " + mySqlDialect.isPage(str) + "\n" + str);
            TestCase.assertFalse(mySqlDialect.isPage(str));

            str = s + " limit ";
            System.out.println("======  " + mySqlDialect.isPage(str) + "\n" + str);
            TestCase.assertTrue(mySqlDialect.isPage(str));

            str = s + " limit sadfas ";
            System.out.println("======  " + mySqlDialect.isPage(str) + "\n" + str);
            TestCase.assertTrue(mySqlDialect.isPage(str));

            str = s + " count ";
            System.out.println("======  " + mySqlDialect.isPage(str) + "\n" + str);
            TestCase.assertFalse(mySqlDialect.isPage(str));

            str = s + "count(";
            System.out.println("======  " + mySqlDialect.isPage(str) + "\n" + str);
            TestCase.assertFalse(mySqlDialect.isPage(str));
            str = s + "count()";
            System.out.println("======  " + mySqlDialect.isPage(str) + "\n" + str);
            TestCase.assertFalse(mySqlDialect.isPage(str));

            str = s + "count(~!@#$%^&*()_+{}|:\"<>?/.,';][\\=-`')";
            System.out.println("======  " + mySqlDialect.isPage(str) + "\n" + str);
            TestCase.assertTrue(mySqlDialect.isPage(str));
            str = s + " count(~!@#$%^&*()_+{}|:\"<>?/.,';][\\=-`')";
            System.out.println("======  " + mySqlDialect.isPage(str) + "\n" + str);
            TestCase.assertTrue(mySqlDialect.isPage(str));
            str = s + " count(~!@#$%^&*()_+{}|:\"<>?/.,';][\\=-`') ";
            System.out.println("======  " + mySqlDialect.isPage(str) + "\n" + str);
            TestCase.assertTrue(mySqlDialect.isPage(str));

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsReplace() {
        try {
            String s = "SELECT \n" + "t.uuid AS \"uuid\" \n"
                    + " ? AND json_extract(t.name, concat('$.', ?)) LIKE #{test} \n and LIKE #{test} \n and LIKE  concat('%', ";
            ZKMySqlDialect mySqlDialect = new ZKMySqlDialect();

            String str = s;
            System.out.println("======  " + mySqlDialect.isPage(str) + "\n" + str);
            TestCase.assertFalse(mySqlDialect.isReplace(str));

            str = s + "Replace(";
            System.out.println("======  " + mySqlDialect.isPage(str) + "\n" + str);
            TestCase.assertFalse(mySqlDialect.isReplace(str));

            str = s + " Replace( ";
            System.out.println("======  " + mySqlDialect.isPage(str) + "\n" + str);
            TestCase.assertFalse(mySqlDialect.isReplace(str));

            str = s + "Replace()";
            System.out.println("======  " + mySqlDialect.isPage(str) + "\n" + str);
            TestCase.assertFalse(mySqlDialect.isReplace(str));

            str = s + "Replace(~!@#$%^&*()_+{}|:\"<>?/.,';][\\=-`')";
            System.out.println("======  " + mySqlDialect.isPage(str) + "\n" + str);
            TestCase.assertTrue(mySqlDialect.isReplace(str));

            str = s + " Replace(~!@#$%^&*()_+{}|:\"<>?/.,';][\\=-`')";
            System.out.println("======  " + mySqlDialect.isPage(str) + "\n" + str);
            TestCase.assertTrue(mySqlDialect.isReplace(str));
            str = s + " Replace(~!@#$%^&*()_+{}|:\"<>?/.,';][\\=-`') ";
            System.out.println("======  " + mySqlDialect.isPage(str) + "\n" + str);
            TestCase.assertTrue(mySqlDialect.isReplace(str));

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
