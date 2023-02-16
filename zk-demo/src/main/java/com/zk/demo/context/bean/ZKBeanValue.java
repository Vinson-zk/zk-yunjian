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
* @Title: ZKBeanValue.java 
* @author Vinson 
* @Package com.zk.demo.context.bean 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 8, 2023 7:58:49 PM 
* @version V1.0 
*/
package com.zk.demo.context.bean;

/** 
* @ClassName: ZKBeanValue 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKBeanValue {

//    @Value("${zk.bean.value}")
    String value;

    public ZKBeanValue() {
    }

    public ZKBeanValue(String value) {
        this.value = value;
    }

    /**
     * @return value sa
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }


}
