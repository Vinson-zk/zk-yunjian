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
* @Title: ZKConfigAutoOne.java 
* @author Vinson 
* @Package com.zk.demo.web.context.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 11, 2023 3:34:01 PM 
* @version V1.0 
*/
package com.zk.demo.web.context.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;

import com.zk.demo.web.context.ZKDemoContextMain.Print;

/**
 * 默认顺序是：ZKConfigAutoOne，ZKConfigAutoTwo
 * 
 * @ClassName: ZKConfigAutoOne
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
// @AutoConfigureBefore @AutoConfigureAfter 作用优先级比 @AutoConfigureOrder 高
@AutoConfigureOrder(value = -2) // 默认是 0
@AutoConfigureAfter(value = { ZKConfigAutoTwo.class })
public class ZKConfigAutoOne {

	public ZKConfigAutoOne() {
        System.out.println(Print.config + "[]" + this.getClass().getSimpleName() + "【构造函数】" + this.hashCode());
    }

    public ZKConfigAutoOne(String s) {
    }

    @Bean
    public ZKConfigAutoOne zkConfigAutoOne() {
        System.out.println(Print.config + "[]" + this.getClass().getSimpleName() + "【create Bean】" + this.hashCode());
        return new ZKConfigAutoOne(null);
    }
}
