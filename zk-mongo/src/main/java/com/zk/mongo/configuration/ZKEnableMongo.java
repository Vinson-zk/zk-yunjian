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
* @Title: ZKEnableMongo.java 
* @author Vinson 
* @Package com.zk.mongo.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 6, 2023 3:44:13 PM 
* @version V1.0 
*/
package com.zk.mongo.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

/**
 * 需要注入 zkMongoProperties bean
 * 
 * @ClassName: ZKEnableMongo
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(value = { ZKMongoConfiguration.class })
public @interface ZKEnableMongo {
    public static final String printLog = "[^_^:20230209-2148-004] ----- zk-mongo config: ";
}
