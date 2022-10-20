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
* @Title: ZKSecDefaultUserPrincipal.java 
* @author Vinson 
* @Package com.zk.security.principal 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 14, 2021 7:10:48 PM 
* @version V1.0 
*/
package com.zk.security.principal;
/** 
* @ClassName: ZKSecDefaultUserPrincipal 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecDefaultUserPrincipal<ID> extends ZKSecGroupAbstractPrincipal<ID> implements ZKSecUserPrincipal<ID> {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 8880963427437366589L;

    public ZKSecDefaultUserPrincipal(ID pkId, String username, String name, long osType, String udid, long appType,
            String appId, String groupCode, ID companyId, String companyCode) {
        super(pkId, ZKSecPrincipal.KeyType.User, osType, udid, appType, appId, groupCode, companyId, companyCode);
        this.setPrimary(true);
        this.username = username;
        this.name = name;
    }

    // 登录名
    private String username;

    // 姓名
    private String name;

    /**
     * 登录名
     * 
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * 姓名
     * 
     * @return
     */
    public String getName() {
        return name;
    }

}
