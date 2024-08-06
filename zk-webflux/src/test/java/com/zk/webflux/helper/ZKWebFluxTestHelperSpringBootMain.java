/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKWebFluxTestHelperSpringBootMain.java 
* @author Vinson 
* @Package com.zk.webflux.helper.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 22, 2024 11:28:20 AM 
* @version V1.0 
*/
package com.zk.webflux.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.config.EnableWebFlux;

import com.zk.webflux.configuration.ZKEnableWebFluxFileUpload;

/**
 * @ClassName: ZKWebFluxTestHelperSpringBootMain
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@SpringBootApplication(exclude = { //
        DataSourceAutoConfiguration.class, //
        TransactionAutoConfiguration.class, //
//        HibernateJpaAutoConfiguration.class, //
//        MongoAutoConfiguration.class, // 
        WebMvcAutoConfiguration.class, //
//        WebFluxAutoConfiguration.class, //
        ServletWebServerFactoryAutoConfiguration.class, //
})
@EnableWebFlux
@EnableTransactionManagement(proxyTargetClass = true)
@ImportAutoConfiguration(value = { ZKWebfluxParentConfiguration.class,
        ZKWebfluxChildConfiguration.class })
@ZKEnableWebFluxFileUpload
public class ZKWebFluxTestHelperSpringBootMain {

    protected static Logger log = LogManager.getLogger(ZKWebFluxTestHelperSpringBootMain.class);

    public static void main(String[] args) {
        try {
            log.info("[^_^:20240123-0009-001]========================================");
            log.info("[^_^:20240123-0009-001]=== zk ZKWebFluxTestHelperSpringBootMain 启动 ... ... ");
            log.info("[^_^:20240123-0009-001]========================================");
            run(args);
            log.info("[^_^:20240123-0009-001]----------------------------------------");

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static ConfigurableApplicationContext ctx;

    // 启动
    public static ConfigurableApplicationContext run(String[] args) {
        if (ctx == null) {
            SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder();
            springApplicationBuilder = springApplicationBuilder.sources(ZKWebFluxTestHelperSpringBootMain.class);

            /*** 修改默认的配置文件名称和路径 ***/
            // 配置文件路径；默认：[file:./config/, file:./, classpath:/config/, classpath:/]
            springApplicationBuilder = springApplicationBuilder.properties("spring.config.location=classpath:/");

            // 定义配置文件名；默认：applicaiton；添加下面这一行后，不会读取 properties/yml 配置文件；多个时用英文逗号","
            // 隔；
            springApplicationBuilder = springApplicationBuilder.properties("spring.config.name=test.zk.webflux");

            SpringApplication springApplication = springApplicationBuilder.build();
            springApplication.setWebApplicationType(WebApplicationType.REACTIVE);

            ctx = springApplication.run(args);
        }
        return ctx;
    }

    public static ConfigurableApplicationContext getCtx() {
        if (ctx == null) {
            ctx = run(new String[] {});
        }
        return ctx;
    }

    // 退出
    public static void exit() {
        if (ctx != null) {
            SpringApplication.exit(ctx);
            ctx = null;
        }
    }
    public static int exit(ConfigurableApplicationContext ctx) {
        return SpringApplication.exit(ctx);
    }

    @Bean
    public TestRestTemplate testRestTemplate(HttpMessageConverters messageConverters) {
        TestRestTemplate trt = new TestRestTemplate();
        trt.getRestTemplate().setMessageConverters(messageConverters.getConverters());
        return trt;
    }

}
