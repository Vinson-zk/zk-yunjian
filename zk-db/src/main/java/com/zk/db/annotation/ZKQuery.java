package com.zk.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.zk.db.commons.ZKDBOptComparison;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: 字段的查询条件注解
 * @ClassName ZKQuery
 * @Package com.zk.db.annotation
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-08 09:18:51
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface ZKQuery {

    /**
     * 此字段是否做查询时的条件；默认为 false
     * conditionSql 不为空时，直接使用使用 conditionSql；否则按配置生成查询条件语句; 如 mybatis 会生成 <if></if> 语句
     *
     * @MethodName isCondition
     * @return boolean
     * @throws
     * @Author bs
     * @DATE 2022-09-08 00:20:168
     */
    boolean value() default true;

    /**
     * 查询方式；注意 使用 IN 或 NIN 时，只能使用集合，此时 javaType 填写集合中对象实体的类型
     *
     * 默认为 ZKDBOptComparison.EQ
     *
     * @Title: queryType
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 15, 2020 11:38:29 AM
     * @return
     * @return ZKDBOptComparison
     */
    ZKDBOptComparison queryType() default ZKDBOptComparison.EQ;

    /**
     * 主要是为集成 mybatis 的 <if test='' ></if>
     * 是否强制做为查询条件；true-是；false-不是；
     * false 时，会根据 testRule 的条件规则，判断是否需要做为查询条件；
     * 默认为 false
     * @Title: isForce
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 15, 2020 11:38:22 AM
     * @return
     * @return boolean
     */
    boolean isForce() default false;

    /**
     * 主要是为集成 mybatis 的 <if test='' ></if>
     * isForce 为 true 时，此参数无效；fasle 时，是否做查询条件的判断规则；
     * 参数值见 ZKDBQueryScript.convertTestRuleByClass 参数说明；
     * @MethodName testRule 
     * @return int
     * @throws 
     * @Author bs
     * @DATE 2022-09-13 10:49:139
     */
    int testRule() default 0;

    /**
     * 是否区分大小写; 字段属性为 String/ZKJson 时，生效；true-区分大小写；false-不区分大小写；
     *
     * 默认为 false，不区分大小写
     *
     * @Title: isCaseSensitive
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 11, 2020 11:40:32 AM
     * @return
     * @return boolean
     */
    boolean isCaseSensitive() default false;

    /**
     * 查询条件的 sql 片段；isCondition 为 true 才会生效；
     * @MethodName conditionSql
     * @return java.lang.String
     * @throws
     * @Author bs
     * @DATE 2022-09-08 00:19:769
     */
    String conditionSql() default "";
}
