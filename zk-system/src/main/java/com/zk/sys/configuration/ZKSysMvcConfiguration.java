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
* @Title: ZKSysMvcConfiguration.java 
* @author Vinson 
* @Package com.zk.sys.configuration 
* @Description: TODO(simple description this file what to do.) 
* @date Jul 16, 2020 3:24:20 PM 
* @version V1.0 
*/
package com.zk.sys.configuration;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zk.webmvc.configuration.ZKEnableWebmvc;

/** 
* @ClassName: ZKSysMvcConfiguration 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@ZKEnableWebmvc
public class ZKSysMvcConfiguration implements WebMvcConfigurer{ 

//    public ZKSysMvcConfiguration() {
//        System.out.println("[^_^:20230211-1022-001] ----- 实例化 -----" + this.getClass().getSimpleName());
//    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // 静态文件访问映射；如：将 /static/** 访问映射到 classpath: /mystatic/
////        registry.addResourceHandler("/**").addResourceLocations("/","classpath:templates/");
////        registry.addResourceHandler("/static/**").addResourceLocations("/static/**", "classpath:/static/");
////        registry.addResourceHandler("/eureka/**").addResourceLocations("classpath:/static/eureka/");
//
////      registry.addResourceHandler("/favicon.ico").addResourceLocations("/");
////        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//    }

}




