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
* @Title: ZKSerCenCerCipherManager.java 
* @author Vinson 
* @Package com.zk.server.central.commons 
* @Description: TODO(simple description this file what to do.) 
* @date Mar 12, 2020 8:57:14 PM 
* @version V1.0 
*/
package com.zk.server.central.commons;

import java.security.NoSuchAlgorithmException;

import com.zk.core.encrypt.ZKRSAKey;

/** 
* @ClassName: ZKSerCenCerCipherManager 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSerCenCerCipherManager {

    /**
     * 生成证书
     * 
     *
     * @Title: genCer
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 11, 2020 11:14:08 PM
     * @return
     * @throws NoSuchAlgorithmException
     * @return ZKRSAKey
     */
    public ZKRSAKey genCer() throws NoSuchAlgorithmException;


}
