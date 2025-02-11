package com.zk.db.mybatis.commons;

import com.zk.core.commons.data.ZKJson;
import com.zk.core.commons.data.ZKJsonArray;
import com.zk.db.commons.*;

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
 * @Description: 查询条件，字段脚本
 * @ClassName ZKDBQueryColScript
 * @Package com.zk.db.mybatis.commons
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-13 11:32:12
 **/
public class ZKDBQueryScript extends ZKDBQuery{

    /**
     *
     * @Title: as
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 15, 2022 11:48:38 AM
     * @param queryCondition
     *            需要包裹查询条件，一般为 ZKDBQueryCol；
     * @param testRule
     *            isForce 为 true 时，此参数无效；fasle 时，是否做查询条件的判断规则；
     *            0 - 不为 null 且 不为空；为空根据 javaClass 自动判断；!= ""/0/isEmpty/false/
     *            1 - 不为 null 但 为空；为空根据 javaClass 自动判断；== ""/0/isEmpty/false/
     *            2 - 不为 null
     *            3 - 为 null
     *            4 - 为 null 或 为空；为空根据 javaClass 自动判断；== ""/0/isEmpty/false/
     * @param attrName
     *            java 属性名; 这里会添加 #{}
     * @param javaClassz
     *            查询字段的 java 数据类型
     * @return ZKDBQueryConditionCol
     */
    public static ZKDBQueryScript asIf(ZKDBQuery queryCondition, int testRule, String attrName,
                                          Class<?> javaClassz) {
        StringBuffer sb = new StringBuffer();
        sb.append("<if ");
        convertTestRuleByClass(sb, testRule, attrName, javaClassz);
        sb.append(" >");
        return as(queryCondition, sb.toString(), "</if>");
    }

    public static ZKDBQueryScript as(ZKDBQuery queryCondition, String prefix, String suffix) {
        return new ZKDBQueryScript(queryCondition, prefix, suffix);
    }

    private ZKDBQuery queryCondition;

    // 包裹脚本; 此时，父类中的，prefix 和 suffix 为包裹的 mybatis 脚本；
    private ZKDBQueryScript(ZKDBQuery queryCondition, String prefix, String suffix) {
        super(prefix, suffix);
        this.queryCondition = queryCondition;
    }

    public ZKDBQuery getQueryCondition() {
        return queryCondition;
    }

    @Override
    public void toQueryCondition(ZKSqlConvert convert, StringBuffer sb, String tableAlias, ZKDBOptLogic queryLogic) {
        this.convertQueryCondition(convert, sb, tableAlias, queryLogic);
    }

    @Override
    protected void convertQueryCondition(ZKSqlConvert convert, StringBuffer sb, String tableAlias,
                                         ZKDBOptLogic queryLogic) {
        sb.append(this.getPrefix());
        this.getQueryCondition().toQueryCondition(convert, sb, tableAlias, queryLogic);
        sb.append(this.getSuffix());
    }

    /***************************************************************/
    /***************************************************************/

    /**
     * 转换 mybatis 的查询 test 规则
     * 0 - 不为 null 但 不为空；为空根据 javaClass 自动判断；!= ""/0/isEmpty/false/
     * 1 - 不为 null 但 为空；为空根据 javaClass 自动判断；== ""/0/isEmpty/false/
     * 2 - 不为 null
     * 3 - 为 null
     * 4 - 为 null 或 为空；为空根据 javaClass 自动判断；== ""/0/isEmpty/false/
     * @MethodName
     * @param
     * @throws
     * @Author bs
     * @DATE 2022-09-13 10:59:996
     */
    private static void convertTestRuleByClass(StringBuffer sb, int testRule, String attrName, Class<?> javaClassz) {
        sb.append("test = \"");
        if(testRule == 0){
            sb.append(attrName);
            sb.append(" != null");
            convertTestRuleEmptyByClass(sb, " and ", false, attrName, javaClassz);
        }else if(testRule == 1){
            sb.append(attrName);
            sb.append(" != null");
            convertTestRuleEmptyByClass(sb, " and ",true, attrName, javaClassz);
        }else if(testRule == 2){
            sb.append(attrName);
            sb.append(" != null");
        }else if(testRule == 3){
            sb.append(attrName);
            sb.append(" == null");
        }else if(testRule == 4){
            sb.append(attrName);
            sb.append(" == null");
            convertTestRuleEmptyByClass(sb, " or ",true, attrName, javaClassz);
        }
        sb.append(" \"");
    }

    // 根据 java 类型，自动设置为空的条件
    private static void convertTestRuleEmptyByClass(StringBuffer sb, String operatorLogic, boolean isEmpty,
        String attrName, Class<?> javaClassz) {
        operatorLogic = operatorLogic == null?"":operatorLogic;
        if (javaClassz == String.class) {
            sb.append(operatorLogic);
            // 字符串
            sb.append(attrName);
            if(isEmpty){
                sb.append(" == ''");
            }else{
                sb.append(" != ''");
            }
        }
        else if (javaClassz == ZKJson.class) {
            sb.append(operatorLogic);
            // Json 类型
            sb.append(attrName).append(".getKeyValues()");
            if(isEmpty){
                sb.append(" == null");
            }else{
                sb.append(" != null");
            }
        }
        else if (javaClassz == List.class || javaClassz == ZKJsonArray.class) {
            sb.append(operatorLogic);
            // 数组或 List
            sb.append(attrName).append(".isEmpty()");
            if(isEmpty){
                sb.append(" == true");
            }else{
                sb.append(" == false");
            }
        }
        else if (javaClassz.isArray()) {
            sb.append(operatorLogic);
            // 数组
            sb.append(attrName).append(".length");
            if(isEmpty){
                sb.append(" == 0");
            }else{
                sb.append(" > 0");
            }
        }else if (javaClassz == Boolean.class) {
            sb.append(operatorLogic);
            // Boolean
            sb.append(attrName);
            if(isEmpty){
                sb.append(" == false");
            }else{
                sb.append(" == true");
            }
        }else {
            // 其他类型; 不处理
        }
    }
}
