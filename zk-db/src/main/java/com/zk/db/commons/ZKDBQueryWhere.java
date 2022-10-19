package com.zk.db.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: 查询 where 条件
 * @ClassName ZKDBQueryWhere
 * @Package com.zk.db.commons
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-26 00:47:56
 **/
public class ZKDBQueryWhere extends ZKDBQuery{

    // 条件逻辑 and or
    ZKDBOptLogic queryLogic;

    List<ZKDBQuery> conditions = new ArrayList<>();

    public static ZKDBQueryWhere asAnd(ZKDBQuery... conditions) {
        return new ZKDBQueryWhere(ZKDBOptLogic.AND, "", "", conditions);
    }

    public static ZKDBQueryWhere asOr(ZKDBQuery... conditions) {
        return new ZKDBQueryWhere(ZKDBOptLogic.OR, "", "", conditions);
    }

    public static ZKDBQueryWhere asAnd(String prefix, String suffix, ZKDBQuery... conditions) {
        return new ZKDBQueryWhere(ZKDBOptLogic.AND, prefix, suffix, conditions);
    }

    public static ZKDBQueryWhere asOr(String prefix, String suffix, ZKDBQuery... conditions) {
        return new ZKDBQueryWhere(ZKDBOptLogic.OR, prefix, suffix, conditions);
    }

    protected ZKDBQueryWhere(ZKDBOptLogic queryLogic, String prefix, String suffix,
                             ZKDBQuery... conditions) {
        super(prefix, suffix);
        this.queryLogic = queryLogic;
        if (conditions != null) {
            this.put(conditions);
        }
    }

    // 添加条件；方法名定义，不想用 add ；怕与 and 混淆
    public void put(ZKDBQuery... queryConditions) {
        for (ZKDBQuery queryCondition : queryConditions) {
            this.put(queryCondition, -1);
        }
    }

    public void put(ZKDBQuery queryCondition, int point) {
        if (queryCondition != null) {
            if (point < 0) {
                this.getConditions().add(queryCondition);
            }
            else {
                this.getConditions().add(point, queryCondition);
            }
        }
    }

    public boolean isEmpty() {
        return this.getConditions().isEmpty();
    }

    /**
     * @return queryLogic sa
     */
    public ZKDBOptLogic getQueryLogic() {
        return queryLogic;
    }

    /**
     * @return conditions sa
     */
    public List<ZKDBQuery> getConditions() {
        return conditions;
    }

    /**
     *
     * @MethodName convertQueryCondition
     * @param convert 在 where 转换中，convert 只会做为中间参数会给下一个条件；
     * @param sb
     * @param tableAlias
     * @param queryLogic
     * @return void
     * @throws
     * @Author bs
     * @DATE 2022-09-28 18:06:559
     */
    @Override
    public void convertQueryCondition(ZKSqlConvert convert, StringBuffer sb, String tableAlias,
        ZKDBOptLogic queryLogic) {
        if (!this.isEmpty()) {
            Iterator<ZKDBQuery> iterator = this.getConditions().iterator();
            ZKDBQuery condition = iterator.next();
            // 第一个子条件，要添加查询条件逻辑
            condition.toQueryCondition(convert, sb, tableAlias, null);
            while (iterator.hasNext()) {
                condition = iterator.next();
                // 其余子条件，要添加查询条件逻辑
                condition.toQueryCondition(convert, sb, tableAlias, this.getQueryLogic());
            }
        }
    }

    public String toQueryCondition(ZKSqlConvert convert, String tableAlias) {
        StringBuffer sb = new StringBuffer();
        super.toQueryCondition(convert, sb, tableAlias, null);
        return sb.toString();
    }
}
