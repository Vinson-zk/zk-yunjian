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
* @Title: ZKResultConllectSubscribeMono.java 
* @author Vinson 
* @Package com.zk.core.commons.reactor 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 25, 2024 1:00:34 AM 
* @version V1.0 
*/
package com.zk.core.commons.reactor;

import java.util.List;

import reactor.core.publisher.MonoSink;

/** 
* @ClassName: ZKResultConllectSubscribeMono 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKResultConllectSubscribeMono<T> implements ZKResultConllectSubscribe<T> {

    MonoSink<List<T>> monoSink;

    public ZKResultConllectSubscribeMono(MonoSink<List<T>> monoSink) {
        this.monoSink = monoSink;
    }

    @Override
    public void doSubscribe(List<T> result) {
        this.monoSink.success(result);
    }

}
