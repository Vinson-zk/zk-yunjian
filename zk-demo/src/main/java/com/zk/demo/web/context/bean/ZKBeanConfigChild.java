/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKBeanConfigChild.java 
* @author Vinson 
* @Package com.zk.demo.web.context.bean 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 25, 2024 11:04:37 PM 
* @version V1.0 
*/
package com.zk.demo.web.context.bean;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/** 
* @ClassName: ZKBeanConfigChild 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@PropertySources(value = { //
        @PropertySource(encoding = "UTF-8", value = { "context/zk.demo.bean.child.properties" }), //
})
public class ZKBeanConfigChild {

}
