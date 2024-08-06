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
* @Title: ZKDemoConfigAnnotation.java 
* @author Vinson 
* @Package com.zk.demo.web.context.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 11, 2023 3:51:46 PM 
* @version V1.0 
*/
package com.zk.demo.web.context.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Import;

/** 
* @ClassName: ZKDemoConfigAnnotation 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(value = { ZKConfigImportOne.class, ZKConfigImportTwo.class, ZKConfigBeanDefinitionOne.class,
        ZKConfigBeanDefinitionTwo.class })
@ImportAutoConfiguration(value = { ZKConfigImportAutoOne.class })
public @interface ZKDemoConfigAnnotation {
    
}
