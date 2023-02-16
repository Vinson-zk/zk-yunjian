package com.zk.db.commons;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: 查询条件，字段抽象类
 * @ClassName ZKDBQuery
 * @Package com.zk.db.commons
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-26 00:52:12
 **/
public abstract class ZKDBQuery {

    // sql 前缀，如 括号
    private String prefix;
    // sql 后缀，如 括号
    private String suffix;

//    protected  ZKDBQuery(){}

    public ZKDBQuery(String prefix, String suffix){
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /**
     * @return prefix sa
     */
    public String getPrefix() {
        return prefix == null ? "" : prefix;
    }

    /**
     * @return suffix sa
     */
    public String getSuffix() {
        return suffix == null ? "" : suffix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     *
     *
     * @Title: toQueryCondition
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 19, 2022 3:11:03 PM
     * @param convert
     * @param sb
     * @param tableAlias
     *            表的别名；
     * @param queryLogic
     *            查询关系逻辑，and/or 不为 null 时，会添加在条件前面；
     * @return void
     */
    public void toQueryCondition(ZKSqlConvert convert, StringBuffer sb, String tableAlias,
                                 ZKDBOptLogic queryLogic){
        if (queryLogic != null) {
            // 这里的查询逻辑 ZKDBQueryLogic，不是对象本身在的 ZKDBQueryLogic，是做为子查询条件时，上级查询条件传过来的；若没有上级查询，应该传入 null
            // 当做为子条件时，上一级查询条件有传入 查询逻辑时，先添加上一级查询逻辑；
            // 不在上一级查询条件中直接控制，是为了方便在添加查询逻辑前添加一些其他内容，如 mybatis "<if test... "
            sb.append(queryLogic.getEsc());
        }
        sb.append(this.getPrefix());
        this.convertQueryCondition(convert, sb, tableAlias, queryLogic);
        sb.append(this.getSuffix());
    }

    protected abstract void convertQueryCondition(ZKSqlConvert convert, StringBuffer sb, String tableAlias,
                                                  ZKDBOptLogic queryLogic);
}
