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
* @Title: ZKConfigConfigurationOne.java 
* @author Vinson 
* @Package com.zk.demo.web.context.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 11, 2023 3:37:21 PM 
* @version V1.0 
*/
package com.zk.demo.web.context.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zk.demo.web.context.ZKDemoContextMain.Print;

/** 
* @ClassName: ZKConfigConfigurationOne 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Configuration
public class ZKConfigConfigurationOne {

	public ZKConfigConfigurationOne() {
        System.out.println(
                Print.config + "[@Configuration]" + this.getClass().getSimpleName() + "【构造函数】" + this.hashCode());
    }

    public ZKConfigConfigurationOne(String s) {
    }

    @Bean
    public ZKConfigConfigurationOne zkConfigConfigurationOne() {
        System.out.println(Print.config + "[@Configuration]" + this.getClass().getSimpleName() + "【create Bean】"
                + this.hashCode());
        return new ZKConfigConfigurationOne(null);
    }

    @Configuration
    public static class ZKConfigConfigurationInner {

        public ZKConfigConfigurationInner() {
            System.out.println(Print.config + "[@Configuration.@Configuration]" + this.getClass().getSimpleName()
                    + "【构造函数】" + this.hashCode());
        }

        public ZKConfigConfigurationInner(String s) {
        }

        @Bean
        public ZKConfigConfigurationInner zkConfigConfigurationInner() {
            System.out
                    .println(Print.config + "[@Configuration.@Configuration]" + this.getClass().getSimpleName()
                            + "【create Bean】" + this.hashCode());
            return new ZKConfigConfigurationInner(null);
        }

    }

}
