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
* @Title: ZKSerCenCipher.java 
* @author Vinson 
* @Package com.zk.framework.serCen 
* @Description: TODO(simple description this file what to do.) 
* @date Mar 12, 2020 9:01:59 PM 
* @version V1.0 
*/
package com.zk.framework.serCen;

/** 
* @ClassName: ZKSerCenCipher 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSerCenCipher {
    
    // 密文上送标识
    String getSignature();

}
