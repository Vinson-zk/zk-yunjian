/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSqlConvert.java 
* @author Vinson 
* @Package com.zk.db.commons 
* @Description: TODO(simple description this file what to do. ) 
* @date Sep 11, 2020 9:45:49 AM 
* @version V1.0 
*/
package com.zk.db.commons;

import java.util.Collection;
import java.util.List;

import com.zk.core.commons.data.ZKOrder;

/** 
 * 两个作用，一个是解析实体上的注解；二将解析的注解转换成以 sql
 * @ClassName: ZKSqlConvert
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public interface ZKSqlConvert {


    /**
     * sql 关键字
     */
    public static interface SqlKeyword {
        public static final String insert = " INSERT INTO ";
        public static final String value = " VALUE ";
        public static final String update = " UPDATE ";
        public static final String set = " SET ";
        public static final String from = " FROM ";
        public static final String deleteFrom = " DELETE FROM ";
        public static final String where = " WHERE ";
        public static final String select = " SELECT ";
        public static final String orderBy = " ORDER BY ";
        public static final String space = " ";
        public static final String[] bracketed = {"(", ")"};
        public static final String comma = ", ";
    }

    /********************************************************/
    /*** 通过注解直接转换成 sql **/
    /********************************************************/
    // insert sql
    String convertSqlInsert(ZKDBMapInfo mapInfo);

    // update sql
    String convertSqlUpdate(ZKDBMapInfo mapInfo);

    // 逻辑删除 sql，逻辑删除语句，实体中定义
    String convertSqlDel(ZKDBMapInfo mapInfo, String delSetSql);

    // 物理删除 sql
    String convertSqlDiskDel(ZKDBMapInfo mapInfo);

    /**
     * 查询结果映射
     *
     * @Title: convertSqlSelCols
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 1, 2024 6:05:48 PM
     * @param mapInfo
     *            实体的注解对应库表信息
     * @param tableAlias
     *            表别名
     * @param columnPrefix
     *            映射字段名前缀
     * @return
     * @return String
     */
    String convertSqlSelCols(ZKDBMapInfo mapInfo, String tableAlias, String columnPrefix);

    // 主键查询条件
    String convertPkCondition(ZKDBMapInfo mapInfo, String tableAlias);

    /********************************************************/
    /*** 解析类上的注解 **/
    /********************************************************/

    // 根据注解 解析查询的条件
    ZKDBQueryWhere resolveQueryCondition(ZKDBMapInfo mapInfo);
    // 根据注解 解析查询的条件，为过虑的属性名；
    ZKDBQueryWhere resolveQueryCondition(ZKDBMapInfo mapInfo, List<String> filterAttrNames);

    /********************************************************/
    /*** 一些动态 转换 **/
    /********************************************************/

    // 这里将根据字段名映射到数据库字段；所以这里 ZKOrder 中填写的是 JAVA 实体字段名；
    String convertSqlOrderBy(ZKDBMapInfo mapInfo, Collection<ZKOrder> sorts, String tableAlias, boolean isDefault);

    // 查询条件，单个字段的转换
    void convertQueryCondition(StringBuffer sb, ZKDBQueryCol queryCol, String tableAlias);

    // 查询条件，单个字段的转换
    void convertQueryCondition(ZKDBOptLogic queryLogic, StringBuffer sb, ZKDBQueryCol queryCol, String tableAlias);

}
