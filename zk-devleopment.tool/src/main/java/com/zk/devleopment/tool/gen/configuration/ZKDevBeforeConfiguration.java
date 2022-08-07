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
* @Title: ZKCodeGenConfiguration.java 
* @author Vinson 
* @Package com.zk.code.generate.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 29, 2021 7:09:04 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.configuration;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.MutableDiscoveryClientOptionalArgs;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.alibaba.druid.pool.DruidDataSource;
import com.zk.core.commons.ZKValidatorMessageInterpolator;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.core.web.filter.ZKCrosFilter;
import com.zk.core.web.resolver.ZKExceptionHandlerResolver;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.db.dynamic.spring.dataSource.ZKDynamicDataSource;
import com.zk.db.dynamic.spring.transaction.ZKDynamicTransactionManager;
import com.zk.framework.serCen.ZKSerCenEncrypt;
import com.zk.framework.serCen.eureka.ZKEurekaTransportClientFactories;
import com.zk.framework.serCen.support.ZKSerCenSampleCipher;
import com.zk.log.interceptor.ZKLogAccessInterceptor;

/** 
* @ClassName: ZKCodeGenConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Configuration
@ImportResource(locations = { "classpath:xmlConfig/spring_ctx_application.xml",
        "classpath:xmlConfig/spring_ctx_devleopment_tool_application.xml", "classpath:xmlConfig/spring_ctx_mvc.xml",
        "classpath:xmlConfig/spring_ctx_dynamic_mybatis.xml" })
//@ImportAutoConfiguration(classes = { ZKMongoAutoConfiguration.class })
@AutoConfigureBefore(value = { 
//        ZKMongoAutoConfiguration.class, 
        ZKCodeGenAfterConfiguration.class,
        ZKCodeGenShiroConfiguration.class,
        EnableWebMvcConfiguration.class, 
        ServletWebServerFactoryAutoConfiguration.class
        })
public class ZKCodeGenConfiguration {

    @Value("${zk.sys.db.dynamic.jdbc.username_w}")
    private String dbUserName_w;

    @Value("${zk.sys.db.dynamic.jdbc.password_w}")
    private String dbPwd_w;

    @Value("${zk.sys.db.dynamic.jdbc.username_r}")
    private String dbUserName_r;

    @Value("${zk.sys.db.dynamic.jdbc.password_r}")
    private String dbPwd_r;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    ConfigurationPropertiesBindingPostProcessor configurationPropertiesBinder;

    @PostConstruct
    public void postConstruct() {
        // 方法在 @Autowired before 后执行
//        System.out.println("[^_^:20191219-2154-001] ===== ZKSerCenConfiguration class postConstruct ");
//        System.out.println("[^_^:20191219-2154-001] ----- ZKSerCenConfiguration class postConstruct ");
    }

    @Autowired
    public void before(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        System.out.println("[^_^:20200805-1808-001] -------- configuration before begin... ... " + this.getClass());

        ZKEnvironmentUtils.initContext(applicationContext);
//        ZKLocaleUtils.setLocale(ZKLocaleUtils.valueOf("en_US"));
//        ZKLocaleUtils.setLocale(ZKLocaleUtils.valueOf("zh_CN"));
//        // # 默认语言；注意这里不影响到 localeResolver 的默认语言
        ZKWebUtils.setLocale(
				ZKLocaleUtils.distributeLocale(ZKEnvironmentUtils
						.getString("zk.default.locale", "zh_CN")));

        // 设置下 RequestMappingHandlerAdapter 的 ignoreDefaultModelOnRedirect=true,
        // 这样可以提高效率，避免不必要的检索。
        requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
        System.out.println("[^_^:20200805-1808-001] -------- configuration before end______ " + this.getClass());
    }

    @Bean
    public FilterRegistrationBean<Filter> zkCrosFilterRegistrationBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
        ZKCrosFilter zkCrosFilter = new ZKCrosFilter();
        filterRegistrationBean.setFilter(zkCrosFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("zkCrosFilter");
//        Map<String, String> zkCrosFilterInitParams = new HashMap<>();
//        zkCrosFilterInitParams.put(ParamsName.allowOrigin, "*");
//        zkCrosFilterInitParams.put(ParamsName.maxAge, "3600");
//        zkCrosFilterInitParams.put(ParamsName.allowMethods, "POST,GET");
//        zkCrosFilterInitParams.put(ParamsName.allowHeaders, "__SID,locale,Lang,X-Requested-With");
//        filterRegistrationBean.setInitParameters(zkCrosFilterInitParams);
        return filterRegistrationBean;
    }

//    /**
//     * mongo 属性配置
//     *
//     * @Title: zkMongoProperties
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date Oct 28, 2019 3:02:22 PM
//     * @return
//     * @return ZKMongoProperties
//     */
//    @Bean
//    @ConfigurationProperties(prefix = "zk.sys.mongodb")
//    public ZKMongoProperties zkMongoProperties() {
//        return new ZKMongoProperties();
//    }

    /**
     * 数据源
     *
     * @Title: parentDataSource
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Oct 28, 2019 3:02:07 PM
     * @return
     * @return DruidDataSource
     */
