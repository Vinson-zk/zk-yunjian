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
* @Title: ClassDemo.java 
* @author Vinson 
* @Package com.zk.demo.instance 
* @Description: TODO(simple description this file what to do. ) 
* @date Sep 10, 2020 9:11:03 AM 
* @version V1.0 
*/
package com.zk.demo.instance;
/** 
* @ClassName: ClassDemo 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ClassDemo {

    private String name;

    public ClassDemo(String name) {
        this.name = name;
    }

    /**
     * @return name sa
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
