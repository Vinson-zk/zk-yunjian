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
* @Title: ZKCountSqlParser.java 
* @author Vinson 
* @Package com.zk.db.parser 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 3, 2024 5:03:51 PM 
* @version V1.0 
*/
package com.zk.db.parser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * 参考：https://github.com/pagehelper/Mybatis-PageHelper
 * 
 * @ClassName: ZKCountSqlParser
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public interface ZKCountSqlParser {
    
    // SQL 占位
    public static final String PLACEHOLDER = "?";

    // count 临时查询结果别名
    public static final String COUNT_TABLE_ALIAS = "_temp_table";

    // count sql 片段
    public static final String DEFAULT_COUNT_SQL_BLOCK = "COUNT(0)";

    public static enum ZKModel {
        Simple, Perfect
    }

    /**
     * 聚合函数，以下列函数开头的都认为是聚合函数;
     * 
     * 此定义应该有问题
     */
    Set<String> AGGREGATE_FUNCTIONS = new HashSet<String>(Arrays.asList(
            "APPROX_COUNT_DISTINCT",
            "ARRAY_AGG",
            "AVG",
            "BIT_",
            //"BIT_AND",
            //"BIT_OR",
            //"BIT_XOR",
            "BOOL_",
            //"BOOL_AND",
            //"BOOL_OR",
            "CHECKSUM_AGG",
            "COLLECT",
            "CORR",
            //"CORR_",
            //"CORRELATION",
            "COUNT",
            //"COUNT_BIG",
            "COVAR",
            //"COVAR_POP",
            //"COVAR_SAMP",
            //"COVARIANCE",
            //"COVARIANCE_SAMP",
            "CUME_DIST",
            "DENSE_RANK",
            "EVERY",
            "FIRST",
            "GROUP",
            //"GROUP_CONCAT",
            //"GROUP_ID",
            //"GROUPING",
            //"GROUPING",
            //"GROUPING_ID",
            "JSON_",
            //"JSON_AGG",
            //"JSON_ARRAYAGG",
            //"JSON_OBJECT_AGG",
            //"JSON_OBJECTAGG",
            //"JSONB_AGG",
            //"JSONB_OBJECT_AGG",
            "LAST",
            "LISTAGG",
            "MAX",
            "MEDIAN",
            "MIN",
            "PERCENT_",
            //"PERCENT_RANK",
            //"PERCENTILE_CONT",
            //"PERCENTILE_DISC",
            "RANK",
            "REGR_",
            "SELECTIVITY",
            "STATS_",
            //"STATS_BINOMIAL_TEST",
            //"STATS_CROSSTAB",
            //"STATS_F_TEST",
            //"STATS_KS_TEST",
            //"STATS_MODE",
            //"STATS_MW_TEST",
            //"STATS_ONE_WAY_ANOVA",
            //"STATS_T_TEST_*",
            //"STATS_WSR_TEST",
            "STD",
            //"STDDEV",
            //"STDDEV_POP",
            //"STDDEV_SAMP",
            //"STDDEV_SAMP",
            //"STDEV",
            //"STDEVP",
            "STRING_AGG",
            "SUM",
            "SYS_OP_ZONE_ID",
            "SYS_XMLAGG",
            "VAR",
            //"VAR_POP",
            //"VAR_SAMP",
            //"VARIANCE",
            //"VARIANCE_SAMP",
            //"VARP",
            "XMLAGG"));

    /**
     * 添加到聚合函数，可以是逗号隔开的多个函数前缀
     *
     * @param functions
     */
    static void addAggregateFunctions(String... functions) {
        for (String f : functions) {
            AGGREGATE_FUNCTIONS.add(f.toUpperCase());
        }
    }

    /**
     * 获取的 countSql
     *
     * @param sql
     */
    default String getCountSql(String sourceSql) {
        return getCountSql(sourceSql, "count(0)");
    }

    /**
     * 获取的 countSql
     *
     * @param sql
     * @param countSqlBlock
     *            统计记数的 sql 片段，默认为 count(0)
     */
    default String getCountSql(String sourceSql, String countSqlBlock) {
        return getCountSql(sourceSql, "count(0)", ZKModel.Perfect, true);
    }

    /**
     * 
     *
     * @Title: getCountSql
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jan 3, 2024 11:23:31 PM
     * @param sourceSql
     *            原查询 sql
     * @param countSqlBlock
     *            统计记数的 sql 片段，默认为 count(0)
     * @param model
     *            生成的 countSql 模式，Simple 模是最简单的模式; 默认：Perfect
     * @param removeOrderBy
     *            是否需要删除 orderBy true-是；false-不是；默认 false;
     * @return String
     */
    public String getCountSql(String sourceSql, String countSqlBlock, ZKModel model, boolean removeOrderBy);


}
