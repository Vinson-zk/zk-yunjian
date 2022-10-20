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
* @Title: ZKCoreRedisConfiguration.java 
* @author Vinson 
* @Package com.zk.core.helper.configuration.redis 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 12, 2021 6:02:59 PM 
* @version V1.0 
*/
package com.zk.core.helper.configuration.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.zk.core.redis.ZKJedisOperatorByte;
import com.zk.core.redis.ZKJedisOperatorStringKey;
import com.zk.core.utils.ZKJsonUtils;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/** 
* @ClassName: ZKCoreRedisConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@PropertySources(value = { @PropertySource(value = { "classpath:redis/test_redis.properties" }, encoding = "UTF-8") })
public class ZKCoreRedisConfiguration {

    private static Logger logger = LoggerFactory.getLogger(ZKCoreRedisConfiguration.class);

    /*** jedis 整合 配置 ***/
    @Value("${zk.core.redis.host:127.0.0.1}")
    String host;

    @Value("${zk.core.redis.port:6379}")
    int port;

    @Value("${zk.core.redis.password}")
    String password;

    @Value("${zk.core.redis.timeout:3000}")
    int timeout;

    @Bean
    @ConfigurationProperties(prefix = "zk.core.redis.pool")
    public JedisPoolConfig jedisPoolConfig() {
        return new JedisPoolConfig();
    }

    /** 模式一：单机模式 */
    @Bean
    @ConditionalOnBean(name = "jedisPoolConfig")
    public JedisPool jedisPool(JedisPoolConfig jedisPoolConfig) {
        logger.info("[^_^:20210809-1255-001] hosr:{}, port:{}, password:{}", host, port, password);
        logger.info("[^_^:20210809-1255-001] JedisPoolConfig:{}", ZKJsonUtils.writeObjectJson(jedisPoolConfig));

        /** 单机模式 */
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
        return jedisPool;
    }

    @Bean
    @ConditionalOnBean(name = "jedisPool")
    public ZKJedisOperatorByte jedisOperatorByte(JedisPool jedisPool) {
        ZKJedisOperatorByte jedisOperatorByte = new ZKJedisOperatorByte(jedisPool);
        return jedisOperatorByte;
    }

    @Bean
    @ConditionalOnBean(name = "jedisOperatorByte")
    public ZKJedisOperatorStringKey jedisOperatorStringKey(ZKJedisOperatorByte jedisOperatorByte) {
        ZKJedisOperatorStringKey jedisOperatorStringKey = new ZKJedisOperatorStringKey();
        jedisOperatorStringKey.setJedisOperatorByte(jedisOperatorByte);
        return jedisOperatorStringKey;
    }

}
