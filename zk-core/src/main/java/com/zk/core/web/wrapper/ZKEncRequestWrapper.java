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
* @Title: ZKEncRequestWrapper.java 
* @author Vinson 
* @Package com.zk.core.web.wrapper 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 16, 2024 4:24:51 PM 
* @version V1.0 
*/
package com.zk.core.web.wrapper;

import java.io.IOException;
import java.util.Map;

import com.zk.core.web.common.ZKWebInputStream;

/** 
* @ClassName: ZKEncRequestWrapper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKEncRequestWrapper extends ZKRequestWrapper {

    public Map<String, Object> getEncInfo();

    ZKWebInputStream getZKInputStream() throws IOException;

    void afterDecryptSet(Map<String, String[]> zkParameterMap, byte[] reqBody);
}
