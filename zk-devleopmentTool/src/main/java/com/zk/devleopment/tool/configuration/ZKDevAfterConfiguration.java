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
* @Title: ZKDevAfterConfiguration.java 
* @author Vinson 
* @Package com.zk.devleopment.tool.gen.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 14, 2022 6:56:25 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.configuration;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zk.core.configuration.ZKEnableCoreServlet;
import com.zk.core.redis.configuration.ZKEnableRedis;
import com.zk.db.configuration.ZKEnableDB;
import com.zk.log.configuration.ZKEnableLog;
import com.zk.security.configuration.ZKEnableSecurity;
import com.zk.webmvc.configuration.ZKEnableWebmvc;

/** 
* @ClassName: ZKDevAfterConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKEnableCoreServlet
@ZKEnableLog
@ZKEnableDB
@ZKEnableSecurity
@ZKEnableWebmvc
@ZKEnableRedis
public class ZKDevAfterConfiguration implements WebMvcConfigurer {



}
