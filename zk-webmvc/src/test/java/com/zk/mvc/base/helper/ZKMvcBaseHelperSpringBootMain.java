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
 * @Title: ZKMvcBaseHelperSpringBootMain.java 
 * @author Vinson 
 * @Package com.zk.mvc.basehelper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 4:10:44 PM 
 * @version V1.0   
*/
package com.zk.mvc.base.helper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.zk.core.configuration.ZKEnableCoreServlet;
import com.zk.db.configuration.ZKDBProperties;
import com.zk.db.configuration.ZKEnableDB;
import com.zk.webmvc.configuration.ZKEnableWebmvc;

/**
 * @ClassName: ZKMvcBaseHelperSpringBootMain
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
@SpringBootApplication(exclude = {
        MongoRepositoriesAutoConfiguration.class, //
        SecurityAutoConfiguration.class, //
        DataSourceAutoConfiguration.class, //
        MongoAutoConfiguration.class, //
})
@PropertySources(value = { //
        @PropertySource(ignoreResourceNotFound = true, encoding = "UTF-8", value = { "classpath:test.zk.base.db.properties" }), // 
})
@ImportAutoConfiguration(classes = { //
//        ZKBaseHelperMongoConfiguration.class, // 测试 mongo 时，需要引入这个配置
})
@ComponentScan(
        basePackages = {
                "com.zk.base.helper.entity", "com.zk.mvc.base.helper.controller",
                "com.zk.base.helper.dao"
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.zk.base.helper.mongo.*.*")
        }
)
@ZKEnableCoreServlet
@ZKEnableDB(configLocation = "classpath:mybatis/test_mybatis_config.xml")
@ZKEnableWebmvc
public class ZKMvcBaseHelperSpringBootMain {

    public static void main(String[] args) {
        System.out.println("[^_^:20190816-1008-001]=========================================================");
        System.out.println("[^_^:20190816-1008-001]=== zk base ZKMvcBaseHelperSpringBootMain 启动 ... ...");
        System.out.println("[^_^:20190816-1008-001]=========================================================");
        run(args);
        System.out.println("[^_^:20190816-1008-001]---------------------------------------------------------");
    }

    private static ConfigurableApplicationContext ctx;

    public static ConfigurableApplicationContext run(String[] args) {

        if (ctx == null) {
            SpringApplicationBuilder appCtxBuilder = new SpringApplicationBuilder(ZKMvcBaseHelperSpringBootMain.class);

            /*** 修改默认的配置文件名称和路径 ***/
            // 配置文件路径；默认：[file:./config/, file:./, classpath:/config/,
            // classpath:/]
//            appCtxBuilder = appCtxBuilder.properties("spring.config.location=classpath:/eureka/");
            // 定义配置文件名；默认：applicaiton；添加下面这一行后，不会读取 application.properties/yml
            // 配置文件；多个时，用英文逗号分隔；
//            appCtxBuilder = appCtxBuilder.properties("spring.config.name=");

            appCtxBuilder = appCtxBuilder.web(WebApplicationType.SERVLET);
            SpringApplication app = appCtxBuilder.build();
            ctx = app.run(args);
        }
        return ctx;
    }

    public static void exit() {
        SpringApplication.exit(ctx);
    }

    @Bean("zkDBProperties")
    @ConfigurationProperties(prefix = "zk.base.dynamic.jdbc")
    public ZKDBProperties zkDBProperties() {
        System.out.println(" ----- zk-webmvc ZKDBProperties zk.base.dynamic.jdbc ");
        return new ZKDBProperties();
    }

//    @Bean("redisProperties")
//    @ConfigurationProperties(prefix = "zk.ser.cen.redis")
//    public ZKRedisProperties redisProperties() {
//        return new ZKRedisProperties();
//    }

}
