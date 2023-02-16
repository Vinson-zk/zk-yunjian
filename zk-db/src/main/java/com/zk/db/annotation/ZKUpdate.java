package com.zk.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: 修改 sql 注解
 * @ClassName ZKUpdate
 * @Package com.zk.db.annotation
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-08 00:17:46
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface ZKUpdate {

    /**
     * true 是修改字段；false-不修改；
     * true 时，如果 setSqls 不为空，直接使用 setSql 做为修改 setSql 片段；
     *
     * @MethodName isUpdate
     * @return boolean
     * @throws
     * @Author bs
     * @DATE 2022-09-08 00:24:969
     */
    boolean value() default true;

    /**
     * 修改的 setSql 片段；isUpdate 为 true 才会生效；
     * @MethodName setSql
     * @return java.lang.String
     * @throws
     * @Author bs
     * @DATE 2022-09-08 00:19:769
     */
    String setSql() default "";

    /**
     * 此字段是否做修改时的条件；默认为 false；
     * 如果为 true 会被强制做为查询条件；
     * @MethodName isCondition
     * @return boolean
     * @throws
     * @Author bs
     * @DATE 2022-09-08 00:20:168
     */
    boolean isCondition() default false;

    /**
     * 是否强制修改，true-强制修改；false-有值时才修改；
     *
     * 主要是为集成 mybatis; false 时会修改语句会包裹 <if test='' ></if>
     *
     * @MethodName isForce
     * @return boolean
     * @throws
     * @Author bs
     * @DATE 2022-09-17 11:51:805
     */
    boolean isForce() default false;
}
