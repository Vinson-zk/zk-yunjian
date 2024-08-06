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
* @Title: ZKSecAuthorizer.java 
* @author Vinson 
* @Package com.zk.security.authz 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 8, 2021 6:25:36 PM 
* @version V1.0 
*/
package com.zk.security.authz;

import com.zk.security.annotation.ZKSecApiCode;

/** 
* @ClassName: ZKSecAuthorizer 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecAuthorizer {

    /**
     * 对 api 接口代码对待鉴权
     *
     * @Title: checkApiCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 8, 2021 6:26:33 PM
     * @param apiCode
     * @return boolean 有权限返回 true; 没有返回 false;
     */
    public boolean checkApiCode(ZKSecApiCode apiCode);

}
