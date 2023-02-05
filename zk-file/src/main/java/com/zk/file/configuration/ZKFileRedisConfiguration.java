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
* @Title: ZKFileRedisConfiguration.java 
* @author Vinson 
* @Package com.zk.file.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 21, 2022 11:45:22 AM 
* @version V1.0 
*/
package com.zk.file.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.alibaba.fastjson.JSONObject;
import com.zk.core.redis.ZKJedisOperatorByte;
import com.zk.core.redis.ZKJedisOperatorStringKey;
import com.zk.core.utils.ZKJsonUtils;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/** 
* @ClassName: ZKFileRedisConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Configuration
@AutoConfigureBefore(value = { ZKFileAfterConfiguration.class })
@PropertySource(encoding = "UTF-8", value = { "classpath:zk.file.redis.properties" })
public class ZKFileRedisConfiguration {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /*** jedis 整合 配置 ***/
    @Value("${zk.file.cache.redis.host:127.0.0.1}")
    String host;

    @Value("${zk.file.cache.redis.port:6379}")
    int port;

    @Value("${zk.file.cache.redis.password}")
    String password;

    @Value("${zk.file.cache.redis.timeout:3000}")
    int timeout;

    @Bean
    @ConfigurationProperties(prefix = "zk.file.redis.pool")
    public JedisPoolConfig jedisPoolConfig() {
        return new JedisPoolConfig();
    }

    /** 模式一：单机模式 */
    @Bean
    @ConditionalOnBean(name = "jedisPoolConfig")
    public JedisPool jedisPool(JedisPoolConfig jedisPoolConfig) {

        System.out.println("[^_^:20220621-1255-001] ====================================================");
        System.out.println(String.format("[^_^:20220621-1255-001] Redis 链接 hosr:%s, port:%s, password:%s", host, port, password));
        System.out.println("[^_^:20220621-1255-001] Redis 链接 JedisPoolConfig: " + JSONObject.toJSONString(jedisPoolConfig));
        System.out.println("[^_^:20220621-1255-001] ====================================================");

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
