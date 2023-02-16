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
* @Title: ZKConfigConfigurationImportTwo.java 
* @author Vinson 
* @Package com.zk.demo.context.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 11, 2023 8:57:14 PM 
* @version V1.0 
*/
package com.zk.demo.context.configuration;

import org.springframework.context.annotation.Bean;

import com.zk.demo.context.ZKDemoContextMain.Print;

/** 
* @ClassName: ZKConfigConfigurationImportTwo 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKConfigConfigurationImportTwo {

	public ZKConfigConfigurationImportTwo() {
        System.out.println(Print.config + this.getClass().getSimpleName() + "【构造函数】" + this.hashCode());
    }

    public ZKConfigConfigurationImportTwo(String s) {
    }

    @Bean
    public ZKConfigConfigurationImportTwo zkConfigConfigurationImportTwo() {
        System.out.println(Print.config + this.getClass().getSimpleName() + "【create Bean】" + this.hashCode());
        return new ZKConfigConfigurationImportTwo(null);
    }

}
