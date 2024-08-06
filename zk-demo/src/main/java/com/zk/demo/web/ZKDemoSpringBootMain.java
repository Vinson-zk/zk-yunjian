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
 * @Title: ZKDemoSpringBootMain.java 
 * @author Vinson 
 * @Package com.zk.demo 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 4:50:28 PM 
 * @version V1.0   
*/
package com.zk.demo.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zk.demo.web.configuration.ZKDemoConfigutation;

/** 
* @ClassName: ZKDemoSpringBootMain 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@SpringBootApplication(exclude = { //
        DataSourceAutoConfiguration.class, //
        TransactionAutoConfiguration.class, //
})
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan( //
        basePackages = { "com.zk.demo.web.*" }, //
        excludeFilters = { @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.zk.demo.web.context.*") } //
)
@Import(value = { ZKDemoConfigutation.class, ServletWebServerFactoryAutoConfiguration.class })
public class ZKDemoSpringBootMain {

    protected static Logger log = LogManager.getLogger(ZKDemoSpringBootMain.class);

    public static void main(String[] args) {
        try {
            log.info("[^_^:20190628-1708-001]========================================");
            log.info("[^_^:20190628-1708-001]=== zk Demo  启动 ... ... ");
            log.info("[^_^:20190628-1708-001]========================================");
            run(args);
            log.info("[^_^:20190628-1708-001]----------------------------------------");

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    static ConfigurableApplicationContext ctx;
    public static ConfigurableApplicationContext run(String[] args) {
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder();
        springApplicationBuilder = springApplicationBuilder.sources(ZKDemoSpringBootMain.class);

        /*** 修改默认的配置文件名称和路径 ***/
        // 配置文件路径；默认：[file:./config/, file:./, classpath:/config/, classpath:/]
        springApplicationBuilder = springApplicationBuilder.properties("spring.config.location=classpath:/");
        // 定义配置文件名；默认：applicaiton；添加下面这一行后，不会读取 properties/yml 配置文件；多个时用英文逗号","
        // 隔；
        springApplicationBuilder = springApplicationBuilder
                .properties("spring.config.name=zk.demo,eureka/zk.demo.eureka.client");

        SpringApplication springApplication = springApplicationBuilder.build();
        springApplication.setWebApplicationType(WebApplicationType.NONE);

        ctx = springApplication.run(args);
        return ctx;
    }

    public static int exit() {
        return SpringApplication.exit(ctx);
    }

    /**************************************** */
    /**************************************** */
    /**************************************** */

}
