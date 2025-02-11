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
* @Title: ZKEnableWebmvc.java 
* @author Vinson 
* @Package com.zk.webmvc.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 6, 2023 3:55:11 PM 
* @version V1.0 
*/
package com.zk.webmvc.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.catalina.Context;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.zk.core.commons.ZKEnvironment;
import com.zk.webmvc.configuration.ZKEnableWebmvc.ZKWebmvcConfig;
import com.zk.webmvc.configuration.ZKEnableWebmvc.ZKWebmvcInit;
import com.zk.webmvc.tomcat.catalina.ZKWebmvcLifecycleListener;

/** 
* @ClassName: ZKEnableWebmvc 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@AutoConfigureBefore(value = { ServletWebServerFactoryAutoConfiguration.class })
@ImportAutoConfiguration(value = { ZKWebmvcConfiguration.class, ZKWebmvcInit.class })
@Import(value = { ZKWebmvcConfig.class })
public @interface ZKEnableWebmvc {

    public static final String printLog = "[^_^:20230209-2148-007] ----- zk-webmvc config: ";

    public class ZKWebmvcInit {

//        @DependsOn(value = { "localeResolver", "requestMappingHandlerAdapter" })
//        @Autowired
        public ZKWebmvcInit(RequestMappingHandlerAdapter requestMappingHandlerAdapter, ZKEnvironment zkEnv) {
            System.out.println(printLog + "init [" + this.getClass().getSimpleName() + ":" + this.hashCode()
                    + "] =================================");

            // 设置下 RequestMappingHandlerAdapter 的 ignoreDefaultModelOnRedirect=true,
            // 这样可以提高效率，避免不必要的检索。spring 6.0 开始弃用
//            requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);

            System.out.println(printLog + "init [" + this.getClass().getSimpleName() + ":" + this.hashCode()
                    + "] ---------------------------------");
        }
    }
    
    public static class ZKWebmvcConfig {

//        @Value("${zk.servlet.context.resource.sets}")
//        String[] resourceSets;

        @Bean
        ZKWebmvcLifecycleListener zkWebmvcLifecycleListener() {
            ZKWebmvcLifecycleListener zkWebmvcLifecycleListener = new ZKWebmvcLifecycleListener();
//            ZKWebmvcLifecycleListener zkWebmvcLifecycleListener = new ZKWebmvcLifecycleListener(this.resourceSets);
            return zkWebmvcLifecycleListener;
        }

        @Bean
        TomcatServletWebServerFactory tomcatServletWebServerFactory(
                ObjectProvider<TomcatConnectorCustomizer> connectorCustomizers,
                ObjectProvider<TomcatContextCustomizer> contextCustomizers,
                ObjectProvider<TomcatProtocolHandlerCustomizer<?>> protocolHandlerCustomizers,
                ZKWebmvcLifecycleListener zkWebmvcLifecycleListener) {
            TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory() {
                @Override
                protected void postProcessContext(Context context) {
                    context.addLifecycleListener(zkWebmvcLifecycleListener);
                }
            };

            factory.getTomcatConnectorCustomizers().addAll(connectorCustomizers.orderedStream().toList());
            factory.getTomcatContextCustomizers().addAll(contextCustomizers.orderedStream().toList());
            factory.getTomcatProtocolHandlerCustomizers().addAll(protocolHandlerCustomizers.orderedStream().toList());
            return factory;
        }

//        @Bean
        ConfigurableServletWebServerFactory webServerFactory() {
            TomcatServletWebServerFactory tomcatFactory = null;
            tomcatFactory = new TomcatServletWebServerFactory();

//          factory.setPort(9000);
////          factory.setSessionTimeout(10, TimeUnit.MINUTES);
//          factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/notfound.html"));

            /*** 下面是 嵌入式Servlet容器的一些其他配置代码 ***/
            /*** 1 ***/
//            tomcatFactory.addAdditionalTomcatConnectors(createSslConnector());
            /*** 2 ***/
//          return new TomcatServletWebServerFactory() {
            //
//              @Override
//              protected void prepareContext(Host host, ServletContextInitializer[] initializers) {
//                  super.prepareContext(host, initializers);
//                  StandardContext child = new StandardContext();
//                  child.addLifecycleListener(new Tomcat.FixContextListener());
//                  child.setPath("/cloudfoundryapplication");
//                  ServletContainerInitializer initializer = getServletContextInitializer(getContextPath());
//                  child.addServletContainerInitializer(initializer, Collections.emptySet());
//                  child.setCrossContext(true);
//                  host.addChild(child);
//              }
//          };

            return tomcatFactory;

        }

    }

}
