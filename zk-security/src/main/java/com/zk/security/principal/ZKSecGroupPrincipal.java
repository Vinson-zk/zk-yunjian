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
* @Title: ZKSecGroupPrincipal.java 
* @author Vinson 
* @Package com.zk.security.principal 
* @Description: TODO(simple description this file what to do. ) 
* @date May 13, 2022 5:27:43 PM 
* @version V1.0 
*/
package com.zk.security.principal;
/** 
* @ClassName: ZKSecGroupPrincipal 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecGroupPrincipal<ID> extends ZKSecPrincipal<ID> {

    /**
     * 集团代码
     */
    public String getGroupCode();

    /**
     * 公司ID
     */
    public ID getCompanyId();

    /**
     * 公司代码
     */
    public String getCompanyCode();

}

