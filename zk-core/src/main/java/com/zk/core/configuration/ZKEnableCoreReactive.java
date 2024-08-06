/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKEnableCoreReactive.java 
* @author Vinson 
* @Package com.zk.core.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 24, 2024 7:04:02 PM 
* @version V1.0 
*/
package com.zk.core.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import com.zk.core.configuration.ZKCoreConfiguration.ZKCoreAutoConfigutation;
import com.zk.core.configuration.ZKEnableCore.ZKCoreInit;

/**
 * @ClassName: ZKEnableCoreReactive
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ZKEnableCore
@ImportAutoConfiguration(value = { ZKCoreAutoConfigutation.class, ZKCoreInit.class,
        ZKCoreReactiveAutoConfiguration.class })
public @interface ZKEnableCoreReactive {

}
