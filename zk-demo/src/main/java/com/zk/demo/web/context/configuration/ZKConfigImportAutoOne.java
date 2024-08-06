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
* @Title: ZKConfigImportAutoOne.java 
* @author Vinson 
* @Package com.zk.demo.web.context.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 11, 2023 4:19:08 PM 
* @version V1.0 
*/
package com.zk.demo.web.context.configuration;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.zk.demo.web.context.ZKDemoContextMain.Print;

/** 
* @ClassName: ZKConfigImportAutoOne 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Import(value = { ZKConfigImportAutoImportOne.class })
@ImportAutoConfiguration(value = { ZKConfigImportAutoImportAutoOne.class })
public class ZKConfigImportAutoOne {
	
    public ZKConfigImportAutoOne() {
        System.out
                .println(Print.config + "[@ImportAuto]" + this.getClass().getSimpleName() + "【构造函数】" + this.hashCode());
    }

    public ZKConfigImportAutoOne(String s) {
    }

    @Bean
    public ZKConfigImportAutoOne zkConfigImportAutoOne() {
        System.out.println(
                Print.config + "[@ImportAuto]" + this.getClass().getSimpleName() + "【create Bean】" + this.hashCode());
        return new ZKConfigImportAutoOne(null);
    }
}
