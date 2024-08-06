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
* @Title: ZKShiroPrincipal.java 
* @author Vinson 
* @Package com.zk.demo.shiro.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 29, 2021 6:51:44 PM 
* @version V1.0 
*/
package com.zk.demo.shiro.common;

import java.io.Serializable;

/** 
* @ClassName: ZKShiroPrincipal 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKShiroPrincipal implements Serializable {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    String name;

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
