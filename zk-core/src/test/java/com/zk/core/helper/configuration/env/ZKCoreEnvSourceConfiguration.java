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
 * @Title: ZKCoreEnvSourceConfiguration.java 
 * @author Vinson 
 * @Package com.zk.core.helper.configuration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 1:49:53 PM 
 * @version V1.0   
*/
package com.zk.core.helper.configuration.env;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/** 
* @ClassName: ZKCoreEnvSourceConfiguration 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@ConfigurationPropertiesScan
@PropertySources(value = { @PropertySource(value = {
        "classpath:env/env.test.source.properties" }, ignoreResourceNotFound = true, encoding = "UTF-8") })
public class ZKCoreEnvSourceConfiguration {

}
