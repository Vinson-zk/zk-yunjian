/** 
* Copyright (c) 2012-2025 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKGetIpDesc.java 
* @author Vinson 
* @Package com.zk.core.web.net 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 8, 2025 6:05:06 PM 
* @version V1.0 
*/
package com.zk.core.web.net;

import java.util.Locale;

/** 
* @ClassName: ZKGetIpDesc 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKGetIpDesc {

    public ZKIpDesc getIpDesc(String ip, Locale locale);

}
