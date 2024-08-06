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
* @Title: ZKConfigImportAutoImportOne.java 
* @author Vinson 
* @Package com.zk.demo.web.context.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 12, 2023 12:26:41 AM 
* @version V1.0 
*/
package com.zk.demo.web.context.configuration;

import org.springframework.context.annotation.Bean;

import com.zk.demo.web.context.ZKDemoContextMain.Print;

/** 
* @ClassName: ZKConfigImportAutoImportOne 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKConfigImportAutoImportOne {

    public ZKConfigImportAutoImportOne() {
        System.out.println(
                Print.config + "[@ImportAuto@Import]" + this.getClass().getSimpleName() + "【构造函数】" + this.hashCode());
    }

    public ZKConfigImportAutoImportOne(String s) {
    }

    @Bean
    public ZKConfigImportAutoImportOne zkConfigImportAutoImportOne() {
        System.out.println(Print.config + "[@ImportAuto@Import]" + this.getClass().getSimpleName() + "【create Bean】"
                + this.hashCode());
        return new ZKConfigImportAutoImportOne(null);
    }

}
