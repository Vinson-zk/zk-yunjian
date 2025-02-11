/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKIotTestHelper.java 
* @author Vinson 
* @Package com.zk.iot 
* @Description: TODO(simple description this file what to do. ) 
* @date Dec 31, 2024 3:17:00 PM 
* @version V1.0 
*/
package com.zk.iot.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.rabbitmq.client.ConnectionFactory;
import com.zk.framework.serCen.eureka.ZKEurekaClientAutoConfiguration;
import com.zk.iot.configuration.ZKIotAfterConfiguration;
import com.zk.iot.configuration.ZKIotBeforeConfiguration;
import com.zk.iot.configuration.ZKIotSecConfiguration;
import com.zk.iot.helper.api.sys.ZKSysOrgCompanyApiImpl;
import com.zk.iot.helper.api.sys.ZKSysOrgUserApiImpl;
import com.zk.iot.helper.api.sys.ZKSysSecAuthcApiImpl;
import com.zk.iot.rabbitmq.ZKIotRabbitMqReceiverTest;
import com.zk.iot.rabbitmq.ZKIotRabbitMqReceiverTest.ExchangeName;
import com.zk.security.principal.ZKSecDefaultUserPrincipal;
import com.zk.security.principal.ZKSecPrincipal;
import com.zk.security.principal.pc.ZKSecDefaultPrincipalCollection;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.sys.org.api.ZKSysOrgCompanyApi;
import com.zk.sys.org.api.ZKSysOrgUserApi;
import com.zk.sys.sec.api.ZKSysSecAuthcApi;

import junit.framework.TestCase;

/** 
* @ClassName: ZKIotTestHelper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@SpringBootApplication(exclude = { //
        DataSourceAutoConfiguration.class, //
        TransactionAutoConfiguration.class, //
//        HibernateJpaAutoConfiguration.class,
//        MongoAutoConfiguration.class,
//        WebMvcAutoConfiguration.class
})
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = { "com.zk.iot.*" })
//@EnableFeignClients(basePackages = { "com.zk.**.api" })
@Import(value = { //
        ZKIotBeforeConfiguration.class, //
        ZKIotAfterConfiguration.class, //
        ZKIotSecConfiguration.class, //
        ZKEurekaClientAutoConfiguration.class, //
})
public class ZKIotTestHelper {

    protected static Logger log = LogManager.getLogger(ZKIotTestHelper.class);

    public static void main(String[] args) {
        try {
            log.info("[^_^:20241231-1708-001]========================================");
            log.info("[^_^:20241231-1708-001]=== zk iot test  启动 ... ... ");
            log.info("[^_^:20241231-1708-001]========================================");
            run(args);
            log.info("[^_^:20241231-1708-001]----------------------------------------");

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    // 启动
    public static ConfigurableApplicationContext run(String[] args) {
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder();
        springApplicationBuilder = springApplicationBuilder.sources(ZKIotTestHelper.class);

        /*** 修改默认的配置文件名称和路径 ***/
        // 配置文件路径；默认：[file:./config/, file:./, classpath:/config/, classpath:/]
//        springApplicationBuilder = springApplicationBuilder.properties("spring.config.location=classpath:/");
        // 定义配置文件名；默认：applicaiton；添加下面这一行后，不会读取 properties/yml 配置文件；多个时用英文逗号","
        // 隔；
        springApplicationBuilder = springApplicationBuilder
                .properties("spring.config.name=zk,zk.iot,zk.iot.env,test.zk.iot");

        SpringApplication springApplication = springApplicationBuilder.build();
        springApplication.setWebApplicationType(WebApplicationType.REACTIVE);

        ctx = springApplication.run(args);

        return ctx;
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
            ctx = ZKIotTestHelper.run(new String[] {});
        }
        return ctx;
    }

    @Test
    public void testCtx() {
        TestCase.assertNotNull(getCtx());
    }

    // ==============================================================
    @Bean
    ZKSysOrgCompanyApi sysOrgCompanyApi() {
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

    // ==============================================================
    // 给当前会话设置登录用户信息
    public static ZKSecPrincipal<String> putCurrentUser() {
        ZKSecTicket tk = ZKSecSecurityUtils.getTikcet(true);
        ZKSecPrincipalCollection<String> pc = new ZKSecDefaultPrincipalCollection<String>();
        ZKSecPrincipal<String> p = new ZKSecDefaultUserPrincipal<String>("-1", "t-Accout", "t-Nickname", 0l, "t-Udid",
                0, "-1", "t-GroupCode", "9527", "t-CompanyCode");
        pc.add("t-RealmName", p);
        tk.setPrincipalCollection(pc);
        return p;
    }

    // ==============================================================
    // 启动消息接收监听
    @Autowired
    public void startListenRabbitMQ(ConnectionFactory factory) {
        ZKIotRabbitMqReceiverTest.receiverMsg(factory, ExchangeName.devReceiver, ExchangeName.devSender);
    }

}
