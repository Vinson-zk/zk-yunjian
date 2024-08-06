package com.zk.db.mybatis.commons;

import java.util.List;

import com.zk.core.commons.data.ZKOrder;
import com.zk.core.utils.ZKStringUtils;
import com.zk.db.annotation.ZKTable;
import com.zk.db.commons.ZKDBMapInfo;
import com.zk.db.commons.ZKDBQueryWhere;
import com.zk.db.commons.ZKSqlConvert;
import com.zk.db.commons.ZKSqlConvert.SqlKeyword;
import com.zk.db.entity.ZKDBEntity;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: 实体类的 sql 转换 助手
 * @ClassName ZKDBSqlHelper
 * @Package com.zk.db.mybatis.commons
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-27 14:23:25
 **/
public class ZKDBSqlHelper {

    ZKDBMapInfo mapInfo;

    ZKSqlConvert sqlConvert;

    public ZKDBMapInfo getMapInfo() {
        return mapInfo;
    }

    public ZKSqlConvert getSqlConvert() {
        return sqlConvert;
    }

    public static ZKDBSqlHelper as(ZKSqlConvert sqlConvert, ZKDBEntity<?> entity) {
        return new ZKDBSqlHelper(sqlConvert, entity);
    }

    public ZKDBSqlHelper(ZKSqlConvert sqlConvert, ZKDBEntity<?> entity) {
        this.mapInfo = new ZKDBMapInfo(entity.getClass());
        this.sqlConvert = sqlConvert;

        // 通过注解解析的查询条件结果
        this.where = entity.getZKDbWhere(sqlConvert, mapInfo);
        // 查询结果映射 带表默认别名
        this.blockSqlCols = this.getBlockSqlCols(this.getTableAlias());
        // 默认排序字段，带表默认别名
        this.blockSqlOrderBy = this.getBlockSqlOrderBy(null);
        // 主键查询，带表默认别名
        this.blockSqlPkWhere = this.getBlockSqlPkWhere(this.getTableAlias());
        // 查询条件，带表默认别名
        this.blockSqlWhere = this.getBlockSqlWhere(this.getTableAlias());
        // 列表查询，带表默认别名
        StringBuffer sb = new StringBuffer();
        sb.append(SqlKeyword.select);
        sb.append(this.getBlockSqlCols());
        sb.append(SqlKeyword.from);
        sb.append(this.getTableName());
        sb.append(SqlKeyword.space);
        sb.append(this.getTableAlias());
        if(!ZKStringUtils.isEmpty(this.getBlockSqlWhere())){
            sb.append(SqlKeyword.space);
            sb.append(ZKDBScriptKey.where[0]);
            sb.append(this.getBlockSqlWhere());
            sb.append(ZKDBScriptKey.where[1]);
        }

        this.blockSqlSelelctList = sb.toString();

        // 插入语句
        this.sqlInsert = this.getSqlConvert().convertSqlInsert(this.getMapInfo());
        // 修改语句
        this.sqlUpdate = this.getSqlConvert().convertSqlUpdate(this.getMapInfo());
        // 逻辑删除
        this.sqlDel = this.getSqlConvert().convertSqlDel(this.getMapInfo(), entity.getZKDbDelSetSql());
        // 物理删除
        this.sqlDiskDel = this.getSqlConvert().convertSqlDiskDel(this.getMapInfo());
        // 主键查询
        sb = new StringBuffer();
        sb.append(ZKDBScriptKey.Script[0]);
        sb.append(SqlKeyword.select);
        sb.append(this.getBlockSqlCols());
        sb.append(SqlKeyword.from);
        sb.append(this.getTableName());
        sb.append(SqlKeyword.space);
        sb.append(this.getTableAlias());
        sb.append(SqlKeyword.where);
        sb.append(this.getBlockSqlPkWhere());
        sb.append(ZKDBScriptKey.Script[1]);
        this.sqlGet = sb.toString();
    }

    /********************************************************/
    /*** 动态生成 sql 片段的方法 *****************************/
    /********************************************************/

    /**
     * 自定义查询结果的表别名
     *
     * @Title: getBlockSqlCols
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 15, 2022 9:34:43 AM
     * @param tableAlias
     * @return
     * @return String
     */
    public String getBlockSqlCols(String tableAlias) {
        return this.getSqlConvert().convertSqlSelCols(this.getMapInfo(), tableAlias, "");
    }

    public String getBlockSqlCols(String tableAlias, String columnPrefix) {
        return this.getSqlConvert().convertSqlSelCols(this.getMapInfo(), tableAlias, columnPrefix);
    }

