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
* @Title: ZKFileTestHelper.java 
* @author Vinson 
* @Package com.zk.file.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 25, 2022 11:45:05 AM 
* @version V1.0 
*/
package com.zk.file.helper;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zk.file.helper.sysApi.ZKSysOrgCompanyApiImpl;

import junit.framework.TestCase;

/** 
* @ClassName: ZKFileTestHelper 
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
//        FeignAutoConfiguration.class
})
@EnableEurekaClient
@EnableTransactionManagement(proxyTargetClass = true)
//@ComponentScan(basePackages = { "com.zk.server.central.filter" })
//@ServletComponentScan(basePackages = { "com.zk.server.central.filter" })
//@AutoConfigureOrder(value = Ordered.HIGHEST_PRECEDENCE)
@ComponentScan(basePackages = { "com.zk.file.*", "com.zk.log.*" })
//@EnableFeignClients(basePackages = { "com.zk.**.api" })
public class ZKFileTestHelper {

    protected static Logger log = LoggerFactory.getLogger(ZKFileTestHelper.class);

    @Bean
    public ZKSysOrgCompanyApiImpl sysOrgCompanyApi(){
        return new ZKSysOrgCompanyApiImpl();
    }

    // 启动
    public static ConfigurableApplicationContext run(String[] args) {
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder();
        springApplicationBuilder = springApplicationBuilder.sources(ZKFileTestHelper.class);

        /*** 修改默认的配置文件名称和路径 ***/
        // 配置文件路径；默认：[file:./config/, file:./, classpath:/config/, classpath:/]
        springApplicationBuilder = springApplicationBuilder.properties("spring.config.location=classpath:/");
        // 定义配置文件名；默认：applicaiton；添加下面这一行后，不会读取 properties/yml 配置文件；多个时用英文逗号","
        // 隔；
        springApplicationBuilder = springApplicationBuilder.properties("spring.config.name=zk,zk.file,zk.file.env");

        SpringApplication springApplication = springApplicationBuilder.build();
        springApplication.setWebApplicationType(WebApplicationType.SERVLET);

        return springApplication.run(args);
    }

    // 退出
    public static int exit(ConfigurableApplicationContext ctx) {
        return SpringApplication.exit(ctx);
    }

    static ConfigurableApplicationContext ctx = null;

    public static ConfigurableApplicationContext getMainCtx() {
        return getCtx();

    }

    public static ConfigurableApplicationContext getCtx() {
        if (ctx == null) {
            log.info("[^_^:20221012-1442-001]========================================");
            log.info("[^_^:20221012-1442-001]=== zk fiile Test 启动 ... ... ");
            log.info("[^_^:20221012-1442-001]========================================");
//            ctx = ZKFileSpringBootMain.run(new String[] {});
            ctx = ZKFileTestHelper.run(new String[] {});
            log.info("[^_^:20221012-1442-001]----------------------------------------");
        }
        return ctx;
    }

    @Test
    public void testCtx() {
        TestCase.assertNotNull(getCtx());
    }

}
