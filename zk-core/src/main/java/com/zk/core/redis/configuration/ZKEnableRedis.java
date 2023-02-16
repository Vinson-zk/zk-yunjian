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
* @Title: ZKEnableRedis.java 
* @author Vinson 
* @Package com.zk.core.redis.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 6, 2023 4:03:42 PM 
* @version V1.0 
*/
package com.zk.core.redis.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

/**
 * 需要注入 ZKRedisProperties 对象
 * 
 * @ClassName: ZKEnableRedis
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ImportAutoConfiguration(value = { ZKRedisConfiguration.class })
public @interface ZKEnableRedis {
    public static final String printLog = "[^_^:20230209-2148-003] ----- zk-redis config: ";
}
