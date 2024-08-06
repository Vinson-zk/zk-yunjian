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
* @Title: ZKColumn.java 
* @author Vinson 
* @Package com.zk.db.annotation 
* @Description: TODO(simple description this file what to do. ) 
* @date Sep 10, 2020 11:29:41 AM 
* @version V1.0 
*/
package com.zk.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
* @ClassName: ZKColumn 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/

/*
1、RetentionPolicy.SOURCE：注解只保留在源文件，当Java文件编译成class文件的时候，注解被遗弃；
2、RetentionPolicy.CLASS：注解被保留到class文件，但jvm加载class文件时候被遗弃，这是默认的生命周期；
3、RetentionPolicy.RUNTIME：注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在；
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Documented
public @interface ZKColumn {

    /**
     * 表字段名；name 为空时，不处理；这样处理是为了方便删除父类中的注解；
     */
    String name() default "";

    /**
     * 是否是主键；可以是复合主键，但仅支持解析一组主键；
     *
     * 默认为 false
     *
     * @Title: isPk
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 11, 2020 2:28:51 PM
     * @return boolean
     */
    boolean isPk() default false;

    /**
     * 字段说明；默认为 空
     *
     * @Title: comment
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 15, 2020 11:38:46 AM
     * @return String
     */
    String comment() default "";

    /**
     * 实体属性的 java 类型；
     *
     * 主要是为解决，集合中对象的类型、数据交互使用的类型与在代码中的类型的场景
     *
     * 默认 String.class
     *
     * @Title: javaType
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 15, 2020 11:38:32 AM
     * @return
     * @return Class<?>
     */
    Class<?> javaType() default String.class;

    /**
     * 格式化字符串，比如日期格式化; 第一位为数据库的格式，第二位为 java 中转换的格式; 注：第二位在 script sql 生成中未使用；
     *
     * mysql 格式化字符说明：%Y-%m-%d %H:%i:%s
     *
     * %Y：代表4位的年份； %y：代表2为的年份；
     *
     * %m：代表月, 格式为(01……12)； %c：代表月, 格式为(1……12)；
     *
     * %d：代表月份中的天数,格式为(00……31)； %e：代表月份中的天数, 格式为(0……31)；
     *
     * %H：代表小时,格式为(00……23)；%k： 代表 小时,格式为(0……23)； %h： 代表小时,格式为(01……12)；
     *
     * %I： 代表小时,格式为(01……12)； %l ：代表小时,格式为(1……12)；
     *
     * %i： 代表分钟, 格式为(00……59)；
     *
     * %r：代表 时间,格式为12 小时(hh:mm:ss [AP]M)； %T：代表 时间,格式为24 小时(hh:mm:ss)；
     *
     * %S：代表 秒,格式为(00……59)； %s：代表 秒,格式为(00……59)；
     *
     */
    String[] formats() default {};

    /**
     * 字段是否做为结果映射，true-做为结果映射；false-不做为结果映射；不做为结果集时，可以做查询条件；
     *
     * 默认为 true
     *
     * @Title: isResult
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 15, 2020 11:38:34 AM
     * @return
     * @return boolean
     */
    boolean isResult() default true;

    /**
     * 字段是否插入；
     *
     * 默认为 true
     *
     * @Title: isInsert
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 15, 2020 11:38:41 AM
     * @return
     * @return boolean
     */
    boolean isInsert() default true;

    ZKUpdate update() default @ZKUpdate(false);

    ZKQuery query() default @ZKQuery(false);

}
