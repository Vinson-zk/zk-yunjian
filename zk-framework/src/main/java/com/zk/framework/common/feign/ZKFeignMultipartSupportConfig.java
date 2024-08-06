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
* @Title: ZKFeignMultipartSupportConfig.java 
* @author Vinson 
* @Package com.zk.framework.common.feign 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 15, 2024 2:23:45 AM 
* @version V1.0 
*/
package com.zk.framework.common.feign;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;

import com.zk.framework.common.feign.support.ZKFeignSpringFormEncoder;

/** 
* @ClassName: ZKFeignMultipartSupportConfig 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKFeignMultipartSupportConfig {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    ZKFeignSpringFormEncoder feignFormEncoder() {
        ZKFeignSpringFormEncoder feignSpringFormEncoder = new ZKFeignSpringFormEncoder(
                new SpringEncoder(messageConverters));
        return feignSpringFormEncoder;
    }

}
