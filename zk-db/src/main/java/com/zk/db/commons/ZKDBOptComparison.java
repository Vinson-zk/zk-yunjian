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
 * @Description: sql 比较运算符
 * @ClassName ZKDBOptComparison
 * @Package com.zk.db.commons
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-26 00:07:10
 **/
public enum ZKDBOptComparison {


//  <!--
//  原符号:         <      <=      >       >=      &       '       "
//  替换符号:       &lt;    &lt;=   &gt;    &gt;=   &amp;   &apos;  &quot;
//  例如：sql如下：
//  where code &gt; 20000
//  第二种写法是用cdata符号包括住逻辑符号
//  <![CDATA[ >= ]]>
//  小于等于
//  <![CDATA[ <= ]]>
//   -->

    //
    EQ("=", " = ", " = "),
    //
    NEQ("!=", " != ", " != "),

    /**
     * > 大于
     */
    GT(">", " > ", " &gt; "),
    /**
     * >= 大于等于
     */
    GTE(">=", " >= ", " &gt;= "),

    /**
     * < 小于
     */
    LT("<", " < ", " &lt; "),

    /**
     * <= 小于等于
     */
    LTE("<=", " <= ", " &lt;= "),

    //
    IN("in", " in ", " in "),
    //
    NIN("not in", " not in ", " not in "),

    //
    LIKE("like", " like ", " like "),
    //
    NLIKE("not like", " not like ", " not like "),

    //
    ISNULL("is null", " is null ", " is null "),
    //
    ISNOTNULL("is not null", " is not null ", " is not null ");

    // key
    private final String key;

    // 符号
    private final String mark;
    // 转译字符
    private final String esc;

    // // 是否强制做为条件
    // private boolean isForce;
    //
    // // 是否区分大小写，字符内容时，生效
    // private boolean isCaseSensitive;

    ZKDBOptComparison(String key, String mark, String esc) {
        this.key = key;
        this.mark = mark;
        this.esc = esc;
    }

    /**
     * @return key sa
     */
    public String getKey() {
        return key;
    }

    /**
     * @return esc sa
     */
    public String getEsc() {
        return esc;
    }

    /**
     * @return mark sa
     */
    public String getMark() {
        return mark;
    }

}
