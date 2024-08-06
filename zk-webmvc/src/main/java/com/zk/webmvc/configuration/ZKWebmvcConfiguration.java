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
* @Title: ZKWebmvcConfiguration.java 
* @author Vinson 
* @Package com.zk.webmvc.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 10, 2023 2:00:53 PM 
* @version V1.0 
*/
package com.zk.webmvc.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;

import com.zk.core.commons.ZKEnvironment;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.webmvc.resolver.ZKSessionLocaleResolver;

/** RequestMappingHandlerAdapter
* @ClassName: ZKWebmvcConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@AutoConfigureBefore(value = { //
        WebMvcAutoConfiguration.class //
})
public class ZKWebmvcConfiguration {

    /**
     * 国际化语言适配器
     *
     * @Title: localeResolver
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 6, 2023 5:28:18 PM
     * @return
     * @return ZKSessionLocaleResolver
     */
    @ConditionalOnMissingBean(value = { LocaleResolver.class })
    @Bean(value = { "localeResolver", "zkLocaleResolver" })
    ZKSessionLocaleResolver localeResolver(ZKEnvironment zkEnv) {
        System.out.println(ZKEnableWebmvc.printLog + "localeResolver --- [" + this.getClass().getSimpleName() + "] " + this);
        ZKSessionLocaleResolver localeResolver = new ZKSessionLocaleResolver();
        localeResolver.setDefaultLocale(ZKLocaleUtils.distributeLocale(zkEnv.getString("zk.default.locale", "zh_CN")));
        return localeResolver;
    }

//    /**
//     * 异常处理适配器
//     *
//     * @Title: zkExceptionHandlerResolver
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date Aug 21, 2020 11:13:24 AM
//     * @return
//     * @return ZKExceptionHandlerResolver
//     */
//    @ConditionalOnMissingBean(value = { ZKExceptionHandlerResolver.class })
//    @Bean
//    public ZKExceptionHandlerResolver zkExceptionHandlerResolver() {
//        System.out.println(ZKEnableWebmvc.printLog + "zkExceptionHandlerResolver --- [" + this.getClass().getSimpleName() + "] " + this);
//        return new ZKExceptionHandlerResolver();
//    }


}
