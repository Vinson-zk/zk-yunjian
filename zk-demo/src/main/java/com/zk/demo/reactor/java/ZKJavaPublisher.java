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
* @Title: ZKJavaPublisher.java 
* @author Vinson 
* @Package com.zk.demo.reactor.java 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 21, 2024 10:43:27 PM 
* @version V1.0 
*/
package com.zk.demo.reactor.java;

import java.util.concurrent.SubmissionPublisher;

/** 
* @ClassName: ZKJavaPublisher 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKJavaPublisher extends SubmissionPublisher<String> {

    @Override
    public int submit(String item) {
        System.out.println("[^_^:20240321-2244-001] ZKJavaPublisher.submit!");
        return super.submit(item);
    }
}
