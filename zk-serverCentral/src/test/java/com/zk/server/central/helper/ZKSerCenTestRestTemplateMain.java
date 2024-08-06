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
 * @Title: ZKSerCenTestRestTemplateMain.java 
 * @author Vinson 
 * @Package com.zk.server.central.helper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 20, 2019 4:25:46 PM 
 * @version V1.0   
*/
package com.zk.server.central.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zk.server.central.configuration.ZKSerCenBeforeConfiguration;
import com.zk.server.central.configuration.ZKSerCenMvcConfiguration;
import com.zk.server.central.configuration.ZKSerCenShiroConfiguration;

/** 
* @ClassName: ZKSerCenTestRestTemplateMain 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
//@SpringCloudApplication
@SpringBootApplication(exclude = { 
        DataSourceAutoConfiguration.class, //
        TransactionAutoConfiguration.class, //
        HibernateJpaAutoConfiguration.class, //
//      MongoAutoConfiguration.class
})
@EnableEurekaServer
@EnableTransactionManagement(proxyTargetClass = true)
//@ComponentScan(basePackages = { "com.zk.server.central.filter" })
//@ServletComponentScan(basePackages = { "com.zk.server.central.filter" })
@ImportAutoConfiguration(classes = { 
        ZKSerCenBeforeConfiguration.class, //
        ZKSerCenMvcConfiguration.class, //
        ZKSerCenShiroConfiguration.class, //
      })
//@AutoConfigureOrder(value = Ordered.HIGHEST_PRECEDENCE)
@ComponentScan(basePackages = { "com.zk.server.central.*" })
public class ZKSerCenTestRestTemplateMain {

    protected static Logger log = LogManager.getLogger(ZKSerCenTestRestTemplateMain.class);

    public static void main(String[] args) {
        try {
            System.out.println("[^_^:20190628-1708-001]====================================================");
            System.out.println("[^_^:20190628-1708-001]=== zk ZKSerCenTestRestTemplateMain 启动 ... ... ");
            System.out.println("[^_^:20190628-1708-001]====================================================");
            run(args);
            System.out.println("[^_^:20190628-1708-001]----------------------------------------------------");

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static ConfigurableApplicationContext ctx = null;

    public static ConfigurableApplicationContext run(String[] args) {
        if (ctx == null || !ctx.isRunning()) {
            SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder();
            springApplicationBuilder = springApplicationBuilder.sources(ZKSerCenTestRestTemplateMain.class);

            /*** 修改默认的配置文件名称和路径 ***/
            // 配置文件路径；默认：[file:./config/, file:./, classpath:/config/,
            // classpath:/]
            springApplicationBuilder = springApplicationBuilder.properties("spring.config.location=classpath:/");
            // 定义配置文件名；默认：applicaiton；添加下面这一行后，不会读取 properties/yml
            // 配置文件；多个时用英文逗号"," 隔；
            springApplicationBuilder = springApplicationBuilder.properties("spring.config.name=zk,zk.ser.cen,zk.ser.cen.env");
            
            SpringApplication springApplication = springApplicationBuilder.build();
            springApplication.setWebApplicationType(WebApplicationType.SERVLET);

            ctx = springApplication.run(args);
        }
        return ctx;
    }

    public static int exit() {
        return SpringApplication.exit(ctx);
    }

}
