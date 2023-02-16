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
* @Title: ZKEurekaTestMain.java 
* @author Vinson 
* @Package com.zk.framework.serCen.eureka 
* @Description: TODO(simple description this file what to do.) 
* @date Jun 24, 2020 2:31:52 PM 
* @version V1.0 
*/
package com.zk.framework.serCen.eureka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.MutableDiscoveryClientOptionalArgs;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.zk.framework.serCen.ZKSerCenEncrypt;
import com.zk.framework.serCen.support.ZKSerCenSampleCipher;

/** 
* @ClassName: ZKEurekaTestMain 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@SpringBootApplication
@EnableEurekaClient
public class ZKEurekaTestMain {

    protected static Logger log = LoggerFactory.getLogger(ZKEurekaTestMain.class);

    public static void main(String[] args) {
        try {
            log.info("[^_^:20200624-1708-001]========================================");
            log.info("[^_^:20200624-1708-001]=== zk Demo  启动 ... ... ");
            log.info("[^_^:20200624-1708-001]========================================");
            run(args);
            log.info("[^_^:20200624-1708-001]----------------------------------------");

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static ConfigurableApplicationContext run(String[] args) {
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder();
        springApplicationBuilder = springApplicationBuilder.sources(ZKEurekaTestMain.class);

        /*** 修改默认的配置文件名称和路径 ***/
        // 配置文件路径；默认：[file:./config/, file:./, classpath:/config/, classpath:/]
        springApplicationBuilder = springApplicationBuilder.properties("spring.config.location=classpath:/");
        // 定义配置文件名；默认：applicaiton；添加下面这一行后，不会读取 properties/yml 配置文件；多个时用英文逗号","
        // 隔；
        springApplicationBuilder = springApplicationBuilder
                .properties("spring.config.name=test.zk.framework,eureka/test.zk.framework.eureka.client");

        SpringApplication springApplication = springApplicationBuilder.build();
        springApplication.setWebApplicationType(WebApplicationType.SERVLET);

        return springApplication.run(args);
    }

    /**************************************** */
    /**************************************** */
    /**************************************** */

    @Bean
    public ZKSerCenSampleCipher zkSerCenSampleCipher() {
        return new ZKSerCenSampleCipher();
    }

    @Bean
    @ConditionalOnClass(name = "com.sun.jersey.api.client.filter.ClientFilter")
//    @ConditionalOnMissingBean(value = AbstractDiscoveryClientOptionalArgs.class, search = SearchStrategy.CURRENT)
    public MutableDiscoveryClientOptionalArgs discoveryClientOptionalArgs(ZKSerCenEncrypt zkSerCenEncrypt) {

        MutableDiscoveryClientOptionalArgs ms = new MutableDiscoveryClientOptionalArgs();
        ms.setTransportClientFactories(new ZKEurekaTransportClientFactories(zkSerCenEncrypt));

        return ms;
    }

}
