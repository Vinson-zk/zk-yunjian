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
* @Title: ZKConfigBeanDefinitionTwo.java 
* @author Vinson 
* @Package com.zk.demo.web.context.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 11, 2023 3:38:03 PM 
* @version V1.0 
*/
package com.zk.demo.web.context.configuration;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import com.zk.demo.web.context.ZKDemoContextMain.Print;

/** 
* @ClassName: ZKConfigBeanDefinitionTwo 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKConfigBeanDefinitionTwo implements ImportBeanDefinitionRegistrar {

    public ZKConfigBeanDefinitionTwo() {
        System.out.println(Print.config + "[]" + this.getClass().getSimpleName() + "【构造函数】" + this.hashCode());
    }

    public ZKConfigBeanDefinitionTwo(String s) {
    }
    
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry,
            BeanNameGenerator importBeanNameGenerator) {
        System.out.println(Print.config + "[]" + "registerBeanDefinitions - 1; " + this.getClass().getSimpleName() + " "
                + this.hashCode());
        registerBeanDefinitions(importingClassMetadata, registry);
    }

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        System.out.println(Print.config + "[]" + "registerBeanDefinitions - 2; " + this.getClass().getSimpleName() + " "
                + this.hashCode());
    }

    @Bean
    public ZKConfigBeanDefinitionTwo zkConfigBeanDefinitionTwo() {
        System.out.println(Print.config + "[]" + this.getClass().getSimpleName() + "【create Bean】" + this.hashCode());
        return new ZKConfigBeanDefinitionTwo(null);
    }

}
