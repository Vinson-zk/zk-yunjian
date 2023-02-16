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
* @Title: ZKSysAfterConfiguration.java 
* @author Vinson 
* @Package com.zk.sys.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 13, 2023 11:14:47 AM 
* @version V1.0 
*/
package com.zk.sys.configuration;

import org.springframework.context.annotation.Bean;

import com.zk.cache.redis.ZKRedisCacheManager;
import com.zk.core.configuration.ZKEnableCore;
import com.zk.core.redis.ZKJedisOperatorStringKey;
import com.zk.core.redis.configuration.ZKEnableRedis;
import com.zk.db.configuration.ZKEnableDB;
import com.zk.log.configuration.ZKEnableLog;

/** 
* @ClassName: ZKSysAfterConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKEnableCore
@ZKEnableLog
@ZKEnableRedis
@ZKEnableDB
public class ZKSysAfterConfiguration {

    /**
     * 缓存管理
     *
     * @Title: redisCacheManager
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 11, 2022 7:17:31 PM
     * @param jedisOperatorStringKey
     * @return
     * @return ZKRedisCacheManager
     */
    @Bean
    public ZKRedisCacheManager redisCacheManager(ZKJedisOperatorStringKey jedisOperatorStringKey) {
        ZKRedisCacheManager cm = new ZKRedisCacheManager();
        cm.setJedisOperator(jedisOperatorStringKey);
        return cm;
    }

}
