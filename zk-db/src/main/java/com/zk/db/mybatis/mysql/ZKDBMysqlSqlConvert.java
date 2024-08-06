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
* @Title: ZKDBMysqlSqlConvert.java 
* @author Vinson 
* @Package com.zk.db.commons.support 
* @Description: TODO(simple description this file what to do. ) 
* @date Sep 11, 2020 9:51:50 AM 
* @version V1.0 
*/
package com.zk.db.mybatis.mysql;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.commons.data.ZKJson;
import com.zk.core.commons.data.ZKOrder;
import com.zk.core.utils.ZKStringUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.commons.ZKDBMapInfo;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKDBOptLogic;
import com.zk.db.commons.ZKDBQueryCol;
import com.zk.db.mybatis.commons.ZKDBMybatisConvert;
import com.zk.db.mybatis.commons.ZKDBScriptKey;

/** 
* @ClassName: ZKDBMysqlSqlConvert 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDBMysqlSqlConvert extends ZKDBMybatisConvert {

    protected static Logger logger = LogManager.getLogger(ZKDBMysqlSqlConvert.class);

    @Override
    public void convertQueryCondition(StringBuffer sb, ZKDBQueryCol queryCol, String tableAlias) {
        convertQueryCondition(null, sb, queryCol, tableAlias);
    }

    /**
     * 将 ZKDBQueryCol 转换为查询条件；
     * @MethodName convertQueryCondition
     * @param queryLogic 仅在 javaClassz 为 ZKJson 时有效，因为，会将 Json 展开为一个一个属性做为查询条件；默认为: AND
     * @param sb
     * @param queryCol
     * @param tableAlias 表的别名；
     * @return void
     * @throws
     * @Author bs
     * @DATE 2022-09-28 16:40:992
     */
    @Override
    public void convertQueryCondition(ZKDBOptLogic queryLogic, StringBuffer sb, ZKDBQueryCol queryCol,
        String tableAlias) {
        appendScriptQueryCondition(queryLogic, sb, tableAlias, queryCol.getColumnName(),
                queryCol.getAttrName(), queryCol.getQueryType(), queryCol.getJavaClassz(),
                queryCol.getFormats(), queryCol.isCaseSensitive());
    }

    // 这里将根据字段名映射到数据库字段；所以这里 ZKOrder 中填写的是 JAVA 实体字段名； 不带有 ORDER BY
    @Override
    public String convertSqlOrderBy(ZKDBMapInfo mapInfo, Collection<ZKOrder> sorts, String tableAlias, boolean isDefault) {
        if(sorts == null || sorts.isEmpty()){
            if(isDefault){
                // 取注解中默认排序字段
                return this.convertSqlOrderBy(mapInfo, tableAlias);
            }
        }else{
            return this.convertSqlOrderBy(mapInfo, sorts, tableAlias);
        }
        return "";
    }

    // 从注解 table 中取默认的排序字段，在注解中直接定义 排序映射，不需要再解析属性与字段的映射；
    protected String convertSqlOrderBy(ZKDBMapInfo mapInfo, String tableAlias) {
        if (mapInfo.getTable().orderBy().length < 1) {
            return "";
        }
        String alias = "";
        if (!ZKStringUtils.isEmpty(tableAlias)) {
            alias = tableAlias + ".";
        }
        StringBuffer sb = new StringBuffer();
        for (String col : mapInfo.getTable().orderBy()) {
            if(sb.length() > 0){
                sb.append(", ");
            }
            col = col.trim();
            sb.append(alias);
            sb.append(col);
        }
        return sb.toString();
    }

    // 这里将根据字段名映射到数据库字段；所以这里 ZKOrder 中填写的是 JAVA 实体字段名； 不带有 ORDER BY
    protected String convertSqlOrderBy(ZKDBMapInfo mapInfo, Collection<ZKOrder> sorts, String tableAlias) {
        if (sorts == null || sorts.isEmpty()) {
            return "";
        }
        String alias = "";
        if (!ZKStringUtils.isEmpty(tableAlias)) {
            alias = tableAlias + ".";
        }
        StringBuffer sb = new StringBuffer();
        for (ZKOrder item : sorts) {
            if (mapInfo.getColumn(item.getColumnName()) != null) {
                if(sb.length() > 0){
                    sb.append(SqlKeyword.comma);
                }
                sb.append(alias);
                sb.append(mapInfo.getColumn(item.getColumnName()).name());
                sb.append(SqlKeyword.space);
                sb.append(item.getSortMode().getValue());
            }
        }
        return sb.toString();
    }

    /********************************************************/
    /*** 通过注解直接转换成 sql **/
    /********************************************************/
    @Override
    public String convertPkCondition(ZKDBMapInfo mapInfo, String alias){
        if(mapInfo.isEmpty()){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<PropertyDescriptor, ZKColumn> item:mapInfo.getColumnSet()){
            if(item.getValue().isPk()){
                if(sb.length() > 0){
                    sb.append(ZKDBOptLogic.AND.getEsc());
                }
                if(!ZKStringUtils.isEmpty(alias)){
                    sb.append(alias);
                    sb.append(".");
                }
                sb.append(item.getValue().name());
                sb.append(ZKDBOptComparison.EQ.getEsc());
                sb.append("#{");
                sb.append(item.getKey().getName());
                sb.append("}");
            }
        }
        return sb.toString();
    }

    @Override
    public String convertSqlSelCols(ZKDBMapInfo mapInfo, String tableAlias, String columnPrefix) {

        if(mapInfo.isEmpty()){
            return "";
        }

        StringBuffer sb = new StringBuffer();
        if (columnPrefix == null) {
            // 结果字段映射不为 null 时，设置为空串
            columnPrefix = "";
        }
        String alias = "";
        if (!ZKStringUtils.isEmpty(tableAlias)) {
            alias = tableAlias + ".";
        }
        Iterator<Map.Entry<PropertyDescriptor, ZKColumn>> iterator = mapInfo.getColumnsIterator();
        Map.Entry<PropertyDescriptor, ZKColumn> col = null;
        do{
            col = iterator.next();
            // 是否做为查询结果映射
            if (col.getValue().isResult()) {
                if(sb.length() > 0){
                    sb.append(SqlKeyword.comma);
                }
                sb.append(alias);
                sb.append(col.getValue().name());
                sb.append(" AS ");
                sb.append("\"");
                sb.append(columnPrefix);
                sb.append(col.getKey().getName());
                sb.append("\"");
            }
        }while (iterator.hasNext());
        return sb.toString();
    }

    // ----------------------------

    // 转换成插入语句
    @Override
    public String convertSqlInsert(ZKDBMapInfo mapInfo) {
        if(mapInfo.isEmpty()){
            return "";
        }
        // 插入的字段名和值
        StringBuffer sbColumn = new StringBuffer();
        StringBuffer sbValue = new StringBuffer();
        for(Map.Entry<PropertyDescriptor, ZKColumn> item:mapInfo.getColumnSet()){
            if(item.getValue().isInsert()){
                if(sbColumn.length() > 0){
                    sbColumn.append(", ");
                }
                if(sbValue.length() > 0){
                    sbValue.append(", ");
                }
                sbColumn.append(item.getValue().name());
                if (item.getValue().javaType() == ZKJson.class) {
                    // ZKJson 格式数据为 null 时，默认插入 {}
                    sbValue.append("<choose>");
                    sbValue.append("<when test = \"");
                    sbValue.append(item.getKey().getName());
                    sbValue.append(" == null");
                    sbValue.append("\" >");
                    sbValue.append("'{}'");
                    sbValue.append("</when>");
                    sbValue.append("<otherwise>");
                    sbValue.append("#{");
                    sbValue.append(item.getKey().getName());
                    sbValue.append("}");
                    sbValue.append("</otherwise>");
                    sbValue.append("</choose>");
                }
                else {
                    sbValue.append("#{");
                    sbValue.append(item.getKey().getName());
                    sbValue.append("}");
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append(ZKDBScriptKey.Script[0]);
        sb.append(SqlKeyword.insert);
        sb.append(mapInfo.getTableName());
        sb.append(SqlKeyword.bracketed[0]);
        sb.append(sbColumn.toString());
        sb.append(SqlKeyword.bracketed[1]);
        sb.append(SqlKeyword.value);
        sb.append(SqlKeyword.bracketed[0]);
        sb.append(sbValue.toString());
        sb.append(SqlKeyword.bracketed[1]);
        sb.append(ZKDBScriptKey.Script[1]);
        return sb.toString();
    }

    @Override
    public String convertSqlUpdate(ZKDBMapInfo mapInfo) {
        if(mapInfo.isEmpty()){
            return "";
        }
        StringBuffer sbSet = new StringBuffer();
        StringBuffer sbCondition = new StringBuffer();
        for(Map.Entry<PropertyDescriptor, ZKColumn> item : mapInfo.getColumnSet()){
            // 修改的条件
            if(item.getValue().isPk() || item.getValue().update().isCondition()){
                if(sbCondition.length() > 0){
                    sbCondition.append(ZKDBOptLogic.AND.getEsc());
                }
                sbCondition.append(item.getValue().name());
                sbCondition.append(ZKDBOptComparison.EQ.getEsc());
                sbCondition.append("#{");
                sbCondition.append(item.getKey().getName());
                sbCondition.append("}");
            }
            // 修改 set 语句
            if(item.getValue().update().value()){
                if(!item.getValue().update().isForce()){
                    // 不强制修改，添加 <if>
                    sbSet.append("<if test = \"");
                    sbSet.append(item.getKey().getName());
                    sbSet.append(" != null\">");
                }
                if(ZKStringUtils.isEmpty(item.getValue().update().setSql())){
                    if (item.getValue().javaType() == ZKJson.class) {
                        sbSet.append(item.getValue().name());
                        sbSet.append(" = ");
                        sbSet.append("JSON_MERGE_PATCH(");
                        sbSet.append(item.getValue().name());
                        sbSet.append(", ");
                        sbSet.append("#{");
                        sbSet.append(item.getKey().getName());
                        sbSet.append("}), ");
                    }
                    else {
                        sbSet.append(item.getValue().name());
                        sbSet.append(" = ");
                        sbSet.append("#{");
                        sbSet.append(item.getKey().getName());
                        sbSet.append("}, ");
                    }
                }else{
                    sbSet.append(item.getValue().update().setSql());
                    sbSet.append(SqlKeyword.comma);
                }

                if(!item.getValue().update().isForce()){
                    // 不强制修改，添加 <if>
                    sbSet.append("</if>");
                }
            }
        }

        StringBuffer sb = new StringBuffer();
        sb.append(ZKDBScriptKey.Script[0]);
        sb.append(SqlKeyword.update);
        sb.append(mapInfo.getTableName());
        sb.append(SqlKeyword.set);
        // sb.append("<set >");
        sb.append("<trim suffixOverrides=\",\">");
        sb.append(sbSet.toString());
        sb.append("</trim>");
        // sb.append("</set>");
        sb.append(SqlKeyword.where);
        sb.append(sbCondition.toString());
        sb.append(ZKDBScriptKey.Script[1]);
        return sb.toString();
    }

    // 逻辑删除，逻辑删除语句，实体中定义
    @Override
    public String convertSqlDel(ZKDBMapInfo mapInfo, String delSetSql) {
        StringBuffer sb = new StringBuffer();
        sb.append(SqlKeyword.update);
        sb.append(mapInfo.getTableName());
        sb.append(SqlKeyword.set);
        sb.append(delSetSql);
        sb.append(SqlKeyword.where);
        sb.append(convertPkCondition(mapInfo, null));
        return sb.toString();
    }

    // 物理删除
    @Override
    public String convertSqlDiskDel(ZKDBMapInfo mapInfo) {
        StringBuffer sb = new StringBuffer();
        sb.append(SqlKeyword.deleteFrom);
        sb.append(mapInfo.getTableName());
        sb.append(SqlKeyword.where);
        sb.append(convertPkCondition(mapInfo, null));
        return sb.toString();
    }

    /********************************************************/
    /***  **/
    /********************************************************/

    /**
     * 添加查询条件; 没有查询的逻辑关系，即没 AND OR
     *
     * @Title: convertScriptQueryCondition
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 14, 2021 8:05:17 PM
     * @param queryLogic 仅在 javaClassz 为 ZKJson 时有效，因为，会将 Json 展开为一个一个属性做为查询条件；默认为: AND
     * @param sb
     * @param tableAlias
     *            表的别名；
     * @param columnName 字段名；如果有表别名，需要包含在字段名中；
     * @param attrName
     * @param queryType
     * @param javaClassz
     * @param formats
     * @param isCaseSensitive
     * @return String
     */
    public static void appendScriptQueryCondition(ZKDBOptLogic queryLogic, StringBuffer sb, String tableAlias,
        String columnName, String attrName, ZKDBOptComparison queryType, Class<?> javaClassz, String[] formats,
        boolean isCaseSensitive) {
        if(ZKStringUtils.isEmpty(tableAlias)){
            tableAlias = "";
        }else{
            tableAlias = tableAlias + ".";
        }
        String dateFormatPattern = null;
        if (formats != null && formats.length > 0) {
            dateFormatPattern = formats[0];
        }
        if (javaClassz == ZKJson.class) {
            if(queryLogic == null){
                queryLogic = ZKDBOptLogic.AND;
            }
            // JSON 属性条件细分
            sb.append("<foreach item=\"_v\" index=\"_k\" collection=\"");
            sb.append(attrName);
            sb.append(".getKeyValues()\" open=\"\" separator=\"");
            sb.append(queryLogic.getEsc());
            sb.append("\" close=\"\">");
            appendScriptQueryCondition(sb, "JSON_UNQUOTE(JSON_EXTRACT(" + tableAlias + columnName + ", #{_k}))",
                    "_v", queryType, String.class, dateFormatPattern, isCaseSensitive);
            sb.append("</foreach>");
        }
        else {
            appendScriptQueryCondition(sb, tableAlias + columnName, attrName, queryType, javaClassz,
                    dateFormatPattern, isCaseSensitive);
        }
    }

    /**
     * 添加查询条件值; 没有查询的逻辑关系，即没 AND OR
     *
     * @Title: convertScriptQueryCondition
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 14, 2021 8:06:10 PM
     * @param sb
     * @param columnName
     *            字段名；如果有表别名，需要包含在字段名中；
     * @param attrName
     *            java 属性名; 这里会添加 #{}
     * @param queryType
     *            查询类型
     * @param javaClassz
     *            查询字段的 java 数据类型
     * @param dateFormatPattern
     *            日期格式化参数；不为 null 时，格式化数据库字段；不为 null 且属性 java 数据类型为 Date
     *            时，同时也用他格式化参数；
     * @param isCaseSensitive
     *            是否区分大小，注，此参数仅在 javaClassz 为 String.class
     *            类型下有效；true-区分大小；false-不区分大小写；
     * @return void
     */
    public static void appendScriptQueryCondition(StringBuffer sb, String columnName, String attrName,
        ZKDBOptComparison queryType, Class<?> javaClassz, String dateFormatPattern, boolean isCaseSensitive) {
        switch (queryType) {
            case ISNULL:
            case ISNOTNULL:
                sb.append(columnName);
                sb.append(queryType.getEsc());
                break;
            case IN:
            case NIN: // 为 IN, NIN 时，Java 数据类型 要为数组或 List
                sb.append(columnName);
                sb.append(queryType.getEsc());
                sb.append("<foreach item=\"_v\" index=\"_index\" collection=\"");
                sb.append(attrName);
                sb.append("\" open=\"(\" separator=\",\"");
                sb.append(" close=\")\">");
                sb.append(
                        appendConditionFormatValue("#{_v}", queryType, javaClassz, dateFormatPattern, isCaseSensitive));
                sb.append("</foreach>");
                break;
            default:
                appendConditionFormat(sb, columnName, "#{" + attrName + "}", queryType, javaClassz, dateFormatPattern,
                        isCaseSensitive);
                break;
        }
    }

    /**
     *
     *
     * @Title: appendConditionFormat
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 14, 2021 8:12:10 PM
     * @param sb
     * @param columnName 字段名；如果有表别名，需要包含在字段名中；
     * @param attrName
     * @param queryType
     * @param javaClassz
     * @param dateFormatPattern
     *            日期格式化参数；不为 null 时，格式化数据库字段；不为 null 且属性 java 数据类型为 Date
     *            时，同时也用他格式化参数；
     * @param isCaseSensitive
     *            是否区分大小，注，此参数仅在 javaClassz 为 String.class
     *            类型下有效；true-区分大小；false-不区分大小写；
     * @return void
     */
    public static void appendConditionFormat(StringBuffer sb, String columnName, String attrName,
        ZKDBOptComparison queryType, Class<?> javaClassz, String dateFormatPattern, boolean isCaseSensitive) {
        sb.append(appendConditionFormatColumnName(columnName, javaClassz, dateFormatPattern, isCaseSensitive));
        sb.append(queryType.getEsc());
        sb.append(appendConditionFormatValue(attrName, queryType, javaClassz, dateFormatPattern, isCaseSensitive));
    }

    /**
     *
     * @MethodName appendConditionFormatColumnName
     * @param columnName 字段名；如果有表别名，需要包含在字段名中；
     * @param javaClassz
     * @param dateFormatPattern
     * @param isCaseSensitive
     * @return java.lang.String
     * @throws
     * @Author bs
     * @DATE 2022-09-28 18:17:344
     */
    public static String appendConditionFormatColumnName(String columnName, Class<?> javaClassz,
        String dateFormatPattern, boolean isCaseSensitive) {

        StringBuffer tsb = null;
        // 日期格式化
        if (dateFormatPattern != null) {
            tsb = new StringBuffer();
            tsb.append("DATE_FORMAT(");
            tsb.append(columnName);
            tsb.append(", \"");
            tsb.append(dateFormatPattern);
            tsb.append("\")");
            columnName = tsb.toString();

        }
        // 字段 java 类型为字符串，且不区分大小定时
        if (javaClassz == String.class && !isCaseSensitive) {
            tsb = new StringBuffer();
            tsb.append("UPPER(");
            tsb.append(columnName);
            tsb.append(")");
            columnName = tsb.toString();
        }
        return columnName;
    }

    public static String appendConditionFormatValue(String attrName, ZKDBOptComparison queryType,
        Class<?> javaClassz, String dateFormatPattern, boolean isCaseSensitive) {

        StringBuffer tsb = null;
        // 日期格式化
        if (javaClassz == Date.class && dateFormatPattern != null) {
            tsb = new StringBuffer();
            tsb.append("DATE_FORMAT(");
            tsb.append(attrName);
            tsb.append(", \"");
            tsb.append(dateFormatPattern);
            tsb.append("\")");
            attrName = tsb.toString();
        }
        // 字段 java 类型为字符串，且不区分大小定时
        if (javaClassz == String.class && !isCaseSensitive) {
            tsb = new StringBuffer();
            tsb.append("UPPER(");
            tsb.append(attrName);
            tsb.append(")");
            attrName = tsb.toString();
        }
        // like 和 not like 处理；仅 java 数据类型为 String.class 时进行处理；
        if (javaClassz == String.class && (queryType == ZKDBOptComparison.LIKE || queryType == ZKDBOptComparison.NLIKE)) {
            // "CONCAT('%', REPLACE(REPLACE(REPLACE(?, '\\\\\\\\', '\\\\\\\\\\\\\\\\'), '_', '\\\\_'), '%', '\\\\%'), '%')"
            //
            // public static final String replaceString = "$1LIKE$2$3CONCAT('%', REPLACE(REPLACE(REPLACE(?, '\\\\\\\\', '\\\\\\\\\\\\\\\\'), '_', '\\\\_'), '%', '\\\\%'), '%')";
            tsb = new StringBuffer();
            tsb.append("CONCAT('%', REPLACE(REPLACE(REPLACE(");
            tsb.append(attrName);
            tsb.append(", '\\\\\\\\', '\\\\\\\\\\\\\\\\'), '_', '\\\\_'), '%', '\\\\%'), '%')");
            attrName = tsb.toString();
        }
        return attrName;
    }

}

