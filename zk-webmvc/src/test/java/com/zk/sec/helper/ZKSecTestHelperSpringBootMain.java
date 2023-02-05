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
* @Title: ZKSecTestHelperSpringBootMain.java 
* @author Vinson 
* @Package com.zk.security.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 29, 2021 10:32:22 AM 
* @version V1.0 
*/
package com.zk.sec.helper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.mongo.configuration.ZKMongoAutoConfiguration;
import com.zk.security.helper.ZKSecTestHelperMongoConfiguration;
import com.zk.security.helper.ZKSecTestHelperRedisConfiguration;

/** 
* @ClassName: ZKSecTestHelperSpringBootMain 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@SpringBootApplication(exclude = { //
        DataSourceAutoConfiguration.class, //
        MongoAutoConfiguration.class, //
})
@ImportAutoConfiguration(value = { //
        ZKMongoAutoConfiguration.class,
        ZKSecTestHelperMongoConfiguration.class, //
        ZKSecTestHelperRedisConfiguration.class, //
})
public class ZKSecTestHelperSpringBootMain {

    public static void main(String[] args) {
        System.out.println("[^_^:20210704-1100-001]=========================================================");
        System.out.println("[^_^:20210704-1100-001]=== zk Security ZKSecTestHelperSpringBootMain 启动 ... ...");
        System.out.println("[^_^:20210704-1100-001]=========================================================");
        run(args);
        System.out.println("[^_^:20210704-1100-001]---------------------------------------------------------");
    }

    private static ConfigurableApplicationContext ctx;

    public static ConfigurableApplicationContext run(String[] args) {

        if (ctx == null) {
            SpringApplicationBuilder appCtxBuilder = new SpringApplicationBuilder(ZKSecTestHelperSpringBootMain.class);

            /*** 修改默认的配置文件名称和路径 ***/
            // 配置文件路径；默认：[file:./config/, file:./, classpath:/config/,
            // classpath:/]
//            appCtxBuilder = appCtxBuilder.properties("spring.config.location=classpath:/eureka/");
            // 定义配置文件名；默认：applicaiton；添加下面这一行后，不会读取 application.properties/yml
            // 配置文件；多个时，用英文逗号分隔；
            appCtxBuilder = appCtxBuilder.properties("spring.config.name=test.zk.sec");

            appCtxBuilder = appCtxBuilder.web(WebApplicationType.SERVLET);

            SpringApplication app = appCtxBuilder.build();

            ctx = app.run(args);
        }
        return ctx;
    }

    public static void exit() {
        SpringApplication.exit(ctx);
    }

}
