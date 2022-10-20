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
* @Title: ZKSecAbstractAuthenticationToken.java 
* @author Vinson 
* @Package com.zk.security.token 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 29, 2021 8:16:53 AM 
* @version V1.0 
*/
package com.zk.security.token;
/** 
* @ClassName: ZKSecAbstractAuthenticationToken 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecAbstractAuthenticationToken implements ZKSecAuthenticationToken {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    // 公司代码
    String companyCode;

    /**
     * 终端设备类型
     * 
     */
    long osType;

    /**
     * 设备UDID
     * 
     */
    String udid;

    /**
     * 应用类型
     */
    long appType;

    /**
     * 应用标识
     */
    String appId;

    public ZKSecAbstractAuthenticationToken() {

    }

    public ZKSecAbstractAuthenticationToken(String companyCode, long osType, String udid, long appType, String appId) {
        this.companyCode = companyCode;
        this.osType = osType;
        this.udid = udid;
        this.appType = appType;
        this.appId = appId;
    }

    /**
     * @return groupCode sa
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * @return osType sa
     */
    public long getOsType() {
        return osType;
    }

    /**
     * @return udid sa
     */
    public String getUdid() {
        return udid;
    }

    /**
     * @return appType sa
     */
    public long getAppType() {
        return appType;
    }

    /**
     * @return appId sa
     */
    public String getAppId() {
        return appId;
    }

}
