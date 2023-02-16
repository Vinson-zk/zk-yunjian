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
 * @Title: ZKCoreEnvParentConfiguration.java 
 * @author Vinson 
 * @Package com.zk.core.helper.configuration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 1:49:26 PM 
 * @version V1.0   
*/
package com.zk.core.helper.configuration.env;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/** 
* @ClassName: ZKCoreEnvParentConfiguration 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@ConfigurationPropertiesScan
@PropertySources(value = { // 
        @PropertySource(ignoreResourceNotFound = true, encoding = "UTF-8", value = {"classpath:env/env.test.parent.properties" }), // 
})
public class ZKCoreEnvParentConfiguration {

}
