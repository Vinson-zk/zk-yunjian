/** 
* Copyright (c) 2012-2023 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKFileTestSpringBootMainHelper.java 
* @author Vinson 
* @Package com.zk.file.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Dec 26, 2023 4:00:49 PM 
* @version V1.0 
*/
package com.zk.file.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zk.file.configuration.ZKFileAfterConfiguration;
import com.zk.file.configuration.ZKFileBeforeConfiguration;
import com.zk.file.configuration.ZKFileSecConfiguration;
import com.zk.file.helper.sysApi.ZKSysOrgCompanyApiImpl;
import com.zk.file.helper.sysApi.ZKSysOrgUserApiImpl;
import com.zk.file.helper.sysApi.ZKSysSecAuthcApiImpl;
import com.zk.framework.serCen.eureka.ZKEurekaClientAutoConfiguration;
import com.zk.sys.org.api.ZKSysOrgUserApi;
import com.zk.sys.sec.api.ZKSysSecAuthcApi;

import junit.framework.TestCase;

/** 
* @ClassName: ZKFileTestSpringBootMainHelper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, TransactionAutoConfiguration.class,
//        HibernateJpaAutoConfiguration.class,
//        MongoAutoConfiguration.class,
//        WebMvcAutoConfiguration.class
})
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = { "com.zk.file.*" })
//@EnableFeignClients(basePackages = { "com.zk.**.api" })
@Import(value = { ZKFileBeforeConfiguration.class, //
        ZKFileAfterConfiguration.class, //
        ZKFileSecConfiguration.class, //
        ZKEurekaClientAutoConfiguration.class, //
})
public class ZKFileTestSpringBootMainHelper {

    protected static Logger log = LogManager.getLogger(ZKFileTestSpringBootMainHelper.class);

    public static void main(String[] args) {
        try {
            log.info("[^_^:20220621-1410-001]========================================");
            log.info("[^_^:20220621-1410-001]=== zk fiile test  启动 ... ... ");
            log.info("[^_^:20220621-1410-001]========================================");
            run(args);
            log.info("[^_^:20220621-1410-001]----------------------------------------");

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    static ConfigurableApplicationContext ctx = null;


    // 启动
    public static ConfigurableApplicationContext run() {
        return run(new String[] {});
    }
    public static ConfigurableApplicationContext run(String[] args) {
        if (ctx == null) {
            SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder();
            springApplicationBuilder = springApplicationBuilder.sources(ZKFileTestSpringBootMainHelper.class);

            /*** 修改默认的配置文件名称和路径 ***/
            // 配置文件路径；默认：[file:./config/, file:./, classpath:/config/, classpath:/]
            springApplicationBuilder = springApplicationBuilder.properties("spring.config.location=classpath:/");
            // 定义配置文件名；默认：applicaiton；添加下面这一行后，不会读取 properties/yml 配置文件；多个时用英文逗号","
            // 隔；
            springApplicationBuilder = springApplicationBuilder.properties("spring.config.name=zk,zk.file,zk.file.env");

            SpringApplication springApplication = springApplicationBuilder.build();
            springApplication.setWebApplicationType(WebApplicationType.SERVLET);

            ctx = springApplication.run(args);
        }
        return ctx;
    }

    // 退出
    public static int exit(ConfigurableApplicationContext ctx) {
        return SpringApplication.exit(ctx);
    }

    // ============================================================================
    /*
     * 测试使用 bean，与正式环境下的实现不同；一般微服务间的调用 api 测试时，写个模拟实现即可；
     * 
     * 正式环境再使用 @EnableFeignClients(basePackages = { "com.zk.**.api" }) 扫描正式实现。
     */

    @Bean
    ZKSysOrgCompanyApiImpl sysOrgCompanyApi() {
        return new ZKSysOrgCompanyApiImpl();
    }

    @Bean
    ZKSysSecAuthcApi sysSecAuthcApi() {
        return new ZKSysSecAuthcApiImpl();
    }

    @Bean
    ZKSysOrgUserApi sysOrgUserApi() {
        return new ZKSysOrgUserApiImpl();
    }

    // ============================================================================
    @Test
    public void testCtx() {
        TestCase.assertNotNull(run(null));
    }

}
