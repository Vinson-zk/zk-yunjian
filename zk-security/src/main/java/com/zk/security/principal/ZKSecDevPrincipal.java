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
* @Title: ZKSecDevPrincipal.java 
* @author Vinson 
* @Package com.zk.security.principal 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 11:21:39 PM 
* @version V1.0 
*/
package com.zk.security.principal;

/**
 * 开发者身份
 * 
 * @ClassName: ZKSecDevPrincipal
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public interface ZKSecDevPrincipal<ID> extends ZKSecGroupPrincipal<ID> {

    /**
     * 应用ID
     */
    public String getDevId();

    /**
     * 第三方扩展标识
     * 
     * @return
     */
    public String getThirdPartyId();

}
