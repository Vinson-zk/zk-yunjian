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
* @Title: ZKBeanConfig.java 
* @author Vinson 
* @Package com.zk.demo.context.bean 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 8, 2023 3:49:58 PM 
* @version V1.0 
*/
package com.zk.demo.web.context.bean;

import java.util.List;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.annotation.Order;

/** 
* @ClassName: ZKBeanConfig 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Import(value = { //
//        ZKConfigBeanName.class, //
//        ZKConfigBeanOrder.class, //
//        ZKConfigBeanLifeCycle.class, //
})
@ImportAutoConfiguration(value = { //
        ZKBeanConfigChild.class, //
////        ZKConfigBeanName.class, //
////        ZKConfigBeanOrder.class, //
////        ZKConfigBeanLifeCycle.class, //
})
@PropertySources(value = { //
        @PropertySource(encoding = "UTF-8", value = { "context/zk.demo.bean.properties" }), //
})
public class ZKBeanConfig {

//    @Bean
//    public ZKConfigBeanLifeCycle ZKConfigBeanLifeCycle() {
//        return new ZKConfigBeanLifeCycle();
//    }

    @Bean
    @ConfigurationProperties(prefix = "zk.demo.bean")
    public ZKBeanProperties zkBeanProperties() {
        return new ZKBeanProperties();
    }

    // ===============================================================
    public static class ZKConfigBeanLifeCycle {

        @Bean(initMethod = "initMethod")
        public ZKBeanLifeCycle ZKBeanLifeCycle() {
            return new ZKBeanLifeCycle();
        }

//      @DependsOn("ZKBeanLifeCycle")
        @Bean(initMethod = "initMethod")
        public ZKBeanLifeCycle ZKBeanLifeCycle_two() {
            return new ZKBeanLifeCycle("ZKBeanLifeCycle_two");
        }
        
        @Bean("mapperScannerConfigurer")
        public MapperScannerConfigurer mapperScannerConfigurer() {
            MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
            mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
            mapperScannerConfigurer.setBasePackage("com.zk.demo.context.bean");
//            mapperScannerConfigurer.setAnnotationClass(ZKBeanLifeCycle.class);
            return mapperScannerConfigurer;
        }

//        @DependsOn("ZKBeanLifeCycle")
//        @Bean
//        public ZKBeanValue ZKBeanValue_noPrint() {
//            return new ZKBeanValue("ZKBeanValue_noPrint");
//        }

    }

    // ===============================================================
    public static class ZKConfigBeanOrder {

        @Order(value = 2)
        @Bean
        public ZKBeanOrder ZKBeanOrder1() {
            return new ZKBeanOrder("1");
        }

        @Order(value = 1)
        @Bean
        public ZKBeanOrder ZKBeanOrder2() {
            return new ZKBeanOrder("2");
        }

        // @Order(value = 2) 控制的是 List<ZKBeanOrderInterface> 的顺序；
        @Autowired
        public void antZKBeanOrder(List<ZKBeanOrder> orderBeans) {
            for (ZKBeanOrder bo : orderBeans) {
                System.out
                        .println("[^_^:20230207-220913-001] ￥￥￥ " + this.getClass().getSimpleName() + " " + bo.getValue());
            }
        }
    }

    // ===============================================================
    public static class ZKConfigBeanName {

        @Bean // ZKBeanName
        public ZKBeanName ZKBeanName() {
            return new ZKBeanName();
        }

        @Bean // BeanName
        public ZKBeanName BeanName() {
            return new ZKBeanName();
        }

        @Bean("bn") // bn
        public ZKBeanName beanName() {
            return new ZKBeanName();
        }
    }
}


