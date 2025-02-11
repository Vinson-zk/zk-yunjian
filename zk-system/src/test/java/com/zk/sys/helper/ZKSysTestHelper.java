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
* @Title: ZKSysTestHelper.java 
* @author Vinson 
* @Package com.zk.sys.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 5, 2020 12:27:32 PM 
* @version V1.0 
*/
package com.zk.sys.helper;

import org.apache.logging.log4j.LogManager;

/** 
* @ClassName: ZKSysTestHelper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/

import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zk.framework.serCen.eureka.ZKEurekaClientAutoConfiguration;
import com.zk.sys.configuration.ZKSysAfterConfiguration;
import com.zk.sys.configuration.ZKSysBeforeConfiguration;
import com.zk.sys.configuration.ZKSysMvcConfiguration;
import com.zk.sys.configuration.ZKSysSecConfiguration;

@SpringBootTest
@SpringBootApplication(exclude = { //
    DataSourceAutoConfiguration.class, //
    TransactionAutoConfiguration.class, //
    HibernateJpaAutoConfiguration.class, //
        MongoAutoConfiguration.class, //
//    WebMvcAutoConfiguration.class, //
//    EurekaDiscoveryClientConfiguration.class //
})
@EnableTransactionManagement(proxyTargetClass = true)
@Import(value = { //
        ZKSysBeforeConfiguration.class, //
        ZKSysAfterConfiguration.class, //
        ZKSysMvcConfiguration.class, //
        ZKSysSecConfiguration.class, //
//      ZKSysShiroConfiguration.class, //
        ZKEurekaClientAutoConfiguration.class, //
})
@PropertySource(encoding = "UTF-8", value = { "classpath:zk.sys.jdbc.properties", "classpath:zk.sys.mongo.properties" })
//@AutoConfigureOrder(value = Ordered.HIGHEST_PRECEDENCE)
@ComponentScan(basePackages = { "com.zk.sys.*" })
@EnableFeignClients(basePackages = { "com.zk.**.api" })
public class ZKSysTestHelper {

    static ConfigurableApplicationContext ctx = null;

    public static ConfigurableApplicationContext getMainCtx() {
        if (ctx == null) {
            log.info("[^_^:20200805-1508-001]========================================");
            log.info("[^_^:20200805-1508-001]=== zk system test 启动测试 ... ... ");
            log.info("[^_^:20200805-1508-001]========================================");
            ctx = run(new String[] {});
            log.info("[^_^:20200805-1508-001]----------------------------------------");
        }
        return ctx;
    }

    protected static Logger log = LogManager.getLogger(ZKSysTestHelper.class);

    // 启动
    public static ConfigurableApplicationContext run(String[] args) {

        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder();
        springApplicationBuilder = springApplicationBuilder.sources(ZKSysTestHelper.class);

        /*** 修改默认的配置文件名称和路径 ***/
        // 配置文件路径；默认：[file:./config/, file:./, classpath:/config/, classpath:/]
        springApplicationBuilder = springApplicationBuilder.properties("spring.config.location=classpath:/");
        // 定义配置文件名；默认：applicaiton；添加下面这一行后，不会读取 properties/yml 配置文件；多个时用英文逗号","
        // 隔；
        springApplicationBuilder = springApplicationBuilder
                .properties("spring.config.name=zk,zk.sys,zk.sys.env,test.zk.sys");

        SpringApplication springApplication = springApplicationBuilder.build();
        springApplication.setWebApplicationType(WebApplicationType.SERVLET);

        return springApplication.run(args);
    }

    // 退出
    public static int exit(ConfigurableApplicationContext ctx) {
        return SpringApplication.exit(ctx);
    }

}
