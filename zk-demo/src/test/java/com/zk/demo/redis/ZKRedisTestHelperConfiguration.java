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
* @Title: ZKRedisTestHelperConfiguration.java 
* @author Vinson 
* @Package com.zk.demo.redis 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 9, 2021 11:55:26 AM 
* @version V1.0 
*/
package com.zk.demo.redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zk.core.commons.ZKJsonObjectMapper;
import com.zk.core.utils.ZKJsonUtils;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/** 
* @ClassName: ZKRedisTestHelperConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@PropertySources(value = {
        @PropertySource(value = { "classpath:redis/test_redis.properties" }, encoding = "UTF-8") })
public class ZKRedisTestHelperConfiguration {

    private static Logger logger = LogManager.getLogger(ZKRedisTestHelperConfiguration.class);

    @Value("${zk.test.redis.host:127.0.0.1}")
    String host;

    @Value("${zk.test.redis.port:6379}")
    int port;

    @Value("${zk.test.redis.password}")
    String password;

    @Value("${zk.test.redis.timeout:3000}")
    int timeout;

    @Bean
    @ConfigurationProperties(prefix = "zk.test.redis.pool")
    public JedisPoolConfig jedisPoolConfig() {
        return new JedisPoolConfig();
    }

    /*** jedis 整合 配置 ***/
    /** 模式一：单机模式 */
    @Bean
    @ConditionalOnBean(name = "jedisPoolConfig")
    public JedisPool jedisPool(JedisPoolConfig jedisPoolConfig) {
        logger.info("[^_^:20210809-1255-001] hosr:{}, port:{}, password:{}", host, port, password);
        logger.info("[^_^:20210809-1255-001] JedisPoolConfig:{}", ZKJsonUtils.toJsonStr(jedisPoolConfig));

        /** 单机模式 */
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
        return jedisPool;
    }

//    /** 模式二：哨模式；需要 redis 做好主从配置 */
//    @Bean
//    @ConditionalOnBean(name = "jedisPoolConfig")
//    public JedisSentinelPool jedisPool(JedisPoolConfig jedisPoolConfig) {
//        logger.info("[^_^:20210809-1255-001] hosr:{}, port:{}, password:{}", host, port, password);
//        logger.info("[^_^:20210809-1255-001] JedisPoolConfig:{}", ZKJsonUtils.toJsonStr(jedisPoolConfig));
//        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool("zk_test",
//                ZKCollectionUtils.asSet(String.format("%s:%s", host, port)));
//        return jedisSentinelPool;
//    }

    /*******************************************************************************************************************************/
    /*** spring-data-redis 整合 配置 ***/

