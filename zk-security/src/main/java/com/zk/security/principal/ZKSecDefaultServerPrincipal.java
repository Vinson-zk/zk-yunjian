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
* @Title: ZKSecDefaultServerPrincipal.java 
* @author Vinson 
* @Package com.zk.security.principal 
* @Description: TODO(simple description this file what to do. ) 
* @date May 13, 2022 3:13:05 PM 
* @version V1.0 
*/
package com.zk.security.principal;

/**
 * 微信服务身份
 * 
 * @ClassName: ZKSecDefaultServerPrincipal
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKSecDefaultServerPrincipal<ID> extends ZKSecAbstractPrincipal<ID> {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * Title:
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param pkId
     * @param type
     * @param osType
     * @param udid
     * @param appType
     */
    public ZKSecDefaultServerPrincipal(ID pkId, long osType, String udid, long appType, String appId) {
        super(pkId, ZKSecPrincipal.KeyType.Distributed_server, osType, udid, appType, appId);
    }

}
