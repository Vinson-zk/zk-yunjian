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
* @Title: ZKSecTestHelperRedisConfiguration.java 
* @author Vinson 
* @Package com.zk.security.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 17, 2021 10:54:58 AM 
* @version V1.0 
*/
package com.zk.security.helper;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.zk.cache.redis.ZKRedisCacheManager;
import com.zk.core.redis.ZKJedisOperatorStringKey;
import com.zk.core.redis.configuration.ZKEnableRedis;
import com.zk.core.redis.configuration.ZKRedisProperties;
import com.zk.security.ticket.support.redis.ZKSecRedisTicketManager;

/** 
* @ClassName: ZKSecTestHelperRedisConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Configuration
@PropertySources(value = {
        @PropertySource(value = { "classpath:test.zk.sec.redis.properties" }, encoding = "UTF-8") })
@AutoConfigureAfter(value = { ZKEnableRedis.class })
public class ZKSecTestHelperRedisConfiguration {

//    private static Logger logger = LogManager.getLogger(ZKSecTestHelperRedisConfiguration.class);

    @Bean("redisProperties")
    @ConfigurationProperties(prefix = "zk.sec.redis")
    public ZKRedisProperties redisProperties() {
        return new ZKRedisProperties();
    }

    @Primary
    @Bean
    public ZKSecRedisTicketManager zkSecRedisTicketManager(ZKJedisOperatorStringKey jedisOperatorStringKey) {
        return new ZKSecRedisTicketManager(jedisOperatorStringKey);
    }

    @Primary
    @Bean
    public ZKRedisCacheManager redisCacheManager(ZKJedisOperatorStringKey jedisOperatorStringKey) {
        ZKRedisCacheManager cm = new ZKRedisCacheManager();
        cm.setJedisOperator(jedisOperatorStringKey);
        return cm;
    }

}
