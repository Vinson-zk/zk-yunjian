package com.zk.db.mybatis.mysql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.xmltags.XMLScriptBuilder;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import com.zk.core.commons.data.ZKJson;
import com.zk.core.commons.data.ZKOrder;
import com.zk.core.commons.data.ZKSortMode;
import com.zk.db.ZKMybatisSessionFactory;
import com.zk.db.commons.ZKDBMapInfo;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKDBQueryCol;
import com.zk.db.commons.ZKDBQueryWhere;
import com.zk.db.helper.ZKDBTestConfig;
import com.zk.db.helper.entity.ZKDBTestSampleEntity;
import com.zk.db.mybatis.commons.ZKDBQueryScript;

import junit.framework.TestCase;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: mysql sql 转换设置
 * @ClassName ZKDBMysqlSqlConvertTest
 * @Package com.zk.db.mybatis.mysql
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-28 09:39:26
 **/
@SuppressWarnings("unused")
public class ZKDBMysqlSqlConvertTest {

    @Test
    public void testConvertSqlInsert() {
        ZKDBMysqlSqlConvert mysqlSqlConvert = new ZKDBMysqlSqlConvert();
        try {
            ZKDBMapInfo mapInfo = new ZKDBMapInfo(ZKDBTestSampleEntity.class);

            String expectedStr, res;
            expectedStr = "<script> INSERT INTO t_zk_db_test(c_boolean, c_json, c_remarks, c_id, c_date, c_int, " +
                    "c_parent_id, c_id_2, c_value) VALUE (#{mBoolean}, <choose><when test = \"json == null\" >'{}'</when><otherwise>#{json}</otherwise></choose>, #{remarks}, #{id}, #{mDate}, #{mInt}, #{parentId}, #{id2}, #{value})</script>";
            res = mysqlSqlConvert.convertSqlInsert(mapInfo);
            System.out.println("[^_^:20220928-0943-001] insert sql: \n" + res);
            // 有可能会因为生成的顺序问题导致结果不匹配
//            TestCase.assertEquals(expectedStr, res);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testConvertSqlUpdate() {
        ZKDBMysqlSqlConvert mysqlSqlConvert = new ZKDBMysqlSqlConvert();
        try {
            ZKDBMapInfo mapInfo = new ZKDBMapInfo(ZKDBTestSampleEntity.class);
            String expectedStr, res;
            expectedStr = "<script> UPDATE t_zk_db_test SET <trim suffixOverrides=\",\"><if test = \"mBoolean != null\">c_boolean = #{mBoolean}, </if><if test = \"json != null\">c_json = JSON_MERGE_PATCH(c_json, #{json}), </if><if test = \"remarks != null\">c_remarks = #{remarks}, </if><if test = \"mDate != null\">c_date = #{mDate}, </if>c_int = c_int + 1, <if test = \"value != null\">c_value = #{value}, </if></trim> WHERE c_id = #{id} AND c_int = #{mInt} AND c_id_2 = #{id2}</script>";
            res = mysqlSqlConvert.convertSqlUpdate(mapInfo);
            System.out.println("[^_^:20220928-0943-002] update sql: \n" + res);
            // 有可能会因为生成的顺序问题导致结果不匹配
//            TestCase.assertEquals(expectedStr, res);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testConvertSqlDel() {
        ZKDBMysqlSqlConvert mysqlSqlConvert = new ZKDBMysqlSqlConvert();
        try {
            ZKDBMapInfo mapInfo = new ZKDBMapInfo(ZKDBTestSampleEntity.class);
            String expectedStr, res;
            expectedStr = " UPDATE t_zk_db_test SET c_1=v_1 WHERE c_id = #{id} AND c_id_2 = #{id2}";
            res = mysqlSqlConvert.convertSqlDel(mapInfo, "c_1=v_1");
            System.out.println("[^_^:20220928-0943-003] del sql: " + res);
            // 有可能会因为生成的顺序问题导致结果不匹配
//            TestCase.assertEquals(expectedStr, res);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testConvertSqlDiskDel() {
        ZKDBMysqlSqlConvert mysqlSqlConvert = new ZKDBMysqlSqlConvert();
        try {
            ZKDBMapInfo mapInfo = new ZKDBMapInfo(ZKDBTestSampleEntity.class);
            String expectedStr, res;
            expectedStr = " DELETE FROM t_zk_db_test WHERE c_id = #{id} AND c_id_2 = #{id2}";
            res = mysqlSqlConvert.convertSqlDiskDel(mapInfo);
            System.out.println("[^_^:20220928-0943-004] diskDel sql: " + res);
            // 有可能会因为生成的顺序问题导致结果不匹配
//            TestCase.assertEquals(expectedStr, res);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testConvertSqlSelCols() {
        ZKDBMysqlSqlConvert mysqlSqlConvert = new ZKDBMysqlSqlConvert();
        try {
            ZKDBMapInfo mapInfo = new ZKDBMapInfo(ZKDBTestSampleEntity.class);
            String expectedStr = "t.c_boolean AS \"mBoolean\", t.c_json AS \"json\", t.c_remarks AS \"remarks\", t.c_id AS \"id\", t.c_date AS \"mDate\", t.c_int AS \"mInt\", t.c_parent_id AS \"parentId\", t.c_id_2 AS \"id2\", t.c_value AS \"value\"";
            String res = mysqlSqlConvert.convertSqlSelCols(mapInfo, "t", "");
            System.out.println("[^_^:20220928-0943-004] sel Cols sql: " + res);
            // 有可能会因为生成的顺序问题导致结果不匹配
//            TestCase.assertEquals(expectedStr, res);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testSql() {
        try {
            ZKDBMysqlSqlConvert mysqlSqlConvert = new ZKDBMysqlSqlConvert();
            ZKDBQueryWhere where = null;
//            StringBuffer sb = null;
            String expectedStr = "";
            String resStr = "";
            List<ZKOrder> orders = new ArrayList<ZKOrder>();

            orders.add(ZKOrder.asOrder("id", ZKSortMode.ASC));
            orders.add(ZKOrder.asOrder("mInt", ZKSortMode.DESC));
            ZKDBMapInfo mapInfo = new ZKDBMapInfo(ZKDBTestSampleEntity.class);

            // selCols 偶尔可能会出现顺序问题，测试不匹配
            expectedStr = "t0.c_value AS \"value\", t0.c_json AS \"json\", t0.c_int AS \"mInt\", t0.c_boolean AS " +
                    "\"mBoolean\", t0.c_remarks AS \"remarks\", t0.c_id AS \"id\", t0.c_date AS \"mDate\"";
            resStr = mysqlSqlConvert.convertSqlSelCols(mapInfo, mapInfo.getTable().alias(), "");
            System.out.println("[^_^:20200911-1435-001] selColumns: " + resStr);
//            TestCase.assertEquals(expectedStr, resStr);

            // where 偶尔可能会出现顺序问题，测试不匹配
            expectedStr = "<if test=\"json != null and json.getKeyValues() != null\"><foreach item=\"_v\" " +
                    "index=\"_k\" collection=\"json.getKeyValues()\" open=\"\" separator=\" AND \" " +
                    "close=\"\">JSON_UNQUOTE(JSON_EXTRACT(ta.c_json, #{_k})) like CONCAT('%', REPLACE(REPLACE(REPLACE" +
                    "(#{_v}, '\\\\\\\\', '\\\\\\\\\\\\\\\\'), '_', '\\\\_'), '%', '\\\\%'), '%')</foreach></if><if " +
                    "test=\" mInt != null\"> AND ta.c_int = #{mInt}</if><if test=\" mBoolean != null\"> AND ta" +
                    ".c_boolean = #{mBoolean}</if><if test=\" remarks != null and remarks != ''\"> AND UPPER(ta" +
                    ".c_remarks) like CONCAT('%', REPLACE(REPLACE(REPLACE(UPPER(#{remarks}), '\\\\\\\\', " +
                    "'\\\\\\\\\\\\\\\\'), '_', '\\\\_'), '%', '\\\\%'), '%')</if><if test=\" startDate != null\"> AND" +
                    " DATE_FORMAT(ta.c_date, \"%Y-%m-%d\") &gt;= DATE_FORMAT(#{startDate}, \"%Y-%m-%d\")</if><if " +
                    "test=\" endDate != null\"> AND DATE_FORMAT(ta.c_date, \"%Y-%m-%d %H:%i\") &lt;= DATE_FORMAT" +
                    "(#{endDate}, \"%Y-%m-%d %H:%i\")</if><if test=\" id != null and id != ''\"> AND ta.c_id = " +
                    "#{id}</if><if test=\"mInts != null and mInts.length > 0\"> AND ta.c_int in <foreach item=\"_v\"" +
                    " index=\"_index\" collection=\"mInts\" open=\"(\" separator=\",\" close=\")" +
                    "\">#{_v}</foreach></if><if test=\"mIntStrs != null and mIntStrs.isEmpty() != true\"> AND ta" +
                    ".c_int in <foreach item=\"_v\" index=\"_index\" collection=\"mIntStrs\" open=\"(\" separator=\"," +
                    "\" close=\")\">#{_v}</foreach></if>";
            where = mysqlSqlConvert.resolveQueryCondition(mapInfo);
            resStr = where.toQueryCondition(mysqlSqlConvert, "ta");
            System.out.println("[^_^:20200911-1435-002] whereIf: " + resStr);
//            TestCase.assertEquals(expectedStr, resStr);

            // order by
            expectedStr = "";
            resStr = mysqlSqlConvert.convertSqlOrderBy(mapInfo, mapInfo.getAlias());
            System.out.println("[^_^:20200911-1435-003-1] orderBy: " + resStr);
            TestCase.assertEquals(expectedStr, resStr);
            expectedStr = "t0.c_id ASC, t0.c_int DESC";
            resStr = mysqlSqlConvert.convertSqlOrderBy(mapInfo, orders, mapInfo.getAlias());
            System.out.println("[^_^:20200911-1435-003-2] orderBy: " + resStr);
            TestCase.assertEquals(expectedStr, resStr);

            System.out.println("[^_^:20200911-1435-004] getPkWhereCondition: "
                    + mysqlSqlConvert.convertPkCondition(mapInfo, "---"));

            /*** 输出一些完成 sql ***/
            System.out.println("[^_^:20200911-1435-005] getSql: " + mysqlSqlConvert.convertSqlSelCols(mapInfo, "ta", "")
                    + " FROM " + mapInfo.getTableName() + " ta"
                    + mysqlSqlConvert.convertPkCondition(mapInfo, "ta"));

            where = mysqlSqlConvert.resolveQueryCondition(mapInfo);
            resStr = where.toQueryCondition(mysqlSqlConvert, "ta");
            System.out.println("[^_^:20200911-1435-006-1] selectListSql: "
                    + mysqlSqlConvert.convertSqlSelCols(mapInfo, "ta", "")
                    + " FROM " + mapInfo.getTableName() + " ta"
                    + resStr
                    + mysqlSqlConvert.convertSqlOrderBy(mapInfo, "ta"));

            ZKDBTestSampleEntity entity = new ZKDBTestSampleEntity();
            ZKJson json = new ZKJson();
            json.put("key-1", "v-1");
            json.put("key-2", "v-2");

            /* 插入/新增 */
            entity.setId("12");
            entity.setmInt(1L);
            entity.setValue("TET");
            entity.setRemarks("SS");
            entity.setJson(json);

            where = mysqlSqlConvert.resolveQueryCondition(mapInfo);
            resStr = where.toQueryCondition(mysqlSqlConvert, "ta");
            System.out.println("[^_^:20200911-1435-006-2] while: " + resStr);
            System.out.println("[^_^:20200911-1435-006-3] selectListSql: "
                    + mysqlSqlConvert.convertSqlSelCols(mapInfo, "ta", "")
                    + " FROM " + mapInfo.getTableName() + " ta"
                    + resStr
                    + mysqlSqlConvert.convertSqlOrderBy(mapInfo, "ta"));
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testParseScriptSql() {
        try {
//            ZKMybatisSessionFactory mybatisSessionFactory = ZKDBTestConfig.getJavaConfigSessionFactory();
            ZKMybatisSessionFactory mybatisSessionFactory = ZKDBTestConfig.getXmlConfigSessionFactory();
            Configuration configuration = mybatisSessionFactory.getSqlSessionFactory().getConfiguration();
            XPathParser parser = null;
            XMLScriptBuilder builder = null;
            String script = "";
            ZKDBQueryWhere where = null;
            ZKDBTestSampleEntity entity = null;
            ZKDBMapInfo mapInfo = null;
            ZKJson json = null;
            ZKDBMysqlSqlConvert mysqlSqlConvert = new ZKDBMysqlSqlConvert();

//            ZKDBEntity entity = new ZKDBEntity();
            List<ZKOrder> orders = new ArrayList<ZKOrder>();

            orders.add(ZKOrder.asOrder("id", ZKSortMode.ASC));
            orders.add(ZKOrder.asOrder("mInt", ZKSortMode.DESC));

            mapInfo = new ZKDBMapInfo(ZKDBTestSampleEntity.class);
            entity = new ZKDBTestSampleEntity();
            json = new ZKJson();
            json.put("key-1", "v-1");
            json.put("key-2", "v-2");
            json.put("key-3", "v-3; delete * from t_dd;");

            /*  */
            entity.setId("1");
            entity.setmInt(1L);
            entity.setmInts(new Long[] { 1l });

            System.out.println("===============================================");
            where = ZKDBQueryWhere.asAnd(
                    ZKDBQueryScript.asIf(
                            ZKDBQueryCol.as(ZKDBOptComparison.EQ, "c_id", "id", String.class, null, true),
                            0,"id", String.class),
                    ZKDBQueryScript.asIf(
                            ZKDBQueryCol.as(ZKDBOptComparison.NIN, "c_int", "mInts", Long[].class, null, true),
                            0, "mInts", Long[].class)
            );
            where.put(ZKDBQueryWhere.asOr("(", ")", where.getConditions().get(0),
                    where.getConditions().get(1)));
            script = "";
            script += "<script>";
            script += "SELECT ";
            script += mysqlSqlConvert.convertSqlSelCols(mapInfo, "ta", "");
            script += " FROM " + mapInfo.getTableName() + " ta WHERE ";
            script += where.toQueryCondition(mysqlSqlConvert, "ta");
            script += "</script>";
            System.out.println("[^_^:20210414-1431-001]: select get sql: " + script);
            parser = new XPathParser(script, false, configuration.getVariables(), new XMLMapperEntityResolver());
            builder = new XMLScriptBuilder(configuration, parser.evalNode("/script"), entity.getClass());
            script = builder.parseScriptNode().getBoundSql(entity).getSql();
            System.out.println("[^_^:20210414-1431-001]: select get sql.builder:" + script);

            entity.setValue("TET");
            entity.setRemarks("SS");
            entity.setJson(json);
            entity.getExtraParams().put("mIntStrs", Arrays.asList("1", "2"));

            // 查询 get
            System.out.println("===============================================");
            script = "";
            script += "<script>";
            script += "SELECT ";
            script += mysqlSqlConvert.convertSqlSelCols(mapInfo, "ta", "");
            script += " FROM " + mapInfo.getTableName() + " ta";
            script += mysqlSqlConvert.convertPkCondition(mapInfo, "ta");
            script += "</script>";
            System.out.println("[^_^:20210414-0905-001]: select get sql: " + script);
            parser = new XPathParser(script, false, configuration.getVariables(),
                    new XMLMapperEntityResolver());
            builder = new XMLScriptBuilder(configuration, parser.evalNode("/script"),
                    entity.getClass());
            script = builder.parseScriptNode().getBoundSql(entity).getSql();
            System.out.println("[^_^:20210414-0905-001]: select get sql.builder:" + script);

            // 查询 list
            System.out.println("===============================================");
            where = mysqlSqlConvert.resolveQueryCondition(mapInfo);
            script = "";
            script += "<script>";
            script += "SELECT ";
            script += mysqlSqlConvert.convertSqlSelCols(mapInfo, "ta", "");
            script += " FROM " + mapInfo.getTableName() + " ta WHERE";
            script += where.toQueryCondition(mysqlSqlConvert, " ta");
            script += mysqlSqlConvert.convertSqlOrderBy(mapInfo, "ta");
            script += "</script>";

            System.out.println("[^_^:20210414-0847-001]: select list sql: " + script);
            parser = new XPathParser(script, false, configuration.getVariables(),
                    new XMLMapperEntityResolver());
            builder = new XMLScriptBuilder(configuration, parser.evalNode("/script"),
                    entity.getClass());
//            DynamicContext context = new DynamicContext(configuration, entity);
//            SqlSource sqlSource = builder.parseScriptNode();
            script = builder.parseScriptNode().getBoundSql(entity).getSql();
            System.out.println("[^_^:20210414-0847-001]: select list sql.builder:" + script);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
