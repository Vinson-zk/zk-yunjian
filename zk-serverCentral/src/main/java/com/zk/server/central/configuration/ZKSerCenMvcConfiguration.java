/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKSerCenMvcConfig.java 
 * @author Vinson 
 * @Package com.zk.server.central.configuration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:10:32 PM 
 * @version V1.0   
*/
package com.zk.server.central.configuration;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** 
* @ClassName: ZKSerCenMvcConfig 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSerCenMvcConfiguration implements WebMvcConfigurer {

//    @Autowired
//    HttpMessageConverters messageConverters;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态文件访问映射；如：将 /static/** 访问映射到 classpath: /mystatic/
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/eureka/**").addResourceLocations("classpath:/static/eureka/");
    }

//    // 国际化语言信息扩展
//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
////        converters.addAll(messageConverters.getConverters());
//    }
//
//    // 国际化语言信息扩展
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.addAll(messageConverters.getConverters());
//    }
}
