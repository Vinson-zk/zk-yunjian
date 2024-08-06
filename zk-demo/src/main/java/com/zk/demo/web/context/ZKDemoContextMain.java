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
* @Title: ZKDemoContext.java 
* @author Vinson 
* @Package com.zk.demo.context 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 8, 2023 1:58:12 PM 
* @version V1.0 
*/
package com.zk.demo.web.context;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.BoundConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.zk.core.utils.ZKJsonUtils;
import com.zk.demo.web.context.bean.ZKBeanConfig;
import com.zk.demo.web.context.bean.ZKBeanConfig.ZKConfigBeanLifeCycle;
import com.zk.demo.web.context.bean.ZKBeanName;
import com.zk.demo.web.context.bean.ZKBeanOrder;
import com.zk.demo.web.context.bean.ZKBeanProperties;
import com.zk.demo.web.context.configuration.ZKDemoConfigAnnotation;

/**
 * @ClassName: ZKDemoContext
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@SpringBootApplication(exclude = { //
        DataSourceAutoConfiguration.class, //
        TransactionAutoConfiguration.class, //
//        EurekaClientAutoConfiguration.class, //
//        EurekaClientConfigServerAutoConfiguration.class, //
//        DiscoveryClientOptionalArgsConfiguration.class, //
//        EurekaDiscoveryClientConfiguration.class, //
//        EurekaReactiveDiscoveryClientConfiguration.class, //
//        LoadBalancerEurekaAutoConfiguration.class, //

//        FeignHalAutoConfiguration.class, //
//        FeignAutoConfiguration.class, //
//        FeignAcceptGzipEncodingAutoConfiguration.class, //
//        FeignContentGzipEncodingAutoConfiguration.class, //
//        FeignLoadBalancerAutoConfiguration.class, //

        TaskExecutionAutoConfiguration.class, //
})
//@EnableAutoConfiguration(exclude = { //
//        DataSourceAutoConfiguration.class, //
//        TransactionAutoConfiguration.class, //
//        EurekaClientAutoConfiguration.class, //
//        EurekaClientConfigServerAutoConfiguration.class, //
//        DiscoveryClientOptionalArgsConfiguration.class, //
//        EurekaDiscoveryClientConfiguration.class, //
//        EurekaReactiveDiscoveryClientConfiguration.class, //
//        LoadBalancerEurekaAutoConfiguration.class, //
//        FeignHalAutoConfiguration.class, //
//        FeignAutoConfiguration.class, //
//        FeignAcceptGzipEncodingAutoConfiguration.class, //
//        FeignContentGzipEncodingAutoConfiguration.class, //
//        FeignLoadBalancerAutoConfiguration.class, //
//        TaskExecutionAutoConfiguration.class, //
//})
//@ConfigurationPropertiesScan // 添加此注解，@ConfigurationProperties 才会生效
@ZKDemoConfigAnnotation
//@Import(value = { ZKConfigBeanDefinitionOne.class })
//@ImportAutoConfiguration
@Import(value = { ZKBeanConfig.class })
@PropertySources(value = { //
        @PropertySource(encoding = "UTF-8", value = { "zk.demo.properties" }), //
})
@ComponentScan(basePackages = { "com.zk.demo.web.context" })
public class ZKDemoContextMain {
    
    public static interface Print {
        public final static String config = "[^_^:20230211-1142-001] ZKDemoContextConfig: ";
        public final static String main = "[^_^:20230211-1142-001] main(): ";
    }

    public ZKDemoContextMain() {
        System.out.println(Print.config + this.getClass().getSimpleName() + "【构造函数】" + this.hashCode());
    }
    
    public ZKDemoContextMain(String s) {
        
    }

    @Bean
    public ZKDemoContextMain zkDemoContextMain() {
        System.out.println(Print.config + "main() create bean ZKDemoContextMain " + this.getClass().getSimpleName() + " " + this.hashCode());
        return new ZKDemoContextMain(null);
    }

    public static void main(String[] args) {
        try {

//          DefaultApplicationContextFactory acf = new DefaultApplicationContextFactory();

            System.out.println("[20230208-2202-001] ===================================================== ");

            ApplicationContext ctx = null;
//            SpringApplicationBuilder saBuilder = new SpringApplicationBuilder(ZKDemoContextMain.class); //
            SpringApplicationBuilder saBuilder = new SpringApplicationBuilder(); //
            saBuilder = saBuilder.sources(ZKDemoContextMain.class);
//            saBuilder = saBuilder.sources(ZKConfigConfigurationTwo.class, ZKDemoContextMain.class);
            saBuilder = saBuilder.properties("spring.config.name=zk");
            SpringApplication springApp = saBuilder.build();
            springApp.setWebApplicationType(WebApplicationType.NONE);
            ctx = springApp.run(args);

//            ApplicationContext ctx = SpringApplication.run(ZKDemoContextMain.class, args);

//            @SuppressWarnings("resource")
//            ApplicationContext ctx = new AnnotationConfigApplicationContext(//
////                    ZKConfigBeanLifeCycle.class, //
//                    ZKBeanConfig.class, //
//                    ZKDemoContextMain.class);

            System.out.println(Print.main + " -----------【ctx info】---------------------- ");
            System.out.println(Print.main + " getId: " + ctx.getId());
            System.out.println(Print.main + " getStartupDate: " + ctx.getStartupDate());
            System.out.println(Print.main + " getApplicationName: " + ctx.getApplicationName());
            System.out.println(Print.main + " getBeanDefinitionCount: " + ctx.getBeanDefinitionCount());
            System.out.println(Print.main + " getDisplayName: " + ctx.getDisplayName());
            System.out.println(Print.main + " getParent: " + ctx.getParent());
            System.out.println(Print.main + ZKJsonUtils.toJsonStr(ctx.getBeanDefinitionNames()));

            System.out.println(Print.main + " -----------【@ConfigurationProperties】---------------------- ");
            ZKBeanProperties zkBeanProperties = ctx.getBean(ZKBeanProperties.class);
            System.out.println(
                    Print.main + " zk.demo.bean.value: " + ctx.getEnvironment().getProperty("zk.demo.bean.value"));
            System.out.println(Print.main + " getValue: " + zkBeanProperties.getValue());
            System.out.println(Print.main + " getValueContext: " + zkBeanProperties.getValueContext());
            System.out.println(Print.main + " getValueContextChild: " + zkBeanProperties.getValueContextChild());

            System.out.println(Print.main + " -----------【@Order】---------------------- ");
            Map<String, ZKBeanOrder> orderBeans = ctx.getBeansOfType(ZKBeanOrder.class);
            for (Entry<String, ZKBeanOrder> e : orderBeans.entrySet()) {
                System.out.println(Print.main + e.getKey() + "  " + e.getValue().getValue());
            }

            System.out.println(Print.main + " -----------【Bean name】---------------------- ");
            System.out.println(Print.main + " ZKBeanName.class bean names: " + ZKJsonUtils.toJsonStr(ctx.getBeanNamesForType(ZKBeanName.class)));
            System.out.println(Print.main + " BoundConfigurationProperties.class bean names: " + ZKJsonUtils.toJsonStr(ctx.getBeanNamesForType(BoundConfigurationProperties.class)));
            System.out.println(Print.main + " ZKConfigBeanLifeCycle.class bean names: " + ZKJsonUtils.toJsonStr(ctx.getBeanNamesForType(ZKConfigBeanLifeCycle.class)));
            
            System.out.println(Print.main + " ----------------------------------------------------- ");
        }
        catch(Exception e) {
            e.printStackTrace();

        }
    }

//    @Bean
//    public ZKConfigBeanDefinitionTwo zkConfigBeanDefinitionTwo() {
//        return new ZKConfigBeanDefinitionTwo();
//    }

//    @Bean
//    public ZKConfigBeanLifeCycle ZKConfigBeanLifeCycle() {
//        return new ZKConfigBeanLifeCycle();
//    }

//    @Bean
//    public ZKBeanLifeCycle ZKBeanLifeCycle() {
//        return new ZKBeanLifeCycle();
//    }

}
