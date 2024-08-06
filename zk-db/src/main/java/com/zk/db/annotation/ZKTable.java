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
* @Title: ZKTable.java 
* @author Vinson 
* @Package com.zk.db.annotation 
* @Description: TODO(simple description this file what to do. ) 
* @date Sep 10, 2020 2:11:52 PM 
* @version V1.0 
*/
package com.zk.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
* @ClassName: ZKTable 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface ZKTable {

    // 表名
    String name() default "";

    // 别名
    String alias() default "t0";

    // 说明
    String comment() default "";

    // 默认排序, 都直接填写数据库字段; 不要添加 order by
    String[] orderBy() default {};

}
