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
* @Title: ZKWebmvcTestHelperMvcSpringBootMain.java 
* @author Vinson 
* @Package com.zk.core.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 12, 2021 5:29:58 PM 
* @version V1.0 
*/
package com.zk.webmvc.helper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.zk.core.commons.ZKEnvironment;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.webmvc.helper.configuration.ZKWebmvcChildConfiguration;
import com.zk.webmvc.helper.configuration.ZKWebmvcParentConfiguration;

/**
 * @ClassName: ZKWebmvcTestHelperMvcSpringBootMain
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@SpringBootApplication(exclude = { //
        WebMvcAutoConfiguration.class, //
        DataSourceAutoConfiguration.class, //
        ServletWebServerFactoryAutoConfiguration.class, //
        SecurityAutoConfiguration.class, //
})
@ImportAutoConfiguration(value = { ZKWebmvcParentConfiguration.class, ZKWebmvcChildConfiguration.class })
public class ZKWebmvcTestHelperMvcSpringBootMain {

    static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        System.out.println("[^_^:20210812-1730-001]=========================================================");
        System.out.println("[^_^:20210812-1730-001]=== zk ZKWebmvc test 测试启动 ... ...");
        System.out.println("[^_^:20210812-1730-001]=========================================================");
        run(args);
        System.out.println("[^_^:20210812-1730-001]---------------------------------------------------------");
    }

    public static ConfigurableApplicationContext run(String[] args) {
        if (ctx != null) {
            return ctx;
        }

        SpringApplicationBuilder appCtxBuilder = new SpringApplicationBuilder(
                ZKWebmvcTestHelperMvcSpringBootMain.class);

//      /*** 修改默认的配置文件名称和路径 ***/
//      // 配置文件路径；默认：[file:./config/, file:./, classpath:/config/, classpath:/]
//        appCtxBuilder = appCtxBuilder.properties("spring.config.location=classpath:/test.zk.webmvc.properties");
        appCtxBuilder = appCtxBuilder.properties("spring.config.location=classpath:/");
//      // 定义配置文件名；默认：applicaiton；添加下面这一行后，不会读取 application.properties/yml 配置文件
        appCtxBuilder = appCtxBuilder.properties("spring.config.name=zk,test.zk.webmvc");

//        appCtxBuilder = appCtxBuilder.parent(ZKCoreParentConfiguration.class);
//        appCtxBuilder = appCtxBuilder.child(ZKCoreChildConfiguration.class);

        SpringApplication springApplication = appCtxBuilder.build();
        springApplication.setWebApplicationType(WebApplicationType.SERVLET);
        ctx = springApplication.run(args);

        ZKEnvironmentUtils.setZKEnvironment(new ZKEnvironment(ctx));

        return ctx;
    }

    public static ConfigurableApplicationContext getCtx() {
        if (ctx == null) {
            ctx = run(new String[] {});
        }
        return ctx;
    }

    public static void exit() {
        if (ctx != null) {
            SpringApplication.exit(ctx);
            ctx = null;
        }
    }

    @Bean
    TestRestTemplate testRestTemplate(HttpMessageConverters messageConverters) {
        TestRestTemplate trt = new TestRestTemplate();
        trt.getRestTemplate().setMessageConverters(messageConverters.getConverters());
        return trt;
    }
}
