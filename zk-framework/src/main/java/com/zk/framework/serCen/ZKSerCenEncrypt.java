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
* @Title: ZKSerCenEncrypt.java 
* @author Vinson 
* @Package com.zk.framework.serCen 
* @Description: TODO(simple description this file what to do.) 
* @date Jun 25, 2020 10:07:33 PM 
* @version V1.0 
*/
package com.zk.framework.serCen;

import java.util.Map;

/**
 * 服务-客户端使用，加密
 * 
 * @ClassName: ZKSerCenEncrypt
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public interface ZKSerCenEncrypt extends ZKSerCenCipher {

    /**
     * 加密；加密哪些类型由不实现决定；
     *
     * @Title: encrypt
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 4, 2020 11:22:46 AM
     * @return Map<String, String>
     */
    Map<String, String> encrypt();

}
