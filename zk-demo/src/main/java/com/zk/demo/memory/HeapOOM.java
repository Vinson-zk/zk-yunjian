/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: HeapOOM.java 
* @author Vinson 
* @Package com.zk.demo.memory 
* @Description: TODO(simple description this file what to do.) 
* @date Jul 29, 2020 1:49:27 PM 
* @version V1.0 
*/
package com.zk.demo.memory;

import java.util.ArrayList;
import java.util.List;

/** 
* @ClassName: HeapOOM 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class HeapOOM {

    /**
     * Java 堆内存溢出异常测试
     * 
     * vm Args:-Xms20m -Xmx20M -XX:-HeapDumpOnOutOfMemoryError
     */
    static class OOMObject {

    }

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<OOMObject>();
        while (true) {
            list.add(new OOMObject());
        }
    }
}

