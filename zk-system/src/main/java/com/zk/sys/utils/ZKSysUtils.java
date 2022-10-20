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
* @Title: ZKSysUtils.java 
* @author Vinson 
* @Package com.zk.sys.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 21, 2022 10:10:28 AM 
* @version V1.0 
*/
package com.zk.sys.utils;

import com.zk.core.utils.ZKEnvironmentUtils;

/** 
* @ClassName: ZKSysUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSysUtils {

    /**
     * 取拥有者公司代码
     *
     * @Title: getOwnerCompanyCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 21, 2022 10:11:14 AM
     * @return
     * @return String
     */
    public static String getOwnerCompanyCode() {
        return ZKEnvironmentUtils.getString("zk.sys.owner.company.code");
    }

}
