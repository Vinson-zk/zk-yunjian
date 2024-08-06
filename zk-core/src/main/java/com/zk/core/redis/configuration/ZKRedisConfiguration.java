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
* @Title: ZKRedisConfiguration.java 
* @author Vinson 
* @Package com.zk.core.redis.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 10, 2023 1:39:49 PM 
* @version V1.0 
*/
package com.zk.core.redis.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import com.zk.core.exception.base.ZKUnknownException;
import com.zk.core.redis.ZKJedisOperatorByte;
import com.zk.core.redis.ZKJedisOperatorStringKey;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/** 
* @ClassName: ZKRedisConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKRedisConfiguration {

    @ConditionalOnMissingBean(value = JedisPoolConfig.class)
    @Bean
    public JedisPoolConfig jedisPoolConfig(ZKRedisProperties redisProperties) {
        System.out.println(ZKEnableRedis.printLog + "jedis pool info [" + this.getClass().getSimpleName() + ":" + this.hashCode() 
                + "] =================================");
        
        System.out.println(ZKEnableRedis.printLog + "jedis pool info " + String.format("host:%s, port:%s, database:%s",
                redisProperties.getHost(), redisProperties.getPort(), redisProperties.getDatabase()));
//        System.out.println(ZKEnableRedis.printLog + " jedis pool info " + JSONObject.toJSONString(redisProperties));
        
        System.out.println(ZKEnableRedis.printLog + "jedis pool info [" + this.getClass().getSimpleName() + ":" + this.hashCode() 
                + "] ---------------------------------");

        if (redisProperties.getJedisPool() == null) {
            throw new ZKUnknownException("[>_<:20230212-2001-001] JedisPool in ZKRedisProperties cannot be null");
        }
        return redisProperties.getJedisPool();
    }

    /** 模式一：单机模式 */
    @ConditionalOnMissingBean(value = JedisPool.class)
    @Bean
    public JedisPool jedisPool(JedisPoolConfig jedisPoolConfig, ZKRedisProperties redisProperties) {
        System.out.println(
                ZKEnableRedis.printLog + "jedisPool --- [" + this.getClass().getSimpleName() + "] " + this.hashCode());
        /** 单机模式 */
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, redisProperties.getHost(), redisProperties.getPort(),
                redisProperties.getTimeout(), redisProperties.getPassword(), redisProperties.getDatabase());
        
//        ZKRedisUtils.printJedisPoolInfo(this.getClass().getName(), jedisPool);

        return jedisPool;
    }

    @ConditionalOnMissingBean(value = ZKJedisOperatorByte.class)
    @Bean
    public ZKJedisOperatorByte jedisOperatorByte(JedisPool jedisPool) {
        System.out.println(ZKEnableRedis.printLog + "jedisOperatorByte --- [" + this.getClass().getSimpleName() + "] "
                + this.hashCode());
        ZKJedisOperatorByte jedisOperatorByte = new ZKJedisOperatorByte(jedisPool);
        return jedisOperatorByte;
    }

    @ConditionalOnMissingBean(value = ZKJedisOperatorStringKey.class)
    @Bean
    public ZKJedisOperatorStringKey jedisOperatorStringKey(ZKJedisOperatorByte jedisOperatorByte) {
        System.out.println(ZKEnableRedis.printLog + "jedisOperatorStringKey --- [" + this.getClass().getSimpleName()
                + "] " + this.hashCode());
        ZKJedisOperatorStringKey jedisOperatorStringKey = new ZKJedisOperatorStringKey();
        jedisOperatorStringKey.setJedisOperatorByte(jedisOperatorByte);
        return jedisOperatorStringKey;
    }
}