//    /* redis 集群模式 配置 JedisConnectionFactory */
//    public RedisClusterConfiguration redisClusterConfiguration() {
//        Collection<String> clusterNodes = ZKCollectionUtils.asList(String.format("%s:%s", host, port));
//        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusterNodes);
//        redisClusterConfiguration.setPassword(password);
//        return redisClusterConfiguration;
//    }
////    @Bean
////    @ConditionalOnBean(name = { "jedisPoolConfig", "redisClusterConfiguration" })
////    public JedisConnectionFactory jedisConnectionFactory(RedisClusterConfiguration redisClusterConfiguration,
////            JedisPoolConfig jedisPoolConfig) {
////        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration,
////                jedisPoolConfig);
////        return jedisConnectionFactory;
////    }
//
//    /* redis 哨兵模式配置；需要 redis 做好主从配置 */
//    @Bean
//    public RedisSentinelConfiguration redisSentinelConfiguration() {
//        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
////        redisSentinelConfiguration.sentinel(host, port);
//        RedisNode rn = new RedisNode(host, port);
////        redisSentinelConfiguration.sentinel(rn);
//        redisSentinelConfiguration.setSentinels(ZKCollectionUtils.asList(rn));
//        redisSentinelConfiguration.setDatabase(2);
//        redisSentinelConfiguration.setPassword(password);
//        return redisSentinelConfiguration;
//    }
//
//    @Bean
//    @ConditionalOnBean(name = { "jedisPoolConfig", "redisSentinelConfiguration" })
//    public JedisConnectionFactory jedisConnectionFactory(RedisSentinelConfiguration redisSentinelConfiguration,
//            JedisPoolConfig jedisPoolConfig) {
//        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisSentinelConfiguration,
//                jedisPoolConfig);
//        return jedisConnectionFactory;
//    }
//
    /* redis 单机模式 配置 */
    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(2);
        redisStandaloneConfiguration.setPassword(password);
        return redisStandaloneConfiguration;
    }

    @Bean
    @ConditionalOnBean(name = { "jedisPoolConfig", "redisStandaloneConfiguration" })
    public JedisConnectionFactory jedisConnectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration,
            JedisPoolConfig jedisPoolConfig) {
        // 获得默认的连接池构造器(怎么设计的，为什么不抽象出单独类，供用户使用呢)
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration
                .builder();
        // 指定jedisPoolConifig来修改默认的连接池构造器（真麻烦，滥用设计模式！）
        jpcb.poolConfig(jedisPoolConfig);
        // 通过构造器来构造jedis客户端配置
        JedisClientConfiguration jedisClientConfiguration = jpcb.build();
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration,
                jedisClientConfiguration);
        return jedisConnectionFactory;
    }

    @SuppressWarnings({ "deprecation", "removal" })
    @Bean
    @Primary
    @ConditionalOnBean(name = { "jedisConnectionFactory" })
    public RedisTemplate<String, ?> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, ?> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setEnableTransactionSupport(true);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer<Object> jacksonSeial = new Jackson2JsonRedisSerializer<Object>(Object.class);
//        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        ZKJsonObjectMapper om = new ZKJsonObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);

        // 设置 key 和value序列化模式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jacksonSeial);

        // 设置hash key 和value序列化模式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jacksonSeial);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////// ///////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Bean
    @ConfigurationProperties(prefix = "zk.test.redis.pool2")
    public JedisPoolConfig jedisPoolConfig2() {
        return new JedisPoolConfig();
    }

    @Bean
    @ConditionalOnBean(name = "jedisPoolConfig2")
    public JedisPool jedisPool2(JedisPoolConfig jedisPoolConfig2) {
        logger.info("[^_^:20210809-1255-001] hosr:{}, port:{}, password:{}", host, port, password);
        logger.info("[^_^:20210809-1255-001] JedisPoolConfig:{}", ZKJsonUtils.toJsonStr(jedisPoolConfig2));

        /** 单机模式 */
        JedisPool jedisPool = new JedisPool(jedisPoolConfig2, host, port, timeout, password);
        return jedisPool;
    }

    @Bean
    @ConditionalOnBean(name = { "jedisPoolConfig2", "redisStandaloneConfiguration" })
    public JedisConnectionFactory jedisConnectionFactory2(RedisStandaloneConfiguration redisStandaloneConfiguration,
            JedisPoolConfig jedisPoolConfig2) {
        // 获得默认的连接池构造器(怎么设计的，为什么不抽象出单独类，供用户使用呢)
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration
                .builder();
        // 指定jedisPoolConifig来修改默认的连接池构造器（真麻烦，滥用设计模式！）
        jpcb.poolConfig(jedisPoolConfig2);
        // 通过构造器来构造jedis客户端配置
        JedisClientConfiguration jedisClientConfiguration = jpcb.build();
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration,
                jedisClientConfiguration);
        return jedisConnectionFactory;
    }

    @SuppressWarnings({ "deprecation", "removal" })
    @Bean
    @ConditionalOnBean(name = { "jedisConnectionFactory2" })
    public RedisTemplate<String, ?> redisTemplate2(JedisConnectionFactory jedisConnectionFactory2) {
        RedisTemplate<String, ?> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory2);
        redisTemplate.setEnableTransactionSupport(true);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer<Object> jacksonSeial = new Jackson2JsonRedisSerializer<Object>(Object.class);
//        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        ZKJsonObjectMapper om = new ZKJsonObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);

        // 设置 key 和value序列化模式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jacksonSeial);

        // 设置hash key 和value序列化模式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jacksonSeial);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

}
