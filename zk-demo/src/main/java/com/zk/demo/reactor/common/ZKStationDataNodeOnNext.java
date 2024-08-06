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
* @Title: ZKStationDataNodeOnNext.java 
* @author Vinson 
* @Package com.zk.demo.reactive.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 20, 2024 4:18:18 PM 
* @version V1.0 
*/
package com.zk.demo.reactor.common;

/** 
* @ClassName: ZKStationDataNodeOnNext 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKStationDataNodeOnNext<T> implements ZKStationDataNode<T> {
    
    T data;
    
    public ZKStationDataNodeOnNext(T data) {
        this.data = data;
    }

    @Override
    public void doOffer(ZKStationProcessor<T> processor) {
        processor.submit(data);
    }

}