    /**
     * 生成主键查询条件，自定义表名
     *
     * 不带 WHERE
     *
     * @Title: getBlockSqlPkWhere
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 15, 2022 9:41:08 AM
     * @param tableAlias
     * @return
     * @return String
     */
    public String getBlockSqlPkWhere(String tableAlias) {
        return this.getSqlConvert().convertPkCondition(this.getMapInfo(), tableAlias);
    }

    /**
     * 自定定义查询条件的表别名
     *
     * @Title: getBlockSqlWhere
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 15, 2022 9:35:05 AM
     * @param tableAlias
     * @return
     * @return String
     */
    public String getBlockSqlWhere(String tableAlias) {
        return this.getWhere().toQueryCondition(this.getSqlConvert(), tableAlias);
    }

    /**
     * 转换成排序 sql，带默认表别名
     *
     * sorts 为空或为 null 时，会取默认排序片段
     * @Title: getBlockSqlOrderBy
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 15, 2022 9:35:34 AM
     * @param sorts
     * @return
     * @return String
     */
    public String getBlockSqlOrderBy(List<ZKOrder> sorts) {
        return this.getBlockSqlOrderBy(this.getTableAlias(), sorts);
    }

    /**
     * 转换成排序 sql，自定义
     *
     * sorts 为空或为 null 时，会取默认排序片段
     * @Title: getBlockSqlOrderBy
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 15, 2022 9:36:24 AM
     * @param tableAlias
     * @param sorts
     * @return
     * @return String
     */
    public String getBlockSqlOrderBy(String tableAlias, List<ZKOrder> sorts) {
        return this.getBlockSqlOrderBy(tableAlias, sorts, true);
    }

    /**
     * 转换成排序 sql，自定义
     *
     * @Title: getBlockSqlOrderBy
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 15, 2022 9:39:19 AM
     * @param tableAlias
     * @param sorts
     * @param isDefault
     *            true-sorts 为空或为 null 时，会取默认排序片段; false-sorts 为空或为 null 时，返回空窜；
     * @return
     * @return String
     */
    public String getBlockSqlOrderBy(String tableAlias, List<ZKOrder> sorts, boolean isDefault) {
        return this.getSqlConvert().convertSqlOrderBy(this.getMapInfo(), sorts, tableAlias, isDefault);
    }

    /********************************************************/
    /*** 成员及方法定义 **********************************************/
    /********************************************************/


    /*** 表信息 **********************************************/
    public ZKTable getTable() {
        return this.getMapInfo().getTable();
    }

    public String getTableName() {
        return this.getMapInfo().getTableName();
    }

    public String getTableAlias() {
        return this.getMapInfo().getTable().alias();
    }

    /*** 完成 sql ********************************************/
    // 插入语句
    String sqlInsert;
    // 修改语句
    String sqlUpdate;
    // 逻辑删除
    String sqlDel;
    // 物理删除
    String sqlDiskDel;
    // 查询
    String sqlGet;

    /*** sql 片段 ********************************************/
    // 查询结果映射 带表默认别名
    String blockSqlCols;
    // 默认排序字段，带表默认别名
    String blockSqlOrderBy;
    // 主键查询，带表默认别名
    String blockSqlPkWhere;
    // 查询条件，带表默认别名
    String blockSqlWhere;
    // 列表查询，带表默认别名
    String blockSqlSelelctList;

    /*** 其他 sql 生成过程数据对象 *****************************/
    // 通过注解解析的查询条件结果
    ZKDBQueryWhere where;

    /********************************************************/
    /*** 成员 get 方法 **********************************************/
    /********************************************************/

    public String getSqlInsert() {
        return sqlInsert;
    }

    public String getSqlUpdate() {
        return sqlUpdate;
    }

    public String getSqlDel() {
        return sqlDel;
    }

    public String getSqlDiskDel() {
        return sqlDiskDel;
    }

    public String getSqlGet() {
        return sqlGet;
    }

    public String getBlockSqlCols() {
        return blockSqlCols;
    }

    public String getBlockSqlOrderBy() {
        return blockSqlOrderBy;
    }

    public String getBlockSqlPkWhere() {
        return blockSqlPkWhere;
    }

    public String getBlockSqlWhere() {
        return blockSqlWhere;
    }

    public String getBlockSqlSelelctList() {
        return blockSqlSelelctList;
    }

    public ZKDBQueryWhere getWhere() {
        return where;
    }




}
