/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKPrincipal.java 
 * @author Vinson 
 * @Package com.zk.server.central.security 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:29:27 PM 
 * @version V1.0   
*/
package com.zk.server.central.security;

import java.io.Serializable;

/** 
* @ClassName: ZKPrincipal 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKPrincipal implements Serializable {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    private String account;

    public ZKPrincipal(String account) {
        this.account = account;
    }

    /**
     * @return account
     */
    public String getAccount() {
        return account;
    }

}
