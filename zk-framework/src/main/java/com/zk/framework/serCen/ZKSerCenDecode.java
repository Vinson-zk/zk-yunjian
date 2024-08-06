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
* @Title: ZKSerCenDecode.java 
* @author Vinson 
* @Package com.zk.framework.serCen 
* @Description: TODO(simple description this file what to do.) 
* @date Jun 25, 2020 10:08:26 PM 
* @version V1.0 
*/
package com.zk.framework.serCen;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 服务端使用，解密
 * 
 * @ClassName: ZKSerCenDecode
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public interface ZKSerCenDecode extends ZKSerCenCipher {
    
//    /**
//     * 解密
//     *
//     * @Title: decode
//     * @Description: TODO(simple description this method what to do.)
//     * @author Vinson
//     * @date Jun 4, 2020 3:12:38 PM
//     * @param encryptStr
//     * @return Map<String,Object>
//     */
//    Map<String, Object> decode(String encryptStr);
//
//    // 从 request 中取官方
//    String getEncryptStr(HttpServletRequest hReq);

    // 断言：解密后判断相关信息是否正常
    boolean assertServerClient(HttpServletRequest hReq);

}
