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
* @Title: ZKResultConllectSubscribeMonoFormat.java 
* @author Vinson 
* @Package com.zk.core.commons.reactor 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 25, 2024 1:11:12 AM 
* @version V1.0 
*/
package com.zk.core.commons.reactor;
/** 
* @ClassName: ZKResultConllectSubscribeMonoFormat 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/

import java.util.List;

import reactor.core.publisher.MonoSink;

public class ZKResultConllectSubscribeMonoFormat<R, T> implements ZKResultConllectSubscribe<T> {

    MonoSink<R> monoSink;

    ZKResultFormat<R, T> format;

    public ZKResultConllectSubscribeMonoFormat(MonoSink<R> monoSink, ZKResultFormat<R, T> format) {
        this.monoSink = monoSink;
        this.format = format;
    }

    @Override
    public void doSubscribe(List<T> result) {
        this.monoSink.success(this.format.format(result));
    }

}
