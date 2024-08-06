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
* @Title: ZKMailAfterConfiguration.java 
* @author Vinson 
* @Package com.zk.mail.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date May 25, 2022 2:21:20 AM 
* @version V1.0 
*/
package com.zk.mail.configuration;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zk.core.configuration.ZKEnableCoreServlet;
import com.zk.core.redis.configuration.ZKEnableRedis;
import com.zk.db.configuration.ZKEnableDB;
import com.zk.log.configuration.ZKEnableLog;
import com.zk.security.configuration.ZKEnableSecurity;
import com.zk.webmvc.configuration.ZKEnableWebmvc;

/** 
* @ClassName: ZKMailAfterConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKEnableCoreServlet
@ZKEnableLog
@ZKEnableDB
@ZKEnableRedis
@ZKEnableSecurity
@ZKEnableWebmvc
public class ZKMailAfterConfiguration implements WebMvcConfigurer {




}
