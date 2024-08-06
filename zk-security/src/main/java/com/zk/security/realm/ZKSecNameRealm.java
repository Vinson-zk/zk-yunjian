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
* @Title: ZKSecNameRealm.java 
* @author Vinson 
* @Package com.zk.security.realm 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 11:36:31 PM 
* @version V1.0 
*/
package com.zk.security.realm;

/** 
* @ClassName: ZKSecNameRealm 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecNameRealm implements ZKSecRealm {

    /**
     * 域名称
     */
    private String realmName;

    public String getRealmName() {
        if (realmName == null) {
            return this.getClass().getName();
        }
        return realmName;
    }

    public ZKSecNameRealm(String realmName) {
        this.realmName = realmName;
    }

}
