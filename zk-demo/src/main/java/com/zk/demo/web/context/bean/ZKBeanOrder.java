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
* @Title: ZKBeanOrder.java 
* @author Vinson 
* @Package com.zk.demo.context.bean 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 8, 2023 4:33:10 PM 
* @version V1.0 
*/
package com.zk.demo.web.context.bean;
/** 
* @ClassName: ZKBeanOrder 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKBeanOrder {

    public String value;

    public ZKBeanOrder(String arg) {
        this.value = this.getClass().getSimpleName() + " " + arg;
        System.out.println("[^_^:20230207-220912-001] " + this.value);
    }

    public String getValue() {
        return this.value;
    }

}
