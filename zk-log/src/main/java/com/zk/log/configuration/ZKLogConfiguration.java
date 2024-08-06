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
* @Title: ZKLogConfiguration.java 
* @author Vinson 
* @Package com.zk.log.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 10, 2023 1:55:31 PM 
* @version V1.0 
*/
package com.zk.log.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.zk.core.web.ZKWebConstants.ZKFilterLevel;
import com.zk.log.filter.ZKLogAccessFilter;

import jakarta.servlet.Filter;

/** 
* @ClassName: ZKLogConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKLogConfiguration {

    // 访问日志记录 过虑器; 没有 ZKLogAccessCustomConfig bean 时，才注入 logAccessFilter；
    @Bean("logAccessFilterRegistrationBean")
    public FilterRegistrationBean<Filter> logAccessFilterRegistrationBean() {
        System.out.println(ZKEnableLog.printLog + " logAccessFilterRegistrationBean 配置 zk 日志过滤器 Filter --- ["
                + this.getClass().getSimpleName() + "] " + this.hashCode());
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
        // 访问日志记录 过虑器
        ZKLogAccessFilter logAccessFilter = new ZKLogAccessFilter();
        filterRegistrationBean.setFilter(logAccessFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("logAccessFilter");
        filterRegistrationBean.setOrder(ZKFilterLevel.Log.HIGHEST);
        return filterRegistrationBean;
    }

}
