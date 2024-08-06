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
* @Title: ZKBeanLifeCycle.java 
* @author Vinson 
* @Package com.zk.demo.context.bean 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 8, 2023 5:14:04 PM 
* @version V1.0 
*/
package com.zk.demo.web.context.bean;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.lang.Nullable;

import jakarta.annotation.PostConstruct;

/** 
* @ClassName: ZKBeanLifeCycle 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
//@Configuration
public class ZKBeanLifeCycle implements //
        BeanDefinitionRegistryPostProcessor, //
        BeanFactoryPostProcessor, //
        BeanPostProcessor, //
        InstantiationAwareBeanPostProcessor, //
        InitializingBean, //
        BeanNameAware, //
        BeanFactoryAware, //
        DisposableBean {

    final static String logFlag = "[^_^:20230209-0030-001]";

    final static String allPrintName = " - ";

    String name;

    public ZKBeanLifeCycle() {
        this.name = "ZKBeanLifeCycle";
        this.print(" 1.", "构造方法;", null, allPrintName);
    }

    public ZKBeanLifeCycle(String name) {
        this.name = name;
        this.print(" 1.", "构造方法;", null, allPrintName);
    }

    // 配置 null 时，所有的都打印；不为 null 时，只打印设置的 bean 名称
    public List<String> printBeanNames = Arrays.asList(); // ZKBeanValue2

    boolean isPrint(String beanName) {
        if (printBeanNames == null || printBeanNames.isEmpty()) {
            return true;
        }
        if (allPrintName.equals(beanName) || printBeanNames.contains(beanName)) {
            return true;
        }
        return false;
    }
    
    public void print(String serialNumber, String printFlag, Object bean, String beanName) {
        if (this.isPrint(beanName)) {
            String msg = String.format("%s %s%s %s ", logFlag, serialNumber, this.name, printFlag);
            if (bean != null && bean instanceof ZKBeanProperties) {
                msg = String.format("%s[%s].value: %s", msg, bean.hashCode(), ((ZKBeanProperties) bean).getValue());
                System.out.println(msg);
            }
            else {
                System.out.println(msg + beanName);
            }
        }
    }
    
    // ==================================================================

    // 有继成 BeanFactoryPostProcessor 时，不会被调用
    @PostConstruct
    public void PostConstruct() {
        this.print(" 4.", "@PostConstruct;", null, allPrintName);
    }

    public void initMethod() {
        this.print(" 6.", "initMethod;", null, allPrintName);
    }

    /**
     * 
     * @param name
     * @see org.springframework.beans.factory.BeanNameAware#setBeanName(java.lang.String)
     */
    @Override
    public void setBeanName(String name) {
        this.print(" 2.", "setBeanName; name: " + name, null, allPrintName);
    }

    /**
     * 
     * @param beanFactory
     * @throws BeansException
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.print(" 3.", "setBeanFactory;[" + beanFactory.hashCode() + "]", null, allPrintName);
    }


    /**
     * 
     * @throws Exception
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.print(" 5.", "afterPropertiesSet;", null, allPrintName);
    }

    /**
     * 
     * @param beanFactory
     * @throws BeansException
     * @see org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.print(" 8.", "postProcessBeanFactory;[" + beanFactory.hashCode() + "]", null, allPrintName);
    }

    /**
     * 
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeansException
     * @see org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation(java.lang.Class, java.lang.String)
     */
    @Nullable
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        this.print(" 9.", "postProcessBeforeInstantiation;[" + beanClass.getSimpleName() + "]", null, beanName);
        return null;
    }

    /**
     * 
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     * @see org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor#postProcessAfterInstantiation(java.lang.Object, java.lang.String)
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        this.print("10.", "postProcessAfterInstantiation;", bean, beanName);
        return true;
    }

    /**
     * 
     * @param pvs
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     * @see org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor#postProcessProperties(org.springframework.beans.PropertyValues,
     *      java.lang.Object, java.lang.String)
     */
    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName)
            throws BeansException {
        this.print("11.", "postProcessProperties; pvs:" + pvs.getPropertyValues().length + ";", bean, beanName);
        return null;
    }

    /**
     * 
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
     */
    @Nullable
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        this.print("12.", "postProcessBeforeInitialization;", bean, beanName);
        return bean;
    }

    /**
     * 
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
     */
    @Nullable
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        this.print("13.", "postProcessAfterInitialization;", bean, beanName);
        return bean;
    }

    /**
     * 
     * @throws Exception
     * @see org.springframework.beans.factory.DisposableBean#destroy()
     */
    @Override
    public void destroy() throws Exception {
        this.print("14.", "destroy: " + this.hashCode(), null, allPrintName);
    }

    /**
     * 
     * @param registry
     * @throws BeansException
     * @see org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry(org.springframework.beans.factory.support.BeanDefinitionRegistry)
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // TODO Auto-generated method stub
        this.print(" 7.", "postProcessBeanDefinitionRegistry；registry：" + registry.hashCode(), null, allPrintName);
    }

}
