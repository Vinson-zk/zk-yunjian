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
* @Title: ZKConfigImportOne.java 
* @author Vinson 
* @Package com.zk.demo.web.context.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 11, 2023 3:32:42 PM 
* @version V1.0 
*/
package com.zk.demo.web.context.configuration;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.zk.demo.web.context.ZKDemoContextMain.Print;

/** 
* @ClassName: ZKConfigImportOne 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Import(value = { ZKConfigImportImportOne.class })
@ImportAutoConfiguration(value = { ZKConfigImportImportAutoOne.class })
public class ZKConfigImportOne {

	public ZKConfigImportOne() {
        System.out.println(Print.config + "[@Import]" + this.getClass().getSimpleName() + "【构造函数】" + this.hashCode());
    }

    public ZKConfigImportOne(String s) {
    }

    @Bean
    public ZKConfigImportOne zkConfigImportOne() {
        System.out.println(
                Print.config + "[@Import]" + this.getClass().getSimpleName() + "【create Bean】" + this.hashCode());
        return new ZKConfigImportOne(null);
    }

}
