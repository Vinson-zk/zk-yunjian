/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKMySqlDialect.java 
 * @author Vinson 
 * @Package com.zk.db.dialect.support 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 10:30:17 AM 
 * @version V1.0   
*/
package com.zk.db.dialect.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zk.core.commons.data.ZKJson;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.db.dialect.ZKDialect;
import com.zk.db.parser.ZKCountSqlParser;
import com.zk.db.parser.ZKCountSqlParser.ZKModel;
import com.zk.db.parser.ZKDefaultCountSqlParser;

/**
 * @ClassName: ZKMySqlDialect
 * @Description: Mysql 方言的实现
 * @author Vinson
 * @version 1.0
 */
public class ZKMySqlDialect implements ZKDialect {

    static ZKCountSqlParser DFAULT_COUNTSQLPARSER = new ZKDefaultCountSqlParser();

    /**
     * 不区分大小写替换
     */
//    public static final Pattern likeParamPattern = Pattern.compile("(\\b)like(\\b)([^\\#\\{,^\\$\\{]*)\\#\\{(\\S*)\\}",
//            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    public static final Pattern likeParamPattern = Pattern.compile("(\\b)like(\\b)([^\\?]*)\\?",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

//  public static final String replaceString = "$1LIKE$2$3CONCAT('%', REPLACE(REPLACE(REPLACE(#{$4}, '\\\\\\\\', '\\\\\\\\\\\\\\\\'), '_', '\\\\_'), '%', '\\\\%'), '%')";
    public static final String replaceString = "$1LIKE$2$3CONCAT('%', REPLACE(REPLACE(REPLACE(?, '\\\\\\\\', '\\\\\\\\\\\\\\\\'), '_', '\\\\_'), '%', '\\\\%'), '%')";

    /**
     * sql 中是否有分页相关关键字
     */
    private static final Pattern isPagePattern = Pattern.compile("(\\S*) limit (\\S*)|(\\S*)count\\(([^\\)]+)\\)",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    /**
     * sql 中是否有替换函数
     */
    private static final Pattern isReplacePattern = Pattern.compile("(\\S*)REPLACE\\(([^\\)]+)\\)",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public boolean supportsPage() {
        return true;
    }

    @Override
    public boolean supportsJson() {
        return true;
    }

    /**
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
    public String getCountSql(String sourceSql, String countSqlBlock, ZKModel model, boolean removeOrderBy) {
        return DFAULT_COUNTSQLPARSER.getCountSql(sourceSql, countSqlBlock, model, removeOrderBy);
    }

    public String getPageSql(String sourceSql, ZKPage<?> page) {
        return getPageSql(sourceSql, page.getStartRow(), page.getPageSize());
    }

    /**
     * 
     * @param sourceSql
     *            查询 sql
     * @param offset
     *            分页开始纪录条数
     * @param limit
     *            分页纪录条数
     * @return 分页查询 sql
     * @see com.zk.db.dialect.ZKDialect#getPageSql(java.lang.String, int, int)
     */
    @Override
    public String getPageSql(String sourceSql, int offset, int limit) {
        StringBuilder stringBuilder = new StringBuilder(sourceSql);
        stringBuilder.append(" limit ");
        if (offset > 0) {
            stringBuilder.append(offset).append(",").append(limit);
        }
        else {
            stringBuilder.append(limit);
        }
        return stringBuilder.toString();
    }

    /**
     * sql 是否有分页
     *
     * @Title: isPage
     * @Description: TODO(simple description this method what to do.)
     * @author zk
     * @date 2018年3月12日 下午12:57:45
     * @param sql
     * @return
     * @return boolean
     */
    @Override
    public boolean isPage(String sql) {
        return isPagePattern.matcher(sql).find();
    }

    /**
     * sql 是否有替换字符
     *
     * @Title: isReplace
     * @Description: TODO(simple description this method what to do.)
     * @author zk
     * @date 2018年3月12日 下午12:57:45
     * @param sql
     * @return
     * @return boolean
     */
    public boolean isReplace(String sql) {
        return isReplacePattern.matcher(sql).find();
    }

    @Override
    @Deprecated
    public String replaceLikeParam(String sql) {
        // 不做这个替换，则需要直接自己在 sql 中写
        Matcher matcher = likeParamPattern.matcher(sql);
        return matcher.replaceAll(replaceString);
//        return sql;
    }

    @Override
    public String getInsertParam(ZKJson json) {
        return (json == null || json.size() < 1) ? null : ZKJsonUtils.toJsonStr(json);
//      String s = this.getJsonStr(json);
//      return StringUtils.isEmpty(s)?null:s;
//      return StringUtils.isEmpty(s)?null: "'" + s + "'";
    }

    @Override
    public String getUpdateParam(ZKJson json) {
        return (json == null || json.size() < 1) ? null : ZKJsonUtils.toJsonStr(json);
//      String s = this.getJsonStr(json);
//      return StringUtils.isEmpty(s)?null:s;
//      return StringUtils.isEmpty(s)?null: "'" + s + "'";
    }

    @Override
    public Object getSelectParam(ZKJson json) {
//      Map<String, String> sMap = makeKeyValue("$", json);
//      return (sMap == null || sMap.size() < 1)?null:sMap.entrySet();
        return (json == null || json.size() < 1) ? null : ZKJsonUtils.toJsonStr(json);
//      return (json == null || json.size()<1)?null:json;
    }

//  /**
//   * 制作 JSON 函数支持的 json 字符串
//   * @param json
//   * @return
//   */
//  protected String getJsonStr(Json<?> json){
//      String resStr = null;
//      if(json != null){
//          if(json.size() > 0){
//              resStr = "{";
//              String tStr = null;
//              for(Entry<String, ?> e : json.entrySet()){
//                  if(e.getValue() instanceof Json<?>){
//                      tStr = null;
//                      tStr = this.getJsonStr((Json<?>)e.getValue());
//                      if(tStr != null){
//                          resStr += "\"" + e.getKey() + "\":" + tStr + ",";
//                      }
//                  }else{
//                      resStr += "\"" + e.getKey() + "\":" + this.getSqlValueByJsonValue(e.getValue(), "\"") + ",";
//                  }
//              }
//              resStr = resStr.substring(0, resStr.length()-1) + "}";
//          }
//      }
//      return resStr;
//  }

//  /**
//   * 制作插入的 json sql 字符；如：, '$.t2', 'update t2', '$.t3', 'add attr t3', '$.t4.t1', 'ddd'
//   * JSON_INSERT('{}', '$.t1', 10, '$.t2', '[true, false]', '$.t4', JSON_INSERT('{}', '$.t1', 10))
//   * @param json
//   * @return
//   */
//  protected String getJsonInsert(Json<?> json){
//      Map<String, String> sMap = makeKeyValue("$", json);
//      if(sMap != null){
//          String insStr = null;
//          if(sMap.size() > 0){
//              insStr = "JSON_INSERT('{}'";
//              for(Entry<String, String> e:sMap.entrySet()){
//                  insStr += ". '" + e.getKey() + "'," + e.getValue();
//              }
//              insStr += ")";
//          }else{
//              // 属性 json 类型为空时处理
//              insStr = null;
//          }
//          return insStr;
//      }else{
//          return null;
//      }
//  }

}
