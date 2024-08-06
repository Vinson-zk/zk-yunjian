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
* @Title: ZKSerCenAfterConfiguration.java 
* @author Vinson 
* @Package com.zk.server.central.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 2, 2023 4:43:31 PM 
* @version V1.0 
*/
package com.zk.server.central.configuration;

import org.springframework.beans.factory.annotation.Autowired;

import com.zk.core.commons.ZKEnvironment;
import com.zk.core.configuration.ZKEnableCoreServlet;
import com.zk.core.redis.configuration.ZKEnableRedis;
import com.zk.db.configuration.ZKEnableDB;
import com.zk.log.configuration.ZKEnableLog;
import com.zk.security.configuration.ZKEnableSecurity;

/** 
* @ClassName: ZKSerCenAfterConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKEnableCoreServlet
@ZKEnableRedis
@ZKEnableLog
@ZKEnableSecurity
@ZKEnableDB(configLocation = "classpath:xmlConfig/mybatis/sc_mybatis_config.xml")
public class ZKSerCenAfterConfiguration {

    public ZKSerCenAfterConfiguration() {
        System.out.println("[^_^:20230211-1022-001] ----- 实例化 -----" + this.getClass().getSimpleName());
    }

    @Autowired
    public void ZKSerCenInit(ZKEnvironment zkEnv) {
        System.out.println("[^_^:20191219-2154-001] === ZKSerCenInit [" + this.getClass().getSimpleName() + "] " + this.hashCode());
        System.out.println("[^_^:20191219-2154-001] -- spring.mvc.view.prefix:     " + zkEnv.getString("spring.mvc.view.prefix"));
        System.out.println("[^_^:20191219-2154-001] -- spring.freemarker.prefix:   " + zkEnv.getString("spring.freemarker.prefix"));
        System.out.println("[^_^:20191219-2154-001] --- ZKSerCenInit [" + this.getClass().getSimpleName() + "] " + this.hashCode());
    }

}
