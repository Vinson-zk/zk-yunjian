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
* @Title: ZKSecDefaultDevPrincipal.java 
* @author Vinson 
* @Package com.zk.security.principal 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 11:25:15 PM 
* @version V1.0 
*/
package com.zk.security.principal;
/** 
* @ClassName: ZKSecDefaultDevPrincipal 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecDefaultDevPrincipal<ID> extends ZKSecGroupAbstractPrincipal<ID> implements ZKSecDevPrincipal<ID> {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = -4602060229158707569L;

    public ZKSecDefaultDevPrincipal(ID pkId, String devId, String thirdPartyId, long osType, String udid, long appType,
            String appId, String groupCode, ID companyId, String companyCode) {
        super(pkId, ZKSecPrincipal.KeyType.Developer, osType, udid, appType, appId, groupCode, companyId, companyCode);
        this.devId = devId;
        this.thirdPartyId = thirdPartyId;
    }

    // 应用ID
    private String devId;

    // 第三方扩展标识
    private String thirdPartyId;

    /**
     * 应用ID
     */
    public String getDevId() {
        return devId;
    }

    /**
     * 第三方扩展标识
     * 
     * @return
     */
    public String getThirdPartyId() {
        return this.thirdPartyId;
    }

}
