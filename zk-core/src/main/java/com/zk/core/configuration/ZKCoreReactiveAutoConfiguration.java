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
* @Title: ZKCoreReactiveAutoConfiguration.java 
* @author Vinson 
* @Package com.zk.core.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 24, 2024 7:08:24 PM 
* @version V1.0 
*/
package com.zk.core.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import com.zk.core.web.ZKWebConstants.ZKFilterLevel;
import com.zk.core.web.support.webFlux.filter.ZKExceptionHandlerWebFilter;

/**
 * @ClassName: ZKCoreReactiveAutoConfiguration
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKCoreReactiveAutoConfiguration {

//    @ConditionalOnMissingFilterBean(value = ZKExceptionHandlerWebFilter.class)
//    @Bean({ "exceptionHandlerWebFilter", "zkExceptionHandlerWebFilter" })
//    public FilterRegistrationBean<Filter> zkExceptionHandlerWebFilter() {
//        System.out.println("[^_^:20230211-1022-001] ----- zkExceptionHandlerFilter 配置 异常处理拦截器 Filter --- ["
//                + this.getClass().getSimpleName() + "] " + this.hashCode());
//        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
//        // 访问日志记录 过虑器
//        filterRegistrationBean.setFilter(new ZKExceptionHandlerWebFilter());
//        filterRegistrationBean.setOrder(ZKFilterLevel.Exception.HIGHEST);
//        filterRegistrationBean.addUrlPatterns("/*");
//        filterRegistrationBean.setName("zkExceptionHandlerFilter");
//        return filterRegistrationBean;
//    }

    @Order(ZKFilterLevel.Exception.HIGHEST)
    @ConditionalOnMissingBean(value = ZKExceptionHandlerWebFilter.class)
    @Bean({ "exceptionHandlerFilter", "fluxExceptionFilter", "zkFluxExceptionFilter" })
    ZKExceptionHandlerWebFilter zkFluxExceptionFilter() {
        ZKExceptionHandlerWebFilter filter = new ZKExceptionHandlerWebFilter();
        filter.setOrder(ZKFilterLevel.Exception.HIGHEST);
        return filter;
    }

}
