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
* @Title: ZKDBMapperScannerConfiguration.java 
* @author Vinson 
* @Package com.zk.db.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 10, 2023 5:26:24 PM 
* @version V1.0 
*/
package com.zk.db.configuration;

import java.lang.annotation.Annotation;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import jakarta.annotation.PostConstruct;

/** 
* @ClassName: ZKDBMapperScannerConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
//@DependsOn(value = { //
//        "sqlSessionFactory", //
//})
public class ZKDBMapperScannerConfiguration {

    public ZKDBMapperScannerConfiguration() {
        System.out.println(ZKEnableDB.printLog + "[" + this.getClass().getSimpleName() + "]");
    }

    String basePackage;

    Class<? extends Annotation> annotationClass;

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Autowired
    public void Autowired() {
        System.out.println(ZKEnableDB.printLog + "@Autowired: [" + this.getClass().getSimpleName() + "]");
    }

    @PostConstruct
    public void PostConstruct() {
        System.out.println(ZKEnableDB.printLog + "@PostConstruct: [" + this.getClass().getSimpleName() + "]");
    }

    /**
     * 
     *
     * @Title: mapperScannerConfigurer
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 10, 2023 6:39:55 PM
     * @return
     * @return MapperScannerConfigurer
     */
    @ConditionalOnMissingBean(value = { MapperScannerConfigurer.class })
    @Bean("mapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        System.out.println(ZKEnableDB.printLog + "mapperScannerConfigurer --- [" + this.getClass().getSimpleName()
                + "] " + this.hashCode());
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(this.basePackage);
        mapperScannerConfigurer.setAnnotationClass(
                (Class<? extends Annotation>) this.annotationClass);
        return mapperScannerConfigurer;
    }

}
