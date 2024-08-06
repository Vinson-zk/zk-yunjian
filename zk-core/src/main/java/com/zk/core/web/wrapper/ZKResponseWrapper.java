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
* @Title: ZKResponseWrapper.java 
* @author Vinson 
* @Package com.zk.core.web.wrapper 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 16, 2024 9:13:33 AM 
* @version V1.0 
*/
package com.zk.core.web.wrapper;

/**
 * @ClassName: ZKResponseWrapper
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public interface ZKResponseWrapper {

    void addHeader(String name, String value);

    void setHeader(String name, String value);

    <T> T getWrapperResponse();

    boolean containsHeader(String name);

}
