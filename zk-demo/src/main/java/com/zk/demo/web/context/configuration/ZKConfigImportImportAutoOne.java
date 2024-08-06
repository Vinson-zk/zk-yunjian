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
* @Title: ZKConfigImportImportAutoOne.java 
* @author Vinson 
* @Package com.zk.demo.web.context.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 11, 2023 8:58:16 PM 
* @version V1.0 
*/
package com.zk.demo.web.context.configuration;

import org.springframework.context.annotation.Bean;

import com.zk.demo.web.context.ZKDemoContextMain.Print;

/** 
* @ClassName: ZKConfigImportImportAutoOne 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKConfigImportImportAutoOne {

	public ZKConfigImportImportAutoOne() {
        System.out.println(
                Print.config + "[@Import@ImportAuto]" + this.getClass().getSimpleName() + "【构造函数】" + this.hashCode());
    }

    public ZKConfigImportImportAutoOne(String s) {
    }

    @Bean
    public ZKConfigImportImportAutoOne zkConfigImportImportAutoOne() {
        System.out.println(Print.config + "[@Import@ImportAuto]" + this.getClass().getSimpleName() + "【create Bean】"
                + this.hashCode());
        return new ZKConfigImportImportAutoOne(null);
    }

}
