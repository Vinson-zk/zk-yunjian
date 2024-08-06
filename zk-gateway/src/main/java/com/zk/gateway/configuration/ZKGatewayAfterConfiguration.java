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
* @Title: ZKGatewayAfterConfiguration.java 
* @author Vinson 
* @Package com.zk.gateway.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 14, 2022 7:06:51 PM 
* @version V1.0 
*/
package com.zk.gateway.configuration;

import com.zk.core.configuration.ZKEnableCoreReactive;
import com.zk.core.redis.configuration.ZKEnableRedis;
import com.zk.db.configuration.ZKEnableDB;
import com.zk.log.configuration.ZKEnableLog;
import com.zk.security.configuration.ZKEnableSecurity;

/**
 * @ClassName: ZKGatewayAfterConfiguration
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@ZKEnableCoreReactive
@ZKEnableLog
@ZKEnableDB
@ZKEnableRedis
@ZKEnableSecurity
//@EnableWebFlux
//@ImportAutoConfiguration(value = { //
//        WebFluxAutoConfiguration.class, //
//        WebSocketReactiveAutoConfiguration.class, //
//        GatewayAutoConfiguration.class, //
//        ServletWebServerFactoryAutoConfiguration.class, //
//})
//@Enable
public class ZKGatewayAfterConfiguration {


}
