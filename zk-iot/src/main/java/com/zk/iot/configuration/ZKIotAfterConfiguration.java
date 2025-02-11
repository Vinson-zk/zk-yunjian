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
* @Title: ZKIotAfterConfiguration.java 
* @author Vinson 
* @Package com.zk.iot.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Dec 31, 2024 11:30:21 AM 
* @version V1.0 
*/
package com.zk.iot.configuration;

import org.springframework.beans.factory.annotation.Autowired;

import com.zk.core.commons.ZKEnvironment;
import com.zk.core.configuration.ZKEnableCoreReactive;
import com.zk.core.redis.configuration.ZKEnableRedis;
import com.zk.db.configuration.ZKEnableDB;
import com.zk.log.configuration.ZKEnableLog;
import com.zk.security.configuration.ZKEnableSecurity;
import com.zk.webflux.configuration.ZKEnableWebFluxFileUpload;

/** 
* @ClassName: ZKIotAfterConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKEnableCoreReactive
@ZKEnableLog
@ZKEnableDB
@ZKEnableRedis
@ZKEnableSecurity
@ZKEnableWebFluxFileUpload
public class ZKIotAfterConfiguration {

    @Autowired
    public void before(ZKEnvironment zkEnvironment) {
        System.out.println(
                "[^_^:20241231-1734-001] === [" + ZKIotAfterConfiguration.class.getSimpleName() + "] " + this);
        System.out.println(
                "[^_^:20241231-1734-001] --- iot defaultLocale: " + zkEnvironment.getString("zk.default.locale"));

        System.out.println(
                "[^_^:20241231-1734-001] --- [" + ZKIotAfterConfiguration.class.getSimpleName() + "] " + this);
    }

}
