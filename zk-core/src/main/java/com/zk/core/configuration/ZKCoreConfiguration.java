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
* @Title: ZKCoreConfiguration.java 
* @author Vinson 
* @Package com.zk.core.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 10, 2023 1:36:53 PM 
* @version V1.0 
*/
package com.zk.core.configuration;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractResourceBasedMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.zk.core.commons.ZKEnvironment;
import com.zk.core.commons.ZKJsonObjectMapper;
import com.zk.core.utils.ZKEnvironmentUtils;

/**
 * 
 * @ClassName: ZKCoreConfiguration
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKCoreConfiguration {

    @Bean(value = { "zkEnv", "zkEnvironment" })
    ZKEnvironment zkEnvironment(ApplicationContext applicationContext) {
        System.out.println(ZKEnableCore.printLog + "zkEnvironment --- [" + this.getClass().getSimpleName() + "] " + this.hashCode());
        ZKEnvironmentUtils.setZKEnvironment(new ZKEnvironment(applicationContext));
        return ZKEnvironmentUtils.getZKEnvironment();
    }

    public static class ZKCoreAutoConfigutation {

        @ConditionalOnMissingBean(value = { AbstractResourceBasedMessageSource.class })
        @Bean
        ResourceBundleMessageSource messageSource() {
            System.out.println(ZKEnableCore.printLog + "ResourceBundleMessageSource --- [" + this.getClass().getSimpleName() + "] " + this.hashCode());
            ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
            // 文件名不能包含 [.]，相同 key 配置在前面的生效，后配置的不会覆盖前面的
            messageSource.addBasenames("msg/zkMsg_core");
            messageSource.setUseCodeAsDefaultMessage(true);
            messageSource.setCacheSeconds(3600);
            messageSource.setDefaultEncoding("UTF-8");
            return messageSource;
        }

        /**
         * 传送数据适配处理器
         *
         * @Title: messageConverters
         * @Description: TODO(simple description this method what to do.)
         * @author Vinson
         * @date Feb 6, 2023 5:26:06 PM
         * @return
         * @return HttpMessageConverters
         */
        @ConditionalOnMissingBean(value = { HttpMessageConverters.class })
        @Bean("messageConverters")
        HttpMessageConverters messageConverters() {
            System.out.println(ZKEnableCore.printLog + "messageConverters --- [" + this.getClass().getSimpleName() + "] " + this.hashCode());

            StringHttpMessageConverter stringMessageConverter = new StringHttpMessageConverter();
            stringMessageConverter.setWriteAcceptCharset(false);
            MediaType mediaType = new MediaType("text", "plain", Charset.forName("UTF-8"));
            List<MediaType> mts = Arrays.asList(mediaType, MediaType.valueOf("*/*;charset=UTF-8"),
                    MediaType.valueOf("*/*"));
            stringMessageConverter.setSupportedMediaTypes(mts);

            MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
            mts = Arrays.asList(MediaType.valueOf("application/json;charset=UTF-8"),
                    new MediaType("text", "plain", Charset.forName("UTF-8")),
                    MediaType.valueOf("text/javascript;charset=UTF-8"), MediaType.valueOf("text/html;charset=UTF-8"));
            jackson2HttpMessageConverter.setSupportedMediaTypes(mts);
            jackson2HttpMessageConverter.setPrettyPrint(false);
            jackson2HttpMessageConverter.setObjectMapper(new ZKJsonObjectMapper());

            HttpMessageConverters messageConverters = new HttpMessageConverters(stringMessageConverter,
                    jackson2HttpMessageConverter);
            return messageConverters;
        }
    }

}
