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
 * @Title: ZKMyBatisDao.java 
 * @author Vinson 
 * @Package com.zk.db.annotation 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 9:59:17 AM 
 * @version V1.0   
*/
package com.zk.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/** 
* @ClassName: ZKMyBatisDao 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface ZKMyBatisDao {

    /**
     * The value may indicate a suggestion for a logical component name, to be
     * turned into a Spring bean in case of an autodetected component.
     * 
     * @return the suggested component name, if any
     */
    String value() default "";

}
