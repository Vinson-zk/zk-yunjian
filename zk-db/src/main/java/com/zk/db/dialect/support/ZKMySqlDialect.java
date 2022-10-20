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
import com.zk.core.utils.ZKJsonUtils;
import com.zk.db.dialect.ZKDialect;

/**
 * @ClassName: ZKMySqlDialect
 * @Description: Mysql 方言的实现
 * @author Vinson
 * @version 1.0
 */
public class ZKMySqlDialect implements ZKDialect {

//  public static final String patternStr = "(\\b)like(\\b)([^\\#\\{,^\\$\\{]*)\\#\\{(\\S*)\\}";
    public static final String patternStr = "(\\b)like(\\b)([^\\?]*)\\?";

    /**
     * 不区分大小写替换
     */
    public static final Pattern likeParamPattern = Pattern.compile(patternStr,
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

    @Override
    public String getLimitString(String sql, int offset, int limit) {
        return getLimitString(sql, offset, Integer.toString(offset), Integer.toString(limit));
    }

    public boolean supportsLimit() {
        return true;
    }

    /**
     * 将sql变成分页sql语句,提供将offset及limit使用占位符号(placeholder)替换.
     * 
     * <pre>
     * 如mysql
     * dialect.getLimitString("select * from user", 12, ":offset",0,":limit") 将返回
     * select * from user limit :offset,:limit
     * </pre>
     *
     * @param sql
     *            实际SQL语句
     * @param offset
     *            分页开始纪录条数
     * @param offsetPlaceholder
     *            分页开始纪录条数－占位符号
     * @param limitPlaceholder
     *            分页纪录条数占位符号
     * @return 包含占位符的分页sql
     */
    public String getLimitString(String sql, int offset, String offsetPlaceholder, String limitPlaceholder) {
        StringBuilder stringBuilder = new StringBuilder(sql);
        stringBuilder.append(" limit ");
        if (offset > 0) {
            stringBuilder.append(offsetPlaceholder).append(",").append(limitPlaceholder);
        }
        else {
            stringBuilder.append(limitPlaceholder);
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
    public String replaceLikeParam(String sql) {
        // 不做这个替换，直接自己在 sql 中写
        Matcher matcher = likeParamPattern.matcher(sql);
        return matcher.replaceAll(replaceString);
//        return sql;
    }

    @Override
    public boolean supportsJson() {
        return true;
    }

    @Override
    public String getInsertParam(ZKJson json) {
        return (json == null || json.size() < 1) ? null : ZKJsonUtils.writeObjectJson(json);
//      String s = this.getJsonStr(json);
//      return StringUtils.isEmpty(s)?null:s;
//      return StringUtils.isEmpty(s)?null: "'" + s + "'";
    }

    @Override
    public String getUpdateParam(ZKJson json) {
        return (json == null || json.size() < 1) ? null : ZKJsonUtils.writeObjectJson(json);
//      String s = this.getJsonStr(json);
//      return StringUtils.isEmpty(s)?null:s;
//      return StringUtils.isEmpty(s)?null: "'" + s + "'";
    }

    @Override
    public Object getSelectParam(ZKJson json) {
//      Map<String, String> sMap = makeKeyValue("$", json);
//      return (sMap == null || sMap.size() < 1)?null:sMap.entrySet();
        return (json == null || json.size() < 1) ? null : ZKJsonUtils.writeObjectJson(json);
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
