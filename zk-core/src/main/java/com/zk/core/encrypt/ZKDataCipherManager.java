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
 * @Title: ZKDataCipherManager.java 
 * @author Vinson 
 * @Package com.zk.core.encrypt 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:36:00 PM 
 * @version V1.0   
*/
package com.zk.core.encrypt;

/**
 * 数据存储加解密管理
 * 
 * @ClassName: ZKDataCipherManager
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public interface ZKDataCipherManager {

    // 判断是否需要数据存储加密
    boolean isEncryption();

    // 判断是否需要数据存储解密
    boolean isDecode();

}
