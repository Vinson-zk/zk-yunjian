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
* @Title: ZKDefaultCountSqlParser.java 
* @author Vinson 
* @Package com.zk.db.parser 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 3, 2024 5:32:52 PM 
* @version V1.0 
*/
package com.zk.db.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.parser.Token;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.ParenthesedFromItem;
import net.sf.jsqlparser.statement.select.ParenthesedSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.WithItem;

/**
 * 参考：https://github.com/pagehelper/Mybatis-PageHelper
 * 
 * @ClassName: ZKDefaultCountSqlParser
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKDefaultCountSqlParser implements ZKCountSqlParser {

    protected static final Alias TABLE_ALIAS;

    protected final Set<String> skipFunctions = Collections.synchronizedSet(new HashSet<>());

    protected final Set<String> falseFunctions = Collections.synchronizedSet(new HashSet<>());

    static {
        TABLE_ALIAS = new Alias(ZKCountSqlParser.COUNT_TABLE_ALIAS);
        TABLE_ALIAS.setUseAs(false);
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
    @Override
    public String getCountSql(String sourceSql, String countSqlBlock, ZKModel model, boolean removeOrderBy) {
        // 简单模式，且不需要去掉 orderBy
        if (model == ZKModel.Simple && !removeOrderBy) {
            return getSimpleCountSql(sourceSql, countSqlBlock);
        }
        // 解析SQL
        Statement stmt = null;
        try {
            stmt = ZKSqlParser.DEFAULT.parse(sourceSql);
        }
        catch(Throwable e) {
            // 无法解析的用一般方法返回count语句
            return getSimpleCountSql(sourceSql, countSqlBlock);
        }
        Select select = (Select) stmt;
        try {
            // 处理body-去order by
            processSelect(select, removeOrderBy);
        }
        catch(Exception e) {
            // 当 sql 包含 group by 时，不去除 order by
            return getSimpleCountSql(sourceSql, countSqlBlock);
        }
        // 处理 with -去order by
        processWithItemsList(select.getWithItemsList(), removeOrderBy);
        // 处理为count查询
        Select countSelect = null;
        if (model == ZKModel.Simple) {
            return getSimpleCountSql(select.toString(), countSqlBlock);
        }
        else {
            countSelect = parserSqlToCount(select, countSqlBlock);
        }
        String result = countSelect.toString();
        if (select instanceof PlainSelect) {
            Token token = select.getASTNode().jjtGetFirstToken().specialToken;
            if (token != null) {
                String hints = token.toString().trim();
                // 这里判断是否存在hint, 且result是不包含hint的
                if (hints.startsWith("/*") && hints.endsWith("*/") && !result.startsWith("/*")) {
                    result = hints + result;
                }
            }
        }
        return result;
    }

    /**
     * 获取普通的Count-sql
     *
     * @param sql
     *            原查询sql
     * @return 返回count查询sql
     */
    public String getSimpleCountSql(final String sql, String countSqlBlock) {
        StringBuilder stringBuilder = new StringBuilder(sql.length() + 40);
        stringBuilder.append("SELECT ");
        stringBuilder.append(countSqlBlock);
        stringBuilder.append(" FROM (");
        stringBuilder.append(sql);
        stringBuilder.append(") ");
        stringBuilder.append(ZKCountSqlParser.COUNT_TABLE_ALIAS);
        return stringBuilder.toString();
    }

    /**
     * 将 sql 转换为 count 查询
     *
     * @param select
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Select parserSqlToCount(Select select, String countSqlBlock) {
        // 是否能简化count查询
        List<SelectItem<?>> COUNT_ITEM = new ArrayList<>();
        COUNT_ITEM.add(new SelectItem(new Column(countSqlBlock)));
        if (select instanceof PlainSelect && isSimpleCount((PlainSelect) select)) {
            ((PlainSelect) select).setSelectItems(COUNT_ITEM);
            return select;
        }
        else {
            PlainSelect plainSelect = new PlainSelect();
            ParenthesedSelect subSelect = new ParenthesedSelect();
            subSelect.setSelect(select);
            subSelect.setAlias(TABLE_ALIAS);
            plainSelect.setFromItem(subSelect);
            plainSelect.setSelectItems(COUNT_ITEM);
            if (select.getWithItemsList() != null) {
                plainSelect.setWithItemsList(select.getWithItemsList());
                select.setWithItemsList(null);
            }
            return plainSelect;
        }
    }

    /**
     * 是否可以用简单的count查询方式
     *
     * @param select
     * @return
     */
    public boolean isSimpleCount(PlainSelect select) {
        // 包含group by的时候不可以
        if (select.getGroupBy() != null) {
            return false;
        }
        // 包含distinct的时候不可以
        if (select.getDistinct() != null) {
            return false;
        }
        // #606,包含having时不可以
        if (select.getHaving() != null) {
            return false;
        }
        for (SelectItem<?> item : select.getSelectItems()) {
            // select列中包含参数的时候不可以，否则会引起参数个数错误
            if (item.toString().contains("?")) {
                return false;
            }
            // 如果查询列中包含函数，也不可以，函数可能会聚合列
            Expression expression = item.getExpression();
            if (expression instanceof Function) {
                String name = ((Function) expression).getName();
                if (name != null) {
                    String NAME = name.toUpperCase();
                    if (skipFunctions.contains(NAME)) {
                        // go on
                    }
                    else if (falseFunctions.contains(NAME)) {
                        return false;
                    }
                    else {
                        for (String aggregateFunction : AGGREGATE_FUNCTIONS) {
                            if (NAME.startsWith(aggregateFunction)) {
                                falseFunctions.add(NAME);
                                return false;
                            }
                        }
                        skipFunctions.add(NAME);
                    }
                }
            }
            else if (expression instanceof Parenthesis && item.getAlias() != null) {
                // #555，当存在 (a+b) as c 时，c 如果出现了 order by 或者 having 中时，会找不到对应的列，
                // 这里想要更智能，需要在整个SQL中查找别名出现的位置，暂时不考虑，直接排除
                return false;
            }
        }
        return true;
    }

    /**
     * 处理selectBody去除Order by
     *
     * @param select
     */
    public void processSelect(Select select, boolean removeOrderBy) {
        if (select != null) {
            if (select instanceof PlainSelect) {
                processPlainSelect((PlainSelect) select, removeOrderBy);
            }
            else if (select instanceof ParenthesedSelect) {
                processSelect(((ParenthesedSelect) select).getSelect(), removeOrderBy);
            }
            else if (select instanceof SetOperationList) {
                List<Select> selects = ((SetOperationList) select).getSelects();
                for (Select sel : selects) {
                    processSelect(sel, removeOrderBy);
                }
                if (removeOrderBy && !orderByHashParameters(select.getOrderByElements())) {
                    select.setOrderByElements(null);
                }
            }
        }
    }

    /**
     * 处理PlainSelect类型的selectBody
     *
     * @param plainSelect
     */
    public void processPlainSelect(PlainSelect plainSelect, boolean removeOrderBy) {
        if (removeOrderBy && !orderByHashParameters(plainSelect.getOrderByElements())) {
            plainSelect.setOrderByElements(null);
        }
        if (plainSelect.getFromItem() != null) {
            processFromItem(plainSelect.getFromItem(), removeOrderBy);
        }
        if (plainSelect.getJoins() != null && plainSelect.getJoins().size() > 0) {
            List<Join> joins = plainSelect.getJoins();
            for (Join join : joins) {
                if (join.getRightItem() != null) {
                    processFromItem(join.getRightItem(), removeOrderBy);
                }
            }
        }
    }

    /**
     * 处理WithItem
     *
     * @param withItemsList
     */
    public void processWithItemsList(List<WithItem> withItemsList, boolean keepSubOrderBy) {
        if (withItemsList != null && !withItemsList.isEmpty()) {
            for (WithItem item : withItemsList) {
                if (item.getSelect() != null && !keepSubOrderBy) {
                    processSelect(item.getSelect(), keepSubOrderBy);
                }
            }
        }
    }

    /**
     * 处理子查询
     *
     * @param fromItem
     */
    public void processFromItem(FromItem fromItem, boolean removeOrderBy) {
        if (fromItem instanceof ParenthesedSelect) {
            ParenthesedSelect parenthesedSelect = (ParenthesedSelect) fromItem;
            if (parenthesedSelect.getSelect() != null) {
                processSelect(parenthesedSelect.getSelect(), removeOrderBy);
            }
        }
        else if (fromItem instanceof Select) {
            processSelect((Select) fromItem, removeOrderBy);
        }
        else if (fromItem instanceof ParenthesedFromItem) {
            ParenthesedFromItem parenthesedFromItem = (ParenthesedFromItem) fromItem;
            processFromItem(parenthesedFromItem.getFromItem(), removeOrderBy);
        }
        // Table时不用处理
    }

    /**
     * 判断Orderby是否包含参数，有参数的不能去
     *
     * @param orderByElements
     * @return
     */
    public boolean orderByHashParameters(List<OrderByElement> orderByElements) {
        if (orderByElements == null) {
            return false;
        }
        for (OrderByElement orderByElement : orderByElements) {
            if (orderByElement.toString().contains(ZKCountSqlParser.PLACEHOLDER)) {
                return true;
            }
        }
        return false;
    }

}
