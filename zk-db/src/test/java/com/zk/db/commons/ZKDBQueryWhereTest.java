package com.zk.db.commons;

import com.zk.db.mybatis.mysql.ZKDBMysqlSqlConvert;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Date;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: 测试
 * @ClassName ZKDBQueryWhereTest
 * @Package com.zk.db.commons
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-28 10:19:27
 **/
public class ZKDBQueryWhereTest {

    @Test
    public void test() {
        try {
            ZKDBMysqlSqlConvert convert = new ZKDBMysqlSqlConvert();
            ZKDBQueryWhere where = null;
            String expectStr, resStr;

            where = ZKDBQueryWhere.asAnd(
                    ZKDBQueryCol.as(ZKDBOptComparison.EQ, "c_column", "attrName", String.class, null, true),
                    ZKDBQueryCol.as(ZKDBOptComparison.EQ, "c_column", "attrName", String.class, null, true));

            where.put(ZKDBQueryWhere.asOr("(", ")",
                    ZKDBQueryCol.as(ZKDBOptComparison.EQ, "c_column", "attrName", Date.class, null, true),
                    ZKDBQueryCol.as(ZKDBOptComparison.EQ, "c_column", "attrName", Date.class, null, true)));

            resStr = where.toQueryCondition(convert, "t_Alias");
            System.out.println("[^_^:20220928-1825-001] resStr: " + resStr);
            expectStr = "t_Alias.c_column = #{attrName} AND t_Alias.c_column = #{attrName} AND (t_Alias.c_column = #{attrName} OR t_Alias.c_column = #{attrName})";
            TestCase.assertEquals(expectStr, resStr);

            where = ZKDBQueryWhere.asAnd(
                    ZKDBQueryCol.as(ZKDBOptComparison.EQ, "c_column", "attrName", String.class, null, true),
                    ZKDBQueryCol.as(ZKDBOptComparison.EQ, "c_column", "attrName", String.class, null, true));

            where.put(ZKDBQueryWhere.asOr("(<trim prefixOverrides=\"and|or\">", "</trim>)",
                    ZKDBQueryCol.as(ZKDBOptComparison.EQ, "c_column", "attrName", Date.class, null, true),
                    ZKDBQueryCol.as(ZKDBOptComparison.EQ, "c_column", "attrName", Date.class, null, true)));

            resStr = where.toQueryCondition(convert, "t_Alias");
            System.out.println("[^_^:20220928-1825-002] resStr: " + resStr);
            expectStr = "t_Alias.c_column = #{attrName} AND t_Alias.c_column = " +
                    "#{attrName} AND (<trim prefixOverrides=\"and|or\">t_Alias.c_column = #{attrName} OR t_Alias" +
                    ".c_column = #{attrName}</trim>)";
            TestCase.assertEquals(expectStr, resStr);

            where = ZKDBQueryWhere.asAnd("<trim prefixOverrides=\"and|or\">", "</trim>",
                    ZKDBQueryCol.as(ZKDBOptComparison.EQ, "c_column", "attrName", String.class, null, false),
                    ZKDBQueryCol.as(ZKDBOptComparison.LIKE, "c_column", "attrName", String.class, null, true),
                    ZKDBQueryCol.as(ZKDBOptComparison.GT, "c_column", "attrName", Date.class,
                            new String[] { "sqlF" }, true));

            where.put(ZKDBQueryWhere.asOr(
                    ZKDBQueryCol.as(ZKDBOptComparison.EQ, "c_column", "attrName", Date.class, null, true),
                    ZKDBQueryCol.as(ZKDBOptComparison.LIKE, "c_column", "attrName", String.class, null, false),
                    ZKDBQueryCol.as(ZKDBOptComparison.GT, "c_column", "attrName", Date.class,
                            new String[] { "", null }, true)));

            resStr = where.toQueryCondition(convert, "t_Alias");
            System.out.println("[^_^:20220928-1826-003] resStr: " + resStr);
            expectStr = "<trim prefixOverrides=\"and|or\">UPPER(t_Alias.c_column) = UPPER(#{attrName}) AND t_Alias.c_column like CONCAT('%', REPLACE(REPLACE(REPLACE(#{attrName}, '\\\\\\\\', '\\\\\\\\\\\\\\\\'), '_', '\\\\_'), '%', '\\\\%'), '%') AND DATE_FORMAT(t_Alias.c_column, \"sqlF\") &gt; DATE_FORMAT(#{attrName}, \"sqlF\") AND t_Alias.c_column = #{attrName} OR UPPER(t_Alias.c_column) like CONCAT('%', REPLACE(REPLACE(REPLACE(UPPER(#{attrName}), '\\\\\\\\', '\\\\\\\\\\\\\\\\'), '_', '\\\\_'), '%', '\\\\%'), '%') OR DATE_FORMAT(t_Alias.c_column, \"\") &gt; DATE_FORMAT(#{attrName}, \"\")</trim>";
            TestCase.assertEquals(expectStr, resStr);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }

    }
}
