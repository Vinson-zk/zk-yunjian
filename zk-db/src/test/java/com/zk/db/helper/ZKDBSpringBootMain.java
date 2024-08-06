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
 * @Title: ZKDBSpringBootMain.java 
 * @author Vinson 
 * @Package com.zk.db.dynamic.spring 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 11:29:55 AM 
 * @version V1.0   
*/
package com.zk.db.helper;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** 
* @ClassName: ZKDBSpringBootMain 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@SpringBootApplication(exclude = { //
        DataSourceAutoConfiguration.class, //
        TransactionAutoConfiguration.class, //
        HibernateJpaAutoConfiguration.class, //
})
@ComponentScan(basePackages = "com.zk.db.helper")
@EnableTransactionManagement(proxyTargetClass = true)
@ImportAutoConfiguration(classes = { ZKSpringMyBatisConfiguration.class })
public class ZKDBSpringBootMain {

    protected static Logger log = LogManager.getLogger(ZKDBSpringBootMain.class);

    public static void main(String[] args) {
        try {
            System.out.println("[^_^:20190628-1708-001]====================================================");
            System.out.println("[^_^:20190628-1708-001]=== zk db ZKDBSpringBootMain  启动 ... ... ");
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
            SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(ZKDBSpringBootMain.class);
//          springApplicationBuilder = springApplicationBuilder.sources(ZKDBSpringBootMain.class);
            SpringApplication springApplication = springApplicationBuilder.build();
            springApplication.setWebApplicationType(WebApplicationType.NONE);
            ctx = springApplication.run(args);
        }
        return ctx;
    }
    
    public static void exit() {
        if(ctx != null) {
            SpringApplication.exit(ctx);
        }
    }

}
