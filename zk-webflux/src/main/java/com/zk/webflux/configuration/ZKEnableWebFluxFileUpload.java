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
* @Title: ZKEnableWebFluxFileUpload.java 
* @author Vinson 
* @Package com.zk.webflux.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 23, 2024 9:50:33 PM 
* @version V1.0 
*/
package com.zk.webflux.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

/** 
* @ClassName: ZKEnableWebFluxFileUpload 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ImportAutoConfiguration(value = { ZKWebFluxFileUploadConfiguration.class })
public @interface ZKEnableWebFluxFileUpload {

}
