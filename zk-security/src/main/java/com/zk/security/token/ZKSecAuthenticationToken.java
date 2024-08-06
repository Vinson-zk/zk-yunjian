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
* @Title: ZKSecAuthenticationToken.java 
* @author Vinson 
* @Package com.zk.security.token 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 12:57:46 PM 
* @version V1.0 
*/
package com.zk.security.token;

import java.io.Serializable;

/** 
* @ClassName: ZKSecAuthenticationToken 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecAuthenticationToken extends Serializable {

    /**
     * 公司代码
     */
    public String getCompanyCode();

    /**
     * 终端设备类型
     * 
     * @return
     */
    public long getOsType();

    /**
     * 设备UDID
     * 
     * @return
     */
    public String getUdid();

    /**
     * 应用类型
     */
    public long getAppType();

    /**
     * 应用标识
     */
    public String getAppId();

}
