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
* @Title: ZKStationDataNode.java 
* @author Vinson 
* @Package com.zk.demo.reactive.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 20, 2024 4:13:31 PM 
* @version V1.0 
*/
package com.zk.demo.reactor.common;

/** 
* @ClassName: ZKStationDataNode 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKStationDataNode<T> {

    void doOffer(ZKStationProcessor<T> processor);

}
