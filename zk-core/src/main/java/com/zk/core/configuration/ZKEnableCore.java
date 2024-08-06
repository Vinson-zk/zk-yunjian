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
* @Title: ZKEnableCoreConfiguration.java 
* @author Vinson 
* @Package com.zk.core.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 6, 2023 2:18:18 PM 
* @version V1.0 
*/
package com.zk.core.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.AbstractResourceBasedMessageSource;

import com.zk.core.commons.ZKEnvironment;
import com.zk.core.configuration.ZKCoreConfiguration.ZKCoreAutoConfigutation;
import com.zk.core.configuration.ZKEnableCore.ZKCoreInit;
import com.zk.core.utils.ZKLocaleUtils;

/**
 * 需要注入 messageSource
 * 
 * @ClassName: ZKEnableCoreConfiguration
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(value = { ZKCoreConfiguration.class })
@ImportAutoConfiguration(value = { ZKCoreAutoConfigutation.class, ZKCoreInit.class })
public @interface ZKEnableCore {

    public static final String printLog = "[^_^:20230209-2148-003] ----- zk-core config: ";

    public class ZKCoreInit {

        // @Autowired
        // ApplicationContext applicationContext;

        // @PostConstruct // 方法在 @Autowired before 后执行
        // public void postConstruct() {
        // // 方法在 @Autowired before 方法后执行
        //// System.out.println("[^_^:20210210-2154-001] ===== ZKCoreConfiguration class postConstruct ");
        //// System.out.println("[^_^:20210210-2154-001] ----- ZKCoreConfiguration class postConstruct ");
        // }

//        @Autowired
        public ZKCoreInit(ZKEnvironment zkEnvironment, AbstractResourceBasedMessageSource messageSource) {
            System.out.println(printLog + "init [" + this.getClass().getSimpleName() + ":" + this.hashCode()
                    + "] ==================================");
            // # 默认语言；注意这里不影响到 localeResolver 的默认语言
            ZKLocaleUtils.setLocale(ZKLocaleUtils.distributeLocale(zkEnvironment.getString("zk.default.locale", "zh_CN")));
            System.out.println(printLog + "zk.default.locale: "+ zkEnvironment.getString("zk.default.locale", "zh_CN"));
            System.out.println(printLog + "messageSource.addBasenames... [" + this.getClass().getSimpleName() + "] " + this.hashCode());
            messageSource.addBasenames("msg/zkMsg_core");

            System.out.println(printLog + "init [" + this.getClass().getSimpleName() + ":" + this.hashCode()
                    + "] ----------------------------------");
        }

    }

}
