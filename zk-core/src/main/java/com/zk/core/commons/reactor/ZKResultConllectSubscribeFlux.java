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
* @Title: ZKResultConllectSubscribeFlux.java 
* @author Vinson 
* @Package com.zk.core.commons.reactor 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 25, 2024 1:16:12 AM 
* @version V1.0 
*/
package com.zk.core.commons.reactor;

import java.util.List;

import reactor.core.publisher.FluxSink;

/**
 * @ClassName: ZKResultConllectSubscribeFlux
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKResultConllectSubscribeFlux<T> implements ZKResultConllectSubscribe<T> {

    FluxSink<List<T>> fluxSink;

    public ZKResultConllectSubscribeFlux(FluxSink<List<T>> fluxSink) {
        this.fluxSink = fluxSink;
    }

    @Override
    public void doSubscribe(List<T> result) {
        this.fluxSink.next(result);
    }

}
