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
 * @Title: ZKBaseHelperCtx.java 
 * @author Vinson 
 * @Package com.zk.base.helper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 4:10:44 PM 
 * @version V1.0   
*/
package com.zk.base.helper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.zk.core.configuration.ZKEnableCoreServlet;
import com.zk.db.configuration.ZKDBProperties;
import com.zk.db.configuration.ZKEnableDB;

/** 
* @ClassName: ZKBaseHelperCtx 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
//@ComponentScan(
//        basePackages = { //
//                "com.zk.base.helper.entity", //
//                "com.zk.base.helper.controller", //
//                "com.zk.base.helper.dao", //
////                "com.zk.base.helper.mongo.*.*", //
//        }
////        excludeFilters = { //
//////                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.zk.base.helper.mongo.*.*")
////        }
//)
@ImportAutoConfiguration(classes = { //
        ZKBaseHelperMongoConfiguration.class, // 测试 mongo 时，需要引入这个配置
//        ZKDBConfiguration.class, // 
})
@SpringBootApplication(exclude = { //
        MongoRepositoriesAutoConfiguration.class, //
        DataSourceAutoConfiguration.class, //
        MongoAutoConfiguration.class, //
})
@PropertySources(value = { // 
        @PropertySource(ignoreResourceNotFound = true, encoding = "UTF-8", value = { "classpath:test.zk.base.db.properties" }), // 
})
@ZKEnableCoreServlet
@ZKEnableDB(configLocation = "classpath:mybatis/test_mybatis_config.xml", mapperLocations = {
        "classpath*:mappers/**/*.xml" })
public class ZKBaseHelperCtx {

    public static void main(String[] args) {
        System.out.println("[^_^:20190816-1008-001]=========================================================");
        System.out.println("[^_^:20190816-1008-001]=== zk base ZKBaseHelperCtx 启动 ... ...");
        System.out.println("[^_^:20190816-1008-001]=========================================================");
        run(args);
        System.out.println("[^_^:20190816-1008-001]---------------------------------------------------------");
    }

    private static ConfigurableApplicationContext ctx;

    public static ConfigurableApplicationContext run(String[] args) {

        if (ctx == null) {
            SpringApplicationBuilder appCtxBuilder = new SpringApplicationBuilder(ZKBaseHelperCtx.class);
            appCtxBuilder = appCtxBuilder.web(WebApplicationType.NONE);
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
        return new ZKDBProperties();
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("msg/test_msg");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setCacheSeconds(60);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
