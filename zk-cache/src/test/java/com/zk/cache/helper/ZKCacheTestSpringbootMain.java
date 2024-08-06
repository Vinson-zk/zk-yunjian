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
* @Title: ZKCacheTestSpringbootMain.java 
* @author Vinson 
* @Package com.zk.cache.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 7, 2021 8:42:40 AM 
* @version V1.0 
*/
package com.zk.cache.helper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.zk.cache.memory.ZKMemoryCacheManager;
import com.zk.cache.mongo.ZKMongoCacheManager;
import com.zk.cache.redis.ZKRedisCacheManager;
import com.zk.core.redis.ZKJedisOperatorStringKey;
import com.zk.core.redis.configuration.ZKEnableRedis;
import com.zk.core.redis.configuration.ZKRedisProperties;
import com.zk.mongo.configuration.ZKEnableMongo;
import com.zk.mongo.configuration.ZKMongoProperties;

/** 
* @ClassName: ZKCacheTestSpringbootMain 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, MongoAutoConfiguration.class })
@PropertySources(value = { @PropertySource(encoding = "UTF-8", value = { "classpath:test_redis.properties",
        "classpath:test_mongodb.properties" }) })
//@ImportAutoConfiguration(classes = { ZKSecTestHelperMongoConfiguration.class, 
//        ZKSecTestHelperConfigurationBefore.class, ZKSecTestHelperConfigurationAfter.class })
//@ZKEnableCoreServlet
@ZKEnableRedis
@ZKEnableMongo
public class ZKCacheTestSpringbootMain {

    public static void main(String[] args) {
        System.out.println("[^_^:20210807-1247-001]=========================================================");
        System.out.println("[^_^:20210807-1247-001]=== zk Cache ZKCacheTestSpringbootMain 启动 ... ...");
        System.out.println("[^_^:20210807-1247-001]=========================================================");
        run(args);
        System.out.println("[^_^:20210807-1247-001]---------------------------------------------------------");
    }

    private static ConfigurableApplicationContext ctx;

    public static ConfigurableApplicationContext run(String[] args) {

        if (ctx == null) {
            SpringApplicationBuilder appCtxBuilder = new SpringApplicationBuilder(ZKCacheTestSpringbootMain.class);

            /*** 修改默认的配置文件名称和路径 ***/
            // 配置文件路径；默认：[file:./config/, file:./, classpath:/config/,
            // classpath:/]
//            appCtxBuilder = appCtxBuilder.properties("spring.config.location=classpath:/eureka/");
            // 定义配置文件名；默认：applicaiton；添加下面这一行后，不会读取 application.properties/yml
            // 配置文件；多个时，用英文逗号分隔；
            appCtxBuilder = appCtxBuilder.properties("spring.config.name=test_cache");

            appCtxBuilder = appCtxBuilder.web(WebApplicationType.NONE);

            SpringApplication app = appCtxBuilder.build();

            ctx = app.run(args);
        }
        return ctx;
    }

    public static void exit() {
        SpringApplication.exit(ctx);
    }

    @Bean
    public ZKMemoryCacheManager memoryCacheManager() {
        return new ZKMemoryCacheManager();
    }

    @Bean("redisProperties")
    @ConfigurationProperties(prefix = "zk.cache.redis")
    public ZKRedisProperties redisProperties() {
        return new ZKRedisProperties();
    }

    @Primary
    @Bean
    public ZKRedisCacheManager redisCacheManager(ZKJedisOperatorStringKey jedisOperatorStringKey) {
        ZKRedisCacheManager cm = new ZKRedisCacheManager();
        cm.setJedisOperator(jedisOperatorStringKey);
        return cm;
    }

    @Bean
    @ConfigurationProperties(prefix = "zk.cache.mongodb")
    public ZKMongoProperties mongoProperties() {
        return new ZKMongoProperties();
    }

    @Bean
    public ZKMongoCacheManager mongoCacheManager(MongoTemplate mongoTemplate) {
        ZKMongoCacheManager mcm = new ZKMongoCacheManager(mongoTemplate);
        return mcm;
    }



}
