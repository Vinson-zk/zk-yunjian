package com.zk.db.commons;

import com.zk.core.utils.ZKStringUtils;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: 查询条件 sql 片段
 * @ClassName ZKDBQueryColSql
 * @Package com.zk.db.commons
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-13 17:22:06
 **/
public class ZKDBQueryColSql extends ZKDBQuery{

    // 条件字段名
    private String columnName;

    // 查询方式
    private ZKDBOptComparison queryType;

    // 条件语句
    private String conditionSql;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public ZKDBOptComparison getQueryType() {
        return queryType;
    }

    public void setQueryType(ZKDBOptComparison queryType) {
        this.queryType = queryType;
    }

    public String getConditionSql() {
        return conditionSql;
    }

    public void setConditionSql(String conditionSql) {
        this.conditionSql = conditionSql;
    }

    public static ZKDBQueryColSql as(ZKDBOptComparison queryType, String columnName, String conditionSql) {
        return new ZKDBQueryColSql(queryType, columnName, conditionSql);
    }
    public static ZKDBQueryColSql as(ZKDBOptComparison queryType, String columnName, String conditionSql, String prefix, String suffix) {
        return new ZKDBQueryColSql(queryType, columnName, conditionSql, prefix, suffix);
    }

    protected ZKDBQueryColSql(ZKDBOptComparison queryType, String columnName, String conditionSql) {
        super(null, null);
        this.queryType = queryType;
        this.columnName = columnName;
        this.conditionSql = conditionSql;
    }

    protected ZKDBQueryColSql(ZKDBOptComparison queryType, String columnName, String conditionSql, String prefix, String suffix) {
        super(prefix, suffix);
        this.queryType = queryType;
        this.columnName = columnName;
        this.conditionSql = conditionSql;
    }

    @Override
    protected void convertQueryCondition(ZKSqlConvert convert, StringBuffer sb, String tableAlias,
                                         ZKDBOptLogic queryLogic) {
        if(!ZKStringUtils.isEmpty(tableAlias)){
            sb.append(tableAlias);
            sb.append(".");
        }
        sb.append(columnName);
        sb.append(queryType.getEsc());
        sb.append(conditionSql);
    }


}
