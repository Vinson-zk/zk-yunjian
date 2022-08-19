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
* @Title: ZKWechatRedisConfiguration.java 
* @author Vinson 
* @Package com.zk.wechat.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Nov 7, 2021 3:09:15 PM 
* @version V1.0 
*/
package com.zk.wechat.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import com.zk.core.redis.ZKJedisOperatorByte;
import com.zk.core.redis.ZKJedisOperatorStringKey;
import com.zk.core.utils.ZKJsonUtils;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/** 
* @ClassName: ZKWechatRedisConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@PropertySource(encoding = "UTF-8", value = { "classpath:zk.wechat.redis.properties" })
public class ZKWechatRedisConfiguration {

    private static Logger logger = LoggerFactory.getLogger(ZKWechatRedisConfiguration.class);

    /*** jedis 整合 配置 ***/
    @Value("${zk.wechat.cache.redis.host:127.0.0.1}")
    String host;

    @Value("${zk.wechat.cache.redis.port:6379}")
    int port;

    @Value("${zk.wechat.cache.redis.password}")
    String password;

    @Value("${zk.wechat.cache.redis.timeout:3000}")
    int timeout;

    @Bean
    @ConfigurationProperties(prefix = "zk.wechat.redis.pool")
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
