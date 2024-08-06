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
* @Title: ZKCoreTestHelperSpringBootMain.java 
* @author Vinson 
* @Package com.zk.core.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 12, 2021 5:23:14 PM 
* @version V1.0 
*/
package com.zk.core.helper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import com.zk.core.commons.ZKEnvironment;
import com.zk.core.helper.configuration.ZKCoreParentConfiguration;
import com.zk.core.helper.configuration.env.ZKCoreEnvChildConfiguration;
import com.zk.core.helper.configuration.env.ZKCoreEnvParentConfiguration;
import com.zk.core.helper.configuration.env.ZKCoreEnvSourceConfiguration;
import com.zk.core.utils.ZKEnvironmentUtils;

/** 
* @ClassName: ZKCoreTestHelperSpringBootMain 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@SpringBootApplication
@Import(value = { //
        ZKCoreParentConfiguration.class, //
        ZKCoreEnvSourceConfiguration.class, //
        ZKCoreEnvParentConfiguration.class, //
        ZKCoreEnvChildConfiguration.class, //
})
public class ZKCoreTestHelperSpringBootMain {

    static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        System.out.println("[^_^:20210812-1700-001]=========================================================");
        System.out.println("[^_^:20210812-1700-001]=== zk Core ZKCoreTestHelperSpringBootMain 测试启动 ... ...");
        System.out.println("[^_^:20210812-1700-001]=========================================================");
        run(args);
        System.out.println("[^_^:20210812-1700-001]---------------------------------------------------------");
    }

    public static ConfigurableApplicationContext run(String[] args) {
        if (ctx != null) {
            return ctx;
        }

        SpringApplicationBuilder appCtxBuilder = new SpringApplicationBuilder(ZKCoreTestHelperSpringBootMain.class);

//      /*** 修改默认的配置文件名称和路径 ***/
//      // 配置文件路径；默认：[file:./config/, file:./, classpath:/config/, classpath:/]
//      appCtxBuilder = appCtxBuilder.properties("spring.config.location=classpath:/");
//      // 定义配置文件名；默认：applicaiton；添加下面这一行后，不会读取 application.properties/yml 配置文件
        appCtxBuilder = appCtxBuilder.properties("spring.config.name=test.zk.core");
//        appCtxBuilder = appCtxBuilder.parent(ZKCoreParentConfiguration.class);
//        appCtxBuilder = appCtxBuilder.child(ZKCoreChildConfiguration.class);

        SpringApplication springApplication = appCtxBuilder.build();
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        ctx = springApplication.run(args);

        ZKEnvironmentUtils.setZKEnvironment(new ZKEnvironment(ctx, null));

        return ctx;
    }

    public static void exit() {
        if (ctx != null) {
            SpringApplication.exit(ctx);
            ctx = null;
        }
    }

}