//    @Primary
    @Bean("parentDataSource")
    @ConfigurationProperties(prefix = "zk.sys.db.dynamic.jdbc.druid.pool")
    public DruidDataSource parentDataSource() {
        return new DruidDataSource();
    }

    // 动态数据源
    @Bean("zkDynamicDataSource")
    public ZKDynamicDataSource zkDynamicDataSource() {

        ZKDynamicDataSource zkDynamicDataSource = new ZKDynamicDataSource();

        DruidDataSource dds_w = new DruidDataSource();
        DruidDataSource dds_r = new DruidDataSource();

        configurationPropertiesBinder.postProcessBeforeInitialization(dds_w, "parentDataSource");
        configurationPropertiesBinder.postProcessBeforeInitialization(dds_r, "parentDataSource");

        dds_w.setUsername(this.dbUserName_w);
        dds_w.setPassword(dbPwd_w);

        dds_r.setUsername(this.dbUserName_r);
        dds_r.setPassword(dbPwd_r);

        zkDynamicDataSource.setWriteDataSource(dds_w);
        zkDynamicDataSource.setReadDataSource(dds_r);

        return zkDynamicDataSource;
    }

    /**
     * 动态数据源事务
     *
     * @Title: dynamicTransactionManager
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Oct 28, 2019 3:02:16 PM
     * @param zkDynamicDataSource
     * @return
     * @return ZKDynamicTransactionManager
     */
    @Bean("dynamicTransactionManager")
    public ZKDynamicTransactionManager dynamicTransactionManager(ZKDynamicDataSource zkDynamicDataSource) {
        ZKDynamicTransactionManager zkDynamicTransactionManager = new ZKDynamicTransactionManager();
        zkDynamicTransactionManager.setDataSource(zkDynamicDataSource);
        return zkDynamicTransactionManager;
    }

    /**
     * 数据验证消息处理
     *
     * @Title: validator
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Oct 28, 2019 3:02:00 PM
     * @param messageSource
     * @param applicationContext
     * @return
     * @return Validator
     */
    @Bean
    public Validator validator(MessageSource messageSource, ApplicationContext applicationContext) {

        /*
         * 重写这个方法比较好
         * 
         * 重写这个类 org.springframework.validation.beanvalidation.
         * MessageSourceResourceBundleLocator 里的，这个类
         * org.springframework.context.support.MessageSourceResourceBundle.
         * MessageSourceResourceBundle(MessageSource source, Locale locale) 里的
         * handleGetObject 和 containsKey 这个方法比较好；
         * 
         * return new ResourceBundleMessageInterpolator(new
         * MessageSourceResourceBundleLocator(messageSource));
         */
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        ZKValidatorMessageInterpolator zkValidatorMessageInterpolator = new ZKValidatorMessageInterpolator(
                messageSource);
        localValidatorFactoryBean.setMessageInterpolator(zkValidatorMessageInterpolator);
        // localValidatorFactoryBean.setValidationMessageSource(messageSource);
        return localValidatorFactoryBean;
    }

//    // 创建 RestTemplate
//    @DependsOn(value = { "messageConverters" })
//    @Bean
////    @LoadBalanced
//    public RestTemplate restTemplate(HttpMessageConverters messageConverters) {
//        return new RestTemplate(messageConverters.getConverters());
//    }

    // 异常处理 xml 中定义
//  @Bean
//  public ZKViewExceptionHandlerResolver exceptionResolver() {
//      return new ZKViewExceptionHandlerResolver();
//  }

//    @Bean
//    @DependsOn(value = { "messageConverters" })
//    public RequestMappingHandlerAdapter requestMappingHandlerAdapter(HttpMessageConverters messageConverters) {
//        RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
//        // session 线程安全配置
//        requestMappingHandlerAdapter.setSynchronizeOnSession(
//                ZKWebUtils.isTrue(ZKEnvironmentUtils.getString("zk.core.web.mvc.synchronizeOnSession", "true")));
//        // 一定要在这些设置 HttpMessageConverters 因为其他需要属性需要用到这个属性；
//        requestMappingHandlerAdapter.setMessageConverters(messageConverters.getConverters());
//        return requestMappingHandlerAdapter;
//    }

    /**
     * 服务注册加解密
     *
     * @Title: zkSerCenSampleCipher
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 6, 2020 6:07:27 PM
     * @return
     * @return ZKSerCenSampleCipher
     */
    @Bean
    public ZKSerCenEncrypt zkSerCenEncrypt() {
        return new ZKSerCenSampleCipher();
    }

//    @Bean
//    public ZKSerCenDecode zkSerCenDecode() {
//        return new ZKSerCenSampleCipher();
//    }

    @Bean
//    @ConditionalOnBean(name = "eurekaDiscoverClientMarker")
    @ConditionalOnClass(name = "com.sun.jersey.api.client.filter.ClientFilter")
//    @ConditionalOnMissingBean(value = AbstractDiscoveryClientOptionalArgs.class, search = SearchStrategy.CURRENT)
    public MutableDiscoveryClientOptionalArgs discoveryClientOptionalArgs(ZKSerCenEncrypt zkSerCenEncrypt) {
        MutableDiscoveryClientOptionalArgs ms = new MutableDiscoveryClientOptionalArgs();
        ms.setTransportClientFactories(new ZKEurekaTransportClientFactories(zkSerCenEncrypt));
        return ms;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 异常处理适配器
     *
     * @Title: zkExceptionHandlerResolver
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 21, 2020 11:13:24 AM
     * @return
     * @return ZKExceptionHandlerResolver
     */
    @Bean
    public ZKExceptionHandlerResolver zkExceptionHandlerResolver() {
        return new ZKExceptionHandlerResolver();
    }

    /**
     * 记录日志拦截器
     *
     * @Title: logAccessInterceptor
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 14, 2022 11:59:49 AM
     * @return
     * @return ZKLogAccessInterceptor
     */
    @Bean
    public ZKLogAccessInterceptor logAccessInterceptor() {
        return new ZKLogAccessInterceptor();
    }

}
