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
* @Title: ZKMailSpringBootMain.java 
* @author Vinson 
* @Package com.zk.mail 
* @Description: TODO(simple description this file what to do. ) 
* @date May 23, 2022 10:26:35 PM 
* @version V1.0 
*/
package com.zk.mail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zk.framework.serCen.eureka.ZKEurekaClientAutoConfiguration;
import com.zk.mail.configuration.ZKMailAfterConfiguration;
import com.zk.mail.configuration.ZKMailBeforeConfiguration;
import com.zk.mail.configuration.ZKMailSecConfiguration;

/** 
* @ClassName: ZKMailSpringBootMain 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@SpringBootApplication(exclude = { 
        DataSourceAutoConfiguration.class, 
        TransactionAutoConfiguration.class,
//        HibernateJpaAutoConfiguration.class,
//        MongoAutoConfiguration.class,
//        WebMvcAutoConfiguration.class
})
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = { "com.zk.mail.*" })
@EnableFeignClients(basePackages = { "com.zk.**.api" })
@Import(value = { ZKMailBeforeConfiguration.class, //
        ZKMailAfterConfiguration.class, //
        ZKMailSecConfiguration.class, //
        ZKEurekaClientAutoConfiguration.class, //
})
//@EnableAsync
public class ZKMailSpringBootMain {

    protected static Logger log = LogManager.getLogger(ZKMailSpringBootMain.class);

    public static void main(String[] args) {
        try {
            log.info("[^_^:20220526-1708-001]========================================");
            log.info("[^_^:20220526-1708-001]=== zk mail  启动 ... ... ");
            log.info("[^_^:20220526-1708-001]========================================");
            run(args);
            log.info("[^_^:20220526-1708-001]----------------------------------------");

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    // 启动
    public static ConfigurableApplicationContext run(String[] args) {
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder();
        springApplicationBuilder = springApplicationBuilder.sources(ZKMailSpringBootMain.class);

        /*** 修改默认的配置文件名称和路径 ***/
        // 配置文件路径；默认：[file:./config/, file:./, classpath:/config/, classpath:/]
        springApplicationBuilder = springApplicationBuilder.properties("spring.config.location=classpath:/");
        // 定义配置文件名；默认：applicaiton；添加下面这一行后，不会读取 properties/yml 配置文件；多个时用英文逗号","
        // 隔；
        springApplicationBuilder = springApplicationBuilder.properties("spring.config.name=zk,zk.mail,zk.mail.env");

        SpringApplication springApplication = springApplicationBuilder.build();
        springApplication.setWebApplicationType(WebApplicationType.SERVLET);

        return springApplication.run(args);
    }

    // 退出
    public static int exit(ConfigurableApplicationContext ctx) {
        return SpringApplication.exit(ctx);
    }

}
