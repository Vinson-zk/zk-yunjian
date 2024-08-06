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
* @Title: ZKCoreServletAutoConfiguration.java 
* @author Vinson 
* @Package com.zk.core.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 24, 2024 7:06:47 PM 
* @version V1.0 
*/
package com.zk.core.configuration;

import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.zk.core.web.ZKWebConstants.ZKFilterLevel;
import com.zk.core.web.support.servlet.filter.ZKExceptionHandlerFilter;

import jakarta.servlet.Filter;

/**
 * @ClassName: ZKCoreServletAutoConfiguration
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKCoreServletAutoConfiguration {

    @ConditionalOnMissingFilterBean(value = ZKExceptionHandlerFilter.class)
    @Bean({ "exceptionHandlerFilter", "zkExceptionHandlerFilter" })
    FilterRegistrationBean<Filter> zkExceptionHandlerFilter() {
        System.out.println("[^_^:20230211-1022-001] ----- zkExceptionHandlerFilter 配置 异常处理拦截器 Filter --- ["
                + this.getClass().getSimpleName() + "] " + this.hashCode());
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
        // 访问日志记录 过虑器
        filterRegistrationBean.setFilter(new ZKExceptionHandlerFilter());
        filterRegistrationBean.setOrder(ZKFilterLevel.Exception.HIGHEST);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("zkExceptionHandlerFilter");
        return filterRegistrationBean;
    }

}
