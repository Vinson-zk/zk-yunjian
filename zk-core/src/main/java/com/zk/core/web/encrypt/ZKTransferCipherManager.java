/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKTransferCipherManager.java 
 * @author Vinson 
 * @Package com.zk.core.encrypt 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:36:49 PM 
 * @version V1.0   
*/
package com.zk.core.web.encrypt;

import com.zk.core.web.wrapper.ZKEncRequestWrapper;
import com.zk.core.web.wrapper.ZKEncResponseWrapper;

/** 
* @ClassName: ZKTransferCipherManager 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKTransferCipherManager {

    /**
     * 解密请求数据
     *
     * @Title: decryptPatameterMap
     * @Description: 解密请求数据
     * @author Vinson
     * @date Jun 27, 2019 9:30:04 AM
     * @param request
     * @return
     * @return Map<String,String[]>
     */
    void decrypt(ZKEncRequestWrapper request);

    /**
     * 加密响应数据
     *
     * @Title: encrypt
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jun 27, 2019 11:00:29 AM
     * @param response
     * @param response
     * @return void
     */
    void encrypt(ZKEncRequestWrapper request, ZKEncResponseWrapper response);

    /**
     * 判断是否需要加解密
     *
     * @Title: isEnc
     * @Description: 判断是否需要加解密
     * @author Vinson
     * @date Jun 27, 2019 9:30:15 AM
     * @param request
     * @return
     * @return boolean
     */
    boolean isEnc(ZKEncRequestWrapper request);

}
