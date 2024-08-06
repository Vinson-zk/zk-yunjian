/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKMongoAutoConfigurationTest.java 
 * @author Vinson 
 * @Package com.zk.mongo.helper.configuration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:11:50 PM 
 * @version V1.0   
*/
package com.zk.mongo.configuration;

import org.junit.Test;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.zk.core.lock.ZKDistributedLock;
import com.zk.mongo.lock.ZKMongoDistributedLockImpl;

import junit.framework.TestCase;

/** 
* @ClassName: ZKMongoAutoConfigurationTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@SpringBootApplication(exclude = { //
        MongoAutoConfiguration.class, //
})
@AutoConfigureBefore(value = { ZKEnableMongo.class })
@ZKEnableMongo
public class ZKMongoAutoConfigurationTest {

    protected static Logger log = LogManager.getLogger(ZKMongoAutoConfigurationTest.class);

    public static void main(String[] args) {
        log.info("[^_^:20190822-1554-001]============================================");
        log.info("[^_^:20190822-1554-001]=== Spring Boot zk-mongo ZKMongoConfigurationTest DoMain  启动 ... ... ");
        log.info("[^_^:20190822-1554-001]============================================");
        run(args);
        log.info("[^_^:20190822-1554-001]--------------------------------------------");
    }

    public static ConfigurableApplicationContext run(String[] args) {

        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(
                ZKMongoAutoConfigurationTest.class);
//        springApplicationBuilder = springApplicationBuilder.sources(ZKMongoConfigurationTest.class);
        // 下面两行可达到同一效果
//        springApplicationBuilder = springApplicationBuilder.profiles("springApplicationBuilder");
//        springApplicationBuilder = springApplicationBuilder.properties("spring.profiles.active=springApplicationBuilder");

        /*** 修改默认的配置文件名称和路径 ***/
        // 配置文件路径；默认：[file:./config/, file:./, classpath:/config/, classpath:/]
//        springApplicationBuilder = springApplicationBuilder.properties("spring.config.location=classpath:/");
        // 定义配置文件名；默认：applicaiton；添加下面这一行后，不会读取 properties/yml 配置文件；多个时用英文逗号","
        // 隔；
//        springApplicationBuilder = springApplicationBuilder.properties("spring.config.name=application,other");

        springApplicationBuilder = springApplicationBuilder.properties("spring.config.location=classpath:/");
        springApplicationBuilder = springApplicationBuilder.properties("spring.config.name=test_mongodb");

        SpringApplication springApplication = springApplicationBuilder.build();
        springApplication.setWebApplicationType(WebApplicationType.NONE);

        return springApplication.run(args);
    }

    @Bean
    @ConfigurationProperties(prefix = "zk.mongodb")
    public ZKMongoProperties mongoProperties() {
        return new ZKMongoProperties();
    }

    @Bean
    public ZKDistributedLock distributedLock(MongoTemplate mongoTemplate) {
        return new ZKMongoDistributedLockImpl(mongoTemplate);
    }

    @Test
    public void test() {
        try {
            ConfigurableApplicationContext ctx = ZKMongoAutoConfigurationTest.run(new String[0]);
            TestCase.assertNotNull(ctx);

            MongoTemplate mongoTemplate = ctx.getBean(MongoTemplate.class);
            TestCase.assertNotNull(mongoTemplate);

            MongoClient mongoClient = ctx.getBean(MongoClient.class);
            System.out.println("----- 1: " + mongoClient.getClusterDescription().getShortDescription());
            System.out.println("----- 2: " + mongoClient.getClusterDescription().getServerDescriptions().get(0).getAddress());
            TestCase.assertEquals("127.0.0.1", mongoClient.getClusterDescription().getServerDescriptions().get(0).getAddress().getHost());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
