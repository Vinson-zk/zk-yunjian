/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKEncResponseWrapper.java 
* @author Vinson 
* @Package com.zk.core.web.wrapper 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 16, 2024 4:25:23 PM 
* @version V1.0 
*/
package com.zk.core.web.wrapper;

import java.io.IOException;

import com.zk.core.web.common.ZKWebOutputStream;

/** 
* @ClassName: ZKEncResponseWrapper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKEncResponseWrapper extends ZKResponseWrapper {

    ZKWebOutputStream getZKOutputStream() throws IOException;

    byte[] getData() throws IOException;

}
